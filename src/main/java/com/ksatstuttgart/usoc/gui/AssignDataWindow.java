package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Chart;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Assign Data Window
 * Responsible for assigning data defined in a protocol
 * to the charts and states
 */
public class AssignDataWindow extends Stage {

    /**
     * Window Title
     */
    private static final String WINDOW_TITLE = "Assign Data";

    /**
     * Layout Data previously set
     */
    private Layout layoutData;

    /**
     * Stage's Main Layout
     */
    private VBox mainLayout = new VBox();

    /**
     * Left Tree View
     */
    private TreeView<String> allVariablesTree = new TreeView<>();

    /**
     * Right Tree View
     */
    private TreeView<String> chartsStatesTree = new TreeView<>();

    /**
     * Displays selected sensor and variable information
     */
    private TextArea informationTextArea = new TextArea();

    /**
     * Creates window
     */
    public AssignDataWindow() {
        layoutData = MainController.getInstance().getLayout();

        setProperties();
        prepareComponents();

        setScene(new Scene(mainLayout));
    }

    /**
     * Sets window properties
     */
    private void setProperties() {
        setTitle(WINDOW_TITLE);
        setResizable(false);
        initModality(Modality.NONE);

        mainLayout.setPadding(new Insets(20));
        mainLayout.setSpacing(20);
    }

    /**
     * Prepares window components
     */
    private void prepareComponents() {
        prepareTreeViews();
        prepareTextArea();
    }

    /**
     * Prepares Left and Right Tree Views
     */
    private void prepareTreeViews() {
        prepareAllVariablesTreeView();
        prepareChartsStatesTreeView();

        // Assign and Remove Buttons
        Button assignBtn = new Button("Assign");
        assignBtn.setPrefSize(100, 30);
        assignBtn.setOnAction(onAction -> {
            // Get Selected Item from Left Tree
            TreeItem<String> selectedLeft =
                    allVariablesTree.getSelectionModel().getSelectedItem();

            // Get Selected Item from Right Tree
            TreeItem<String> selectedRight =
                    chartsStatesTree.getSelectionModel().getSelectedItem();

            // Check if selected proper tree item
            if (!validSelectionLeft(selectedLeft)
                    || !validSelectionRight(selectedRight)) return;

            selectedRight.getChildren().add(selectedLeft);
        });

        Button removeBtn = new Button("Remove");
        removeBtn.setPrefSize(100, 30);
        removeBtn.setOnAction(onAction -> {
            TreeItem<String> rightSelection = chartsStatesTree.getSelectionModel()
                    .getSelectedItem();

            if (!canBeRemoved(rightSelection)) return;

            rightSelection.getParent().getChildren().remove(rightSelection);
        });

        Button confirmButton = new Button("Confirm");
        confirmButton.setPrefSize(100, 30);
        confirmButton.setOnAction(onAction -> {
            //TODO Implement confirm

            // Re-write to file

            // Load data
        });

        VBox buttonBox = new VBox(assignBtn, removeBtn, confirmButton);
        buttonBox.setSpacing(30);
        buttonBox.setAlignment(Pos.CENTER);

        HBox treeBox = new HBox(allVariablesTree, buttonBox, chartsStatesTree);
        treeBox.setAlignment(Pos.CENTER);
        treeBox.setSpacing(20);
        mainLayout.getChildren().add(treeBox);
    }

    /**
     * Checks if the user selected a valid item in the left tree
     * @param leftSelection right selection
     * @return return true
     */
    private boolean validSelectionLeft(TreeItem<String> leftSelection) {
        if (leftSelection == null) return false;
        if (leftSelection.getParent() == null) return false;
        if (leftSelection.getParent().getValue().equals("Sensors")
                && !leftSelection.isLeaf()) return false;

        return true;
    }

    /**
     * Checks if the user selected a valid item in the right tree
     * @param rightSelection right selection
     * @return return true
     */
    private boolean validSelectionRight(TreeItem<String> rightSelection) {
        if (rightSelection == null) return false;
        if (rightSelection.getParent() == null) return false;
        if (rightSelection.getParent().getValue().equals("Elements")) return false;
        if (rightSelection.getParent().getValue().equals("Charts")) return true;

        if (rightSelection.isLeaf()
                && rightSelection.getParent()
                .getParent().getValue().equals("Segments")) return true;

        return false;
    }

    /**
     * Checks if the selected right tree item can be removed
     * @param rightSelection selected right tree item
     * @return true if can be removed
     */
    private boolean canBeRemoved(TreeItem<String> rightSelection) {
        if (rightSelection == null) return false;
        if (rightSelection.getParent() == null) return false;
        if (rightSelection.getValue().equals("Segments")
                || rightSelection.getValue().equals("Charts")) return false;
        if (rightSelection.getParent().getValue()
                .equals("Segments")) return false;

        return true;
    }

    /**
     * Prepares information text area
     */
    private void prepareTextArea() {
        informationTextArea.setEditable(false);
        informationTextArea.setPrefColumnCount(20);

        mainLayout.getChildren().add(informationTextArea);
    }

    /**
     * Prepares Left Tree View
     */
    private void prepareAllVariablesTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("Sensors");

        for (Sensor sensor :
                MainController.getInstance().getMessageController().getData().getSensors()) {
            TreeItem<String> sensorItem = new TreeItem<>(sensor.getSensorName());

            for (Var sensorVar :
                    sensor.getVars()) {
                sensorItem.getChildren().add(new TreeItem<>(sensorVar.getDataName()));
            }

            rootItem.getChildren().add(sensorItem);
        }

        rootItem.setExpanded(true);
        allVariablesTree.setMaxSize(200, 400);
        allVariablesTree.setEditable(false);
        allVariablesTree.setRoot(rootItem);
    }

    /**
     * Prepares Right Tree View
     */
    private void prepareChartsStatesTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("Elements");

        TreeItem<String> chartsItem = new TreeItem<>("Charts");
        TreeItem<String> statesItem = new TreeItem<>("Segments");

        // Populate Charts
        for (Chart chart :
                layoutData.getUsocPaneProperties().getCharts()) {
            chartsItem.getChildren().add(new TreeItem<>(chart.getTitle()));
        }

        // Populate States
        for (Segment segment :
                layoutData.getStatePaneProperties().getSegments()) {
            TreeItem<String> segmentItem = new TreeItem<>(segment.getName());

            for (State segmentState :
                    segment.getStates()) {
                segmentItem.getChildren().add(new TreeItem<>(segmentState.getKeyword()));
            }

            statesItem.getChildren().add(segmentItem);
        }

        rootItem.getChildren().addAll(chartsItem, statesItem);
        rootItem.setExpanded(true);

        chartsStatesTree.setMaxSize(200, 400);
        chartsStatesTree.showRootProperty().setValue(false);
        chartsStatesTree.setEditable(false);
        chartsStatesTree.setRoot(rootItem);
    }
}
