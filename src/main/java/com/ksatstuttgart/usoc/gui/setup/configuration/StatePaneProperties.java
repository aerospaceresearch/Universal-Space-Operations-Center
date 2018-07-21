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

    public Segment findSegmentByTitle(String title) {
        for (Segment segment : segments) {
            if (segment.getName().equals(title)) return segment;
        }

        return null;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
