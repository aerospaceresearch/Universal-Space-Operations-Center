package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AssignDataWindow extends Stage {

    /**
     * Window Title
     */
    private static final String WINDOW_TITLE = "Assign Data";

    /**
     * Stage's Main Layout
     */
    private GridPane mainLayout = new GridPane();

    /**
     * Sensor List View
     */
    private ListView<String> sensorListView = new ListView<>();

    /**
     * Variable List View
     */
    private ListView<String> variableListView = new ListView<>();

    /**
     * Shows selected Sensor Information
     */
    private TextArea sensorInfoTextArea = new TextArea();

    /**
     * Show selected Var Information
     */
    private TextArea variableInfoTextArea = new TextArea();

    /**
     * Creates window
     */
    public AssignDataWindow() {
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
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setHgap(20);
        mainLayout.setVgap(20);
    }

    /**
     * Prepares window components
     */
    private void prepareComponents() {
        prepareVariableListView();
        prepareSensorListView();
        prepareInfoTextArea();
    }

    /**
     * Prepares Variable List View
     */
    private void prepareVariableListView() {
        variableListView.setPrefHeight(150);

        ObservableList<String> variableObservableList =
                FXCollections.observableArrayList();

        variableListView.setItems(variableObservableList);

        ChangeListener<String> listener = (observableValue, oldItem, newItem) -> {
            if (newItem == null) return;
            Sensor selectedSensor = MainController.getInstance().getMessageController()
                    .getSensorByName(sensorListView.getSelectionModel().getSelectedItem());
            Var selectedVar = selectedSensor.getVarByName(newItem);

            variableInfoTextArea.setText(selectedVar.toStringVerbose());
        };

        variableListView.getSelectionModel().selectedItemProperty().addListener(listener);

        VBox varBox = new VBox(new Label("Sensor Variables: "), variableListView);
        varBox.setAlignment(Pos.CENTER);
        varBox.setSpacing(5);

        mainLayout.add(varBox, 0, 1);
    }

    /**
     * Prepares Sensor List View
     */
    private void prepareSensorListView() {
        sensorListView.setPrefHeight(150);

        ArrayList<Sensor> sensors = MainController.getInstance().
                getMessageController().getData().getSensors();

        ObservableList<String> sensorObservableList =
                FXCollections.observableArrayList();

        // Adds all sensor names to sensorListView
        for (Sensor s :
                sensors) {
            sensorObservableList.add(s.getSensorName());
        }

        sensorListView.setItems(sensorObservableList);
        sensorListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ChangeListener<String> listener = (observableValue, oldItem, newItem) -> {
            Sensor selectedSensor = MainController.getInstance()
                    .getMessageController().getSensorByName(newItem);
            ArrayList<Var> selectedSensorVariables = selectedSensor.getVars();

            variableListView.getItems().clear();

            List<String> varNames = new ArrayList<>();
            for (Var var :
                    selectedSensorVariables) {
                varNames.add(var.getDataName());
            }
            variableListView.getItems().addAll(varNames);

            sensorInfoTextArea.setText(selectedSensor.toString());
            variableInfoTextArea.setText("Please select a Variable");
        };
        sensorListView.getSelectionModel().selectedItemProperty()
                .addListener(listener);

        VBox sensorListBox = new VBox(new Label("Sensors:"), sensorListView);
        sensorListBox.setAlignment(Pos.CENTER);
        sensorListBox.setSpacing(5);

        mainLayout.add(sensorListBox, 0, 0);
    }

    /**
     * Prepares Info Text Area
     */
    private void prepareInfoTextArea() {
        sensorInfoTextArea.setPrefColumnCount(20);
        variableInfoTextArea.setPrefColumnCount(20);

        sensorInfoTextArea.setText("Please select a Sensor");
        variableInfoTextArea.setText("Please select a Variable");

        sensorInfoTextArea.setEditable(false);
        variableInfoTextArea.setEditable(false);

        VBox sensorInfoBox = new VBox(new Label("Sensor Information"), sensorInfoTextArea);
        sensorInfoBox.setSpacing(5);
        sensorInfoBox.setAlignment(Pos.CENTER);

        VBox variableInfoBox = new VBox(new Label("Var Information"), variableInfoTextArea);
        variableInfoBox.setSpacing(5);
        variableInfoBox.setAlignment(Pos.CENTER);

        mainLayout.add(sensorInfoBox, 1, 0);
        mainLayout.add(variableInfoBox, 1, 1);
    }
}
