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

package com.meyling.mulumis.base.simulator;



import com.meyling.mulumis.base.stars.StarField;
import com.meyling.mulumis.base.viewpoint.ViewPoint;

/**
 * Star field simulator.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class Camera {

    private PhotoPlate photoPlate;

    private ViewPoint viewPoint;

    public Camera(final PhotoPlate photoPlate, final ViewPoint viewPoint) {
        this.photoPlate = photoPlate;
        this.viewPoint = viewPoint;
    }

    public final void takePicture(final StarField field) {
        photoPlate.generateImage(field);
    }

    public final double getZoom() {
        return photoPlate.getZoom();
    }

    public final void setZoom(final double zoom) {
        photoPlate.setZoom(zoom);
    }

    public final double getSensitivity() {
        return photoPlate.getSensitivity();
    }

    public final void setSensitivity(final double sensitivity) {
        photoPlate.setSensitivity(sensitivity);
    }

    public final void setSnapshot(final int snapshot) {
        photoPlate.setSnapshot(snapshot);
    }
}

