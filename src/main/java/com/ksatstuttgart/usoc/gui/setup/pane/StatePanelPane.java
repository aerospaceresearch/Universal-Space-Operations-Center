package com.ksatstuttgart.usoc.gui.setup.pane;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates and prepares the StatePanel Pane
 */
public class StatePanelPane extends BorderPane {

    /**
     * Enabled checkbox
     */
    private final CheckBox enabledCheckBox =
            new CheckBox("Enabled");

    /**
     * List that holds all active rows presented to the user
     */
    private List<KeywordRow> keywordRowList = new ArrayList<>();

    /**
     * Keeps track of the x position of each keyword
     */
    private int xCounter = 0;

    /**
     * Keeps track of the y position of each keyword
     */
    private int yCounter = 0;

    /**
     * Default Node Padding
     */
    private static final Insets DEFAULT_PADDING =
            new Insets(20);

    /**
     * Creates a State Panel Pane
     */
    public StatePanelPane() {
        preparePane();
    }

    /**
     * Prepares component
     */
    private void preparePane() {
        enabledCheckBox.setPadding(DEFAULT_PADDING);

        // Create and add first row to list
        KeywordRow firstRow = new KeywordRow(xCounter, yCounter);
        keywordRowList.add(firstRow);
        yCounter++;
        final VBox keyWordsPaneBox = new VBox(firstRow);
        keyWordsPaneBox.setSpacing(5);

        ScrollPane keywordsPane = new ScrollPane(keyWordsPaneBox);
        keywordsPane.setPadding(DEFAULT_PADDING);
        keywordsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        final Button minusButton = new Button("-");
        minusButton.setPrefSize(30, 30);
        minusButton.setDisable(true);
        minusButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                KeywordRow lastRow = keywordRowList.get(keywordRowList.size() - 1);
                keywordRowList.remove(lastRow);
                keyWordsPaneBox.getChildren().remove(lastRow);
                if (yCounter == 0) {
                    yCounter = 3;
                    xCounter--;
                } else {
                    yCounter--;
                }

                if (keywordRowList.size() == 1) {
                    minusButton.setDisable(true);
                }

            }
        });

        Button plusButton = new Button("+");
        plusButton.setPrefSize(30, 30);
        plusButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                KeywordRow row = new KeywordRow(xCounter, yCounter);
                if (yCounter == 3) {
                    yCounter = 0;
                    xCounter++;
                } else {
                    yCounter++;
                }

                keywordRowList.add(row);
                keyWordsPaneBox.getChildren().add(row);

                minusButton.setDisable(false);
            }
        });

        HBox buttonBox = new HBox(plusButton, minusButton);
        buttonBox.setPadding(DEFAULT_PADDING);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setSpacing(20);

        setTop(enabledCheckBox);
        setCenter(keywordsPane);
        setBottom(buttonBox);
    }
}

/**
 * Represents a Row with a label and a textfield
 */
class KeywordRow extends HBox {

    /**
     * Keyword Label (example: Keyword[1][2]
     */
    private Label keywordLbl = new Label();

    /**
     * Keyword User Input
     */
    private TextField keywordValueTextField = new TextField();

    /**
     * Keyword Text Field column count
     */
    private static final int COLUMN_COUNT = 10;

    /**
     * Spacing between label and text field
     */
    private static final int SPACING = 70;

    /**
     * Constructs an new KeyWord row with a given x and y coordinate
     * @param x x position of the keyword in the grid
     * @param y y position of the keyword in the grid
     */
    protected KeywordRow(int x, int y) {
        keywordLbl.setText(String.format("Keyword[%d][%d]", x, y));
        prepareRow();
    }

    /**
     * Prepares row
     */
    private void prepareRow() {
        keywordValueTextField.setPromptText("Value");
        keywordValueTextField.setPrefColumnCount(COLUMN_COUNT);

        keywordLbl.setFont(new Font("Verdana", 15));

        setAlignment(Pos.CENTER);
        setSpacing(SPACING);
        getChildren().addAll(keywordLbl, keywordValueTextField);
    }

    /**
     * Gets the text field value
     * @return text field value
     */
    protected String getKeywordValue() {
        return keywordValueTextField.getText();
    }
}
