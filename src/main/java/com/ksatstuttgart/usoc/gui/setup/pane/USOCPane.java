package com.ksatstuttgart.usoc.gui.setup.pane;

import com.ksatstuttgart.usoc.gui.setup.configuration.Parsable;
import com.ksatstuttgart.usoc.gui.setup.configuration.Properties;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * USOC Panel Pane (Charts + GNSS View)
 */
public class USOCPane extends BorderPane implements Parsable {

    /**
     * Default Node Padding
     */
    private static final Insets DEFAULT_PADDING =
            new Insets(20);

    /**
     * Pane Enabled CheckBox
     */
    private final CheckBox enabledCheckBox =
            new CheckBox("Enabled");

    /**
     * GNSS View Enabled CheckBox
     */
    private final CheckBox gnssCheckBox =
            new CheckBox("GNSS View");

    /**
     * Chart Columns Text Field
     */
    private final TextField chartColumnsTextField =
            new TextField();

    /**
     * Chart VBox
     */
    private VBox chartBox = new VBox();

    /**
     * Holds all visible ChartRows
     */
    private List<ChartRow> chartRowList = new ArrayList<>();

    /**
     * Creates and prepares USOC Pane
     */
    public USOCPane() {
        prepareComponents();
    }

    /**
     * Prepares pane components
     */
    private void prepareComponents() {
        prepareTop();
        prepareCenter();
        prepareBot();
    }

    /**
     * Prepares Top Region of the Border Pane
     */
    private void prepareTop() {
        GridPane topPane = new GridPane();
        topPane.setHgap(30);
        topPane.setVgap(10);

        topPane.add(enabledCheckBox, 0, 0);
        topPane.add(gnssCheckBox, 1, 0);

        GridPane labelTextFieldPane = new GridPane();
        labelTextFieldPane.setAlignment(Pos.CENTER_LEFT);
        labelTextFieldPane.setHgap(10);
        labelTextFieldPane.add(new Label("Chart Columns"), 0, 0);
        labelTextFieldPane.add(chartColumnsTextField, 1, 0);
        chartColumnsTextField.setPrefColumnCount(3);
        topPane.add(labelTextFieldPane, 2, 0);

        topPane.setPadding(DEFAULT_PADDING);

        setTop(topPane);
    }

    /**
     * Prepares Center Region of the BorderPane
     */
    private void prepareCenter() {
        chartBox.setSpacing(5);
        chartBox.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(chartBox);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        setCenter(scrollPane);
    }

    /**
     * Prepares Bottom Region of the BorderPane
     */
    private void prepareBot() {
        final Button minusButton = new Button("Remove Chart");
        minusButton.setPrefSize(150, 30);
        minusButton.setDisable(true);
        minusButton.setOnAction(actionEvent -> {
            ChartRow lastRow = chartRowList.remove(chartRowList.size() - 1);

            chartBox.getChildren().remove(lastRow);

            if (chartRowList.size() == 0) {
                minusButton.setDisable(true);
            }
        });

        Button plusButton = new Button("Add Chart");
        plusButton.setPrefSize(150, 30);
        plusButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                minusButton.setDisable(false);

                ChartRow newRow = new ChartRow(chartRowList.size());

                chartBox.getChildren().add(newRow);
                chartRowList.add(newRow);
            }
        });

        HBox buttonBox = new HBox(plusButton, minusButton);
        buttonBox.setPadding(DEFAULT_PADDING);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setSpacing(20);

        setBottom(buttonBox);
    }

    /**
     * Writes properties to respective POJO Class
     * @param pojoClass POJO Class to set properties
     */
    @Override
    public void writeToPOJO(Properties pojoClass) {

    }

    /**
     * Represents a Chart Row (Chart Title + xLabel + yLabel)
     */
    private class ChartRow extends GridPane {

        /**
         * Chart Title Text Field
         */
        private TextField chartTitleTextField = new TextField();

        /**
         * xLabel Text Field
         */
        private TextField xLabelTextField = new TextField();

        /**
         * yLabel Text Field
         */
        private TextField yLabelTextField = new TextField();

        /**
         * Creates and prepares a ChartRow with a given index
         *
         * @param index position of ChartRow in the chartRowList
         */
        protected ChartRow(int index) {
            setProperties();
            prepareComponent(index);
        }

        /**
         * Sets Component Properties
         */
        private void setProperties() {
            setPadding(new Insets(5, 20, 5, 20));
            setHgap(50);
            setVgap(10);
            setAlignment(Pos.CENTER);

            chartTitleTextField.setPrefColumnCount(10);
            xLabelTextField.setPrefColumnCount(5);
            yLabelTextField.setPrefColumnCount(5);

            chartTitleTextField.setPromptText("Title");
            xLabelTextField.setPromptText("x Label");
            yLabelTextField.setPromptText("y Label");
        }

        /**
         * Adds Fields to Component
         *
         * @param index index of chartRow
         */
        private void prepareComponent(int index) {
            add(new Label(String.format("Chart[%d]", index)), 0, 0);
            add(chartTitleTextField, 1, 0);
            add(xLabelTextField, 2, 0);
            add(yLabelTextField, 2, 1);
        }
    }
}

