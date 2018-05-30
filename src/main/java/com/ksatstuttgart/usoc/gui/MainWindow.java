package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.gui.panel.LogPanel;
import com.ksatstuttgart.usoc.gui.panel.StatePanel;
import com.ksatstuttgart.usoc.gui.panel.USOCPanel;
import com.ksatstuttgart.usoc.gui.setup.USOCTabPane;
import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Main Application Window
 * Former Scene from GUIBuilder
 */
public class MainWindow extends BorderPane {

    public MainWindow() {
        createWindow();
    }

    /**
     * Creates the main application scene
     *
     * @return Scene
     */
    private void createWindow() {
        // Loads the configuration file
        Layout properties = MainController.getInstance().getLayout();

        // Sets experiment title as Stage title
        MainController.getInstance().getStage()
                .setTitle(properties.getExperimentName());

        //TODO set either fullscreen or fixed size
        // Sets the BorderPane of the MainFrame
        setPrefSize(700, 500);

        // Create the MenuBar
        setTop(prepareMenuBar());

        SplitPane mainFrameSplitPane = new SplitPane();
        mainFrameSplitPane.setDividerPositions(
                0.15, 0.75
        );

        // Create the State Panel
        if (properties.getStatePaneProperties().isEnabled()) {
            ScrollPane statePanel = new StatePanel();
            mainFrameSplitPane.getItems().add(statePanel);
        }

        // Create the Charts Panel
        // The USOCPanel (Contains Charts and GNSS)
        mainFrameSplitPane.getItems().add(new USOCPanel());

        // Create the Log Panel
        USOCTabPane logPanel = new LogPanel();

        // if not empty add the log view to the main pane
        if (!logPanel.getTabs().isEmpty()) {
            mainFrameSplitPane.getItems().add(logPanel);
        }

        setCenter(mainFrameSplitPane);
    }

    /**
     * Creates the menu bar for the main stage
     *
     * @return menu bar
     */
    private MenuBar prepareMenuBar() {
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
        quitMenuItem.setOnAction(actionEvent ->
                MainController.getInstance().getStage().close());

        // Adds all menu items to file menu
        editMenu.getItems().addAll(loadProtocolSubMenu, new SeparatorMenuItem(), quitMenuItem);

        // Adds all Menus to Menubar
        menuBar.getMenus().addAll(editMenu);

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
