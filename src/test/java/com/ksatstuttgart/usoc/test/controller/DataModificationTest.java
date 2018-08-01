/*
 * MIT License
 *
 * Copyright (c) 2017 KSat e.V. and AerospaceResearch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.ksatstuttgart.usoc.test.controller;

import com.ksatstuttgart.usoc.controller.DataModification;
import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.controller.xml.XMLReader;
import com.ksatstuttgart.usoc.data.message.SBD340;
import com.ksatstuttgart.usoc.data.message.dataPackage.Data;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import static com.ksatstuttgart.usoc.test.controller.MessageControllerTest.TESTFILEPATH;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author valentinstarlinger
 */
public class DataModificationTest {

    public static final String TESTFILEPATH = "tests" + File.separator + "files";
    public static final String TESTPROTOCOLPATH = "tests" + File.separator + "protocols";

    MessageController msgController;

    public DataModificationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        //use MainController with 
        SBD340 structure = XMLReader.getInstance()
                .getMessageStructure(TESTPROTOCOLPATH + "/USOC_SBD340_ICV.xml");
        MainController.getInstance().getMessageController().setMessageStructure(structure);
        MainController.getInstance().addBinaryFile(new File(TESTFILEPATH + File.separator + "testMsg.bin"));
    }

    /**
     * Test of adjustSensorData method, of class DataModification.
     */
    @Test
    public void testAdjustSensorData() {

        System.out.println("adjustSensorData");
        Data resultData = new Data(MainController.getInstance().getMessageController().getData());
        resultData.setSensors(new ArrayList<Sensor>());
        for (Sensor sensor : MainController.getInstance().getMessageController().getData().getSensors()) {
            resultData.addSensor(DataModification.adjustSensorData(sensor));
        }

        String result = resultData.toString();
        String expResult = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(TESTFILEPATH + File.separator + "dataModificationTest.txt"));
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                expResult += nextLine+"\n";
            }
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assertEquals(expResult.trim(), result.trim());
    }

}
