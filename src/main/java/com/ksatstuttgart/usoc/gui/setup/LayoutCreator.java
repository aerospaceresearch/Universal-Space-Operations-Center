package com.ksatstuttgart.usoc.gui.setup;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.gui.setup.configuration.Properties;
import com.ksatstuttgart.usoc.gui.setup.configuration.Parsable;
import com.ksatstuttgart.usoc.gui.setup.pane.GeneralPane;
import com.ksatstuttgart.usoc.gui.setup.pane.LogPane;
import com.ksatstuttgart.usoc.gui.setup.pane.StatePanelPane;
import com.ksatstuttgart.usoc.gui.setup.pane.USOCPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * The Layout Creator Window
 */
public class LayoutCreator extends BorderPane {

    /**
     * Window Title
     */
    private static final String SCENE_TITLE = "Layout Creator";

    /**
     * Window Width
     */
    private static final int WINDOW_WIDTH = 700;

    /**
     * Window Height
     */
    private static final int WINDOW_HEIGHT = 570;

    /**
     * Default Pane Border
     */
    private static final Border DEFAULT_PANE_BORDER =
            new Border(new BorderStroke(Color.DARKGRAY,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

    /**
     * Properties Pane Title
     */
    private static final String PROPERTIES_PANE_TITLE = "Properties";

    /**
     * General Pane Title
     */
    private static final String GENERAL_PANE_TITLE = "General";

    /**
     * Panels Pane Title
     */
    private static final String PANELS_PANE_TITLE = "Panels";

    /**
     * State Panel Pane Title
     */
    private static final String STATE_PANE_TITLE = "State Panel";

    /**
     * USOC Panel Pane Title
     */
    private static final String USOC_PANE_TITLE = "USOC Panel";

    /**
     * Log Panel Pane Title
     */
    private static final String LOG_PANE_TITLE = "Log Panel";

    /**
     * Holds all right side panes
     * Used to properly change the right panel contents
     * when a TreeItem is clicked
     */
    private Map<String, Parsable> componentsMap = new HashMap<>();

    /**
     * Creates a new instance of the Layout Creator Class
     */
    public LayoutCreator() {
        setProperties();
        createRightPanes();
        prepareComponents();
    }

    /**
     * Sets Scene/Window Properties
     */
    private void setProperties() {
        Stage mainStage = MainController.getInstance().getStage();

        mainStage.setTitle(SCENE_TITLE);
        mainStage.setMinWidth(WINDOW_WIDTH);
        mainStage.setMinHeight(WINDOW_HEIGHT);
        mainStage.setResizable(false);
        mainStage.centerOnScreen();

        setPadding(new Insets(10, 0, 10, 30));
    }

    /**
     * Creates all Right Side Panes
     */
    private void createRightPanes() {
        componentsMap.put(PROPERTIES_PANE_TITLE, null);
        componentsMap.put(PANELS_PANE_TITLE, null);
        componentsMap.put(GENERAL_PANE_TITLE, prepareGeneralPane());
        componentsMap.put(STATE_PANE_TITLE, prepareStatePane());
        componentsMap.put(USOC_PANE_TITLE, prepareUSOCPane());
        componentsMap.put(LOG_PANE_TITLE, prepareLogPane());
    }

    /**
     * Prepares window components and layouts
     */
    private void prepareComponents() {
        // Prepare Header
        prepareHeader();

        setLeft(prepareTreeViewPane());

        prepareBottom();
    }

    /**
     * Prepares Scene Header
     */
    private void prepareHeader() {
        Label headerLabel = new Label("Elements");
        headerLabel.setAlignment(Pos.CENTER_LEFT);
        headerLabel.setFont(new Font(25));

        setTop(headerLabel);
        setMargin(headerLabel, new Insets(20));
    }

    private void prepareBottom() {
        Button confirmButton = new Button("Confirm");
        confirmButton.setAlignment(Pos.CENTER);
        confirmButton.setPrefWidth(200);
        confirmButton.setOnAction(actionEvent -> {
            Properties properties = MainController.getInstance().getProperties();
            for (Parsable parsableComponent : componentsMap.values()) {
                parsableComponent.writeToPOJO(properties);
            }
        });

        HBox buttonBox = new HBox(confirmButton);
        buttonBox.setAlignment(Pos.CENTER);

        setBottom(buttonBox);
        setMargin(buttonBox, new Insets(20));
    }

    /**
     * Prepares Tree View pane
     *
     * @return Group containing the TreeView
     */
    private Group prepareTreeViewPane() {
        Group treeViewGroup = new Group();

        // Root Item (Properties)
        TreeItem<String> rootItem = new TreeItem<>(PROPERTIES_PANE_TITLE);

        // General Item
        TreeItem<String> generalItem = new TreeItem<>(GENERAL_PANE_TITLE);

        // Panel Item
        TreeItem<String> panelItem = new TreeItem<>(PANELS_PANE_TITLE);

        // State Panel
        TreeItem<String> statePanelItem = new TreeItem<>(STATE_PANE_TITLE);

        // USOC Panel
        TreeItem<String> usocPanelItem = new TreeItem<>(USOC_PANE_TITLE);

        // Log Panel
        TreeItem<String> logPanelItem = new TreeItem<>(LOG_PANE_TITLE);

        rootItem.setExpanded(true);
        panelItem.setExpanded(true);
        panelItem.getChildren().addAll(statePanelItem, usocPanelItem, logPanelItem);
        rootItem.getChildren().addAll(generalItem, panelItem);

        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setEditable(false);
        treeView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {

            if (oldValue != null) {
                setCenter(null);
            }

            Node newNode = (Node) componentsMap.get(newValue.getValue());
            if (newNode != null) {
                setCenter(newNode);
                setMargin(newNode, new Insets(0, 30, 0, 10));
            }
        });

        treeView.setMaxWidth(150);

        treeViewGroup.getChildren().add(treeView);

        return treeViewGroup;
    }

    /**
     * Creates the General Pane
     *
     * @return generalPane
     */
    private Parsable prepareGeneralPane() {
        GeneralPane pane = new GeneralPane();
        pane.setBorder(DEFAULT_PANE_BORDER);

        return pane;
    }

    /**
     * Creates the State Panel Pane
     *
     * @return statePane
     */
    private Parsable prepareStatePane() {
        StatePanelPane statePanelPane = new StatePanelPane();
        statePanelPane.setBorder(DEFAULT_PANE_BORDER);

        return statePanelPane;
    }

    /**
     * Creates the USOC Panel Pane
     *
     * @return usocPane
     */
    private Parsable prepareUSOCPane() {
        USOCPane usocPane = new USOCPane();
        usocPane.setBorder(DEFAULT_PANE_BORDER);

        return usocPane;
    }

    /**
     * Creates the Log Panel Pane
     *
     * @return logPane
     */
    private Parsable prepareLogPane() {
        LogPane logPane = new LogPane();
        logPane.setBorder(DEFAULT_PANE_BORDER);

        return logPane;
    }
}
