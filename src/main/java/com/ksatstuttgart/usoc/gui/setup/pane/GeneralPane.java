package com.ksatstuttgart.usoc.gui.setup.pane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Creates and prepares the GeneralPane
 */
public class GeneralPane extends Pane {

    /**
     * Full Screen CheckBox
     */
    private final CheckBox fullScreenCheckBox = new CheckBox("Full Screen");

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
     * Creates a Pane with specific components
     */
    public GeneralPane() {
        setComponents();
    }

    /**
     * Sets GeneralPane Window Properties
     */
    private void setComponents() {
        windowHeight.setPrefColumnCount(5);
        windowHeight.setPromptText("Height");
        windowWidth.setPrefColumnCount(5);
        windowWidth.setPromptText("Width");
        fullScreenCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue, Boolean newValue) {
                if (newValue.booleanValue() == true) {
                    windowWidth.setDisable(true);
                    windowHeight.setDisable(true);
                } else {
                    windowWidth.setDisable(false);
                    windowHeight.setDisable(false);
                }
            }
        });

        Region nodeSeparator = new Region();
        nodeSeparator.setPrefWidth(150);
        HBox.setHgrow(nodeSeparator, Priority.ALWAYS);

        HBox windowSize = new HBox(fullScreenCheckBox, nodeSeparator, windowWidth, windowHeight);

        windowSize.setPadding(new Insets(20));
        windowSize.setSpacing(10);

        resizableCheckBox.setPadding(new Insets(20));

        VBox generalPaneLayout = new VBox(windowSize, resizableCheckBox);

        getChildren().add(generalPaneLayout);
    }
}
