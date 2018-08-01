/*
 * MIT License
 *
 * Copyright (c) 2017 KSat e.V. and AerospaceResearch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
