package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.gui.setup.configuration.UIEntity;

import java.util.Objects;

public class State implements UIEntity {

    private String keyword;

    private Var var;

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

    public Var getVar() {
        return var;
    }

    public void setVar(Var var) {
        this.var = var;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(keyword, state.keyword) &&
                Objects.equals(var, state.var);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, var);
    }

    @Override
    public String toString() {
        return keyword;
    }
}
