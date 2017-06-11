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
package com.ksatstuttgart.usoc.data.sensors.chartData;

import com.ksatstuttgart.usoc.data.sensors.Mode;
import com.ksatstuttgart.usoc.data.sensors.SensorPos;
import com.ksatstuttgart.usoc.controller.Utility;

/**
 *
 * @author valentinstarlinger
 */
public class MCU {
    
    private int resetcounter;
    private Mode mode;    
    private int systemtime;
    private int timeOffset;
    private int msgnumber;
    private int num;
    
    public MCU(int num, String content){
        this(num, content, 0);
    }

    public MCU(int num, String content, int timeOffset) {
        this.num = num;
        this.timeOffset = timeOffset;
        parseData(content);
    }

    public int getMsgnumber() {
        return msgnumber;
    }

    public int getResetcounter() {
        return resetcounter;
    }
    
    public Mode getMode() {
        return mode;
    }

    public int getSystemtime() {
        return systemtime - timeOffset;
    }

    public void parseData(String content) {
        
        this.msgnumber = Integer.parseUnsignedInt(content.substring(SensorPos.MCU_MSG.getStart(), SensorPos.MCU_MSG.getEnd()),2);
        this.mode = (Mode.getModeFromInt(Integer.parseUnsignedInt(content.substring(SensorPos.MCU.getStart()+(num-1)*2, SensorPos.MCU.getEnd()+(num-1)*2),2)));
        this.resetcounter = Integer.parseUnsignedInt(content.substring(SensorPos.MCU_RC.getStart()+(num-1)*8, SensorPos.MCU_RC.getEnd()+(num-1)*8),2);
        this.systemtime = Utility.binToUInt(content.substring(SensorPos.MCU_TIME.getStart()+(num-1)*16, SensorPos.MCU_TIME.getEnd()+(num-1)*16), false);
        
    }
    
    @Override
    public String toString(){
        return "MCU"+num+": "+"state: "+mode+", Message number: "+msgnumber+", Systemtime: "+systemtime+", Resetcounter: "+resetcounter;
    }
    
}
