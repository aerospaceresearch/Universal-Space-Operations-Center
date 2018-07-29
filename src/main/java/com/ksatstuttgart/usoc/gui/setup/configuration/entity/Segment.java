package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Segment implements UIEntity {

    private String name;

    private List<State> states;

    public Segment() {
        states = new ArrayList<>();
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

    public State findStateByKeyword(String keyword) {
        for (State state :
                states) {
            if (state.getKeyword().equals(keyword)) return state;
        }

        return null;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Objects.equals(name, segment.name) &&
                Objects.equals(states, segment.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, states);
    }
}
