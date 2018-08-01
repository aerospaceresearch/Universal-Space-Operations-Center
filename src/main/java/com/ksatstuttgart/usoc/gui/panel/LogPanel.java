/*
 * MIT License
 *
 * Copyright (c) 2017 KSat e.V. and AerospaceResearch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.ksatstuttgart.usoc.gui.panel;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.gui.setup.USOCTabPane;
import com.ksatstuttgart.usoc.gui.setup.configuration.LogPaneProperties;

/**
 * Log Panel
 */
public class LogPanel extends USOCTabPane {

    /**
     * Properties Class
     */
    private LogPaneProperties properties;

    /**
     * Creates an instance of the Log Panel
     */
    public LogPanel() {
        properties = MainController.getInstance()
                .getLayout().getLogPaneProperties();
        prepareComponents();
    }

    /**
     * Sets up components and prepares layouts
     */
    private void prepareComponents() {
        if (properties.isSerialPanelEnabled()) {
            addFXMLTab("fxml/SerialPanel.fxml", "Serial Connection");
        }

        //TODO: should be named something like Mail
        if (properties.isIridiumPanelEnabled()) {
            addFXMLTab("fxml/IridiumPanel.fxml", "Iridium Connection");
        }

        minWidth(200);
    }
}
