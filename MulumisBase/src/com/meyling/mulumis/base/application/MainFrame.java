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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.meyling.mulumis.base.config.Parameter;
import com.meyling.mulumis.base.config.SimulumProperties;
import com.meyling.mulumis.base.gui.CPDoubleField;
import com.meyling.mulumis.base.gui.CPIntegerField;
import com.meyling.mulumis.base.gui.SpringUtility;
import com.meyling.mulumis.base.help.HtmlInfo;
import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.simulator.Simulator;
import com.meyling.mulumis.base.simulator.SimulatorAttributes;
import com.meyling.mulumis.base.view.CameraAttributes;
import com.meyling.mulumis.base.view.ViewChangedListener;
import com.meyling.mulumis.base.viewpoint.ManualMovement;

/**
 * Show and edit preferences of this application. Start simulation.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public final class MainFrame extends JFrame implements ViewChangedListener {

    /** String length of double fields. Includes decimal seperator. */
    private static final int DOUBLE_LENGTH = 30;

    /** X margin. */
    private static final int MARGIN_X = 33;

    /** X margin. */
    private static final int MARGIN_Y = 17;
    
    /** All saved properties. */
    private SimulumProperties simulumProperties;

    private SimulatorAttributes simulatorAttributes;

    private CameraAttributes cameraProperties;

    private FieldViewer viewer;
    
    private Simulator simulator;

    private CPIntegerField stars;

    private JComboBox movement;

    private CPDoubleField sensitivity;

    private CPDoubleField zoom;

    private CPDoubleField radius;

    private CPIntegerField snapshot;

    private CPDoubleField gamma;

    private CPDoubleField deltat;

    private JLabel impulseCurrent;
    
    private boolean editCameraFields;

    private boolean editGravityFields;
    
    /** Are we in maximized mode? */
    private boolean maximized;

    private JButton start;

    private JPanel visual;

    private JPanel simulation;

    private JPanel appletPanel;

    private JButton maximize;
    
    private JButton editGravity;

    private JLabel helpLabel;
    
    private JButton help;

    /** First time start of application before the animation starts? */
    private boolean firstTimeStart = true;


    /**
     * Constructor.
     *
     * @param   title           Dialog title.
     */
    public MainFrame(final String title) {
        super(title);
        final String method = "StarterDialog(String)";
        try {
            Trace.traceBegin(this, method);
            simulatorAttributes = new SimulatorAttributes(); 
            cameraProperties = new CameraAttributes();
            simulator = new Simulator(simulatorAttributes);
            loadParameters();
            viewer = new FieldViewer();
            viewer.init();        
            copyProperties();
            setBounds(getToolkit().getScreenSize().width * 1 / 8,
                getToolkit().getScreenSize().height * 1 / 16, 
                getToolkit().getScreenSize().width * 3 / 4, 
                getToolkit().getScreenSize().height * 6 / 7);
            setupView();
//            simulator.applyChanges(simulatorProperties);
//            viewer.applyVisualChanges(simulator, viewerProperties);
//            setSize(2 * MARGIN_X + CONTENTS_WIDTH, 500);
        } catch (Throwable e) {
            Trace.trace(this, method, e);
            e.printStackTrace();
            System.exit(99);
        } finally {
            Trace.traceEnd(this, method);
        }
    }

    public void show() {
        super.show();
        editCameraFields();
        editGravityFields();
        repaint();
    }
    
    /**
     * Assembles the GUI components of the panel.
     */
    public final void setupView() {
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
                Trace.traceParam(this, "componentResized", "e", e.paramString());
                if (viewer.isRunning()) {
                    viewer.stop();
                    setupSize();
                    viewer.start();
                } else {
                    setupSize();
                }
            }
            
            public void componentShown(ComponentEvent e) {
                setupSize();
            }
        });

        setupCamera();
        setupGravity();

        appletPanel = new JPanel();
        appletPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        contents.add(appletPanel);
        appletPanel.setLayout(null);
        appletPanel.add(viewer);
        fillProperties();
        
        help = new JButton("Help");
        help.setToolTipText("Some more information.");
        help.addActionListener(new ActionListener() {
            private HtmlInfo info;

            public void actionPerformed(final ActionEvent actionEvent) {
                if (info == null) {
                    info = new HtmlInfo();
                }
                info.show();
            }
        });
        contents.add(help);
        contents.add(helpLabel = new JLabel("drag mouse fast, roll wheel"));
        
        
        impulseCurrent = new JLabel();  // TODO mime 20060306: fill impulse information
        contents.add(impulseCurrent);
        
        setupSize();

    }

    /**
     * Restart viewer. Called by maximized window to restore the previous stage.
     * 
     * @param   properties  Probably changed values.    // TODO mime 20060306: missing: thetax, thetay
     */
    public synchronized void restart(CameraAttributes properties) {
        cameraProperties = properties;
        viewer.applyVisualChanges(simulator, properties);
        refreshProperties();
        copyProperties();
//        setupSize();
//        viewer.requestFocus();
//        startViewer();
        maximized = false;
        editCameraFields();
        editCameraFields();
    }
    
    /**
     * Recalculate the size of the sub components.
     */
    private void setupSize() {
        final int width = getContentPane().getWidth() - getContentPane().getInsets().left 
            - getContentPane().getInsets().right;
        Trace.traceParam(this, "setupSize", "width", width);
        final int height = getContentPane().getHeight() - getContentPane().getInsets().top 
            - getContentPane().getInsets().bottom;
        Trace.traceParam(this, "setupSize", "height", height);
        visual.setBounds(MARGIN_X, MARGIN_Y, 0, 0);
        SpringUtility.makeCompactGrid(visual,
//                visual.getComponentCount() / 2, 2,      //rows, cols
                visual.getComponentCount() / 1, 1,      //rows, cols
                6, 6,        //initX, initY
                6, 3);       //xPad, yPad
        Dimension size = visual.getPreferredSize();
        visual.setSize(size);
        
        simulation.setBounds(MARGIN_X, visual.getHeight() + visual.getY()
                + MARGIN_Y, 0, 0);
        SpringUtility.makeCompactGrid(simulation,
//                simulation.getComponentCount() / 2, 2,  //rows, cols
                simulation.getComponentCount() / 1, 1,  //rows, cols
                6, 6,        //initX, initY
                6, 3);       //xPad, yPad
        size = simulation.getPreferredSize();
        simulation.setSize(size);
        
        
        help.setLocation(MARGIN_X, simulation.getY() + simulation.getHeight() + MARGIN_Y);
        help.setSize(help.getPreferredSize());

        helpLabel.setLocation(MARGIN_X, help.getY() + help.getHeight() + MARGIN_Y);
        helpLabel.setSize(helpLabel.getPreferredSize());
        
        appletPanel.setBounds(2 * MARGIN_X + visual.getSize().width, MARGIN_Y, 
                width - 3 * MARGIN_X - visual.getSize().width, height - 2 * MARGIN_Y);

        viewer.setBounds(appletPanel.getInsets().left, appletPanel.getInsets().top, 
                appletPanel.getWidth() - appletPanel.getInsets().right - appletPanel.getInsets().left, 
                appletPanel.getHeight() - appletPanel.getInsets().top - appletPanel.getInsets().bottom);
// TODO mime 20060223: the following line is neccessary, so something must be wrong with the viewer        
        viewer.setSize(appletPanel.getWidth() - appletPanel.getInsets().right - appletPanel.getInsets().left, 
                appletPanel.getHeight() - appletPanel.getInsets().top - appletPanel.getInsets().bottom);
//        viewer.applyVisualChanges(simulator, viewerProperties);
        viewer.setBackground(Color.BLACK);
        
    }
    
    /**
     * Setup gravity parameter panel.
     */
    private void setupGravity() {
        final Container contents = getContentPane();

        simulation = new JPanel(new SpringLayout());
        simulation.setBorder(BorderFactory.createRaisedBevelBorder());
        contents.add(simulation);

        JLabel panelDescriptionGravity = new JLabel("Gravity");
        panelDescriptionGravity.setForeground(Color.BLUE);
        simulation.add(panelDescriptionGravity);
        final JButton gravity = new JButton(viewer != null && viewer.getViewer() != null 
                && viewer.getViewer().isGravityOn() ? "Stop" : "Start");
        contents.add(gravity);
        gravity.setToolTipText("Set gravity on or off.");
        gravity.addActionListener(new  ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                gravity.setText(viewer != null && viewer.getViewer() != null 
                        && viewer.getViewer().isGravityOn() ? "Start" : "Stop");
                viewer.getViewer().setGravityOn(!viewer.getViewer().isGravityOn());
            }
        });
        simulation.add(gravity);

        stars = createIntegerField(simulation, simulumProperties.get("stars"), 0, 99999999);
        simulation.add(stars);

        gamma = createDoubleField(simulation, simulumProperties.get("gamma"), 0, 1000000000, DOUBLE_LENGTH);
        simulation.add(gamma);

        deltat = createDoubleField(simulation, simulumProperties.get("deltat"), 0, 1000000000, DOUBLE_LENGTH);
        simulation.add(deltat);
        
        editGravity = new JButton("Edit");
            contents.add(editGravity);
            editGravity.setToolTipText("Edit or apply gravity parameters.");
            editGravity.addActionListener(new  ActionListener() {
                public void actionPerformed(final ActionEvent actionEvent) {
                    final String method ="actionPerformed";
                    try {
                        editGravityFields();
                    } catch (final Exception e) {
                        Trace.trace(this, method, e);
                        displayErrorMessage(e);
                    } catch (final Error e) {
                        Trace.trace(this, method, e);
                        displayErrorMessage(e);
                        // seems to be the only solution to deal with this kind of error
                        if (!(e instanceof OutOfMemoryError)) {
                            shutdown();
                        }
                    }
                }
            });
        simulation.add(editGravity);
        simulation.add(new JLabel(""));
    }

    /**
     * Setup camera parameter panel.
     */
    private void setupCamera() {
        final Container contents = getContentPane();

        visual = new JPanel(new SpringLayout());
        visual.setBorder(BorderFactory.createRaisedBevelBorder());
        contents.add(visual);

        JLabel panelDescriptionCamera = new JLabel("Camera");
        panelDescriptionCamera.setForeground(Color.BLUE);
        visual.add(panelDescriptionCamera);

        maximize = new JButton("Maximize");
        maximize.setToolTipText("Change to full screen mode.");
        maximize.addActionListener(new  ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                if (maximized) {    // we are already maximized
                    return;
                }
                maximized = true;
                stopViewer();
                refreshProperties();
                copyProperties();
                StarScreen screen = new StarScreen(simulator, cameraProperties, 
                    MainFrame.this, viewer);
                screen.show();
                screen = null;
            }
        });
        visual.add(maximize);

        sensitivity = createDoubleField(visual, simulumProperties.get("sensitivity"), 
            0, 1000000000d, DOUBLE_LENGTH);
        visual.add(sensitivity);

        zoom = createDoubleField(visual, simulumProperties.get("zoom"), 
            0, 1000000000d, DOUBLE_LENGTH);
        visual.add(zoom);

        radius = createDoubleField(visual, simulumProperties.get("radius"), 
            0, 100000000000d, DOUBLE_LENGTH);
        visual.add(radius);

        snapshot = createIntegerField(visual, simulumProperties.get("snapshot"), 
            0, 99999999);
        visual.add(snapshot);

        movement = createListField(visual, simulumProperties.get("movement"));
        visual.add(movement);

        start = new JButton("Edit");
        start.setToolTipText("Edit or apply camera parameters.");
        start.addActionListener(new  ActionListener() {
            
            public void actionPerformed(final ActionEvent actionEvent) {
                final String method ="actionPerformed";
                try {
                    editCameraFields();
                } catch (final Exception e) {
                    Trace.trace(this, method, e);
                    displayErrorMessage(e);
                } catch (final Error e) {
                    Trace.trace(this, method, e);
                    displayErrorMessage(e);
                    // seems to be the only solution to deal with this kind of error
                    if (!(e instanceof OutOfMemoryError)) {
                        shutdown();
                    }
                }
                saveParameters();
            }
        });
        visual.add(start);

        visual.add(new JLabel(""));
    }
        
    /**
     * Display an error message.
     * 
     * @param   e   Error.
     */    
    private void displayErrorMessage(final Throwable e) {
        Trace.trace(this, "displayErrorMessage", e);
        JOptionPane.showMessageDialog(this, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Add combo box field for list parameter.
     * @param contentPane TODO
     * @param   parameter   Add combo box for this parameter.
     */
    private JComboBox createListField(Container contentPane, final Parameter parameter) {
        final Container contents = contentPane;
        final JLabel label = new JLabel(parameter.getLabel());
        contents.add(label);
        final Vector vector = new Vector(parameter.getList());
        final JComboBox comboBox = new JComboBox(vector);
        if (parameter.getStringValue() != null) {
            comboBox.setSelectedItem(parameter.getStringValue());
        }
        contents.add(comboBox);
        comboBox.setToolTipText(parameter.getComment());
        label.setLabelFor(comboBox);
        return comboBox;
    }

    /**
     * Add integer field for string parameter.
     * 
     * @param   contentPane Put new field here.
     * @param   parameter   Add text field selector for this parameter.
     */
    CPIntegerField createIntegerField(final Container contentPane, final Parameter parameter, 
            final int minimum, final int maximum) {
        final JLabel label = new JLabel(parameter.getLabel());
        contentPane.add(label);
        final CPIntegerField integerField = new CPIntegerField(parameter.getIntegerValue(), minimum, maximum);
        integerField.setValue(parameter.getIntegerValue());
        integerField.setToolTipText(parameter.getComment());
        integerField.setColumns(integerField.getColumns());
        label.setLabelFor(integerField);
        return integerField;
    }

    /**
     * Add double field for double parameter.
     * 
     * @param   contentPane Put new field here.
     * @param   parameter   Add text field selector for this parameter.
     */
    private CPDoubleField createDoubleField(final Container contentPane, final Parameter parameter,
            final double minimum, final double maximum, final int length) {
        final JLabel label = new JLabel(parameter.getLabel());
        contentPane.add(label);
        final CPDoubleField doubleField = new CPDoubleField(parameter.getDoubleValue(),
            minimum, maximum, length);
        doubleField.setValue(parameter.getDoubleValue());
        doubleField.setToolTipText(parameter.getComment());
        doubleField.setColumns(length / 3 + 2); // don't show all columns
        label.setLabelFor(doubleField);
        return doubleField;
    }

    /**
     * Exit application.
     */
    private void shutdown() {
        final String method = "shutdown()";
        stopViewer();
        viewer.destroy();
        viewer = null;
        dispose();
        Trace.trace(this, method, "calling System.exit");
        System.exit(0);
    }

    /**
     * Make camera fields editable or disable them.
     */
    private void editCameraFields() {
        if (!editCameraFields) {
            fillProperties();
            startViewer();
            // if it is a manual mover we animate also in the beginning, if it is the first time
            if (firstTimeStart) {
                try {
                    final ManualMovement mover = (ManualMovement) viewer.getViewer().getPositionCalculator();
                    if (!"manual".equals(viewer.getViewer().getMovement())) {    // TODO mime 20060306: move constant declaration
                        mover.setXtheta(-0.007);
                        mover.setYtheta(-0.000);
                    }
                } catch (Exception e) {
                    Trace.trace(this, "MainFrame", e);
                }
                firstTimeStart = false;
            }
        } else {
//            copyProperties();
            stopViewer();
        }
        movement.setEnabled(editCameraFields);
        sensitivity.setEnabled(editCameraFields);
        sensitivity.setCaretPosition(0);
        radius.setEnabled(editCameraFields);
        radius.setCaretPosition(0);
        zoom.setEnabled(editCameraFields);
        zoom.setCaretPosition(0);
        snapshot.setEnabled(editCameraFields);
        snapshot.setCaretPosition(0);
        start.setText(editCameraFields ? "Apply" : "Edit");
        editCameraFields = !editCameraFields;
    }
    
    private void editGravityFields() {
        fillProperties();
        simulator.applyChanges(simulatorAttributes);
        viewer.applyVisualChanges(simulator, cameraProperties);
        viewer.repaint();
        saveParameters();
        stars.setEnabled(editGravityFields);
        gamma.setEnabled(editGravityFields);
        deltat.setEnabled(editGravityFields);
        editGravity.setText((editGravityFields ? "Apply" : "Edit"));
        editGravityFields = !editGravityFields;
    }
    
    private synchronized void startViewer() {
        stopViewer();
//        simulator.start();
        simulator.applyChanges(simulatorAttributes);
        viewer.applyVisualChanges(simulator, cameraProperties);
        viewer.addViewChangedListener(this);
        viewer.start();
    }
    
    private synchronized void stopViewer() {
        if (simulator != null) {
            simulator.stop();
        }
        if (viewer != null) {
            viewer.stop();
        }
        saveParameters();
    }

    /**
     * Fill model and viewer parameters with GUI parameters.
     */
    private void fillProperties() {
        final Integer i = stars.getValue();
        if (i != null) {
            simulatorAttributes.setStars(i.intValue());
        }
        final String m = (String) movement.getSelectedItem();
        if (m != null) {
            cameraProperties.setMovement(m);
        }
        final Double s = sensitivity.getValue();
        if (s != null) {
            cameraProperties.setSensitivity(s.doubleValue());
        }
        final Double r = radius.getValue();
        if (r != null) {
            cameraProperties.setRadius(r.doubleValue());
        }
        final Double z = zoom.getValue();
        if (z != null) {
            cameraProperties.setZoom(z.doubleValue());
        }
        final Integer n = snapshot.getValue();
        if (n != null) {
            cameraProperties.setSnapshot(n.intValue());
        }
        final Double g = gamma.getValue();
        if (g != null) {
            simulatorAttributes.setGamma(g.doubleValue());
        }
        final Double t = deltat.getValue();
        if (t != null) {
            simulatorAttributes.setDeltat(t.doubleValue());
        }
    }

    /**
     * Init view and model parameters with saved values from file.
     */
    private void loadParameters() {
        simulumProperties = new SimulumProperties();
        cameraProperties.setMovement(simulumProperties.get("movement").getStringValue());
        cameraProperties.setSensitivity(simulumProperties.get("sensitivity").getDoubleValue().doubleValue());
        cameraProperties.setRadius(simulumProperties.get("radius").getDoubleValue().doubleValue());
        cameraProperties.setZoom(simulumProperties.get("zoom").getDoubleValue().doubleValue());
        cameraProperties.setSnapshot(simulumProperties.get("snapshot").getIntegerValue().intValue());
        simulatorAttributes.setStars(simulumProperties.get("stars").getIntegerValue().intValue());
        simulatorAttributes.setGamma(simulumProperties.get("gamma").getDoubleValue().doubleValue());
        simulatorAttributes.setDeltat(simulumProperties.get("deltat").getDoubleValue().doubleValue());
    }

    /**
     * Save view and model parameters into file.
     */
    private void saveParameters() {
        final String method = "saveParameters";
        Trace.trace(this, method, "saving parameters");
        simulumProperties.get("stars").setValue(stars.getValue());
        simulumProperties.get("movement").setValue((String) movement.getSelectedItem());
        simulumProperties.get("sensitivity").setValue(sensitivity.getValue());
        simulumProperties.get("radius").setValue(radius.getValue());
        simulumProperties.get("zoom").setValue(zoom.getValue());
        simulumProperties.get("snapshot").setValue(snapshot.getValue());
        simulumProperties.get("gamma").setValue(gamma.getValue());
        simulumProperties.get("deltat").setValue(deltat.getValue());
        try {
            simulumProperties.save();
        } catch (IOException e) {
            Trace.trace(this, method, e);
        }
    }

    public void zoomChanged() {
        final CameraAttributes view = viewer.getProperties();
        zoom.setValue(new Double(view.getZoom()));
        zoom.setCaretPosition(0);
    }

    public void sensitivityChanged() {
        final CameraAttributes view = viewer.getProperties();
        sensitivity.setValue(new Double(view.getSensitivity()));
        sensitivity.setCaretPosition(0);
    }

    public void radiusChanged() {
        final CameraAttributes view = viewer.getProperties();
        radius.setValue(new Double(view.getRadius()));
        radius.setCaretPosition(0);
    }

    public void snapshotChanged() {
        final CameraAttributes view = viewer.getProperties();
        snapshot.setValue(new Integer(view.getSnapshot()));
        snapshot.setCaretPosition(0);
    }
/*
    private void startSimulator() {
        stopViewer();
        viewer.init();
//        copyProperties();
        simulator = new Simulator(simulatorProperties);
        viewer.applyVisualChanges(simulator, viewerProperties);
        viewer.start();
        setResultMessage(true, "viewer started");
        started = true;
        start.setText(started ? "Stop" : "Start");
    }
    
    private void stopSimulator() {
        started = false;
        if (viewer != null) {
            viewer.stop();
            viewer.destroy();
        }
        setResultMessage(true, "viewer stopped");
        start.setText(started ? "Stop" : "Start");
    }
*/
    
    /**
     * Copy current view and model properties into editable text fields.
     */
    private void copyProperties() {
        try {
            Trace.traceBegin(this, "copyProperties");
            if (viewer == null) {
                return;
            }
            final CameraAttributes view = viewer.getProperties();
            if (view == null) {
                return;
            }
            movement.setSelectedItem(view.getMovement());
            sensitivity.setValue(new Double(view.getSensitivity()));
            radius.setValue(new Double(view.getRadius()));
            zoom.setValue(new Double(view.getZoom()));
            snapshot.setValue(new Integer(view.getSnapshot()));
            stars.setValue(new Integer(simulatorAttributes.getStars()));
            gamma.setValue(new Double(simulatorAttributes.getGamma()));
            deltat.setValue(new Double(simulatorAttributes.getDeltat()));
//          TODO show current impulse
//          impulseCurrent.setText("" + applet.getSimulator().getImpulse());
        } catch (RuntimeException e) {
            Trace.trace(this, "copyProperties", e);
            throw e;
        } finally {
            Trace.traceEnd(this, "copyProperties");
        }
    }

    
    /**
     * Get current view properties and fill them in properties.
     */
    private void refreshProperties() {
        try {
            Trace.traceBegin(this, "refreshProperties");
            if (viewer == null) {
                return;
            }
            final CameraAttributes v = viewer.getProperties();
            if (v == null) {
                return;
            }
            cameraProperties.setMovement(v.getMovement());
            cameraProperties.setSensitivity(v.getSensitivity());
            cameraProperties.setRadius(v.getRadius());
            cameraProperties.setZoom(v.getZoom());
            cameraProperties.setSnapshot(v.getSnapshot());
        } finally {
            Trace.traceEnd(this, "refreshProperties");
        }
    }


    private void maximizeNotWorking() {
        System.out.println("nothing");
/*                
//                final Window screen = new Window(MainFrame.this);
            final JFrame maxi = new JFrame();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // used for full-screen exclusive mode  
            GraphicsDevice gd;
            Graphics gScr;
            BufferStrategy bufferStrategy;
            gd = ge.getDefaultScreenDevice();

            maxi.setUndecorated(true);    // no menu bar, borders, etc. or Swing components
            maxi.setIgnoreRepaint(true);  // turn off all paint events since doing active rendering
            maxi.setResizable(false);

            if (!gd.isFullScreenSupported()) {
              System.out.println("Full-screen exclusive mode not supported");
              System.exit(0);
            }
            
            final boolean run = viewer.isRunning();
            if (run) {
                viewer.stop();
            }
//                appletPanel.remove(viewer);
            
            final Window screen = new Window(maxi);
//                gd.setFullScreenWindow(screen); // switch on full-screen exclusive mode

            final FieldViewer viewer = new FieldViewer(MainFrame.this.viewer);
            screen.setSize(getToolkit().getScreenSize());
            screen.setLayout(null);
            screen.addNotify();

            viewer.setSize(getToolkit().getScreenSize().width, 
                    getToolkit().getScreenSize().height);
            
//                MainFrame.this.hide();
//                MainFrame.this.setState(Frame.ICONIFIED);
            screen.add(viewer);
            screen.getParent().addKeyListener(new KeyAdapter() {
                public void keyPressed(final KeyEvent e) {
                    e.consume();
                    screen.hide();
                    maxi.dispose();
                    System.out.println("helleo1");
                    System.exit(0);
                }
            });
            screen.repaint();
            viewer.applyVisualChanges(simulator, viewerProperties);
            viewer.init();
            viewer.start();
            MainFrame.this.hide();
//                 MainFrame.this.setState(JFrame.ICONIFIED);
            screen.show();
            screen.requestFocus();
//                screen.requestFocusInWindow();
            viewer.requestFocus();
//                viewer.requestFocusInWindow();
            
            } catch (Exception  e) {
                e.printStackTrace();
            }
             
            }
        });

/*                
            viewer.resize(getToolkit().getScreenSize().width,
                getToolkit().getScreenSize().height);
            maxi.requestFocus();
            maxi.setFocusableWindowState(true);
            
            viewer.getParent().addKeyListener(new KeyAdapter() {
                public void keyPressed(final KeyEvent e) {
                    System.out.println("helleo2");
                    System.exit(0);
                }
            });
            screen.getParent().addKeyListener(new KeyAdapter() {
                public void keyPressed(final KeyEvent e) {
                    System.out.println("helleo3");
                    System.exit(0);
                }
            });
/*                
            screen.addKeyListener(new KeyAdapter() {
                public void keyPressed(final KeyEvent e) {
                    System.exit(0);
                }
            });
            screen.getParent().addKeyListener(new KeyAdapter() {
                public void keyPressed(final KeyEvent e) {
                    System.exit(0);
                }
            });
            MainFrame.this.addKeyListener(new KeyAdapter() {
                public void keyPressed(final KeyEvent e) {
                    System.exit(0);
                }
            });
            screen.setEnabled(true);
//                if (run) {
                viewer.start();
//                }
            maxi.setFocusableWindowState(true);
            maxi.requestFocus();
            screen.requestFocus();
            viewer.requestFocus();
            getToolkit().addAWTEventListener(new AWTEventListener(){

                public void eventDispatched(AWTEvent event) {
                    System.out.println(event);
                    
                }}, AWTEvent.KEY_EVENT_MASK);
//                getToolkit().getSystemEventQueue();

            

            stopViewer();
            StarScreen screen = new StarScreen(simulator, viewer.getProperties());
            screen.show();
            screen = null;
*/
    }

    
    
}
