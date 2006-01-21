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
 * Calculates the observer viewpoint.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public abstract class AbstractAutomaticMover implements ViewPointMover  {

    private double delta = 0.001;
    private double radius = 0.25;
    private final double[] zero;

    /**
     * Constructor.
     *
     * @param    zero    Observer reference vector. For example the center of gravity.
     */
    public AbstractAutomaticMover(final double[] zero) {
        this.zero = zero;
    }

    public abstract void calculateMovement(final ViewPoint viewPoint);

    /**
     * @return Returns the delta.
     */
    public final double getDelta() {
        return delta;
    }

    /**
     * @param delta The delta to set.
     */
    public final void setDelta(double delta) {
        this.delta = delta;
    }

    /**
     * @return Returns the radius.
     */
    public final double getRadius() {
        return radius;
    }

    /**
     * @param radius The radius to set.
     */
    public final void setRadius(double radius) {
        this.radius = radius;
    }

    public final double[] getZero() {
        return zero;
    }

}


