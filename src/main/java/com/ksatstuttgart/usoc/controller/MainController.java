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
package com.ksatstuttgart.usoc.controller;

import com.ksatstuttgart.usoc.controller.communication.MailReceiver;
import com.ksatstuttgart.usoc.controller.communication.MailUpdateListener;
import com.ksatstuttgart.usoc.controller.communication.SerialComm;
import com.ksatstuttgart.usoc.controller.communication.SerialListener;
import com.ksatstuttgart.usoc.controller.xml.XMLReader;
import com.ksatstuttgart.usoc.data.DataSource;
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.MailEvent;
import com.ksatstuttgart.usoc.data.SerialEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import com.ksatstuttgart.usoc.data.message.SBD340;
import com.ksatstuttgart.usoc.gui.controller.SerialPanelController;
import com.ksatstuttgart.usoc.gui.setup.configuration.Layout;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Controller class
 *
 * @author valentinstarlinger
 */
public class MainController {

    /**
     * Instance of Main Controller
     */
    private static MainController instance;

    /**
     * Message Controller
     */
    private final MessageController messageController;

    /**
     * Current stage
     */
    private Stage stage;

    /**
     * POJO Class that stores all window properties
     */
    private Layout layout;

    /**
     * List of Data Listeners
     */
    private ArrayList<DataUpdateListener> listeners;

    /**
     * Creates an instance of MainController if it doesnt exist
     *
     * @return existing instance
     */
    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    /**
     * Instantiates all needed components
     */
    public MainController() {
        listeners = new ArrayList<>();
        layout = new Layout();

        // Loads defaultProtocol by default
        // Can be changed on runtime in the UI
        SBD340 structure =
                loadProtocol("protocols/defaultProtocol.xml");

        messageController = new MessageController(structure);

        MailReceiver.getInstance().addMailUpdateListener(new MailListener());
        SerialComm.getInstance().addSerialListener(new RXListener());
    }

    /**
     * Loads a given protocol
     *
     * @param protocol
     * @return
     */
    public SBD340 loadProtocol(String protocol) {
        return XMLReader.getInstance()
                .getMessageStructure(protocol);
    }

    /**
     * Gets Configuration class
     *
     * @return
     */
    public Layout getLayout() {
        return layout;
    }

    /**
     * Sets new Configuration Class
     *
     * @param layout
     */
    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    /**
     * Sets a new Stage
     *
     * @param stage new stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Gets current stage
     *
     * @return current stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Gets Message Controller
     *
     * @return current Message Controller
     */
    public MessageController getMessageController() {
        return this.messageController;
    }

    /**
     * Adds an Update Listener do the current list
     *
     * @param listener new Update Listener
     */
    public void addDataUpdateListener(DataUpdateListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a given Update Listener
     *
     * @param listener Update Listener to be removed
     */
    public void removeDataUpdateListener(DataUpdateListener listener) {
        listeners.remove(listener);
    }

    /**
     * Iterates all Data Update Listeners and updates them
     *
     * @param e
     */
    public void updateListeners(USOCEvent e) {
        for (DataUpdateListener dataUpdateListener : listeners) {
            dataUpdateListener.update(messageController, e);
        }
    }

    /**
     * Exports data to CSV
     */
    public void exportCSV() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Export .csv");

        File selectedFile = fc.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                ExportController.saveDataAsCSV(messageController.getData(), selectedFile, false);
            } catch (IOException ex) {
                System.out.println("something wrong happend when saving the file");
            }
        }
    }

    /**
     * Reads a given binary file
     */
    public void openBinaryFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open binary file");

        List<File> files = fc.showOpenMultipleDialog(stage);

        if (files != null) {
            for (File file : files) {
                addBinaryFile(file);
            }
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
            System.out.println("something wrong happened when adding the file");
        }
    }

    /**
     * Creates a thread that updates the Serial Port list every
     * 500 ms
     *
     * @param sp SerialPanelController
     */
    public static void startPortThread(final SerialPanelController sp) {
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

    /**
     * Clears all data from Message Controller
     * and updates Listeners
     */
    public void clearData() {
        messageController.clearData();
        updateListeners(new USOCEvent(DataSource.ALL));
    }

    private class RXListener implements SerialListener {
        String buffer = "";

        @Override
        public void messageReceived(final SerialEvent e) {
            MainController.getInstance().getMessageController().addSBD340Message(e.getMsg());
            MainController.getInstance().updateListeners(e);
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
