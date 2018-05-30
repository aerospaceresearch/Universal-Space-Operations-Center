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
