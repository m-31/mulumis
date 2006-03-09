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

package com.meyling.mulumis.base.view;


import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.meyling.mulumis.base.common.GravityObject;
import com.meyling.mulumis.base.simulator.Simulator;
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
public final class Viewer {

    private Simulator simulator;
    
    private AbstractAutomaticMover positionCalculator;

    private String movement;

    private Camera camera;
    
    private boolean gravityOn;

    private final List listener = new ArrayList();;

    public Viewer(final Viewer viewer, final int width, final int height, final Component parent) {
        this.simulator = viewer.simulator;
        this.movement = viewer.movement;
        this.positionCalculator = viewer.positionCalculator;
        final PhotoPlate photoPlate = new PhotoPlate();
        final ViewPoint viewPoint = viewer.camera.getViewPoint();
        photoPlate.setSensitivity(viewer.camera.getPhotoPlate().getSensitivity());
        photoPlate.setZoom(viewer.camera.getPhotoPlate().getZoom());
        photoPlate.setSnapshot(viewer.camera.getPhotoPlate().getSnapshot());
        photoPlate.init(width, height, parent);
        camera = new Camera(photoPlate, viewPoint);
        this.gravityOn = viewer.gravityOn;
    }
    
    public Viewer(final Simulator simulator, final String movement, final double delta, final double sensitivity, 
            final double radius, final double zoom, final int snapshot,
            final int width, final int height, final Component parent) {
        this.simulator = simulator;
        final double[] zero = new double[GravityObject.DIMENSION];
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
    }

    public Viewer(final Simulator simulator, final CameraAttributes properties, final int width, final int height,
            final Component parent) {
        this(simulator, properties.getMovement(), properties.getDelta(), properties.getSensitivity(), 
            properties.getRadius(), properties.getZoom(), properties.getSnapshot(),
            width, height, parent);
    }

    public final void resize(final int width, final int height, final Component parent) {
        camera.getPhotoPlate().init(width, height, parent);
    }
    
    public final void addViewChangedListener(final ViewChangedListener list) {
        listener.add(list);
    }
    
    public final void removeViewChangedListener(final ViewChangedListener list) {
        listener.remove(list);
    }
    
    public final void close() {
        listener.clear();
        simulator = null;
        camera = null;
        positionCalculator = null;
    }
    
    public final CameraAttributes getProperties() {
        return new CameraAttributes(movement, positionCalculator.getDelta(),
            camera.getSensitivity(), positionCalculator.getRadius(), camera.getZoom(),
            camera.getSnapshot());
    }

    public final void applyVisualChanges(final Simulator simulator, final CameraAttributes properties) {
        applyVisualChanges(simulator, properties.getMovement(), properties.getDelta(),
        properties.getSensitivity(), properties.getRadius(), properties.getZoom(),
        properties.getSnapshot());
    }
    
    public final void applyVisualChanges(final Simulator simulator, final String movement,
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
        this.simulator = simulator;
// TODO remove?        moveViewPoint();
        takePicture();
    }

    public final void paintPicture(Graphics g) {
        if (g != null) {
            camera.paint(g);
        }
    }

    /**
     * Move viewpoint and apply gravity (if gravity is on).
     */
    public final void move() {
        positionCalculator.calculateMovement(camera.getViewPoint());
        // here the simulator is explicitly called
        // this is done to synchronize the view with the calculation
        if (isGravityOn()) {
            simulator.applyGravity();
        }
    }

    public final AbstractAutomaticMover getPositionCalculator() {
        return positionCalculator;
    }

    public final void takePicture() {
        camera.takePicture(simulator.getField());
    }

    public boolean isGravityOn() {
        return gravityOn;
    }

    public void setGravityOn(boolean gravityOn) {
        this.gravityOn = gravityOn;
    }

    public final void setDelta(final double delta) {
        positionCalculator.setDelta(delta);
    }
    
    public final void setRadius(final double radius) {
        positionCalculator.setRadius(radius);
        for (int i = 0; i < listener.size(); i++) {
            ((ViewChangedListener) listener.get(i)).radiusChanged();
        }
    }
    
    public final double getRadius() {
        return positionCalculator.getRadius();
    }
    
    public final void setSensitivity(final double sensitivity) {
        camera.setSensitivity(sensitivity);
        for (int i = 0; i < listener.size(); i++) {
            ((ViewChangedListener) listener.get(i)).sensitivityChanged();
        }
    }
    
    public final double getSensitivity() {
        return camera.getSensitivity();
    }

    public final void setZoom(final double zoom) {
        camera.setZoom(zoom);
        for (int i = 0; i < listener.size(); i++) {
            ((ViewChangedListener) listener.get(i)).zoomChanged();
        }
    } 
   
    public final double getZoom() {
        return camera.getZoom();
    }
    
    public final void setSnapshot(final int snapshot) {
        camera.setSnapshot(snapshot);
        for (int i = 0; i < listener.size(); i++) {
            ((ViewChangedListener) listener.get(i)).snapshotChanged();
        }
    }

    public final String getMovement() {
        return movement;
    }
    
    public void removeAllViewChangedListeners() {
        listener.clear();
        
    }
    
}

