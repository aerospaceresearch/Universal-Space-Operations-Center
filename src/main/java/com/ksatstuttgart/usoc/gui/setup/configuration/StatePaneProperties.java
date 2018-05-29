package com.ksatstuttgart.usoc.gui.setup.configuration;

import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;

import java.util.List;

public class StatePaneProperties {

    private boolean enabled;

    private List<Segment> segments;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
