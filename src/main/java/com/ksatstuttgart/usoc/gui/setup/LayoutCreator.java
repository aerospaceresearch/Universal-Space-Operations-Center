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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
     * Holds the tree view and the right side Pane
     */
    private FlowPane horizontalLayout = new FlowPane();

    /**
     * Creates a new instance of the Layout Creator Class
     */
    public LayoutCreator() {
        setProperties();
        //TODO instantiate right side panels
        createRightPanes();
        prepareComponents();
    }

    /**
     * Sets Scene/Window Properties
     */
    private void setProperties() {
        MainController.getInstance().getStage().setTitle(SCENE_TITLE);
        MainController.getInstance().getStage().setMinWidth(WINDOW_WIDTH);
        MainController.getInstance().getStage().setMinHeight(WINDOW_HEIGHT);
        MainController.getInstance().getStage().setResizable(false);

        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10, 0, 10, 30));
        setSpacing(10);
        setVgrow(this, Priority.ALWAYS);
    }

    /**
     * Creates all Right Side Panes
     */
    private void createRightPanes() {
        //TODO Create Panes
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
        horizontalLayout = new FlowPane();
        horizontalLayout.setAlignment(Pos.BASELINE_LEFT);

        Group treeViewPanel = prepareTreeViewPane();

        horizontalLayout.getChildren().add(treeViewPanel);
        //TODO Add General properties pane

        getChildren().add(horizontalLayout);
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

        treeView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<TreeItem<String>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<String>> observableValue,
                                        TreeItem<String> oldValue, TreeItem<String> newValue) {
                        Pane oldPane = componentsMap.get(oldValue.getValue());
                        Pane newPane = componentsMap.get(newValue.getValue());

                        horizontalLayout.getChildren().remove(oldPane);
                        horizontalLayout.getChildren().add(newPane);
                    }
                });

        treeViewGroup.getChildren().add(treeView);

        return treeViewGroup;
    }
}
