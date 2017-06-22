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

import com.ksatstuttgart.usoc.controller.communication.SerialComm;
import com.ksatstuttgart.usoc.controller.communication.MailUpdateListener;
import com.ksatstuttgart.usoc.controller.communication.MailReceiver;
import com.ksatstuttgart.usoc.controller.communication.SerialListener;
import com.ksatstuttgart.usoc.controller.xml.XMLReader;
import com.ksatstuttgart.usoc.data.MailEvent;
import com.ksatstuttgart.usoc.data.SerialEvent;
import com.ksatstuttgart.usoc.data.message.SBD340;
import com.ksatstuttgart.usoc.gui.MainFrame;
import com.ksatstuttgart.usoc.gui.SerialPanel;
import java.util.ArrayList;
import java.util.Arrays;
import static java.lang.Thread.sleep;

/**
 *
 * @author valentinstarlinger
 */
public class MainController {

    private final MainFrame frame;
    private final MessageController messageController;
    
    public MainController(MainFrame frame){
        this.frame = frame;
        
        //TODO: remove this and somehow integrate this in the GUI or find better 
        //place. This loads the xml structure for reading the messages received
        //via the Iridium communication link
        SBD340 structure = XMLReader.getInstance()
                .getMessageStructure("protocols/messageProtocol.xml");
        messageController = new MessageController(structure);
        
        MailReceiver.getInstance().addMailUpdateListener(new MailListener());  
        MailReceiver.getInstance().connect();
        SerialComm.getInstance().addSerialListener(new RXListener());
    }
    
    public static void startPortThread(final SerialPanel sp){
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ArrayList<String> ports = new ArrayList<>();
                        ports.addAll(Arrays.asList(jssc.SerialPortList.getPortNames()));
                        sp.updatePortList(ports);
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }.start();
    }
    
    private class RXListener implements SerialListener {

        String buffer = "";

        @Override
        public void messageReceived(final SerialEvent e) {
            
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(6000);
                        if (!buffer.isEmpty()) {
                            LogSaver.saveDownlink("EIMESSAGE" + buffer + "ENDEIMESSAGE", false);
                            buffer = "";
                            frame.updateSerialLog(new SerialEvent("Received erroneous Iridium data\n", e.getPort(), e.getTimeStamp()));

                        }
                    } catch (InterruptedException ex) {
                    }
                }
            }.start();
        }

        @Override
        public void error(String msg) {
            frame.updateSerialError(msg);
            LogSaver.saveDownlink(msg, false);
        }

    }

    private class MailListener implements MailUpdateListener {

        @Override
        public void mailUpdated(MailEvent e) {
            //System.out.println("mail updated");
            messageController.addSBD340Message(e.getText());
            frame.updateIridiumLog(e, messageController);
            frame.updateData(messageController);
            LogSaver.saveIridium(e.toString());
        }

        @Override
        public void error(String msg) {
            frame.updateIridiumError(msg);
            LogSaver.saveIridium(msg);
        }

    }
}
