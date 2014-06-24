package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.io.File;
import java.util.concurrent.Callable;

/**
 * Updates the ClearCase VOB view directory. The ClearTool configuration require
 * a full recursive update. Otherwise, only control stamp files are updated.
 *
 * @author Daniel Felix Ferber
 */
public class UpdateVobDirectoryTask implements Callable<Void> {

    private final ClearToolConfigSource cleartoolConfig;
    private final ClearToolCommander ctCommander;
    private final Meter meter;

    public UpdateVobDirectoryTask(ClearToolConfigSource environmentConfig, ClearToolCommander ctCommander, Meter outerMeter) {
        this.cleartoolConfig = environmentConfig;
        this.ctCommander = ctCommander;
        this.meter = outerMeter.sub("UpdateVob");
    }

    @Override
    public Void call() throws Exception {
        meter.start();
        try {
            if (cleartoolConfig.getUpdateVobRoot()) {
                updateFullVob();
            } else {
                updateCommitStampFile();
                updateCounterStampFile();
            }
            meter.ok();
        } catch (Exception e) {
            meter.fail(e);
            throw e;
        }
        return null;
    }

    private void updateCommitStampFile() throws SyncTaskException {
        File commitStampFile = cleartoolConfig.getCommitStampAbsoluteFile();
        Meter m = meter.sub("commitFile").m("Update sync commit control file.").ctx("file", commitStampFile).start();
        ctCommander.updateFiles(commitStampFile);
        m.ok();
    }

    private void updateCounterStampFile() throws SyncTaskException {
        File counterStampFile = cleartoolConfig.getCounterStampAbsoluteFile();
        Meter m = meter.sub("counterFile").m("Update sync counter control file.").ctx("file", counterStampFile).start();
        ctCommander.updateFiles(counterStampFile);
        m.ok();
    }

    private void updateFullVob() {
        Meter m = meter.sub("vobDir").m("Update entire VOB directory.").ctx("dir", cleartoolConfig.getVobViewDir()).start();
        ctCommander.updateVobViewDir();
        m.ok();
    }
}