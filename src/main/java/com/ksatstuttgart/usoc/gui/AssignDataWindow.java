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
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.UIEntity;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Chart;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Placeholder;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.State;
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

import java.util.List;
import java.util.Set;

/**
 * Assign Data Window
 * Responsible for assigning data defined in a protocol
 * to the charts and states
 *
 * @author Pedro Portela (Pedro12909)
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

                        if (selectedChart.getAssignedData().isEmpty()) {
                            informationTextArea.setText("No variables assigned");
                            return;
                        }

                        StringBuilder content = new StringBuilder("Variables\n");

                        // Get list of Sensors
                        Set<String> sensorNames = selectedChart.getAssignedData().keySet();

                        for (String sensorName :
                                sensorNames) {
                            Sensor currentSensor = MainController.getInstance().getMessageController()
                                    .getSensorByName(sensorName);

                            content.append(separator);
                            content.append("Sensor: " + sensorName + "\n" + currentSensor.toString() + "\n");

                            // Get selected sensor variables
                            List<String> sensorVariableNames = selectedChart.getAssignedData().get(sensorName);
                            for (String sensorVariableName :
                                    sensorVariableNames) {
                                Var currentSensorVariable = currentSensor.getVarByName(sensorVariableName);
                                content.append(currentSensorVariable.toStringVerbose());
                                content.append("\n");
                            }

                        }

                    } else if (newValue.getValue() instanceof State) {
                        State selectedState = (State)newValue.getValue();

                        final String sensorName = selectedState.getSensorName();
                        final String varName = selectedState.getVarName();

                        if (sensorName == null || varName == null) {
                            informationTextArea.setText("No variable assigned");
                            informationTextArea.setWrapText(false);
                            return;
                        }

                        Sensor foundSensor = MainController.getInstance()
                                .getMessageController().getSensorByName(sensorName);

                        if (foundSensor == null) {
                            throw new IllegalArgumentException("Could not find sensor " + sensorName);
                        }

                        Var foundVar = foundSensor.getVarByName(varName);

                        if (foundVar == null) {
                            throw new IllegalArgumentException(
                                    String.format("Could not find variable %s from sensor %s",
                                            varName, sensorName));
                        }

                        if (foundVar == null) {
                            informationTextArea.setText("No variable assigned");
                            informationTextArea.setWrapText(false);
                            return;
                        }

                        final String textAreaContent = String.format("Sensor: %s\n%s\nVariable: %s\n%s\n",
                                sensorName, foundSensor.toString(),
                                foundVar.getDataName(), foundVar.toStringVerbose());

                        informationTextArea.setText(textAreaContent);
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
