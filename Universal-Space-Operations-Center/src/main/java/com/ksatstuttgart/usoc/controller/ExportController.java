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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author valentinstarlinger
 */
public class ExportController {

    public static void saveDataAsCSV(Data data, File f, boolean saveRaw) throws IOException {
        if (f == null) {
            //TODO: throw exception
            return;
        }

        if (!f.exists()) {
            f.createNewFile();
        }

        //store a list of all keys to be able to probably store the data in order
        LinkedList<Long> keys = new LinkedList<>();
        LinkedList<String> varTitles = new LinkedList<>();

        for (Sensor sensor : data.getSensors()) {
            //save adjusted data
            Sensor adjusted = sensor;
            if (!saveRaw) {
                adjusted = DataModification.adjustSensorData(sensor);
            }
            for (Var var : adjusted.getVars()) {
                varTitles.add(var.getDataName() + " [" + var.getUnit() + "]");
                for (Long key : var.getValues().keySet()) {
                    if (!keys.contains(key)) {
                        keys.add(key);
                    }
                }
            }
        }

        Collections.sort(keys);

        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write("time");
        bw.flush();
        for (String varName : varTitles) {
            //this works because the first element is time
            System.out.print(", " + varName);
            bw.write("," + varName);
            bw.flush();
        }
        System.out.println("");
        bw.write("\n");
        bw.flush();
        for (Long key : keys) {
            bw.write("" + key);
            bw.flush();
            for (Sensor sensor : data.getSensors()) {
                //save adjusted data
                Sensor adjusted = sensor;
                if (!saveRaw) {
                    adjusted = DataModification.adjustSensorData(sensor);
                }
                for (String varName : varTitles) {
                    for (Var var : adjusted.getVars()) {
                        if (varName.equals(var.getDataName() + " [" + var.getUnit() + "]")) {
                            System.out.print(", " + varName);
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
            System.out.println("");
            bw.write("\n");
            bw.flush();
        }

    }

}
