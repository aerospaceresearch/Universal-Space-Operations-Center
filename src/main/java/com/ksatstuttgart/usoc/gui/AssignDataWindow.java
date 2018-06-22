package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.UIEntity;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Chart;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Placeholder;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.State;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.SensorDTO;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.VarDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
     * Information Text Area Placeholder text
     */
    private static final String INFO_PLACEHOLDER =
            "Please select a state/chart to display assigned variables";

    /**
     * Layout Data previously set
     */
    private Layout layoutData;

    /**
     * Stage's Main Layout
     */
    private BorderPane mainLayout = new BorderPane();

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
                        informationTextArea.setText(INFO_PLACEHOLDER);
                        informationTextArea.setWrapText(true);
                        return;
                    }

                    final String separator = "===========================";

                    if (newValue.getValue() instanceof Chart) {
                        Chart selectedChart = (Chart)newValue.getValue();

                        if (selectedChart.getSensors().isEmpty()) {
                            informationTextArea.setText("No variables assigned");
                            return;
                        }

                        StringBuilder content = new StringBuilder("Variables\n");

                        for (SensorDTO chartSensor :
                                selectedChart.getSensors()) {
                            Sensor realSensor = MainController.getInstance().getMessageController()
                                    .getSensorByName(chartSensor.getSensorName());
                            content.append("SENSOR\n" + separator + "\n" + realSensor.toString());

                            for (VarDTO chartSensorVar :
                                    chartSensor.getVariables()) {
                                Var realVar = realSensor.getVarByName(chartSensorVar.getVarName());

                                content.append(realVar.toStringVerbose()
                                        + "\n" + separator + "\n");
                            }
                        }

                    } else if (newValue.getValue() instanceof State) {
                        State selectedState = (State)newValue.getValue();

                        if (selectedState.getVar() == null) {
                            informationTextArea.setText("No variable assigned");
                            informationTextArea.setWrapText(false);
                            return;
                        }

                        informationTextArea.setText("Variable\n" +
                                selectedState.getVar() + "\n" + separator);
                        informationTextArea.setWrapText(false);
                        return;
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

        informationTextArea.setText(INFO_PLACEHOLDER);
        informationTextArea.setWrapText(true);

        mainLayout.getChildren().add(informationTextArea);
    }

    /**
     * Prepares Bottom Region
     */
    private void prepareBottom() {
        Button manageButton = new Button("Manage Variables");
        manageButton.setPrefSize(150, 40);
        manageButton.setOnAction(onClick -> {
            TreeItem<UIEntity> selectedItem =
                    chartsStatesTree.getSelectionModel().getSelectedItem();

            if (selectedItem == null) return;

            UIEntity selected = selectedItem.getValue();

            VarManagementWindow varManagementWindow = new VarManagementWindow(selected);
            varManagementWindow.showAndWait();
        });

        Button okButton = new Button("Ok");
        okButton.setPrefSize(150, 40);
        okButton.setOnAction(onClick -> {
            // TODO

            // Check for unassigned variables

            // Continue
            onClick.consume();
            close();
        });

        HBox buttonBox = new HBox(manageButton, okButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(50);

        mainLayout.setBottom(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(20, 0, 0, 0));
    }
}
