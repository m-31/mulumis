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


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;

/**
 * Simulates star field.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
//class Stars extends JPanel {
public final class StarField  {
    private double sensitivity = 6;
	private int stars = 40000;
    private double delta = 0.001;
    private double alpha = 0;
    private double radius = 0.25;
    private double zoom = 320;
    private double pos[] = new double[] {0.5 - radius, 0.5 - radius, 0.5 - radius};

    private double x[] = new double[] {-2/Math.sqrt(6), 1/Math.sqrt(6), 1/Math.sqrt(6)};
    private double z[] = new double[] {1/Math.sqrt(3), 1/Math.sqrt(3), 1/Math.sqrt(3)};
    // cross product
    private double y[] = new double[] {z[1]*x[2] - z[2]*x[1], -(z[0]*x[2] - z[2]*x[0]), z[0]*x[1] - z[1]*x[0]};

    private int width;
    private int height;
    private int halfWidth;
    private int halfHeight;
    private MemoryImageSource mem;
    private Image im;
    private byte[] pix;
    private byte[] emptyPix;
    private double[] bright;
    private double[] emptyBright;
    private IndexColorModel icm;
    private Graphics offscreen = null;
    byte[][] paletteTable;
    private double star[][];
    final double[] zero = new double[] {0.5, 0.5, 0.5};


    public StarField() {
    }

    public void init(final int width, final int height, final Component parent) {
        this.width = width;
        this.height = height;

        this.halfWidth = width / 2;
        this.halfHeight = height / 2;
        star = new double[stars][3];
        // starting view point position

        for (int i = 0; i < stars; i++){
            do {
                for (int j = 0; j < 3; j++) {
                    star[i][j] = Math.random();
                }
            } while (distance(star[i], zero) > 0.5);
        }
        System.out.println("len x = " + len(x));
        System.out.println("len y = " + len(y));
        System.out.println("len z = " + len(z));
        System.out.println("<x,y> = " + scalar(x, y));
        System.out.println("<x,z> = " + scalar(x, z));
        System.out.println("<y,z> = " + scalar(y, z));
        System.out.println("d(x,y)= " + distance(x, y));
        System.out.println("d(x,z)= " + distance(x, z));
        System.out.println("d(y,z)= " + distance(y, z));

        while (width == 0 || height == 0) {
            System.err.println("waiting");
        }
        calculatePaletteTable();
        pix = new byte[width * height];
        bright = new double[width * height];
        emptyPix = new byte[width * height];
        emptyBright = new double[width * height];
        for (int i = 0; i < width * height; i++) {
            emptyBright[i] = 0;
            emptyPix[i] = 0;
            bright[i] = 0;
            pix[i] = 0;
        }

        System.out.println("len=" + pix.length);
        mem = new MemoryImageSource(width, height, icm, pix, 0, width);
        mem.setAnimated(true);
        mem.setFullBufferUpdates(true);
        im = parent.createImage(mem);

    }

    public final void paint(Graphics g) {
		if (g != null) {
	        mem.newPixels();
	        g.drawImage(im, 0, 0, null);
		}
    }

    public final void calculateCircleMovement() {
        // new viewpoint
        alpha += delta;
		pos[0] = Math.sin(alpha) * radius + 0.5;
        pos[1] = Math.cos(alpha) * radius + 0.5;
        pos[2] = 0.5;
    }    

    public final void calculateCircleMovementWithChangingViewingDirection() {
        // new viewpoint
        calculateCircleMovement();
        z = new double[] {-Math.sin(alpha), -Math.cos(alpha), 0};
        x = new double[] {0, 0, 1};
        // cross product
        y = new double[] {z[1]*x[2] - z[2]*x[1], -(z[0]*x[2] - z[2]*x[0]), z[0]*x[1] - z[1]*x[0]};        
    }    

    public final void calculateLinearMovement() {
        // new viewpoint
    	if (this.pos[0] < 2) {
    		final double distance = distance(pos, zero);
//        	final double distanceSquare = distanceSquare(pos, zero);
        	final double d;
//        	if (3 * delta * (distanceSquare + 1) > distance) {
	    		d = delta * (distance + 1);
//        	} else {
//    		    d = delta * (distanceSquare + 1);
//        	}
    		pos[0] = pos[0] + d;
	    	pos[1] = pos[1] + d;
	    	pos[2] = pos[2] + d;
    	} else {
	        pos[0] = 0.5 - radius;
	        pos[1] = 0.5 - radius;
	        pos[2] = 0.5 - radius;
    	}
    }    

    
    public final void generateImage() {    
        // empty brightness everywhere
        System.arraycopy(emptyBright, 0, bright, 0, emptyBright.length);
        // black screen
        System.arraycopy(emptyPix, 0, pix, 0, emptyPix.length);

        for (int i = 0; i < stars; i++) {
            final double d = minusscalar(star[i], pos, z);
            if (d > 0) {
                double xr = zoom * minusscalar(star[i], pos, x) / d + halfWidth;
                if (xr < 0 ||  xr >= width) {
                    continue;
                }
                double yr = zoom * minusscalar(star[i], pos, y) / d + halfHeight;
                if (yr < 0 || yr >= height) {
                    continue;
                }
                int xir = (int) xr;
                int yir = (int) yr;
                double brightness = 255;
                try {
                    brightness = sensitivity / distanceSquare(star[i], pos);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                bright[width * yir + xir] += brightness;
                int hell = (int) bright[width * yir + xir];
                drawBrightness(xir, yir, hell);
            }
        }
    }

    private final void drawBrightness(final int xir, final int yir, int hell) {
        if (hell < 0){
            hell = 0;
        }
        if (hell > 255) {
            pix[width * yir + xir] = (byte) 255;
            hell -= 256;
            if (hell > 255) {
            	drawPoint(xir + 1, yir, (byte) 255);
                hell -= 256;
                if (hell > 255) {
                	drawPoint(xir, yir + 1, (byte) 255);
                    hell -= 256;
                    if (hell > 255) {
                        drawPoint(xir + 1, yir + 1, (byte) 255);
                        hell -= 256;
                        if (hell > 255) {
                        	drawPoint(xir - 1, yir, (byte) 255);
                        	hell -= 256;
                            if (hell > 255) {
                            	drawPoint(xir - 1, yir, (byte) 255);
                            	hell -= 256;
                                if (hell > 255) {
                                	drawPoint(xir - 1, yir + 1, (byte) 255);
                                	hell -= 256;
                                    if (hell > 255) {
                                    	drawPoint(xir, yir - 1, (byte) 255);
                                    	hell -= 256;
                                        if (hell > 255) {
                                        	drawPoint(xir + 1, yir - 1, (byte) 255);
                                        	hell -= 256;
                                            if (hell > 255) {
                                            	drawPoint(xir - 1, yir - 1, (byte) 255);
                                            	hell -= 256;
                                                if (hell > 255) {
                                                	drawPoint(xir, yir - 2, (byte) 255);
                                                	hell -= 256;
                                                    if (hell > 255) {
                                                    	drawPoint(xir, yir + 2, (byte) 255);
                                                    	hell -= 256;
                                                        if (hell > 255) {
                                                        	drawPoint(xir + 2, yir, (byte) 255);
                                                        	hell -= 256;
                                                            if (hell > 255) {
                                                            	drawPoint(xir - 2, yir, (byte) 255);
//                                                            	System.out.println("cut: " + hell);
                                                            } else {
                                                                drawPoint(xir - 2, yir, (byte) hell);
                                                            }
                                                        } else {
                                                            drawPoint(xir + 2, yir, (byte) hell);
                                                        }
                                                    } else {
                                                        drawPoint(xir, yir + 2, (byte) hell);
                                                    }
                                                } else {
                                                    drawPoint(xir, yir - 2, (byte) hell);
                                                }
                                            } else {
                                                drawPoint(xir - 1, yir - 1, (byte) hell);
                                            }
                                        } else {
                                            drawPoint(xir + 1, yir - 1, (byte) hell);
                                        }
                                    } else {
                                        drawPoint(xir, yir - 1, (byte) hell);
                                    }
                                } else {
                                    drawPoint(xir - 1, yir + 1, (byte) hell);
                                }
                            } else {
                                drawPoint(xir - 1, yir, (byte) hell);
                            }
                        } else {
                            drawPoint(xir - 1, yir, (byte) hell);
                        }
                    } else {
                        drawPoint(xir + 1, yir + 1, (byte) hell);
                    }
                } else {
                	drawPoint(xir, yir + 1, (byte) hell);
                }
            } else {
            	drawPoint(xir + 1, yir, (byte) hell);
            }
        } else {
        	drawPoint(xir, yir, (byte) hell);
        }
    }
    
	private void drawPoint(final int xir, final int yir, byte hell) {
		if (xir >= 0 && xir < this.width && yir >= 0 & yir < this.height) {
			this.pix[this.width * yir + xir] = (byte) hell;
		}
	}

	private final double distanceSquare(double[] a, double[] b) {
        return (a[0]- b[0])*(a[0]- b[0]) + (a[1]- b[1])*(a[1]- b[1])
            + (a[2]- b[2])*(a[2]- b[2]);
    }

    private final double distance(double[] a, double[] b) {
        return Math.sqrt((a[0]- b[0])*(a[0]- b[0]) + (a[1]- b[1])*(a[1]- b[1])
            + (a[2]- b[2])*(a[2]- b[2]));
    }

    private final double len(double[] a) {
        return Math.sqrt(a[0]*a[0] + a[1]*a[1] + a[2]*a[2]);
    }

    private final double scalar(double[] a, double[] b) {
        return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
    }


    private final double minusscalar(double[] a, double[] b, double[] c) {
        return (a[0]-b[0])*c[0] + (a[1]-b[1])*c[1] + (a[2]-b[2])*c[2];
    }

    void calculatePaletteTable() {
        paletteTable = new byte[3][256];
        for(int i=0;i<256;i++) {
            paletteTable[0][i]=(byte) i;
            paletteTable[1][i]=(byte) i;
            paletteTable[2][i]=(byte) i;
        }
        icm = new IndexColorModel(8, 256, paletteTable[0], paletteTable[1], paletteTable[2]);
    }


	/**
	 * @return Returns the delta.
	 */
	public final double getDelta() {
		return delta;
	}
	
	/**
	 * @param delta The delta to set.
	 */
	public final void setDelta(double delta) {
		this.delta = delta;
	}
	
	/**
	 * @return Returns the stars.
	 */
	public final int getNumberOfStars() {
		return stars;
	}
	
	/**
	 * @param stars The stars to set.
	 */
	public final void setNumberOfStars(int stars) {
		this.stars = stars;
	}
	/**
	 * @return Returns the sensitivity.
	 */
	public final double getSensitivity() {
		return sensitivity;
	}
	/**
	 * @param sensitivity The sensitivity to set.
	 */
	public final void setSensitivity(double sensitivity) {
		this.sensitivity = sensitivity;
	}
	/**
	 * @return Returns the radius.
	 */
	public final double getRadius() {
		return radius;
	}
	/**
	 * @param radius The radius to set.
	 */
	public final void setRadius(double radius) {
		this.radius = radius;
        this.pos[0] = 0.5 - radius;
        this.pos[1] = 0.5 - radius;
        this.pos[2] = 0.5 - radius;
	}
	/**
	 * @return Returns the zoom.
	 */
	public final double getZoom() {
		return zoom;
	}
	/**
	 * @param zoom The zoom to set.
	 */
	public final void setZoom(double zoom) {
		this.zoom = zoom;
	}
}


