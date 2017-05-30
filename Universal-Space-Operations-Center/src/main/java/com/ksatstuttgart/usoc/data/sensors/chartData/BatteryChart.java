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
package com.ksatstuttgart.usoc.data.sensors.chartData;

import com.ksatstuttgart.usoc.data.sensors.Mode;
import com.ksatstuttgart.usoc.data.sensors.SensorPos;
import com.ksatstuttgart.usoc.data.sensors.State;

/**
 *
 * @author valentinstarlinger
 */
public class BatteryChart extends ChartSensor {

    private State state;
    private final int systime;
    private int voltage;
    private final Mode m;
    
    public BatteryChart(String content, Mode m, int systime) {
        this.m = m;
        this.systime = systime;
        parseData(content);
    }

    @Override
    public Double getTime() {
        return new Double(systime);
    }

    @Override
    public Double getYValue() {
        return getVoltage();
    }

    public State getState() {
        return state;
    }

    public double getVoltage() {
        double i = voltage * 4;
        return (i/1024)*9.9;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public void parseData(String content) {

        this.state = State.getState(Integer.parseUnsignedInt(content.substring(SensorPos.VB_HK.getStart(m), SensorPos.VB_HK.getEnd(m)), 2));
        this.voltage = Integer.parseUnsignedInt(content.substring(SensorPos.VB.getStart(m), SensorPos.VB.getEnd(m)), 2);

    }

    @Override
    public String toString() {
        return "Battery state: " + state + ", Voltagelevel: " + getVoltage();
    }

}
