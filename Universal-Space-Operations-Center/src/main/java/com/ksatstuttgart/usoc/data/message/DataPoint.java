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
package com.ksatstuttgart.usoc.data.message;

import com.ksatstuttgart.usoc.data.message.dataPackage.DataType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * This class represents a data point for an arbitrary sensor. 
 * 
 * @author valentinstarlinger
 */
public class DataPoint {
    private String dataName;
    private DataType dataType;
    private Object value;
    private int startPosition;
    private int numPoints;
    private double frequency;
    private String unit;

    public DataPoint(String dataName, DataType dataType, Object value, int startPosition, int numPoints, double frequency, String unit) {
        this.dataName = dataName;
        this.dataType = dataType;
        this.value = value;
        this.startPosition = startPosition;
        this.numPoints = numPoints;
        this.frequency = frequency;
        this.unit = unit;
    }
    
    public DataPoint(){
        
    }

    @XmlAttribute
    public String getDataName() {
        if(dataName == null) return "";
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
    
    @XmlAttribute
    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    @XmlAttribute
    public String getUnit() {
        if(unit == null) return "";
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @XmlAttribute
    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    @XmlAttribute
    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    @XmlAttribute
    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DataPoint) {
            DataPoint dp = (DataPoint) o;
            return (dp.getDataName().equals(this.getDataName())
                    && dp.getFrequency() == this.frequency
                    && dp.getNumPoints() == this.numPoints
                    && dp.getStartPosition() == this.startPosition
                    && dp.getUnit().equals(this.getUnit())
                    && dp.getDataType() == this.dataType);
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return "Name: "+this.getDataName()+"\nType: "+this.getDataType()
                +"\nStartPosition: "+this.getStartPosition()+"\nNumPoints: "+this.getNumPoints()
                +"\nFrequency: "+this.getFrequency()+"\nUnit: "+this.getUnit();
    }
}
