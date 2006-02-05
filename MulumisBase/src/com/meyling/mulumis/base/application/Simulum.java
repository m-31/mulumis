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


package com.meyling.mulumis.base.application;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.meyling.mulumis.base.gui.window.MainFrame;
import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.util.IoUtility;


/**
 * Test application.
 *
 * @version $Revision$
 * @author    Michael Meyling
 */
public final class Simulum  {

    private static boolean initialized;

    /** Location of trace file. */
    public static final String TRACE_FILE_PATH = "log/trace.txt";

    /**
     * Constructor.
     */
    private Simulum() {
        // nothing to do
    }

    /**
     * Main method.
     *
     * @param   args        Not used.
     */
    public static final void main(final String[] args) {
        final String method = "main(String[])";
        try {
            initalizeTrace();
        } catch (IOException e) {
            // ignore
        }
        try {
            Trace.traceBegin(Simulum.class, method);
            Trace.trace(Simulum.class, method, "after initialization of trace");
            final MainFrame starterDialog = new MainFrame("Simulum - Star Field Simulation");
            Trace.trace(Simulum.class, method, "before show of starter dialog");
            starterDialog.show();
            Trace.trace(Simulum.class, method, "after show of starter dialog");
        } catch (final Exception e) {
            e.printStackTrace();
            Trace.trace(Simulum.class, method, e);
        } catch (final Error e) {
            e.printStackTrace();
            Trace.trace(Simulum.class, method, e);
        } finally {
            Trace.traceEnd(Simulum.class, method);
        }
    }
    /**
     * Initialize trace file. See {@link #TRACE_FILE_PATH}.
     *
     * @throws  IOException Initialization failed.
     */
    public static void initalizeTrace() throws IOException {
        if (!initialized) {
            final File traceFile = new File(TRACE_FILE_PATH);
            IoUtility.createNecessaryDirectories(traceFile);
            Trace.setPrintStream(new PrintStream(new FileOutputStream(traceFile)));
            initialized = true;
            System.out.println("Logging into file: " + traceFile.getAbsolutePath());
        }
    }


}
