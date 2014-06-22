package br.com.danielferber.gittocc2.config.sync;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
public class SyncConfigBean implements SyncConfig {

    private final SyncConfig wrapped;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String ACTIVITY_MESSAGE_PATTERN_PROPERTY = "cc.activityMessagePattern";
    public static final String CREATE_ACTIVITY_PROPERTY = "git.createActivity";
    public static final String CLEAN_LOCAL_GIT_REPOSITORY_PROPERTY = "git.cleanLocalGitRepository";
    public static final String RESET_LOCAL_GIT_REPOSITORY_PROPERTY = "git.resetLocalGitRepository";
    public static final String FAST_FORWARD_LOCAL_GIT_REPOSITORY_PROPERTY = "git.fastForwardLocalGitRepository";
    public static final String FETCH_REMOTE_GIT_REPOSITORY_PROPERTY = "git.fetchRemoteGitRepository";
    public static final String UPDATE_VOB_ROOT_PROPERTY = "cc.updateVobRoot";
    public static final String COMMIT_STAMP_FILE_PROPERTY = "cc.commitStampFile";
    public static final String COUNTER_STAMP_FILE_PROPERTY = "cc.counterStampFile";
    public static final String OVERRIDDEN_SYNC_COUNTER = "cc.overriddenSyncCounter";
    public static final String OVERRIDDEN_SYNC_FROM_COMMIT = "cc.overridenSyncFromCommit";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public SyncConfigBean() {
        super();
        this.wrapped = new SyncConfigPojo();
    }

    public SyncConfigBean(SyncConfig other) {
        this.wrapped = other;
    }

    @Override
    public SyncConfig setUpdateVobRoot(Boolean value) {
        final Boolean oldValue = wrapped.getUpdateVobRoot();
        wrapped.setUpdateVobRoot(value);
        this.pcs.firePropertyChange(UPDATE_VOB_ROOT_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public SyncConfig setFetchRemoteGitRepository(Boolean value) {
        final Boolean oldValue = wrapped.getFetchRemoteGitRepository();
        wrapped.setFetchRemoteGitRepository(value);
        this.pcs.firePropertyChange(FETCH_REMOTE_GIT_REPOSITORY_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public SyncConfig setFastForwardLocalGitRepository(Boolean value) {
        final Boolean oldValue = wrapped.getFastForwardLocalGitRepository();
        wrapped.setFastForwardLocalGitRepository(value);
        this.pcs.firePropertyChange(FAST_FORWARD_LOCAL_GIT_REPOSITORY_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public SyncConfig setResetLocalGitRepository(Boolean value) {
        final Boolean oldValue = wrapped.getResetLocalGitRepository();
        wrapped.setResetLocalGitRepository(value);
        this.pcs.firePropertyChange(RESET_LOCAL_GIT_REPOSITORY_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public SyncConfig setCleanLocalGitRepository(Boolean value) {
        final Boolean oldValue = wrapped.getCleanLocalGitRepository();
        wrapped.setCleanLocalGitRepository(value);
        this.pcs.firePropertyChange(CLEAN_LOCAL_GIT_REPOSITORY_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public SyncConfig setCreateActivity(Boolean value) {
        final Boolean oldValue = wrapped.getCreateActivity();
        wrapped.setCreateActivity(value);
        this.pcs.firePropertyChange(CREATE_ACTIVITY_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public SyncConfig setActivityMessagePattern(String value) {
        final String oldValue = wrapped.getActivityMessagePattern();
        wrapped.setActivityMessagePattern(value);
        this.pcs.firePropertyChange(ACTIVITY_MESSAGE_PATTERN_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public SyncConfig setCommitStampFile(File file) {
        final File oldValue = wrapped.getCommitStampFile();
        wrapped.setCommitStampFile(file);
        this.pcs.firePropertyChange(COMMIT_STAMP_FILE_PROPERTY, oldValue, file);
        return this;
    }

    @Override
    public SyncConfig setCounterStampFile(File file) {
        final File oldValue = wrapped.getCounterStampFile();
        wrapped.setCounterStampFile(file);
        this.pcs.firePropertyChange(COUNTER_STAMP_FILE_PROPERTY, oldValue, file);
        return this;
    }

    @Override
    public SyncConfig setOverriddenSyncCounter(Long value) {
        Long oldValue = wrapped.getOverriddenSyncCounter();
        wrapped.setOverriddenSyncCounter(value);
        this.pcs.firePropertyChange(OVERRIDDEN_SYNC_COUNTER, oldValue, value);
        return this;
    }

    @Override
    public SyncConfig setOverriddenSyncFromCommit(String value) {
        String oldValue = wrapped.getOverriddenSyncFromCommit();
        wrapped.setOverriddenSyncFromCommit(value);
        this.pcs.firePropertyChange(OVERRIDDEN_SYNC_FROM_COMMIT, oldValue, value);
        return this;
    }

    @Override
    public Boolean getUpdateVobRoot() {
        return wrapped.getUpdateVobRoot();
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        return wrapped.getFetchRemoteGitRepository();
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        return wrapped.getFastForwardLocalGitRepository();
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        return wrapped.getResetLocalGitRepository();
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        return wrapped.getCleanLocalGitRepository();
    }

    @Override
    public Boolean getCreateActivity() {
        return wrapped.getCreateActivity();
    }

    @Override
    public String getActivityMessagePattern() {
        return wrapped.getActivityMessagePattern();
    }

    @Override
    public File getCommitStampFile() {
        return wrapped.getCommitStampFile();
    }

    @Override
    public File getCounterStampFile() {
        return wrapped.getCounterStampFile();
    }

    @Override
    public Long getOverriddenSyncCounter() {
        return wrapped.getOverriddenSyncCounter();
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        return wrapped.getOverriddenSyncFromCommit();
    }
}