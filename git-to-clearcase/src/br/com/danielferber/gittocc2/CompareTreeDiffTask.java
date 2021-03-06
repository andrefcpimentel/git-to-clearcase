package br.com.danielferber.gittocc2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;

/**
 *
 * @author daniel
 */
class CompareTreeDiffTask extends MeterCallable<TreeDiff> {

    private final File gitRootDir;
    private final File clearCaseRootDir;
    private final File compareRoot;

    CompareTreeDiffTask(Meter outerMeter, final File gitRootDir, final File clearCaseRootDir, final File compareRoot) {
        super(outerMeter, "GitTreeDiffTask", "Calculate differences from file by file comparison.");
        this.gitRootDir = gitRootDir;
        this.clearCaseRootDir = clearCaseRootDir;
        this.compareRoot = compareRoot;
    }

    @Override
    protected TreeDiff meteredCall() throws Exception {
        Meter m2 = null;
        final Set<File> vobDirs = new TreeSet<>();
        final Set<File> vobFiles = new TreeSet<>();
        final Set<File> repositoryDirs = new TreeSet<>();
        final Set<File> repositoryFiles = new TreeSet<>();

        final TreeDiff treeDiff;

        final FileVisitor<Path> vobVisitor = new SimpleFileVisitor<Path>() {
            Path rootPath = clearCaseRootDir.toPath();

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                final Path path = rootPath.relativize(file);
                vobFiles.add(path.toFile());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                final Path path = rootPath.relativize(dir);
                vobDirs.add(path.toFile());
                return FileVisitResult.CONTINUE;
            }
        };

        final FileVisitor<Path> repositoryVisitor = new SimpleFileVisitor<Path>() {
            Path rootPath = gitRootDir.toPath();

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                final Path path = rootPath.relativize(file);
                repositoryFiles.add(path.toFile());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                final Path path = rootPath.relativize(dir);
                repositoryDirs.add(path.toFile());
                return FileVisitResult.CONTINUE;
            }
        };

        m2 = getMeter().sub("vobScan").m("Execute ClearCase VOB scan.").start();
        final File clearCaseCompareDir = new File(clearCaseRootDir, compareRoot.getPath());
        Files.walkFileTree(clearCaseCompareDir.toPath(), vobVisitor);
        m2.ok();

        m2 = getMeter().sub("gitScan").m("Execute GIT repository scan.").start();
        final File gitCompareDir = new File(gitRootDir, compareRoot.getPath());
        Files.walkFileTree(gitCompareDir.toPath(), repositoryVisitor);
        m2.ok();

        final Set<File> dirsAdded = new TreeSet<>(repositoryDirs);
        dirsAdded.removeAll(vobDirs);
        final Set<File> dirsDeleted = new TreeSet<>(vobDirs);
        dirsDeleted.removeAll(repositoryDirs);
        final Set<File> filesAdded = new TreeSet<>(repositoryFiles);
        filesAdded.removeAll(vobFiles);
        final Set<File> filesDeleted = new TreeSet<>(vobFiles);
        filesDeleted.removeAll(repositoryFiles);

        final Set<File> filesToCompare = new TreeSet<>(vobFiles);
        filesToCompare.retainAll(repositoryFiles);
        m2 = getMeter().sub("fileCompare").m("Compare file by file.").iterations(filesToCompare.size()).start();
        final Set<File> filesModified = new TreeSet<>();
        for (final File file : filesToCompare) {
            final File gitSourceFile = new File(gitRootDir, file.getPath());
            final File ccTargetFile = new File(clearCaseRootDir, file.getPath());
            if (gitSourceFile.length() != ccTargetFile.length()) {
                filesModified.add(file);
                continue;
            }
            try (
                    InputStream i1 = new FileInputStream(gitSourceFile);
                    InputStream i2 = new FileInputStream(ccTargetFile)) {
                if (!compare(i1, i2)) {
                    filesModified.add(file);
                }
            } catch (final IOException e) {
                m2.getLogger().error("Failed to compare file.", e);
            }
            m2.inc().progress();
        }
        m2.ok();
        final Set<File> filesMovedFrom = Collections.emptySet();
        final Set<File> filesMovedTo = Collections.emptySet();
        final Set<File> filesMovedModified = Collections.emptySet();
        final Set<File> filesCopiedFrom = Collections.emptySet();
        final Set<File> filesCopiedTo = Collections.emptySet();
        final Set<File> filesCopiedModified = Collections.emptySet();
        treeDiff = new TreeDiff(
                new ArrayList<>(dirsAdded),
                new ArrayList<>(dirsDeleted),
                new ArrayList<>(filesAdded),
                new ArrayList<>(filesDeleted),
                new ArrayList<>(filesModified),
                new ArrayList<>(filesMovedFrom),
                new ArrayList<>(filesMovedTo),
                new ArrayList<>(filesMovedModified),
                new ArrayList<>(filesCopiedFrom),
                new ArrayList<>(filesCopiedTo),
                new ArrayList<>(filesCopiedModified));

        return treeDiff;
    }

    private static boolean compare(final InputStream input1, final InputStream input2) throws IOException {
        boolean error = false;
        try {
            final byte[] buffer1 = new byte[1_024];
            final byte[] buffer2 = new byte[1_024];
            try {
                int numRead1 = 0;
                int numRead2 = 0;
                while (true) {
                    numRead1 = input1.read(buffer1);
                    numRead2 = input2.read(buffer2);
                    if (numRead1 > -1) {
                        if (numRead2 != numRead1) {
                            return false;
                        }
                        // Otherwise same number of bytes read
                        if (!Arrays.equals(buffer1, buffer2)) {
                            return false;
                        }
                        // Otherwise same bytes read, so continue ...
                    } else {
                        // Nothing more in stream 1 ...
                        return numRead2 < 0;
                    }
                }
            } finally {
                input1.close();
            }
        } catch (IOException | RuntimeException e) {
            error = true; // this error should be thrown, even if there is an error closing stream 2
            throw e;
        } finally {
            try {
                input2.close();
            } catch (final IOException e) {
                if (!error) {
                    throw e;
                }
            }
        }
    }
}
