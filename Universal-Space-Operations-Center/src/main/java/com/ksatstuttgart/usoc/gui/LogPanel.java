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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

      
/**
* <h1>LogPanel</h1>
* This class provides a superclass for displaying log information.
*
* @author  Valentin Starlinger
* @version 1.0
*/
public abstract class LogPanel extends JPanel{
    
    private JTextArea jta;
    private JLabel lastReceivedLabel, statusLabel;
    private long lastReceived = -1;
    
    private static final Color OK = new Color(2, 100, 64);
    private static final Color ERROR = new Color(224, 5, 32);
    
    public LogPanel (){
        JScrollPane jsp = new JScrollPane();
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jta = new JTextArea();
        jsp.setViewportView(jta);
        
        lastReceivedLabel = new JLabel();
        updateLastReceived();
        statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        updateStatusPanel(false);
        JPanel sp = new JPanel();
        sp.setLayout(new GridLayout(1,2));
        
        sp.add(lastReceivedLabel);
        sp.add(statusLabel);
        
        this.setLayout(new BorderLayout(5,5));
        this.add(jsp,BorderLayout.CENTER);
        this.add(sp,BorderLayout.SOUTH);
        this.setPreferredSize(new java.awt.Dimension(0,250));
        
    }
    
    private void updateLastReceived(){
        String s = "Last Data received at: ";
        if(lastReceived == -1){
            s+="-";
        }else{
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(lastReceived);
            s+=c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        }
        
        lastReceivedLabel.setText(s);
    }
    
    protected void updateStatusPanel(boolean statusOK){
        String s = "";
        
       // System.out.println("status: "+statusOK);
        
        if(statusOK){
            s+="CONNECTED";
            statusLabel.setForeground(OK);
        }else{
            s+="NOT CONNECTED";
            statusLabel.setForeground(ERROR);
        }
        
        statusLabel.setText(s);
    }

    public void updateData(String s, long timeStamp) {
        jta.setText(jta.getText() + s);
        jta.setCaretPosition(jta.getText().length());
        lastReceived = timeStamp;
        updateLastReceived();
    }

    public void updateError(String msg) {
        jta.setText(jta.getText() + msg);
        jta.setCaretPosition(jta.getText().length());
    }
    
    public abstract void updateStatus();
}
