package com.ksatstuttgart.usoc.gui.setup.configuration;

/**
 * POJO Class used to hold all layout information
 * related to the Log Pane
 *
 * @author Pedro Portela (Pedro12909)
 */
public class LogPaneProperties {

    /**
     * Should Log Pane be enabled
     */
    private boolean enabled;

    /**
     * Should Serial Panel be enabled
     */
    private boolean serialPanelEnabled;

    /**
     * Should Iridium Panel be enabled
     */
    private boolean iridiumPanelEnabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSerialPanelEnabled() {
        return serialPanelEnabled;
    }

    public void setSerialPanelEnabled(boolean serialPanelEnabled) {
        this.serialPanelEnabled = serialPanelEnabled;
    }

    public boolean isIridiumPanelEnabled() {
        return iridiumPanelEnabled;
    }

    public void setIridiumPanelEnabled(boolean iridiumPanelEnabled) {
        this.iridiumPanelEnabled = iridiumPanelEnabled;
    }
}
