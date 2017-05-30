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
package com.ksatstuttgart.usoc.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
* <h1>Log Saver</h1>
* This class provides static methods to save and export the data shown in the
* Log Viewer
* <p>
* 
*
* @author  Valentin Starlinger
* @version 1.0
*/
public class LogSaver {
    
    private static File parent;
    
    private static final String PARENTNAME = "GS_Data";

    public static void saveIridium(String s) {
        File dir = new File(getParent()+"Iridium");
        if (!dir.exists() || dir.isDirectory()) {
            dir.mkdir();
        }

        File iFile = new File(getParent()+"Iridium/iridium_" +getTimeField()+ ".txt");

        FileWriter fw = null;
        try {
            fw = new FileWriter(iFile);
            fw.write(s);
            fw.flush();
        } catch (IOException ex) {
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public static void saveDownlink(String s, boolean isIridium) {

        File dir = new File(getParent()+"Downlink");
        if (!dir.exists() || dir.isDirectory()) {
            dir.mkdir();
        }
        
        if (!isIridium) {
            File dFile = new File(getParent()+"Downlink/downlink_data.txt");
            File bFile = new File(getParent()+"Downlink/downlink_backup.txt");
            
            FileWriter fw = null;
            try {
                fw = new FileWriter(dFile,true);
                fw.write(s);
                fw.flush();
                fw.close();
                fw = new FileWriter(bFile,true);
                fw.write(s);
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                
            } finally {
                try {
                    if (fw != null) {
                        fw.close();
                    }
                } catch (IOException ex) {
                }
            }
        } else {
            File idir = new File(getParent()+"Downlink/Iridium");
            
            if (!idir.exists() || idir.isDirectory()) {
                idir.mkdir();
            }

            File iFile = new File(getParent()+"Downlink/Iridium/iridium_" + getTimeField() + ".txt");

            FileWriter fw = null;
            try {
                fw = new FileWriter(iFile);
                fw.write(s);
                fw.flush();
            } catch (IOException ex) {
            } finally {
                try {
                    if (fw != null) {
                        fw.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
    }
    
    /*
    
    TODO: THIS DOES NOT BELONG HERE!!!
    
    */
    
    private static String getParent(){
        if(parent == null){
            parent = new File(PARENTNAME+getTimeField());
            parent.mkdir();
        } 
        return parent.getAbsolutePath()+File.separator;
    }
    
    private static String getTimeField(){
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);
            int sec = c.get(Calendar.SECOND);
            
            return year+"-"+month+"-"+day+"_"+hour+"-"+min+"-"+sec;
    }

    private static boolean isIridium(String s) {
        return false;
    }
}
