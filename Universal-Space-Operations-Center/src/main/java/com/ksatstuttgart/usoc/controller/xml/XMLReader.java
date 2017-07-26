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

import com.ksatstuttgart.usoc.data.message.dataPackage.Data;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.ProtocolType;
import com.ksatstuttgart.usoc.data.message.SBD340;
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
 * TODO: add validation of XML file e.g. vars with numpoints > 1 must contain 
 * a frequency value as well.
 * 
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

    public SBD340 getMessageStructure(String filename) {
        InputStream file;
        try {
            file = new FileInputStream(filename);
            return stax(file);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return new SBD340();
    }

    public SBD340 stax(InputStream file) {
        SBD340 sbd = new SBD340();
        Data dataPackage = new Data();
        Header header = new Header();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader parser = factory.createXMLStreamReader(file);

            Sensor lastSensor = new Sensor();
            Var lastVariable = new Var();
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
//                        System.out.println("START_ELEMENT: " + parser.getLocalName());
                        switch (parser.getLocalName()) {
                            case "sensor":
                                lastSensor = new Sensor();
                                break;
                            case "metadata":
                                lastMetaData = new MetaData();
                                break;
                            case "var":
                                lastVariable = new Var();
                                break;
                        }
                        
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String value = parser.getAttributeValue(i);
//                            System.out.println("  Attribut: "
//                                    + parser.getAttributeLocalName(i)
//                                    + " Wert: " + parser.getAttributeValue(i));
                            switch (parser.getAttributeLocalName(i)) {
                                case "protocol":
                                    sbd.setProtocol(ProtocolType.valueOf(value));
                                    break;
                                case "name":
                                    lastMetaData.setMetaDataName(value);
                                    lastSensor.setSensorName(value);
                                    break;
                                case "type":
                                    //two tries are needed otherwise one value would
                                    //never be set as the other .valueOf would
                                    //trigger the exception.
                                    try{
                                        lastSensor.setType(SensorType.valueOf(value));
                                    } catch(IllegalArgumentException e){
                                        //do nothing (one of the two will always throw)
                                    }
                                    try{
                                        lastMetaData.setType(MetaDataType.valueOf(value));
                                    } catch(IllegalArgumentException e){
                                        //do nothing (one of the two will always throw)
                                    }
                                    break;
                                case "start":
                                    lastVariable.setStartPosition(Integer.parseInt(value));
                                    break;
                                case "dataname":
                                    lastVariable.setDataName(value);
                                    break;
                                case "datatype":
                                    lastVariable.setDataType(DataType.valueOf(value));
                                    break;
                                case "numpoints":
                                    lastVariable.setNumPoints(Integer.parseInt(value));
                                    break;
                                case "frequency":
                                    lastVariable.setFrequency(Double.parseDouble(value));
                                    break;
                                case "sensorpoints":
                                    lastSensor.setNumPoints(Integer.parseInt(value));
                                    break;
                                case "sensorfrequency":
                                    lastSensor.setFrequency(Double.parseDouble(value));
                                    break;
                                case "unit":
                                    lastVariable.setUnit(value);
                                    break;
                                case "isLittleEndian":
                                    lastVariable.setIsLittleEndian(Boolean.parseBoolean(value));
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
                            case "var":
                                lastSensor.addVariable(lastVariable);
                                lastMetaData.addDataPoint(lastVariable);
                                break;
                            case "metadata":
                                header.addMetaData(lastMetaData);
                                lastMetaData = new MetaData();
                                break;
                            case "header":
                                sbd.setHeader(header);
                                break;
                            case "data":
                                sbd.setData(dataPackage);
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
