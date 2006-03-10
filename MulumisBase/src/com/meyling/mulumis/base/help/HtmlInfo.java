// $Id$
//
// This file is part of the program suite "Principia Mathematica II"
// which is a working prototype for the main project:
// "Hilbert II" - http://www.qedeq.org
//
// Copyright 2001-2004  Michael Meyling <mime@qedeq.org>.
//
// "Principia Mathematica II" is free software; you can redistribute
// it and/or modify it under the terms of the GNU General Public
// License as published by the Free Software Foundation; either
// version 2 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.


package com.meyling.mulumis.base.help;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.util.BrowserLauncher;


/**
 * Show program information.
 *
 */
public class HtmlInfo extends JFrame {

    private JEditorPane html;

    /**
     * Main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        HtmlInfo demo = new HtmlInfo();
        demo.show();
    }

    /**
     * Constructor.
     */
    public HtmlInfo() {
        super("Information");
        final String method = "constructor";
        try {
            URL url = null;
            String path = null;
            try {
                path = "/com/meyling/mulumis/base/help/information.html";
                url = getClass().getResource(path);
            } catch (Exception e) {
                Trace.trace(this, method, "Failed to open resource " + path, e);
                url = null;
            }

            if (url != null) {
                html = new JEditorPane(url);
                html.setEditable(false);
                html.setEnabled(true);
                html.addHyperlinkListener(createHyperLinkListener());

                JScrollPane scroller = new JScrollPane();
                JViewport vp = scroller.getViewport();
                vp.add(html);
                this.getContentPane().add(scroller, BorderLayout.CENTER);
            }
        } catch (MalformedURLException e) {
            Trace.trace(this, method, e);
        } catch (IOException e) {
            Trace.trace(this, method, e);
        }
        this.setSize(700, 600);
    }

    public HyperlinkListener createHyperLinkListener() {
        return new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        ((HTMLDocument) html.getDocument()).processHTMLFrameHyperlinkEvent(
                            (HTMLFrameHyperlinkEvent) e);
                    } else {
                        try {
                            BrowserLauncher.openURL(e.getURL().toString());
                        } catch (IOException ioe) {
                            Trace.trace(this, "createHyperLinkListener", ioe);
                        }
                    }
                }
            }
        };
    }

}
