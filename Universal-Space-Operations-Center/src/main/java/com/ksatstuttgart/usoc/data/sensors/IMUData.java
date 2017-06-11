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
import com.ksatstuttgart.usoc.data.sensors.chartData.IMUChart;
import com.ksatstuttgart.usoc.controller.Utility;

/**
* <h1>IMU</h1>
* This class is a data wrapper for the IMU chip on the MIRKA2-RX mission.
* 
* <p>
* 
*
* @author  Valentin Starlinger
* @version 1.0
*/
public class IMUData {

    private State state;
    private int rx, ry, rz;
    private int qw, qx, qy, qz;
    private int calib;
    private int tpos, systime;
    private Mode m;

    public IMUData(String content, int tpos, Mode m, int systime) {
        this.m = m;
        this.tpos = tpos;
        this.systime = systime;
        parseData(content);
    }
    
        
    public Double getTime() {
        return new Double(systime + (tpos - 1) * (12/SensorPos.IMU_Qw.getFrequency(m)));
    }

    public State getState() {
        return state;
    }

    public ChartSensor getRx() {
        return new IMUChart(getTime(), (new Double(rx)/10 * 57.296),state);
    }

    public ChartSensor getRy() {
        return new IMUChart(getTime(), -(new Double(ry)/10 * 57.296),state);
    }

    public ChartSensor getRz() {
        return new IMUChart(getTime(), (new Double(rz)/10 * 57.296),state);
    }

    public ChartSensor getQw() {
        return new IMUChart(getTime(), new Double(qw),state);
    }

    public ChartSensor getQx() {
        return new IMUChart(getTime(), new Double(qx),state);
    }

    public ChartSensor getQy() {
        return new IMUChart(getTime(), new Double(qy),state);
    }

    public ChartSensor getQz() {
        return new IMUChart(getTime(), new Double(qz),state);
    }

    public int getCalib() {
        return calib;
    }

    public void parseData(String content) {

        this.state = State.getState(Integer.parseUnsignedInt(content.substring(SensorPos.IMU_HK.getStart(m), SensorPos.IMU_HK.getEnd(m)),2));

        if ((m == Mode.LOWALT && tpos % 2 != 0) || ((tpos-1) % 4 == 0)) {
            
            int o = m == Mode.LOWALT ? tpos / 2 : tpos / 4;

            this.rx = Utility.binToInt(content.substring(SensorPos.IMU_Rx.getStart(m) + o * SensorPos.IMU_Rx.getDiff(m)*6
                    , SensorPos.IMU_Rx.getEnd(m) + o * SensorPos.IMU_Rx.getDiff(m)*6), false);
//                System.out.println(content.substring(SensorPos.IMU_Rx.getStart(m) + o * SensorPos.IMU_Rx.getDiff(m)*3
//                        , SensorPos.IMU_Rx.getEnd(m) + o * SensorPos.IMU_Rx.getDiff(m)*3));
//                System.out.println((SensorPos.IMU_Rx.getStart(m) + o * SensorPos.IMU_Rx.getDiff(m)*6)
//                        +" - "+
//                        (SensorPos.IMU_Rx.getEnd(m) + o * SensorPos.IMU_Rx.getDiff(m)*6));
            this.ry = Utility.binToInt(content.substring(SensorPos.IMU_Ry.getStart(m) + o * SensorPos.IMU_Ry.getDiff(m)*6
                    , SensorPos.IMU_Ry.getEnd(m) + o * SensorPos.IMU_Ry.getDiff(m)*6), false);
            
            this.rz = Utility.binToInt(content.substring(SensorPos.IMU_Rz.getStart(m) + o * SensorPos.IMU_Rz.getDiff(m)*6
                    , SensorPos.IMU_Rz.getEnd(m) + o * SensorPos.IMU_Rz.getDiff(m)*6), false);
            
            if(rx < 0){
                rx = rx*(-1)-32767;
            }
            if(ry < 0){
                ry = ry*(-1)-32767;
            }
            if(rz < 0){
                rz = rz*(-1)-32767;
            }
        }

//        System.out.println("pos: "+(posQw.x + (tpos - 1) * 72)+" - "+(posQw.y + (tpos - 1) * 72)+" - "+tpos);
//        System.out.println(Arrays.toString(Util.getContentFromPos(posQw.x + (tpos - 1) * 72, posQw.y + (tpos - 1) * 72, content)));

        int diff = (SensorPos.IMU_Qw.getDiff(m))*4 + (SensorPos.IMU_C.getDiff(m));

        this.qw = Utility.binToInt(content.substring(SensorPos.IMU_Qw.getStart(m) + (tpos - 1) * diff
                , SensorPos.IMU_Qw.getEnd(m) + (tpos - 1) * diff), false);
        this.qx = Utility.binToInt(content.substring(SensorPos.IMU_Qx.getStart(m) + (tpos - 1) * diff
                , SensorPos.IMU_Qx.getEnd(m) + (tpos - 1) * diff), false);
        this.qy = Utility.binToInt(content.substring(SensorPos.IMU_Qy.getStart(m) + (tpos - 1) * diff
                , SensorPos.IMU_Qy.getEnd(m) + (tpos - 1) * diff), false);
        this.qz = Utility.binToInt(content.substring(SensorPos.IMU_Qz.getStart(m) + (tpos - 1) * diff
                , SensorPos.IMU_Qz.getEnd(m) + (tpos - 1) * diff), false);
        
        this.calib = Integer.parseUnsignedInt(content.substring(SensorPos.IMU_C.getStart(m) + (tpos - 1) * diff, SensorPos.IMU_C.getEnd(m) + (tpos - 1) * diff),2);

    }
    
    @Override
    public String toString() {
        String s = "IMU: " + state;
        if (m == Mode.LOWALT || tpos % 2 != 0) {
            s += ", rx: " + rx + ", ry: " + ry + ", rz: " + rz;
        }
        s += ", qw: " + qw + ", qx: " + qx + ", qy: " + qy + ", qz: " + qz + ", Calib: " + calib;
        return s;
    }

    public boolean hasRs() {
        return ((m == Mode.LOWALT && tpos % 2 != 0) || ((tpos-1) % 4 == 0));
    }

}
