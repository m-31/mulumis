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


package com.meyling.mulumis.base.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * List of {@link Parameter}s.
 *
 * @version $Revision$
 * @author Michael Meyling
 */
public final class ParameterList {

    /** Parameter list. */
    private final List list;

    /**
     * Constructor.
     */
    public ParameterList() {
        list = new ArrayList();
    }

    /**
     * Add parameter to list.
     *
     * @param   parameter   Parameter to add.
     */
    public void add(final Parameter parameter) {
        list.add(parameter);
    }

    /**
     * Get number of parameters.
     *
     * @return  Number of parameters.
     */
    public final int size() {
        return list.size();
    }

    /**
     * Get <code>i</code>-th parameter.
     *
     * @param   i   Parameter number.
     * @return  <code>i</code>-th Parameter.
     */
    public final Parameter get(final int i) {
        return (Parameter) list.get(i);
    }

    /**
     * Get parameter by name.
     *
     * @param   name    Parameter name.
     * @return  Parameter with this <code>name</code>.
     * @throws  NullPointerException    Parameter not found.
     */
    public final Parameter get(final String name) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getName().equals(name)) {
                return get(i);
            }
        }
        throw new NullPointerException("parameter \"" + name + "\" not found");
    }

    /**
     * Fill parameter values from property file.
     *
     * @param   file    Property file to get the values from.
     * @throws  IOException File problem.
     */
    public final void fill(final File file) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        for (int i = 0; i < size(); i++) {
            Parameter parameter = get(i);
            if (properties.getProperty(parameter.getName()) != null) {
                parameter.setStringValue(properties.getProperty(parameter.getName()));
            }
        }
    }

    /**
     * Save parameter values in property file.
     *
     * @param   file    Property file to save the values in.
     * @throws  IOException File problem.
     */
    public void save(final File file) throws IOException {
        Properties properties = new Properties();
        for (int i = 0; i < size(); i++) {
            Parameter parameter = get(i);
            if (parameter.getStringValue() != null) {
                properties.setProperty(parameter.getName(), parameter.getStringValue());
            }
        }
        properties.store(new FileOutputStream(file), null);
    }

    /**
     * Set all parameters back to default values.
     */
    public void resetToDefaultValues() {
        for (int i = 0; i < size(); i++) {
            Parameter parameter = get(i);
            parameter.resetToDefault();
        }
    }

}
