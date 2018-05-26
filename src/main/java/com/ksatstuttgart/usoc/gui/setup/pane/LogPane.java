package com.ksatstuttgart.usoc.gui.setup.pane;

import com.ksatstuttgart.usoc.gui.setup.configuration.Properties;
import com.ksatstuttgart.usoc.gui.setup.configuration.Parseable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

/**
 * Creates and prepares the LogPanel Pane
 */
public class LogPane extends GridPane implements Parseable {

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
     * Sets Pane Properties
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
        add(enabledCheckBox, 0, 0);
        add(serialPanelCheckBox, 0, 1);
        add(iridiumPanelCheckBox, 1, 1);
    }

    @Override
    public void writeToPOJO(Properties pojoClass) {

    }
}
