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

import com.ksatstuttgart.usoc.controller.DataModification;
import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.data.DataSource;
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <h1>MailUpdateListener</h1>
 * This GUI class provides an overview of the sensor data received.
 *
 * @author Valentin Starlinger
 * @version 1.0
 */
public class CurrentDataPanel extends DataPanel {

    private final LineChart batteryChart, thermocoupleChart,
            imuQuaternionChart, pressureChart, timerChart;

    private final GPSPanel gpsp;

    private static final Font BIGFONT = new Font("Helvetica,Arial", Font.BOLD, 14);

    public CurrentDataPanel() {
        super();
        
        gpsp = new GPSPanel();
        timerChart = new LineChart("Time Signal","Time [s]", "Time");

        /**
         *
         */
        JLabel jl_temp = new JLabel("Temperature:");
        jl_temp.setFont(BIGFONT);

        thermocoupleChart = new LineChart("Thermocouples", "Time [s]", "Temperature [°C]");

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

        GroupLayout gl = new GroupLayout(this);
        this.setLayout(gl);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup()
                        .addComponent(jl_temp)
                        .addComponent(jl_battery)
                )
                .addGroup(gl.createParallelGroup()
                        .addComponent(thermocoupleChart)
                        .addComponent(batteryChart)
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
                        .addComponent(gpsp)
                        .addComponent(timerChart)
                )
        );

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup()
                        .addComponent(jl_temp)
                        .addComponent(thermocoupleChart)
                        .addComponent(jl_imu)
                        .addComponent(imuQuaternionChart)
                        .addComponent(gpsp)
                )
                .addGroup(gl.createParallelGroup()
                        .addComponent(jl_battery)
                        .addComponent(batteryChart)
                        .addComponent(jl_pressure)
                        .addComponent(pressureChart)
                        .addComponent(timerChart)
                )
        );
    }

    @Override
    public void updateData(MessageController mc, USOCEvent e) {
        
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
                thermocoupleChart.addSeries(adjusted.getSensorName()
                        , adjusted.getVars().get(0).getValues()
                        , adjusted.getVars().get(0).getDataType());
            }

            //search for pressure sensors
            if (adjusted.getSensorName().contains("Pressure")) {
                //pressure sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                pressureChart.addSeries(adjusted.getSensorName()
                        , adjusted.getVars().get(0).getValues()
                        , adjusted.getVars().get(0).getDataType());
                pressureChart.updateAxis();

            }
            
            //search for pressure sensors
            if (adjusted.getSensorName().contains("GNSS TIME")) {
                //pressure sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                timerChart.addSeries(adjusted.getSensorName()
                        , adjusted.getVars().get(0).getValues()
                        , adjusted.getVars().get(0).getDataType());
                timerChart.updateAxis();

            }
            
            //search for voltage sensors
            if (adjusted.getSensorName().startsWith("Battery")) {
                //voltage sensors have only one variable with the current
                //data scheme and it uses the sensor name as variable name 
                //TODO: this needs to be fixed
                batteryChart.addSeries(adjusted.getSensorName()
                        , adjusted.getVars().get(0).getValues()
                        , adjusted.getVars().get(0).getDataType());
                batteryChart.updateAxis();

            }
            
            //search for IMU data
            if (adjusted.getSensorName().contains("IMU")){
                for (Var var : adjusted.getVars()) {
                    //only visualize quaternion data ignore calibration data
                    //for chart
                    imuQuaternionChart.addSeries(var.getDataName()
                            , var.getValues()
                            , var.getDataType());
                    imuQuaternionChart.updateAxis();
                }
            }
            
            //search for gps data
            if (adjusted.getSensorName().startsWith("UBLOX")){
                //gps panel has its separate update method
                gpsp.updateGPS(adjusted);
            }
        }
    }
}
