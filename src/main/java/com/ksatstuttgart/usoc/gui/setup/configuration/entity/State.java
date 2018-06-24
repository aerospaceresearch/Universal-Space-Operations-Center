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
