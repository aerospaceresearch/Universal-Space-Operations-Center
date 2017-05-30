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
public class PressureChart extends ChartSensor{
    
    private State state;
    private int pressure;
    private int tpos, systime;
    private int type;
    private Mode m;
    

    public PressureChart(String content, int tpos, int type, Mode m, int systime) {
        this.tpos = tpos;
        this.type = type;
        this.m = m;
        this.systime = systime;
        parseData(content);
    }
    
    @Override
    public Double getTime() {
        return new Double((systime + (tpos - 1)*(12/SensorPos.PS.getFrequency(m))));
    }

    @Override
    public Double getYValue() {
        if(type==3){
            double vout = pressure * 4;
            return (((vout/1024)+0.095)/0.009);
        }else{
            double d = pressure/10;
            return d*0.1;
        }
    }

    @Override
    public State getState() {
        return state;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }
    
    public void parseData(String content) {
        
        this.state = State.getState(Integer.parseUnsignedInt(content.substring(SensorPos.PS_HK.getStart(m)+(type-1), SensorPos.PS_HK.getEnd(m)+(type-1)),2));
        //tauschen für spi -> endian
        if(type == 3){
            this.pressure = Integer.parseUnsignedInt(content.substring(SensorPos.AP.getStart(m)+(tpos-1)*8
                    , SensorPos.AP.getEnd(m)+(tpos-1)*8),2);
        } else {
            this.pressure = Integer.parseUnsignedInt(content.substring(SensorPos.PS.getStart(m)+(tpos-1)*16
                    , SensorPos.PS.getEnd(m)+(tpos-1)*16),2);
        }
        
    }
    
    @Override
    public String toString(){
        return "Pressure Sensor "+type+": "+state+", pressure: "+getYValue()+" at tpos: "+tpos;
    }
    
}
