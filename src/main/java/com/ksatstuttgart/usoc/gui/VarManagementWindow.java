package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Chart;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Placeholder;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.State;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.UIEntity;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.SensorDTO;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.VarDTO;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * After selecting a Chart/State, this window allows
 * assigning data to it
 */
public class VarManagementWindow extends Stage {

    /**
     * Window Title
     */
    private static final String WINDOW_TITLE = "Variable Management";

    /**
     * Selected Entity
     */
    private UIEntity entity;

    /**
     * Main Window Layout
     */
    private BorderPane mainLayout = new BorderPane();

    /**
     * Tree View that displays all Sensors and Variables
     */
    private TreeView<UIEntity> allVariablesTreeView = new TreeView<>();

    /**
     * Displays Information about the selected variable
     */
    private TextArea selectedVarInfoArea = new TextArea();

    /**
     * Default Constructor
     * @param entity selected chart/state
     */
    public VarManagementWindow(UIEntity entity) {
        this.entity = entity;

        setProperties();
        prepareWindow();

        setScene(new Scene(mainLayout));
    }

    /**
     * Sets Window Properties
     */
    private void setProperties() {
        setTitle(WINDOW_TITLE);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * Prepares Window
     */
    private void prepareWindow() {
        prepareCenter();
        prepareBottom();
    }

    /**
     * Prepares Center
     */
    private void prepareCenter() {
        // Sensor Tree
        prepareTree();

        // Selected Var Info Area
        selectedVarInfoArea.setPrefColumnCount(20);
        selectedVarInfoArea.setText("Select a Sensor/Variable to display information about it");
        selectedVarInfoArea.setWrapText(true);

        HBox centerBox = new HBox(allVariablesTreeView, selectedVarInfoArea);
        centerBox.setSpacing(20);

        mainLayout.setCenter(centerBox);
        BorderPane.setMargin(centerBox, new Insets(20));
    }

    /**
     * Prepares Tree
     */
    private void prepareTree() {
        allVariablesTreeView.setEditable(false);


        allVariablesTreeView.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldSelection, newSelection) -> {
                    if (newSelection == null) return;

                    if (newSelection.getValue() instanceof Placeholder) {
                        selectedVarInfoArea.setText("Select a Sensor/Variable to display information about it");
                        selectedVarInfoArea.setWrapText(true);
                    } else if (newSelection.getValue() instanceof SensorDTO) {
                        Sensor actualSensor = MainController.getInstance().getMessageController()
                                .getSensorByName(((SensorDTO) newSelection.getValue()).getSensorName());

                        selectedVarInfoArea.setText("SENSOR\n===================\n"
                                + actualSensor.toString());
                        selectedVarInfoArea.setWrapText(false);
                    } else if (newSelection.getValue() instanceof VarDTO) {
                        Sensor varSensor = MainController.getInstance().getMessageController()
                                .getSensorByName(((SensorDTO) newSelection.getParent().getValue())
                                        .getSensorName());

                        Var actualVar = varSensor.getVarByName(((VarDTO) newSelection.getValue()).getVarName());

                        selectedVarInfoArea.setText("SENSOR\n===================\n"
                                + varSensor.toString() + "\n===================\n"
                                + "DATA POINT\n===================\n"
                                + actualVar.toStringVerbose());
                        selectedVarInfoArea.setWrapText(false);
                    }
                });

        CheckBoxTreeItem<UIEntity> root = new CheckBoxTreeItem<>(new Placeholder("Sensors"));

        List<Sensor> sensors = MainController.getInstance().getMessageController()
                .getData().getSensors();

        for (Sensor sensor :
                sensors) {
            CheckBoxTreeItem<UIEntity> sensorItem = new CheckBoxTreeItem<>(sensor.toDTO());

            for (Var sensorVar :
                    sensor.getVars()) {
                sensorItem.getChildren().add(new CheckBoxTreeItem<>(sensorVar.toDTO()));
            }

            root.getChildren().add(sensorItem);
        }

        allVariablesTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        allVariablesTreeView.setRoot(root);
    }

    /**
     * Prepares Bottom Region
     */
    private void prepareBottom() {
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setPrefHeight(40);
        confirmBtn.setOnAction(onClick -> {
            List<CheckBoxTreeItem<UIEntity>> checkedItems = new ArrayList<>();

            // Goes through the list and checks marked/ticked items
            findCheckedItems((CheckBoxTreeItem<UIEntity>) allVariablesTreeView.getRoot(),
                    checkedItems);

            for (TreeItem<UIEntity> item :
                    checkedItems) {
                UIEntity selectedEntity = item.getValue();

                if (selectedEntity instanceof VarDTO) {
                    SensorDTO sensor = (SensorDTO) item.getParent().getValue();
                    VarDTO var = (VarDTO) selectedEntity;

                    if (this.entity instanceof Chart) {
                        ((Chart) this.entity).addVariable(sensor.getSensorName(),
                                var.getVarName());
                    } else if (this.entity instanceof State) {
                        ((State) this.entity).addVariable(sensor.getSensorName(),
                                var.getVarName());
                    }
                }
            }

            onClick.consume();

            this.close();
        });


        HBox buttonBox = new HBox(confirmBtn);
        buttonBox.setAlignment(Pos.CENTER);

        mainLayout.setBottom(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(20));
    }

    /**
     * Finds Selected Items
     * @param item root item
     * @param checkedItems empty list
     */
    private void findCheckedItems(CheckBoxTreeItem<UIEntity> item,
                                  List<CheckBoxTreeItem<UIEntity>> checkedItems) {
        if (item == null || checkedItems == null) return;

        if (item.isSelected()) {
            checkedItems.add(item);
        }

        for (TreeItem<?> child : item.getChildren()) {
            findCheckedItems((CheckBoxTreeItem<UIEntity>) child, checkedItems);
        }
    }
}
