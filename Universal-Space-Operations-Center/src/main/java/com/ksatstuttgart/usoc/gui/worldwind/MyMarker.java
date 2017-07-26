package com.ksatstuttgart.usoc.gui.worldwind;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;

public class MyMarker extends BasicMarker
{
    protected int id;

    public MyMarker(Position position, MarkerAttributes attrs, int id)
    {
        super(position, attrs);
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
}