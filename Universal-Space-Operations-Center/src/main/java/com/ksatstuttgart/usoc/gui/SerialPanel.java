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
import com.ksatstuttgart.usoc.data.SerialEvent;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ksatstuttgart.usoc.controller.communication.SerialComm;

/**
* <h1>SerialPanel</h1>
* This class creates a GUI to control the Serial communication and to send 
* commands to the experiment using the serial communication port. 
*
* @author  Valentin Starlinger
* @version 1.0
*/

public class SerialPanel extends JPanel {

    private final JComboBox commandBox;
    private final JButton send, connect;
    private final LogPanel lp;
    private final JComboBox ports, baudrate;
    
    private static final String[] baudrates = {"38400","57600"};
    private static final String[] commands = {"Ping","Testing","Camtest","Cameras","Abort","Prelaunch","Active","Reset","LaunchMacro"};
    

    public SerialPanel() {
        commandBox = new JComboBox(commands);
        send = new JButton("Send Command");
        send.addActionListener(new SendListener());
        ports = new JComboBox();
        baudrate = new JComboBox(baudrates);
        connect = new JButton("Connect");
        connect.addActionListener(new ConnectListener());
        
        JPanel cp = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        cp.setLayout(new GridLayout(3,2));
        
        cp.add(baudrate);
        cp.add(new JLabel());
        cp.add(ports);
        cp.add(connect);
        cp.add(commandBox);
        cp.add(send);
        
        lp = new SerialLogPanel();
        
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setLayout(new BorderLayout(5,5));
        this.add(cp,BorderLayout.NORTH);
        this.add(lp, BorderLayout.CENTER);
        this.setPreferredSize(new java.awt.Dimension(0, 250));

    }

    public void updateData(SerialEvent e) {
        lp.updateData(e.getMsg(), e.getTimeStamp());
    }
    
    public void updateError(String msg){
        lp.updateError(msg);
    }
    
    public String getPort() {
        return ((String)ports.getSelectedItem());
    }
    
    public void updatePortList(ArrayList<String> ports){
        for (String port : ports) {
            if(!portsContains(port)){
                this.ports.addItem(port);
            }
        }
        for (int i = 0; i < this.ports.getItemCount(); i++) {
            boolean contains = false;
            for (String port : ports) {
                if(port.equals(this.ports.getItemAt(i))){
                    contains = true;
                }
            }
            if(!contains){
                this.ports.removeItemAt(i);
            }
        }
    }
    
    private boolean portsContains(String s){
        for (int i = 0; i < ports.getItemCount(); i++) {
            if(((String)ports.getItemAt(i)).equals(s)){
                return true;
            }
        }
        return false;
    }

    public int getBaudrate() {
        return Integer.parseInt((String)baudrate.getSelectedItem());
    }
    
    private class SendListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            ActionEvent ae = new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, (String)commandBox.getSelectedItem());
            SerialComm.sendAction(ae);
        }
        
    }
    
    private class ConnectListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            SerialComm.getInstance().start((String)ports.getSelectedItem(), getBaudrate());
        }
        
    }
}
