package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

import java.util.List;

public class Segment {

    private String name;

    private List<String> keywords;

    public Segment() {
    }

    public Segment(String name, List<String> keywords) {
        this.name = name;
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
