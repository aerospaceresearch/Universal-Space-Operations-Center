package com.ksatstuttgart.usoc.gui.panel;

import com.ksatstuttgart.usoc.gui.controller.ChartController;
import com.ksatstuttgart.usoc.gui.setup.ConfigHandler;
import com.ksatstuttgart.usoc.gui.worldwind.GNSSPanel;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Properties;

/**
 * Tab Panel
 */
public class TabPanel extends TabPane {

    /**
     * Config file relative path
     */
    private static final String CONFIG_PATH = "config/config.properties";

    /**
     * Creates an instance of the Tab Panel
     * @param config configuration file containing all GUI properties
     */
    public TabPanel(Properties config) {
        //TODO Read JSON File and set needed parameters and values
        prepareComponents(config);
    }

    /**
     * Sets up components and prepares layouts
     * @param config configuration file containing all GUI properties
     */
    private void prepareComponents(Properties config) {
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
        int maxColumns = Integer.parseInt(config.getProperty("chartColumns"));
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        ChartController chartController = new ChartController();

        // Setting
        for (int i = 0; i < ConfigHandler.countItems("chartTitle", CONFIG_PATH); i++) {
            //
            LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
            chart.setTitle(config.getProperty("chartTitle[" + (i+1) + "]"));
            chartController.addChart(chart);
            chartGrid.add(chart, column, row);

            //
            column++;
            if (column > (maxColumns-1)) {
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
        if (Boolean.parseBoolean(config.getProperty("GNSS3dView"))) {
            Tab gnssTab = new Tab();
            StackPane gnssStack = new StackPane();
            GNSSPanel.addGNSSPaneltoStackPane(gnssStack);
            gnssTab.setText("GNSS 3D View");
            gnssTab.setContent(gnssStack);

            getTabs().add(gnssTab);
        }
    }
}
