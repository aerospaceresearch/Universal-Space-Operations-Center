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
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.UIEntity;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.VarDTO;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This class represents a data point for an arbitrary sensor.
 *
 * @author valentinstarlinger
 */
public class Var implements Cloneable, UIEntity {
    private String dataName;
    private DataType dataType;
    private int startPosition;
    private int numPoints;
    private double frequency;
    private boolean isLittleEndian;
    private String unit;

    private TreeMap<Long, Object> values = new TreeMap<>();

    public Var() {
    }

    public Var(Var var) {
        this.dataName = (var.getDataName());
        this.dataType = (var.getDataType());
        this.frequency = (var.getFrequency());
        this.numPoints = (var.getNumPoints());
        this.startPosition = (var.getStartPosition());
        this.unit = (var.getUnit());
        this.isLittleEndian = var.isLittleEndian();

        for (Long key : var.getValues().keySet()) {
            this.values.put(key, var.getValues().get(key));
        }
    }

    public void addValue(long time, Object value) {
        values.put(time, value);
    }

    public TreeMap<Long, Object> getValues() {
        return values;
    }

    @XmlAttribute(name = "dataname")
    public String getDataName() {
        if (dataName == null) return "";
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    @XmlAttribute(name = "datatype")
    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    @XmlAttribute
    public String getUnit() {
        if (unit == null) return "";
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @XmlAttribute(name = "start")
    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    @XmlAttribute(name = "numpoints")
    public int getNumPoints() {
        //return number of numpoints. miminum numPoints is 1
        return numPoints == 0 ? 1 : numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    @XmlAttribute
    public double getFrequency() {
        //if frequency is zero but is required, this will default to 1Hz
        return frequency == 0 ? 1 : frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    @XmlAttribute(name = "isLittleEndian")
    public boolean isLittleEndian() {
        return isLittleEndian;
    }

    public void setIsLittleEndian(boolean isLittleEndian) {
        this.isLittleEndian = isLittleEndian;
    }

    @Override
    public String toString() {
        String s = this.getDataName();
        for (Long time : this.getValues().keySet()) {
            s+="\n"+time+": "+this.getValues().get(time);
        }
        return s;
    }

    /**
     * ToString method with more textual information
     * @return
     */
    public String toStringVerbose() {
        return "Data Type = " + dataType +
                "\nStart Position = " + startPosition +
                "\nNum Points = " + numPoints +
                "\nFrequency = " + frequency +
                "\nLittle Endian = " + isLittleEndian +
                "\nUnit = '" + unit + '\'' +
                "\nValues = " + values;
    }

    /*
     * AUTOGENERATED hashCode and equals functions
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.dataName);
        hash = 53 * hash + Objects.hashCode(this.dataType);
        hash = 53 * hash + this.startPosition;
        hash = 53 * hash + this.numPoints;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.frequency) ^ (Double.doubleToLongBits(this.frequency) >>> 32));
        hash = 53 * hash + (this.isLittleEndian ? 1 : 0);
        hash = 53 * hash + Objects.hashCode(this.unit);
        hash = 53 * hash + Objects.hashCode(this.values);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Var other = (Var) obj;
        if (this.getStartPosition() != other.getStartPosition()) {
            return false;
        }
        if (this.getNumPoints() != other.getNumPoints()) {
            return false;
        }
        if (Double.doubleToLongBits(this.getFrequency()) != Double.doubleToLongBits(other.getFrequency())) {
            return false;
        }
        if (this.isLittleEndian() != other.isLittleEndian()) {
            return false;
        }
        if (!Objects.equals(this.getDataName(), other.getDataName())) {
            return false;
        }
        if (!Objects.equals(this.getUnit(), other.getUnit())) {
            return false;
        }
        if (this.getDataType() != other.getDataType()) {
            return false;
        }
        return Objects.equals(this.getValues(), other.getValues());
    }

    public VarDTO toDTO() {
        return new VarDTO(dataName, dataType.name());
    }
}
