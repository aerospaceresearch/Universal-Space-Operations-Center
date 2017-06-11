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
import com.ksatstuttgart.usoc.controller.Utility;

/**
 *
 * @author valentinstarlinger
 */
public class ThermopileChart extends ChartSensor{
    
    public State state;
    
    public int temp;
    private int tpos, systime;
    private Mode m;

    public ThermopileChart(String content, int tpos, Mode mode, int systime) {
        this.tpos = tpos;
        this.systime = systime;
        this.m = mode;
        parseData(content);
    }
    
    @Override
    public Double getTime() {
        return new Double((systime + (tpos - 1)*(12/SensorPos.TP.getFrequency(m))));
    }

    @Override
    public Double getYValue() {
        return new Double(temp);
    }
    
    public void parseData(String content) {
        this.state = State.getState(Integer.parseUnsignedInt(content.substring(SensorPos.TP_HK.getStart(m), SensorPos.TP_HK.getEnd(m)),2));
        
        int diff = SensorPos.TP.getDiff(m);
        
        this.temp = Utility.binToInt(content.substring(SensorPos.TP.getStart(m)+(tpos-1)*diff
                , SensorPos.TP.getEnd(m)+(tpos-1)*diff), false);
        
    }
    
    @Override
    public String toString(){
        return "Thermopile: "+state+", temp: "+getYValue()+" at tpos: "+tpos;
    }
    
    public State getState() {
        return state;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
    
    
    
}
