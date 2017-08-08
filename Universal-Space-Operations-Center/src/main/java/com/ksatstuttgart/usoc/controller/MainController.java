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
import com.ksatstuttgart.usoc.data.DataSource;
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.MailEvent;
import com.ksatstuttgart.usoc.data.SerialEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import com.ksatstuttgart.usoc.data.message.SBD340;
import com.ksatstuttgart.usoc.gui.SerialPanel;
import com.ksatstuttgart.usoc.gui.controller.LogController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import static java.lang.Thread.sleep;

/**
 *
 * @author valentinstarlinger
 */
public class MainController {

    private final MessageController messageController;

    private static MainController instance;

    private ArrayList<DataUpdateListener> listeners;

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public MainController() {
        //TODO: remove this and somehow integrate this in the GUI or find better 
        //place. This loads the xml structure for reading the messages received
        //via the Iridium communication link
        listeners = new ArrayList<>();

        SBD340 structure = XMLReader.getInstance()
                .getMessageStructure("protocols/USOC_SBD340_ICV.xml");
        messageController = new MessageController(structure);

        MailReceiver.getInstance().addMailUpdateListener(new MailListener());
        SerialComm.getInstance().addSerialListener(new RXListener());
    }

    public MessageController getMessageController() {
        return this.messageController;
    }

    public void addDataUpdateListener(DataUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeDataUpdateListener(DataUpdateListener listener) {
        listeners.remove(listener);
    }

    public void updateListeners(USOCEvent e) {
        for (DataUpdateListener dataUpdateListener : listeners) {
            dataUpdateListener.update(messageController, e);
        }
    }

    public void exportCSV() {
        JFileChooser jf = new JFileChooser();
        int returnVal = jf.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                ExportController.saveDataAsCSV(messageController.getData(), jf.getSelectedFile(), false);
            } catch (IOException ex) {
                System.out.println("something wrong happend when saving the file");
            }
        }
    }

    public void openBinaryFile() {
        JFileChooser jf = new JFileChooser();
        int returnVal = jf.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            addBinaryFile(jf.getSelectedFile());
        }
    }

    public void addBinaryFile(File file) {
        InputStream stream;
        try {
            stream = new FileInputStream(file);
            String text = "";
            int b;
            while ((b = stream.read()) != -1) {
                //System.out.println(counter + ": " + b);
                String t = Utility.intToBits(b);
                //System.out.println(t);
                text += t;
            }
            text = text.trim();
            messageController.addSBD340Message(text);
            updateListeners(new USOCEvent(DataSource.FILE));
        } catch (IOException ex) {
            System.out.println("something wrong happend when adding the file");
        }
    }

    public static void startPortThread(final SerialPanel sp) {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sp.updatePortList(SerialComm.getInstance().getPorts());
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }.start();
    }
    
    public static void startPortThread(final LogController sp) {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sp.updatePortList(SerialComm.getInstance().getPorts());
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };
        t.setDaemon(true);
        t.start();
    }

    public void clearData() {
        messageController.clearData();
        updateListeners(new USOCEvent(DataSource.ALL));
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
                            MainController.getInstance().updateListeners(
                                    new SerialEvent("Received erroneous Iridium data\n",
                                            e.getPort(), e.getTimeStamp(), DataSource.SERIAL));
                        }
                    } catch (InterruptedException ex) {
                    }
                }
            }.start();
        }

        @Override
        public void error(String msg) {
            MainController.getInstance().updateListeners(new ErrorEvent(msg, DataSource.SERIAL));
            LogSaver.saveDownlink(msg, false);
        }

    }

    private class MailListener implements MailUpdateListener {

        @Override
        public void mailUpdated(MailEvent e) {
            //System.out.println("mail updated");
            MainController.getInstance().getMessageController().addSBD340Message(e.getText());
            MainController.getInstance().updateListeners(e);
            LogSaver.saveIridium(e.toString());
        }

        @Override
        public void error(String msg) {
            MainController.getInstance().updateListeners(new ErrorEvent(msg, DataSource.MAIL));
            LogSaver.saveIridium(msg);
        }

    }
}
