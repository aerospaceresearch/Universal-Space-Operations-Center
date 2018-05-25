package com.ksatstuttgart.usoc.gui.setup;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.gui.setup.pane.GeneralPane;
import com.ksatstuttgart.usoc.gui.setup.pane.StatePanelPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * The Layout Creator Window
 */
public class LayoutCreator extends VBox {

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
    private static final int WINDOW_HEIGHT = 520;

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
    private Map<String, Node> componentsMap = new HashMap<>();

    /**
     * Holds the tree view and the right side Pane
     */
    private BorderPane middleLayout = new BorderPane();

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

        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10, 0, 10, 30));
        setSpacing(10);
        setVgrow(this, Priority.ALWAYS);
    }

    /**
     * Creates all Right Side Panes
     */
    private void createRightPanes() {
        componentsMap.put(PROPERTIES_PANE_TITLE, new Pane());
        componentsMap.put(PANELS_PANE_TITLE, new Pane());
        componentsMap.put(GENERAL_PANE_TITLE, prepareGeneralPane());
        componentsMap.put(STATE_PANE_TITLE, prepareStatePane());
    }

    /**
     * Prepares window components and layouts
     */
    private void prepareComponents() {
        // Prepare Header
        prepareHeader();

        // Prepare Left and Right Panel
        preparePanels();
    }

    /**
     * Prepares Scene Header
     */
    private void prepareHeader() {
        Label headerLabel = new Label("Elements");
        headerLabel.setAlignment(Pos.CENTER_LEFT);
        headerLabel.setFont(new Font(25));
        getChildren().add(headerLabel);
    }

    /**
     * Prepares Left and Right Panel
     * (Tree View and right pane)
     */
    private void preparePanels() {
        middleLayout = new BorderPane();

        Group treeViewPanel = prepareTreeViewPane();

        middleLayout.setLeft(treeViewPanel);
        //TODO SetGeneral properties pane as default

        getChildren().add(middleLayout);
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
        treeView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<TreeItem<String>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<String>> observableValue,
                                        TreeItem<String> oldValue, TreeItem<String> newValue) {

                        if (oldValue != null) {
                            Node oldNode = componentsMap.get(oldValue.getValue());
                            middleLayout.getChildren().remove(oldNode);
                        }

                        Node newNode = componentsMap.get(newValue.getValue());
                        if (newNode != null) {
                            middleLayout.setCenter(newNode);
                            middleLayout.setMargin(newNode, new Insets(0, 30, 0, 10));
                        }
                    }
                });

        treeView.setPrefSize(150, -1);

        treeViewGroup.getChildren().add(treeView);

        return treeViewGroup;
    }

    /**
     * Creates the General Pane
     *
     * @return generalPane
     */
    private Pane prepareGeneralPane() {
        GeneralPane pane = new GeneralPane();
        pane.setBorder(DEFAULT_PANE_BORDER);

        return pane;
    }

    /**
     * Create the State Panel Pane
     *
     * @return statePane
     */
    private Pane prepareStatePane() {
        StatePanelPane statePanelPane = new StatePanelPane();
        statePanelPane.setBorder(DEFAULT_PANE_BORDER);

        return statePanelPane;
    }
}
