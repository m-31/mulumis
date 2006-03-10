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
//    http://www.gnu.org/licenses/lgpl.html
//
// If you didn't download this code from the following link, you should
// check if you aren't using an obsolete version:
//     http://sourceforge.net/projects/mulumis
//
// The hompage of the simulum project is:
//     http://www.mulumis.meyling.com

package com.meyling.mulumis.base.application;

import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.simulator.Simulator;
import com.meyling.mulumis.base.simulator.SimulatorAttributes;
import com.meyling.mulumis.base.util.IoUtility;
import com.meyling.mulumis.base.view.CameraAttributes;


/**
 * Simulates star field.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class StarApplet extends FieldViewer {

    public StarApplet() {
        super();
    }

    public void init() {
        super.init();
        final SimulatorAttributes simulatorProperties = new SimulatorAttributes();
        final CameraAttributes viewerProperties = new CameraAttributes();
        try {
            if (getParameter("stars") != null) {
                simulatorProperties .setStars(Integer.parseInt(getParameter("stars")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getParameter("movement") != null) {
                viewerProperties.setMovement(getParameter("movement"));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getParameter("delta") != null) {
                viewerProperties.setDelta(IoUtility.parseDouble(getParameter("delta")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getParameter("sensitivity") != null) {
                viewerProperties.setSensitivity(IoUtility.parseDouble(getParameter("sensitivity")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getParameter("radius") != null) {
                viewerProperties.setRadius(IoUtility.parseDouble(getParameter("radius")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getParameter("zoom") != null) {
                viewerProperties.setZoom(IoUtility.parseDouble(getParameter("zoom")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getParameter("gamma") != null) {
                simulatorProperties.setGamma(IoUtility.parseDouble(getParameter("gamma")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getParameter("deltat") != null) {
                simulatorProperties.setDeltat(IoUtility.parseDouble(getParameter("deltat")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean gravityOn = false;
        try {
            if (getParameter("gravity") != null) {
                gravityOn = "true".equals(getParameter("gravity").toString().toLowerCase())
                    || "on".equals(getParameter("gravity").toString().toLowerCase());
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        Trace.traceParam(this, "init", "gravityOn", gravityOn);
        final Simulator simulator = new Simulator(simulatorProperties);
        applyVisualChanges(simulator, viewerProperties);
        setGravityOn(gravityOn);
        try {
            if (getParameter("xtheta") != null) {
                setXtheta(IoUtility.parseDouble(getParameter("xtheta")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getParameter("ytheta") != null) {
                setYtheta(IoUtility.parseDouble(getParameter("ytheta")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
     }

}


