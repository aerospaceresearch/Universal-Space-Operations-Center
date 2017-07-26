/*
 * The MIT License
 *
 * Copyright 2017 KSat Stuttgart e.V..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ksatstuttgart.usoc.gui.worldwind;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.util.StatusBar;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author valentinstarlinger
 */
public class WorldWindPanel extends JPanel{
    
        WorldWindowGLCanvas wwd;

        public WorldWindPanel(WorldWindowGLCanvas shareWith, int width, int height)
        {
            // To share resources among World Windows, pass the first World Window to the constructor of the other
            // World Windows.
            this.wwd = shareWith != null ? new WorldWindowGLCanvas(shareWith) : new WorldWindowGLCanvas();
            this.wwd.setSize(new java.awt.Dimension(width, height));

            this.setLayout(new BorderLayout(5, 5));
            this.add(this.wwd, BorderLayout.CENTER);

            StatusBar statusBar = new StatusBar();
            statusBar.setBorder(new EmptyBorder(5, 5, 5, 5));
            statusBar.setEventSource(wwd);
            this.add(statusBar, BorderLayout.SOUTH);
        }
}
