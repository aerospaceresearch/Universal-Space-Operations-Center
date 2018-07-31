package com.ksatstuttgart.usoc.gui.setup.configuration;

import com.ksatstuttgart.usoc.gui.setup.configuration.entity.Segment;

import java.util.List;

/**
 * POJO Class used to hold all layout information
 * related to the State Pane
 *
 * @author Pedro Portela (Pedro12909)
 */
public class StatePaneProperties {

    /**
     * Should the State Pane be enabled
     */
    private boolean enabled;

    /**
     * List of Segments shown
     */
    private List<Segment> segments;

    /**
     * Finds a Segment with a given title
     *
     * @param title title
     * @return found Segment. null if none were found
     */
    public Segment findSegmentByTitle(String title) {
        for (Segment segment : segments) {
            if (segment.getName().equals(title)) return segment;
        }

        return null;
    }

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
