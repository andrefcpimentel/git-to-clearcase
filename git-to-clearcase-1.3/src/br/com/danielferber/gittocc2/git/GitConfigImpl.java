/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import java.io.File;

public abstract class GitConfigImpl implements GitConfig {

    @Override
    public File getGitAbsoluteExec() {
        return getGitExec().getAbsoluteFile();
    }

    @Override
    public File getRepositoryAbsoluteDir() {
        return getRepositoryDir().getAbsoluteFile();
    }

}
