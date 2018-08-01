/*
 * MIT License
 *
 * Copyright (c) 2017 KSat e.V. and AerospaceResearch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

/**
 * Represents a State, with a keyword
 */
public class State implements UIEntity {

    /**
     * Keyword
     */
    private String keyword;

    /**
     * Assigned Sensor Name
     */
    private String sensorName;

    /**
     * Assigned Variable/Data Point Name
     */
    private String varName;

    /**
     * Empty Constructor
     */
    public State() {
    }

    /**
     * Constructor with keyword
     * @param keyword keyword
     */
    public State(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Getter method
     * @return keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Setter method
     * @param keyword keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Getter method
     * @return sensor name
     */
    public String getSensorName() {
        return sensorName;
    }
    /**
     * Setter method
     * @param sensorName sensor name
     */
    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    /**
     * Getter method
     * @return assigned variable name
     */
    public String getVarName() {
        return varName;
    }

    /**
     * Setter method
     * @param varName assigned variable name
     */
    public void setVarName(String varName) {
        this.varName = varName;
    }

    /**
     * Adds a variable to this State, given a variable and its sensor
     *
     * @param sensorName sensor name
     * @param varName variable name
     */
    public void addVariable(String sensorName, String varName) {
        setSensorName(sensorName);
        setVarName(varName);
    }

    /**
     * ToString
     * @return Object Description
     */
    @Override
    public String toString() {
        return keyword;
    }
}
