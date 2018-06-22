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

import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.UIEntity;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.SensorDTO;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.VarDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author valentinstarlinger
 */
public class Sensor implements UIEntity {

    /**
     * SensorDTO Type
     */
    private SensorType type;

    /**
     * SensorDTO Name
     */
    private String sensorName;

    /**
     * List of Variables for this SensorDTO
     */
    private ArrayList<Var> vars;

    /**
     * Number of Points
     */
    private int numPoints;

    /**
     * Frequency
     */
    private double frequency;

    /**
     * Creates a new SensorDTO with no information and
     * an empty variable list
     */
    public Sensor() {
        vars = new ArrayList<>();
    }

    /**
     * Clones another SensorDTO Object
     * @param s other SensorDTO
     */
    public Sensor(Sensor s) {
        this.type = s.getType();
        this.sensorName = s.getSensorName();
        this.numPoints = s.getNumPoints();
        this.frequency = s.getFrequency();

        vars = new ArrayList<>();
        for (Var variable : s.getVars()) {
            vars.add(new Var(variable));
        }
    }

    /**
     * Getter Method
     * @return SensorDTO Type
     */
    @XmlAttribute(name = "type")
    public SensorType getType() {
        return type;
    }

    /**
     * Setter Method
     * @param type SensorDTO Type
     */
    public void setType(SensorType type) {
        this.type = type;
    }

    @XmlAttribute(name = "name")
    public String getSensorName() {
        if (sensorName == null) {
            return "";
        }
        return sensorName;
    }

    /**
     * Setter Method
     * @param sensorName SensorDTO Name
     */
    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    @XmlElement(name = "var")
    public ArrayList<Var> getVars() {
        return vars;
    }

    /**
     * Setter Method
     * @param datapoints variables/data points
     */
    public void setVariables(ArrayList<Var> datapoints) {
        this.vars = datapoints;
    }

    /**
     * Adds a Variable to the List
     * @param dataPoint data point/variable
     */
    public void addVariable(Var dataPoint) {
        vars.add(dataPoint);
    }

    /**
     * Getter Method
     * Returns num points.
     * Minimum is 1
     *
     * @return numPoints
     */
    @XmlAttribute (name = "sensorpoints")
    public int getNumPoints() {
        return numPoints == 0 ? 1 : numPoints;
    }

    /**
     * Setter Method
     * @param numPoints numPoints
     */
    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    /**
     * Getter Method
     * @return frequency
     */
    @XmlAttribute (name = "sensorfrequency")
    public double getFrequency() {
        //if frequency is zero but is required, this will default to 1Hz
        return frequency == 0 ? 1 : frequency;
    }

    /**
     * Setter method
     * @param frequency frequency
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }


    /**
     * Finds a Variable, given its name
     * @param name name of the variable
     * @return found variable. null if it didnt find it
     */
    public Var getVarByName(String name) {
        for (Var v :
                vars) {
            if (v.getDataName().equals(name)) {
                return v;
            }
        }

        return null;
    }



    /**
     * Calculates total data length
     * @return data length
     */
    public int getTotalDataLength(){
        int length = 0;
        for (Var var : vars) {
            length += var.getDataType().getLength();
        }
        return length;
    }

    /**
     * Textual description of the current Object
     * @return toString
     */
    @Override
    public String toString() {
        String s = "Sensor name: " + this.getSensorName() + " (Type: " + this.type + ")\n";
        for (Var var : this.getVars()) {
            s += "\t" + var.toString() + "\n";
        }
        return s;
    }

    /**
     * Calculates Object HashCode
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.type);
        hash = 71 * hash + Objects.hashCode(this.sensorName);
        hash = 71 * hash + Objects.hashCode(this.vars);
        return hash;
    }

    /**
     * Equals Method
     * @param obj other object
     * @return true if both objcts are equal
     */
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
        final Sensor other = (Sensor) obj;
        if (!Objects.equals(this.getSensorName(), other.getSensorName())) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        return Objects.equals(this.getVars(), other.getVars());
    }

    /**
     * Creates a Data Transfer Object that
     * stores this Object's Data
     * @return SensorDTO
     */
    public SensorDTO toDTO() {
        List<VarDTO> varDTOList = new ArrayList<>();

        for (Var v :
                getVars()) {
            varDTOList.add(v.toDTO());
        }

        return new SensorDTO(sensorName, varDTOList);
    }
}
