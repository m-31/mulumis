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



import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.stars.GravityObject;
import com.meyling.mulumis.base.stars.StarField;
import com.meyling.mulumis.base.util.CalculatorUtility;

/**
 * Star field simulator.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class GravityEngine {

    private final double gamma;
    private final double deltat;
    private double[][] vn;
    private double[] impulse;

    public GravityEngine(final double gamma, double deltat) {
        this.gamma = gamma;
        this.deltat = deltat;
        impulse = new double[GravityObject.DIMENSION];
    }

    public synchronized final void calculate(final StarField field) {
        if (!hasGravity()) {
            return;
        }
        if (vn == null || field.getNumberOfStars() != vn.length / GravityObject.DIMENSION) {
            vn = new double[field.getNumberOfStars()][GravityObject.DIMENSION];
        }

        // for each star calculating the new position
        for (int k = 0; k < GravityObject.DIMENSION; k++) {
            impulse[k] = 0;
        }
        for (int i = 0; i < field.getNumberOfStars(); i++) {
            for (int k = 0; k < GravityObject.DIMENSION; k++) {
                vn[i][k] = field.getStar(i).getVelocity()[k];
                for (int j = 0; j < field.getNumberOfStars(); j++) {
                    if (i == j) {
                        continue;
                    }
                    final double r = CalculatorUtility.distance(field.getStar(i).getPosition(),
                        field.getStar(j).getPosition());
                    if (r < 0.0001) {
                        // build cluster from both stars
                        System.out.println("Contact");
                    } else if (r < 0.0003) {
                        // the stars are so close together, that dt must be smaller to reduce the calculation error
                        System.out.println("Close together");
                    }
                    vn[i][k] -= deltat * gamma * field.getStar(j).getMass()
                       * (field.getStar(i).getPosition()[k] - field.getStar(j).getPosition()[k]) / r / r / r;
                }
                impulse[k] += field.getStar(i).getMass() * vn[i][k];
                if (Double.isNaN(impulse[k])) {
                    Trace.traceParam(this, "calculate", "vn[i][k]", vn[i][k]);
                }
            }
        }

        // Doing some kind of renormation to correct calculation errors. As example a primitive impulse renormation if the complete impuls was initially  = 0:
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < field.getNumberOfStars(); i++) {
//                double v = impulse[k] / field.getMass() / field.getMass() * field.getStar(i).getMass();
//                double v = impulse[k] / field.getMass();
                double v = 0;
                if (Double.isNaN(v)) {
                    Trace.traceParam(this, "calculate", "impulse[k]", impulse[k]);
                    Trace.traceParam(this, "calculate", "field.getMass()", field.getMass());
                    Trace.trace(this, "calculate", "NaN");
                }
                vn[i][k] -= v;
            }
        }

        // for each star calculating the new position
        for (int k = 0; k < GravityObject.DIMENSION; k++) {
            impulse[k] = 0;
        }
        for (int i = 0; i < field.getNumberOfStars(); i++) {
            for (int k = 0; k < GravityObject.DIMENSION; k++) {
                field.getStar(i).getVelocity()[k] = vn[i][k];
                impulse[k] += field.getStar(i).getMass() * vn[i][k];
                field.getStar(i).getPosition()[k] = field.getStar(i).getPosition()[k]
                    + deltat * vn[i][k];
            }
        }

    }

    public final double getGamma() {
        return gamma;
    }

    public final double getDeltat() {
        return deltat;
    }

    public boolean hasGravity() {
        if (gamma == 0 || deltat == 0) {
            return false;
        }
        return true;
    }
    
    public synchronized double[] getImpulse() {
        return impulse;
    }

}

