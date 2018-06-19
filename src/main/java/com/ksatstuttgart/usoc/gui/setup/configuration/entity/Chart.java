package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.gui.setup.configuration.UIEntity;

import java.util.List;
import java.util.Objects;

public class Chart implements UIEntity {

    private String title;

    private String xLabel;

    private String yLabel;

    private List<Var> variables;

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

    public List<Var> getVariables() {
        return variables;
    }

    public void setVariables(List<Var> variables) {
        this.variables = variables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chart chart = (Chart) o;
        return Objects.equals(title, chart.title) &&
                Objects.equals(xLabel, chart.xLabel) &&
                Objects.equals(yLabel, chart.yLabel) &&
                Objects.equals(variables, chart.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, xLabel, yLabel, variables);
    }

    @Override
    public String toString() {
        return title;
    }
}
