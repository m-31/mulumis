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

package com.meyling.mulumis.base.viewpoint;



/**
 * Makes a linear star field movement.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class LinearMover extends AbstractAutomaticMover {

    public LinearMover(final double[] zero) {
        super(zero);
    }

    public final void calculateMovement(final ViewPoint viewPoint) {
        final double[] x = viewPoint.getX();
        x[0] = 1;
        x[1] = 0;
        x[2] = 0;
        final double[] y = viewPoint.getY();
        y[0] = 0;
        y[1] = 1;
        y[2] = 0;
        final double[] z = viewPoint.getZ();
        z[0] = 0;
        z[1] = 0;
        z[2] = 1;
        final double[] position = viewPoint.getPosition();
        final double[] zero = getZero();
        double d;
        d = getDelta() * (getRadius() + 1);
        if (getRadius() - d < 0) {
            setRadius(10);
        }
        position[0] = zero[0];
        position[1] = zero[1];
        position[2] = zero[2] - getRadius() - d;
        setRadius(getRadius() - d);
    }

}


