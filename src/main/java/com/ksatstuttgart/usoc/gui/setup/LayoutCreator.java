package com.ksatstuttgart.usoc.gui.setup;

import com.ksatstuttgart.usoc.controller.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
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
     * Holds all right side panes
     * Used to properly change the right panel contents
     * when a TreeItem is clicked
     */
    private Map<String, Pane> componentsMap = new HashMap<>();

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
        createEmptyPanes();
        createGeneralPane();
    }

    /**
     * Creates Panes that are not supposed to show anything
     */
    private void createEmptyPanes() {
        componentsMap.put(PROPERTIES_PANE_TITLE, new Pane());
        componentsMap.put(PANELS_PANE_TITLE, new Pane());
    }

    /**
     * Creates the General Pane
     */
    private void createGeneralPane() {
        Pane generalPane = new Pane();

        VBox paneLayout = new VBox();

        paneLayout.getChildren().add(new Label("General"));
        paneLayout.setPadding(new Insets(30));
        paneLayout.setFillWidth(true);

        generalPane.getChildren().add(paneLayout);
        setPaneProperties(generalPane);

        componentsMap.put(GENERAL_PANE_TITLE, generalPane);
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
        //TODO Add General properties pane

        getChildren().add(middleLayout);
    }

    /**
     * Prepares Tree View pane
     * @return Group containing the TreeView
     */
    private Group prepareTreeViewPane() {
        Group treeViewGroup = new Group();

        //TODO Make background transparent

        // Root Item (Properties)
        TreeItem<String> rootItem = new TreeItem<>("Properties");

        // General Item
        TreeItem<String> generalItem = new TreeItem<>("General");

        // Panel Item
        TreeItem<String> panelItem = new TreeItem<>("Panels");

        // State Panel
        TreeItem<String> statePanelItem = new TreeItem<>("State Panel");

        // USOC Panel
        TreeItem<String> usocPanelItem = new TreeItem<>("USOC Panel");

        // Log Panel
        TreeItem<String> logPanelItem = new TreeItem<>("Log Panel");

        rootItem.setExpanded(true);
        panelItem.getChildren().addAll(statePanelItem, usocPanelItem, logPanelItem);
        rootItem.getChildren().addAll(generalItem, panelItem);

        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setEditable(false);
        treeView.getSelectionModel().select(0);

        treeView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<TreeItem<String>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<String>> observableValue,
                                        TreeItem<String> oldValue, TreeItem<String> newValue) {

                        Pane oldPane = componentsMap.get(oldValue.getValue());

                        if (oldPane != null) {
                           middleLayout.getChildren().remove(oldPane);
                        }

                        Pane newPane = componentsMap.get(newValue.getValue());
                        if(newPane != null) {
                            middleLayout.setCenter(newPane);
                            middleLayout.setMargin(newPane, new Insets(0, 30, 0, 10));
                        }
                    }
                });

        treeView.setPrefSize(150, -1);

        treeViewGroup.getChildren().add(treeView);

        return treeViewGroup;
    }

    private void setPaneProperties(Pane pane) {
        pane.setBorder(new Border(new BorderStroke(Color.DARKGRAY,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }
}
