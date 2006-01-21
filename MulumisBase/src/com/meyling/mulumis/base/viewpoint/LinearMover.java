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

import com.meyling.mulumis.base.util.CalculatorUtility;



/**
 * Simulates star field.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class LinearMover extends AbstractAutomaticMover {

    public LinearMover(final double[] zero) {
        super(zero);
    }

    public final void calculateMovement(final ViewPoint viewPoint) {
        // new viewpoint
        final double[] position = viewPoint.getPosition();
        final double[] zero = getZero();

        if (position[0] < 2) {
            final double distance = CalculatorUtility.distance(position, zero);
//            final double distanceSquare = CalculatorUtility.distanceSquare(pos, zero);
            final double d;
//            if (3 * delta * (distanceSquare + 1) > distance) {
                d = getDelta() * (distance + 1);
//            } else {
//                d = delta * (distanceSquare + 1);
//            }
            position[0] = position[0] + d;
            position[1] = position[1] + d;
            position[2] = position[2] + d;
        } else {
            position[0] = zero[0] - getRadius();
            position[1] = zero[1] - getRadius();
            position[2] = zero[2] - getRadius();
        }
    }

}


