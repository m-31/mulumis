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

import com.meyling.mulumis.base.util.CalculatorUtility;



/**
 * Star field.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class StarField  {

    private final Star[] star;

    private final double[] zero = new double[GravityObject.DIMENSION];

    public StarField(final int numberOfStars) {
        star = new Star[numberOfStars];
    }

    public void fillBall() {
        fillBall(0.5, zero);
    }

    public void fillBall(final double radius) {
        fillBall(radius, zero);
    }

    public void fillBall(final double radius, double[] zero) {
        final double radiusSquare = radius * radius;
        for (int i = 0; i < star.length; i++){
            final double[] position = new double[GravityObject.DIMENSION];
            do {
                for (int j = 0; j < GravityObject.DIMENSION; j++) {
                    position[j] = 2.0d * radius * Math.random() - radius + zero[j];
                }
            } while (CalculatorUtility.distanceSquare(position, zero) > radiusSquare);
            star[i] = new Star(1, position);
        }
    }

    public void fillSquare(final double radius) {
        fillSquare(radius, zero);
    }

    public void fillSquare() {
        fillSquare(0.5, zero);
    }

    public void fillSquare(final double radius, double[] zero) {
        for (int i = 0; i < star.length; i++){
            final double[] position = new double[GravityObject.DIMENSION];
            for (int j = 0; j < GravityObject.DIMENSION; j++) {
                position[j] = 2.0d * radius * Math.random() - radius + zero[j];
            }
            star[i] = new Star(1, position);
        }
    }

    /**
     * Returns the number of stars.
     *
     * @return    Number of stars.
     */
    public final int getNumberOfStars() {
        return star.length;
    }

    /**
     * Returns the requested star.
     *
     * @param    i    Star number. Between 0 and <code>{@link #getNumberOfStars()} - 1</code>.
     * @return    Requested star.
     */
    public final Star getStar(int i) {
        return star[i];
    }

}


