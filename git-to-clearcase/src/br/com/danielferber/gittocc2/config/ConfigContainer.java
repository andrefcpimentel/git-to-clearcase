/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import br.com.danielferber.gittocc2.task.ClearCaseActivityConfig;
import br.com.danielferber.gittocc2.task.ClearCaseFinalizeConfig;
import br.com.danielferber.gittocc2.task.ClearCasePrepareConfig;
import br.com.danielferber.gittocc2.task.ClearCaseVobConfig;
import br.com.danielferber.gittocc2.task.ClearToolConfig;
import br.com.danielferber.gittocc2.task.GitConfig;
import br.com.danielferber.gittocc2.task.GitFinishConfig;
import br.com.danielferber.gittocc2.task.GitPrepareConfig;
import br.com.danielferber.gittocc2.task.GitRepositoryConfig;
import java.io.File;
import java.io.PrintStream;
import java.util.Properties;

public class ConfigContainer {

    public static final String PROP_CREATE_ACTIVITY = "activity.create";
    public static final String PROP_ACTIVITY_NAME_PATTERN = "activity.pattern";
    public static final String PROP_CLEAR_TOOL_EXEC = "cleartool.exec";
    public static final String PROP_VOB_VIEW_DIR = "vobview.dir";
    public static final String PROP_COMMIT_STAMP_FILE = "vobview.commit.file";
    public static final String PROP_COUNTER_STAMP_FILE = "vobview.counter.file";
    public static final String PROP_UPDATE_COMMIT_STAMP_FILE = "vobview.commit.update";
    public static final String PROP_UPDATE_COUNTER_STAMP_FILE = "vobview.counter.update";
    public static final String PROP_UPDATE_VOB_VIEW = "vobview.update";
    public static final String PROP_VALIDATE_EXISTING_CHECKOUT = "vobview.validate.checkout";

    public static final String PROP_CLEAN_LOCAL_GIT_REPOSITORY = "repository.clean";
    public static final String PROP_FAST_FORWARD_LOCAL_GIT_REPOSITORY = "repository.fastForward";
    public static final String PROP_FETCH_REMOTE_GIT_REPOSITORY = "repository.fetchRemote";
    public static final String PROP_RESET_LOCAL_GIT_REPOSITORY = "repository.reset";
    public static final String PROP_APPLY_DEFAULT_GIT_CONFIG = "repository.applyDefault";
    public static final String PROP_REPOSITORY_DIR = "repository.dir";
    public static final String PROP_GIT_EXEC = "git.exec";

    protected final ConfigProperties properties;

    private final GitRepositoryBean gitRepositoryBean = new GitRepositoryBean();
    private final GitPrepareBean gitPrepareBean = new GitPrepareBean();
    private final GitFinishBean gitFinishBean = new GitFinishBean();
    private final GitBean gitBean = new GitBean();
    private final ClearToolBean clearToolBean = new ClearToolBean();
    private final ClearCaseVobBean clearCaseVobBean = new ClearCaseVobBean();
    private final ClearCasePrepareBean clearCasePrepareBean = new ClearCasePrepareBean();
    private final ClearCaseFinalizeBean clearCaseFinalizeBean = new ClearCaseFinalizeBean();
    private final ClearCaseActivityBean clearCaseActivityBean = new ClearCaseActivityBean();

    public ConfigContainer(final Properties properties) {
        this.properties = new ConfigProperties(properties);
    }

    public GitRepositoryBean getGitRepositoryBean() {
        return gitRepositoryBean;
    }

    public GitPrepareBean getGitPrepareBean() {
        return gitPrepareBean;
    }

    public GitFinishBean getGitFinishBean() {
        return gitFinishBean;
    }

    public GitBean getGitBean() {
        return gitBean;
    }

    public ClearToolBean getClearToolBean() {
        return clearToolBean;
    }

    public ClearCaseVobBean getClearCaseVobBean() {
        return clearCaseVobBean;
    }

    public ClearCasePrepareBean getClearCasePrepareBean() {
        return clearCasePrepareBean;
    }

    public ClearCaseFinalizeBean getClearCaseFinalizeBean() {
        return clearCaseFinalizeBean;
    }

    public ClearCaseActivityBean getClearCaseActivityBean() {
        return clearCaseActivityBean;
    }

    public void validateAll() throws ConfigException {
        GitRepositoryConfig.validate(gitRepositoryBean);
        GitPrepareConfig.validate(gitPrepareBean);
        GitFinishConfig.validate(gitFinishBean);
        GitConfig.validate(gitBean);
        ClearToolConfig.validate(clearToolBean);
        ClearCaseVobConfig.validate(clearCaseVobBean);
        ClearCasePrepareConfig.validate(clearCasePrepareBean);
        ClearCaseFinalizeConfig.validate(clearCaseFinalizeBean);
        ClearCaseActivityConfig.validate(clearCaseActivityBean);
    }

    public void printConfig(PrintStream ps) {
        GitConfig.printConfig(ps, gitBean);
        GitRepositoryConfig.printConfig(ps, gitRepositoryBean);
        GitPrepareConfig.printConfig(ps, gitPrepareBean);
        GitFinishConfig.printConfig(ps, gitFinishBean);
        ClearToolConfig.printConfig(ps, clearToolBean);
        ClearCaseActivityConfig.printConfig(ps, clearCaseActivityBean);
        ClearCaseVobConfig.printConfig(ps, clearCaseVobBean);
        ClearCasePrepareConfig.printConfig(ps, clearCasePrepareBean);
        ClearCaseFinalizeConfig.printConfig(ps, clearCaseFinalizeBean);
    }

    public class ClearCaseActivityBean implements ClearCaseActivityConfig {

        @Override
        public String getActivityNamePattern() {
            return properties.getString(PROP_ACTIVITY_NAME_PATTERN);
        }

        public ClearCaseActivityBean setActivityNamePattern(final String value) {
            properties.setString(PROP_ACTIVITY_NAME_PATTERN, value);
            return this;
        }

        @Override
        public Boolean getCreateActivity() {
            return properties.getBoolean(PROP_CREATE_ACTIVITY);
        }

        public ClearCaseActivityBean setCreateActivity(final Boolean value) {
            properties.setBoolean(PROP_CREATE_ACTIVITY, value);
            return this;
        }
    };

    public class ClearToolBean implements ClearToolConfig {

        @Override
        public File getClearToolExec() {
            return properties.getFile(PROP_CLEAR_TOOL_EXEC);
        }

        public ClearToolBean setClearToolExec(final File file) {
            properties.setFile(PROP_CLEAR_TOOL_EXEC, file);
            return this;
        }

        @Override
        public File getClearToolAbsoluteExec() {
            return getClearToolExec().getAbsoluteFile();
        }
    }

    public class ClearCaseVobBean implements ClearCaseVobConfig {

        @Override
        public Boolean getUpdateCounterStampFile() {
            return properties.getBoolean(PROP_UPDATE_COUNTER_STAMP_FILE);

        }

        public ClearCaseVobBean setUpdateCounterStampFile(final Boolean value) {
            properties.setBoolean(PROP_UPDATE_COUNTER_STAMP_FILE, value);
            return this;
        }

        @Override
        public Boolean getUpdateCommitStampFile() {
            return properties.getBoolean(PROP_UPDATE_COMMIT_STAMP_FILE);

        }

        public ClearCaseVobBean setUpdateCommitStampFile(final Boolean value) {
            properties.setBoolean(PROP_UPDATE_COMMIT_STAMP_FILE, value);
            return this;
        }

        @Override
        public File getCounterStampFile() {
            return properties.getFile(PROP_COUNTER_STAMP_FILE);
        }

        public ClearCaseVobBean setCounterStampFile(final File file) {
            properties.setFile(PROP_COUNTER_STAMP_FILE, file);
            return this;
        }

        @Override
        public File getCommitStampFile() {
            return properties.getFile(PROP_COMMIT_STAMP_FILE);
        }

        public ClearCaseVobBean setCommitStampFile(final File file) {
            properties.setFile(PROP_COMMIT_STAMP_FILE, file);
            return this;
        }

        @Override
        public File getVobViewDir() {
            return properties.getFile(PROP_VOB_VIEW_DIR);
        }

        public ClearCaseVobBean setVobViewDir(final File dir) {
            properties.setFile(PROP_VOB_VIEW_DIR, dir);
            return this;
        }

        @Override
        public File getCommitStampAbsoluteFile() {
            return new File(getVobViewDir(), getCommitStampFile().getPath());
        }

        @Override
        public File getCounterStampAbsoluteFile() {
            return new File(getVobViewDir(), getCounterStampFile().getPath());
        }

        @Override
        public File getVobViewAbsoluteDir() {
            return getVobViewDir().getAbsoluteFile();
        }
    }

    public class ClearCasePrepareBean implements ClearCasePrepareConfig {

        @Override
        public Boolean getUpdateVobViewDir() {
            return properties.getBoolean(PROP_UPDATE_VOB_VIEW);
        }

        public ClearCasePrepareBean setUpdateVobViewDir(final Boolean value) {
            properties.setBoolean(PROP_UPDATE_VOB_VIEW, value);
            return this;
        }
    }

    public class ClearCaseFinalizeBean implements ClearCaseFinalizeConfig {

        @Override
        public Boolean getValidateExistingCheckout() {
            return properties.getBoolean(PROP_VALIDATE_EXISTING_CHECKOUT);
        }

        public ClearCaseFinalizeBean setValidateExistingCheckout(Boolean value) {
            properties.setBoolean(PROP_VALIDATE_EXISTING_CHECKOUT, value);
            return this;
        }
    }

    public class GitBean implements GitConfig {

        @Override
        public File getGitExec() {
            return properties.getFile(PROP_GIT_EXEC);
        }

        public GitBean setGitExec(final File file) {
            properties.setFile(PROP_GIT_EXEC, file);
            return this;
        }
    }

    public class GitFinishBean implements GitFinishConfig {

    }

    public class GitPrepareBean implements GitPrepareConfig {

        @Override
        public Boolean getCleanLocalGitRepository() {
            return properties.getBoolean(PROP_CLEAN_LOCAL_GIT_REPOSITORY);
        }

        public GitPrepareBean setCleanLocalGitRepository(final Boolean value) {
            properties.setBoolean(PROP_CLEAN_LOCAL_GIT_REPOSITORY, value);
            return this;
        }

        @Override
        public Boolean getFastForwardLocalGitRepository() {
            return properties.getBoolean(PROP_FAST_FORWARD_LOCAL_GIT_REPOSITORY);
        }

        public GitPrepareBean setFastForwardLocalGitRepository(final Boolean value) {
            properties.setBoolean(PROP_FAST_FORWARD_LOCAL_GIT_REPOSITORY, value);
            return this;
        }

        @Override
        public Boolean getFetchRemoteGitRepository() {
            return properties.getBoolean(PROP_FETCH_REMOTE_GIT_REPOSITORY);
        }

        public GitPrepareBean setFetchRemoteGitRepository(final Boolean value) {
            properties.setBoolean(PROP_FETCH_REMOTE_GIT_REPOSITORY, value);
            return this;
        }

        @Override
        public Boolean getResetLocalGitRepository() {
            return properties.getBoolean(PROP_RESET_LOCAL_GIT_REPOSITORY);
        }

        public GitPrepareBean setResetLocalGitRepository(final Boolean value) {
            properties.setBoolean(PROP_RESET_LOCAL_GIT_REPOSITORY, value);
            return this;
        }

        @Override
        public Boolean getApplyDefaultGitConfig() {
            return properties.getBoolean(PROP_APPLY_DEFAULT_GIT_CONFIG);
        }

        public GitPrepareBean setApplyDefaultGitConfig(final Boolean value) {
            properties.setBoolean(PROP_APPLY_DEFAULT_GIT_CONFIG, value);
            return this;
        }
    }

    public class GitRepositoryBean implements GitRepositoryConfig {

        @Override
        public File getRepositoryDir() {
            return properties.getFile(PROP_REPOSITORY_DIR);
        }

        public GitRepositoryBean setRepositoryDir(final File dir) {
            properties.setFile(PROP_REPOSITORY_DIR, dir);
            return this;
        }
    }
}