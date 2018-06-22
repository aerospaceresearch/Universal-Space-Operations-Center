package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.SensorDTO;
import com.ksatstuttgart.usoc.gui.setup.configuration.entity.data.VarDTO;

import java.util.Objects;

public class State implements UIEntity {

    private String keyword;

    private SensorDTO sensor;

    private VarDTO var;

    public State() {
    }

    public State(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }

    public VarDTO getVar() {
        return var;
    }

    public void setVar(VarDTO var) {
        this.var = var;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(keyword, state.keyword) &&
                Objects.equals(sensor, state.sensor) &&
                Objects.equals(var, state.var);
    }

    @Override
    public int hashCode() {

        return Objects.hash(keyword, sensor, var);
    }

    @Override
    public String toString() {
        return keyword;
    }
}
