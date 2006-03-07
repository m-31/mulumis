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
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.simulator.Simulator;
import com.meyling.mulumis.base.simulator.SimulatorAttributes;
import com.meyling.mulumis.base.util.IoUtility;
import com.meyling.mulumis.base.view.Viewer;
import com.meyling.mulumis.base.view.CameraAttributes;
import com.meyling.mulumis.base.viewpoint.AbstractAutomaticMover;
import com.meyling.mulumis.base.viewpoint.ManualMovement;


/**
 * Simulates star field.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class StarApplet extends Applet implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener  {
    private static final long serialVersionUID = -3554962680447033507L;
    private Thread runThread;
    private Boolean stopped = Boolean.TRUE;
    private CameraAttributes viewerProperties = new CameraAttributes();
    private SimulatorAttributes simulatorProperties = new SimulatorAttributes();

    private Simulator simulator;
    private Viewer viewer;
    
    int prevx, prevy;
    private double ytheta;
    private double xtheta;

    public StarApplet() {
    }

    public void init() {
        try {
            if (getParameter("stars") != null) {
                simulatorProperties.setStars(Integer.parseInt(getParameter("stars")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };

        try {
            if (getParameter("movement") != null ) {
                viewerProperties.setMovement(getParameter("movement"));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        try {
            if (getParameter("delta") != null) {
                viewerProperties.setDelta(IoUtility.parseDouble(getParameter("delta")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        try {
            if (getParameter("sensitivity") != null) {
                viewerProperties.setSensitivity(IoUtility.parseDouble(getParameter("sensitivity")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        try {
            if (getParameter("radius") != null) {
                viewerProperties.setRadius(IoUtility.parseDouble(getParameter("radius")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        try {
            if (getParameter("zoom") != null) {
                viewerProperties.setZoom(IoUtility.parseDouble(getParameter("zoom")));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        if (simulator != null) {
            synchronized (simulator) {
                simulator = new Simulator(simulatorProperties);
                viewer = new Viewer(simulator, viewerProperties, getSize().width, getSize().height, this);
            }
        } else {
            simulator = new Simulator(simulatorProperties);
            viewer = new Viewer(simulator, viewerProperties, getSize().width, getSize().height, this);
        }
        if ("manualDelay".equals(viewer.getProperties().getMovement()) && !simulator.hasGravity()) {
            final ManualMovement mover = (ManualMovement) viewer.getPositionCalculator();
            mover.setXtheta(-0.003);
            mover.setYtheta(-0.002);
        }
        prevx = 0;
        prevy = 0;
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
     }

    public void destroy() {
        removeMouseListener(this);
        removeMouseMotionListener(this);
    }

    public final void paint(Graphics g) {
        if (viewer != null) {
            viewer.paintPicture(g);
        }
    }

    public final void start() {
        synchronized (stopped) {
            if (runThread == null) {
                runThread = new Thread(this);
                runThread.start();
            }
            Trace.trace(this, "start", "Thread started");
        }
    }

    public final void stop(){
        synchronized (stopped) {
            if (runThread != null) {
                runThread = null;
            }
            Trace.trace(this, "stop", "Thread stopped");
        }
    }

    public final void run() {
        try {
            synchronized (stopped) {
                stopped = Boolean.FALSE;
            }
            if ("manual".equals(viewerProperties.getMovement())) {
                viewer.moveViewPoint();
            }
            while (runThread != null) {
                synchronized (simulator) {
                    if (!"manual".equals(viewerProperties.getMovement())) {
                        viewer.moveViewPoint();
                    }
                    viewer.takePicture();
                }
                paint(getGraphics());
                if (simulator.hasGravity()) {
                    simulator.applyGravity();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                } else {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                    }
                }
            }
            synchronized (stopped) {
                stopped = Boolean.TRUE;
            }
        } catch (Exception e){
            Trace.trace(this, "run", e);
            e.printStackTrace();
        }
    }

    /* event handling */
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() != 1) {
            return;
        }
        if (!(viewer.getPositionCalculator() instanceof ManualMovement)) {
            return;
        }

        if (!"manualDelay".equals(viewerProperties.getMovement())) {
            return;
        }
        ManualMovement positionCalculator = (ManualMovement) viewer.getPositionCalculator();
        positionCalculator.setXtheta(0);
        positionCalculator.setYtheta(0);
    }

    public void mousePressed(MouseEvent e) {
      prevx = e.getX();
      prevy = e.getY();
      e.consume();
    }
    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (InputEvent.BUTTON1_DOWN_MASK != (e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK)) {
            return;
        }
        if (!(viewer.getPositionCalculator() instanceof ManualMovement)) {
            return;
        }
        final ManualMovement positionCalculator = (ManualMovement) viewer.getPositionCalculator();
        int x = e.getX();
        int y = e.getY();
        final double pi_fraction = Math.PI / 10;
//        final double pi_fraction = Math.PI / 5;
        final double max = Math.max(getSize().width, getSize().height);
        ytheta = (prevy - y) * (pi_fraction / max);
        xtheta = (prevx - x) * (pi_fraction / max);
        positionCalculator.setXtheta(xtheta);
        positionCalculator.setYtheta(ytheta);
        viewer.moveViewPoint();
        viewer.takePicture();
        prevx = x;
        prevy = y;
        e.consume();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public final SimulatorAttributes getSimulatorProperties() {
        return simulatorProperties;
    }

    public final CameraAttributes getViewerProperties() {
        return viewerProperties;
    }

    public final void setSimulatorProperties(final SimulatorAttributes properties) {
        this.simulatorProperties = properties;
    }
    
    public final SimulatorAttributes getCurrentProperties() {
        return simulator.getProperties();
    }

    public final Simulator getSimulator() {
        return simulator;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            AbstractAutomaticMover r = viewer.getPositionCalculator();
            if (InputEvent.BUTTON3_DOWN_MASK != (e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK)) {
                if (e.getUnitsToScroll() < 0) {
                    r.setRadius(r.getRadius() * 1.05);
                } else {
                    r.setRadius(r.getRadius() / 1.05);
                }
            }
            final double s;
            if (e.getUnitsToScroll() < 0) {
    //            zoom = zoom * 1.1;
                s = viewer.getSensitivity() + 2 * Math.log(1.05);
            } else {
    //            zoom = zoom / 1.1;
                s = viewer.getSensitivity() - 2 * Math.log(1.05);
            }
            if (!Double.isInfinite(s)) {
                viewer.setSensitivity(s);
            }
        }
        viewer.moveViewPoint();
        viewer.takePicture();
        e.consume();
    }


}


