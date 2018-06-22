package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.SensorDTO;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.VarDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chart implements UIEntity {

    private String title;

    private String xLabel;

    private String yLabel;

    private List<SensorDTO> sensors = new ArrayList<>();

    public Chart() {

    }

    public Chart(String title, String xLabel, String yLabel) {
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getxLabel() {
        return xLabel;
    }

    public void setxLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    public String getyLabel() {
        return yLabel;
    }

    public void setyLabel(String yLabel) {
        this.yLabel = yLabel;
    }

    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }

    public void addVariable(SensorDTO sensor) {
        if (!sensors.contains(sensor)) {
            sensors.add(sensor);
            return;
        }

        for (SensorDTO sensorDTO : sensors) {
            if (sensorDTO.equals(sensor)) {
                for (VarDTO sensorVar : sensorDTO.getVariables()) {
                    if (!sensorDTO.getVariables().contains(sensorVar)) {
                        sensorDTO.getVariables().add(sensorVar);
                    }

                    return;
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chart chart = (Chart) o;
        return Objects.equals(title, chart.title) &&
                Objects.equals(xLabel, chart.xLabel) &&
                Objects.equals(yLabel, chart.yLabel) &&
                Objects.equals(sensors, chart.sensors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, xLabel, yLabel, sensors);
    }

    @Override
    public String toString() {
        return title;
    }
}
