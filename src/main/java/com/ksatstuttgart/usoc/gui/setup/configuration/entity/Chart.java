package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO Class that stores information about a User Interface
 * Entity (Chart)
 */
public class Chart implements UIEntity {

    /**
     * Chart Title
     */
    private String title;

    /**
     * X Label
     */
    private String xLabel;

    /**
     * Y Label
     */
    private String yLabel;

    /**
     * Holds all assigned values
     * <p>
     * Keys: Sensor Names
     * Values: Variable names in each sensor
     */
    private Map<String, List<String>> assignedData = new HashMap<>();

    /**
     * Default Constructor
     */
    public Chart() {

    }

    /**
     * Constructor with fields
     * @param title title
     * @param xLabel xLabel
     * @param yLabel yLabel
     */
    public Chart(String title, String xLabel, String yLabel) {
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
    }

    /**
     * Getter Method
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter method
     * @param title chart title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter Method
     * @return title
     */
    public String getxLabel() {
        return xLabel;
    }

    /**
     * Setter method
     * @param xLabel xLabel
     */
    public void setxLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    /**
     * Getter Method
     * @return yLabel
     */
    public String getyLabel() {
        return yLabel;
    }

    /**
     * Setter Method
     * @param yLabel yLabel
     */
    public void setyLabel(String yLabel) {
        this.yLabel = yLabel;
    }

    /**
     * Getter Method
     * @return Assigned Data Map
     */
    public Map<String, List<String>> getAssignedData() {
        return assignedData;
    }

    /**
     * Setter Method
     * @param assignedData assigned data
     */
    public void setAssignedData(Map<String, List<String>> assignedData) {
        this.assignedData = assignedData;
    }

    public void addVariable(String sensorName, String varName) {
        if (sensorName.isEmpty() || varName.isEmpty()) return;

        // If map doesn't contain sensor, adds sensor and var to the map
        if (!assignedData.containsKey(sensorName)) {
            assignedData.put(sensorName, new ArrayList<>(Arrays.asList(varName)));
            return;
        }

        List<String> varListInSensor = assignedData.get(sensorName);

        if (!varListInSensor.contains(varName)) {
            varListInSensor.add(varName);
        }
    }

    /**
     * Equals Method
     * @param o other object
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chart chart = (Chart) o;
        return Objects.equals(title, chart.title) &&
                Objects.equals(xLabel, chart.xLabel) &&
                Objects.equals(yLabel, chart.yLabel) &&
                Objects.equals(assignedData, chart.assignedData);
    }

    /**
     * HashCode
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, xLabel, yLabel, assignedData);
    }

    /**
     * ToString
     * @return object description
     */
    @Override
    public String toString() {
        return title;
    }
}
