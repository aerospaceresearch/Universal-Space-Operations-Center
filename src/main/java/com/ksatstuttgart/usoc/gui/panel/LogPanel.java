package com.ksatstuttgart.usoc.gui.panel;

import com.ksatstuttgart.usoc.gui.setup.USOCTabPane;

import java.util.Properties;

/**
 * Log Panel
 */
public class LogPanel extends USOCTabPane {

    /**
     * Creates an instance of the Log Panel
     * @param config configuration file containing all GUI properties
     */
    public LogPanel(Properties config) {
        //TODO Read JSON File and set needed parameters and values
        prepareComponents(config);
    }

    /**
     * Sets up components and prepares layouts
     * @param config configuration file containing all GUI properties
     */
    private void prepareComponents(Properties config) {
        if (Boolean.parseBoolean(config.getProperty("serialPanel"))) {
            addFXMLTab("fxml/SerialPanel.fxml", "Serial Connection");
        }

        //TODO: should be named something like Mail
        if (Boolean.parseBoolean(config.getProperty("iridiumPanel"))) {
            addFXMLTab("fxml/IridiumPanel.fxml", "Iridium Connection");
        }
        minWidth(200);
    }
}
