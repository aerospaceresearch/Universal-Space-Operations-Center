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
package com.ksatstuttgart.usoc.test.controller;

import com.ksatstuttgart.usoc.controller.xml.XMLReader;
import com.ksatstuttgart.usoc.data.message.dataPackage.DataPackage;
import com.ksatstuttgart.usoc.data.message.DataPoint;
import com.ksatstuttgart.usoc.data.message.ProtocolType;
import com.ksatstuttgart.usoc.data.message.SBD340Message;
import com.ksatstuttgart.usoc.data.message.dataPackage.DataType;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.data.message.dataPackage.SensorType;
import com.ksatstuttgart.usoc.data.message.header.Header;
import com.ksatstuttgart.usoc.data.message.header.MetaData;
import com.ksatstuttgart.usoc.data.message.header.MetaDataType;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Valentin Starlinger
 * @version 1.0
 */
public class XMLReaderTest {

    public XMLReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isOK method, of class DataController.
     */
    @org.junit.Test
    public void testReadingDatapackage() {
        System.out.println("testing Reading Data Structure");

        SBD340Message result = XMLReader.getInstance().getMessageStructure("protocols/testProtocol.xml");

        SBD340Message sbd = new SBD340Message();

        DataPackage dp = new DataPackage();
        Sensor s = new Sensor();
        s.setSensorName("testName");
        s.setType(SensorType.PRESSURE);

        DataPoint datapoint = new DataPoint();
        datapoint.setDataName("testSensorData");
        datapoint.setDataType(DataType.FLOAT);
        datapoint.setStartPosition(40);

        s.addDataPoint(datapoint);
        dp.addSensor(s);

        sbd.setDataPackage(dp);

        Header h = new Header();

        MetaData m = new MetaData();
        m.setType(MetaDataType.TIME);
        DataPoint newDataPoint = new DataPoint();

        newDataPoint.setDataName("testMetaData");
        newDataPoint.setDataType(DataType.FLOAT);
        newDataPoint.setStartPosition(40);

        m.addDataPoint(newDataPoint);
        h.addMetaData(m);

        sbd.setHeader(h);

        assertEquals(sbd, result);
    }

}
