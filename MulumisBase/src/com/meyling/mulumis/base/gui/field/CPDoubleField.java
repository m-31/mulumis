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
 * Double field with Cut and Paste.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public class CPDoubleField extends CPTextField {

	private static final long serialVersionUID = 8914097339960479977L;

	private Double value;
	
	private final double minimum;

	private final double maximum;

	private final int len;

    /**
     * Constructor with initial value.
     *
     * @param   value	Initial value.
     * @param	minimum	Minimum value, must be greater or equal to zero.
     * @param   maximum Maximum value.
     * @param	len		Maxium string length.
     */
    public CPDoubleField(final Double value, final double minimum, final double maximum, final int len) {
        super(value == null ? "" : value.toString());
        if (minimum < 0) {
        	throw new IllegalArgumentException("negative values are not allowed");
        }
        if (value != null && (value.doubleValue() < minimum || value.doubleValue() > maximum)) {
        	throw new IllegalArgumentException("initial value out of bounds");
        }
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.len = len;
    }

    private final void setInternValue(final Double value) {
    	if (value == null) {
    		this.value = null;
    		return;
    	}
    	if (value.doubleValue() < minimum || value.doubleValue() > maximum) {
    		throw new IllegalArgumentException("value is out of bound");
    	}
    	this.value = value;
    }

    public final void setValue(final Double value) {
    	Trace.traceParam(this, "setValue", "value", value);
    	setInternValue(value);
    	// TODO mime 20060205: shorten according to length
    	setText(value == null ? "" : value.toString());
    }
    	
    public final Double getValue() {
    	return value;
    }

    protected Document createDefaultModel() {
        return new DoubleDocument();
    }
    
    class DoubleDocument extends PlainDocument {

		private static final long serialVersionUID = 1694648039096424847L;

		public void insertString(int offs, String str, AttributeSet a) 
                throws BadLocationException {

            if (str == null) {
                return;
            }
            StringBuffer buffer = new StringBuffer(str.length());
            boolean noPoint = getContent().getString(0, getContent().length()).indexOf('.')  <  0;
            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i))) {
                    buffer.append(str.charAt(i));
                } else if (noPoint && '.' == str.charAt(i)) {
                    buffer.append(str.charAt(i));
                }
            }
            // TODO mime 20060130: + 1???
            if (buffer.length() + getContent().length() - 1 > len) {
                return;
            }
            try {
	            super.insertString(offs, buffer.toString(), a);
	            if (getContent().length() == 0) {
	            	setInternValue(null);
	            } else {
	            	// TODO mime 20060130: why cut last position???
	            	setInternValue(new Double(getContent().getString(0, getContent().length() - 1))); 
	            }
            } catch (IllegalArgumentException e) {
            	super.remove(offs, str.length());
            	Trace.trace(this, "insertString", e);
            	Toolkit.getDefaultToolkit().beep();
            }
        }
    }

}
