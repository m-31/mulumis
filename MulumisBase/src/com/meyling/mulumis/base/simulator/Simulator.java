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


import java.awt.Component;
import java.awt.Graphics;

import com.meyling.mulumis.base.common.Field;
import com.meyling.mulumis.base.common.Gravity;
import com.meyling.mulumis.base.common.GravityObject;
import com.meyling.mulumis.base.config.MulumisContext;
import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.stars.StarField;
import com.meyling.mulumis.base.util.CalculatorUtility;
import com.meyling.mulumis.base.viewpoint.AbstractAutomaticMover;
import com.meyling.mulumis.base.viewpoint.CirclularMoverWithChangingViewingDirection;
import com.meyling.mulumis.base.viewpoint.CircularMover;
import com.meyling.mulumis.base.viewpoint.LinearMover;
import com.meyling.mulumis.base.viewpoint.ManualMovement;
import com.meyling.mulumis.base.viewpoint.ViewPoint;

/**
 * Star field simulator.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class Simulator {

    private Field field;

    private AbstractAutomaticMover positionCalculator;

    private String movement;

    private int stars;

    private Camera camera;

    private Gravity engine;

    public Simulator(final int stars, final String movement,
            final double delta, final double sensitivity, final double radius, final double zoom,
            final int snapshot, final double gamma, final double deltat,
            final int width, final int height, final Component parent) {
        this.stars = stars;
        field = new StarField(stars);
        final double[] zero = new double[GravityObject.DIMENSION];
        ((StarField) field).fillBall(0.5, zero);
        final PhotoPlate photoPlate = new PhotoPlate();
        final ViewPoint viewPoint = new ViewPoint();
        if (movement == null) {
            throw new NullPointerException("movement is null");
        }
        if (movement.equals("manual") || movement.equals("manualDelay")) {
            positionCalculator = new ManualMovement(zero);
        } else if (movement.equals("linear")) {
            positionCalculator = new LinearMover(zero);
        } else if (movement.equals("circularNormale")) {
            positionCalculator = new CirclularMoverWithChangingViewingDirection(zero);
        } else if (movement.equals("circular")){
            positionCalculator = new CircularMover(zero);
        } else {
            throw new IllegalArgumentException(
                "Mover unknown. Allowed: \"manual\", \"linear\", \"circular\" or \"circularNormale\"");
        }
        this.movement = movement;
        positionCalculator.setDelta(delta);
        positionCalculator.setRadius(radius);
        photoPlate.setSensitivity(sensitivity);
        photoPlate.setZoom(zoom);
        photoPlate.setSnapshot(snapshot);
        photoPlate.init(width, height, parent);
        camera = new Camera(photoPlate, viewPoint);
        engine = MulumisContext.getAbstractGravityFactory().createGravity();
        engine.setGamma(gamma);
        engine.setDeltat(deltat);
    }

    public Simulator(final SimulatorProperties properties, final int width, final int height,
            final Component parent) {
        this(properties.getStars(), properties.getMovement(), properties.getDelta(),
            properties.getSensitivity(), properties.getRadius(), properties.getZoom(),
            properties.getSnapshot(), properties.getGamma(), properties.getDeltat(),
            width, height, parent);
    }

    public final SimulatorProperties getProperties() {
        return new SimulatorProperties(stars, movement, positionCalculator.getDelta(),
            camera.getSensitivity(), positionCalculator.getRadius(), camera.getZoom(),
            camera.getSnapshot(), engine.getGamma(), engine.getDeltat());
    }

    public final void applyVisualChanges(final SimulatorProperties properties) {
        applyVisualChanges(properties.getMovement(), properties.getDelta(),
        properties.getSensitivity(), properties.getRadius(), properties.getZoom(),
        properties.getSnapshot());
        moveViewPoint();
    }
    
    public final void applyVisualChanges(final String movement,
            final double delta, final double sensitivity, final double radius, final double zoom,
            final int snapshot) {
        if (!this.movement.equals(movement)) {
            final double[] zero = new double[GravityObject.DIMENSION];
            if (movement == null) {
                throw new NullPointerException("movement is null");
            }
            if (movement.equals("manual") || movement.equals("manualDelay")) {
                positionCalculator = new ManualMovement(zero);
            } else if (movement.equals("linear")) {
                positionCalculator = new LinearMover(zero);
            } else if (movement.equals("circularNormale")) {
                positionCalculator = new CirclularMoverWithChangingViewingDirection(zero);
            } else if (movement.equals("circular")){
                positionCalculator = new CircularMover(zero);
            } else {
                throw new IllegalArgumentException(
                    "Mover unknown. Allowed: \"manual\", \"linear\", \"circular\" or \"circularNormale\"");
            }
            this.movement = movement;
        }
        positionCalculator.setDelta(delta);
        positionCalculator.setRadius(radius);
        camera.setSensitivity(sensitivity);
        camera.setZoom(zoom);
        camera.setSnapshot(snapshot);
    }

    public final void paintPicture(Graphics g) {
        if (g != null) {
            camera.paint(g);
        }
    }

    public final void moveViewPoint() {
        positionCalculator.calculateMovement(camera.getViewPoint());
    }

    public final void applyGravity() {
        engine.calculate(field);
    }

    public final AbstractAutomaticMover getPositionCalculator() {
        return positionCalculator;
    }

    public final void takePicture() {
        camera.takePicture(field);
    }

    public final Camera getCamera() {
        return camera;
    }

    public final boolean hasGravity() {
        return engine.hasGravity();
    }

    public final double getImpulse() {
        Trace.traceParam(this, "getImpulse", "impulse[0]", engine.getImpulse()[0]);
        Trace.traceParam(this, "getImpulse", "impulse[1]", engine.getImpulse()[1]);
        Trace.traceParam(this, "getImpulse", "impulse[2]", engine.getImpulse()[2]);
        Trace.traceParam(this, "getImpulse", "impulse", CalculatorUtility.len(engine.getImpulse()));
        return CalculatorUtility.len(engine.getImpulse());
    }

}

