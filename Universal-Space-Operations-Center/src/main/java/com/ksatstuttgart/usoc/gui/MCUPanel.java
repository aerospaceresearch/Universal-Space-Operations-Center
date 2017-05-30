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

import com.ksatstuttgart.usoc.data.sensors.Mode;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
      
/**
* <h1>MCUPanel</h1>
* This class acts to display information received from the micro controllers used
* on the MIRKA-RX REXUS experiment.
*
* @author  Valentin Starlinger
* @version 1.0
*/
public class MCUPanel extends JPanel{
    
    private final JLabel mode, time, rc, msgnum;
    
    public MCUPanel(int num){
        
        this.setBorder(BorderFactory.createTitledBorder("MCU"+num));
        
        GridLayout gl = new GridLayout(4,2);
        gl.setHgap(5);
        gl.setVgap(5);
        this.setLayout(gl);
        
        this.add(new JLabel("Mode:",SwingConstants.LEFT));
        mode = new JLabel("",SwingConstants.LEFT);
        this.add(mode);
        this.add(new JLabel("Time:",SwingConstants.LEFT));
        time = new JLabel("",SwingConstants.LEFT);
        this.add(time);
        this.add(new JLabel("RC:",SwingConstants.LEFT));
        rc = new JLabel("",SwingConstants.LEFT);
        this.add(rc);
        this.add(new JLabel("Msg#:",SwingConstants.LEFT));
        msgnum = new JLabel("",SwingConstants.LEFT);
        this.add(msgnum);
        
        
    }

    public void setMode(Mode m) {
        this.mode.setText(m.name());
    }

    public void setTime(double time) {
        this.time.setText(time+"");
    }

    public void setRc(int rc) {
        this.rc.setText(rc+"");
    }

    public void setMsgnum(int msgnum) {
        this.msgnum.setText(msgnum+"");
    }
    
    
}
