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

import com.meyling.mulumis.base.simulator.Simulator;
import com.meyling.mulumis.base.viewpoint.ManualMovement;


/**
 * Simulates star field.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class StarApplet extends Applet implements Runnable, MouseListener, MouseMotionListener  {
    private static final long serialVersionUID = -3554962680447033507L;
    private Thread runThread;
    private Simulator simulator;
    int prevx, prevy;

    public StarApplet() {
    }

    public void init() {
        int stars = 4000;
        try {
            if (getParameter("stars") != null) {
                stars = Integer.parseInt(getParameter("stars"));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };

        String movement = "manual";
        try {
            if (getParameter("movement") != null ) {
                movement = getParameter("movement");
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        double delta = 0.001;
        try {
            if (getParameter("delta") != null) {
                delta = parseDouble(getParameter("delta"));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        double sensitivity = 70;
        try {
            if (getParameter("sensitivity") != null) {
                sensitivity = parseDouble(getParameter("sensitivity"));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        double radius = 0.9;
        try {
            if (getParameter("radius") != null) {
                radius = parseDouble(getParameter("radius"));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        double zoom = 700;
        try {
            if (getParameter("zoom") != null) {
                zoom = parseDouble(getParameter("zoom"));
            }
        } catch (NullPointerException e) {
        } catch (Exception e){
            e.printStackTrace();
        };
        simulator = new Simulator(stars, movement, delta, sensitivity, radius, zoom);
        simulator.getPhotoPlate().init(getSize().width, getSize().height, this);
        addMouseListener(this);
        addMouseMotionListener(this);
     }

    public void destroy() {
        removeMouseListener(this);
        removeMouseMotionListener(this);
    }

    public final void paint(Graphics g) {
        simulator.paint(g);
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
                simulator.moveViewPoint();
                simulator.getPhotoPlate().generateImage();
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

    /* event handling */
    public void mouseClicked(MouseEvent e) {
        if (!(simulator.getPositionCalculator() instanceof ManualMovement)) {
            return;
        }
        ManualMovement positionCalculator = (ManualMovement) simulator.getPositionCalculator();
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
        if (!(simulator.getPositionCalculator() instanceof ManualMovement)) {
            return;
        }
        ManualMovement positionCalculator = (ManualMovement) simulator.getPositionCalculator();
        int x = e.getX();
        int y = e.getY();
        final double pi_fraction = Math.PI / 10;
        double ytheta = (prevy - y) * (pi_fraction / getSize().width);
        double xtheta = (prevx - x) * (pi_fraction / getSize().height);
        positionCalculator.setXtheta(xtheta);
        positionCalculator.setYtheta(ytheta);
          simulator.moveViewPoint();
           simulator.getPhotoPlate().generateImage();
           prevx = x;
           prevy = y;
           e.consume();
    }

    public void mouseMoved(MouseEvent e) {
    }


}


