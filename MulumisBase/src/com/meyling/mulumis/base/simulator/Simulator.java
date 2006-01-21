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


import java.awt.Graphics;

import com.meyling.mulumis.base.stars.GravityObject;
import com.meyling.mulumis.base.stars.StarField;
import com.meyling.mulumis.base.viewpoint.CircularMover;
import com.meyling.mulumis.base.viewpoint.CirclularMoverWithChangingViewingDirection;
import com.meyling.mulumis.base.viewpoint.LinearMover;
import com.meyling.mulumis.base.viewpoint.ManualMovement;
import com.meyling.mulumis.base.viewpoint.AbstractAutomaticMover;
import com.meyling.mulumis.base.viewpoint.ViewPoint;

/**
 * Simulates star field.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class Simulator {

    private PhotoPlate photoPlate;

    private ViewPoint viewPoint;

    private AbstractAutomaticMover positionCalculator;

    public Simulator(final int stars, final String movement,
            final double delta, final double sensitivity, final double radius, final double zoom) {
        final StarField field = new StarField(stars);
        final double[] zero = new double[GravityObject.DIMENSION];
        field.fillBall(0.5, zero);
        photoPlate = new PhotoPlate(field);

        viewPoint = new ViewPoint();
        if (movement.equals("manual")) {
            positionCalculator = new ManualMovement(zero);
        } else if (movement.equals("linear")) {
            positionCalculator = new LinearMover(zero);
        } else if (movement.equals("circularNormale")) {
            positionCalculator = new CirclularMoverWithChangingViewingDirection(zero);
        } else if (movement.equals("circular")){
            positionCalculator = new CircularMover(zero);
        } else {
            throw new IllegalArgumentException(
                "Mover unknown. Allowed: \"manual\", \"linear\", \"circular\" or \"circularNormale\"");
        }
        positionCalculator.setDelta(delta);
        positionCalculator.setRadius(radius);
        photoPlate.setSensitivity(sensitivity);
        photoPlate.setZoom(zoom);
        photoPlate.setPosition(viewPoint.getPosition());
        photoPlate.setOrientation(viewPoint.getX(), viewPoint.getY(), viewPoint.getZ());
    }

    public final void paint(Graphics g) {
        if (g != null) {
            photoPlate.paint(g);
        }
    }

    public final void moveViewPoint() {
        positionCalculator.calculateMovement(viewPoint);
    }

    public final AbstractAutomaticMover getPositionCalculator() {
        return positionCalculator;
    }

    public final PhotoPlate getPhotoPlate() {
        return photoPlate;
    }

}

