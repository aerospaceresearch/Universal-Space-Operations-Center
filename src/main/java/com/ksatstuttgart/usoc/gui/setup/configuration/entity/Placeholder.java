package com.ksatstuttgart.usoc.gui.setup.configuration.entity;

import com.ksatstuttgart.usoc.gui.setup.configuration.UIEntity;

/**
 * A Dummy class used to represent placeholder values
 * in a tree
 *
 * eg.: "Root" or "Charts"
 */
public class Placeholder implements UIEntity {

    private String placeholder;

    public Placeholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String toString() {
        return placeholder;
    }
}
