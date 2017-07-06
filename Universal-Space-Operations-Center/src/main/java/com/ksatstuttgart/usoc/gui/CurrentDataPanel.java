/*
 * The MIT License
 *
 * Copyright 2017 KSat e.V.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * <h1>MailUpdateListener</h1>
 * This GUI class provides an overview of the sensor data received.
 *
 * @author Valentin Starlinger
 * @version 1.0
 */
public class CurrentDataPanel extends JPanel {

    private final LineChart batteryChart, thermocoupleChart, coldjunctionChart,
            imuQuaternionChart, pressureChart, iridiumChart;

    private final GPSPanel gpsp;

    private static final Font BIGFONT = new Font("Helvetica,Arial", Font.BOLD, 14);

    public CurrentDataPanel() {
        super();
        
        gpsp = new GPSPanel();

        /**
         *
         */
        JLabel jl_temp = new JLabel("Temperature:");
        jl_temp.setFont(BIGFONT);

        thermocoupleChart = new LineChart("Thermocouples", "Time [s]", "Temperature [°C]");
        coldjunctionChart = new LineChart("Coldjunctions", "Time [s]", "Temperature [°C]");

        JLabel jl_pressure = new JLabel("Pressure:");
        jl_pressure.setFont(BIGFONT);

        pressureChart = new LineChart("Pressuresensors", "Time [s]", "Pressure [kPa]");

        JLabel jl_imu = new JLabel("Attitude:");
        jl_imu.setFont(BIGFONT);
        JLabel jl_battery = new JLabel("Battery Voltage");
        jl_battery.setFont(BIGFONT);
        JLabel jl_transceiver = new JLabel("Iridium");
        jl_transceiver.setFont(BIGFONT);

        imuQuaternionChart = new LineChart("IMU", "Time [s]", "Quaternions");
        batteryChart = new LineChart("Battery Voltage", "Time [s]", "Voltage [V]");
        iridiumChart = new LineChart("Iridium", "Time [s]", "");

        GroupLayout gl = new GroupLayout(this);
        this.setLayout(gl);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(jl_temp)
                .addGroup(gl.createParallelGroup()
                        .addComponent(thermocoupleChart)
                        .addComponent(coldjunctionChart)
                )
                .addGroup(gl.createParallelGroup()
                        .addComponent(jl_imu)
                        .addComponent(jl_pressure)
                )
                .addGroup(gl.createParallelGroup()
                        .addComponent(imuQuaternionChart)
                        .addComponent(pressureChart)
                )
                .addGroup(gl.createParallelGroup()
                        .addComponent(jl_battery)
                        .addComponent(jl_transceiver)
                )
                .addGroup(gl.createParallelGroup()
                        .addComponent(batteryChart)
                        .addComponent(iridiumChart)
                )
                .addGroup(gl.createParallelGroup()
                        .addComponent(gpsp)
                )
        );

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup()
                        .addComponent(jl_temp)
                        .addComponent(thermocoupleChart)
                        .addComponent(jl_imu)
                        .addComponent(imuQuaternionChart)
                        .addComponent(jl_battery)
                        .addComponent(batteryChart)
                )
                .addGroup(gl.createParallelGroup()
                        .addComponent(coldjunctionChart)
                        .addComponent(jl_pressure)
                        .addComponent(pressureChart)
                        .addComponent(jl_transceiver)
                        .addComponent(iridiumChart)
                        .addComponent(gpsp)
                )
        );
    }

    public void updateData(MessageController mc) {

        //go through the data and update the charts
        for (Sensor sensor : mc.getData().getData().getSensors()) {
            //search for thermocouple sensors
            if (sensor.getSensorName().startsWith("Thermocouple")) {
                //thermocouple sensors have only one variable with the current
                //data scheme it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                thermocoupleChart.addSeries(sensor.getSensorName()
                        , sensor.getVars().get(0).getValues()
                        , sensor.getVars().get(0).getDataType());
            }

            //search for coldjunction sensors
            if (sensor.getSensorName().startsWith("ColdJunction")) {
                //coldjunction sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                coldjunctionChart.addSeries(sensor.getSensorName()
                        , sensor.getVars().get(0).getValues()
                        , sensor.getVars().get(0).getDataType());
                coldjunctionChart.updateAxis();

            }
            
            //search for pressure sensors
            if (sensor.getSensorName().contains("Pressure")) {
                //pressure sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                pressureChart.addSeries(sensor.getSensorName()
                        , sensor.getVars().get(0).getValues()
                        , sensor.getVars().get(0).getDataType());
                pressureChart.updateAxis();

            }
            
            //search for voltage sensors
            if (sensor.getSensorName().startsWith("Battery")) {
                //voltage sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                batteryChart.addSeries(sensor.getSensorName()
                        , sensor.getVars().get(0).getValues()
                        , sensor.getVars().get(0).getDataType());
                batteryChart.updateAxis();

            }
            
            //search for IMU data
            if (sensor.getSensorName().contains("IMU")){
                for (Var var : sensor.getVars()) {
                    //only visualize quaternion data ignore calibration data
                    //for chart
                    if(var.getDataName().startsWith("Quaternion")){
                        imuQuaternionChart.addSeries(var.getDataName()
                                , var.getValues()
                                , var.getDataType());
                        imuQuaternionChart.updateAxis();
                    }
                }
            }
            
            //search for transceiver data
            if (sensor.getSensorName().startsWith("Carrier")){
                //transceiver sensors have only has variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed, should have max and current in same
                //sensor
                iridiumChart.addSeries(sensor.getSensorName()
                        , sensor.getVars().get(0).getValues()
                        , sensor.getVars().get(0).getDataType());
                iridiumChart.updateAxis();    
            }
            
            //search for gps data
            if (sensor.getSensorName().startsWith("UBLOX")){
                //gps panel has its separate update method
                gpsp.updateGPS(sensor);
            }
        }
    }

}
