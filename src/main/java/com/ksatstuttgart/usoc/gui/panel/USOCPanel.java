package com.ksatstuttgart.usoc.gui.panel;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.gui.controller.ChartController;
import com.ksatstuttgart.usoc.gui.setup.configuration.USOCPaneProperties;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Chart;
import com.ksatstuttgart.usoc.gui.worldwind.GNSSPanel;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Optional;

/**
 * Tab Panel
 * Contains GNSS View and Chart Grid
 */
public class USOCPanel extends TabPane {

    /**
     * Properties class
     */
    private USOCPaneProperties properties;

    /**
     * Chart Tab
     */
    private Tab chartTab = new Tab();

    /**
     * Chart Grid
     */
    private GridPane chartGrid = new GridPane();

    /**
     * GNSS Tab
     */
    private Tab gnssTab = new Tab();

    /**
     * Keeps track of the current row index
     * in the chart grid
     */
    private int row = 0;

    /**
     * Keeps track of the current column index
     * in the chart grid
     */
    private int column = 0;

    /**
     * Creates an instance of the Tab Panel
     */
    public USOCPanel() {
        properties = MainController.getInstance()
                .getLayout().getUsocPaneProperties();

        prepareComponents();
    }

    /**
     * Sets up components and prepares layouts
     */
    private void prepareComponents() {
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        minWidth(320);

        // Charts Tab
        ScrollPane chartScroll = new ScrollPane();
        chartScroll.setFitToWidth(true);
        chartScroll.setFitToHeight(true);

        // Adds each chart to the chart grid
        for (Chart chart : properties.getCharts()) {
            addChart(chart);
        }

        prepareContextMenu();

        // Add chart to main tabs
        chartScroll.setContent(chartGrid);

        chartTab.setText("Charts");
        chartTab.setContent(chartScroll);

        getTabs().add(chartTab);

        if (properties.isGnssEnabled()) {
            StackPane gnssStack = new StackPane();
            GNSSPanel.addGNSSPaneltoStackPane(gnssStack);

            gnssTab.setText("GNSS 3D View");
            gnssTab.setContent(gnssStack);

            getTabs().add(gnssTab);
        }
    }

    /**
     * Prepares ChartGrid Context Menu
     */
    private void prepareContextMenu() {
        ContextMenu chartGridContextMenu = new ContextMenu();
        chartGridContextMenu.setImpl_showRelativeToWindow(true);

        MenuItem addChartMenuItem = new MenuItem("Add Chart");
        addChartMenuItem.setOnAction(onAction -> {
            chartGridContextMenu.hide();

            Dialog<Chart> addChartDialog = createAddEditChartDialog(false);

            Optional<Chart> result = addChartDialog.showAndWait();

            result.ifPresent(newChart -> {
                addChart(newChart);
                properties.getCharts().add(newChart);
            });
        });

        chartGridContextMenu.setImpl_showRelativeToWindow(true);

        chartGrid.setOnContextMenuRequested(onRequest -> {
            onRequest.consume();
            chartGridContextMenu.show(chartGrid,
                    onRequest.getScreenX(), onRequest.getScreenY());
        });

        chartGridContextMenu.getItems().addAll(addChartMenuItem);
    }

    /**
     * Creates the add/edit chart dialog
     *
     * @param isEditDialog flag used to tell if the dialog is used to edit or add a chart
     * @return the created dialog
     */
    private Dialog<Chart> createAddEditChartDialog(boolean isEditDialog) {
        Dialog<Chart> dialog = new Dialog<>();

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField title = new TextField();
        title.setPromptText("Title");
        TextField xLabel = new TextField();
        xLabel.setPromptText("x Label");
        TextField yLabel = new TextField();
        yLabel.setPromptText("y Label");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(title, 1, 0);
        grid.add(new Label("x Label:"), 0, 1);
        grid.add(xLabel, 1, 1);
        grid.add(new Label("y Label:"), 0, 2);
        grid.add(yLabel, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Chart(title.getText(),
                        xLabel.getText(), yLabel.getText());
            }
            return null;
        });

        if (isEditDialog) {
            dialog.setTitle("Edit Chart");
            dialog.setHeaderText("Edit a current chart");
        } else {
            dialog.setTitle("Add Chart");
            dialog.setHeaderText("Add a new Chart to the grid");
        }

        return dialog;
    }

    /**
     * Creates a new Line Chart, and adds it to the correct
     * position inside the chartGrid
     *
     * @param chart given chart configuration instance
     */
    private void addChart(Chart chart) {
        final int maxColumns = properties.getChartColumns();

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        ChartController chartController = new ChartController();
        xAxis.setLabel(chart.getxLabel());
        yAxis.setLabel(chart.getyLabel());

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(chart.getTitle());

        // Create context menu for each chart
        ContextMenu chartContextMenu = new ContextMenu();

        MenuItem editChartMenuItem = new MenuItem("Edit");
        editChartMenuItem.setOnAction(onAction -> {
            chartContextMenu.hide();

            Dialog<Chart> editChartDialog = createAddEditChartDialog(true);
            Optional<Chart> res = editChartDialog.showAndWait();

            res.ifPresent(newChart -> {
                chart.setTitle(newChart.getTitle());
                chart.setxLabel(newChart.getxLabel());
                chart.setyLabel(newChart.getyLabel());

                lineChart.setTitle(chart.getTitle());
                lineChart.getXAxis().setLabel(chart.getxLabel());
                lineChart.getYAxis().setLabel(chart.getyLabel());
            });
        });
        MenuItem deleteChartMenuItem = new MenuItem("Delete");
        deleteChartMenuItem.setOnAction(onAction -> {
            chartContextMenu.hide();

            chartGrid.getChildren().remove(lineChart);
            properties.getCharts().remove(chart);
        });

        // Auto-Hides Menu when clicked outside of it
        chartContextMenu.setImpl_showRelativeToWindow(true);
        chartContextMenu.getItems().addAll(editChartMenuItem,
                new SeparatorMenuItem(), deleteChartMenuItem);

        lineChart.setOnContextMenuRequested(onAction -> {
            onAction.consume();
            chartContextMenu.show(lineChart,
                    onAction.getScreenX(), onAction.getScreenY());
        });

        chartController.addChart(lineChart);
        chartGrid.add(lineChart, column, row);

        column++;
        if (column > (maxColumns - 1)) {
            row++;
            column = 0;
        }
    }
}
