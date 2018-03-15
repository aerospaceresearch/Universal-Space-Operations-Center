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
package com.ksatstuttgart.usoc.data.message.dataPackage;

/**
 * This enum shows the different data types that can occur in the Iridium message.
 * The length of the DataType is used to determine what part of the message needs
 * to be parsed. 
 * The length attribute is used in the MessageController and Utility classes to
 * parse the data in the messages.
 * 
 * @author Valentin Starlinger
 */
public enum DataType {
    STRING (0),
    FLOAT16 (16),
    FLOAT32 (32),
    UINT8 (8),
    UINT16 (16),
    UINT32 (32),
    INT8 (8),
    INT16 (16),
    BIT (1),
    BIT3 (3),
    BIT10 (10),
    BOOLEAN (1),
    INTEGER (32),
    FLOAT (32);
    
    
    private final int length;
    private DataType(int length){
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
