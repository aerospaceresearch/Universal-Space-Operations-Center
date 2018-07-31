package com.ksatstuttgart.usoc.gui.setup.configuration;

/**
 * POJO class for the General Layout
 *
 * @author Pedro Portela (Pedro12909)
 */
public class Layout {

    /**
     * Protocol Name
     */
    private String protocolName;

    /**
     * Experiment Name
     */
    private String experimentName;

    /**
     * Should window open maximized
     */
    private boolean maximized;

    /**
     * Width of window (if not maximized)
     */
    private int width;

    /**
     * Height of window (if not maximized)
     */
    private int height;

    /**
     * Should window be resizable
     */
    private boolean resizable;

    /**
     * All configuration items related with the State Pane
     */
    private StatePaneProperties statePaneProperties;

    /**
     * All configuration items related with the USOC Pane
     */
    private USOCPaneProperties usocPaneProperties;

    /**
     * All configuration items related with the Log Pane
     */
    private LogPaneProperties logPaneProperties;

    public Layout() {
        statePaneProperties = new StatePaneProperties();
        usocPaneProperties = new USOCPaneProperties();
        logPaneProperties = new LogPaneProperties();
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
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
