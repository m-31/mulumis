// $Id$
//
// This file is part of the program suite "Simulum".
//
// Copyright 2001-2004  Michael Meyling <michael@at@meyling.com>.
//
// "Simulum" is free software; you can redistribute
// it and/or modify it under the terms of the GNU General Public
// License as published by the Free Software Foundation; either
// version 2 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

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
