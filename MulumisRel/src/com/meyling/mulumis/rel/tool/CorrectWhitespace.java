/* $Id$
 *
 * (C) 2005 Michael Meyling
 */

package com.meyling.mulumis.rel.tool;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;


/**
 * Searches for certain strings in all files and replaces them.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public class CorrectWhitespace extends ProcessFiles {


    /** counter of current replacements */
    private int replacements = 0;

    /**
     * Replaces strings in files.
     * 
     * @param   args    First parameter is the string we are searching for. Second parameter
     *                  is the replacement string. Optional third parameter is the starting
     *                  directory.
     */
    public static void main(final String args[]) {
        if (args.length < 1) {
            help();
            return;
        }
        final CorrectWhitespace replace = new CorrectWhitespace();
        for (int i = 0; i < args.length; i++) {
            try {
                replace.processFiles(new File(args[i]), new FileFilter() {
                    public boolean accept(File file) {
                        return !"CVS".equals(file.getName()) &&
                            (file.isDirectory() || file.getName().endsWith(".java"));
                    }
                    
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Replacements: " + replace.getReplacements());
    }

    /**
     * Print the calling conventions to <code>System.err</code>.
     */
    public static void help() {
        System.out.println("Parameters: <directory>");
        System.out.println();
        System.out.println("    directory:   work in this directory and all of its subdirectories");
    }


    /**
     * Constructor.
     */
    private CorrectWhitespace() {
        // nothing to do
    }


    /**
     * Returns the number of replacements that have been made.
     *
     * @return  Current number of replacements.
     */
    protected int getReplacements() {
        return replacements;
    }


    /**
     * The contents of a file is processed.
     * 
     * @param   file    File to process
     * @throws  IOException     A reading or writing exception occurred.
     */
    protected void process(final File file) throws IOException {

        final String data = GeneratorUtility.loadFile(file);
        StringBuffer result = new StringBuffer(data);
        GeneratorUtility.replace(result, "\t", "    ");
//        while (GeneratorUtility.replace(result, " \015\012", "\015\012")) {
        while (GeneratorUtility.replace(result, " \r\n", "\r\n")) {
            // nothing to do
        }
        if (!result.toString().endsWith("\r\n")) {
        	result.append("\r\n");
        }
        if (!result.toString().equals(data)) {
            replacements++;
            GeneratorUtility.saveFile(file, result);
        }

    }


}