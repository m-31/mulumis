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
//     http://www.gnu.org/licenses/lgpl.html
//
// If you didn't download this code from the following link, you should
// check if you aren't using an obsolete version:
//     http://sourceforge.net/projects/mulumis
//
// The hompage of the simulum project is:
//     http://www.mulumis.meyling.com

package com.meyling.mulumis.base.application;


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
public class ToInfinityAndBeyond2 extends Window {

    private static final long serialVersionUID = 2563504527447287674L;
    private final StarApplet1 visualizer;


    public ToInfinityAndBeyond2() {
        super(new Frame());
        this.setSize(getToolkit().getScreenSize());

        this.setLayout(null);
        addNotify();

        visualizer = new StarApplet1();
        visualizer.setSize(getToolkit().getScreenSize());
        this.add(visualizer);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                visualizer.stop();
                dispose();
                System.exit(0);
            }
        });
        visualizer.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                visualizer.stop();
                dispose();
                System.exit(0);
            }
        });
        this.repaint();
        visualizer.init();
        visualizer.start();

    }

    public void show() {
        super.show();
        getParent().setVisible(true);
    }


    public static final void main(final String args[]) {
        try {
            final ToInfinityAndBeyond2 infinity = new ToInfinityAndBeyond2();
            infinity.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }



 }
