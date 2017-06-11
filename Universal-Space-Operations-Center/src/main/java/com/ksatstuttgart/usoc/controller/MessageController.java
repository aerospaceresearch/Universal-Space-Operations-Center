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

import com.ksatstuttgart.usoc.data.message.DataPoint;
import com.ksatstuttgart.usoc.data.message.SBD340Message;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import com.ksatstuttgart.usoc.data.message.header.MetaData;
import java.util.ArrayList;

/**
 * This class is as a controller for the messages received via the Iridium 
 * communication interface.
 * 
 * This class offers a parser for those messages using the message structure 
 * defined in the SBD340Message.
 * 
 * @author Valentin Starlinger
 */
public class MessageController {

    private SBD340Message messageStructure;

    private final ArrayList<SBD340Message> messages;

    /**
     * Creates a new MessageController instance from a predefined message
     * structure. 
     * 
     * This message structure can be generated from an XML file
     * using the XMLReader class. The current version of the software provides 
     * a default file that can be found at protocols/defaultProtocol.xml and 
     * has the structure of the messages used in the MIRKA2-ICV experiment.
     * 
     * @param messageStructure - the message structure to be used when parsing
     * the incoming data.
     */
    public MessageController(SBD340Message messageStructure) {
        this.messageStructure = messageStructure;

        messages = new ArrayList<>();
    }

    /**
     * Allows to set a new message structure. The structure of the previously 
     * received data will not be changed.
     * 
     * @param messageStructure - the message structure to be used when parsing 
     * the incoming data.
     */
    public void setMessageStructure(SBD340Message messageStructure) {
        this.messageStructure = messageStructure;
    }

    /**
     * Returns the currently used message structure.
     * @return The currently used message structure.
     */
    public SBD340Message getMessageStructure() {
        return messageStructure;
    }

    /**
     * Adds and parses a new Message received via the Iridium communication link.
     * 
     * @param s - A String received via the Iridium satellite link containing the 
     * data as described in the message structure.
     */
    public void addSBD340Message(String s) {
        //TODO: add validation
        messages.add(parseContent(s));
    }

    /**
     * This parses the data received via the Iridium satellite link and puts it
     * into a SBD340Message for easy access. 
     * 
     * @param content - A String received via the Iridium satellite link containing the 
     * data as described in the message structure.
     * @return A SBD340Message with fully parsed data meaning that all value fields
     * in the respective data points are filled.
     */
    private SBD340Message parseContent(String content) {
        //use messageStructure message to parse and save the messages.

        //only sequential message type supported at the moment!
        switch (messageStructure.getProtocol()) {
            case SEQUENTIAL:
                //parse the message using a String that represents the bit values
                //of the content with 1's and 0's 
                return parseSequential(Utility.bytesToBinString(content));
            case BISECTION: //not supported yet
        }

        //replace with 
        //throw new ProtocolNotSupportedException(messageStructure.getProtocol());
        return null;
    }

    /**
     * This method parses the data of an Iridium Message that is using the 
     * SEQUENTIAL protocol type.
     * 
     * @param content - A String representing the data received via the Iridium 
     * communication link with 1's and 0's 
     * @return A SBD340Message with fully parsed data meaning that all value fields
     * in the respective data points are filled.
     */
    private SBD340Message parseSequential(String content) {
        SBD340Message message = new SBD340Message();

        for (MetaData metaData : this.messageStructure.getHeader().getMetaData()) {
            //copy meta data structure into a temporary object
            MetaData tmp = new MetaData(metaData);
            tmp.setDatapoints(parseDataPoints(content, metaData.getDatapoints()));
            message.getHeader().addMetaData(tmp);
        }

        for (Sensor sensor : this.messageStructure.getDataPackage().getSensors()) {
            //copy the sensor structure into a temporary object
            Sensor tmp = new Sensor(sensor);
            tmp.setDatapoints(parseDataPoints(content, sensor.getDatapoints()));
            message.getDataPackage().addSensor(tmp);
        }

        return message;
    }

    /**
     * Parses the data point from the Iridium message and saves the data in the
     * DataPoint value field.
     * 
     * @param content - The message the data should be parsed from.
     * @param dataPoints - The structure of the data points that should be parsed.
     * @return The list of data points that was provided as structure but with all
     * the value fields filled.
     */
    private ArrayList<DataPoint> parseDataPoints(String content, ArrayList<DataPoint> dataPoints) {
        ArrayList<DataPoint> tmpPoints = new ArrayList<>();
        for (DataPoint datapoint : dataPoints) {
            int start, end = 0;
            for (int i = 0; i < datapoint.getNumPoints(); i++) {
                //copy the datapoint from the messagestructure
                DataPoint tmpPoint = new DataPoint(datapoint);
                
                //check if first datapoint
                if (i == 0) {
                    //starting position of the relevant point
                    start = tmpPoint.getStartPosition();
                } else {
                    //if the structure represents more than one datapoint, the others
                    //are located behind when using the sequential protocol type
                    start = end;
                }
                end = start + tmpPoint.getDataType().getLength();

                tmpPoint.setValue(Utility.getDataPointValue(tmpPoint, content.substring(start, end)));
                tmpPoints.add(tmpPoint);
            }
        }
        return tmpPoints;
    }
    
    /**
     * Returns the message at the selected position.
     * @param i - the position of the message to be returned.
     * @return The message at the selected position.
     */
    public SBD340Message getMessageAtPos(int i) {
        return messages.get(i);
    }
    
    /**
     * Returns the latest message that was the last to be added to the message 
     * controller
     * @return The latest message that was the last to be added to the message 
     * controller
     */
    public SBD340Message getLastMessage() {
        return messages.get(messages.size()-1);
    }

    /**
     * Returns the number of messages currently stored by the MessageController.
     * @return The number of messages currently stored by the MessageController.
     */
    public int getNumMessages() {
        return messages.size();
    }
}
