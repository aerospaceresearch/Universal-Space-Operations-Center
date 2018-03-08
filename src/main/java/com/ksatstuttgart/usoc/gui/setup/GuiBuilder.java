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

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.gui.controller.ChartController;
import com.ksatstuttgart.usoc.gui.controller.StatePanelController;
import com.ksatstuttgart.usoc.gui.worldwind.GNSSPanel;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.*;

/**
 * This class builds the GUI FXML structure based on input parameters in the properties file
 *
 *
 * @author Victor Hertel
 * @version 2.0
 */
public class GuiBuilder {

    /**
     * Config file relative path
     */
    private static final String CONFIG_PATH = "config/config.properties";
    
    /**
     * Creates the main scene that will be added
     * to the Stage
     * @return Scene
     */
    public static Scene createGUIFromConfig() {
        // Loads the configuration file
        Properties config = ConfigHandler.getAllValues(CONFIG_PATH);

        // Sets experiment title as Stage title
        MainController.getInstance().getStage()
                .setTitle(config.getProperty("experimentName"));

        // Sets the BorderPane of the MainFrame
        BorderPane mainBorder = new BorderPane();
        mainBorder.setPrefSize(700,500);

        // Create the MenuBar
        MenuBar menuBar = createMenuBar();
        mainBorder.setTop(menuBar);

        SplitPane mainFrameSplitPane = new SplitPane();
        mainFrameSplitPane.setDividerPositions(
                0.15, 0.75
        );

        // Create the State Panel
        if (Boolean.parseBoolean(config.getProperty("statePanel"))) {
            ScrollPane statePanel = createStatePanel(config);
            mainFrameSplitPane.getItems().add(statePanel);
        }

        // Create the Charts Panel

        // The TabPane (Contains Charts and GNSS)
        TabPane mainTab = new TabPane();
        mainTab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        createTabsPanel(config, mainTab);
        mainTab.minWidth(320);
        mainFrameSplitPane.getItems().add(mainTab);

        // Create the Log Panel
        USOCTabPane logPanel = createLogPanel(config);
        // if not empty add the log view to the main pane
        if (!logPanel.getTabs().isEmpty()) {
            mainFrameSplitPane.getItems().add(logPanel);
        }

        mainBorder.setCenter(mainFrameSplitPane);
        return new Scene(mainBorder);
    }

    /**
     * Creates the menu bar for the main stage
     * @return menu bar
     */
    private static MenuBar createMenuBar() {
        // Main MenuBar
        MenuBar menuBar = new MenuBar();

        // Create File Menu
        Menu editMenu = new Menu("Edit");

        // Load Protocol Menu Item
        Menu loadProtocolSubMenu = new Menu("Protocol");

        // Get List of Protocols in /protocols
        List<String> protocols = getAvailableProtocols();

        // Below condition should never happen
        // By default, defaultProtocol is loaded
        if (protocols.isEmpty()) {
            loadProtocolSubMenu.setDisable(true);
        } else {
            // Toggle Group ensures only one radio button is selected at a time
            ToggleGroup group = new ToggleGroup();
            for (final String protocol : protocols) {
                RadioMenuItem radioMenuItem = new RadioMenuItem(protocol);
                radioMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        MainController.getInstance()
                                .loadProtocol("protocols/" + protocol);
                        Alert a = new Alert(Alert.AlertType.INFORMATION,
                                protocol, ButtonType.OK);
                        a.setTitle("Success");
                        a.setHeaderText("Protocol Loaded");
                        a.showAndWait();
                    }
                });
                group.getToggles().add(radioMenuItem);
                loadProtocolSubMenu.getItems().add(radioMenuItem);
            }
        }

        // Quit Menu Item
        MenuItem quitMenuItem = new MenuItem("Quit");
        quitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MainController.getInstance().getStage().close();
            }
        });

        // Adds all menu items to file menu
        editMenu.getItems().addAll(loadProtocolSubMenu, new SeparatorMenuItem(), quitMenuItem);

        // Adds all Menus to Menubar
        menuBar.getMenus().addAll(editMenu);

        return menuBar;
    }

    private static ScrollPane createStatePanel(Properties config) {
        ScrollPane parentPane = new ScrollPane();

        // All Segment Panes should be inside stateBox
        VBox stateBox = new VBox();
        stateBox.setSpacing(5);

        // Get number of segments from config
        final int segmentCount =
                ConfigHandler.countItems("segmentTitle", CONFIG_PATH);

        // Adds each segment to statePane
        for (int i = 0; i < segmentCount; i++) {
            // Gets current segment title
            final String segmentTitle =
                    config.getProperty("segmentTitle[" + (i + 1) + "]");

            // Gets all keywords for current segment
            GridPane labelGrid = new GridPane();
            final int labelCountInSegment =
                    ConfigHandler.countItems("keyword[" + (i + 1) + "]", CONFIG_PATH);

            // Keeps track of each segment's row and col
            // Needs to start at one because GridPane.add() starts at 1 and not 0
            int currentRow = 1;
            int currentCol = 1;

            for (int j = 0; j < labelCountInSegment; j++) {
                if (currentCol == 5) {
                    currentRow++;
                    currentCol = 1;
                }

                Label label =
                        new Label(config.getProperty("keyword[" + (i + 1) + "][" + (j + 1) + "]"));
                GridPane.setHgrow(label, Priority.SOMETIMES);
                GridPane.setVgrow(label, Priority.SOMETIMES);
                GridPane.setHalignment(label, HPos.CENTER);
                labelGrid.add(label, currentCol, currentRow);

                currentCol++;
            }

            // Each titledPane holds information about a single segment
            TitledPane titledPane = new TitledPane(segmentTitle, labelGrid);
            titledPane.setCollapsible(false);
            stateBox.getChildren().add(titledPane);
        }

        parentPane.setMinWidth(200);
        parentPane.setFitToWidth(true);
        parentPane.setFitToHeight(true);
        parentPane.setContent(stateBox);
        return parentPane;
    }

    /**
     * Creates the contents of the TabPane (Charts View and GNSS View)
     * @param config instante of config properties
     * @param mainTab the TabPane where both tabs will be added
     */
    private static void createTabsPanel(Properties config, TabPane mainTab) {
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
        mainTab.getTabs().add(chartTab);

        //TODO: Doesn't work yet with JavaFX -> doesn't close on window close
        if (Boolean.parseBoolean(config.getProperty("GNSS3dView"))) {
            Tab gnssTab = new Tab();
            StackPane gnssStack = new StackPane();
            GNSSPanel.addGNSSPaneltoStackPane(gnssStack);
            gnssTab.setText("GNSS 3D View");
            gnssTab.setContent(gnssStack);
            mainTab.getTabs().add(gnssTab);
        }
    }

    /**
     * Creates the Log Panel. Will only be added to the main Stage
     * if requested in the config file
     * @param config instance of config properties
     * @return
     */
    private static USOCTabPane createLogPanel(Properties config) {
        // Create the log views
        USOCTabPane logTab = new USOCTabPane();

        if (Boolean.parseBoolean(config.getProperty("serialPanel"))) {
            logTab.addFXMLTab("fxml/SerialPanel.fxml", "Serial Connection");
        }
        //TODO: should be named something like Mail
        if (Boolean.parseBoolean(config.getProperty("iridumPanel"))) {
            logTab.addFXMLTab("fxml/IridiumPanel.fxml", "Iridium Connection");
        }

        logTab.minWidth(200);
        return logTab;
    }

    private static List<String> getAvailableProtocols() {
        List<String> availableProtocols = new LinkedList<>();

        File[] allProtocols = new File("protocols/").listFiles();

        for (File f : allProtocols) {
            availableProtocols.add(f.getName());
        }

        return availableProtocols;
    }

}
