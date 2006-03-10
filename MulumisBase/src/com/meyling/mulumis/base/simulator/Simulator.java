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
import com.meyling.mulumis.base.config.MulumisContext;
import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.stars.StarField;
import com.meyling.mulumis.base.util.CalculatorUtility;

/**
 * Star field simulator.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class Simulator {

    private Field field;

    private Gravity engine;

    private Thread thread;

    private Boolean stopped = Boolean.TRUE;


    public Simulator(final int stars, final double gamma, final double deltat) {
        engine = MulumisContext.getAbstractGravityFactory().createGravity();
        engine.setGamma(gamma);
        engine.setDeltat(deltat);
        createField(stars);
    }

    /**
     * Create new star field.
     *
     * @param   stars   Number of stars.
     */
    public void createField(final int stars) {
        field = null;
        field = new StarField(stars);
        final double[] zero = new double[GravityObject.DIMENSION];
        ((StarField) field).fillBall(0.5, zero);
        engine.init(field);
    }

    public Simulator(final SimulatorAttributes properties) {
        this(properties.getStars(), properties.getGamma(), properties.getDeltat());
    }

    public final SimulatorAttributes getProperties() {
        return new SimulatorAttributes(field.getNumberOfStars(), engine.getGamma(),
            engine.getDeltat());
    }

    public final void applyChanges(final SimulatorAttributes properties) {
        synchronized (stopped) {
            if (properties.getStars() != field.getNumberOfStars()) {
                createField(properties.getStars());
            }
            engine.setGamma(properties.getGamma());
            engine.setDeltat(properties.getDeltat());
        }
    }

    public final void applyGravity() {
        engine.calculate();
    }

    public final Field getField() {
        return field;
    }

    public final double getImpulse() {
        Trace.traceParam(this, "getImpulse", "impulse[0]", engine.getImpulse()[0]);
        Trace.traceParam(this, "getImpulse", "impulse[1]", engine.getImpulse()[1]);
        Trace.traceParam(this, "getImpulse", "impulse[2]", engine.getImpulse()[2]);
        Trace.traceParam(this, "getImpulse", "impulse", CalculatorUtility.len(engine.getImpulse()));
        return CalculatorUtility.len(engine.getImpulse());
    }

    public final synchronized void start() {
        thread = new Thread() {
            public void run() {
                synchronized (stopped) {
                    stopped = Boolean.FALSE;
                }
                while (thread != null) {
                    synchronized (stopped) {
                        applyGravity();
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                synchronized (stopped) {
                    stopped = Boolean.TRUE;
                }
            }
        };
        thread.start();
    }

    public final synchronized void stop() {
        synchronized (stopped) {
            thread = null;
        }
    }
}

