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
public interface ClearCasePrepareConfig {

    /**
     * @return If true, update the entire vob before synchronization.
     */
    Boolean getUpdateVobViewDir();

    public static void printConfig(PrintStream ps, ClearCasePrepareConfig config) {
        ps.println(" * ClearCase preparation configuration:");
        ps.println("   - Update VOB view directory: " + config.getUpdateVobViewDir());
    }

    static void validate(final ClearCasePrepareConfig wrapped) throws ConfigException {
        if (wrapped.getUpdateVobViewDir() == null) {
            throw new ConfigException("Update VOB view directory: missing value.");
        }

    }
}