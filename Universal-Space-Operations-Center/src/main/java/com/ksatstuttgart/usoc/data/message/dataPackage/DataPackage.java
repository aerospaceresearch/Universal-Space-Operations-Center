/*
 * The MIT License
 *
 * Copyright 2017 KSat Stuttgart e.V..
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
package com.ksatstuttgart.usoc.data.message.dataPackage;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author valentinstarlinger
 */

public class DataPackage {
    
    private ArrayList<Sensor> sensors;
    
    public DataPackage(){
        
    }
    
    @XmlElement ( name = "sensor" )
    public ArrayList<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(ArrayList<Sensor> sensors) {
        this.sensors = sensors;
    }
    
    public void addSensor(Sensor sensor) {
       if(sensors == null){
            sensors = new ArrayList<>();
        }
        sensors.add(sensor);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DataPackage) {
            DataPackage dp = (DataPackage) o;
            return (dp.getSensors().equals(this.sensors));
        } else {
            return false;
        }
    }
    
    @Override
    public String toString(){
        String s = "";
        for (Sensor sensor : sensors) {
            s+=sensor.toString()+"\n";
        }
        return s;
    }
}
