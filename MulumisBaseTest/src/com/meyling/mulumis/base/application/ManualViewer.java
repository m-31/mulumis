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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.meyling.mulumis.base.common.GravityObject;
import com.meyling.mulumis.base.simulator.PhotoPlate;
import com.meyling.mulumis.base.stars.StarField;
import com.meyling.mulumis.base.viewpoint.ManualMovement;
import com.meyling.mulumis.base.viewpoint.ViewPoint;

/**
 * Simulates star field.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class ManualViewer extends Applet implements Runnable, MouseListener, MouseMotionListener {

    private static final long serialVersionUID = -3554962680447033507L;
    private Thread runThread;
    private PhotoPlate visualizer;
    private ManualMovement positionCalculator;
    final ViewPoint viewPoint = new ViewPoint();
    float xfac;
    int prevx, prevy;
    float xtheta, ytheta;
    private StarField field;


    public ManualViewer() {
    }

    public void init() {
        int stars = 100;
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
            if (getParameter("sensitivity") != null) {
                visualizer.setSensitivity(parseDouble(getParameter("sensitivity")));
            } else {
                visualizer.setSensitivity(8);
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        try {
            if (getParameter("zoom") != null) {
                visualizer.setZoom(parseDouble(getParameter("zoom")));
            } else {
                visualizer.setZoom(400);
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };


        positionCalculator = new ManualMovement(zero);
        positionCalculator.setRadius(2);
        positionCalculator.calculateMovement(viewPoint);
        visualizer.init(getSize().width, getSize().height, this);

        addMouseListener(this);
        addMouseMotionListener(this);
        System.out.println("applet initialized");
    }

    public void destroy() {
        removeMouseListener(this);
        removeMouseMotionListener(this);
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
        while (true) {
            visualizer.generateImage(viewPoint, field);
            paint(getGraphics());
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }
   }

    /* event handling */
    public void mouseClicked(MouseEvent e) {
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
      int x = e.getX();
      int y = e.getY();
      final double pi_half = Math.PI / 2;
      double ytheta = (prevy - y) * (pi_half / getSize().width);
      double xtheta = (prevx - x) * (pi_half / getSize().height);
      positionCalculator.setXtheta(xtheta);
      positionCalculator.setYtheta(ytheta);
//      System.out.println("dragged");
//      if (painted) {
//        painted = false;
          positionCalculator.calculateMovement(viewPoint);
           visualizer.generateImage(viewPoint, field);
//      }
      prevx = x;
      prevy = y;
      e.consume();
    }

    public void mouseMoved(MouseEvent e) {
    }

}


