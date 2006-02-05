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

package com.meyling.mulumis.base.gui.window;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.meyling.mulumis.base.application.StarApplet;
import com.meyling.mulumis.base.config.Parameter;
import com.meyling.mulumis.base.config.SimulumProperties;
import com.meyling.mulumis.base.gui.field.CPDoubleField;
import com.meyling.mulumis.base.gui.field.CPIntegerField;
import com.meyling.mulumis.base.gui.field.CPTextField;
import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.simulator.SimulatorProperties;

/**
 * Show and edit preferences of this application. Start simulation.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class MainFrame extends JFrame {

	/** String length of double fields. Includes decimal seperator. */
    private static final int DOUBLE_LENGTH = 30;

	private static final long serialVersionUID = 9195725908114213914L;

    /** Width for big components inside this dialog. */
    private static final int CONTENTS_WIDTH = 800;

    private int contentsWidth = CONTENTS_WIDTH;
    
    /** Height for components inside this dialog. */
    private static final int CONTENT_HEIGHT = 17;

    /** X margin. */
    private static final int MARGIN_X = 33;

    private SimulumProperties properties;
    
    private StarApplet applet;

    /** Current y height. */
    private int y;

    private JButton current;

    private JButton copy;

    private CPIntegerField stars;
    private JLabel starsCurrent;

    private JComboBox movement;
    private JLabel movementCurrent;

    private CPDoubleField sensitivity;
    private JLabel sensitivityCurrent;

    private CPDoubleField zoom;
    private JLabel zoomCurrent;

    private CPDoubleField radius;
    private JLabel radiusCurrent;

	private CPTextField message;

    /**
     * Constructor.
     *
     * @param   title           Dialog title.
     * @param   configLocation  path of config file. 
     */
    public MainFrame(final String title) {
        super(title);
        final String method = "StarterDialog(String)";
        try {
            Trace.traceBegin(this, method);
            setSize(2 * MARGIN_X + CONTENTS_WIDTH, 400);
            setupView(2 * MARGIN_X + CONTENTS_WIDTH, 400);
        } catch (Throwable e) {
            Trace.trace(this, method, e);
            e.printStackTrace();
            System.exit(99);
        } finally {
            Trace.traceEnd(this, method);
        }
    }

    /**
     * Assembles the GUI components of the panel.
     */
    public final void setupView(int width, int height) {
        int deltay = 40;
    	int contentsWidth = width - 2 * MARGIN_X;
    	int contentsHight = height - 4 * deltay;
        final int startY = 21;
        y = 21;

        properties = new SimulumProperties();

        final Container contents = getContentPane();
        contents.removeAll();
        contents.setLayout(null);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                shutdown();
            }
        });
        this.addComponentListener(new ComponentAdapter() {
        	public void componentResized(ComponentEvent e) {
            	applet.stop();
            	applet.destroy();
        		System.out.println(e.paramString());
        		setupView(getSize().width, getSize().height);
                copyCurrentProperties();
        	}
        });
        
        setupFields(contentsWidth);

        // setup applet
        applet = new StarApplet();
        applet.setBounds(4 * MARGIN_X + contentsWidth / 6, startY, contentsWidth - contentsWidth / 6 - 3* MARGIN_X, contentsHight);
        contents.add(applet);
        fillProperties();
        applet.init();
        applet.start();
        y = 1 * deltay + contentsHight;
        
        setupMessageAndButtons(contentsWidth);

    }

	private void setupFields(int contentsWidth) {
        final Container contents = getContentPane();
		stars = createIntegerField(properties.get("stars"));
        contents.add(stars);
        starsCurrent = new JLabel();
        contents.add(starsCurrent);
        starsCurrent.setBounds((int)(1.2 * MARGIN_X + contentsWidth / 6), y, contentsWidth, CONTENT_HEIGHT);
        y += CONTENT_HEIGHT * 1.5;

        movement = createListField(properties.get("movement"));
        contents.add(movement);
        movementCurrent = new JLabel();
        contents.add(movementCurrent);
        movementCurrent.setBounds((int)(1.2 * MARGIN_X + contentsWidth / 6), y, contentsWidth, CONTENT_HEIGHT);
        y += CONTENT_HEIGHT * 1.5;
        
        sensitivity = createDoubleField(properties.get("sensitivity"), 0, 1000000000, DOUBLE_LENGTH);
        contents.add(sensitivity);
        sensitivityCurrent = new JLabel();
        contents.add(sensitivityCurrent);
        sensitivityCurrent.setBounds((int)(1.2 * MARGIN_X + contentsWidth / 6), y, contentsWidth, CONTENT_HEIGHT);
        y += CONTENT_HEIGHT * 1.5;

        zoom = createDoubleField(properties.get("zoom"), 0, 1000000000, DOUBLE_LENGTH);
        contents.add(zoom);
        zoomCurrent = new JLabel();
        contents.add(zoomCurrent);
        zoomCurrent.setBounds((int)(1.2 * MARGIN_X + contentsWidth / 6), y, contentsWidth, CONTENT_HEIGHT);
        y += CONTENT_HEIGHT * 1.5;

        radius = createDoubleField(properties.get("radius"), 0, 1000000000, DOUBLE_LENGTH);
        contents.add(radius);
        radiusCurrent = new JLabel();
        contents.add(radiusCurrent);
        radiusCurrent.setBounds((int)(1.2 * MARGIN_X + contentsWidth / 6), y, contentsWidth, CONTENT_HEIGHT);
        y += CONTENT_HEIGHT * 1.5;
        
	}

	private void setupMessageAndButtons(int contentsWidth) {
        final Container contents = getContentPane();

        message = new CPTextField();
        contents.add(message);
        message.setBounds(MARGIN_X, y, contentsWidth, CONTENT_HEIGHT);
        message.setBorder(null);
        message.setEditable(false);
        y += CONTENT_HEIGHT * 1.5;

        final JButton dflt = new JButton("Default");
        contents.add(dflt);
        dflt.setBounds(MARGIN_X, y, 90, 21);
        dflt.setToolTipText("Resets all parameters to default values.");
        dflt.addActionListener(new  ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
            	applet.stop();
            	applet.destroy();
                MainFrame.this.setupView(getSize().width, getSize().height);
            }
        });

        final JButton start = new JButton("Start");
        contents.add(start);
        start.setBounds(MARGIN_X + 90 + 21, y, 90, 21);
        start.setToolTipText("Starts the application.");
        start.addActionListener(new  ActionListener() {

            public void actionPerformed(final ActionEvent actionEvent) {
                final String method ="actionPerformed";
                try {
                    setResultMessage(true, "generating");
                    fillProperties();
                    applet.stop();
                    applet.destroy();
                    applet.init();
                    copyCurrentProperties();
                    applet.start();
                    Trace.trace(this, method, "successfully started");
                    setResultMessage(true, "successfully started");
                } catch (final Exception e) {
                    Trace.trace(this, method, e);
                    setResultMessage(false, e.toString());
                } catch (final Error e) {
                    Trace.trace(this, method, e);
                    setResultMessage(false, e.toString());
                    // seems to be the only solution to deal with this kind of error
                    if (!(e instanceof OutOfMemoryError)) {
                    	shutdown();
                    }
                }
    
                saveParameters();
            }
        });

        current = new JButton("Current");
        contents.add(current);
        current.setBounds(MARGIN_X + contentsWidth - 290 - 21, y, 90, 21);
        current.setToolTipText("Display current model properties.");
        current.addActionListener(new  ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
            	copyCurrentProperties();
            }
        });

        copy = new JButton("Copy");
        contents.add(copy);
//        copy.setEnabled(false);
        copy.setBounds(MARGIN_X + contentsWidth - 290 - 6 * 21, y, 90, 21);
        copy.setToolTipText("Copy current model properites into start parameters.");
        copy.addActionListener(new  ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
            	copyProperties();
            	copyCurrentProperties();
//                final SimulatorProperties properties = applet.getSimulator().getProperties();
//                openTextFileAtPosition();
            }
        });

        final JButton cancel = new JButton("Exit");
        contents.add(cancel);
        cancel.setBounds(MARGIN_X + contentsWidth - 90, y, 90, 21);
        cancel.setToolTipText("Exits the application.");
        cancel.addActionListener(new  ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
            	applet.stop();
            	applet.destroy();
                shutdown();
            }
        });
	}

    private void fillProperties() {
        final SimulatorProperties properties = applet.getProperties();
        final Integer i = stars.getValue();
        if (i != null) {
            properties.setStars(i.intValue());
        }
        
        final String m = (String) movement.getSelectedItem();
        if (m != null) {
            properties.setMovement(m);
        }
        
        final Double s = sensitivity.getValue();
        if (s != null) {
            properties.setSensitivity(s.doubleValue());
        }
        final Double r = radius.getValue();
        if (r != null) {
            properties.setRadius(r.doubleValue());
        }
        final Double z = zoom.getValue();
        if (z != null) {
            properties.setZoom(z.doubleValue());
        }
    }

    private void copyProperties() {
    	try {
    		Trace.traceBegin(this, "copyProperties");
	        final SimulatorProperties properties = applet.getCurrentProperties();
	    	stars.setValue(new Integer(properties.getStars()));
	        movement.setSelectedItem(properties.getMovement());
	    	sensitivity.setValue(new Double(properties.getSensitivity()));
	    	radius.setValue(new Double(properties.getRadius()));
	    	zoom.setValue(new Double(properties.getZoom()));
    	} catch (RuntimeException e) {
    		Trace.trace(this, "copyProperties", e);
    		throw e;
    	} finally {
    		Trace.traceEnd(this, "copyProperties");
    	}
    }

    private void copyCurrentProperties() {
        final SimulatorProperties properties = applet.getCurrentProperties();
    	starsCurrent.setText("" + properties.getStars());
    	movementCurrent.setText(properties.getMovement());
    	sensitivityCurrent.setText("" + properties.getSensitivity());
    	radiusCurrent.setText("" + properties.getRadius());
    	zoomCurrent.setText("" + properties.getZoom());
    }

    private void setResultMessage(final boolean ok, final String message) {
        if (ok) {
            this.message.setForeground(Color.GREEN);
        } else {
            this.message.setForeground(Color.RED);
        }
        this.message.setText(message);
    }
    
    /**
     * Add combo box field for list parameter.
     *
     * @param   parameter   Add combo box for this parameter.
     */
    private JComboBox createListField(final Parameter parameter) {
        final Container contents = getContentPane();
        final JLabel label = new JLabel(parameter.getLabel());
        contents.add(label);
        label.setBounds(MARGIN_X, y, contentsWidth, CONTENT_HEIGHT);
        y += CONTENT_HEIGHT;
        final Vector vector = new Vector(parameter.getList());
        final JComboBox comboBox = new JComboBox(vector);
        if (parameter.getStringValue() != null) {
            comboBox.setSelectedItem(parameter.getStringValue());
        }
        System.out.println("selected: " + comboBox.getSelectedItem());
        contents.add(comboBox);
        comboBox.setBounds(MARGIN_X, y, contentsWidth / 6, CONTENT_HEIGHT); // TODO mime 20050205: just Q & D
        comboBox.setToolTipText(parameter.getComment());
        System.out.println(comboBox);
        return comboBox;
    }

    /**
     * Add integer field for string parameter.
     *
     * @param   parameter   Add text field selector for this parameter.
     */
    CPIntegerField createIntegerField(final Parameter parameter) {
        final Container contents = getContentPane();
        final JLabel label = new JLabel(parameter.getLabel());
        contents.add(label);
        label.setBounds(MARGIN_X, y, contentsWidth, CONTENT_HEIGHT);
        y += CONTENT_HEIGHT;
//        final CPIntegerField integerField = new CPIntegerField(parameter.getIntegerValue(), 0, 99999);
        final CPIntegerField integerField = new CPIntegerField(parameter.getIntegerValue(), 0, 99999999);
        integerField.setValue(parameter.getIntegerValue());
        integerField.setToolTipText(parameter.getComment());
        integerField.setBounds(MARGIN_X, y, contentsWidth / 6, CONTENT_HEIGHT); // TODO mime 20050205: just Q & D
        return integerField;
    }

    /**
     * Add double field for double parameter.
     *
     * @param   parameter   Add text field selector for this parameter.
     */
    private CPDoubleField createDoubleField(final Parameter parameter, final double minimum, 
    		final double maximum, final int length) {
        final Container contents = getContentPane();
        final JLabel label = new JLabel(parameter.getLabel());
        contents.add(label);
        label.setBounds(MARGIN_X, y, contentsWidth, CONTENT_HEIGHT);
        y += CONTENT_HEIGHT;
        final CPDoubleField doubleField = new CPDoubleField(parameter.getDoubleValue(), 
        	minimum, maximum, length);
        doubleField.setValue(parameter.getDoubleValue());
        doubleField.setBounds(MARGIN_X, y, contentsWidth / 6, CONTENT_HEIGHT); // TODO mime 20050205: just Q & D
        doubleField.setToolTipText(parameter.getComment());
        return doubleField;
    }
    
    private void shutdown() {
        final String method = "shutdown()";
        dispose();
        Trace.trace(this, method, "calling System.exit");
        System.exit(0);
    }

    /**
     * Save parameters.
     */
	private void saveParameters() {
		final String method = "saveParameters";
		Trace.trace(this, method, "saving parameters");
    	properties.get("stars").setValue(stars.getValue());
    	properties.get("movement").setValue((String) movement.getSelectedItem());
    	properties.get("sensitivity").setValue(sensitivity.getValue());
    	properties.get("radius").setValue(radius.getValue());
    	properties.get("zoom").setValue(zoom.getValue());
		try {
		    properties.save();
		} catch (IOException e) {
		    Trace.trace(this, method, e);
		}
	}
    

}
