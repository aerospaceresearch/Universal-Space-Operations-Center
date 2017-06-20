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
import com.ksatstuttgart.usoc.data.message.SBD340;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.data.message.header.MetaData;
import com.ksatstuttgart.usoc.data.message.header.MetaDataType;

/**
 * This class is as a controller for the messages received via the Iridium
 * communication interface.
 *
 * This class offers a parser for those messages using the message structure
 * defined in the SBD340.
 *
 * @author Valentin Starlinger
 */
public class MessageController {

    private final SBD340 data;

    /**
     * Creates a new MessageController instance from a predefined message
     * structure.
     *
     * This message structure can be generated from an XML file using the
     * XMLReader class. The current version of the software provides a default
     * file that can be found at protocols/defaultProtocol.xml and has the
     * structure of the messages used in the MIRKA2-ICV experiment.
     *
     * @param messageStructure - the message structure to be used when parsing
     * the incoming data.
     */
    public MessageController(SBD340 messageStructure) {
        this.data = new SBD340(messageStructure);
    }

    /**
     * Adds and parses a new Message received via the Iridium communication
     * link.
     *
     * @param s - A String received via the Iridium satellite link containing
     * the data as described in the message structure.
     */
    public void addSBD340Message(String s) {
        //TODO: add validation
        parseContent(s);
    }

    /**
     * This parses the data received via the Iridium satellite link and puts it
     * into a SBD340 for easy access.
     *
     * @param content - A String received via the Iridium satellite link
     * containing the data as described in the message structure.
     */
    private void parseContent(String content) {
        //use messageStructure message to parse and save the messages.

        //only sequential message type supported at the moment!
        switch (data.getProtocol()) {
            case SEQUENTIAL:
                //parse the message using a String that represents the bit values
                //of the content with 1's and 0's 
                parseSequential(content);
            case BISECTION: //not supported yet
        }

        //throw new ProtocolNotSupportedException(messageStructure.getProtocol());
    }

    /**
     * This method parses the data of an Iridium Message that is using the
     * SEQUENTIAL protocol type and saves the data in the respective Var objects
     *
     * @param content - A String representing the data received via the Iridium
     * communication link with 1's and 0's
     */
    private void parseSequential(String content) {

        long time = -1;

        //get time metadata
        for (MetaData metaData : this.data.getHeader().getMetaData()) {
            //get time value and parse datapoints for other metadata
            if (metaData.getType() == MetaDataType.TIME) {
                //Time values must always contain only one variable and one data
                //point
                //time values must also always be in type Long/UINT32
                if (metaData.getVars().isEmpty()) {
                    System.err.println("EMPTY TIME!!!!");
                    continue;
                }
                Var v = metaData.getVars().get(0);

                int start = v.getStartPosition() + 1;
                int end = start + v.getDataType().getLength();

                time = (long) Utility.getVariableValue(v, content.substring(start, end));
            } else {
                //for other metadata save the messages with the corresponding time.
            }
        }
        //TODO: get other metadata

        for (Sensor sensor : this.data.getData().getSensors()) {
            for (Var var : sensor.getVars()) {

                int start, end = 0;
                long timeStamp;
                for (int i = 0; i < var.getNumPoints(); i++) {

                    //check if first datapoint
                    if (i == 0) {
                        //starting position of the relevant point
                        timeStamp = time;
                        start = var.getStartPosition() + 1;
                    } else {
                        //if the structure represents more than one data value, the others
                        //are located behind when using the sequential protocol type
                        start = end;
                        //if more than one data value is added then calculate 
                        //the next time values using the vars frequency value
                        //the value must be multiplied by 1000 as the time 
                        //value is represented in milliseconds.
                        timeStamp = (long) (time + i * (1 / var.getFrequency()) * 1000);
                    }
                    end = start + var.getDataType().getLength();

                    //get the value of the variable and store it in its HashMap
                    Object value = Utility.getVariableValue(var, content.substring(start, end));
                    var.addValue(timeStamp, value);
                }
            }
        }
    }

    /**
     * Returns the desired Sensor
     *
     * @param sensorName - sensor that should be returned, note that senor names
     * are supposed to be unique so only one sensor will be returned.
     * @return the sensor matching the sensorName
     */
    public Sensor getSensorByName(String sensorName) {
        if (sensorName == null) {
            //throw InvalidSensorNameException
            return null;
        }
        for (Sensor sensor : data.getData().getSensors()) {
            if (sensorName.equals(sensor.getSensorName())) {
                return sensor;
            }
        }
        //throw InvalidSensorNameException
        return null;
    }

    public SBD340 getData() {
        return data;
    }
}
