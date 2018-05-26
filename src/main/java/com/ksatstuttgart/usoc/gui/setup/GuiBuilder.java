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
import com.ksatstuttgart.usoc.gui.panel.LogPanel;
import com.ksatstuttgart.usoc.gui.panel.StatePanel;
import com.ksatstuttgart.usoc.gui.panel.TabPanel;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.ksatstuttgart.usoc.gui.setup.configuration.ConfigHandler;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        MenuBar menuBar = prepareMenuBar();
        mainBorder.setTop(menuBar);

        SplitPane mainFrameSplitPane = new SplitPane();
        mainFrameSplitPane.setDividerPositions(
                0.15, 0.75
        );

        // Create the State Panel
        if (Boolean.parseBoolean(config.getProperty("statePanel"))) {
            ScrollPane statePanel = new StatePanel(config);
            mainFrameSplitPane.getItems().add(statePanel);
        }

        // Create the Charts Panel
        // The TabPanel (Contains Charts and GNSS)
        mainFrameSplitPane.getItems().add(new TabPanel(config));

        // Create the Log Panel
        USOCTabPane logPanel = new LogPanel(config);
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
    private static MenuBar prepareMenuBar() {
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

    /**
     * Creates the initial window that is presented to the user
     * when launching the application
     *
     * @return a new scene that needs to be added to the stage
     */
    public static Scene createInitialWindow() {
        VBox parentLayout = new VBox();
        Label header = new Label("Please select an option:");
        header.autosize();

        HBox buttonLayout = new HBox();

        Button newLayoutBtn = new Button("New Layout");
        Button loadLayoutBtn = new Button("Import Layout");
        Button defaultLayoutBtn = new Button("Default Layout");

        // Sets Click Listeners (Event handlers) for all the buttons
        newLayoutBtn.setOnAction(newLayoutBtnEventHandler());
        loadLayoutBtn.setOnAction(loadLayoutBtnEventHandler());
        defaultLayoutBtn.setOnAction(defaultLayoutBtnEventHandler());

        buttonLayout.setSpacing(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(newLayoutBtn, loadLayoutBtn, defaultLayoutBtn);

        parentLayout.getChildren().addAll(header, buttonLayout);
        parentLayout.setSpacing(20);
        parentLayout.setAlignment(Pos.CENTER);

        parentLayout.setPadding(new Insets(10));

        Scene initialWindowPopup = new Scene(parentLayout);
        MainController.getInstance().getStage().setTitle("Universal Space Operations Center");
        return initialWindowPopup;
    }

    /**
     * Click Listener/Event Handler for the New Layout Button
     * @return EventHandler
     */
    private static EventHandler newLayoutBtnEventHandler() {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                MainController.getInstance().getStage()
                        .getScene().setRoot(new LayoutCreator());
            }
        };
    }

    /**
     * Click Listener/Event Handler for the Load Layout Button
     * @return EventHandler
     */
    private static EventHandler<ActionEvent> loadLayoutBtnEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //TODO Implement event handler
            }
        };
    }

    /**
     * Click Listener/Event Handler for the Default Layout Button
     * @return EventHandler
     */
    private static EventHandler<ActionEvent> defaultLayoutBtnEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //TODO Implement event handler
            }
        };
    }

    /**
     * Gets a list of protocol names inside the protocols/ folder
     * @return list of available protocols
     */
    private static List<String> getAvailableProtocols() {
        List<String> availableProtocols = new LinkedList<>();

        File[] allProtocols = new File("protocols/").listFiles();

        for (File f : allProtocols) {
            availableProtocols.add(f.getName());
        }

        return availableProtocols;
    }
}
