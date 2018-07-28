package com.ksatstuttgart.usoc.gui.panel;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.gui.setup.configuration.StatePaneProperties;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.State;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.SensorDTO;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.VarDTO;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Handles the creation of the State Panel
 *
 * @author Pedro Portela (Pedro12909)
 */
public class StatePanel extends ScrollPane {

    /**
     * State Panel Properties instance from the
     * current Layout
     */
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
            SegmentPane segmentPane = new SegmentPane(segment);

            stateBox.getChildren().add(segmentPane);
        }

        setMinWidth(200);
        setFitToWidth(true);
        setFitToHeight(true);
        setContent(stateBox);
    }

    /**
     * Custom Implementation of a Titled Pane
     * Each Titled Pane contains a single segment information
     */
    private class SegmentPane extends TitledPane {

        /**
         * Segment that is associated with this Pane
         */
        private Segment segment;

        /**
         * Grid of states associated with this segment
         */
        private GridPane labelGrid = new GridPane();

        /**
         * Keeps track of the current column in the labelGrid
         * Needed in order to add new states in the correct order
         */
        private int currentCol = 1;

        /**
         * Keeps track of the current row in the labelGrid
         * Needed in order to add new states in the correct order
         */
        private int currentRow = 1;

        /**
         * Creates a new SegmentPane
         * @param segment associated segment
         */
        private SegmentPane(Segment segment) {
            this.segment = segment;

            prepareComponents();

            setText(segment.getName());
            setContent(labelGrid);
            setCollapsible(true);
        }

        /**
         * Prepares Components
         */
        private void prepareComponents() {
            prepareGrid();
            prepareContextMenu();
        }

        /**
         * Prepares Label Grid
         */
        private void prepareGrid() {
            for (State state : segment.getStates()) {
                addState(state);
            }
        }

        /**
         * Prepares Context Menu
         */
        private void prepareContextMenu() {
            ContextMenu segmentContextMenu = new ContextMenu();

            // Menu Items
            MenuItem addStateItem = new MenuItem("Add State");
            addStateItem.setOnAction(onAction -> {
                // Create a new State on the current Segment
                State newState = new State();

                segment.getStates().add(newState);
                addState(newState);
            });
            MenuItem renameSegmentItem = new MenuItem("Rename");
            renameSegmentItem.setOnAction(onAction -> {
                segmentContextMenu.hide();
                renameSegment();
            });
            MenuItem deleteSegmentItem = new MenuItem("Delete");
            deleteSegmentItem.setOnAction(onAction -> {
                segmentContextMenu.hide();
                deleteSegment();
            });

            segmentContextMenu.getItems().addAll(addStateItem, new SeparatorMenuItem(),
                    renameSegmentItem, deleteSegmentItem);
            setContextMenu(segmentContextMenu);
        }

        private void renameSegment() {
            // TODO
        }

        private void deleteSegment() {
            // TODO
        }

        /**
         * Adds a state to the grid
         * @param state state to be added
         */
        private void addState(State state) {
            if (currentCol == 5) {
                currentRow++;
                currentCol = 1;
            }

            ContextMenu labelContextMenu = new ContextMenu();

            Label label = new Label(state.getKeyword());
            GridPane.setHgrow(label, Priority.SOMETIMES);
            GridPane.setVgrow(label, Priority.SOMETIMES);
            GridPane.setHalignment(label, HPos.CENTER);

            // Handles Right Click Context Menu
            MenuItem editItem = new MenuItem("Edit Data");
            editItem.setOnAction(onAction -> {
                labelContextMenu.hide();
                editState(label, state);
            });
            MenuItem removeStateItem = new MenuItem("Remove");
            removeStateItem.setOnAction(onAction -> {
                labelContextMenu.hide();
                removeState(label, state);
            });

            labelContextMenu.getItems().addAll(editItem, removeStateItem);
            label.setContextMenu(labelContextMenu);

            labelGrid.add(label, currentCol, currentRow);

            currentCol++;
        }

        /**
         * Edit an existing state
         * <p>
         * If the context menu is not hidden on Click, it will
         * cause the popup window to not be focused and thus
         * the user will not be able to interact with it
         */
        private void editState(Label label, State state) {
            Dialog<ResultStruct> editDialog =
                    changeStateDataDialog("Edit", state);

            Optional<ResultStruct> result = editDialog.showAndWait();

            result.ifPresent(usernamePassword -> {
                ResultStruct resultStruct = result.get();
                label.setText(resultStruct.keywordField);
                state.setKeyword(resultStruct.keywordField);
                state.setVarName(resultStruct.selectedVar);
                state.setSensorName(resultStruct.selectedSensor);
            });
        }

        /**
         * Removes a state
         * @param label label associated to a state
         * @param state state to be removed
         */
        private void removeState(Label label, State state) {
            labelGrid.getChildren().remove(label);
            properties.findSegmentByTitle(segment.getName())
                    .getStates().remove(state);
        }

        /**
         * Creates a Dialog that permits the user to either add or edit a state
         * @param title title of the dialog. Should either be 'Edit' or 'Add'
         * @param state state to be added/edited.
         * @return new Dialog
         */
        private Dialog<ResultStruct> changeStateDataDialog(String title, State state) {
            Dialog<ResultStruct> popupDialog = new Dialog<>();

            // Dialog Properties
            popupDialog.initStyle(StageStyle.UTILITY);
            popupDialog.initModality(Modality.NONE);
            popupDialog.setTitle(title);
            popupDialog.setHeaderText("Modify State keyword and data point.");
            popupDialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY,
                    ButtonType.CANCEL);

            // Dialog Layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField keywordTF = new TextField();
            keywordTF.setPromptText(state.getKeyword());

            Sensor foundSensor = MainController.getInstance().getMessageController()
                    .getSensorByName(state.getSensorName());

            // If no data is assigned to this state, the sensor that displays in the comboBox
            // is the first one from the selected protocol
            if (foundSensor == null) {
                foundSensor = MainController.getInstance().getMessageController()
                        .getData().getSensors().get(0);
            }

            // Handles Var Choice Dialog
            ComboBox<VarDTO> varComboBox = new ComboBox<>();
            for (Var var :
                    foundSensor.getVars()) {
                varComboBox.getItems().add(var.toDTO());
            }

            // Same thing as above. If no data is assigned to this state,
            // then foundVar returns null. In that case, the first var in foundSensor
            // is shown  by default in the comboBox
            Var foundVar = foundSensor.getVarByName(state.getVarName());
            if (foundVar == null) {
                foundVar = foundSensor.getVars().get(0);
            }
            varComboBox.setValue(foundVar.toDTO());

            ComboBox<SensorDTO> sensorComboBox =
                    new ComboBox<>();
            sensorComboBox.setValue(foundSensor.toDTO());

            // Populate Sensor Combo Box
            for (Sensor sensor : MainController.getInstance().getMessageController()
                    .getData().getSensors()) {
                sensorComboBox.getItems().add(sensor.toDTO());
            }
            // Sensor Combo Box Value Change Listener
            // Updates Var ComboBox
            sensorComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                varComboBox.getItems().clear();
                varComboBox.getItems().addAll(newValue.getVariables());
                varComboBox.setValue(newValue.getVariables().get(0));
            });

            // Adds nodes to GridPane
            grid.add(new Label("Keyword:"), 0, 0);
            grid.add(keywordTF, 1, 0);
            grid.add(new Label("Sensor:"), 0, 1);
            grid.add(sensorComboBox, 1, 1);
            grid.add(new Label("Variable:"), 0, 2);
            grid.add(varComboBox, 1, 2);

            popupDialog.getDialogPane().setContent(grid);

            // Set Focus on Keyword Text Field
            Platform.runLater(() -> keywordTF.requestFocus());

            // Set Dialog Pane Result
            popupDialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.APPLY) {
                    return new ResultStruct(keywordTF.getText(),
                            sensorComboBox.getValue().getSensorName(),
                            varComboBox.getValue().getVarName());
                }
                return null;
            });

            return popupDialog;
        }
    }

    /**
     * A data class used exclusively to represent a result from the changeStateData dialog
     */
    private class ResultStruct {
        private String keywordField;
        private String selectedSensor;
        private String selectedVar;

        public ResultStruct(String keywordField, String selectedSensor, String selectedVar) {
            this.keywordField = keywordField;
            this.selectedSensor = selectedSensor;
            this.selectedVar = selectedVar;
        }
    }
}
