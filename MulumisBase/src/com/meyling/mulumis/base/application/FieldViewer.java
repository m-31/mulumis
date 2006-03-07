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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.simulator.Simulator;
import com.meyling.mulumis.base.view.ViewChangedListener;
import com.meyling.mulumis.base.view.Viewer;
import com.meyling.mulumis.base.view.CameraAttributes;
import com.meyling.mulumis.base.viewpoint.AbstractAutomaticMover;
import com.meyling.mulumis.base.viewpoint.ManualMovement;


/**
 * Star field viewer.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class FieldViewer extends Applet implements Runnable, 
        MouseListener, MouseMotionListener, MouseWheelListener {
    private static final long serialVersionUID = -3554962680447033507L;
    private Thread runThread;
    int prevx, prevy;
    private double ytheta;
    private double xtheta;

    private Viewer viewer;
    private boolean threadSuspended;


    public FieldViewer() {
    }
    
    public FieldViewer(final FieldViewer v, final int width, final int height, final Component parent) {
        this.viewer = new Viewer(v.viewer, width, height, parent);
        this.prevx = v.prevx;
        this.prevy = v.prevy;
        super.resize(width, height);
    }

    public void init() {
        prevx = 0;
        prevy = 0;
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        threadSuspended = false;
    }
    
    public void destroy() {
        super.destroy();
        removeMouseListener(this);
        removeMouseMotionListener(this);
        if (viewer != null) {
            viewer.close();
            viewer = null;
        }
    }

    public void resize(int width, int height) {
        super.resize(width, height);
        if (viewer != null) {
            viewer.resize(width, height, this);
        }
    }

    public final void applyVisualChanges(final Simulator simulator, final CameraAttributes properties) {
        if (viewer == null) {
            viewer = new Viewer(simulator, properties, getWidth(), getHeight(), this);
        }
        viewer.applyVisualChanges(simulator, properties);
        paint(getGraphics());
    }

    public final void addViewChangedListener(final ViewChangedListener list) {
        viewer.addViewChangedListener(list);
    }
    
    public final void removeViewChangedListener(final ViewChangedListener list) {
        viewer.removeViewChangedListener(list);
    }
    
    public final void removeAllViewChangedListeners() {
        viewer.removeAllViewChangedListeners();
    }
    
    public final void paint(Graphics g) {
        if (viewer != null) {
            viewer.paintPicture(g);
        }
    }

    public final void start() {
        if (runThread == null) {
            runThread = new Thread(this);
            runThread.start();
        }
        Trace.trace(this, "start", "Thread started");
    }

    public synchronized final void stop(){
        runThread = null;
        if (threadSuspended) {
            threadSuspended = false;
            notify();
        }         
        Trace.trace(this, "stop", "Thread stopped");
    }

    public final boolean isRunning(){
        return runThread != null;
    }

    public final void run() {
        try {
            while (null != runThread) { 
                viewer.moveViewPoint();
                viewer.takePicture();
                paint(getGraphics());
                try {
                    Thread.sleep(30);
                    synchronized(this) {
                        while (threadSuspended) {
                            wait();
                        }
                    }
                } catch (InterruptedException e) {
                }
                
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
        e.consume();
    }

    public void mouseEntered(MouseEvent e) {
        e.consume();
    }

    public void mouseExited(MouseEvent e) {
        e.consume();
    }

    public void mouseDragged(MouseEvent e) {
        e.consume();
        if (InputEvent.BUTTON1_DOWN_MASK != (e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK)) {
            return;
        }
        if (!(viewer.getPositionCalculator() instanceof ManualMovement)) {
            return;
        }
        final ManualMovement positionCalculator = (ManualMovement) viewer.getPositionCalculator();
        int x = e.getX();
        int y = e.getY();
        final double pi_fraction;
        if ("manual".equals(viewer.getMovement())) {    // TODO mime 20060306: move constant declaration
            pi_fraction = Math.PI / 2;
        } else {
            pi_fraction = Math.PI / 10;
        }
        final double max = Math.max(getToolkit().getScreenSize().width, getToolkit().getScreenSize().height);
        ytheta = (prevy - y) * (pi_fraction / max);
        xtheta = (prevx - x) * (pi_fraction / max);
        positionCalculator.setXtheta(xtheta);
        positionCalculator.setYtheta(ytheta);
        viewer.moveViewPoint();
        viewer.takePicture();
        if ("manual".equals(viewer.getMovement())) {    // TODO mime 20060306: move constant declaration
            positionCalculator.setXtheta(0);
            positionCalculator.setYtheta(0);
        }
        prevx = x;
        prevy = y;
        e.consume();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            AbstractAutomaticMover r = viewer.getPositionCalculator();
            if (InputEvent.BUTTON3_DOWN_MASK != (e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK)) {
                if (e.getUnitsToScroll() < 0) {
                    viewer.setRadius(r.getRadius() * 1.05);
                } else {
                    viewer.setRadius(r.getRadius() / 1.05);
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
        viewer.takePicture();
        e.consume();
    }

    public final CameraAttributes getProperties() {
        if (viewer == null) {
            return null;
        }
        return viewer.getProperties();
    }

    public final Viewer getViewer() {
        return viewer;
    }

}


