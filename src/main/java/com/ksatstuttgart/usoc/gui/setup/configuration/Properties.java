package com.ksatstuttgart.usoc.gui.setup.configuration;

/**
 * POJO class for the General Properties
 */
public class Properties {

    private String layoutName;

    private boolean fullScreen;

    private int width;

    private int height;

    private boolean resizable;

    private StatePaneProperties statePaneProperties;

    private USOCPaneProperties usocPaneProperties;

    private LogPaneProperties logPaneProperties;

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public StatePaneProperties getStatePaneProperties() {
        return statePaneProperties;
    }

    public void setStatePaneProperties(StatePaneProperties statePaneProperties) {
        this.statePaneProperties = statePaneProperties;
    }

    public USOCPaneProperties getUsocPaneProperties() {
        return usocPaneProperties;
    }

    public void setUsocPaneProperties(USOCPaneProperties usocPaneProperties) {
        this.usocPaneProperties = usocPaneProperties;
    }

    public LogPaneProperties getLogPaneProperties() {
        return logPaneProperties;
    }

    public void setLogPaneProperties(LogPaneProperties logPaneProperties) {
        this.logPaneProperties = logPaneProperties;
    }
}
