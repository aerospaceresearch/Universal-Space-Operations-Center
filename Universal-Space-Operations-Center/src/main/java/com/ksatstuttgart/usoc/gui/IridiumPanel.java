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
package com.ksatstuttgart.usoc.gui;

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.data.MailEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.mail.Address;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ksatstuttgart.usoc.controller.communication.MailReceiver;
import com.ksatstuttgart.usoc.data.DataSource;
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import javax.swing.JTextField;

/**
 * <h1>IridiumPanel</h1>
 * This GUI class allows to control the MailReceiver for the Iridium messages
 * and also shows information about the received messages.
 *
 * @author Valentin Starlinger
 * @version 1.0
 */
public class IridiumPanel extends DataPanel {

    private final LogPanel lp;
    private final JLabel lastSubject, lastFrom, lastFilename, lastTimestamp;
    private final JButton reconnect, openButton, clearData, exportCSV;
    private JTextField lastMessages;

    public IridiumPanel() {
        super();

        lp = new IridiumLogPanel();

        JPanel infoPanel = new JPanel();

        infoPanel.setLayout(new GridLayout(8, 2));

        JLabel from = new JLabel("Last Message from: ");
        lastFrom = new JLabel();
        infoPanel.add(from);
        infoPanel.add(lastFrom);

        JLabel subject = new JLabel("Subject: ");
        lastSubject = new JLabel();
        infoPanel.add(subject);
        infoPanel.add(lastSubject);

        JLabel filename = new JLabel("Filename: ");
        lastFilename = new JLabel();
        infoPanel.add(filename);
        infoPanel.add(lastFilename);

        JLabel received = new JLabel("Received At: ");
        lastTimestamp = new JLabel();
        infoPanel.add(received);
        infoPanel.add(lastTimestamp);

        JLabel addFile = new JLabel("Add File: ");
        openButton = new JButton("Open");
        openButton.addActionListener(new AddFileListener());
        infoPanel.add(addFile);
        infoPanel.add(openButton);

        clearData = new JButton("Clear Data");
        clearData.addActionListener(new ClearDataListener());
        infoPanel.add(clearData);
        infoPanel.add(new JLabel());

        infoPanel.add(new JLabel("#Messages on reconnect: "));
        lastMessages = new JTextField("0");
        lastMessages.addActionListener(new MessageNumberListener());
        infoPanel.add(lastMessages);

        exportCSV = new JButton("exportCSV");
        exportCSV.addActionListener(new ExportCSVListener());
        infoPanel.add(exportCSV);

        reconnect = new JButton("Reconnect");
        reconnect.addActionListener(new ReconnectListener());
        infoPanel.add(reconnect);

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setLayout(new BorderLayout(5, 5));
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(lp, BorderLayout.CENTER);

    }

    @Override
    public void updateData(MessageController mc, USOCEvent ue) {
        if (ue instanceof MailEvent) {
            MailEvent e = (MailEvent) ue;
            String s = "";
            for (Address from : e.getFrom()) {
                s += "," + from.toString();
            }
            lastFrom.setText(s.substring(1));
            lastSubject.setText(e.getSubject());
            lastFilename.setText(e.getFilename());
            lastTimestamp.setText(new Date(e.getTimeStampGmail()).toString());

            lp.updateData(mc.getData().toString(), e.getTimeStamp());
        } else if (ue instanceof ErrorEvent && DataSource.MAIL == ue.getDataSource()){
            lp.updateError(((ErrorEvent)ue).getErrorMessage());
        }
    }

    private class ReconnectListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Status is: "+mr.isConnected());
            MailReceiver.getInstance().reconnect();
            //System.out.println("Status is: "+mr.isConnected());
        }

    }

    private class AddFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MainController.getInstance().addIridiumFile();
        }

    }

    private class ClearDataListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MainController.getInstance().clearData();
        }

    }

    private class ExportCSVListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MainController.getInstance().exportCSV();
        }

    }

    private class MessageNumberListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int number = Integer.parseInt(lastMessages.getText());
                //MailReceiver.getInstance().setReconnectMessageNumber(number);
            } catch (Exception ex) {
                //do nothing
                System.out.println("parsing error");
            }
        }

    }
}
