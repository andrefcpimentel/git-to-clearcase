/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.task;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface GitFinishConfig {

    static void printConfig(PrintStream ps, GitFinishConfig config) {
        // void
    }

    static void validate(final GitFinishConfig config) throws ConfigException {
        // void
    }

}