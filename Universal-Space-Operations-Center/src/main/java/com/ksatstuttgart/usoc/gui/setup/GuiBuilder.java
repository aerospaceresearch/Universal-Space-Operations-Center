/*
 * The MIT License
 *
 * Copyright 2017 KSat Stuttgart e.V..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ksatstuttgart.usoc.gui.setup;

import com.ksatstuttgart.usoc.gui.controller.ChartController;
import com.ksatstuttgart.usoc.gui.controller.IridiumPanelController;
import com.ksatstuttgart.usoc.gui.controller.SerialPanelController;
import com.ksatstuttgart.usoc.gui.controller.StatePanelController;
import com.ksatstuttgart.usoc.gui.worldwind.GNSSPanel;
import java.util.Properties;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class builds the GUI FXML structure based on input parameters in the properties file
 *
 *
 * @author Victor Hertel
 * @version 2.0
 */
public class GuiBuilder {

    private static final String CONFIGPATH = "config/config.properties";

    /**
     * The title of the generated ground station is updated and reseted.
     *
     * @param stage
     * @param path
     */
    public static void setExperimentName(Stage stage, String path) {
        Properties config = ConfigHandler.getAllValues(path);
        stage.setTitle(config.getProperty("experimentName"));
    }

    
    /**
     * 
     *
     * @return 
     */
    public static Scene createGUIFromConfig() {
        
        // Loads the configuration file
        Properties config = ConfigHandler.getAllValues(CONFIGPATH);

        // Sets the BorderPane of the MainFrame
        BorderPane mainBorder = new BorderPane();
        mainBorder.setPrefSize(600,400);

        // Sets 
        TabPane mainTab = new TabPane();
        mainTab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        ScrollPane chartScroll = new ScrollPane();
        GridPane chartGrid = new GridPane();

        // 
        int column = 0;
        int row = 0;
        int maxColumns = Integer.parseInt(config.getProperty("chartColumns"));
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        ChartController chartController = new ChartController();

        // 
        for (int i = 0; i < ConfigHandler.countItems("chartTitle", CONFIGPATH); i++) {
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

        //
        chartScroll.setContent(chartGrid);
        Tab chartTab = new Tab();
        chartTab.setText("Charts");
        chartTab.setContent(chartScroll);
        mainTab.getTabs().add(chartTab);

        //
        if (Boolean.parseBoolean(config.getProperty("GNSS3dView"))) {
            Tab gnssTab = new Tab();
            StackPane gnssStack = new StackPane();
            GNSSPanel.addGNSSPaneltoStackPane(gnssStack);
            gnssTab.setText("GNSS 3D View");
            gnssTab.setContent(gnssStack);
            mainTab.getTabs().add(gnssTab);
        }

        // 
        mainBorder.setCenter(mainTab);

        // 
        USOCTabPane logTab = new USOCTabPane();

        // 
        if (Boolean.parseBoolean(config.getProperty("serialPanel"))) {
            logTab.addFXMLTab("fxml/SerialPanel.fxml");
        }
        // 
        if (Boolean.parseBoolean(config.getProperty("iridumPanel"))) {
            logTab.addFXMLTab("fxml/IridiumPanel.fxml");
        }
        // 
        if (!logTab.getTabs().isEmpty()) {
            mainBorder.setRight(logTab);
        }

        // 
        if (Boolean.parseBoolean(config.getProperty("statePanel"))) {
            //
            ScrollPane stateScroll = new ScrollPane();
            StatePanelController statePanelController = new StatePanelController();
            VBox stateBox = new VBox();
            
            //
            for (int i = 0; i < ConfigHandler.countItems("segmentTitle", CONFIGPATH); i++) {
                // 
                VBox vBox = new VBox();
                GridPane stateGrid = new GridPane();
                //
                column = 0;
                row = 1;
                maxColumns = 2;
                // 
                Label segmentTitle = new Label();
                segmentTitle.setText(config.getProperty("segmentTitle[" + i + "]"));
                stateGrid.add(segmentTitle, 0, 0);
                //
                for (int j = 0; j < ConfigHandler.countItems("keyword[" + (i + 1) + "]", CONFIGPATH); j++) {
                    // 
                    Label label = new Label();
                    // 
                    if (column == 0) {
                        label.setText(config.getProperty("keyword[" + (i + 1) + "][" + (j + 1) + "]"));
                    }
                    // 
                    if (column == 1) {
                        //TODO: get variable name
                        statePanelController.addLabel(label, "Test");
                    }
                    // 
                    stateGrid.add(label, column, row);
                    // 
                    column++;
                    if (column > (maxColumns-1)) {
                        row++;
                        column = 0;
                    }
                }
                // 
                vBox.getChildren().add(stateGrid);
                stateBox.getChildren().add(vBox);
            }
            // 
            stateScroll.setContent(stateBox);
            mainBorder.setLeft(stateScroll);
        }

        return new Scene(mainBorder);
    }
}
