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
package com.ksatstuttgart.usoc.controller;

import com.ksatstuttgart.usoc.data.sensors.Mode;
import com.ksatstuttgart.usoc.data.sensors.SensorPos;
import com.ksatstuttgart.usoc.data.sensors.chartData.ColdjunctionChart;
import com.ksatstuttgart.usoc.data.sensors.chartData.ChartSensor;
import com.ksatstuttgart.usoc.data.sensors.GPSData;
import com.ksatstuttgart.usoc.data.sensors.IMUData;
import com.ksatstuttgart.usoc.data.sensors.TransceiverData;
import com.ksatstuttgart.usoc.data.sensors.chartData.MCU;
import com.ksatstuttgart.usoc.data.sensors.chartData.PressureChart;
import com.ksatstuttgart.usoc.data.sensors.SDHealth;
import com.ksatstuttgart.usoc.data.sensors.chartData.ThermocoupleChart;
import com.ksatstuttgart.usoc.data.sensors.chartData.ThermopileChart;
import com.ksatstuttgart.usoc.data.sensors.chartData.BatteryChart;
import java.util.ArrayList;

/**
 *
 * @author valentinstarlinger
 */
public class DataController {

    private final int posPB = 47;
    private int timeOffset = 0;

    private final String content;

    private ArrayList<MCU> mcus;
    private ChartSensor batteryVoltage;
    private TransceiverData transceiver;
    private SDHealth sd_card;
    private ArrayList<ChartSensor> digitalPressure1, digitalPressure2, analogPressure;
    private ArrayList<IMUData> imu;
    private ArrayList<ChartSensor> thermocouple1, thermocouple2, thermocouple3, 
                                    thermocouple4, thermocouple5, thermocouple6;
    private ArrayList<ChartSensor> coldjunction1, coldjunction2, coldjunction3, 
                                    coldjunction4, coldjunction5, coldjunction6;
    private ArrayList<ChartSensor> thermopile;
    private ArrayList<GPSData> gps;

    public DataController(String content) {
        this(content,0);
    }

    public DataController(String content, int time) {
        this.content = content;
        this.timeOffset = time;

        parseContent();
    }

    public boolean isOK() {
        return (content.matches("[01]*"));// && checkParity());
    }

    public boolean checkParity() {
        int c = 0;
        for (int i = 0; i < content.length(); i++) {
            if (i == posPB) {
                continue;
            }
            if (content.charAt(i) == '1') {
                c++;
            }
        }

        return ((c % 2) == Integer.parseInt(content.charAt(posPB) + ""));

    }

    private void parseContent() {
        //first parse HKdata

        mcus = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            mcus.add(new MCU((i + 1), content, timeOffset));
        }

        Mode m = mcus.get(0).getMode();
        int time = mcus.get(0).getSystemtime();

        batteryVoltage = new BatteryChart(content, m, time);
        transceiver = new TransceiverData(content, m, time);
        sd_card = new SDHealth(content, m);

        digitalPressure1 = new ArrayList<>();
        digitalPressure2 = new ArrayList<>();
        analogPressure = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            digitalPressure1.add(new PressureChart(content, i + 1, 1, m, time));
            digitalPressure2.add(new PressureChart(content, i + 1, 2, m, time));
            analogPressure.add(new PressureChart(content, i + 1, 3, m, time));
        }

        imu = new ArrayList<>();

        for (int i = 0; i < SensorPos.IMU_C.getFrequency(m); i++) {
            imu.add(new IMUData(content, i + 1, m, time));
        }

        thermocouple1 = new ArrayList<>();
        thermocouple2 = new ArrayList<>();
        thermocouple3 = new ArrayList<>();
        thermocouple4 = new ArrayList<>();
        thermocouple5 = new ArrayList<>();
        thermocouple6 = new ArrayList<>();

        for (int i = 0; i < SensorPos.TC.getFrequency(m); i++) {
            thermocouple1.add(new ThermocoupleChart(content, i + 1, 1, m, time));
            thermocouple2.add(new ThermocoupleChart(content, i + 1, 2, m, time));
            thermocouple3.add(new ThermocoupleChart(content, i + 1, 3, m, time));
            thermocouple4.add(new ThermocoupleChart(content, i + 1, 4, m, time));
            thermocouple5.add(new ThermocoupleChart(content, i + 1, 5, m, time));
            thermocouple6.add(new ThermocoupleChart(content, i + 1, 6, m, time));
        }

        coldjunction1 = new ArrayList<>();
        coldjunction2 = new ArrayList<>();
        coldjunction3 = new ArrayList<>();
        coldjunction4 = new ArrayList<>();
        coldjunction5 = new ArrayList<>();
        coldjunction6 = new ArrayList<>();

        for (int i = 0; i < SensorPos.CJ.getFrequency(m); i++) {
            coldjunction1.add(new ColdjunctionChart(content, i + 1, 1, m, time));
            coldjunction2.add(new ColdjunctionChart(content, i + 1, 2, m, time));
            coldjunction3.add(new ColdjunctionChart(content, i + 1, 3, m, time));
            coldjunction4.add(new ColdjunctionChart(content, i + 1, 4, m, time));
            coldjunction5.add(new ColdjunctionChart(content, i + 1, 5, m, time));
            coldjunction6.add(new ColdjunctionChart(content, i + 1, 6, m, time));
        }

        thermopile = new ArrayList<>();
        for (int i = 0; i < SensorPos.TP.getFrequency(m); i++) {
            thermopile.add(new ThermopileChart(content, i + 1, m, time));
        }

        gps = new ArrayList<>();
        for (int i = 0; i < SensorPos.GPS.getFrequency(m); i++) {
            gps.add(new GPSData(content, i + 1, m, time));
        }
    }

    public String getOriginalContent() {
        return content;
    }

    @Override
    public String toString() {
        if (mcus == null) {
            return "";
        }
        String s = "";

        for (MCU mcu : mcus) {
            s += mcu.toString() + "\n";
        }

        Mode m = mcus.get(0).getMode();

        s += transceiver.toString() + "\n";
        s += batteryVoltage.toString() + "\n";
        s += sd_card.toString() + "\n";

        for (ChartSensor cs : digitalPressure1) {
            s += cs.toString() + "\n";
        }
        for (ChartSensor cs : digitalPressure2) {
            s += cs.toString() + "\n";
        }
        for (ChartSensor cs : analogPressure) {
            s += cs.toString() + "\n";
        }

        for (IMUData imu1 : imu) {
            s += imu1.toString() + "\n";
        }

        for (int i = 0; i < SensorPos.TC.getFrequency(m); i++) {
            s += thermocouple1.get(i).toString() + "\n";
            s += thermocouple2.get(i).toString() + "\n";
            s += thermocouple3.get(i).toString() + "\n";
            s += thermocouple4.get(i).toString() + "\n";
            s += thermocouple5.get(i).toString() + "\n";
            s += thermocouple6.get(i).toString() + "\n";
        }

        for (int i = 0; i < SensorPos.CJ.getFrequency(m); i++) {
            s += coldjunction1.get(i).toString() + "\n";
            s += coldjunction2.get(i).toString() + "\n";
            s += coldjunction3.get(i).toString() + "\n";
            s += coldjunction4.get(i).toString() + "\n";
            s += coldjunction5.get(i).toString() + "\n";
            s += coldjunction6.get(i).toString() + "\n";
        }

        for (ChartSensor tp1 : thermopile) {
            s += tp1.toString() + "\n";
        }

        for (GPSData g : gps) {
            s += g.toString() + "\n";
        }

        return s;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<MCU> getMcus() {
        return mcus;
    }

    public ArrayList<ChartSensor> getBatteryVoltage() {
        ArrayList<ChartSensor> a = new ArrayList<>();
        a.add(batteryVoltage);
        return a;
    }

    public TransceiverData getTransceiver() {
        return transceiver;
    }

    public SDHealth getSdCard() {
        return sd_card;
    }

    public ArrayList<ChartSensor> getDigitalPressure1() {
        return digitalPressure1;
    }

    public ArrayList<ChartSensor> getDigitalPressure2() {
        return digitalPressure2;
    }

    public ArrayList<ChartSensor> getAnalogPressure() {
        return analogPressure;
    }

    public ArrayList<IMUData> getImu() {
        return imu;
    }

    public ArrayList<ChartSensor> getThermocouple1() {
        return thermocouple1;
    }

    public ArrayList<ChartSensor> getThermocouple2() {
        return thermocouple2;
    }

    public ArrayList<ChartSensor> getThermocouple3() {
        return thermocouple3;
    }

    public ArrayList<ChartSensor> getThermocouple4() {
        return thermocouple4;
    }

    public ArrayList<ChartSensor> getThermocouple5() {
        return thermocouple5;
    }

    public ArrayList<ChartSensor> getThermocouple6() {
        return thermocouple6;
    }

    public ArrayList<ChartSensor> getColdjunction1() {
        return coldjunction1;
    }

    public ArrayList<ChartSensor> getColdjunction2() {
        return coldjunction2;
    }

    public ArrayList<ChartSensor> getColdjunction3() {
        return coldjunction3;
    }

    public ArrayList<ChartSensor> getColdjunction4() {
        return coldjunction4;
    }

    public ArrayList<ChartSensor> getColdjunction5() {
        return coldjunction5;
    }

    public ArrayList<ChartSensor> getColdjunction6() {
        return coldjunction6;
    }

    public ArrayList<ChartSensor> getThermopile() {
        return thermopile;
    }

    public ArrayList<GPSData> getGps() {
        return gps;
    }
    
}
