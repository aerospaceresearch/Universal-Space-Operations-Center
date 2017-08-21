package com.ksatstuttgart.usoc.gui.controller; 

import com.ksatstuttgart.usoc.controller.DataModification;
import java.net.URL; 
import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
/** 
 * 
 * @author Victor 
 */ 

public class MainPanelController extends DataController implements Initializable { 

    public LineChart<Long, Object> lineChart1;
    public XYChart.Series series1;
    public LineChart<Long, Object> lineChart2;
    public XYChart.Series series2;
    public LineChart<Long, Object> lineChart3;
    public XYChart.Series series3;
    public LineChart<Long, Object> lineChart4;
    public XYChart.Series series4;
    public LineChart<Long, Object> lineChart5;
    public XYChart.Series series5;
    public LineChart<Long, Object> lineChart6;
    public XYChart.Series series6;
    
    
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
        if(e instanceof ErrorEvent){
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
                //TODO: this needs to be fixed
                Map.Entry<Long, Object> entry = adjusted.getVars().get(0).getValues().lastEntry();
                series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
                lineChart1.getData().add(series1);  
            }

            //search for pressure sensors
            if (adjusted.getSensorName().contains("Pressure")) {
                //pressure sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                Map.Entry<Long, Object> entry = adjusted.getVars().get(0).getValues().lastEntry();
                series2.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
                lineChart2.getData().add(series2); 
            }
            
            //search for pressure sensors
            if (adjusted.getSensorName().contains("GNSS TIME")) {
                //pressure sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                Map.Entry<Long, Object> entry = adjusted.getVars().get(0).getValues().lastEntry();
                series3.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
                lineChart3.getData().add(series3); 
            }
            
            //search for voltage sensors
            if (adjusted.getSensorName().startsWith("Battery")) {
                //voltage sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                Map.Entry<Long, Object> entry = adjusted.getVars().get(0).getValues().lastEntry();
                series4.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
                lineChart4.getData().add(series4); 
            }
            
            //search for IMU data
            if (adjusted.getSensorName().contains("IMU")){
                for (Var var : adjusted.getVars()) {
                    //only visualize quaternion data ignore calibration data
                    //for chart
                    Map.Entry<Long, Object> entry = adjusted.getVars().get(0).getValues().lastEntry();
                    series5.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
                    lineChart5.getData().add(series5); 
                }
            }
        }
    }

    @Override 
    public void initialize(URL url, ResourceBundle rb) { 
        // TODO
        MainController.getInstance().addDataUpdateListener(new UpdateListener());
        //setData();

    }
}
