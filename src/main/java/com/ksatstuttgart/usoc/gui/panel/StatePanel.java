package com.ksatstuttgart.usoc.gui.panel;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.gui.setup.configuration.StatePaneProperties;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.State;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

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
                labelGrid.add(label, currentCol, currentRow);

                currentCol++;
            }

            // Each titledPane holds information about a single segment
            TitledPane titledPane = new TitledPane(segmentTitle, labelGrid);
            titledPane.setCollapsible(false);
            stateBox.getChildren().add(titledPane);
        }

        setMinWidth(200);
        setFitToWidth(true);
        setFitToHeight(true);
        setContent(stateBox);
    }
}
