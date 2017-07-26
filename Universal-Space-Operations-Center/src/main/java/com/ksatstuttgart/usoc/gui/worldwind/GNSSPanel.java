/*
 * The MIT License
 *
 * Copyright 2017 KSat Stuttgart e.V..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ksatstuttgart.usoc.gui.worldwind;

import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.data.message.dataPackage.SensorType;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.swing.JPanel;

/**
 *
 * @author valentinstarlinger
 */
public class GNSSPanel extends JPanel {

    WorldWindPanel wwp;

    RenderableLayer pathLayer;
    MarkerLayer markerLayer;

    private static final boolean OFFLINEMODE = true;

    public GNSSPanel() {

        boolean networkUnavailable = WorldWind.getNetworkStatus().isNetworkUnavailable();
        //if network is unavailable or usenetwork is false go into offline mode
        //true to use network false otherwise
        WorldWind.getNetworkStatus().setOfflineMode(OFFLINEMODE || networkUnavailable);

        pathLayer = new RenderableLayer();
        markerLayer = new MarkerLayer();

        this.setLayout(new BorderLayout(5, 5));
        wwp = new WorldWindPanel(null, 500, 500);
        this.add(wwp, BorderLayout.CENTER);

        // Create a Model for each window, starting with the Globe they share.
        Globe earth = new Earth();

        Model model = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);

        for (Layer layer : model.getLayers()) {
            if (layer.getValue(AVKey.IGNORE) != null) {
                continue;
            }
            //enable Bing Imagery for higher resolution imagery
            if (layer.getName().startsWith("Bing")) {
                layer.setEnabled(true);
            }
//            if (layer.getName().toLowerCase().contains("open")) {
//                layer.setEnabled(true);
//            }
        }

        model.getLayers().add(pathLayer);
        model.getLayers().add(markerLayer);
        model.setGlobe(earth);
        wwp.wwd.setModel(model);

        // Add view control layers, which the World Windows cannnot share.
        ViewControlsLayer viewControlsA = new ViewControlsLayer();
        wwp.wwd.getModel().getLayers().add(viewControlsA);
        wwp.wwd.addSelectListener(new ViewControlsSelectListener(wwp.wwd, viewControlsA));
    }

    public void updateData(MessageController controller) {

        ArrayList<Position> positions = new ArrayList<>();
        //get Sensor data for gps positions
        for (Sensor sensor : controller.getData().getSensors()) {
            //currently only supports one GNSS Sensor
            if (sensor.getType() == SensorType.GNSS) {
                TreeMap<Long, Object> altValues = null, lonValues = null, latValues = null;
                for (Var var : sensor.getVars()) {
                    //Prettify String values (non-static?)
                    switch (var.getDataName()) {
                        case "Altitude":
                            altValues = var.getValues();
                            break;
                        case "Longitude":
                            lonValues = var.getValues();
                            break;
                        case "Latitude":
                            latValues = var.getValues();
                            break;
                        default:
                            //ignore other values for now
                            break;
                    }
                }

                if (altValues == null || lonValues == null || latValues == null) {
                    //not a valid GNSS Sensor Type
                    //TODO: maybe throw exception here
                    continue;
                }

                
                for (Long timeStamp : altValues.keySet()) {
                    double lat = (double) latValues.get(timeStamp);
                    double lon = (double) lonValues.get(timeStamp);
                    double alt = (double) altValues.get(timeStamp);
                    Position p = Position.fromDegrees(lat, lon, alt+20);
                    if(!positions.contains(p)){
                        positions.add(p);
                    } else {
                    }
                }
            }
        }

        System.out.println("position list");
        for (Position position : positions) {
            System.out.println(position.toString());
        }
        System.out.println("position list end");

        // Create and set an attribute bundle
        ShapeAttributes shapeAttrs = new BasicShapeAttributes();
        shapeAttrs.setOutlineMaterial(Material.WHITE);
        shapeAttrs.setOutlineWidth(2d);

        // Create a path, set some of its properties and set its attributes.
        Path path = new Path(positions);
        path.setAttributes(shapeAttrs);
        path.setVisible(true);
        path.setAltitudeMode(WorldWind.ABSOLUTE);
        path.setPathType(AVKey.LINEAR);

        pathLayer.removeAllRenderables();
        pathLayer.addRenderable(path);

        BasicMarkerAttributes markerAttrs = new BasicMarkerAttributes(Material.RED,
                BasicMarkerShape.SPHERE, 1d);

        ArrayList<Marker> markers = new ArrayList<>();
        for (Position position : positions) {
            MyMarker m = new MyMarker(position, markerAttrs, (int) position.elevation);
            markers.add(m);
        }
        markerLayer.setMarkers(markers);

        //viewController.setObjectsToTrack(markerLayer.getMarkers());
        //viewController.sceneChanged();
        wwp.wwd.redraw();
    }
}
