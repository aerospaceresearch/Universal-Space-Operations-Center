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

import com.ksatstuttgart.usoc.data.sensors.chartData.ChartSensor;
import com.ksatstuttgart.usoc.data.sensors.GPSData;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
* <h1>GPSPanel</h1>
* This class provides visualization of the current GPS position.
*
* @author  Valentin Starlinger
* @version 1.0
*/

public class GPSPanel extends JPanel{
    JTextField lastRec, predictedImpact, jlA, jlB, jlPhi;
    JButton export,exportpredicted;
    
    public GPSPanel(){
        
        this.setBorder(BorderFactory.createTitledBorder("GPS"));
        
        GridLayout gl = new GridLayout(6,2);
        gl.setHgap(5);
        gl.setVgap(5);
        
        this.setLayout(gl);
        
        this.add(new JLabel("Last Recoreded Pos: "));
        lastRec = new JTextField();
        this.add(lastRec);
        
        this.add(new JLabel("Predicted Impact: "));
        predictedImpact = new JTextField();
        this.add(predictedImpact);
        
        this.add(new JLabel("A: "));
        jlA = new JTextField();
        this.add(jlA);
        
        this.add(new JLabel("B: "));
        jlB = new JTextField();
        this.add(jlB);
        
        this.add(new JLabel("Phi: "));
        jlPhi = new JTextField();
        this.add(jlPhi);
        
        export = new JButton("export raw GPS");
        export.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File f = new File("export_gps.csv");
                    
                    if(f.exists()){
                        f.delete();
                    }
                    f.createNewFile();
                    FileWriter fw = new FileWriter(f);
                    
                    
                    fw.flush();
                    fw.close();
                } catch (IOException ex) {
                }
                
            }
            
        });
        this.add(export);
             
        exportpredicted = new JButton("export predicted GPS");
        exportpredicted.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File f = new File("export_predicted.csv");
                    
                    if(f.exists()){
                        f.delete();
                    }
                    f.createNewFile();
                    FileWriter fw = new FileWriter(f);
                    
                    
                    fw.flush();
                    fw.close();
                } catch (IOException ex) {
                }
                
            }
            
        });
        this.add(exportpredicted);
        
        this.setMaximumSize(new java.awt.Dimension(400,this.getHeight()));
        
    }
    
    public ArrayList<ChartSensor> updateGPS(GPSData gps){
        
        if(gps.getLat()<40||gps.getLat()>90){
            return new ArrayList<>();
        }
        
        if(gps.getLon()<10||gps.getLon()>40){
            return new ArrayList<>();
        }
        
        
        lastRec.setText("lat: "+gps.getLat()+", lon: "+gps.getLon());
        
        ArrayList<ChartSensor> al = new ArrayList<>();
        return al;
    }
}