/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.File;

/**
 *
 * @author Daniel
 */
public interface EnvironmentConfig {

    File getGitExec();

    File getRepositoryDir();

    File getClearToolExec();

    File getVobViewDir();

    void setGitExec(File file);

    void setRepositoryDir(File dir);
    
    void setClearToolExec(File file);
    
    void setVobViewDir(File dir);
}