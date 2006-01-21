/* $Id$
 *
 * (C) 2005 Michael Meyling
 */
package com.meyling.mulumis.rel.tool;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;


/**
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public abstract class ProcessFiles {

    /**
     * Process the given single file.
     * 
     * @param   file    Check this file.
     * @throws  IOException Processing exception occurred.
     */
    abstract protected void process(File file) throws IOException; 

    /**
     * Processes a file or all files of a directory that match a
     * given file filter. For all non directory entries the method
     * {@link #process(File)} is called. For all directory entries
     * this method is calls itself again.
     *
     * @param   file    Check this file or directory.
     * @param   filter  Use this filter when processing a directory.
     *
     * @throws  IOException Processing exception occurred.
     */
    protected void processFiles(final File file, final FileFilter filter)
            throws IOException {
        if (!file.exists()) {
            throw new IOException("File " + file + " doesn't exist");
        }
        if (file.isDirectory()) {
            final File dirFiles[] = file.listFiles(filter);
            if (dirFiles == null) {
                return;
            }
            for (int i = 0; i < dirFiles.length; i++) {
                processFiles(dirFiles[i], filter);
            }
        } else {
            process(file);
        }
    }



    
    
}
