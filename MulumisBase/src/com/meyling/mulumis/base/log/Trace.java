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


package com.meyling.mulumis.base.log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.meyling.mulumis.base.util.IoUtility;


/**
 * Developer trace. If the trace output stream is not set via {@link #setPrintStream(PrintStream)}
 * the output goes to {@link java.lang.System#out}.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class Trace {

    /** Write trace output to this stream. */
    private static PrintStream out = System.out;

    /** Date output format .*/
    private static final SimpleDateFormat FORMATTER
        = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss,SSS");

    // TODO mime 20050205: make output of qualified classname configurable

    /**
     * Constructor.
     */
    private Trace() {
        // don't call me
    }

    /**
     * Set trace output stream.
     *
     * @param   stream  New trace output.
     */
    public static void setPrintStream(final PrintStream stream) {
// TODO mime: remove        out = stream;
    }

    /**
     * Trace object.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that object.
     * @param   object          Object to trace.
     */
    public static void trace(final Object tracingObject, final String method,
            final Object object) {
        printObjectMethod(tracingObject, method);
        out.println(object);
    }

    /**
     * Trace object.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     * @param   object          Object to trace.
     */
    public static void trace(final Class tracingClass, final String method,
            final Object object) {
        printClassMethod(tracingClass, method);
        out.println(object);
    }

    /**
     * Trace throwable.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that object.
     * @param   throwable       Throwable to trace.
     */
    public static void trace(final Object tracingObject, final String method,
            final Throwable throwable) {
        printObjectMethod(tracingObject, method);
        out.println(throwable);
        throwable.printStackTrace(out);
    }

    /**
     * Trace throwable.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     * @param   throwable       Throwable to trace.
     */
    public static void trace(final Class tracingClass, final String method,
            final Throwable throwable) {
        printClassMethod(tracingClass, method);
        out.println(throwable);
        throwable.printStackTrace(out);
    }

    /**
     * Trace throwable and extra description.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that object.
     * @param   description     Further information.
     * @param   throwable       Throwable to trace.
     */
    public static void trace(final Object tracingObject, final String method,
            final String description, final Throwable throwable) {
        printObjectMethod(tracingObject, method);
        out.println(description);
        printObjectMethod(tracingObject, method);
        out.println(throwable);
        throwable.printStackTrace(out);
    }

    /**
     * Trace throwable and extra description.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     * @param   description     Further information.
     * @param   throwable       Throwable to trace.
     */
    public static void trace(final Class tracingClass, final String method,
            final String description, final Throwable throwable) {
        printClassMethod(tracingClass, method);
        out.println(description);
        printClassMethod(tracingClass, method);
        out.println(throwable);
        throwable.printStackTrace(out);
    }

    /**
     * Trace method begin. Should be followed by an analogous
     * {@link #traceEnd(Object, String)} call.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that object.
     */
    public static void traceBegin(final Object tracingObject, final String method) {
        trace(tracingObject, method, "begin");
    }

    /**
     * Trace method begin. Should be followed by an analogous {@link #traceEnd(Class, String)} call.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     */
    public static void traceBegin(final Class tracingClass, final String method) {
        trace(tracingClass, method, "begin");
    }

    /**
     * Trace method end.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that object.
     */
    public static void traceEnd(final Object tracingObject, final String method) {
        trace(tracingObject, method, "end");
    }

    /**
     * Trace method end.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     */
    public static void traceEnd(final Class tracingClass, final String method) {
        trace(tracingClass, method, "end");
    }

    /**
     * Trace parameter.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that object.
     * @param   param           Parameter to trace.
     * @param   value           Value of parameter.
     */
    public static void traceParam(final Object tracingObject, final String method,
            final String param, final Object value) {
        trace(tracingObject, method, param + "=" + value);
    }

    /**
     * Trace parameter.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     * @param   param           Parameter to trace.
     * @param   value           Value of parameter.
     */
    public static void traceParam(final Class tracingClass, final String method,
            final String param, final Object value) {
        trace(tracingClass, method, param + "=" + value);
    }

    /**
     * Trace parameter.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that object.
     * @param   param           Parameter to trace.
     * @param   value           Value of parameter.
     */
    public static void traceParam(final Object tracingObject, final String method,
            final String param, final int value) {
        trace(tracingObject, method, param + "=" + value);
    }

    /**
     * Trace parameter.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     * @param   param           Parameter to trace.
     * @param   value           Value of parameter.
     */
    public static void traceParam(final Class tracingClass, final String method,
            final String param, final int value) {
        trace(tracingClass, method, param + "=" + value);
    }

    /**
     * Trace parameter.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that object.
     * @param   param           Parameter to trace.
     * @param   value           Value of parameter.
     */
    public static void traceParam(final Object tracingObject, final String method,
            final String param, final double value) {
        trace(tracingObject, method, param + "=" + value);
    }

    /**
     * Trace parameter.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     * @param   param           Parameter to trace.
     * @param   value           Value of parameter.
     */
    public static void traceParam(final Class tracingClass, final String method,
            final String param, final double value) {
        trace(tracingClass, method, param + "=" + value);
    }

    /**
     * Write stacktrace into trace.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that object.
     */
    public static void traceStack(final Object tracingObject, final String method) {
        try {
            throw new Exception("Stacktrace");
        } catch (Exception e) {
            trace(tracingObject, method, e);
        }
    }

    /**
     * Write stacktrace into trace.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     */
    public static final void traceStack(final Class tracingClass, final String method) {
        try {
            throw new Exception("Stacktrace");
        } catch (Exception e) {
            trace(tracingClass, method, e);
        }
    }

    /**
     * Write traced location into trace.
     *
     * @param   tracingObject   Instance that wants to make a trace entry.
     * @param   method          Method of that class.
     */
    private static final void printObjectMethod(final Object tracingObject, final String method) {
        out.print(getTimestamp() + " [" + (tracingObject != null
                ? IoUtility.getClassName(tracingObject.getClass()) + "." : "")
                + method + "] ");
    }

    /**
     * Write traced location into trace.
     *
     * @param   tracingClass    Class that wants to make a trace entry.
     * @param   method          Method of that class.
     */
    private static final void printClassMethod(final Class tracingClass, final String method) {
        out.print(getTimestamp() + " [" + IoUtility.getClassName(tracingClass) + "."
            + method + "] ");
    }

    /**
     * Get current time.
     *
     * @return  Current timestamp.
     */
    private static final String getTimestamp() {
        return FORMATTER.format(new Date()).toString();
    }

}
