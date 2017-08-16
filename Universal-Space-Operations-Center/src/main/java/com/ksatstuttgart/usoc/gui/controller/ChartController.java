package com.ksatstuttgart.usoc.gui.controller; 

import java.net.URL; 
import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.data.USOCEvent;
import java.util.ResourceBundle; 
import javafx.fxml.Initializable; 
import javafx.scene.chart.LineChart; 
import javafx.scene.chart.XYChart; 

/** 
 * 
 * @author Victor 
 */ 

public class ChartController extends DataController implements Initializable { 

    public LineChart<Integer, Integer> lineChart1;
    public LineChart<Integer, Integer> lineChart2;
    public LineChart<Integer, Integer> lineChart3;
    public LineChart<Integer, Integer> lineChart4;
    public LineChart<Integer, Integer> lineChart5;
    public LineChart<Integer, Integer> lineChart6;

    public void updateData() {
        lineChart1.getXAxis().setAutoRanging(true); 
        lineChart1.getYAxis().setAutoRanging(true); 
        XYChart.Series series1 = new XYChart.Series<>(); 
        series1.getData().add(new XYChart.Data<>(5, 23)); 
        series1.getData().add(new XYChart.Data<>(6, 15)); 
        lineChart1.getData().add(series1); 

        lineChart2.getXAxis().setAutoRanging(true); 
        lineChart2.getYAxis().setAutoRanging(true); 
        XYChart.Series series2 = new XYChart.Series<>(); 
        series2.getData().add(new XYChart.Data<>(5, 23)); 
        series2.getData().add(new XYChart.Data<>(6, 15)); 
        lineChart2.getData().add(series2); 

        lineChart3.getXAxis().setAutoRanging(true); 
        lineChart3.getYAxis().setAutoRanging(true); 
        XYChart.Series series3 = new XYChart.Series<>(); 
        series3.getData().add(new XYChart.Data<>(5, 23)); 
        series3.getData().add(new XYChart.Data<>(6, 15)); 
        lineChart3.getData().add(series3); 

        lineChart4.getXAxis().setAutoRanging(true); 
        lineChart4.getYAxis().setAutoRanging(true); 
        XYChart.Series series4 = new XYChart.Series<>(); 
        series4.getData().add(new XYChart.Data<>(5, 23)); 
        series4.getData().add(new XYChart.Data<>(6, 15)); 
        lineChart4.getData().add(series4); 

        lineChart5.getXAxis().setAutoRanging(true); 
        lineChart5.getYAxis().setAutoRanging(true); 
        XYChart.Series series5 = new XYChart.Series<>(); 
        series5.getData().add(new XYChart.Data<>(5, 23)); 
        series5.getData().add(new XYChart.Data<>(6, 15)); 
        lineChart5.getData().add(series5); 

        lineChart6.getXAxis().setAutoRanging(true); 
        lineChart6.getYAxis().setAutoRanging(true); 
        XYChart.Series series6 = new XYChart.Series<>(); 
        series6.getData().add(new XYChart.Data<>(5, 23)); 
        series6.getData().add(new XYChart.Data<>(6, 15)); 
        lineChart6.getData().add(series6); 

    } 

    @Override 
    public void initialize(URL url, ResourceBundle rb) { 
        // TODO
        MainController.getInstance().addDataUpdateListener(new UpdateListener());
    } 
    @Override
    public void updateData(MessageController msgController, USOCEvent e) {
        updateData();
    }
}
