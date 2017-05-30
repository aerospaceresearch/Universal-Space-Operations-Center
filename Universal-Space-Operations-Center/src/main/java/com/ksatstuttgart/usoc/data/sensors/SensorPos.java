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

/**
 *
 * @author valentinstarlinger
 */
public enum SensorPos {
    GPS (-1,-1,2144,2176,2176,2208,0,4,2),
    GPS_HK (45,46,1),
    
    IMU_Rx (368,384,3),
    IMU_Ry (384,400,3),
    IMU_Rz (400,416,3),
    IMU_Qw (656,672,12,12,6),
    IMU_Qx (672,688,12,12,6),
    IMU_Qy (688,704,12,12,6),
    IMU_Qz (704,720,12,12,6),
    IMU_C (720,728,12,12,6),
    IMU_HK (46,47,1),
    
    CJ (2096,2112,1808,1824,1808,1824,6,3,3),
    CJ_HK (33,34,1),
    
    IRIDIUM_SIGNAL (14,17,1),
    IRIDIUM_LOW (17,20,1),
    IRIDIUM_HIGH (20,23,1),
    IRIDIUM_TRIES (23,27,1),
    
    MCU_MSG(0,8,1),
    MCU (8,10,1),
    MCU_RC(48,56,1),
    MCU_TIME(72,88,1),
    
    PS(128,144,6),
    AP(320,328,6),
    PS_HK(42,43,1),
    
    SD (40,41,1),
    
    TC (1520,1536,1520,1536,1520,1536,6,3,3),
    TC_HK (28,29,1),
    
    TP (2672,2688,2096,2112,-1,-1,3,3,0),
    TP_HK (41,42,1),
    
    VB(120,128,1),
    VB_HK(39,40,1),
    
    PB (47,47,1);
    
    private int hstart, hend, mstart, mend, lstart, lend;
    private int hf, mf, lf;
    
    private SensorPos(int start, int end, int hf){
        this(start, end, start, end, start, end, hf, hf, hf);
    }
    
    private SensorPos(int start, int end, int hf, int mf, int lf){
        this(start, end, start, end, start, end, hf, mf, lf);
    }

    private SensorPos(int hstart, int hend, int mstart, int mend, int lstart, int lend, int hf) {
        this(hstart, hend, mstart, mend, lstart, lend, hf, hf, hf);
    }
    
    private SensorPos(int hstart, int hend, int mstart, int mend, int lstart, int lend, int hf, int mf, int lf) {
        this.hstart = hstart;
        this.hend = hend;
        this.mstart = mstart;
        this.mend = mend;
        this.lstart = lstart;
        this.lend = lend;
        this.hf = hf;
        this.mf = mf;
        this.lf = lf;
    }
    
    public int getStart(Mode m){
        switch(m){
            case HIGHALT:
                return hstart;
            case MIDALT:
                return mstart;
            case LOWALT:
                return lstart;
            default:
                return -1;
        }
    }
    
    public int getEnd(Mode m){
        switch(m){
            case HIGHALT:
                return hend;
            case MIDALT:
                return mend;
            case LOWALT:
                return lend;
            default:
                return -1;
        }
    }
    
    public int getStart(){
        return hstart;
    }
    
    public int getEnd(){
        return hend;
    }
    
    public int getDiff(Mode m){
        switch(m){
            case HIGHALT:
                return hend-hstart;
            case MIDALT:
                return mend-mstart;
            case LOWALT:
                return lend-lstart;
            default:
                return -1;
        }
    }

    public int getFrequency(Mode m) {
        switch(m){
            case HIGHALT:
                return hf;
            case MIDALT:
                return mf;
            case LOWALT:
                return lf;
            default:
                return 0;
        }
    }
}
