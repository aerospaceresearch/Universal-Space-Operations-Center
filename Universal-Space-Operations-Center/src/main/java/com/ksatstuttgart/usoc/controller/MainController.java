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

import com.ksatstuttgart.usoc.data.MailEvent;
import com.ksatstuttgart.usoc.data.SerialEvent;
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
    
    public MainController(MainFrame frame){
        this.frame = frame;
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

        boolean isIridium = false;

        String buffer = "";

        String ISTART = "IRIDIUM";
        String IEND = "XMESSAGE";

        @Override
        public void messageReceived(final SerialEvent e) {
            //startwort - IRIDIUM : endwort -XMESSAGE
            //System.out.println(e.getMsg());
            //System.out.println("msg received via serial");
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(6000);
                        isIridium = false;
                        if (!buffer.isEmpty()) {
                            LogSaver.saveDownlink("EIMESSAGE" + buffer + "ENDEIMESSAGE", false);
                            buffer = "";
                            frame.updateSerialLog(new SerialEvent("Received erroneous Iridium data\n", e.getPort(), e.getTimeStamp()));

                        }
                    } catch (InterruptedException ex) {
                    }
                }
            }.start();

            if (isIridium) {
                String tmp = buffer + e.getMsg();
                updateIridium(tmp, e);
                if (!buffer.isEmpty()) {
                    buffer = tmp;
                }
            } else { //currently not iridium 

                String msg = e.getMsg();
                if (msg.matches("([\\s\\w]*?(I(R(I(D(I(U(M)?)?)?)?)?)?))|([\\s\\w]*?IRIDIUM[\\s\\w]*)")) {
                    isIridium = true;
                    if (msg.contains(ISTART)) {
                        String[] sa = msg.split(ISTART);

                        if (sa.length == 1) {
                            updateIridium(sa[0], e);
                        } else if (sa.length == 2) {
                            frame.updateSerialLog(new SerialEvent(sa[0], e.getPort(), e.getTimeStamp()));
                            updateIridium(sa[1], e);
                        }
                    } else {
                        buffer = msg;
                    }
                } else {
                    LogSaver.saveDownlink(e.getMsg(), false);
                    frame.updateSerialLog(e);
                }
            }
//
//            if (e.getMsg().contains("IRIDIUM")) {
//                isIridium = true;
//            }
//
//            if (!isIridium) {
//                frame.updateSerial(e);
//            } else {
//                frame.updateSerial(new SerialEvent("Iridium Data received\n", e.getPort(), e.getTimeStamp()));
//            }
//            LogSaver.saveDownlink(e.getMsg(), isIridium);
//            if (e.getMsg().contains("XMESSAGE")) {
//                isIridium = false;
//            }
        }

        private void updateIridium(String s, SerialEvent e) {
            if (s.contains(IEND)) {
                String[] sab = s.split(IEND);

                buffer = "";
                isIridium = false;
                
                //System.out.println(sab[0]);
                //System.out.println("Iridiummessagelength: "+sab[0].length());
                String istring = Utility.bytesToBinString(sab[0]);
                //System.out.println("Bitlength: "+istring.length());
                //System.out.println(istring);
                
                LogSaver.saveDownlink(istring, true);
                DataController ip = new DataController(istring);
                if (ip.isOK()) {
                    frame.updateSerialLog(new SerialEvent("Iridium Data received", e.getPort(), e.getTimeStamp()));
                    frame.updateSerialLog(new SerialEvent(sab[0], e.getPort(), e.getTimeStamp()));
                    frame.updateData(ip);
                } else {
                    frame.updateSerialLog(new SerialEvent("Received erroneous Iridium data", e.getPort(), e.getTimeStamp()));
                    frame.updateSerialLog(new SerialEvent(sab[0], e.getPort(), e.getTimeStamp()));
                }

                if (sab.length == 2) {
                    messageReceived(new SerialEvent(sab[1], e.getPort(), e.getTimeStamp()));
                }
            } else {
                buffer = s;
            }
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
            frame.updateIridiumLog(e);
            frame.updateData(e.getDataController());
            LogSaver.saveIridium(e.toString());
        }

        @Override
        public void error(String msg) {
            frame.updateIridiumError(msg);
            LogSaver.saveIridium(msg);
        }

    }
}
