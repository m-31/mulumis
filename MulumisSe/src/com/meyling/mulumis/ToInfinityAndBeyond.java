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
//     http://www.gnu.org/licenses/lgpl.html
//
// If you didn't download this code from the following link, you should 
// check if you aren't using an obsolete version:
//     http://sourceforge.net/projects/mulumis
//
// The hompage of the simulum project is:
//     http://www.mulumis.meyling.com

package com.meyling.mulumis;


import java.awt.Frame;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Info window.
 *
 * @version     $Revision$
 * @author      Michael Meyling
 */
public class ToInfinityAndBeyond extends Window {

    private final Stars field;


    public ToInfinityAndBeyond() {
    	super(new Frame());
        final String METHOD = "Constructor";
		this.setSize(getToolkit().getScreenSize());

        this.setLayout(null);
        addNotify();

        field = new Stars();
        field.setSize(getToolkit().getScreenSize());
        this.add(field);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	field.stop();
                dispose();
                System.exit(0);
            }
        });
		field.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				field.stop();
				dispose();
				System.exit(0);
			}
		});
        this.repaint();
        field.init();
        field.start();

    }

	public void show() {
		super.show();
		getParent().setVisible(true);
	}


    public static final void main(final String args[]) {
        try {
            final ToInfinityAndBeyond infinity = new ToInfinityAndBeyond();
            infinity.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }



 }
