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
import java.awt.Image;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;

import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.stars.GravityObject;
import com.meyling.mulumis.base.stars.StarField;
import com.meyling.mulumis.base.util.CalculatorUtility;

/**
 * Simulates photo plate.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public final class PhotoPlate  {
    private double sensitivity = 6;
    private double zoom = 320;
    private double position[] = new double[GravityObject.DIMENSION];
    private double x[] = new double[GravityObject.DIMENSION];
    private double y[] = new double[GravityObject.DIMENSION];
    private double z[] = new double[GravityObject.DIMENSION];

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
    private byte[][] paletteTable;
    private int snapshot;
    private int current;


    public PhotoPlate() {
        x[0] = 1;
        y[1] = 1;
        z[2] = 1;
    }

    public void init(final int width, final int height, final Component parent) {
        this.width = width;
        this.height = height;

        this.halfWidth = width / 2;
        this.halfHeight = height / 2;

        while (width == 0 || height == 0) {
            System.err.println("waiting");
        }
        calculatePaletteTable();
        Trace.traceParam(this, "init", "width*height", (int) width * height);
        Trace.traceParam(this, "init", "width", width);
        Trace.traceParam(this, "init", "height" , height);
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

        mem = new MemoryImageSource(width, height, icm, pix, 0, width);
        mem.setAnimated(true);
        mem.setFullBufferUpdates(true);
        im = parent.createImage(mem);
    }

    public final void setPosition(final double[] position) {
        if (position == null) {
            throw new NullPointerException("position is null");
        }
        if (position.length != GravityObject.DIMENSION) {
            throw new IllegalArgumentException("position has not dimension " + GravityObject.DIMENSION);
        }
        this.position = position;
// old copy code:
/*
        for (int i = 0; i < GravityObject.DIMENSION; i++) {
            this.position[i] = position[i];
        }
*/
    }

    public final void setOrientation(final double[] x, final double[] y, final double[] z) {
        if (x == null) {
            throw new NullPointerException("x vector is null");
        }
        if (x.length != GravityObject.DIMENSION) {
            throw new IllegalArgumentException("x has not dimension " + GravityObject.DIMENSION);
        }
        this.x = x;
        if (y == null) {
            throw new NullPointerException("y vector is null");
        }
        if (y.length != GravityObject.DIMENSION) {
            throw new IllegalArgumentException("y has not dimension " + GravityObject.DIMENSION);
        }
        this.y = y;
        if (z == null) {
            throw new NullPointerException("z vector is null");
        }
        if (z.length != GravityObject.DIMENSION) {
            throw new IllegalArgumentException("z has not dimension " + GravityObject.DIMENSION);
        }
        this.z = z;
    }

    public final void paint(Graphics g) {
        if (g != null) {
            mem.newPixels();
            g.drawImage(im, 0, 0, null);
        }
    }

    public final void generateImage(final StarField field) {
        final double sensitivity = Math.exp(this.sensitivity);
        if (++current > snapshot) {    // clear old image
            // empty brightness everywhere
            System.arraycopy(emptyBright, 0, bright, 0, emptyBright.length);
            // black screen
            System.arraycopy(emptyPix, 0, pix, 0, emptyPix.length);
            current = 0;
        }
        for (int i = 0; i < field.getNumberOfStars(); i++) {
            final double d = CalculatorUtility.minusscalar(field.getStar(i).getPosition(), position, z);
            if (d > 0) {
                double xr = zoom * CalculatorUtility.minusscalar(field.getStar(i).getPosition(), position, x) / d + halfWidth;
                if (xr < 0 ||  xr >= width) {
                    continue;
                }
                double yr = zoom * CalculatorUtility.minusscalar(field.getStar(i).getPosition(), position, y) / d + halfHeight;
                if (yr < 0 || yr >= height) {
                    continue;
                }
                int xir = (int) xr;
                int yir = (int) yr;
                double brightness = 255;
                brightness = sensitivity / CalculatorUtility.distanceSquare(field.getStar(i).getPosition(), position);
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
//                                                                System.out.println("cut: " + hell);
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

    private final void drawPoint(final int xir, final int yir, byte hell) {
        if (xir >= 0 && xir < this.width && yir >= 0 & yir < this.height) {
            this.pix[this.width * yir + xir] = (byte) hell;
        }
    }

    private final void calculatePaletteTable() {
        paletteTable = new byte[3][256];
        for(int i=0;i<256;i++) {
            paletteTable[0][i]=(byte) i;
            paletteTable[1][i]=(byte) i;
            paletteTable[2][i]=(byte) i;
        }
        icm = new IndexColorModel(8, 256, paletteTable[0], paletteTable[1], paletteTable[2]);
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

    /**
     * @return Returns the snapshot.
     */
    public final int getSnapshot() {
        return snapshot;
    }
    /**
     * @param snapshot The snapshot to set.
     */
    public final void setSnapshot(int snapshot) {
        this.snapshot = snapshot;
    }


}


