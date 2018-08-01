/*
 * MIT License
 *
 * Copyright (c) 2017 KSat e.V. and AerospaceResearch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.ksatstuttgart.usoc.gui.setup.pane;

import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import com.ksatstuttgart.usoc.gui.setup.configuration.Parsable;
import com.ksatstuttgart.usoc.gui.setup.configuration.StatePaneProperties;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Creates and prepares the StatePanel Pane
 */
public class StatePanelPane extends BorderPane implements Parsable {

    /**
     * Default Node Padding
     */
    private static final Insets DEFAULT_PADDING =
            new Insets(20);

    /**
     * Segment List View
     */
    private ListView<String> segmentListView = new ListView<>();

    /**
     * State List View
     */
    private ListView<State> stateListView = new ListView<>();

    /**
     * Segment Map
     */
    private Map<String, List<State>> segmentMap =
            new HashMap<>();
    /**
     * Enabled checkbox
     */
    private final CheckBox enabledCheckBox =
            new CheckBox("Enabled");

    /**
     * Add Segment Button
     */
    private final Button addSegmentBtn =
            new Button("Add Segment");
    /**
     * Delete Segment Button
     */
    private final Button delSegmentBtn =
            new Button("Remove Segment");
    /**
     * Add Keyword Button
     */
    private final Button addKeywordBtn =
            new Button("Add");

    /**
     * Delete Keyword Button
     */
    private final Button delKeywordBtn =
            new Button("Remove");

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
        enabledCheckBox.setSelected(true);
        enabledCheckBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            boolean enabled = newValue;

            // If the component is not enabled/selected, deactivates both checkboxes
            getCenter().setDisable(!enabled);
            getBottom().setDisable(!enabled);
        });

        setTop(enabledCheckBox);
        setCenter(prepareCenter());
        setBottom(prepareBottom());
    }

    /**
     * Prepare Center Node
     *
     * @return center node
     */
    private Node prepareCenter() {
        GridPane centerPane = new GridPane();

        centerPane.setPadding(DEFAULT_PADDING);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setHgap(70);

        // Segment List View
        segmentListView.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {

                    stateListView.getItems().clear();
                    if (newValue == null) {
                        return;
                    }

                    List<State> newKeywordList = segmentMap.get(newValue);

                    stateListView.getItems().addAll(newKeywordList);
                });

        segmentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ObservableList<String> stringObservableList = FXCollections.observableArrayList();
        segmentListView.setItems(stringObservableList);
        segmentListView.setMaxHeight(170);
        segmentListView.setEditable(false);

        VBox segmentBox = new VBox(new Label("Segments"), segmentListView);
        segmentBox.setSpacing(10);

        // Keyword List View
        stateListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<State> keywordObservableList = FXCollections.observableArrayList();
        stateListView.setItems(keywordObservableList);
        stateListView.setMaxHeight(170);

        VBox keywordBox = new VBox(new Label("Keywords"), stateListView);
        keywordBox.setSpacing(10);

        centerPane.add(segmentBox, 0, 0);
        centerPane.add(keywordBox, 1, 0);

        return centerPane;
    }

    /**
     * Prepare Bottom Node
     *
     * @return bottom node
     */
    private Node prepareBottom() {
        GridPane buttonPane = new GridPane();

        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setPadding(DEFAULT_PADDING);
        buttonPane.setVgap(10);
        buttonPane.setHgap(100);

        addSegmentBtn.setPrefWidth(150);
        delSegmentBtn.setPrefWidth(150);
        addKeywordBtn.setPrefWidth(150);
        delKeywordBtn.setPrefWidth(150);

        addSegmentBtn.setOnAction(actionEvent -> {
            String input = StatePanelPane.this.showDialog("Input Dialog", "Add Segment",
                    "Please insert segment title");

            if (input == null) {
                return;
            }

            if (segmentMap.containsKey(input)) {
                return;
            }

            segmentListView.getItems().add(input);
            segmentMap.put(input, new ArrayList<>());
            segmentListView.getSelectionModel().select(input);
        });

        delSegmentBtn.setOnAction(actionEvent -> {
            String selectedItem = segmentListView.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                segmentListView.getItems().remove(selectedItem);
                segmentMap.remove(selectedItem);
                stateListView.getItems().clear();
            }
        });

        addKeywordBtn.setOnAction(actionEvent -> {
            String selectedSegment = segmentListView.getSelectionModel().getSelectedItem();
            if (selectedSegment == null) {
                return;
            }

            String input = showDialog("Input Dialog", "Add Keyword",
                    "Please insert keyword value");

            if (input == null || input.isEmpty()) {
                return;
            }

            List<State> segmentStateList = segmentMap.get(selectedSegment);

            for (State s :
                    segmentStateList) {
                if (s.getKeyword().equals(input)) {
                    return;
                }
            }

            State state = new State();
            state.setKeyword(input);
            segmentStateList.add(state);
            stateListView.getItems().add(state);
            stateListView.getSelectionModel().select(state);
        });

        delKeywordBtn.setOnAction(actionEvent -> {
            State selectedState = stateListView.getSelectionModel().getSelectedItem();

            if (selectedState == null) {
                return;
            }

            String selectedSegment = segmentListView.getSelectionModel().getSelectedItem();

            stateListView.getItems().remove(selectedState);
            segmentMap.get(selectedSegment).remove(selectedState);
        });


        buttonPane.add(addSegmentBtn, 0, 0);
        buttonPane.add(delSegmentBtn, 0, 1);
        buttonPane.add(addKeywordBtn, 1, 0);
        buttonPane.add(delKeywordBtn, 1, 1);

        return buttonPane;
    }

    /**
     * Shows an input dialog
     *
     * @param title       dialog title
     * @param header      dialog header
     * @param contentText content
     * @return input value or null if user cancelled dialog
     */
    private String showDialog(String title, String header, String contentText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(contentText);

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get().trim();
        }

        return null;
    }

    /**
     *
     * @param pojoClass POJO Class to set properties
     */
    @Override
    public void writeToPOJO(Layout pojoClass) {
        StatePaneProperties properties = pojoClass.getStatePaneProperties();

        properties.setEnabled(enabledCheckBox.isSelected());

        List<Segment> segments = new ArrayList<>();
        for (Map.Entry<String, List<State>> entry : segmentMap.entrySet()) {
            String segmentName = entry.getKey();
            List<State> states = entry.getValue();

            Segment segment = new Segment(segmentName, states);
            segments.add(segment);
        }

        properties.setSegments(segments);
    }
}
