package com.ksatstuttgart.usoc.gui.setup.pane;

import com.ksatstuttgart.usoc.gui.AssignDataWindow;
import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import com.ksatstuttgart.usoc.gui.setup.configuration.Parsable;
import com.ksatstuttgart.usoc.gui.setup.configuration.StatePaneProperties;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;
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
import javafx.stage.Stage;

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
     * Keyword List View
     */
    private ListView<String> keywordListView = new ListView<>();

    /**
     * Segment Map
     */
    private Map<String, List<String>> segmentMap =
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
     * Assign Data to State Keyword Button
     */
    private final Button assignDataBtn =
            new Button("Assign Data");

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

                    keywordListView.getItems().clear();
                    if (newValue == null) {
                        return;
                    }

                    List<String> newKeywordList = segmentMap.get(newValue);

                    keywordListView.getItems().addAll(newKeywordList);
                });

        segmentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ObservableList<String> stringObservableList = FXCollections.observableArrayList();
        segmentListView.setItems(stringObservableList);
        segmentListView.setMaxHeight(170);
        segmentListView.setEditable(false);

        VBox segmentBox = new VBox(new Label("Segments"), segmentListView);
        segmentBox.setSpacing(10);

        // Keyword List View
        keywordListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<String> keywordObservableList = FXCollections.observableArrayList();
        keywordListView.setItems(keywordObservableList);
        keywordListView.setMaxHeight(170);

        VBox keywordBox = new VBox(new Label("Keywords"), keywordListView);
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
        addKeywordBtn.setPrefWidth(70);
        delKeywordBtn.setPrefWidth(75);
        assignDataBtn.setPrefWidth(150);

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
                keywordListView.getItems().clear();
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

            List<String> segmentKeywordList = segmentMap.get(selectedSegment);

            if (segmentKeywordList.contains(input)) {
                return;
            }

            segmentKeywordList.add(input);
            keywordListView.getItems().add(input);
            keywordListView.getSelectionModel().select(input);
        });

        delKeywordBtn.setOnAction(actionEvent -> {
            String selectedKeyword = keywordListView.getSelectionModel().getSelectedItem();

            if (selectedKeyword == null) {
                return;
            }

            String selectedSegment = segmentListView.getSelectionModel().getSelectedItem();

            keywordListView.getItems().remove(selectedKeyword);
            segmentMap.get(selectedSegment).remove(selectedKeyword);
        });

        assignDataBtn.setOnAction(actionEvent -> {
            if (keywordListView.getSelectionModel().getSelectedItems().isEmpty()) {
                return;
            }

            AssignDataWindow window = new AssignDataWindow();

            window.show();
        });

        HBox addDelKeywordBox = new HBox(addKeywordBtn, delKeywordBtn);
        addDelKeywordBox.setSpacing(5);

        buttonPane.add(addSegmentBtn, 0, 0);
        buttonPane.add(delSegmentBtn, 0, 1);
        buttonPane.add(addDelKeywordBox, 1, 0);
        buttonPane.add(assignDataBtn, 1, 1);

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

    @Override
    public void writeToPOJO(Layout pojoClass) {
        StatePaneProperties properties = pojoClass.getStatePaneProperties();

        properties.setEnabled(enabledCheckBox.isSelected());

        List<Segment> segments = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : segmentMap.entrySet()) {
            String segmentName = entry.getKey();
            List<String> keywords = entry.getValue();

            Segment segment = new Segment(segmentName, keywords);
            segments.add(segment);
        }

        properties.setSegments(segments);
    }
}
