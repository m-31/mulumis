// $Id$
//
// This file is part of the program suite "Simulum". Simulum deals with
// different simulations of star movements and their visualizations.
//
// Copyright (C) 2004 by Michael Meyling
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
//    http://www.gnu.org/licenses/lgpl.html
//
// If you didn't download this code from the following link, you should 
// check if you aren't using an obsolete version:
//     http://sourceforge.net/projects/mulumis
//
// The hompage of the simulum project is:
//     http://www.mulumis.meyling.com

package com.meyling.mulumis.rel.web;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class deals with character seperated files:
 * each line is a sequence of values that are separated by
 * the character ";". If the beginning of a value
 * is a quote character """ each pair of quote characters
 * is replaced by a single quote character til a single
 * quote character marks the end of that value.
 *
 * @author     Michael Meyling
 * @version    $Revision$
 */
public class CsvFileReader {

    /** fixed separator */
    public static final char SEPARATOR = ';';

    /** csv file to read */
    private BufferedReader in;

    /** name of csv file */
    private final String fileName;

    /** line counter */
    private int linecounter = 0;

    /** byte position in file */
    private long filePosition = 0;

    /** last read line */
    private String line = null;

    /** parsed line */
    private final List parsedData = new ArrayList();

    /** was the last line already parsed? */
    private boolean parsed;


    /**
     * Constructs a new object.
     *
     * @param   inputName   name of the imput file
     * @throws  IOException if the file couldn't be opened
     * @throws  FileNotFoundException   if the file couldn't be found
     */
    public CsvFileReader(final String inputName)
             throws IOException, FileNotFoundException {
        this.fileName = inputName;
        this.in = new BufferedReader(new FileReader(fileName));
    }


    /**
     * Constructs a new object.
     *
     * @param   file    imput file
     * @throws  IOException if the file couldn't be opened
     * @throws  FileNotFoundException   if the file could't be found
     */
    public CsvFileReader(final File file)
             throws IOException, FileNotFoundException {
        this.fileName = file.getPath();
        this.in = new BufferedReader(new FileReader(file));
    }


    /**
     * Constructs a new object.
     *
     * @param   inputName   name of the imput file
     * @param   skip                go to this byte position in file
     * @throws  IOException if the file couldn't be opened
     * @throws  FileNotFoundException   if the file could't be found
     */
    public CsvFileReader(final String inputName, final long skip)
             throws IOException, FileNotFoundException {
        this.fileName = inputName;
        this.in = new BufferedReader(new FileReader(fileName));
        if (this.in.skip(skip) != skip) {
            throw new IOException("file position "+ skip + " not found");
        }
        this.filePosition = skip;
    }


    /**
     * Closes the file without any exception.
     */
    public final void closeAll() {
        this.parsed = false;
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
            }
            in = null;
        }
    }


    /**
     * For reading a whole line of the file. .
     * If there are no lines left
     * {@link #closeAll} is automatically called.
     *
     * @return  read line or <code>null</code> if there are no lines left
     * @throws  IOException if a reading exception occured
     */
    public final String readLine() throws IOException {

        this.parsed = false;
        if (this.in == null) {
            return null;
        }
        // TODO: Q & D, if there are not two end of line characters, we get a wrong result
        final int len = (this.line != null ? this.line.length() + 2 : 0);
        this.line = this.in.readLine();
        this.linecounter++;
        this.filePosition += len;
        if (this.line == null) {
            this.in.close();
            this.in = null;
        }
        return this.line;
    }


    /**
     * For reading a whole non empty line of the file. .
     * A comment line is automatically skipped. If there are no lines left
     * {@link #closeAll} is automatically called.
     *
     * @return  read line or <code>null</code> if there are no lines left
     * @throws  IOException if a reading exception occured
     */
    public final String readNonEmptyLine() throws IOException {

        this.parsed = false;
        if (this.in == null) {
            return null;
        }
        do {
            this.line = this.in.readLine();
            this.linecounter++;
            if (this.line != null) {
                this.filePosition += this.line.length();
            }
        } while (this.line != null && (this.line.length() == 0
                || this.line.startsWith("#")));
        if (this.line == null) {
            this.in.close();
            this.in = null;
        }
        return this.line;
    }


    /**
     * Returns the name of the input file.
     *
     * @return file name
     */
    public final String getFileName() {
        return this.fileName;
    }


    /**
     * Get the current line number of file.
     *
     * @return  current line number
     */
    public final int getLineNumber() {
        return this.linecounter;
    }


    /**
     * Get the byte position number of file. This number is only correct, if
     * the carrige return is made of two characters.    // TODO
     *
     * @return  current line number
     */
    public final long getFilePosition() {
        return this.filePosition;
    }


    /**
     * Quotes a <code>String</code> for a csv file: replaces a quote by
     * double quote and appends a quote at the beginning and the end.
     *
     * @param   unquoted    <code>String</code> that will be extended
     * @return  with quotes extended <code>String<code>
     */
    public static final String quote(String unquoted) {

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
     * Gets a certain piece of the line that has {@link #SEPARATOR}
     * separated values. A quoted value is "dequoted".
     *
     * @param    num     piece number
     * @return   the <code>num</code>-th piece of <code>str</code>
     * @throws   IllegalArgumentException
     *                     if a quoted value doesn't end with a quote
     *                     or isn't followed by ";" or the end of the
     *                     line
     */
    public final String getPiece(final int num)
            throws  IllegalArgumentException {

        if (this.in == null || line == null) {
            return "";
        }
        if (!this.parsed) {
            parseLine();
        }
        if (num > 0 && num <= this.parsedData.size()) {
            return (String) this.parsedData.get(num - 1);
        } else {
            return "";
        }
    }


    /**
     * Get number of pieces separated by {@link #SEPARATOR} in line.
     *
     * @throws   IllegalArgumentException
     *                     if a quoted value doesn't end with a quote
     *                     or isn't followed by ";" or the end of the
     *                     line
     */
    public final int getPieceNumber() throws
            IllegalArgumentException {

        if (this.in == null) {
            return 0;
        }
        if (!this.parsed) {
            parseLine();
        }
        return this.parsedData.size();
    }


    /**
     * Parse line into pieces.
     *
     * @throws   IllegalArgumentException
     *                     if a quoted value doesn't end with a quote
     *                     or isn't followed by ";" or the end of the
     *                     line
     */
    private final void parseLine() throws IllegalArgumentException {

        StringBuffer part = new StringBuffer(); // result
        int i = 0;                            // position

        this.parsedData.clear();              // clear old results
        for (int j = 0; ; j++) {              // count til "num"-th piece
            part.setLength(0);
            if (i >= this.line.length()) {
                if (this.line.length() > 0
                        && this.line.charAt(i - 1) == SEPARATOR) {
                    this.parsedData.add("");  //  SEPARATOR at end
                }
                this.parsed = true;
                return;
            }
            if (this.line.charAt(i) != '\"') {
                int k = i;                    // remember this position

                while (i < this.line.length()
                        && this.line.charAt(i) != SEPARATOR) {
                    i++;
                }
                part.append(this.line.substring(k, i));
                if (i < this.line.length()) {
                    i++;
                }
            } else {
                i++;
                if (i >= this.line.length()) {
                    throw new IllegalArgumentException(
                            "closing \" is missing:\n" + line);
                }
                int k;                        // remember this position

                do {
                    k = i;
                    while (i < this.line.length()
                            && this.line.charAt(i) != '\"') {
                        i++;
                    }
                    if (i >= this.line.length()) {
                        throw new IllegalArgumentException(
                            "closing \" is missing:\n" + line);
                    }
                    i++;
                    if (i >= this.line.length()) {
                        break;
                    }
                    if (this.line.charAt(i) == '\"') {
                        part.append(this.line.substring(k, i));
                        i++;
                        continue;
                    }
                    break;
                } while (true);
                part.append(this.line.substring(k, i - 1));
                if (i < this.line.length()) {
                    if (this.line.charAt(i) != SEPARATOR) {
                        throw new IllegalArgumentException(
                            "closing \" must be followed by " + SEPARATOR
                            + "\n" + line);
                    }
                    i++;
                }
            }
            this.parsedData.add(part.toString());
        }
    }



    /**
     * Gets a certain piece of the line that has {@link #SEPARATOR}
     * separated values. A quoted value is "dequoted".
     *
     * @param    str     data line
     * @param    num     piece number
     * @return   the <code>num</code>-th piece of <code>str</code>
     * @throws   IllegalArgumentException
     *                     if a quoted value doesn't end with a quote
     *                     or isn't followed by ";" or the end of the
     *                     line
     */
    public static final String getPiece(String str, int num) throws
            IllegalArgumentException {

        StringBuffer part = new StringBuffer(); // result

        int i = 0;                            // position

        for (int j = 0; j < num; j++) {       // count til "num"-th piece
            if (i >= str.length()) {
                break;
            }
            part.setLength(0);
            if (str.charAt(i) != '\"') {
                int k = i;                    // remember position

                while (i < str.length() && str.charAt(i) != SEPARATOR) {
                    i++;
                }
                part.append(str.substring(k, i));
                if (i < str.length()) {
                    i++;
                }
            } else {
                i++;
                if (i >= str.length()) {
                    throw new IllegalArgumentException(
                            "closing \" is missing:\n" + str);
                }
                int k;                        // remember position

                do {
                    k = i;
                    while (i < str.length() && str.charAt(i) != '\"') {
                        i++;
                    }
                    if (i >= str.length()) {
                        throw new IllegalArgumentException(
                                "closing \" is missing:\n" + str);
                    }
                    i++;
                    if (i >= str.length()) {
                        break;
                    }
                    if (str.charAt(i) == '\"') {
                        part.append(str.substring(k, i));
                        i++;
                        continue;
                    }
                    break;
                } while (true);
                part.append(str.substring(k, i - 1));
                if (i < str.length()) {
                    if (str.charAt(i) != SEPARATOR) {
                        throw new IllegalArgumentException(
                            "closing \" must be followed by " + SEPARATOR
                            + "\n" + str);
                    }
                    i++;
                }
            }
        }
        return part.toString();
    }


    /**
     * Get number of pieces separated by {@link #SEPARATOR} in line.
     *
     * @param   str     parse this line
     * @throws  IllegalArgumentException
     *                     if a quoted value doesn't end with a quote
     *                     or isn't followed by ";" or the end of the
     *                     line
     */
    public static final int getPieceNumber(String str) throws
            IllegalArgumentException {

        int i = 0;                     // position

        for (int j = 0; ; j++) {       // count til "num"-th piece
            if (i >= str.length()) {
                if (str.length() > 0 && str.charAt(i - 1) == SEPARATOR) {
                    j++;               // SEPARATOR at end
                }
                return j;
            }
            if (str.charAt(i) != '\"') {
                int k = i;             // remember this position

                while (i < str.length() && str.charAt(i) != SEPARATOR) {
                    i++;
                }
                if (i < str.length()) {
                    i++;
                }
            } else {
                i++;
                if (i >= str.length()) {
                    throw new IllegalArgumentException(
                        "closing \" is missing:\n" + str);
                }
                int k;                 // remember this position

                do {
                    k = i;
                    while (i < str.length() && str.charAt(i) != '\"') {
                        i++;
                    }
                    if (i >= str.length()) {
                        throw new IllegalArgumentException(
                            "closing \" is missing:\n" + str);
                    }
                    i++;
                    if (i >= str.length()) {
                        break;
                    }
                    if (str.charAt(i) == '\"') {
                        i++;
                        continue;
                    }
                    break;
                } while (true);
                if (i < str.length()) {
                    if (str.charAt(i) != SEPARATOR) {
                        throw new IllegalArgumentException(
                            "closing \" must be followed by " + SEPARATOR
                            + "\n" + str);
                    }
                    i++;
                }
            }
        }
    }


}
