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
    private String movement;
    private double delta;
    private double radius;
    private double sensitivity;
    private double zoom;
    private int snapshot;
    private double gamma;
    private double deltat;

    public SimulatorProperties(final int stars, final String movement,
            final double delta, final double sensitivity, final double radius,
            final double zoom, final int snapshot, final double gamma, final double deltat) {
        setStars(stars);
        setMovement(movement);
        setDelta(delta);
        setRadius(radius);
        setSensitivity(sensitivity);
        setZoom(zoom);
        setSnapshot(snapshot);
        setGamma(gamma);
        setDeltat(deltat);
    }

    public SimulatorProperties() {
        setStars(4000);
        setMovement("manualDelay");
        setDelta(0.001);
        setRadius(4);
        setSensitivity(7);
        setZoom(1000);
        setSnapshot(0);
        setGamma(0);
        setDeltat(0.001);
    }

    public final double getDelta() {
        return delta;
    }

    public final void setDelta(double delta) {
        this.delta = delta;
    }

    public final String getMovement() {
        return movement;
    }

    public final void setMovement(final String movement) {
        if (movement.equals("manual")) {
        } else if (movement.equals("manualDelay")) {
        } else if (movement.equals("linear")) {
        } else if (movement.equals("circularNormale")) {
        } else if (movement.equals("circular")){
        } else {
            throw new IllegalArgumentException(
                "Mover \"" + movement
                + "\" unknown. Allowed: \"manual\", \"manualDelay\", \"linear\", \"circular\" or \"circularNormale\"");
        }
        this.movement = movement;
    }

    public final double getRadius() {
        return radius;
    }

    public final void setRadius(double radius) {
        this.radius = radius;
    }

    public final double getSensitivity() {
        return sensitivity;
    }

    public final void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    public final int getStars() {
        return stars;
    }

    public final void setStars(int stars) {
        this.stars = stars;
    }

    public final double getZoom() {
        return zoom;
    }

    public final void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public int getSnapshot() {
        return snapshot;
    }

    public final void setSnapshot(int snapshot) {
        this.snapshot = snapshot;
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

