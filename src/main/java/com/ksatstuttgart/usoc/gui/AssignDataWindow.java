package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import com.ksatstuttgart.usoc.gui.setup.configuration.UIEntity;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Chart;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Placeholder;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
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
    private BorderPane mainLayout = new BorderPane();

    /**
     * Left Tree View
     */
    private TreeView<String> allVariablesTree = new TreeView<>();

    /**
     * Right Tree View
     */
    private TreeView<UIEntity> chartsStatesTree = new TreeView<>();

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
    }

    /**
     * Prepares window components
     */
    private void prepareComponents() {
        prepareCenter();
        prepareBottom();
    }

    /**
     * Prepares Center Region
     */
    private void prepareCenter() {
        prepareChartsStatesTreeView();
        prepareInfoTextArea();

        HBox centerBox = new HBox(chartsStatesTree, informationTextArea);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(50);

        mainLayout.setCenter(centerBox);
    }

    /**
     * Prepares Right Tree View
     */
    private void prepareChartsStatesTreeView() {
        TreeItem<UIEntity> rootItem = new TreeItem<>(new Placeholder("Root"));

        TreeItem<UIEntity> chartsItem = new TreeItem<>(new Placeholder("Charts"));
        TreeItem<UIEntity> segmentsItem = new TreeItem<>(new Placeholder("Segments"));

        rootItem.setExpanded(true);
        chartsItem.setExpanded(true);
        segmentsItem.setExpanded(true);

        // Populate Charts
        for (Chart chart :
                layoutData.getUsocPaneProperties().getCharts()) {
            chartsItem.getChildren().add(new TreeItem<>(chart));
        }

        // Populate States
        for (Segment segment :
                layoutData.getStatePaneProperties().getSegments()) {
            TreeItem<UIEntity> segmentItem = new TreeItem<>(segment);

            for (State segmentState :
                    segment.getStates()) {
                segmentItem.getChildren().add(new TreeItem<>(segmentState));
            }

            segmentsItem.getChildren().add(segmentItem);
        }

        // Click Listener
        chartsStatesTree.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    if (newValue == null) return;

                    if (newValue.getValue() instanceof Placeholder
                            || newValue.getValue() instanceof Segment) {
                        return;
                    }

                    if (newValue.getValue() instanceof Chart) {
                        System.out.println("Chart");
                    } else if (newValue.getValue() instanceof State) {
                        System.out.println("State");
                    }
                });

        rootItem.getChildren().addAll(chartsItem, segmentsItem);

        chartsStatesTree.setMaxSize(200, 300);
        chartsStatesTree.showRootProperty().setValue(false);
        chartsStatesTree.setEditable(false);
        chartsStatesTree.setRoot(rootItem);
    }

    /**
     * Prepares information text area
     */
    private void prepareInfoTextArea() {
        informationTextArea.setEditable(false);
        informationTextArea.setPrefColumnCount(20);

        mainLayout.getChildren().add(informationTextArea);
    }

    /**
     * Prepares Bottom Region
     */
    private void prepareBottom() {

    }

    /*
    ===================================================================================
                                    OLD METHODS
     */

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
}
