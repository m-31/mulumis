/* $Id$
 *
 * (C) 2005 Michael Meyling
 */
package com.meyling.mulumis.rel.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * Utilities.
 *
 * @author  Michael Meyling
 * @version $Revision$
 */
public abstract class GeneratorUtility {

    /**
     * Constructor.
     */
    private GeneratorUtility() {
        // don't call me
    }

    /**
     * Utility method to set a file as non read only.
     * If the file is already writable, does nothing.
     *
     * @param f the <code>File</code> instance whose read only flag should
     *  be unset.
     *
     * @return whether or not <code>f</code> is writable after trying to make it
     *  writeable -- note that if the file doesn't exist, then this returns
     *  <code>true</code>
     */
    public static boolean setWriteable(final File f) {
        if (!f.exists() || f.canWrite()) {
            return true;
        }

        // get the operating system
        final String os = System.getProperty("os.name").toLowerCase(Locale.US);

        String fileName;
        try {
            fileName = f.getCanonicalPath();
        } catch(IOException ioe) {
            fileName = f.getPath();
        }

        String[] cmds = null;
        if (os.indexOf("windows") != -1) {
            if (os.indexOf("windows 9") != -1) {
                cmds = new String[] {"command.com", "/E:1900",  "/C", "Attrib", "-R",  fileName};
            } else {
                cmds = new String[] {"cmd.exe", "/E:1900",  "/C", "Attrib", "-R",  fileName};
            }
        } else if (os.startsWith("mac os")) {
            // TODO mime 20050205: still missing mac os implementation
            cmds = null; 
        } else {
            cmds = new String[] { "chmod", "u+w", fileName};
        }
        if (cmds != null) {
            try {
                Process p = Runtime.getRuntime().exec(cmds, null, f.getParentFile());
                p.waitFor();
            } catch(SecurityException ignored) {
                // ignored
            } catch(IOException ignored) {
                // ignored
            } catch(InterruptedException ignored) {
                // ignored
            }
        }
        if (!f.canWrite()) {
            return false;
        }
        return true;
    }

    /**
     * Saves a <code>String</code> in a file.
     *
     * @param   file        File to save into.
     * @param   text        Data to save in the file
     * @throws  IOException File exception occurred.
     */
    public static final void saveFile(final File file, final String text) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(text);
        out.close();
    }

    /**
     * Saves a <code>String</code> in a file. 
     *
     * @param   filename    Name of the file (could include path).
     * @param   text        Data to save in the file.
     * @throws  IOException File exception occurred.
     */
    public static final void saveFile(final String filename, final String text) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        out.write(text);
        out.close();
    }

    /**
     * Saves a <code>StringBuffer</code> in a file.
     *
     * @param   file        Name of the file (could include path).
     * @param   text        Data to save in the file.
     * @throws  IOException File exception occurred.
     */
    public static final void saveFile(final File file, final StringBuffer text) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(text.toString());
        out.close();
    }

    /**
     * Appends a <code>String</code> to the end of a file.
     *
     * @param   file        File to append.
     * @param   text        Data to save in the file.
     * @throws  IOException File exception occurred.
     */
    public static final void appendFile(final File file, final String text) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
        out.write(text);
        out.close();
    }

    /**
     * Reads a file and returns the contents as a <code>String</code>.
     *
     * @param   filename    Name of the file (could include path).
     * @return  Contents of file.
     * @throws  IOException File exception occurred.
     */
    public static final String loadFile(final String filename) throws IOException {
        return loadFile(new File(filename));
    }

    /**
     * Reads a file and returns the contents as a <code>String</code>.
     *
     * @param   file        File to load.
     * @return  Contents of file.
     * @throws  IOException File exception occurred.
     */
    public static final String loadFile(File file) throws IOException {
        final int size = (int) file.length();
        final FileReader in = new FileReader(file);
        final char[] data = new char[size];
        int charsread = 0;
        while (charsread < size) {
            charsread += in.read(data, charsread, size - charsread);
        }
        in.close();
        return new String(data);
    }

    /**
     * Reads a file and returns the contents as a <code>String</code>.
     *
     * @param   file        File to load.
     * @return  Contents of file.
     * @throws  IOException File exception occurred.
     */
    public static final byte[] loadFileBinary(final File file) throws IOException {
        final int size = (int) file.length();
        final FileInputStream in = new FileInputStream(file);
        final byte[] data = new byte[size];
        int charsread = 0;
        while (charsread < size) {
            charsread += in.read(data, charsread, size - charsread);
        }
        in.close();
        return data;
    }

    /**
     * Get non qualified class name.
     *
     * @param   clazz   Qualified class name.
     * @return  Non qualified class name.
     */
    public final static String getClassName(final String clazz) {
        return clazz.substring(clazz.lastIndexOf('.') + 1);
    }

    /**
     * Get package name out of qualified class name.
     *
     * @param   clazz   Qualified class name.
     * @return  Package name.
     */
    public final static String getPackageName(final String clazz) {
        return clazz.substring(0, clazz.lastIndexOf('.'));
    }

    /**
     * Quotes a <code>String</code>, if it contains a semicolon or
     * double quotes. In this case at the beginning and end double
     * quotes are added and each occurrence of a double quote is
     * doubled.
     *
     * @param   unquoted    String to quote.
     * @return  possible quoted string.
     */
    public static String quote(final String unquoted) {
    
        if (unquoted == null) {
            return "";
        }
        if (unquoted.indexOf("\"") >= 0 || unquoted.indexOf(";") >= 0) {
            StringBuffer result = new StringBuffer(unquoted.length() + 2);
            result.append("\"");
            for (int i = 0; i < unquoted.length(); i++) {
                if (unquoted.charAt(i) == '\"') {
                    result.append("\"\"");
                } else {
                    result.append(unquoted.charAt(i));
                }
            }
            result.append('\"');
            return result.toString();
        }
        return unquoted;
    }

    /**
     * Escape string for html usage. Certain characters are replaced by their html entities.
     *
     * @param   text    strip this text
     * @return  striped text
     */
    public final static String toHtml(final String text) {

        final StringBuffer buffer = new StringBuffer(text.length());
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            switch (c) {
            case '\"':
                buffer.append("&quot;");
                break;
            case '<':
                buffer.append("&lt;");
                break;
            case '>': buffer.append("&gt;");
                break;
            case '&': buffer.append("&amp;");
                break;
            default:
                buffer.append(c);
            }
        }
        return buffer.toString();
    }


    /**
     * Replaces all occurrences of <code>search</code> in <code>text</code>
     * by <code>replace</code> and returns the result.
     *
     * @param   text    text to work on
     * @param   search  replace this text by <code>replace</code>
     * @param   replace replacement for <code>search</code>
     * @return  resulting string
     */
    public final static String replace(final String text,
            final String search, final String replace) {

        final StringBuffer result = new StringBuffer();
        int pos1 = 0;
        int pos2;
        final int len = search.length();
        while (0 <= (pos2 = text.indexOf(search, pos1))) {
            result.append(text.substring(pos1, pos2));
            result.append(replace);
            pos1 = pos2 + len;
        }
        if (pos1 < text.length()) {
            result.append(text.substring(pos1));
        }
        return result.toString();
    }


    /**
     * Replaces all occurrences of <code>search</code> in <code>text</code>
     * by <code>replace</code> and returns the result.
     *
     * @param   text    text to work on
     * @param   search  replace this text by <code>replace</code>
     * @param   replace replacement for <code>search</code>
     * @return  did replacement take place?
     */
    public final static boolean replace(final StringBuffer text,
            final String search, final String replace) {

        final String buffer = text.toString();
        if (buffer.indexOf(search) >= 0) {
            final String result = replace(text.toString(), search, replace);
            text.setLength(0);
            text.append(result);
            return true;
        }
        return false;
        
// TODO mime 20050205: check if the above could be replaced with: 
/*
        final StringBuffer result = new StringBuffer();
        int pos1 = 0;
        int pos2;
        final int len = search.length();
        while (0 <= (pos2 = text.indexOf(search, pos1))) {
            result.append(text.substring(pos1, pos2));
            result.append(replace);
            pos1 = pos2 + len;
        }
        if (pos1 < text.length()) {
            result.append(text.substring(pos1));
        }
        text.setLength(0);
        text.append(result);
  */
      }


    /**
     * Find number of occurrences.
     *
     * @param   text    text to work on
     * @param   search  replace this text by <code>replace</code>
     * @return  number
     */
    public final static int count(final String text,
            final String search) {

        int counter = 0;
        int pos1 = 0;
        int pos2;
        final int len = search.length();
        while (0 <= (pos2 = text.indexOf(search, pos1))) {
            pos1 = pos2 + len;
            counter++;
        }
        return counter;
    }
    
}
