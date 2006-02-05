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


package com.meyling.mulumis.base.gui.field;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;


/**
 * Text field with Cut and Paste.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public class CPTextField extends JTextField {

	private static final long serialVersionUID = -5925892673653612125L;

	/**
     * Constructor.
     */
    public CPTextField() {
        ClipboardListener clipboardactivator = new ClipboardListener(this);
        addMouseListener(clipboardactivator);
    }

    /**
     * Constructor with initial text.
     *
     * @param   initialText Initial value.
     */
    public CPTextField(final String initialText) {
        setDragEnabled(true);
        ClipboardListener clipboardactivator = new ClipboardListener(this);
        addMouseListener(clipboardactivator);
        setText(initialText);
    }

    /**
     * Clipboard listener.
     *
     * @version $Revision$
     * @author    Michael Meyling
     */
    private class ClipboardListener extends MouseAdapter implements ActionListener {

        /** Popup menu. */
        private final JPopupMenu popedit;

        /** Reference to text field. */
        private final CPTextField outer;


        /**
         * Constructor.
         *
         * @param   outer   Work with this field.
         */
        ClipboardListener(final CPTextField outer) {
            this.outer = outer;
            popedit = new JPopupMenu();
            JMenuItem jmenuitem = new JMenuItem("Copy");
            jmenuitem.addActionListener(this);
            jmenuitem.setActionCommand("copy");
            JMenuItem jmenuitem1 = new JMenuItem("Cut");
            jmenuitem1.addActionListener(this);
            jmenuitem1.setActionCommand("cut");
            JMenuItem jmenuitem2 = new JMenuItem("Paste");
            jmenuitem2.addActionListener(this);
            jmenuitem2.setActionCommand("paste");
            popedit.add(jmenuitem);
            popedit.add(jmenuitem1);
            popedit.add(jmenuitem2);
        }

        public void mousePressed(final MouseEvent mouseevent) {
            if (mouseevent.getModifiers() != 16) {
                popedit.show(outer, mouseevent.getX(), mouseevent.getY());
            }
        }

        public void actionPerformed(final ActionEvent actionevent) {
            final String s = actionevent.getActionCommand();
            if (s.equals("copy")) {
                outer.copy();
            } else if (s.equals("cut")) {
                outer.cut();
            } else if (s.equals("paste")) {
                outer.paste();
            }
        }

    }

}
