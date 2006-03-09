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


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Generates web pages.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public class PageGenerator {

    private static final String selectionForeground = "#E6E6E6";
    private static final String selectionBackground = "#DDEEFF";
    
    /** name of the subdirectory where the input source is in */
    public static final String INPUT = "src";

    /** to this subdirectory goes the output */
    public static final String OUTPUT = "page";

    /** default directory, where input is in */
    public static final String DEFAULT_INPUT_DIR
        = "../MulumisDoc/web/";

    /** default output directory, in which input and output directories are in */
    public static final String DEFAULT_OUTPUT_DIR
        = "../MulumisGen/";


    /** read from this directory */
    private final File readDir;

    /** write to this directory */
    private final File writeDir;

    /** html header */
    private final String head;

    /** html tail */
    private final String tail;

    /** name of csv site file */
    private final String siteName;

    /** list of site */
    final List site = new ArrayList();

    /** full list of site */
    final List fullsite = new ArrayList();


    /**
     * Generate web pages. The html parts must reside in a directory
     * named <code>source</code> (@link{#INPUT}) and the resulting
     * pages are written in the directory <code>pages</code>
     * (@link{#OUTPUT}).
     * The sources must have the files <code>site.csv</code>,
     * <code>head.txt</code> and <code>tail.txt</code>.
     * <code>site.csv</code> contains an description of all web pages,
     * and <code>head.txt</code> is written at the beginning of all
     * pages. Analogous <code>tail.txt</code> is put to the end
     * of all pages.
     *
     * @param   args    could contain one string which is interpreted
     *                  as the root directory
     */
    public static void main(final String args[]) {
    	System.out.println(PageGenerator.class.getName() + " begin");
        try {
            PageGenerator r = null;
            if (args.length == 0) {
            	System.err.println("Please start the ANT script to build the web page!");
            	System.exit(-99);
                try {
                    r = new PageGenerator(DEFAULT_INPUT_DIR + "/" + INPUT,
                        DEFAULT_OUTPUT_DIR + "/" + OUTPUT);
                } catch (Exception e) {
                    e.printStackTrace();
                    help();
                    return;
                }
                r.generate();
            } if (args.length == 1) {
                try {
                    r = new PageGenerator(args[0] + "/" + INPUT,
                        args[0] + "/" + OUTPUT);
                } catch (Exception e) {
                    e.printStackTrace();
                    help();
                    return;
                }
                r.generate();
            } if (args.length == 2) {
                try {
                    r = new PageGenerator(args[0], args[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                    help();
                    return;
                }
                r.generate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			System.out.println(PageGenerator.class.getName() + " end");
        }
		
    }


    /**
     * Print the calling conventions to <code>System.err</code>.
     */
    public static void help() {
        System.out.println("j.PageGenerator [directory]");
        System.out.println();
    }


    /**
     * Constructs a PageGenerator object for the given directories.
     *
     * @param   readDirectory   read in this directory
     * @param   writeDirectory  write in this directory
     * @throws  IOException     if reading or writing fails
     */
    PageGenerator(final String readDirectory, final String writeDirectory)
            throws IOException {
        this.readDir = new File(readDirectory);
        this.writeDir = new File(writeDirectory);
        this.head = loadFile(new File(readDir, "head.txt"));
        this.tail = loadFile(new File(readDir, "tail.txt"));
        this.siteName = "site.csv";
    }


    /**
     * Make all neccessary replacements in current directory and its
     * subdirectory.
     *
     * @throws  IOException if a reading or writing exception occured
     */
    private final void generate()
            throws IOException {

        parseSite(true);
        for (int i = 0; i < this.site.size(); i++) {
            generate(((Entry) this.site.get(i)).getFile());
        }
        parseSite(false);
        for (int i = 0; i < this.site.size(); i++) {
            generate(((Entry) this.site.get(i)).getFile());
        }
        generateSitemap(((Entry) fullsite.get(fullsite.size() - 1)).getFile());
    }


    private final void parseSite(final boolean all) throws IOException {

        {
            final CsvFileReader csv
                = new CsvFileReader(new File(this.readDir, this.siteName));
            site.clear();   // one never knows..
            int line = 0;
            int[] mother = new int[4];
            mother[0] = -1;
            int deep = 0;
            String file = "";
            String name = "";
            String title = "";
            String desc = "";
            boolean menu = true;
            while (null != csv.readLine()) {
                file = csv.getPiece(2).trim();
                name = csv.getPiece(3).trim();
                title = csv.getPiece(4).trim();
                desc = csv.getPiece(5).trim();
                menu = !csv.getPiece(6).trim().toLowerCase().equals("n");
                final int i = csv.getPiece(1).length();
                if (i > deep + 1) {
                    throw new IllegalArgumentException(
                        "error in line " + csv.getLineNumber() + " in file "
                        + csv.getFileName());
                }
                if (i > deep) {
                    mother[i] = line - 1;
                }
                deep = i;
                if (all || (deep < 3 && menu)) {
                    site.add(line, new Entry(deep, file, name, title, desc, mother[deep]));
                    line++;
                }
            }
        }
        {
            final CsvFileReader csv
                = new CsvFileReader(new File(this.readDir, this.siteName));
            fullsite.clear();   // one never knows..
            int line = 0;
            int[] mother = new int[4];
            mother[0] = -1;
            int deep = 0;
            String file = "";
            String name = "";
            String title = "";
            String desc = "";
            while (null != csv.readLine()) {
                file = csv.getPiece(2).trim();
                name = csv.getPiece(3).trim();
                title = csv.getPiece(4).trim();
                desc = csv.getPiece(5).trim();
                final int i = csv.getPiece(1).length();
                if (i > deep + 1) {
                    throw new IllegalArgumentException(
                        "error in line " + csv.getLineNumber() + " in file "
                        + csv.getFileName());
                }
                if (i > deep) {
                    mother[i] = line - 1;
                }
                deep = i;
                fullsite.add(line, new Entry(deep, file, name, title, desc, mother[deep]));
                line++;
            }
        }
    }


    /**
     * Generate a certain page.
     *
     * @param   fileName    file to search on
     * @throws  IOException if a reading or writing exception occurs
     */
    private final void generate(final String fileName) throws IOException {

        String data;
        File file = file = new File(this.readDir, fileName);
        try {
            data = loadFile(file);
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }
        final Map expand = new HashMap();
        String title = "";

        int index = -1;
        for (int i = 0; i < site.size(); i++) {
            if (((Entry) site.get(i)).getFile().equals(fileName)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            System.err.println("File " + fileName + " not found in csv file");
            return;
        }
        final Entry entryIndex = (Entry) site.get(index);
        final String subtitle = entryIndex.getTitle();
        String previous = null;
        String next = null;
        int fullIndex = -1;
        for (int i = 0; i < fullsite.size(); i++) {
            if (((Entry) fullsite.get(i)).getFile().equals(fileName)) {
                fullIndex = i;
                break;
            }
        }
        if (fullIndex < 0) {
            System.err.println("File " + fileName + " not found in csv file");
            return;
        }
        try {
            previous = ((Entry) fullsite.get(fullIndex - 1)).getFile();
        } catch (Exception e) {
            // ignore
        }
        try {
            next = ((Entry) fullsite.get(fullIndex + 1)).getFile();
        } catch (Exception e) {
            // ignore
        }
        {
            int i = index;
            for ( ;i >= 0; i = ((Entry) site.get(i)).getMother()) {
                title = ((Entry) site.get(i)).getTitle()
                    + (title.length() > 0 ? " / " + title : "");
            }
            i = index;
            if (index + 1 < site.size()
                    && ((Entry) site.get(index + 1)).getDeep()
                    <= entryIndex.getDeep()) {
                i = entryIndex.getMother();
            }
            for ( ;i >= 0; i = ((Entry) site.get(i)).getMother()) {
                expand.put(new Integer(i), null);
            }
        }

        final StringBuffer tab = new StringBuffer();
        int deep = 0;
        for (int i = 0; i < site.size(); i++) {
            final Entry entry = (Entry) site.get(i);
            if (entry.getDeep() < deep) {
                for ( ; entry.getDeep() < deep; deep--) {
                    tab.append("</table>\n");
                    tab.append("</td>\n</tr>\n");
                }
            }
            if (expand.containsKey(new Integer(i))) {
                tab.append("<tr>\n<td"
                    + (i == index ? " bgcolor=\"" + selectionBackground + "\"" : "")
                    + ">\n"
                    + "<font face=\"Arial,Helvetica\""
                    + "><a href=\""
                    + entry.getFile() + "\">" + entry.getName() + "</a></font>\n"
                    + "<table cellspacing=2 cellpadding=2 bgcolor=\"" + selectionForeground + "\""
                    + " width=\"100%\">\n");
                deep = entry.getDeep() + 1; // "+ 1": get direct children too
            } else if (entry.getDeep() == deep) {
                tab.append(
                    "<tr>\n<td"
                    + (i == index ? " bgcolor=\"" + selectionBackground + "\"" : "")
                    + "><font face=\"Arial,Helvetica\""
                    + "><a href=\""
                    + entry.getFile() + "\">" + entry.getName() + "</a></font></td>\n"
                    + "</tr>\n");
            } else {
//                System.err.println("missing case for " + entry.getFile());
            }
        }
        for ( ; 0 < deep; deep--) {
            tab.append("</table>\n");
            tab.append("</td>\n</tr>\n");
        }
        saveFile(new File(writeDir, fileName),
            addFrame(title, subtitle, tab, data, file, replace(fileName, ".html", ""), previous, next));

    }


    /**
     * @param string 
     * @throws  IOException if a reading or writing exception occurs
     */
    private final void generateSitemap(String previous) throws IOException {

        final StringBuffer tab = new StringBuffer();
//      tab.append("<table cellspacing=2 cellpadding=2 bgcolor=\"#c0c0c0\""
//                  + " width=\"100%\">\n");

        for (int i = 0; i < site.size(); i++) {
            final Entry entry = (Entry) site.get(i);
            if (entry.getDeep() == 0) {
                tab.append(
                    "<tr>\n<td><font face=\"Arial,Helvetica\""
                    + "><a href=\""
                    + entry.getFile() + "\">" + entry.getName() + "</a></font></td>\n"
                    + "</tr>\n");
            }
        }

        final StringBuffer data = new StringBuffer();
        data.append("<div style=\"margin-left: 10px;\">\n"
            + "<table cellspacing=2 cellpadding=2"
            + " width=\"100%\"><tbody><tr><td><div style=\"margin-left: 10px; margin-top: 10px\">\n");

        for (int i = 0; i < fullsite.size(); i++) {
            final Entry current = (Entry) fullsite.get(i);
            final int followingDeep =
                (i + 1< fullsite.size() ? ((Entry) fullsite.get(i + 1)).getDeep() : 0);
            if (current.getDeep() == followingDeep) {
                data.append(
                    "<tr>\n<td>"
                    + "<div style=\"margin-left: " + (10 + 5*current.getDeep()) + "px;\">"
                    + "<font face=\"Arial,Helvetica\""
                    + "><a href=\""
                    + current.getFile() + "\">"
                    + current.getName()
                    + (current.getDescription().length() > 0
                        ? ": " + current.getDescription()
                        : "")
                    + "</a> "
                    + "</font></div></td>\n"
                    + "</tr>\n");
            } else if (current.getDeep() > followingDeep) {
                data.append(
                    "<tr>\n<td>"
                    + "<div style=\"margin-left: " + (10 + 5*current.getDeep()) + "px;\">"
                    + "<font face=\"Arial,Helvetica\""
                    + "><a href=\""
                    + current.getFile() + "\">"
                    + current.getName()
                    + (current.getDescription().length() > 0
                        ? ": " + current.getDescription()
                        : "")
                    + "</a> "
                    + "</div></font></td>\n"
                    + "</tr>\n");
                for (int j = current.getDeep() ; followingDeep < j; j--) {
                    data.append("</table>\n");
                    data.append("</td>\n</tr>\n");
                }
            } else {
                data.append("<tr>\n<td>\n"
                    + "<div style=\"margin-left: " + (10 + 5*current.getDeep()) + "px;\">"
                    + "<font face=\"Arial,Helvetica\">"
                    + "<a href=\""
                    + current.getFile() + "\">"
                    + current.getName()
                    + (current.getDescription().length() > 0
                        ? ": " + current.getDescription()
                        : "")
                    + "</a> "
                    + "</font>\n"
                    + "<table cellspacing=2 cellpadding=2 bgcolor=\"" + selectionForeground + "\""
                    + " width=\"100%\">\n");
            }
        }
        // add sitemap entry itself
        data.append("<tr>\n<td>"
            + "<div style=\"margin-left: " + (10) + "px;\">"
            + "<font face=\"Arial,Helvetica\""
            + "><a href=\"sitemap.html\">"
            + "Sitemap"
            + "</a></div></font></td>\n"
            + "</tr>\n");

        data.append("</table></div>");

        final String fileName;
        fileName = "sitemap.html";
        try {
            saveFile(new File(writeDir, fileName), replace(addFrame("Site Map", "Site Map", tab, data.toString(), null, 
                    "sitemap", previous, null),
                "<td><font face=\"Arial,Helvetica\"><a href=\"sitemap",                "<td bgcolor=\"" + selectionBackground + "\"><font face=\"Arial,Helvetica\"><a href=\"sitemap"));   // TODO Q & D!!!
        } catch (IOException e) {
            System.out.println(e);
        }

    }


    private final String addFrame(final String title, final String subtitle,
            final StringBuffer tab, final String data, final File file, final String link, 
            final String previous, final String next) {
        String result = new String(head);
        // Format the current time.
        final SimpleDateFormat formatter
          = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        final String date1 = formatter.format(new Date());
        result = replace(result, "@date@", date1);
        result = replace(result, "@generator@", this.getClass().getName());
        result = replace(result, "@select@", tab.toString());
        result = replace(result, "@title@",  title);
        result = replace(result, "@previous@", (previous != null ? previous : "#"));
        result = replace(result, "@next@", (next != null ? next : "sitemap.html"));
        if (title.length() > 0) {
            result = replace(result, "@subtitle@", " - " + subtitle);
        } else {
            result = replace(result, "@subtitle@", "");
        }
        result = replace(result, "@link@",  link);
        final String date2;
        if (file != null) {
            result = replace(result, "@width@", " width=\"100%\"");
            date2 = file != null ? replace(formatter
// TODO                .format(new Date(file.lastModified())), "T", " ")
                .format(new Date()), "T", " ")
                : replace(date1, "T", " ");
        } else {
            result = replace(result, "@width@", "");
            date2 = replace(date1, "T", " ");
        }
        result = result + data + replace(tail, "@date@", date2);
        return result;
    }


    /**
     * Replaces all occurences of <code>search</code> in <code>text</code>
     * by <code>replace</code> and returns the result.
     *
     * @param   text    text to work on
     * @param   search  replace this text by <code>replace</code>
     * @param   replace replacement for <code>search</code>
     * @return  resulting string
     */
    public static final String replace(final String text,
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
     * Loads a file and returns the contents as <code>String</code>.
     *
     * @param   filename    file to load
     * @return  contents of file
     * @throws  IOException if a reading or writing exception occurs
     */
    public static String loadFile(final File file) throws IOException {

        try {
            final int size = (int) file.length();
            final FileReader in = new FileReader(file);
            final char[] data = new char[size];
            int charsread = 0;
            while (charsread < size) {
                charsread += in.read(data, charsread, size-charsread);
            }
            in.close();
            return new String(data);
        } catch (IOException e) {
            System.err.println(file.getAbsolutePath());
            throw e;
        }
    }


    /**
     * Saves the given <code>String</code> into a file.
     * @param   file        file to save the data
     * @param   data        this data will be saved
     *
     * @throws  IOException if a reading or writing exception occurs
     */
    public static void saveFile(final File file, final String data)
            throws IOException {
        file.getParentFile().mkdirs();
        final BufferedWriter out = new BufferedWriter(
            new FileWriter(file));
        out.write(data);
        out.close();
    }



    private final static class Entry {

        private final int deep;
        private final String file;
        private final String name;
        private final String title;
        private final String desc;
        private final int mother;


        Entry(final int deep, final String file, final String name,
                final String title, final String desc, final int mother) {
            this.deep = deep;
            this.file = file;
            this.name = name;
            this.title = title;
            this.desc = desc;
            this.mother = mother;
        }

        final int getDeep() {
            return this.deep;
        }

        final String getFile() {
            return this.file;
        }

        final String getName() {
            return this.name;
        }

        final String getTitle() {
            return this.title;
        }

        final String getDescription() {
            return this.desc;
        }

        final int getMother() {
            return this.mother;
        }

    }



}
