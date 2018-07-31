package com.ksatstuttgart.usoc.gui.setup.configuration;

import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Chart;

import java.util.List;

/**
 * POJO Class used to hold all layout information
 * related to the USOC Pane
 *
 * @author Pedro Portela (Pedro12909)
 */
public class USOCPaneProperties {

    /**
     * Should the USOC Pane be enabled
     */
    private boolean enabled;

    /**
     * Should the GNSS View be enabled
     */
    private boolean gnssEnabled;

    /**
     * How many columns of charts should be presented
     */
    private int chartColumns;

    /**
     * List of shown Charts
     */
    private List<Chart> charts;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isGnssEnabled() {
        return gnssEnabled;
    }

    public void setGnssEnabled(boolean gnssEnabled) {
        this.gnssEnabled = gnssEnabled;
    }

    public int getChartColumns() {
        return chartColumns;
    }

    public void setChartColumns(int chartColumns) {
        this.chartColumns = chartColumns;
    }

    public List<Chart> getCharts() {
        return charts;
    }

    public void setCharts(List<Chart> charts) {
        this.charts = charts;
    }
}
