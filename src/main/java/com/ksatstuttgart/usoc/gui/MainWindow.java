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

package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.gui.panel.LogPanel;
import com.ksatstuttgart.usoc.gui.panel.StatePanel;
import com.ksatstuttgart.usoc.gui.panel.USOCPanel;
import com.ksatstuttgart.usoc.gui.setup.configuration.ConfigHandler;
import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Main Application Window
 * Former Scene from GUIBuilder
 */
public class MainWindow extends BorderPane {

    /**
     * File Menu Title
     */
    private static final String FILE_MENU_TITLE = "File";

    /**
     * View Menu Title
     */
    private static final String VIEW_MENU_TITLE = "View";

    /**
     * Data Menu Title
     */
    private static final String DATA_MENU_TITLE = "Data";

    /**
     * Protocol Menu Title
     */
    private static final String PROTOCOL_MENU_TITLE = "Protocol";

    /**
     * Assign Data Menu Item Title
     */
    private static final String ASSIGN_DATA_MENU_ITEM = "Assign Data";

    /**
     * First divider position (relative)
     */
    private static final double FIRST_DIVIDER_POSITION = 0;

    /**
     * Second divider position (relative)
     */
    private static final double SECOND_DIVIDER_POSITION = 0.75;

    /**
     * Split Pane used to show dividers between panes
     */
    private SplitPane mainWindowSplitPane;

    /**
     * Instance of the State Panel
     */
    private StatePanel statePanel;

    /**
     * Instance of the USOC Panel
     */
    private USOCPanel usocPanel;

    /**
     * Instance of the Log Panel
     */
    private LogPanel logPanel;

    /**
     * Creates an instance of the main window
     */
    public MainWindow() {
        // Loads the configuration file
        Layout properties = MainController.getInstance().getLayout();

        setProperties(properties);
        createWindow(properties);
    }

    /**
     * Sets Window Properties
     */
    private void setProperties(Layout properties) {
        // Gets main stage
        Stage mainStage = MainController.getInstance().getStage();

        // Sets experiment title as Stage title
        mainStage.setTitle(properties.getExperimentName());

        // Handles window size
        double width;
        double height;
        if (properties.isMaximized()) {
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            width = screenSize.getWidth();
            height = screenSize.getHeight();
        } else {
            width = properties.getWidth();
            height = properties.getHeight();
        }
        mainStage.setWidth(width);
        mainStage.setHeight(height);

        // Handles window resizable
        mainStage.setResizable(properties.isResizable());
    }

    /**
     * Creates the main application scene
     *
     * @return Scene
     */
    private void createWindow(Layout properties) {
        // Create the MenuBar
        setTop(prepareMenuBar(properties));

        mainWindowSplitPane = new SplitPane();

        statePanel = new StatePanel();
        usocPanel = new USOCPanel();
        logPanel = new LogPanel();

        addPanelsToSplitPane(properties);

        setCenter(mainWindowSplitPane);
    }

    /**
     * Adds active panels to the Split Pane
     * @param properties instance of properties
     */
    private void addPanelsToSplitPane(Layout properties) {
        boolean isStatePanelEnabled = properties.getStatePaneProperties()
                .isEnabled();

        boolean isUSOCPanelEnabled = properties.getUsocPaneProperties()
                .isEnabled();

        boolean isLogPanelEnabled = properties.getLogPaneProperties()
                .isEnabled();

        mainWindowSplitPane.getItems().clear();

        if (isStatePanelEnabled) {
            mainWindowSplitPane.getItems().add(statePanel);
        }

        if (isUSOCPanelEnabled) {
            mainWindowSplitPane.getItems().add(usocPanel);
        }

        if (isLogPanelEnabled) {
            mainWindowSplitPane.getItems().add(logPanel);
        }

        if (mainWindowSplitPane.getItems().size() == 2) {
            mainWindowSplitPane.setDividerPositions(0.5);
        } else if (mainWindowSplitPane.getItems().size() == 3) {
            mainWindowSplitPane.setDividerPositions(FIRST_DIVIDER_POSITION,
                    SECOND_DIVIDER_POSITION);
        }
    }

    /**
     * Creates the menu bar for the main stage
     *
     * @return menu bar
     */
    private MenuBar prepareMenuBar(Layout properties) {
        // Main MenuBar
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu(FILE_MENU_TITLE);

        // Load Protocol Menu Item
        Menu loadProtocolSubMenu = new Menu(PROTOCOL_MENU_TITLE);
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
                radioMenuItem.setOnAction(actionEvent -> {
                    MainController.getInstance()
                            .loadProtocol("protocols/" + protocol);
                    Alert a = new Alert(Alert.AlertType.INFORMATION,
                            protocol, ButtonType.OK);
                    a.setTitle("Success");
                    a.setHeaderText("Protocol Loaded");
                    a.showAndWait();
                });

                if (protocol.equals(MainController.getInstance().getLayout().getProtocolName())) {
                    radioMenuItem.setSelected(true);
                }
                group.getToggles().add(radioMenuItem);
                loadProtocolSubMenu.getItems().add(radioMenuItem);
            }
        }

        MenuItem saveLayoutItem = new MenuItem("Save Layout");
        saveLayoutItem.setOnAction(onAction -> {
            try {
                ConfigHandler.writeConfigurationFile();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Save Layout to file");
                alert.setContentText("Your layout was saved successfully!");

                alert.showAndWait();
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Could not write to file");
                errorAlert.showAndWait();
            }
        });

        // Quit Menu Item
        MenuItem quitMenuItem = new MenuItem("Quit");
        quitMenuItem.setOnAction(actionEvent ->
                MainController.getInstance().getStage().close());

        fileMenu.getItems().addAll(loadProtocolSubMenu, new SeparatorMenuItem(),
                saveLayoutItem, new SeparatorMenuItem(), quitMenuItem);

        // View Menu
        Menu viewMenu = new Menu(VIEW_MENU_TITLE);
        CheckMenuItem statePanelItem = new CheckMenuItem("State Panel");
        statePanelItem.setOnAction(actionEvent -> {
            properties.getStatePaneProperties().setEnabled(statePanelItem.isSelected());
            addPanelsToSplitPane(properties);
        });

        CheckMenuItem usocPanelItem = new CheckMenuItem("USOC Panel");
        usocPanelItem.setOnAction(actionEvent -> {
            properties.getUsocPaneProperties().setEnabled(usocPanelItem.isSelected());
            addPanelsToSplitPane(properties);
        });
        CheckMenuItem logPanelItem = new CheckMenuItem("Log Panel");
        logPanelItem.setOnAction(actionEvent -> {
            properties.getLogPaneProperties().setEnabled(logPanelItem.isSelected());
            addPanelsToSplitPane(properties);
        });

        boolean isStatePanelEnabled = properties.getStatePaneProperties().isEnabled();

        boolean isUSOCPanelEnabled = properties.getUsocPaneProperties().isEnabled();

        boolean isLogPanelEnabled = properties.getLogPaneProperties().isEnabled();

        statePanelItem.setSelected(isStatePanelEnabled);
        usocPanelItem.setSelected(isUSOCPanelEnabled);
        logPanelItem.setSelected(isLogPanelEnabled);

        viewMenu.getItems().addAll(statePanelItem, usocPanelItem, logPanelItem);

        // Layout Menu
        Menu layoutMenu = new Menu(DATA_MENU_TITLE);
        MenuItem assignDataMenuItem = new MenuItem(ASSIGN_DATA_MENU_ITEM);
        assignDataMenuItem.setOnAction(actionEvent -> {
            new AssignDataWindow().showAndWait();

            // TODO Refresh States/Charts
        });

        layoutMenu.getItems().add(assignDataMenuItem);

        // Adds all Menus to Menubar
        menuBar.getMenus().addAll(fileMenu, viewMenu, layoutMenu);

        return menuBar;
    }

    /**
     * Gets a list of protocol names inside the protocols/ folder
     *
     * @return list of available protocols
     */
    private List<String> getAvailableProtocols() {
        List<String> availableProtocols = new LinkedList<>();

        File[] allProtocols = new File("protocols/").listFiles();

        for (File f : allProtocols) {
            availableProtocols.add(f.getName());
        }

        return availableProtocols;
    }

}
