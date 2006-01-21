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



/**
 * Star with mass, position and velocity.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class Star implements GravityObject  {

    private final double mass;

    private final double[] position;

    private final double[] velocity;


    public Star(final double mass, final double[] position, final double[] velocity) {
        if (position == null) {
            throw new NullPointerException("position is null");
        }
        if (position.length != DIMENSION) {
            throw new IllegalArgumentException("position has not dimension " + DIMENSION);
        }
        if (velocity == null) {
            throw new NullPointerException("velocity is null");
        }
        if (velocity.length != DIMENSION) {
            throw new IllegalArgumentException("velocity has not dimension " + DIMENSION);
        }
        this.mass = mass;
        this.position = new double[GravityObject.DIMENSION];
        System.arraycopy(position, 0, this.position, 0, GravityObject.DIMENSION);
        this.velocity = new double[GravityObject.DIMENSION];
        System.arraycopy(velocity, 0, this.velocity, 0, GravityObject.DIMENSION);
    }

    /**
     * Constructor.
     *
     * @param   mass        Mass of star.
     * @param   position    Position of star.
     */
    public Star(final double mass, final double[] position) {
        this(mass, position, new double[DIMENSION]);
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


