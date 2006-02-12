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

package com.meyling.mulumis.base.stars;

import java.util.ArrayList;
import java.util.List;

import com.meyling.mulumis.base.common.GravityObject;



/**
 * Cluster of stars with mass, position and velocity.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class Cluster implements GravityObject {

    private double mass;

    private final double[] position;

    private final double[] velocity;

    private final List star;

    public Cluster(final Star star) {
        this.mass = star.getMass();
        this.position = new double[GravityObject.DIMENSION];
        this.velocity = new double[GravityObject.DIMENSION];
        this.star = new ArrayList();
        this.star.add(star);
        System.arraycopy(star.getPosition(), 0, position, 0, GravityObject.DIMENSION);
        System.arraycopy(star.getVelocity(), 0, velocity, 0, GravityObject.DIMENSION);
    }

    public final void add(final Star star) {
        for (int i = 0; i < GravityObject.DIMENSION; i++) {
            position[i] = (mass * position[i] + star.getMass() * star.getPosition()[i]) / (mass + star.getMass());
        }
        for (int i = 0; i < GravityObject.DIMENSION; i++) {
            velocity[i] = (mass * velocity[i] + star.getMass() * star.getVelocity()[i]) / (mass + star.getMass());
        }
        this.star.add(star);
        mass += star.getMass();
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.common.GravityObject#getMass()
     */
    public final double getMass() {
        return mass;
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.common.GravityObject#getPosition()
     */
    public final double[] getPosition() {
        return position;
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.common.GravityObject#getVelocity()
     */
    public final double[] getVelocity() {
        return velocity;
    }

}


