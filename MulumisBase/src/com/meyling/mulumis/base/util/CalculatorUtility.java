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

package com.meyling.mulumis.base.util;



/**
 * Contains static methods for calculating with vectors.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class CalculatorUtility  {

    /**
     * Hidden constructor.
     */
    private CalculatorUtility() {
        // nothing to do
    }

    public static final double distanceSquare(double[] a, double[] b) {
        return (a[0]- b[0])*(a[0]- b[0]) + (a[1]- b[1])*(a[1]- b[1])
            + (a[2]- b[2])*(a[2]- b[2]);
    }

    public static final double distance(double[] a, double[] b) {
        return Math.sqrt((a[0]- b[0])*(a[0]- b[0]) + (a[1]- b[1])*(a[1]- b[1])
            + (a[2]- b[2])*(a[2]- b[2]));
    }

    public static final double len(double[] a) {
        return Math.sqrt(a[0]*a[0] + a[1]*a[1] + a[2]*a[2]);
    }

    public static final double scalar(double[] a, double[] b) {
        return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
    }

    public static final double minusscalar(double[] a, double[] b, double[] c) {
        return (a[0]-b[0])*c[0] + (a[1]-b[1])*c[1] + (a[2]-b[2])*c[2];
    }

}


