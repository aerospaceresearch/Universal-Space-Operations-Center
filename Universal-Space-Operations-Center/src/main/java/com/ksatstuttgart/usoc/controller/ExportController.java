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
import com.ksatstuttgart.usoc.data.message.dataPackage.Data;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Controller responsible for exporting the displayed data into different file formats
 * currently supported file formats:
 * .csv
 * @author valentinstarlinger
 */
public class ExportController {

    /**
     * Saves the data from the Data Object to the specified File
     * 
     * @param data - Data: data that should be stored
     * @param f - File: File that the data should be saved to
     * @param saveRaw - boolean: specifies whether the DataModification.adjustSensor method
     * should be used to adjust the sensor data before it is saved.
     * @throws IOException 
     */
    public static void saveDataAsCSV(Data data, File f, boolean saveRaw) throws IOException {
        if (f == null) {
            //TODO: throw exception
            return;
        }

        if (!f.exists()) {
            f.createNewFile();
        }
        
        if(!saveRaw){
            //if adjusted data should be saved get and store adjusted sensor data
            Data adjustedData = new Data();
            for (Sensor sensor : data.getSensors()) {
                adjustedData.addSensor(DataModification.adjustSensorData(sensor));
            }
            data = adjustedData;
        }

        //store a list of all keys to be able to probably store the data in order
        LinkedList<Long> keys = new LinkedList<>();
        LinkedList<String> varTitles = new LinkedList<>();

        for (Sensor sensor : data.getSensors()) {
            for (Var var : sensor.getVars()) {
                varTitles.add(var.getDataName() + " [" + var.getUnit() + "]");
                for (Long key : var.getValues().keySet()) {
                    if (!keys.contains(key)) {
                        keys.add(key);
                    }
                }
            }
        }

        Collections.sort(keys);

        //writing the column titles
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write("time");
        bw.flush();
        for (String varName : varTitles) {
            //this works because the first element is time
            bw.write("," + varName);
            bw.flush();
        }
        bw.write("\n");
        bw.flush();
        
        //make sure the keys are sorted before writing them to the file
        Collections.sort(keys);
        for (Long key : keys) {
            bw.write("" + key);
            bw.flush();
            for (Sensor sensor : data.getSensors()) {
                for (String varName : varTitles) {
                    for (Var var : sensor.getVars()) {
                        if (varName.equals(var.getDataName() + " [" + var.getUnit() + "]")) {
                            bw.write(",");
                            bw.flush();
                            if (var.getValues().containsKey(key)) {
                                bw.write(var.getValues().get(key) + "");
                                bw.flush();
                            }
                        }
                    }
                }
            }
            bw.write("\n");
            bw.flush();
        }

    }

}
