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



import com.meyling.mulumis.base.common.Field;
import com.meyling.mulumis.base.common.Gravity;
import com.meyling.mulumis.base.common.GravityObject;
import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.util.CalculatorUtility;

/**
 * Gravity engine.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class GravityEngine implements Gravity {

    /* Gravity constant. */
    private double gamma;

    /* Delta t, small time interval. */
    private double deltat;

    /** Temporary variable to save velocities. */
    private double[][] vn;

    /** Total impulse. */
    private double[] impulse;

    /** Star field to work on. */
    private Field field;

    public GravityEngine() {
        impulse = new double[GravityObject.DIMENSION];
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.simulator.Gravity#init(com.meyling.mulumis.base.stars.Field)
     */
    public final void init(final Field field) {
        this.field = field;
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.simulator.Gravity#calculate()
     */
    public final synchronized void calculate() {
        if (vn == null || field.getNumberOfStars() != vn.length / GravityObject.DIMENSION) {
            vn = new double[field.getNumberOfStars()][GravityObject.DIMENSION];
        }


        // for each star calculating the new position

        // set total impulse to zero
        for (int k = 0; k < GravityObject.DIMENSION; k++) {
            impulse[k] = 0;
            for (int i = 0; i < field.getNumberOfStars(); i++) {
                vn[i][k] = field.getStar(i).getVelocity()[k];
            }
        }
        for (int i = 0; i < field.getNumberOfStars(); i++) {
            for (int k = 0; k < GravityObject.DIMENSION; k++) {
                for (int j = 0; j < i; j++) {
                    final double r = CalculatorUtility.distance(field.getStar(i).getPosition(),
                        field.getStar(j).getPosition());
                    if (r < 0.0001) {
                        // TODO mime 20060209: build cluster from both stars
                        System.out.println("Contact");
                    } else if (r < 0.0003) {
                        // TODO mime 20060209: the stars are so close together, that dt must
                        //      be smaller to reduce the calculation error
                        System.out.println("Close together");
                    }
                    final double a = deltat * gamma // TODO mime 20060307: extract deltat * gamma
                        * (field.getStar(i).getPosition()[k] - field.getStar(j).getPosition()[k])
                        / r / r / r;
                    vn[i][k] -= a * field.getStar(j).getMass();
                    vn[j][k] += a * field.getStar(i).getMass();
                }
                impulse[k] += field.getStar(i).getMass() * vn[i][k];
                if (Double.isNaN(impulse[k])) {
                    // TODO mime 20060209: what to do?
                    Trace.traceParam(this, "calculate", "vn[i][k]", vn[i][k]);
                }
            }
        }
/*
// TODO mime 20060202: not working
        // Doing some kind of renormation to correct calculation errors.
        // As example a primitive impulse renormation if the complete impuls was initially  = 0:
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < field.getNumberOfStars(); i++) {
//                double v = impulse[k] / field.getMass() / field.getMass()
//                    * field.getStar(i).getMass();
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
*/
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

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.simulator.Gravity#setDeltat()
     */
    public final void setDeltat(final double deltat) {
        this.deltat = deltat;
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.simulator.Gravity#getDeltat()
     */
    public final double getDeltat() {
        return deltat;
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.simulator.Gravity#setGamma()
     */
    public final void setGamma(double gamma) {
       this.gamma = gamma;
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.simulator.Gravity#getGamma()
     */
    public final double getGamma() {
        return gamma;
    }

    /* (non-Javadoc)
     * @see com.meyling.mulumis.base.simulator.Gravity#getImpulse()
     */
    public synchronized double[] getImpulse() {
        return impulse;
    }


}

