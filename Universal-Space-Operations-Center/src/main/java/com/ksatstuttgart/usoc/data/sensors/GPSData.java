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
package com.ksatstuttgart.usoc.data.sensors;

import com.ksatstuttgart.usoc.data.sensors.chartData.ChartSensor;
import com.ksatstuttgart.usoc.controller.Utility;
import java.util.ArrayList;

/**
* <h1>GPS</h1>
* This class is a data wrapper for the GPS receiver on the MIRKA2-RX mission.
* 
* <p>
* 
*
* @author  Valentin Starlinger
* @version 1.0
*/
public class GPSData {
    
    private State state;
    private float lon,lat,speed,heading;
    
    private Mode m;
    private int tpos, systime;

    public GPSData(String content, int tpos, Mode m, int systime){
        this.tpos = tpos;
        this.m = m;
        this.systime = systime;
        parseData(content);
    }
    
    public Double getTime() {
        return new Double(systime + (tpos - 1) * (12/SensorPos.GPS.getFrequency(m)));
    }

    public void parseData(String content){
        this.state = State.getState(Integer.parseUnsignedInt(content.substring(SensorPos.GPS_HK.getStart(m), SensorPos.GPS_HK.getEnd(m)),2));
        
        int diff = SensorPos.GPS.getDiff(m) * 4;
        
        //System.out.println((SensorPos.GPS.getStart(m)+ (tpos-1)*diff)+" - "+(SensorPos.GPS.getStart(m))+" - "+(diff));
        this.lat = Utility.stringToFloat(content.substring(SensorPos.GPS.getStart(m)+ (tpos-1)*diff, SensorPos.GPS.getEnd(m) + (tpos-1)*diff), false);
        this.lon = Utility.stringToFloat(content.substring(SensorPos.GPS.getStart(m)+ (tpos-1)*diff + 32 * 1, SensorPos.GPS.getEnd(m) + (tpos-1)*diff + 32 * 1), false);
        
        //!!!nicht nach protokol wurde evt. bei OBC vertauscht
        this.heading = Utility.stringToFloat(content.substring(SensorPos.GPS.getStart(m)+ (tpos-1)*diff + 32 * 2, SensorPos.GPS.getEnd(m) + (tpos-1)*diff + 32 * 2), false);
        this.speed = Utility.stringToFloat(content.substring(SensorPos.GPS.getStart(m)+ (tpos-1)*diff + 32 * 3, SensorPos.GPS.getEnd(m) + (tpos-1)*diff + 32 * 3), false);
        
    }
    
    @Override
    public String toString(){
        return "GPS: "+state+" lat: "+lat+", lon: "+lon+", speed: "+speed+", heading: "+heading;
    }

    public State getState() {
        return state;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDirection() {
        return heading;
    }

    public void setDirection(float direction) {
        this.heading = direction;
    }

    public ArrayList<ChartSensor> getLatLon() {
        ArrayList<ChartSensor> a = new ArrayList<>();
        return a;
    }
    
}
