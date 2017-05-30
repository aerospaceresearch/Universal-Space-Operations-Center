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

import com.ksatstuttgart.usoc.controller.DataController;
import com.ksatstuttgart.usoc.data.sensors.Mode;
import com.ksatstuttgart.usoc.data.sensors.chartData.ChartSensor;
import com.ksatstuttgart.usoc.data.sensors.GPSData;
import com.ksatstuttgart.usoc.data.sensors.IMUData;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
* <h1>MailUpdateListener</h1>
* This GUI class provides an overview of the sensor data received.
*
* @author  Valentin Starlinger
* @version 1.0
*/

public class CurrentDataPanel extends JPanel {
    
    private final LineChart batteryChart, thermocoupleChart, coldjunctionChart, 
            thermopileChart, imuQuaternionChart, imuEulerChart, pressureChart, 
            iridiumChart;
    
    private final JLabel jl_iss, jl_ihigh, jl_ilow, jl_tries;
    
    private final MCUPanel mcup1, mcup2, mcup3;
    
    private final GPSPanel gpsp;
    
    private static final Font BIGFONT = new Font("Helvetica,Arial", Font.BOLD, 14);

    public CurrentDataPanel() {
        super();
       
        mcup1 = new MCUPanel(1);
        mcup2 = new MCUPanel(2);
        mcup3 = new MCUPanel(3);
        
        JPanel ip = new JPanel();
        ip.setBorder(BorderFactory.createTitledBorder("Iridium"));
        
        GridLayout gl_i = new GridLayout(4,2);
        gl_i.setHgap(5);
        gl_i.setVgap(5);
        ip.setLayout(gl_i);
        
        ip.add(new JLabel("Signal strength:",SwingConstants.LEFT));
        jl_iss = new JLabel("",SwingConstants.LEFT);
        ip.add(jl_iss);
        ip.add(new JLabel("Tries:",SwingConstants.LEFT));
        jl_tries = new JLabel("",SwingConstants.LEFT);
        ip.add(jl_tries);
        ip.add(new JLabel("High:",SwingConstants.LEFT));
        jl_ihigh = new JLabel("",SwingConstants.LEFT);
        ip.add(jl_ihigh);
        ip.add(new JLabel("Low:",SwingConstants.LEFT));
        jl_ilow = new JLabel("",SwingConstants.LEFT);
        ip.add(jl_ilow);
        
        gpsp = new GPSPanel();
        
        /**
         * 
         */
        JLabel jl_temp = new JLabel("Temperature:");
        jl_temp.setFont(BIGFONT);
        
        thermocoupleChart = new LineChart("Thermocouples","Time [s]","Temperature [°C]");
        coldjunctionChart = new LineChart("Coldjunctions","Time [s]","Temperature [°C]");
        
        JLabel jl_therm = new JLabel("Thermopile:");
        jl_therm.setFont(BIGFONT);
        JLabel jl_p = new JLabel("Pressure:");
        jl_p.setFont(BIGFONT);
        
        thermopileChart = new LineChart("Thermopile","Time [s]","Temperature [°C]");
        pressureChart = new LineChart("Pressuresensors","Time [s]","Pressure [kPa]");
        
        JLabel jl_pos = new JLabel("Attitude:");
        jl_pos.setFont(BIGFONT);
        JLabel jl_bat = new JLabel("Battery Voltage");
        jl_bat.setFont(BIGFONT);
        JLabel jl_i = new JLabel("Iridium");
        jl_i.setFont(BIGFONT);
        
        imuQuaternionChart = new LineChart("IMU","Time [s]","Quaternions");
        imuEulerChart = new LineChart("IMU","Time [s]","angular rate [deg/s]");
        batteryChart = new LineChart("Battery Voltage","Time [s]","Voltage [V]");
        iridiumChart = new LineChart("Iridium","Time [s]","");
        
        GroupLayout gl = new GroupLayout(this);
        this.setLayout(gl);
        
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        
        gl.setVerticalGroup(gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup()
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(mcup1)
                                .addComponent(mcup2)
                                .addComponent(mcup3)
                                .addComponent(gpsp)
                            )
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(jl_temp)
                                .addGroup(gl.createParallelGroup()
                                        .addComponent(thermocoupleChart)
                                        .addComponent(coldjunctionChart)
                                )
                                .addGroup(gl.createParallelGroup()
                                        .addComponent(jl_therm)
                                        .addComponent(jl_p)
                                )
                                .addGroup(gl.createParallelGroup()
                                        .addComponent(thermopileChart)
                                        .addComponent(pressureChart)
                                )
                                .addGroup(gl.createParallelGroup()
                                        .addComponent(jl_pos)
                                )
                                .addGroup(gl.createParallelGroup()
                                        .addComponent(imuQuaternionChart)
                                        .addComponent(imuEulerChart)
                                )
                                .addGroup(gl.createParallelGroup()
                                        .addComponent(jl_bat)
                                        .addComponent(jl_i)
                                )
                                .addGroup(gl.createParallelGroup()
                                        .addComponent(batteryChart)
                                        .addComponent(iridiumChart)
                                )
                            )
                    )
                    
        );
        
        gl.setHorizontalGroup(gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup()
                        .addComponent(mcup1)
                        .addComponent(mcup2)
                        .addComponent(mcup3) 
                        .addComponent(gpsp)
                    )
                    .addGroup(gl.createParallelGroup()
                        .addComponent(jl_temp)
                        .addComponent(thermocoupleChart)
                        .addComponent(jl_therm)
                        .addComponent(thermopileChart)
                        .addComponent(jl_pos)
                        .addComponent(imuQuaternionChart)
                        .addComponent(jl_bat)
                        .addComponent(batteryChart)
                    )
                    .addGroup(gl.createParallelGroup()
                        .addComponent(coldjunctionChart)
                        .addComponent(jl_p)
                        .addComponent(pressureChart)
                        .addComponent(imuEulerChart)
                        .addComponent(jl_i)
                        .addComponent(iridiumChart)
                    )
                    
        );
        
    }
    
    public void updateData(DataController ip) {
        
        thermocoupleChart.addSeries("tc1", ip.getThermocouple1());
        thermocoupleChart.addSeries("tc2", ip.getThermocouple2());
        thermocoupleChart.addSeries("tc3", ip.getThermocouple3());
        thermocoupleChart.addSeries("tc4", ip.getThermocouple4());
        thermocoupleChart.addSeries("tc5", ip.getThermocouple5());
        thermocoupleChart.addSeries("tc6", ip.getThermocouple6());
        thermocoupleChart.updateAxis();
        
        coldjunctionChart.addSeries("cj1", ip.getColdjunction1());
        coldjunctionChart.addSeries("cj2", ip.getColdjunction2());
        coldjunctionChart.addSeries("cj3", ip.getColdjunction3());
        coldjunctionChart.addSeries("cj4", ip.getColdjunction4());
        coldjunctionChart.addSeries("cj5", ip.getColdjunction5());
        coldjunctionChart.addSeries("cj6", ip.getColdjunction6());
        coldjunctionChart.updateAxis();
        
        batteryChart.addSeries("battery", ip.getBatteryVoltage());
        batteryChart.updateAxis();
        
        if(ip.getMcus().get(0).getMode() != Mode.LOWALT){
            thermopileChart.addSeries("tp", ip.getThermopile());
        }
        
        pressureChart.addSeries("spi1", ip.getDigitalPressure1());
        pressureChart.addSeries("spi2", ip.getDigitalPressure2());
        pressureChart.addSeries("analog", ip.getAnalogPressure());
        pressureChart.updateAxis();
        
        ArrayList<IMUData> imu = ip.getImu();
        
        ArrayList<ChartSensor> imu_qw = new ArrayList<>();
        ArrayList<ChartSensor> imu_qx = new ArrayList<>();
        ArrayList<ChartSensor> imu_qy = new ArrayList<>();
        ArrayList<ChartSensor> imu_qz = new ArrayList<>();
        
        ArrayList<ChartSensor> imu_rx = new ArrayList<>();
        ArrayList<ChartSensor> imu_ry = new ArrayList<>();
        ArrayList<ChartSensor> imu_rz = new ArrayList<>();
        
        for (IMUData imu1 : imu) {
            imu_qw.add(imu1.getQw());
            imu_qx.add(imu1.getQx());
            imu_qy.add(imu1.getQy());
            imu_qz.add(imu1.getQz());
            
            if(imu1.hasRs()){
                imu_rx.add(imu1.getRx());
                imu_ry.add(imu1.getRy());
                imu_rz.add(imu1.getRz());
            }
        }
        
        imuQuaternionChart.addSeries("qw", imu_qw);
        imuQuaternionChart.addSeries("qx", imu_qx);
        imuQuaternionChart.addSeries("qy", imu_qy);
        imuQuaternionChart.addSeries("qz", imu_qz);
        imuQuaternionChart.updateAxis();
       
        imuEulerChart.addSeries("rx", imu_rx);
        imuEulerChart.addSeries("ry", imu_ry);
        imuEulerChart.addSeries("rz", imu_rz);
        imuEulerChart.updateAxis();
        
        mcup1.setMode(ip.getMcus().get(0).getMode());
        mcup1.setTime(ip.getMcus().get(0).getSystemtime());
        mcup1.setRc(ip.getMcus().get(0).getResetcounter());
        mcup1.setMsgnum(ip.getMcus().get(0).getMsgnumber());
        
        mcup2.setMode(ip.getMcus().get(1).getMode());
        mcup2.setTime(ip.getMcus().get(1).getSystemtime());
        mcup2.setRc(ip.getMcus().get(1).getResetcounter());
        mcup2.setMsgnum(ip.getMcus().get(1).getMsgnumber());
        
        mcup3.setMode(ip.getMcus().get(2).getMode());
        mcup3.setTime(ip.getMcus().get(2).getSystemtime());
        mcup3.setRc(ip.getMcus().get(2).getResetcounter());
        mcup3.setMsgnum(ip.getMcus().get(2).getMsgnumber());
        
        iridiumChart.addSeries("Signalstrength",ip.getTransceiver().getSignalstrength());
        iridiumChart.addSeries("Alltime High",ip.getTransceiver().getAlltimeHigh()); 
        iridiumChart.addSeries("Alltime Low",ip.getTransceiver().getAlltimeLow()); 
        iridiumChart.addSeries("Tries",ip.getTransceiver().getTries());
        iridiumChart.updateAxis();

        if (ip.getMcus().get(0).getMode() != Mode.HIGHALT) {

            for (GPSData gp : ip.getGps()) {
                if (gp.getLat() < 40 || gp.getLat() > 90) {
                    continue;
                }

                if (gp.getLon() < 10 || gp.getLon() > 40) {
                    continue;
                }
            }
        }
    }
    
}
