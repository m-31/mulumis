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
 * Move viewpoint according to two angles.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public class ManualMovement extends AbstractAutomaticMover {

    private double xtheta = 0;

    private double ytheta = 0;

    public ManualMovement(final double[] zero) {
        super(zero);
    }

    public void calculateMovement(final ViewPoint viewPoint) {
        final double[] x = viewPoint.getX();
        final double[] y = viewPoint.getY();
        final double[] z = viewPoint.getZ();

        // rotate around x axis
        rotate(xtheta, x, z);

        // rotate around y axis
        rotate(ytheta, y, z);

        final double[] position = viewPoint.getPosition();
        final double[] zero = getZero();
        position[0] = -z[0] * getRadius() + zero[0];
        position[1] = -z[1] * getRadius() + zero[1];
        position[2] = -z[2] * getRadius() + zero[2];

    }

    private void rotate(double ztheta, final double[] x, final double[] y) {
        // rotate around z axis
        double ct = Math.cos(ztheta);
        double st = Math.sin(ztheta);

        double[] ny = new double[3];
        for (int i = 0; i < 3; i++) {
            ny[i] = x[i] * ct + y[i] * st;
        }
        for (int i = 0; i < 3; i++) {
            y[i] = y[i] * ct - x[i] * st;
        }
        for (int i = 0; i < 3; i++) {
            x[i] = ny[i];
        }
    }

    /**
     * @param xtheta The xtheta to set.
     */
    public final void setXtheta(double xtheta) {
        this.xtheta = xtheta;
    }

    /**
     * @param ytheta The ytheta to set.
     */
    public final void setYtheta(double ytheta) {
        this.ytheta = ytheta;
    }

}


