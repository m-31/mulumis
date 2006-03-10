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

import com.meyling.mulumis.base.common.Field;
import com.meyling.mulumis.base.common.GravityObject;
import com.meyling.mulumis.base.util.CalculatorUtility;



/**
 * Star field.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class StarField implements Field  {

    private final Star[] star;

    /** Total mass of star field. */
    private double mass;

    /** Total impulse of star field. */
    private double[] impulse;

    private static final double[] ZERO = new double[GravityObject.DIMENSION];

    /** Last calculated current total impulse. */
    private double[] currentImpulse;

    /**
     * Constructor.
     *
     * @param   numberOfStars   Number of stars.
     */
    public StarField(final int numberOfStars) {
        star = new Star[numberOfStars];
        impulse = new double[GravityObject.DIMENSION];
        currentImpulse = new double[GravityObject.DIMENSION];
    }

    public void fillBall() {
        fillBall(0.5, ZERO);
    }

    public void fillBall(final double radius) {
        fillBall(radius, ZERO);
    }

    public void fillBall(final double radius, double[] zero) {
        final double radiusSquare = radius * radius;
        mass = 0;
        for (int i = 0; i < star.length; i++) {
            final double[] position = new double[GravityObject.DIMENSION];
            do {
                for (int j = 0; j < GravityObject.DIMENSION; j++) {
                    position[j] = 2.0d * radius * Math.random() - radius + zero[j];
                }
            } while (CalculatorUtility.distanceSquare(position, zero) > radiusSquare);
            star[i] = new Star(1, position);
            mass += 1;
        }
        for (int k = 0; k < GravityObject.DIMENSION; k++) {
            impulse[k] = 0;
        }
    }

    public void fillSquare(final double radius) {
        fillSquare(radius, ZERO);
    }

    public void fillSquare() {
        fillSquare(0.5, ZERO);
    }

    public void fillSquare(final double radius, double[] zero) {
        mass = 0;
        for (int i = 0; i < star.length; i++) {
            final double[] position = new double[GravityObject.DIMENSION];
            for (int j = 0; j < GravityObject.DIMENSION; j++) {
                position[j] = 2.0d * radius * Math.random() - radius + zero[j];
            }
            star[i] = new Star(1, position);
            mass += 1;
        }
        for (int k = 0; k < GravityObject.DIMENSION; k++) {
            impulse[k] = 0;
        }
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.stars.Field#getNumberOfStars()
     */
    public final int getNumberOfStars() {
        return star.length;
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.stars.Field#getMass()
     */
    public final double getMass() {
        return mass;
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.stars.Field#getStar(int)
     */
    public final GravityObject getStar(int i) {
        return star[i];
    }

    /**
     * Get total impulse of star field. This is the initial value and could be different from
     * an newly calculated value.
     *
     * @return  Total impulse.
     */
    public double[] getInitialImpulse() {
        return impulse;
    }

    /**
     * Get total impulse of star field. This is the current value and could be different from
     * the initial value.
     *
     * @return  Total impulse.
     */
    public double[] getCurrentImpulse() {
        for (int k = 0; k < GravityObject.DIMENSION; k++) {
            currentImpulse[k] = 0;
            for (int j = 0; j < getNumberOfStars(); j++) {
                currentImpulse[k] += getStar(j).getMass() * getStar(j).getVelocity()[k];
            }
        }
        return currentImpulse;
    }

}


