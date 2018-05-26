package com.ksatstuttgart.usoc.gui.panel;

import com.ksatstuttgart.usoc.gui.setup.configuration.ConfigHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Properties;

/**
 * State Panel
 */
public class StatePanel extends ScrollPane {

    /**
     * Config file relative path
     */
    private static final String CONFIG_PATH = "config/config.properties";

    /**
     * Creates an instance of the State Panel
     * @param config configuration file containing all GUI properties
     */
    public StatePanel(Properties config) {
        //TODO Read JSON File and set needed parameters and values
        prepareComponents(config);
    }

    /**
     * Sets up components and prepares layouts
     * @param config configuration file containing all GUI properties
     */
    private void prepareComponents(Properties config) {
        // All Segment Panes should be inside stateBox
        VBox stateBox = new VBox();
        stateBox.setSpacing(5);

        // Get number of segments from config
        final int segmentCount =
                ConfigHandler.countItems("segmentTitle", CONFIG_PATH);

        // Adds each segment to statePane
        for (int i = 0; i < segmentCount; i++) {
            // Gets current segment title
            final String segmentTitle =
                    config.getProperty("segmentTitle[" + (i + 1) + "]");

            // Gets all keywords for current segment
            GridPane labelGrid = new GridPane();
            final int labelCountInSegment =
                    ConfigHandler.countItems("keyword[" + (i + 1) + "]", CONFIG_PATH);

            // Keeps track of each segment's row and col
            // Needs to start at one because GridPane.add() starts at 1 and not 0
            int currentRow = 1;
            int currentCol = 1;

            for (int j = 0; j < labelCountInSegment; j++) {
                if (currentCol == 5) {
                    currentRow++;
                    currentCol = 1;
                }

                Label label =
                        new Label(config.getProperty("keyword[" + (i + 1) + "][" + (j + 1) + "]"));
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
