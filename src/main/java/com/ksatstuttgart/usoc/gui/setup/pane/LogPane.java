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

package com.ksatstuttgart.usoc.gui.setup.pane;

import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import com.ksatstuttgart.usoc.gui.setup.configuration.LogPaneProperties;
import com.ksatstuttgart.usoc.gui.setup.configuration.Parsable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

/**
 * Creates and prepares the LogPanel Pane
 *
 * @author Pedro Portela (Pedro12909)
 */
public class LogPane extends GridPane implements Parsable {

    /**
     * Pane enabled CheckBox
     */
    private final CheckBox enabledCheckBox =
            new CheckBox("Enabled");

    /**
     * Iridium Panel CheckBox
     */
    private final CheckBox iridiumPanelCheckBox =
            new CheckBox("Iridium Panel");

    /**
     * Serial Panel Enabled CheckBox
     */
    private final CheckBox serialPanelCheckBox =
            new CheckBox("Serial Panel");

    /**
     * Default Node Padding
     */
    private static final Insets DEFAULT_PADDING =
            new Insets(20);

    /**
     * Creates an instance of the Log Panel
     */
    public LogPane() {
        setProperties();

        prepareComponents();
    }

    /**
     * Sets Pane Layout
     */
    private void setProperties() {
        setPadding(DEFAULT_PADDING);
        setHgap(100);
        setVgap(40);
    }

    /**
     * Prepares Pane Components
     */
    private void prepareComponents() {
        enabledCheckBox.setSelected(true);
        enabledCheckBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            boolean selected = newValue;

            // If the component is not enabled/selected, deactivates both checkboxes
            serialPanelCheckBox.setDisable(!selected);
            iridiumPanelCheckBox.setDisable(!selected);
        });

        add(enabledCheckBox, 0, 0);
        add(serialPanelCheckBox, 0, 1);
        add(iridiumPanelCheckBox, 1, 1);
    }

    /**
     * Sets all Properties related to this class to a POJO instance
     *
     * @param pojoClass POJO Class to set properties
     */
    @Override
    public void writeToPOJO(Layout pojoClass) {
        LogPaneProperties properties = pojoClass.getLogPaneProperties();

        properties.setEnabled(enabledCheckBox.isSelected());
        properties.setSerialPanelEnabled(serialPanelCheckBox.isSelected());
        properties.setIridiumPanelEnabled(iridiumPanelCheckBox.isSelected());
    }
}
