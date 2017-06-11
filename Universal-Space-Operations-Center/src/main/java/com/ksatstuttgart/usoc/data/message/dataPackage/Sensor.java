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

import com.ksatstuttgart.usoc.data.message.DataPoint;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author valentinstarlinger
 */
public class Sensor {
    private SensorType type;
    private String sensorName;
    private ArrayList<DataPoint> datapoints;

    public Sensor() {
    }
    
    public Sensor(Sensor s){
        this.type = s.getType();
        this.sensorName = s.getSensorName();
        
        datapoints = new ArrayList<>();
        for (DataPoint datapoint : s.getDatapoints()) {
            datapoints.add(new DataPoint(datapoint));
        }
    }

    @XmlAttribute ( name = "sensortype" )
    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    @XmlAttribute ( name = "sensorname" )
    public String getSensorName() {
        if(sensorName==null) return "";
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    @XmlElement ( name = "datapoint" )
    public ArrayList<DataPoint> getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(ArrayList<DataPoint> datapoints) {
        this.datapoints = datapoints;
    }

    public void addDataPoint(DataPoint dataPoint) {
        if(datapoints == null){
            datapoints = new ArrayList<>();
        }
        datapoints.add(dataPoint);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Sensor) {
            Sensor s = (Sensor) o;
            return (s.getDatapoints().equals(this.getDatapoints())
                    && s.getSensorName().equals(this.getSensorName())
                    && s.getType() == this.type);
        } else {
            return false;
        }    
    }
    
    
    @Override
    public String toString(){
        String s = "Name: "+this.getSensorName()+"\nType: "+this.type+"\nDatapoints:\n";
        for (DataPoint datapoint : datapoints) {
            s+=datapoint.toString();
        }
        return s;
    }
}
