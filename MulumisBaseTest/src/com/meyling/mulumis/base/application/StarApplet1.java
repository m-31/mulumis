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

package com.meyling.mulumis.base.application;


import java.applet.Applet;
import java.awt.Graphics;

import com.meyling.mulumis.base.common.GravityObject;
import com.meyling.mulumis.base.stars.StarField;
import com.meyling.mulumis.base.view.PhotoPlate;
import com.meyling.mulumis.base.viewpoint.AbstractAutomaticMover;
import com.meyling.mulumis.base.viewpoint.CirclularMoverWithChangingViewingDirection;
import com.meyling.mulumis.base.viewpoint.CircularMover;
import com.meyling.mulumis.base.viewpoint.LinearMover;
import com.meyling.mulumis.base.viewpoint.ViewPoint;

/**
 * Simulates star field.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class StarApplet1 extends Applet implements Runnable {
    private static final long serialVersionUID = -3554962680447033507L;
    private Thread runThread;
    private PhotoPlate visualizer;
    private ViewPoint viewPoint;
    private AbstractAutomaticMover positionCalculator;
    private boolean circleNormale;
    private boolean linear;
    private StarField field;

    public StarApplet1() {
    }

    public void init() {
        int stars = 40000;
        try {
            if (getParameter("stars") != null) {
                stars = Integer.parseInt(getParameter("stars"));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        field = new StarField(stars);
        final double[] zero = new double[GravityObject.DIMENSION];
        for (int i = 0; i < GravityObject.DIMENSION; i++) {
            zero[i] = 0.0;
        }
        field.fillBall(0.5, zero);
        visualizer = new PhotoPlate();

        try {
            this.circleNormale = false;
            if (getParameter("normale") != null && getParameter("normale").equals("true")) {
                this.circleNormale = true;
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        try {
            this.linear = false;
            if (getParameter("linear") != null && getParameter("linear").equals("true")) {
                this.linear = true;
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        viewPoint = new ViewPoint();
        if (linear) {
            positionCalculator = new LinearMover(zero);
        } else if (circleNormale) {
            positionCalculator = new CirclularMoverWithChangingViewingDirection(zero);
        } else {
            positionCalculator = new CircularMover(zero);
        }
        try {
            if (getParameter("delta") != null) {
                positionCalculator.setDelta(parseDouble(getParameter("delta")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        try {
            if (getParameter("sensitivity") != null) {
                visualizer.setSensitivity(parseDouble(getParameter("sensitivity")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        try {
            if (getParameter("radius") != null) {
                positionCalculator.setRadius(parseDouble(getParameter("radius")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        try {
            if (getParameter("zoom") != null) {
                visualizer.setZoom(parseDouble(getParameter("zoom")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };

        visualizer.init(getSize().width, getSize().height, this);
        System.out.println("applet initialized");
    }

    public final void paint(Graphics g) {
        if (g != null) {
            visualizer.paint(g);
        }
    }

    private final double parseDouble(final String value) throws NumberFormatException {
        final String v = value.trim();
        double result = 0;
        int position = 0;
        boolean isDecimal = false;
        double decimal = 1;
        if (v.length() == 0) {
            throw new NumberFormatException("empty String");
        }
        if (v.charAt(position) == '-') {
            result = -1;
            position++;
        } else {
            if (v.charAt(position) == '+') {
                position++;
            }
        }
        while (position < v.length()) {
            switch (v.charAt(position)) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                result = 10 * result + (v.charAt(position) - '0');
                if (isDecimal) {
                    decimal *= 10;
                }
                break;
            case '.':
                if (isDecimal) {
                    throw new NumberFormatException("multiple decimal points in " + v);
                }
                isDecimal = true;
                break;
            default:
                throw new NumberFormatException("unexpected character: " + v.charAt(position) + " in " + v);
            }
            position++;
        }
        System.out.println(v + " -> " + (result / decimal));
        return result / decimal;
    }

    public final void start() {
        try {
            if (runThread == null) {
                runThread = new Thread(this);
                runThread.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public final void stop(){
        if (runThread != null) {
            runThread = null;
        }
    }

    public final void run() {
        try {
            while (runThread != null) {

                positionCalculator.calculateMovement(viewPoint);
//                positionCalculator.printOrientation();
                visualizer.generateImage(viewPoint, field);
                paint(getGraphics());
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}


