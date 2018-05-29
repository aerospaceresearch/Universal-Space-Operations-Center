package com.ksatstuttgart.usoc.gui.setup.configuration;

import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Chart;

import java.util.List;

public class USOCPaneProperties {

    private boolean enabled;

    private boolean gnssEnabled;

    private int chartColumns;

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
