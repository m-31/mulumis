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
 * Circle around the star field and look to zero point.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class CirclularMoverWithChangingViewingDirection extends CircularMover {

    public CirclularMoverWithChangingViewingDirection(final double[] zero) {
        super(zero);
    }

    public final void calculateMovement(final ViewPoint viewPoint) {
        // new viewpoint
        super.calculateMovement(viewPoint);
        final double[] z = viewPoint.getZ();
        z[0] = -Math.sin(getAlpha());
        z[1] = -Math.cos(getAlpha());
        z[2] = 0;
        final double[] y = viewPoint.getY();
        y[0] = 0;
        y[1] = 0;
        y[2] = 1;
        // cross product
        final double[] x = viewPoint.getX();
        x[0] =   z[1]*y[2] - z[2]*y[1];
        x[1] = -(z[0]*y[2] - z[2]*y[0]);
        x[2] =   z[0]*y[1] - z[1]*y[0];
    }

}


