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
public class ThermocoupleChart extends ChartSensor{
    
    private State state;
    
    private int temp;
    private int num,tpos, systime;
    private Mode m;

    public ThermocoupleChart(String content, int tpos, int num, Mode mode, int systime) {
        this.tpos = tpos;
        this.num = num;
        this.systime = systime;
        this.m = mode;
        parseData(content);
    }

    @Override
    public Double getTime() {
        return new Double((systime + (tpos - 1)*(12/SensorPos.TC.getFrequency(m))));
    }

    @Override
    public Double getYValue() {
        double b = temp;
        
        if(b < 0){
            b = ((-1)*b)-32767;
        }
        
        return b/10;
    }
    
    public void parseData(String content) {
        this.state = State.getState(Integer.parseUnsignedInt(content.substring(SensorPos.TC_HK.getStart(m)+(num-1), SensorPos.TC_HK.getEnd(m)+(num-1)),2));
        
        int diff = SensorPos.TC.getDiff(m);
        int timeDiff = diff * 6;
//        
//        System.out.println("!!!!Thermocouplewert bin: "+content.substring(SensorPos.TC.getStart(m)+(num-1)*diff+(tpos-1)*timeDiff
//                , SensorPos.TC.getEnd(m)+(num-1)*diff+(tpos-1)*timeDiff) + ", start: "+(SensorPos.TC.getStart(m)+(num-1)*diff+(tpos-1)*timeDiff)+" - end: "+(SensorPos.TC.getEnd(m)+(num-1)*diff+(tpos-1)*timeDiff));
//        
    
//        System.out.println("ttpos: "+tpos);
        
        this.temp = Utility.binToInt(content.substring(SensorPos.TC.getStart(m)+(num-1)*diff+(tpos-1)*timeDiff
                , SensorPos.TC.getEnd(m)+(num-1)*diff+(tpos-1)*timeDiff), false);
        
    }
    
    @Override
    public String toString(){
        return "Thermocouple "+num+": "+state+", temp: "+getYValue()+" at tpos: "+tpos;
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
