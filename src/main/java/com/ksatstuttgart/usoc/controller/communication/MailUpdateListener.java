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

import com.ksatstuttgart.usoc.data.MailEvent;


/**
* <h1>MailUpdateListener</h1>
* This interface enables classes to listen to mail updates from the Iridium 
* network via the MailReceiver class.
*
* @author  Valentin Starlinger
* @version 1.0
*/
public interface MailUpdateListener {
    /**
     * This is called when a new Iridium message was received. 
     * @param e - MailEvent 
     */
    public abstract void mailUpdated(MailEvent e);
    
    /**
     * This is called if an error concerning the connection to the mail server
     * occurred. 
     * @param msg - String; A message describing the error.
     */
    public abstract void error(String msg);
}
