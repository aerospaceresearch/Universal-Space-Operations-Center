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

import com.ksatstuttgart.usoc.controller.SerialComm;
      
/**
* <h1>SerialLogPanel</h1>
* This class allows to display everything that is received via the SerialComm
* in a log style fashion.
*
* @author  Valentin Starlinger
* @version 1.0
*/
public class SerialLogPanel extends LogPanel{
    
    public boolean run = true;
    
    public SerialLogPanel(){
        updateStatus();
    }
    
    @Override
    public void updateStatus() {
        new Thread(){
            
            private boolean lastStat = false;
            
            @Override
            public void run(){
            
                while (run){
                    try {
                        if(SerialComm.getInstance().isOpen()!=lastStat){
                            lastStat = SerialComm.getInstance().isOpen();
                            SerialLogPanel.this.updateStatusPanel(SerialComm.getInstance().isOpen());
                        }
                        sleep(500);
                    } catch (InterruptedException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
                
            }
            
        }.start();
    }
    
}
