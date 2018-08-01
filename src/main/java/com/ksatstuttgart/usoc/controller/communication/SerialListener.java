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
package com.ksatstuttgart.usoc.controller.communication;

import com.ksatstuttgart.usoc.data.SerialEvent;

/**
* <h1>SerialListener</h1>
* This interface enables classes to listen to events happening on an open serial 
* port via the SerialComm class.
*
* @author  Valentin Starlinger
* @version 1.0
*/
public interface SerialListener {
    /**
     * This is called when a new message is received via the specified serial port.
     * @param e - SerialEvent
     */
    public abstract void messageReceived(SerialEvent e);
    /**
     * This is called when en error occurred regarding the serial communication.
     * @param msg - String containing a description of the error.
     */
    public abstract void error(String msg);
}
