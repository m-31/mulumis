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

package com.meyling.mulumis.base.simulator;

/**
 * Star field simulator properties.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class SimulatorProperties {

    private int stars;
    private double gamma;
    private double deltat;

    public SimulatorProperties(final int stars, 
            final double gamma, final double deltat) {
        setStars(stars);
        setGamma(gamma);
        setDeltat(deltat);
    }

    public SimulatorProperties() {
        setStars(200);
        setGamma(0.1);
        setDeltat(0.0002);
    }

    public final int getStars() {
        return stars;
    }

    public final void setStars(int stars) {
        this.stars = stars;
    }

    public final double getGamma() {
        return gamma;
    }

    public final void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public final double getDeltat() {
        return deltat;
    }

    public final void setDeltat(double deltat) {
        this.deltat = deltat;
    }


}

