package com.ksatstuttgart.usoc.gui.setup.configuration.entity.data;

import com.ksatstuttgart.usoc.gui.setup.configuration.entity.UIEntity;

import java.util.List;
import java.util.Objects;

public class SensorDTO implements UIEntity {

    private String sensorName;

    private List<VarDTO> variables;

    public SensorDTO(String sensorName, List<VarDTO> variables) {
        this.sensorName = sensorName;
        this.variables = variables;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public List<VarDTO> getVariables() {
        return variables;
    }

    public void setVariables(List<VarDTO> variables) {
        this.variables = variables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorDTO sensorDTO = (SensorDTO) o;
        return Objects.equals(sensorName, sensorDTO.sensorName) &&
                Objects.equals(variables, sensorDTO.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorName, variables);
    }

    @Override
    public String toString() {
        return sensorName;
    }
}
