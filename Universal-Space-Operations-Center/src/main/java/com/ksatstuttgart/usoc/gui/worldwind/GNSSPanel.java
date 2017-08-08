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
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.SerialEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.data.message.dataPackage.SensorType;
import com.ksatstuttgart.usoc.gui.DataPanel;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Matrix;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.EarthFlat;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.globes.projections.ProjectionMercator;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.terrain.ZeroElevationModel;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.view.ViewUtil;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author valentinstarlinger
 */
public class GNSSPanel extends DataPanel {

    WorldWindowGLCanvas wwp_big, wwp_topView, wwp_sideView;
    BasicOrbitView bov_big, bov_topView, bov_sideView;
    
    StatusBar statusBar;

    RenderableLayer pathLayer;
    MarkerLayer markerLayer;

    JPanel sidePanel;

    private static final boolean OFFLINEMODE = true;

    public GNSSPanel() {
        super();
        
        boolean networkUnavailable = WorldWind.getNetworkStatus().isNetworkUnavailable();
        //if network is unavailable or usenetwork is false go into offline mode
        //true to use network false otherwise
        WorldWind.getNetworkStatus().setOfflineMode(OFFLINEMODE || networkUnavailable);

        pathLayer = new RenderableLayer();
        markerLayer = new MarkerLayer();

        wwp_big = new WorldWindowGLCanvas();
        wwp_topView = new WorldWindowGLCanvas();
        wwp_sideView = new WorldWindowGLCanvas();

        JLayeredPane jlp = new JLayeredPane();

        sidePanel = new JPanel();
        sidePanel.setOpaque(true);
        sidePanel.setSize(200, 200);
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.add(wwp_topView);
        sidePanel.add(wwp_sideView);

        jlp.add(wwp_big, JLayeredPane.MODAL_LAYER);
        jlp.add(sidePanel, JLayeredPane.DEFAULT_LAYER);

        wwp_big.setBounds(0, 0, 600, 600);
        sidePanel.setBounds(400, 0, 200, 200);

        this.setLayout(new BorderLayout());
        this.add(jlp);
        statusBar = new StatusBar();
        statusBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        statusBar.setEventSource(wwp_big);
        this.add(statusBar, BorderLayout.SOUTH);

        Model bigModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        Model topViewModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        Model sideViewModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);

        // Create a Model for each window, starting with the Globe they share.
        Globe earth = new Earth();
        Globe flatEarth = new EarthFlat();
        flatEarth.setElevationModel(new ZeroElevationModel());
        ((EarthFlat) flatEarth).setProjection(new ProjectionMercator());

        activateLayers(bigModel, earth, true);
        activateLayers(topViewModel, flatEarth, false);
        activateLayers(sideViewModel, earth, false);

        wwp_big.setModel(bigModel);
        wwp_sideView.setModel(sideViewModel);
        wwp_topView.setModel(topViewModel);

        //create new balloon controller with current model
        // Add view control layers, which the World Windows cannnot share.
        ViewControlsLayer viewControlsA = new ViewControlsLayer();
        wwp_big.getModel().getLayers().add(viewControlsA);
        wwp_big.addSelectListener(new ViewControlsSelectListener(wwp_big, viewControlsA));

        //setting basicorbitviews for all the world windows to be able to properly
        //animate transitions
        bov_big = new BasicOrbitView();
        bov_big.setGlobe(earth);
        wwp_big.setView(bov_big);
        bov_sideView = new BasicOrbitView();
        bov_sideView.setPitch(Angle.POS90);
        bov_sideView.setGlobe(earth);
        wwp_sideView.setView(bov_sideView);
        bov_topView = new BasicOrbitView();
        bov_topView.setGlobe(flatEarth);
        wwp_topView.setView(bov_topView);
    }

    /**
     * This method overrides the default repaint method to resize the
     * layeredPane with every repaint
     */
    @Override
    public void repaint() {
        //with every repaint - resize the wwcanvases to fit the panel
        this.resizeComponents();
        //call the super repaint function (not overwritten)
        super.repaint();
    }

    /**
     * This method resizes the components of the GNSSPanel. It is especially
     * used to resize the components of the layeredPane.
     */
    public void resizeComponents() {
        if (wwp_big == null || sidePanel == null) {
            //if either of this happens - do not resize
            return;
        }

        //getting the width and height for the big world window
        int width = this.getWidth();
        int height = this.getHeight() - statusBar.getHeight();
        wwp_big.setBounds(0, 0, width, height);
        //setting the height and width of the sidePanel to a third of the big window
        int spHeight = height / 3;
        int spWidth = width / 3;
        //setting the sidePanel in the upper right corner with the respective width 
        //and height
        sidePanel.setBounds(width - spWidth, 0, spWidth, spHeight);
    }

    /**
     * This method updates the 3D view with the new GPS data
     *
     * It zooms to the last received data on the central panel and shows the
     * data for the whole flight in the panels on the top right for top and side
     * view
     *
     * @param controller - MessageController with the updated data
     * @param e - USOCEvent
     */
    @Override
    public void updateData(MessageController controller, USOCEvent e) {

        //only update for iridium messages at the moment/ignore error and serial 
        //events
        if(e instanceof SerialEvent|| e instanceof ErrorEvent){
            return;
        }
        
        double highestGlobalAlt = 0, highestLastAlt = 0;
        int numPoints = 0;

        ArrayList<Position> positions = new ArrayList<>();
        //get Sensor data for gps positions
        for (Sensor sensor : controller.getData().getSensors()) {
            //currently only supports one GNSS Sensor
            if (sensor.getType() == SensorType.GNSS) {
                //set the number of position per message
                numPoints = sensor.getNumPoints();

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
                    Position p = Position.fromDegrees(lat, lon, alt + 1);
                    if (!positions.contains(p)) {
                        positions.add(p);
                    }
                }
            }
            for (int i = 0; i < positions.size(); i++) {
                if (highestGlobalAlt < positions.get(i).getAltitude()) {
                    highestGlobalAlt = positions.get(i).getAltitude();
                }
                if ((positions.size() - i) < numPoints) {
                    if (highestLastAlt < positions.get(i).getAltitude()) {
                        highestLastAlt = positions.get(i).getAltitude();
                    }
                }
            }

        }

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

        //remove all old paths and set the new one
        //TODO: this can probably be done in a prettier way.
        pathLayer.removeAllRenderables();
        pathLayer.addRenderable(path);

        //setting the markers to red spheres with no transperency with a minimum
        //size of 0.5 meters and no maximum size and 4 pixels per marker
        BasicMarkerAttributes markerAttrs = new BasicMarkerAttributes(Material.RED,
                BasicMarkerShape.SPHERE, 1d, 4, 0.5d, Double.MAX_VALUE);

        ArrayList<Marker> markers = new ArrayList<>();
        for (Position position : positions) {
            MyMarker m = new MyMarker(position, markerAttrs, (int) position.elevation);
            markers.add(m);
        }

        //creating a new 
        ArrayList<Marker> lastMarker = new ArrayList<>();
        int c = numPoints > positions.size() ? positions.size() : numPoints;
        for (int i = c; i > 0; i--) {
            lastMarker.add(markers.get(markers.size() - i));
        }

        markerLayer.setMarkers(markers);

        Position centerPos = positions.get(positions.size() - 1);
        updateViewerPerspective(bov_big, centerPos, lastMarker, highestLastAlt + 100, false);
        updateViewerPerspective(bov_topView, centerPos, markers, highestGlobalAlt + 1000, false);
        updateViewerPerspective(bov_sideView, centerPos, markers, 0.1, true);

        wwp_big.redraw();
        wwp_sideView.redraw();
        wwp_topView.redraw();
    }

    /**
     * This method updates the views for the BasicOrbitView to show all the
     * positions that shall be visible. This method animates both transitions
     * (new center position and new zoom level) using the BasicOrbitView
     * animator methods.
     *
     * @param bov - BasicOrbitView: The view that should be updated
     * @param centerPos - Position: Center position of the view
     * @param markers - ArrayList<Marker>: List of markers that should be
     * visible
     * @param zoomOffset - double: Offset that should be applied to the zoom
     * @param sideView - boolean: if true zoomOffset is used to reduce the
     * latitude value
     */
    private void updateViewerPerspective(BasicOrbitView bov, Position centerPos,
            ArrayList<Marker> markers, double zoomOffset, boolean sideView) {
        Position newPos = centerPos;
        //this is used to set the latitude a bit lower for the side view so that
        //the markers are still visible and not clipped if they are too close
        //together
        if (sideView) {
            newPos = Position.fromDegrees(newPos.latitude.degrees - zoomOffset,
                    newPos.longitude.degrees, newPos.elevation);
        }
        //animating to the new center position
        bov.addCenterAnimator(bov.getCenterPosition(), newPos, true);
        //Calculating and animating the zoom
        //zoom is the current altitude of the viewer in meters
        bov.addZoomAnimator(bov.getZoom(), zoomOffset + computeZoomLevel(bov.getCurrentEyePoint(),
                bov.getGlobe().computePointFromPosition(centerPos),
                bov.getUpVector(), bov.getFieldOfView(), bov.getViewport(), markers, bov.getGlobe()));
    }

    /**
     * This method is used to activate and deactivate the layers that shall be
     * used in the respective model for the world window.
     *
     * Currently it activates the Bing maps for higher resolution imagery.
     *
     * @param m - Model: The model of the WorldWindowGLCanvas
     * @param globe - Globe: The globe used by the WorldWindowGLCanvas
     * @param showNavElements - boolean: indicates whether navigation elements
     * such as the compass or the world map shall be shown
     */
    private void activateLayers(Model m, Globe globe, boolean showNavElements) {

        //go through all available layers 
        for (Layer layer : m.getLayers()) {
            if (layer.getName().startsWith("Bing")) {
                layer.setEnabled(true);//enable Bing Imagery for higher resolution imagery
            }

            if (!showNavElements) {
                if (layer.getName().startsWith("World Map")) {
                    layer.setEnabled(false);
                }
                if (layer.getName().startsWith("Compass")) {
                    layer.setEnabled(false);
                }
            }
        }

        m.getLayers().add(pathLayer);
        m.getLayers().add(markerLayer);
        m.setGlobe(globe);
    }

    /**
     * This method calculates the required zoom level to fit the markers
     * displayed into the field of view of the user.
     *
     * Returns the required zoom level in meters
     *
     * @param eye - Vec4: current eye position of the view
     * @param center - Vec4: current center position of the view
     * @param up - Vec4: current up vector of the view
     * @param fieldOfView - Angle: Current field of view angle
     * @param viewport - Rectangle: The displays viewport
     * @param markers - ArrayList<Marker>: a list of markers that shall be
     * visible
     * @param g - Globe: The globe of the view
     * @return The zoom level required to show all the Markers in real-world
     * meters
     */
    protected double computeZoomLevel(Vec4 eye, Vec4 center, Vec4 up, Angle fieldOfView,
            Rectangle viewport, ArrayList<Marker> markers, Globe g) {

        //Markers must not be null
        if (markers == null) {
            throw new NullPointerException("List of markers must not be null");
        }

        // Compute the modelview matrix from the specified model coordinate look-at parameters.
        Matrix modelview = Matrix.fromViewLookAt(eye, center, up);
        // Compute the forward vector in model coordinates, and the center point in eye coordinates.
        Vec4 f = Vec4.UNIT_NEGATIVE_Z.transformBy4(modelview.getInverse());

        Angle verticalFieldOfView = ViewUtil.computeVerticalFieldOfView(fieldOfView, viewport);
        double hcos = fieldOfView.cosHalfAngle();
        double htan = fieldOfView.tanHalfAngle();
        double vcos = verticalFieldOfView.cosHalfAngle();
        double vtan = verticalFieldOfView.tanHalfAngle();

        double maxDistance = -Double.MAX_VALUE;
        double d;

        // Compute the smallest distance from the center point needed to contain the model 
        //coordinate positions in the viewport.
        for (Marker e : markers) {
            if (e == null || e.getPosition() == null) {
                continue;
            }

            Vec4 p = g.computePointFromPosition(e.getPosition());
            double markerPixels = e.getAttributes().getMarkerPixels();

            d = Math.abs(p.z - center.z) + (Math.abs(p.x - center.x) + (markerPixels / hcos)) / htan;
            if (maxDistance < d) {
                maxDistance = d;
            }

            d = Math.abs(p.z - center.z) + (Math.abs(p.y - center.y) + (markerPixels / vcos)) / vtan;
            if (maxDistance < d) {
                maxDistance = d;
            }
        }

        if (maxDistance == -Double.MAX_VALUE) {
            return 0;
            //TODO throw exception
        }

        return center.distanceTo3(center.add3(f.multiply3(-maxDistance)));
    }
}
