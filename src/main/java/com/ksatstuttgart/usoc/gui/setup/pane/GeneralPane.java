package com.ksatstuttgart.usoc.gui.setup.pane;

import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import com.ksatstuttgart.usoc.gui.setup.configuration.Parsable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Creates and prepares the GeneralPane
 */
public class GeneralPane extends Pane implements Parsable {

    /**
     * Experiment Name Text Field
     */
    private final TextField experimentNameTextField = new TextField();

    /**
     * Maximized CheckBox
     */
    private final CheckBox maximizedCheckBox = new CheckBox("Maximized");

    /**
     * Window Width Text Field
     */
    private final TextField windowWidth = new TextField();

    /**
     * Window Height Text Field
     */
    private final TextField windowHeight = new TextField();

    /**
     * Resizable Window CheckBox
     */
    private final CheckBox resizableCheckBox = new CheckBox("Resizable");

    /**
     * Default Node Padding
     */
    private static final Insets DEFAULT_PADDING =
            new Insets(20);

    /**
     * Creates a Pane with specific components
     */
    public GeneralPane() {
        prepareComponents();
    }

    /**
     * Prepares GeneralPane
     */
    private void prepareComponents() {
        GridPane experimentNameBox = new GridPane();
        experimentNameBox.setAlignment(Pos.CENTER_LEFT);
        experimentNameBox.setHgap(30);
        experimentNameBox.setPadding(DEFAULT_PADDING);
        experimentNameBox.add(new Label("Experiment Name"), 0, 0);
        experimentNameBox.add(experimentNameTextField, 1, 0);
        experimentNameTextField.setPromptText("Experiment Name");

        windowHeight.setPrefColumnCount(5);
        windowHeight.setPromptText("Height");
        windowWidth.setPrefColumnCount(5);
        windowWidth.setPromptText("Width");
        maximizedCheckBox.selectedProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue.booleanValue() == true) {
                        windowWidth.setDisable(true);
                        windowHeight.setDisable(true);
                    } else {
                        windowWidth.setDisable(false);
                        windowHeight.setDisable(false);
                    }
                });

        Region nodeSeparator = new Region();
        nodeSeparator.setPrefWidth(150);
        HBox.setHgrow(nodeSeparator, Priority.ALWAYS);

        HBox windowSize = new HBox(maximizedCheckBox, nodeSeparator, windowWidth, windowHeight);

        windowSize.setPadding(DEFAULT_PADDING);
        windowSize.setSpacing(10);

        resizableCheckBox.setPadding(DEFAULT_PADDING);

        VBox generalPaneLayout = new VBox(experimentNameBox, windowSize, resizableCheckBox);

        getChildren().add(generalPaneLayout);
    }

    /**
     * Writes Pane properties to POJO class
     *
     * @param pojoClass POJO Class to set properties
     */
    @Override
    public void writeToPOJO(Layout pojoClass) {
        pojoClass.setExperimentName(experimentNameTextField.getText().trim());

        boolean isMaximized = maximizedCheckBox.isSelected();

        pojoClass.setMaximized(isMaximized);

        if (!isMaximized) {
            try {
                pojoClass.setWidth(Integer.parseInt(windowWidth.getText()));
                pojoClass.setHeight(Integer.parseInt(windowHeight.getText()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("General: Window Size must be numerical");
            }
        }

        pojoClass.setResizable(resizableCheckBox.isSelected());

    }
}
