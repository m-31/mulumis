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

import com.meyling.mulumis.base.common.GravityObject;

/**
 * Viewpoint with position and orientation.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class ViewPoint  {

    private final double position[];

    private final double[] x;
    private final double[] y;
    private final double[] z;

    public ViewPoint() {
        this(new double[GravityObject.DIMENSION]);
    }

    public ViewPoint(final double[] position) {
        this.position = position;
        x = new double[GravityObject.DIMENSION];
        x[0] = 1;
        y = new double[GravityObject.DIMENSION];
        y[1] = 1;
        z = new double[GravityObject.DIMENSION];
        z[2] = 1;
    }

    public ViewPoint(final double[] position, final double x[], final double[] y, final double[] z) {
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final double[] getPosition() {
        return position;
    }

    public final double[] getX() {
        return x;
    }

    public final double[] getY() {
        return y;
    }

    public final double[] getZ() {
        return z;
    }

}


