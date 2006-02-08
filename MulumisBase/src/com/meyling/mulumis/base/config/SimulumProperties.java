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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.util.IoUtility;


/**
 * List of {@link Parameter}s.
 *
 * @version $Revision$
 * @author Michael Meyling
 */
public final class SimulumProperties {

    /** Configuration file location. */
    private static final String CONFIG_MULUMIS_PROPERTIES = "config/mulumis.properties";

    /** Parameter list. */
    private final ParameterList parameterList;

    private Parameter stars;

    private Parameter movement;

    private Parameter sensitivity;

    private Parameter zoom;

    private Parameter radius;

    private Parameter snapshot;

    private Parameter gamma;

    private Parameter deltat;

    /**
     * Constructor. Reads previously saved values.
     */
    public SimulumProperties() {
        final SimulatorProperties properties = new SimulatorProperties();
        parameterList = new ParameterList();
        stars = new Parameter("stars", "Number of Stars", Integer.class,
                    "Number of stars in this simulation.", "" + properties.getStars());
        parameterList.add(stars);

        final List list = new ArrayList();
        list.add("manual");
        list.add("manualDelay");
        list.add("circular");
        list.add("circularNormale");
        list.add("linear");
        movement = new Parameter("movement", "Viewpoint Movement", "Method to move viewpoint",
                properties.getMovement(), properties.getMovement(), list);
        parameterList.add(movement);

        sensitivity = new Parameter("sensitivity", "Sensitivity", Double.class,
                    "Sensitivity of photo plate.", "" + properties.getSensitivity());
        parameterList.add(sensitivity);

        zoom = new Parameter("zoom", "Zoom", Double.class,
                "Zoom factor for photo plate.", "" + properties.getZoom());
        parameterList.add(zoom);

        radius = new Parameter("radius", "Radius", Double.class,
                "View Point Distance.", "" + properties.getRadius());
        parameterList.add(radius);

        snapshot = new Parameter("snapshot", "Snapshot", Integer.class,
                "Clear the photo plate after this number of pictures.", "" + properties.getSnapshot());
        parameterList.add(snapshot);

        gamma = new Parameter("gamma", "Gamma", Double.class,
                "Gamma constant. Zero means no gravity at all.", "" + properties.getGamma());
        parameterList.add(gamma);

        deltat = new Parameter("deltat", "Delta t", Double.class,
                "Time unit used to calculate the gravity movement.", "" + properties.getDeltat());
        parameterList.add(deltat);

        final File configFile = new File(CONFIG_MULUMIS_PROPERTIES);
        try {
            Trace.trace(this, "SimulumProperties", "loading properties");
            parameterList.fill(configFile);
        } catch (IOException e) {
            Trace.trace(this, "SimulumProperties", e);
        }
    }

    /**
     * Get parameter by name.
     *
     * @param   name    Parameter name.
     * @return  Parameter with this <code>name</code>.
     */
    public final Parameter get(final String name) {
        return parameterList.get(name);
    }

    /**
     * Save parameter values in property file.
     *
     * @throws  IOException File problem.
     */
    public void save() throws IOException {
        final File configFile = new File(CONFIG_MULUMIS_PROPERTIES);
        IoUtility.createNecessaryDirectories(configFile);
        parameterList.save(configFile);
    }

    /**
     * Set all parameters back to default values.
     */
    public void resetToDefaultValues() {
        parameterList.resetToDefaultValues();
    }

}
