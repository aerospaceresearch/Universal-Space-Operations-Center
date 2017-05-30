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
import com.ksatstuttgart.usoc.controller.MailReceiver;

      
/**
* <h1>IridiumPanel</h1>
* This GUI class allows to control the MailReceiver for the Iridium messages 
* and also shows information about the received messages.
*
* @author  Valentin Starlinger
* @version 1.0
*/
public class IridiumPanel extends JPanel {

    private final LogPanel lp;
    private final JLabel lastSubject, lastFrom, lastFilename, lastTimestamp;
    private final JButton reconnect;

    public IridiumPanel() {

        lp = new IridiumLogPanel();

        JPanel infoPanel = new JPanel();

        infoPanel.setLayout(new GridLayout(5, 2));

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
        
        reconnect = new JButton("Reconnect");
        reconnect.addActionListener(new ReconnectListener());
        infoPanel.add(new JLabel());
        infoPanel.add(reconnect);

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setLayout(new BorderLayout(5,5));
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(lp, BorderLayout.CENTER);

    }

    public void updateData(MailEvent e) {
        String s = "";
        for (Address from : e.getFrom()) {
            s += "," + from.toString();
        }
        lastFrom.setText(s.substring(1));
        lastSubject.setText(e.getSubject());
        lastFilename.setText(e.getFilename());
        lastTimestamp.setText(new Date(e.getTimeStampGmail()).toString());
        
        lp.updateData(e.getDataController().toString(), e.getTimeStamp());
    }

    public void updateError(String msg) {
        lp.updateError(msg);
    }

    private class ReconnectListener implements ActionListener {

        @Override 
        public void actionPerformed(ActionEvent e){
            //System.out.println("Status is: "+mr.isConnected());
            MailReceiver.getInstance().reconnect();
            //System.out.println("Status is: "+mr.isConnected());
        }
        
    }
}
