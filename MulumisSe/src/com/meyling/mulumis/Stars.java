// $Id$
//
// This file is part of the program suite "Simulum". Simulum deals with
// different simulations of star movements and their visualizations.
//
// Copyright (C) 2004 by Michael Meyling
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

package com.meyling.mulumis;


import java.applet.Applet;
import java.awt.Graphics;

/**
 * Simulates star field.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
//class Stars extends JPanel {
public final class Stars extends Applet implements Runnable {
    private Thread runThread;
    private StarField field = new StarField();
	private boolean circleNormale;
	private boolean linear;
    
    public Stars() {
    }

    public void init() {
    	try {
    		if (getParameter("stars") != null) {
	    		field.setNumberOfStars(Integer.parseInt(getParameter("stars")));
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	};
    	try {
    		if (getParameter("delta") != null) {
    			field.setDelta(parseDouble(getParameter("delta")));
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	};
    	try {
    		if (getParameter("sensitivity") != null) {
    			field.setSensitivity(parseDouble(getParameter("sensitivity")));
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	};
    	try {
    		if (getParameter("radius") != null) {
    			field.setRadius(parseDouble(getParameter("radius")));
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	};
    	try {
    		if (getParameter("zoom") != null) {
    			field.setZoom(parseDouble(getParameter("zoom")));
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	};
    	try {
    		this.circleNormale = false;
    		if (getParameter("normale") != null && getParameter("normale").equals("true")) {
    			this.circleNormale = true;
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	};
    	try {
    		this.circleNormale = false;
    		if (getParameter("linear") != null && getParameter("linear").equals("true")) {
    			this.linear = true;
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	};
    	this.field.init(getSize().width, getSize().height, this);
    }

    public final void paint(Graphics g) {
		if (g != null) {
			field.paint(g);
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
            	if (Stars.this.linear) {
            		Stars.this.field.calculateLinearMovement();
            	} else if (Stars.this.circleNormale) {
            		Stars.this.field.calculateCircleMovementWithChangingViewingDirection();
            	} else {
            		Stars.this.field.calculateCircleMovement();
            	}
                Stars.this.field.generateImage();
                Stars.this.paint(Stars.this.getGraphics());
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


