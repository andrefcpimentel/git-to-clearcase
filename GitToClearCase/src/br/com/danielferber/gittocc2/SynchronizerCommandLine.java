/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigChain;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigPojo;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigProperties;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigChain;
import br.com.danielferber.gittocc2.config.git.GitConfigPojo;
import br.com.danielferber.gittocc2.config.git.GitConfigProperties;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConversionException;

/**
 *
 * @author Daniel Felix Ferber
 */
class SynchronizerCommandLine {

    final static OptionParser parser = new OptionParser();
    final static OptionSpec<File> propertyFileOpt = parser.accepts("p", "Properties file.").withRequiredArg().ofType(File.class);

    final static OptionSpec<File> gitExecOpt = parser.accepts("g", "Git executable file.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<File> gitRepositoryDirOpt = parser.accepts("r", "Git repository directory.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<Void> gitFastForwardLocalGitRepository = parser.accepts("forward", "Before synchronizing, fast forward logal git repository.");
    final static OptionSpec<Void> gitFetchRemoteGitRepository = parser.accepts("fetch", "Before synchronizing, fetch remote commits from default remote git repository.");
    final static OptionSpec<Void> gitResetLocalGitRepository = parser.accepts("reset", "Before synchronizing, reset (hard) local git repository.");
    final static OptionSpec<Void> gitCleanLocalGitRepository = parser.accepts("clean", "Before synchronizing, clean completely local git repository.");

    final static OptionSpec<File> clearToolExecOpt = parser.accepts("c", "CleartTool executable file.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<File> vobViewDirOpt = parser.accepts("v", "Snapshot vob view directory.").withRequiredArg().required().ofType(File.class);

//    String getActivityMessagePattern();
//    
//    File getClearToolExec();
//
//    File getCommitStampFile();
//
//    File getCounterStampFile();
//
//    Boolean getCreateActivity();
//
//    Long getOverriddenSyncCounter();
//
//    String getOverriddenSyncFromCommit();
//
//    Boolean getUpdateVobRoot();
//
//    File getVobViewDir();
    static void printHelp(PrintStream ps) throws IOException {
        parser.printHelpOn(ps);
    }

    final OptionSet options;
    final Properties properties;

    SynchronizerCommandLine(String[] argv) {
        options = parser.parse(argv);
        File propertyFile = propertyFileOpt.value(options);

        if (propertyFile != null) {
            try (InputStream is = new FileInputStream(propertyFile)) {
                properties = new Properties();
                properties.load(is);
                is.close();
            } catch (FileNotFoundException e) {
                throw new ValueConversionException("Properties file: failed to open.", e);
            } catch (IOException e) {
                throw new ValueConversionException("Properties file: failed to read.", e);
            }
        } else {
            properties = null;
        }
    }

    ClearToolConfigSource getClearToolConfig() {
        final ClearToolConfigPojo config = new ClearToolConfigPojo(clearToolExecOpt.value(options), vobViewDirOpt.value(options));
        if (properties == null) {
            return config;
        }
        return new ClearToolConfigChain(config, new ClearToolConfigProperties(properties));
    }

    GitConfigSource getGitConfig() {
        final GitConfigPojo config = new GitConfigPojo(gitExecOpt.value(options), gitRepositoryDirOpt.value(options));
        config.setFastForwardLocalGitRepository(options.has(gitFastForwardLocalGitRepository));
        config.setCleanLocalGitRepository(options.has(gitCleanLocalGitRepository));
        config.setResetLocalGitRepository(options.has(gitResetLocalGitRepository));
        config.setFastForwardLocalGitRepository(options.has(gitFastForwardLocalGitRepository));

        if (properties == null) {
            return config;
        }
        return new GitConfigChain(config, new GitConfigProperties(properties));
    }

}
