package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

public class State {

    private String keyword;

    private String sensorName;

    private String varName;

    public State() {
    }

    public State(String keyword, String sensorName, String varName) {
        this.keyword = keyword;
        this.sensorName = sensorName;
        this.varName = varName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    @Override
    public String toString() {
        return keyword;
    }
}
