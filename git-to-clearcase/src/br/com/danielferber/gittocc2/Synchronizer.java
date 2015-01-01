/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.ClearCaseActivityConfig;
import br.com.danielferber.gittocc2.config.ClearCaseFinalizeConfig;
import br.com.danielferber.gittocc2.config.ClearCasePrepareConfig;
import br.com.danielferber.gittocc2.config.ClearCaseVobConfig;
import br.com.danielferber.gittocc2.config.ClearToolConfig;
import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.config.GitConfig;
import br.com.danielferber.gittocc2.config.GitFinishConfig;
import br.com.danielferber.gittocc2.config.GitPrepareConfig;
import br.com.danielferber.gittocc2.config.GitRepositoryConfig;
import br.com.danielferber.gittocc2.config.SynchronizationConfig;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigPojo;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigValidated;
import br.com.danielferber.gittocc2.config.git.GitConfigPojo;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigValidated;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import joptsimple.OptionException;
import joptsimple.ValueConversionException;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Synchronizer {

    private static final Logger logger = LoggerFactory.getLogger("GitToClearCase");

    public static void main(final String[] argv) {
        final ClearToolConfigSource nonValidatedClearToolConfig;
        final GitConfigSource nonValidateGitConfig;
        final boolean compareOnly;
        final File compareRoot;
       
        try {
            GitConfigPojo gitConfigDefault = createDefaultGitConfig();
            ClearToolConfigPojo clearToolConfigDefault = createDefaultClearToolConfig();

            final CommandLine cl = new CommandLine(argv, gitConfigDefault, clearToolConfigDefault);
            nonValidatedClearToolConfig = cl.getClearToolConfig();
            nonValidateGitConfig = cl.getGitConfig();
            compareOnly = cl.isCompareOnly();
            compareRoot = cl.getCompareRoot();
        } catch (final ValueConversionException | OptionException e) {
            logger.error("Incorrect command line arguments: {} ", e.getMessage());
            return;
        }
        
        if (nonValidateGitConfig == null || nonValidatedClearToolConfig == null) {
            try (PrintStream ps = LoggerFactory.getInfoPrintStream(logger)) {
                CommandLine.printHelp(ps);
            } catch (final IOException ex) {
                logger.error("Failed to print command line help.", ex);
            }
            return;
        }
        
        if (logger.isInfoEnabled()) {
            try (PrintStream ps = LoggerFactory.getInfoPrintStream(logger)) {
                ps.println("Infer changes from: " + (compareOnly ? "file by file comparison" : "GIT history"));
                if (compareOnly) {
                    ps.println(" - Compare root: " + compareRoot);
                }

                ClearToolConfig.printConfig(ps, (ClearToolConfig) nonValidatedClearToolConfig);
                ClearCaseVobConfig.printConfig(ps, (ClearCaseVobConfig) nonValidatedClearToolConfig);
                ClearCasePrepareConfig.printConfig(ps, (ClearCasePrepareConfig) nonValidatedClearToolConfig);
                ClearCaseFinalizeConfig.printConfig(ps, (ClearCaseFinalizeConfig) nonValidatedClearToolConfig);
                ClearCaseActivityConfig.printConfig(ps, (ClearCaseActivityConfig) nonValidatedClearToolConfig);
                SynchronizationConfig.printConfig(ps, (SynchronizationConfig) nonValidatedClearToolConfig);
                GitConfig.printConfig(ps, (GitConfig) nonValidateGitConfig);
                GitRepositoryConfig.printConfig(ps, (GitRepositoryConfig) nonValidateGitConfig);
                GitPrepareConfig.printConfig(ps, (GitPrepareConfig) nonValidateGitConfig);
                GitFinishConfig.printConfig(ps, (GitFinishConfig) nonValidateGitConfig);
                ps.close();
            }
        }

        final ClearToolConfigValidated cleartoolConfig;
        final GitConfigValidated gitConfig;

        try {
            cleartoolConfig = new ClearToolConfigValidated(nonValidatedClearToolConfig);
            cleartoolConfig.validateAll();
            gitConfig = new GitConfigValidated(nonValidateGitConfig);
            gitConfig.validateAll();
        } catch (final ConfigException e) {
            logger.error("Incorrent environment configuration: {}", e.getMessage());
            return;
        }

        final SynchronizeTask task = new SynchronizeTask(cleartoolConfig, gitConfig, compareOnly, compareRoot);
        try {
            task.call();
        } catch (final Exception ex) {
            logger.error("Failed to execute Sync Task.", ex);
        }
    }

    private static GitConfigPojo createDefaultGitConfig() {
        final GitConfigPojo gitConfigDefault = new GitConfigPojo();
        gitConfigDefault.setApplyDefaultGitConfig(false);
        gitConfigDefault.setCleanLocalGitRepository(false);
        gitConfigDefault.setFastForwardLocalGitRepository(false);
        gitConfigDefault.setFetchRemoteGitRepository(false);
        gitConfigDefault.setResetLocalGitRepository(false);
        return gitConfigDefault;
    }

    private static ClearToolConfigPojo createDefaultClearToolConfig() {
        final ClearToolConfigPojo clearToolConfigDefault = new ClearToolConfigPojo();
        clearToolConfigDefault.setUseCommitStampFile(false);
        clearToolConfigDefault.setUseCounterStampFile(false);
        clearToolConfigDefault.setCommitStampFile(new File("sync-commit-stamp.txt"));
        clearToolConfigDefault.setCounterStampFile(new File("sync-counter-stamp.txt"));
        clearToolConfigDefault.setUseActivity(false);
        clearToolConfigDefault.setUpdateVobRoot(false);
        clearToolConfigDefault.setCheckForgottenCheckout(false);
        return clearToolConfigDefault;
    }
}
