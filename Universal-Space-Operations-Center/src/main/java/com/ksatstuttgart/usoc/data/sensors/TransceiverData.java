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
package com.ksatstuttgart.usoc.data.sensors;

import com.ksatstuttgart.usoc.data.sensors.chartData.ChartSensor;
import com.ksatstuttgart.usoc.data.sensors.chartData.TransceiverChart;
import java.util.ArrayList;

/**
 *
 * @author valentinstarlinger
 */
public class TransceiverData {
    
    private int signalstrength;
    private int alltimeHigh;
    private int alltimeLow;
    private int tries;
    private double systime;
    private Mode m;

    public TransceiverData(String content, Mode m, int systime){
        this.m = m;
        this.systime = systime;
        parseData(content);
    }

    private void parseData(String content) {
        
        String contentSS = content.substring(0, 16) + content.substring(24,32) + content.substring(40);
        
//        System.out.println(contentSS.substring(SensorPos.IRIDIUM_SIGNAL.getStart(m)-12,SensorPos.IRIDIUM_SIGNAL.getStart(m))+" - "+contentSS.substring(SensorPos.IRIDIUM_SIGNAL.getStart(m), SensorPos.IRIDIUM_SIGNAL.getEnd(m)));
//        System.out.println(contentSS.substring(SensorPos.IRIDIUM_HIGH.getStart(m), SensorPos.IRIDIUM_HIGH.getEnd(m)));
//        System.out.println(contentSS.substring(SensorPos.IRIDIUM_LOW.getStart(m), SensorPos.IRIDIUM_LOW.getEnd(m)));
//        System.out.println(contentSS.substring(SensorPos.IRIDIUM_TRIES.getStart(m), SensorPos.IRIDIUM_TRIES.getEnd(m)));
        
        this.signalstrength = Integer.parseUnsignedInt(contentSS.substring(SensorPos.IRIDIUM_SIGNAL.getStart(m), SensorPos.IRIDIUM_SIGNAL.getEnd(m)),2);
        this.alltimeHigh = Integer.parseUnsignedInt(contentSS.substring(SensorPos.IRIDIUM_HIGH.getStart(m), SensorPos.IRIDIUM_HIGH.getEnd(m)),2);
        this.alltimeLow = Integer.parseUnsignedInt(contentSS.substring(SensorPos.IRIDIUM_LOW.getStart(m), SensorPos.IRIDIUM_LOW.getEnd(m)),2);
        this.tries = Integer.parseUnsignedInt(contentSS.substring(SensorPos.IRIDIUM_TRIES.getStart(m), SensorPos.IRIDIUM_TRIES.getEnd(m)),2);
    }

    @Override
    public String toString() {
        return "Iridium: Signalstrength: " + signalstrength + " High: " + alltimeHigh + " Low: " + alltimeLow + " Tries: " + tries;
    }

    public ArrayList<ChartSensor> getSignalstrength() {
        ArrayList<ChartSensor> a = new ArrayList<>();
        a.add(new TransceiverChart(systime,signalstrength,State.OK));
        return a;
    }

    public ArrayList<ChartSensor> getAlltimeHigh() {
        ArrayList<ChartSensor> a = new ArrayList<>();
        a.add(new TransceiverChart(systime,alltimeHigh,State.OK));
        return a;
    }

    public ArrayList<ChartSensor> getAlltimeLow() {
        ArrayList<ChartSensor> a = new ArrayList<>();
        a.add(new TransceiverChart(systime,alltimeLow,State.OK));
        return a;
    }

    public ArrayList<ChartSensor> getTries() {
        ArrayList<ChartSensor> a = new ArrayList<>();
        a.add(new TransceiverChart(systime,tries,State.OK));
        return a;
    }    
}
