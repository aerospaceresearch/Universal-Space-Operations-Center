package com.ksatstuttgart.usoc.gui.setup.configuration;

public class LogPaneProperties {

    private boolean enabled;

    private boolean serialPanelEnabled;

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
