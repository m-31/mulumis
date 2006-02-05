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

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import com.meyling.mulumis.base.log.Trace;


/**
 * Integer field with Cut and Paste.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public class CPIntegerField extends CPTextField {

	private static final long serialVersionUID = 195861153293062811L;
	private final int minimum;
	private final int maximum;
	private final double len;
	private Integer value;

    /**
     * Constructor with initial text.
     *
     * @param   initialText Initial value.
     */
    public CPIntegerField(final Integer value, final int minimum, final int maximum) {
    	super();
        this.minimum = minimum;
        this.maximum = maximum;
        this.len = Math.ceil(Math.log(maximum) / Math.log(10));
        Trace.traceParam(this, "CPIntegerField", "len", len);
        setInternValue(value);
    }
    
    private final void setInternValue(final Integer value) {
    	if (value == null) {
    		this.value = null;
    		return;
    	}
    	if (value.intValue() < minimum || value.intValue() > maximum) {
    		throw new IllegalArgumentException("value is out of bound");
    	}
    	this.value = value;
    	Trace.traceParam(this, "setInternValue", "value", value);
    }

    public final void setValue(final Integer value) {
    	setInternValue(value);
    	setText(value == null ? "" : value.toString());
    }

	public final Integer getValue() {
    	return value;
    }

    protected Document createDefaultModel() {
        return new IntegerDocument();
    }
    
    class IntegerDocument extends PlainDocument {

		private static final long serialVersionUID = -5557503650423744894L;

		public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {

            if (str == null) {
                return;
            }
            StringBuffer buffer = new StringBuffer(str.length());
            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i))) {
                    buffer.append(str.charAt(i));
                }
            }
            // TODO mime 20060130: + 1???
            if (buffer.length() + getContent().length() > len) {
            	Toolkit.getDefaultToolkit().beep();
            }
            try {
            	super.insertString(offs, buffer.toString(), a);
	            if (buffer.length() + getContent().length() - 1 <= 0) {
	            	setInternValue(null);
	            } else {
	            	// TODO mime 20060130: why cut last position???
	            	setInternValue(new Integer(getContent().getString(0, getContent().length() - 1))); 
	            }
            } catch (IllegalArgumentException e) {
            	super.remove(offs, str.length());
            	Trace.trace(this, "insertString", e);
            	Toolkit.getDefaultToolkit().beep();
            }
        }
		
		public void remove(final int offs, int len) throws BadLocationException {
			super.remove(offs, len);
            if (getContent().length() - 1 <= 0) {
            	setInternValue(null);
            } else {
            	// TODO mime 20060130: why cut last position???
            	setInternValue(new Integer(getContent().getString(0, getContent().length() - 1))); 
            }
		}
    }

}
