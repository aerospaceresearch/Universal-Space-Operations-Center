package com.ksatstuttgart.usoc.gui.panel;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.gui.controller.ChartController;
import com.ksatstuttgart.usoc.gui.setup.configuration.USOCPaneProperties;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Chart;
import com.ksatstuttgart.usoc.gui.worldwind.GNSSPanel;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Tab Panel
 */
public class USOCPanel extends TabPane {

    /**
     * Properties class
     */
    private USOCPaneProperties properties;

    /**
     * Creates an instance of the Tab Panel
     */
    public USOCPanel() {
        properties = MainController.getInstance()
                .getLayout().getUsocPaneProperties();
        prepareComponents();
    }

    /**
     * Sets up components and prepares layouts
     */
    private void prepareComponents() {
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        minWidth(320);

        // Charts Tab
        ScrollPane chartScroll = new ScrollPane();
        chartScroll.setFitToWidth(true);
        chartScroll.setFitToHeight(true);

        // Adds each chart to the chart grid
        GridPane chartGrid = new GridPane();
        int column = 0;
        int row = 0;
        int maxColumns = properties.getChartColumns();
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        ChartController chartController = new ChartController();

        // Setting
        for (Chart chart : properties.getCharts()) {
            //
            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle(chart.getTitle());
            chartController.addChart(lineChart);
            chartGrid.add(lineChart, column, row);

            //
            column++;
            if (column > (maxColumns - 1)) {
                row++;
                column = 0;
            }
        }

        // Add chart to main tabs
        chartScroll.setContent(chartGrid);
        Tab chartTab = new Tab();
        chartTab.setText("Charts");
        chartTab.setContent(chartScroll);

        getTabs().add(chartTab);

        //TODO: Doesn't work yet with JavaFX -> doesn't close on window close
        if (properties.isGnssEnabled()) {
            Tab gnssTab = new Tab();
            StackPane gnssStack = new StackPane();
            GNSSPanel.addGNSSPaneltoStackPane(gnssStack);
            gnssTab.setText("GNSS 3D View");
            gnssTab.setContent(gnssStack);

            getTabs().add(gnssTab);
        }
    }
}
