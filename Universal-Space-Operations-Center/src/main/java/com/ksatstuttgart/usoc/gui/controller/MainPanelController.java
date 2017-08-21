package com.ksatstuttgart.usoc.gui.controller;

import com.ksatstuttgart.usoc.controller.DataModification;
import java.net.URL;
import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Victor
 */

public class MainPanelController extends DataController implements Initializable {

    @FXML
    public LineChart<Number, Number> lineChart1;
    @FXML
    public LineChart<Number, Number> lineChart2;
    @FXML
    public LineChart<Number, Number> lineChart3;
    @FXML
    public LineChart<Number, Number> lineChart4;
    @FXML
    public LineChart<Number, Number> lineChart5;
    @FXML
    public LineChart<Number, Number> lineChart6;

    @Override
    public void updateData(MessageController mc, USOCEvent e) {
        
        lineChart1.getXAxis().setAutoRanging(true);
        lineChart1.getYAxis().setAutoRanging(true);
        lineChart2.getXAxis().setAutoRanging(true);
        lineChart2.getYAxis().setAutoRanging(true);
        lineChart3.getXAxis().setAutoRanging(true);
        lineChart3.getYAxis().setAutoRanging(true);
        lineChart4.getXAxis().setAutoRanging(true);
        lineChart4.getYAxis().setAutoRanging(true);
        lineChart5.getXAxis().setAutoRanging(true);
        lineChart5.getYAxis().setAutoRanging(true);

        //in case this is an error event, ignore it
        if (e instanceof ErrorEvent) {
            return;
        }

        //go through the data and update the charts
        for (Sensor sensor : mc.getData().getSensors()) {
            //adjust values for the sensor specific to the current experiment
            Sensor adjusted = DataModification.adjustSensorData(sensor);

            //search for thermocouple sensors
            if (adjusted.getSensorName().startsWith("Thermocouple")) {
                //thermocouple sensors have only one variable with the current
                //data scheme it uses the sensor name as variable name 
                for (Var var : adjusted.getVars()) {
                    addVarToChart(var,lineChart1);
                }
            }

            //search for pressure sensors
            if (adjusted.getSensorName().contains("Pressure")) {
                //pressure sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                for (Var var : adjusted.getVars()) {
                    addVarToChart(var,lineChart2);
                }
            }

            //search for pressure sensors
            if (adjusted.getSensorName().contains("GNSS TIME")) {
                //pressure sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                for (Var var : adjusted.getVars()) {
                    addVarToChart(var,lineChart3);
                }
            }

            //search for voltage sensors
            if (adjusted.getSensorName().startsWith("Battery")) {
                //voltage sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                for (Var var : adjusted.getVars()) {
                    addVarToChart(var,lineChart4);
                }
            }

            //search for IMU data
            if (adjusted.getSensorName().contains("IMU")) {
                for (Var var : adjusted.getVars()) {
                    //only visualize quaternion data ignore calibration data
                    //for chart
                    addVarToChart(var,lineChart5);
                }
            }
        }
    }

    private void addVarToChart(Var var, LineChart<Number, Number> chart) {
        XYChart.Series series = getSeriesForChart(var, chart);
        for (Long time : var.getValues().keySet()) {
            series.getData().add(new XYChart.Data<>(time, var.getValues().get(time)));
        }
    }

    private XYChart.Series getSeriesForChart(Var var, LineChart<Number, Number> chart) {
        for (XYChart.Series<Number, Number> series : chart.getData()) {
            if (series.getName().equals(var.getDataName())) {
                return series;
            }
        }
        XYChart.Series series = new XYChart.Series<>();
        series.setName(var.getDataName());
        chart.getData().add(series);
        return series;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        MainController.getInstance().addDataUpdateListener(new UpdateListener());
        //setData();

    }
}
