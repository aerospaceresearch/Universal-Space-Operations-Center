/*
 * The MIT License
 *
 * Copyright 2017 KSat e.V.
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
package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
* <h1>GPSPanel</h1>
* This class provides visualization of the current GPS position.
*
* @author  Valentin Starlinger
* @version 1.0
*/

public class GPSPanel extends JPanel{
    JLabel longitude, latitude, altitude, groundSpeed, heading, time;
    
    public GPSPanel(){
        
        this.setBorder(BorderFactory.createTitledBorder("GPS"));
        
        GridLayout gl = new GridLayout(6,2);
        gl.setHgap(5);
        gl.setVgap(5);
        
        this.setLayout(gl);
        
        this.add(new JLabel("Longitude: "));
        longitude = new JLabel();
        this.add(longitude);
        
        this.add(new JLabel("Latitude: "));
        latitude = new JLabel();
        this.add(latitude);
        
        this.add(new JLabel("Altitude: "));
        altitude = new JLabel();
        this.add(altitude);
        
        this.add(new JLabel("Ground Speed: "));
        groundSpeed = new JLabel();
        this.add(groundSpeed);
        
        this.add(new JLabel("Heading: "));
        heading = new JLabel();
        this.add(heading);
        
        this.add(new JLabel("Time: "));
        time = new JLabel();
        this.add(time);
        
        this.setMaximumSize(new java.awt.Dimension(400,this.getHeight()));
        
    }
    
    public void updateGPS(Sensor gpsSensor){
        //go through gps sensor and set the respective label texts with the latest
        //values
        for (Var var : gpsSensor.getVars()) {
            Object[] entrySet = var.getValues().entrySet().toArray();
            String s = entrySet[entrySet.length-1].toString();
            
            switch(var.getDataName()){
                case "Longitude": 
                    longitude.setText(s);
                    break;
                case "Latitude": 
                    latitude.setText(s);
                    break;
                case "Altitude": 
                    altitude.setText(s);
                    break;
                case "Ground Speed": 
                    groundSpeed.setText(s);
                    break;
                case "Heading": 
                    heading.setText(s);
                    break;
                case "Time": 
                    time.setText(s);
                    break;
            }
        }
    }
}