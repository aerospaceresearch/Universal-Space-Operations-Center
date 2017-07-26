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
package com.ksatstuttgart.usoc.controller;

import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.DataType;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import java.util.ArrayList;

/**
 *
 * @author valentinstarlinger
 */
public class DataModification {

    public static Sensor adjustSensorData(Sensor sensor) {
        //modify this for every experiment. 
        //currently adjusted for MIRKA2-ICV
        Sensor adjusted = new Sensor(sensor);

        if (sensor.getSensorName().contains("IMU")) {
            adjusted.setVariables(new ArrayList<Var>());

            Var yaw = new Var();
            Var pitch = new Var();
            Var roll = new Var();

            yaw.setDataName("Yaw");
            pitch.setDataName("Pitch");
            roll.setDataName("Roll");

            yaw.setDataType(DataType.FLOAT32);
            pitch.setDataType(DataType.FLOAT32);
            roll.setDataType(DataType.FLOAT32);

            for (Long key : sensor.getVars().get(0).getValues().keySet()) {
                double n = 0, x = 0, y = 0, z = 0;
                for (Var var : sensor.getVars()) {
                    if (var.getDataName().contains("W")) {
                        n = new Double((int) var.getValues().get(key));
                    } else if (var.getDataName().contains("X")) {
                        x = new Double((int) var.getValues().get(key));
                    } else if (var.getDataName().contains("Y")) {
                        y = new Double((int) var.getValues().get(key));
                    } else if (var.getDataName().contains("Z")) {
                        z = new Double((int) var.getValues().get(key));
                    }
                }

                double abs = Math.sqrt((Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2)+Math.pow(n, 2)));
                
                x = x/abs;
                y = y/abs;
                z = z/abs;
                n = n/abs;
                
                yaw.addValue(key, (180/Math.PI)*Math.atan2((2 * x * y + 2 * z * n), (Math.pow(n, 2) + Math.pow(x, 2) - Math.pow(y, 2) - Math.pow(z, 2))));
                pitch.addValue(key,  (180/Math.PI)*Math.asin((2 * y * n - 2 * z * x)));
                roll.addValue(key,  (180/Math.PI)*Math.atan2((2 * z * y + 2 * x * n), (Math.pow(n, 2) - Math.pow(x, 2) - Math.pow(y, 2) + Math.pow(z, 2))));
            }

            adjusted.addVariable(yaw);
            adjusted.addVariable(pitch);
            adjusted.addVariable(roll);
        }

        if (sensor.getSensorName().contains("Thermocouple")) {
            for (Var var : adjusted.getVars()) {
                var.setDataType(DataType.FLOAT32);
                for (Long key : var.getValues().keySet()) {
                    var.getValues().replace(key, (double) (((int) var.getValues().get(key)) / 10));
                }
            }
        }

        if (sensor.getSensorName().contains("GNSS TIME")) {

        }

        if (sensor.getSensorName().contains("Battery")) {
            for (Var var : adjusted.getVars()) {
                var.setDataType(DataType.FLOAT32);
                for (Long key : var.getValues().keySet()) {
                    double newValue = (double) ((((double) ((int) var.getValues().get(key) * 4)) / 1024d) * 3.3 * 3);
                    var.getValues().replace(key, newValue);
                }
            }
        }
//        System.out.println("adjusted data for sensor: " + sensor.getSensorName());
//        for (Var var : adjusted.getVars()) {
//            System.out.print(var.getDataName()+": ");
//            for (Long key : var.getValues().keySet()) {
//                System.out.println("adjusted: " + var.getValues().get(key));
//            }
//
//        }
        return adjusted;
    }

}
