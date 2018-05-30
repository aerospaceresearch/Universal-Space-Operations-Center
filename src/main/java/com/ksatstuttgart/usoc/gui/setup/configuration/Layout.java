package com.ksatstuttgart.usoc.gui.setup.configuration;

/**
 * POJO class for the General Layout
 */
public class Layout {

    private String experimentName;

    private boolean fullScreen;

    private int width;

    private int height;

    private boolean resizable;

    private StatePaneProperties statePaneProperties;

    private USOCPaneProperties usocPaneProperties;

    private LogPaneProperties logPaneProperties;

    public Layout() {
        statePaneProperties = new StatePaneProperties();
        usocPaneProperties = new USOCPaneProperties();
        logPaneProperties = new LogPaneProperties();
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
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
