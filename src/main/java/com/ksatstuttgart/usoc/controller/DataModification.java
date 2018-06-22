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

import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.DataType;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import java.util.ArrayList;

/**
 * This class is responsible for adjusting sensor data for better data 
 * representation.
 * 
 * @author valentinstarlinger
 */
public class DataModification {

    /**
     * This method is used to adjust sensor data.
     * 
     * This should be modified for every experiment. 
     * Currently adjusted for the MIRKA2-ICV experiment!
     * 
     * @param sensor - SensorDTO: A sensor that should be adjusted
     * @return a modified SensorDTO
     */
    public static Sensor adjustSensorData(Sensor sensor) {
        //copy the sensor so the original sensor is not messed with
        Sensor adjusted = new Sensor(sensor);

        //MIRKA2-ICV: adjust IMU data - conversion quaternion to euler angles
        if (sensor.getSensorName().contains("IMU")) {
            //for the IMU conversion it is necessary to generate new variables
            adjusted.setVariables(new ArrayList<Var>());

            //create yaw, pitch and roll variables
            Var yaw = new Var();
            Var pitch = new Var();
            Var roll = new Var();

            yaw.setDataName("Yaw");
            yaw.setUnit("[deg]");
            yaw.setDataType(DataType.FLOAT32);
            pitch.setDataName("Pitch");
            pitch.setUnit("[deg]");
            pitch.setDataType(DataType.FLOAT32);
            roll.setDataName("Roll");
            roll.setUnit("[deg]");
            roll.setDataType(DataType.FLOAT32);
            
            //for every timestep create euler angles from quaternions
            for (Long key : sensor.getVars().get(0).getValues().keySet()) {
                //get w,x,y and z variables for the specified time step
                double w = 0, x = 0, y = 0, z = 0;
                for (Var var : sensor.getVars()) {
                    if (var.getDataName().contains("W")) {
                        w = new Double((int) var.getValues().get(key));
                    } else if (var.getDataName().contains("X")) {
                        x = new Double((int) var.getValues().get(key));
                    } else if (var.getDataName().contains("Y")) {
                        y = new Double((int) var.getValues().get(key));
                    } else if (var.getDataName().contains("Z")) {
                        z = new Double((int) var.getValues().get(key));
                    }
                }
                
                //normalize the values
                double abs = Math.sqrt((Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2)+Math.pow(w, 2)));               
                x = x/abs;
                y = y/abs;
                z = z/abs;
                w = w/abs;
                
                //compute and add values
                yaw.addValue(key, (180/Math.PI)*Math.atan2((2 * x * y + 2 * z * w), 
                        (Math.pow(w, 2) + Math.pow(x, 2) - Math.pow(y, 2) - Math.pow(z, 2))));
                pitch.addValue(key,  (180/Math.PI)*Math.asin((2 * y * w - 2 * z * x)));
                roll.addValue(key,  (180/Math.PI)*Math.atan2((2 * z * y + 2 * x * w), 
                        (Math.pow(w, 2) - Math.pow(x, 2) - Math.pow(y, 2) + Math.pow(z, 2))));
            }

            //add variables to the adjusted sensor
            adjusted.addVariable(yaw);
            adjusted.addVariable(pitch);
            adjusted.addVariable(roll);
        }

        //MIRKA2-ICV: Thermocouple values are multiplied by 10 and saved as UINT
        //to save bandwith
        if (sensor.getSensorName().contains("Thermocouple")) {
            for (Var var : adjusted.getVars()) {
                //change datatype to new value
                var.setDataType(DataType.FLOAT32);
                for (Long key : var.getValues().keySet()) {
                    //devide the current value by 10 and replace the old one
                    var.getValues().replace(key, (double) (((int) var.getValues().get(key)) / 10));
                }
            }
        }

        //MIRKA2-ICV: Battery Voltage is only sent as direct voltage reading.
        if (sensor.getSensorName().contains("Battery")) {
            for (Var var : adjusted.getVars()) {
                //change datatype 
                var.setDataType(DataType.FLOAT32);
                for (Long key : var.getValues().keySet()) {
                    //compute actual battery voltage from voltage reading
                    double newValue = (double) ((((double) ((int) var.getValues().get(key) * 4)) / 1024d) * 3.3 * 3);
                    //replace old value
                    var.getValues().replace(key, newValue);
                }
            }
        }
        return adjusted;
    }

}
