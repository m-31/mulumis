// $Id$
//
// This file is part of the program suite "Simulum". Simulum deals with
// different simulations of star movements and their visualizations.
//
// Copyright (C) 2006 by Michael Meyling
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
//
// An online version of this licence could be found at:
//     http://www.gnu.org/licenses/lgpl.html
//
// If you didn't download this code from the following link, you should
// check if you aren't using an obsolete version:
//     http://sourceforge.net/projects/mulumis
//
// The hompage of the simulum project is:
//     http://www.mulumis.meyling.com


package com.meyling.mulumis.base.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;


/**
 * A collection of useful static methods for input and output.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class IoUtility {

    /**
     * Constructor, should never be called.
     */
    private IoUtility() {
        // don't call me
    }

    /**
     * Reads a file and returns the contents as a <code>String</code>.
     *
     * @param   filename    Name of the file (could include path).
     * @return  Contents of file.
     * @throws  IOException File exception occurred.
     */
    public static String loadFile(final String filename)
            throws IOException {

        final StringBuffer buffer = new StringBuffer();
        loadFile(filename, buffer);
        return buffer.toString();
    }

    /**
     * Reads contents of a file into a string buffer.
     *
     * @param   filename    Name of the file (could include path).
     * @param   buffer      Buffer to fill with file contents.
     * @throws  IOException File exception occurred.
     */
    public static void loadFile(final String filename,
            final StringBuffer buffer)
            throws IOException {
        loadFile(new File(filename), buffer);
    }

    /**
     * Reads contents of a stream into a string buffer.
     *
     * @param   in          This stream will be loaded.
     * @param   buffer      Buffer to fill with file contents.
     * @throws  IOException File exception occurred.
     */
    public static void loadStream(final InputStream in,
            final StringBuffer buffer)
            throws IOException {

        buffer.setLength(0);
        int c;
        while ((c = in.read()) >= 0) {
            buffer.append((char) c);
        }
    }

    /**
     * Reads contents of a file into a string buffer.
     *
     * @param   file        This file will be loaded.
     * @param   buffer      Buffer to fill with file contents.
     * @throws  IOException File exception occurred.
     */
    public static void loadFile(final File file,
            final StringBuffer buffer)
            throws IOException {

        final int size = (int) file.length();
        buffer.setLength(0);
        final FileReader in = new FileReader(file);
        final char[] data = new char[size];
        int charsread = 0;
        while (charsread < size) {
            charsread += in.read(data, charsread, size - charsread);
        }
        in.close();
        buffer.insert(0, data);
    }

    /**
     * Reads a file and returns the contents as a <code>String</code>.
     *
     * @param   file        File to load from.
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
     * Saves a <code>String</code> into a file.
     *
     * @param   filename    Name of the file (could include path).
     * @param   text        Data to save in the file.
     * @throws  IOException File exception occurred.
     */
    public static void saveFile(final String filename, final String text)
            throws IOException {
        saveFile(new File(filename), text);
    }

    /**
     * Saves a <code>StringBuffer</code> in a file.
     *
     * @param   filename    Name of the file (could include path).
     * @param   text        Data to save in the file.
     * @throws  IOException File exception occurred.
     */
    public static void saveFile(final String filename, final StringBuffer text)
            throws IOException {
        saveFile(new File(filename), text.toString());
    }


    /**
     * Saves a <code>StringBuffer</code> in a file.
     *
     * @param   file        File to save into.
     * @param   text        Data to save in the file.
     * @throws  IOException File exception occurred.
     */
    public static void saveFile(final File file, final StringBuffer text)
            throws IOException {
        saveFile(file, text.toString());
    }


    /**
     * Saves a <code>String</code> in a file.
     *
     * @param   file        File to save the data in.
     * @param   text        Data to save in the file.
     * @throws  IOException File exception occurred.
     */
    public static void saveFile(final File file, final String text)
            throws IOException {
        BufferedWriter out = new BufferedWriter(
            new FileWriter(file));
        out.write(text);
        out.close();
    }

    /**
     * Saves a <code>String</code> in a file.
     *
     * @param   file        File to save the data in.
     * @param   data        Data to save in the file.
     * @throws  IOException File exception occurred.
     */
    public static void saveFileBinary(final File file, final byte[] data)
            throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        out.write(data);
        out.close();
    }

    /**
     * Copies a file to a different location.
     *
     * @param   from    Copy source.
     * @param   to      Copy destination.
     * @throws  IOException File exception occurred.
     */
    public static void copyFile(final File from, final File to)
            throws IOException {
/*
        final byte[] data = loadFileBinary(from);
        saveFileBinary(to, data);
*/

        if (from.getAbsoluteFile().equals(to.getAbsoluteFile())) {
            return;
        }
        FileInputStream in = new FileInputStream(from);
        FileOutputStream out = new FileOutputStream(to);

        byte[] data = new byte[8 * 1024];
        int length;

        while ((length = in.read(data)) != -1) {
            out.write(data, 0, length);
        }
        in.close();
        out.close();
    }


    /**
     * Quotes a <code>String</code>. If no quotes exist in the
     * <code>String</code>, a quote character is appended at the
     * beginning and the end of the <code>String</code>.
     *
     * @param   unquoted    the unquoted <code>String</code>
     * @return  quoted <code>String</code>
     * @throws  NullPointerException if <code>unquoted == null</code>
     */
    public static String quote(final String unquoted) {

        String result = new String("\"");

        for (int i = 0; i < unquoted.length(); i++) {
            if (unquoted.charAt(i) == '\"') {
                result += "\"\"";
            } else {
                result += unquoted.charAt(i);
            }
        }
        result += '\"';
        return result;
    }

    /**
     * Tests if given <code>String</code> begins with a letter and contains
     * only letters and digits.
     *
     * @param   text    test this
     * @return  is <code>text</code> only made of letters and digits and has
     *          a leading letter?
     * @throws  NullPointerException if <code>text == null</code>
     */
    public static boolean isLetterDigitString(final String text) {
        if (text.length() <= 0) {
            return false;
        }
        if (!Character.isLetter(text.charAt(0))) {
            return false;
        }
        for (int i = 1; i < text.length(); i++) {
            if (!Character.isLetterOrDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Delete file directory recursive.
     *
     * @param   directory   Directory to delete.
     * @return  Was deletion successful?
     */
    public static boolean deleteDir(final File directory) {
        // to see if this directory is actually a symbolic link to a directory,
        // we want to get its canonical path - that is, we follow the link to
        // the file it's actually linked to
        File candir;
        try {
            candir = directory.getCanonicalFile();
        } catch (IOException e) {
            return false;
        }

        // a symbolic link has a different canonical path than its actual path,
        // unless it's a link to itself
        if (!candir.equals(directory.getAbsoluteFile())) {
            // this file is a symbolic link, and there's no reason for us to
            // follow it, because then we might be deleting something outside of
            // the directory we were told to delete
            return false;
        }

        // now we go through all of the files and subdirectories in the
        // directory and delete them one by one
        File[] files = candir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                // in case this directory is actually a symbolic link, or it's
                // empty, we want to try to delete the link before we try
                // anything
                boolean deleted = file.delete();
                if (!deleted) {
                    // deleting the file failed, so maybe it's a non-empty
                    // directory
                    if (file.isDirectory()) {
                        deleteDir(file);
                    }

                    // otherwise, there's nothing else we can do
                }
            }
        }

        // now that we tried to clear the directory out, we can try to delete it
        // again
        return directory.delete();
    }


    /**
     * Get amount of spaces.
     *
     * @param   length  number of spaces
     * @return  String contains exactly <code>number</code> spaces
     */
    public static StringBuffer getSpaces(final int length) {
        final StringBuffer buffer = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            buffer.append(' ');
        }
        return buffer;
    }

    /**
     * Get non qualified class name.
     *
     * @param   clazz   Class.
     * @return  Non qualified class name.
     */
    public static String getClassName(final Class clazz) {
        return clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
    }

    /**
     * Print current system properties to System.out.
     */
    public static void printAllSystemProperties() {
        Properties sysprops = System.getProperties();
        for (Enumeration e = sysprops.propertyNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            String value = sysprops.getProperty(key);
            System.out.println(key + "=" + value);
        }
    }

    /**
     * Get home directory of user.
     *
     * @return  Home directory of user.
     */
    public static File getUserHomeDirectory() {
        return new File((String) System.getProperties().get("user.home"));
    }

    /**
     * Creates necessary parent directories for a file.
     *
     * @param   file    File.
     */
    public static void createNecessaryDirectories(final File file) {
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
    }

    /**
     * Create relative address from <code>orgin</code> to <code>next</code>.
     *
     * @param   orgin   this is the original location
     * @param   next    this should be the next location
     * @return  relative (or if necessary absolute) file path
     */
    public static final String createRelativePath(final File orgin, final File next) {
        try {
            if (orgin.equals(next)) {
                return "";
            }
            try {
                String org = orgin.getCanonicalPath().replace('\\', '/');
                if (orgin.isDirectory() && !org.endsWith("/")) {
                    org += "/";
                }
                String nex = next.getCanonicalPath().replace('\\', '/');
                if (next.isDirectory() && !nex.endsWith("/")) {
                    nex += "/";
                }
                int i = -1; // position of next '/'
                int j = 0;  // position of last '/'
                while (0 <= (i = org.indexOf("/", j))) {
                    if (i >= 0 && nex.length() > i
                            && org.substring(j, i).equals(
                            nex.substring(j, i))) {
                        j = i + 1;
                    } else {
                        break;
                    }
                }
                if (j > 0) {
                    i = j;
                    StringBuffer result = new StringBuffer(nex.length());
                    while (0 <= (i = org.indexOf("/", i))) {
                        i++;
                        result.append("../");
                    }
                    result.append(nex.substring(j));
                    return result.toString();
                }
                return "/" + nex;
            } catch (RuntimeException e) {
                return next.toString();
            }
        } catch (IOException e) {
            return next.toString();
        }
    }

    /**
     * Waits until a '\n' was read from System.in.
     */
    public static void waitln() {
        System.out.println("\n..press <return> to continue");
        try {
            (new java.io.BufferedReader(new java.io.InputStreamReader(
                System.in))).readLine();
        } catch (IOException e) {
            // ignore
        }
    }

    public static final double parseDouble(final String value) throws NumberFormatException {
        final String v = value.trim();
        int factor = 1;
        double result = 0;
        int position = 0;
        boolean isDecimal = false;
        double decimal = 1;
        if (v.length() == 0) {
            throw new NumberFormatException("empty String");
        }
        if (v.charAt(position) == '-') {
            factor = -1;
            position++;
        } else {
            if (v.charAt(position) == '+') {
                position++;
            }
        }
        while (position < v.length()) {
            switch (v.charAt(position)) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                result = 10 * result + (v.charAt(position) - '0');
                if (isDecimal) {
                    decimal *= 10;
                }
                break;
            case '.':
                if (isDecimal) {
                    throw new NumberFormatException("multiple decimal points in " + v);
                }
                isDecimal = true;
                break;
            default:
                throw new NumberFormatException("unexpected character: " + v.charAt(position)
                    + " in " + v);
            }
            position++;
        }
        System.out.println(v + " -> " + (result / decimal));
        return result * factor / decimal;
    }

}
