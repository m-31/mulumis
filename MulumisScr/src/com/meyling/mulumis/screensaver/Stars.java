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

package com.meyling.mulumis.screensaver;


import java.awt.Component;
import java.awt.Graphics;

import org.jdesktop.jdic.screensaver.ScreensaverSettings;
import org.jdesktop.jdic.screensaver.SimpleScreensaver;

import com.meyling.mulumis.StarField;

/**
 * Simulates star field.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
//class Stars extends JPanel {
public final class Stars extends SimpleScreensaver {

	private StarField field = new StarField();
	
	public Stars() {
    }

    public void init() {
		ScreensaverSettings settings = getContext().getSettings();
        final String delta = settings.getProperty( "delta" );
        if (delta != null ) {
            try {
                double d = Double.parseDouble(delta);
        		this.field.setDelta(d);
            }
            catch( NumberFormatException e ) {
            }
        } 
        final String stars = settings.getProperty( "stars" );
        if (stars != null ) {
            try {
                int s = Integer.parseInt(stars);
        		this.field.setNumberOfStars(s);
            }
            catch( NumberFormatException e ) {
            }
        } 
		final Component c = getContext().getComponent();
		this.field.init(c.getWidth(), c.getHeight(), c);
    }

    public final void paint(Graphics g) {
    	this.field.calculateCircleMovement();
    	this.field.paint(g);
    }

}


