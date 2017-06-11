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
package com.ksatstuttgart.usoc.controller.xml;

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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author valentinstarlinger
 */
public class XMLReader {

    private static XMLReader instance;

    public static XMLReader getInstance() {
        if (instance == null) {
            instance = new XMLReader();
        }
        return instance;
    }

    public SBD340Message getMessageStructure(String filename) {
        InputStream file;
        try {
            file = new FileInputStream(filename);
            return stax(file);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return new SBD340Message();
    }

    public SBD340Message stax(InputStream file) {
        SBD340Message sbd = new SBD340Message();
        DataPackage dataPackage = new DataPackage();
        Header header = new Header();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader parser = factory.createXMLStreamReader(file);

            Sensor lastSensor = new Sensor();
            DataPoint lastDataPoint = new DataPoint();
            MetaData lastMetaData = new MetaData();
            while (parser.hasNext()) {

                switch (parser.getEventType()) {
                    case XMLStreamConstants.START_DOCUMENT:
                        break;

                    case XMLStreamConstants.END_DOCUMENT:
                        parser.close();
                        break;

                    case XMLStreamConstants.NAMESPACE:
                        break;

                    case XMLStreamConstants.START_ELEMENT:
                        System.out.println("START_ELEMENT: " + parser.getLocalName());
                        switch (parser.getLocalName()) {
                            case "sensor":
                                lastSensor = new Sensor();
                                break;
                            case "metadata":
                                lastMetaData = new MetaData();
                                break;
                            case "datapoint":
                                lastDataPoint = new DataPoint();
                                break;
                        }
                        
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String value = parser.getAttributeValue(i);
                            System.out.println("  Attribut: "
                                    + parser.getAttributeLocalName(i)
                                    + " Wert: " + parser.getAttributeValue(i));
                            switch (parser.getAttributeLocalName(i)) {
                                case "protocol":
                                    sbd.setProtocol(ProtocolType.valueOf(value));
                                    break;
                                case "metadatatype":
                                    lastMetaData.setType(MetaDataType.valueOf(value));
                                    break;
                                case "metadataname":
                                    lastMetaData.setMetaDataName(value);
                                    break;
                                case "sensortype":
                                    lastSensor.setType(SensorType.valueOf(value));
                                    break;
                                case "sensorname":
                                    lastSensor.setSensorName(value);
                                    break;
                                case "start":
                                    lastDataPoint.setStartPosition(Integer.parseInt(value));
                                    break;
                                case "dataName":
                                    lastDataPoint.setDataName(value);
                                    break;
                                case "dataType":
                                    lastDataPoint.setDataType(DataType.valueOf(value));
                                    break;
                                case "numPoints":
                                    lastDataPoint.setNumPoints(Integer.parseInt(value));
                                    break;
                                case "startPosition":
                                    lastDataPoint.setStartPosition(Integer.parseInt(value));
                                    break;
                                case "frequency":
                                    lastDataPoint.setFrequency(Double.parseDouble(value));
                                    break;
                                case "unit":
                                    lastDataPoint.setUnit(value);
                                    break;
                                case "isLittleEndian":
                                    lastDataPoint.setIsLittleEndian(Boolean.parseBoolean(value));
                                    break;
                            }
                        }

                        break;

                    case XMLStreamConstants.CHARACTERS:
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        switch (parser.getLocalName()) {
                            case "sensor":
                                dataPackage.addSensor(lastSensor);
                                lastSensor = new Sensor();
                                break;
                            case "datapoint":
                                lastSensor.addDataPoint(lastDataPoint);
                                lastMetaData.addDataPoint(lastDataPoint);
                                break;
                            case "metadata":
                                header.addMetaData(lastMetaData);
                                lastMetaData = new MetaData();
                                break;
                            case "header":
                                sbd.setHeader(header);
                                break;
                            case "datapackage":
                                sbd.setDataPackage(dataPackage);
                                break;
                        }
                        break;

                    default:
                        break;
                }
                parser.next();
            }
        } catch (XMLStreamException ex) {

        }
        return sbd;
    }
}
