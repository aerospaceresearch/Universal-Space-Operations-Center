package com.ksatstuttgart.usoc.gui.panel;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.gui.setup.configuration.StatePaneProperties;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.State;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Optional;

/**
 * State Panel
 */
public class StatePanel extends ScrollPane {

    private StatePaneProperties properties;

    /**
     * Creates an instance of the State Panel
     */
    public StatePanel() {
        properties = MainController.getInstance()
                .getLayout().getStatePaneProperties();
        prepareComponents();
    }

    /**
     * Sets up components and prepares layouts
     */
    private void prepareComponents() {
        // All Segment Panes should be inside stateBox
        VBox stateBox = new VBox();
        stateBox.setSpacing(5);


        // Adds each segment to statePane
        for (Segment segment : properties.getSegments()) {
            // Gets current segment title
            final String segmentTitle = segment.getName();

            // Gets all keywords for current segment
            GridPane labelGrid = new GridPane();
            List<State> states = segment.getStates();

            // Keeps track of each segment's row and col
            // Needs to start at one because GridPane.add() starts at 1 and not 0
            int currentRow = 1;
            int currentCol = 1;

            for (State state : states) {
                if (currentCol == 5) {
                    currentRow++;
                    currentCol = 1;
                }

                Label label = new Label(state.getKeyword());
                GridPane.setHgrow(label, Priority.SOMETIMES);
                GridPane.setVgrow(label, Priority.SOMETIMES);
                GridPane.setHalignment(label, HPos.CENTER);

                // Handles Right Click Context Menu
                MenuItem editItem = new MenuItem("Edit Data");
                editItem.setOnAction(onAction -> {
                    showEditDataPopup(label, state);
                });
                MenuItem removeItem = new MenuItem("Remove");
                removeItem.setOnAction(onAction -> {
                    labelGrid.getChildren().remove(label);
                    properties.findSegmentByTitle(segmentTitle).getStates().remove(state);
                });
                label.setContextMenu(new ContextMenu(editItem, removeItem));

                labelGrid.add(label, currentCol, currentRow);

                currentCol++;
            }

            // Each segmentPane holds information about a single segment
            TitledPane segmentPane = new TitledPane(segmentTitle, labelGrid);
            segmentPane.setCollapsible(false);

            // Segment Pane Context Menu
            MenuItem addStateItem = new MenuItem("Add State");
            addStateItem.setOnAction(onAction -> {
                // TODO add Item
            });
            segmentPane.setContextMenu(new ContextMenu(addStateItem));

            stateBox.getChildren().add(segmentPane);
        }

        setMinWidth(200);
        setFitToWidth(true);
        setFitToHeight(true);
        setContent(stateBox);
    }

    private void showEditDataPopup(Label label, State state) {
        Dialog<String> popupDialog = new Dialog<>();
        popupDialog.initStyle(StageStyle.UTILITY);
        popupDialog.setTitle("Edit Data");
        popupDialog.setHeaderText("Modify State keyword and data point.");
        popupDialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField keywordTF = new TextField();
        keywordTF.setPromptText("Keyword");

        Sensor foundSensor = MainController.getInstance().getMessageController()
                .getSensorByName(state.getSensorName());

        // If no sensors are assigned, the sensor that displays in the comboBox
        // is the first one from the selected protocol
        if (foundSensor == null) {
            foundSensor = MainController.getInstance().getMessageController()
                    .getData().getSensors().get(0);
        }

        // Handles Var Choice Dialog
        ComboBox<Var> varComboBox = new ComboBox();
        varComboBox.getItems().addAll(foundSensor.getVars());

        // Same thing as above. If no data is assigned to this state,
        // then foundVar returns null. In that case, the first var in foundSensor
        // is shown  by default in the comboBox
        Var foundVar = foundSensor.getVarByName(state.getVarName());
        if (foundVar == null) {
            foundVar = foundSensor.getVars().get(0);
        }
        varComboBox.setValue(foundVar);

        ComboBox<Sensor> sensorComboBox =
                new ComboBox<>();
        sensorComboBox.getItems().addAll(MainController.getInstance().getMessageController()
                .getData().getSensors());
        sensorComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            varComboBox.getItems().clear();
            varComboBox.getItems().addAll(newValue.getVars());
        });

        grid.add(new Label("Keyword:"), 0, 0);
        grid.add(keywordTF, 1, 0);
        grid.add(new Label("Sensor:"), 0, 1);
        grid.add(sensorComboBox, 1, 1);
        grid.add(new Label("Variable:"), 0, 2);
        grid.add(varComboBox, 1, 2);

        popupDialog.getDialogPane().setContent(grid);

        // Set Focus on Keyword Text Field
        Platform.runLater(() -> keywordTF.requestFocus());

        popupDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.APPLY) {
                return keywordTF.getText();
            }
            return null;
        });

        Optional<String> result = popupDialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            label.setText(result.get());
            state.setKeyword(result.get());
            state.setVarName(varComboBox.getValue().getDataName());
            state.setSensorName(sensorComboBox.getValue().getSensorName());
        });
    }
}
