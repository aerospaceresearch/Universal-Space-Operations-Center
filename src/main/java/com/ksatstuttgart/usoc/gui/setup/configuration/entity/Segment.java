package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

import java.util.List;

public class Segment {

    private String name;

    private List<State> states;

    public Segment() {
    }

    public Segment(String name, List<State> states) {
        this.name = name;
        this.states = states;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
}
