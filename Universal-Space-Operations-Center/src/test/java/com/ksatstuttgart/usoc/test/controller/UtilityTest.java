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
package com.ksatstuttgart.usoc.test.controller;

import com.ksatstuttgart.usoc.controller.Utility;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Valentin Starlinger
 * @version 1.0
 */
public class UtilityTest {
    
    /**
     * Test of binToUInt method, of class Utility.
     */
    @Test
    public void testBinToUInt() {
        System.out.println("binToUInt");
        String binaryString = "1111000010101010";
        int expResult = 43760;
        int result = Utility.binToUInt(binaryString, false);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of switchIntEndian method, of class Utility.
     */
    @Test
    public void testSwitchIntEndian() {
        System.out.println("switchIntEndian");
        String i = "1111000010101010";
        String expResult = "1010101011110000";
        String result = Utility.switchIntEndian(i);
        assertEquals(expResult, result);
    }

    /**
     * Test of binToInt method, of class Utility.
     */
    @Test
    public void testBinToInt() {
        System.out.println("binToInt");
        String binaryString = "00010101";
        int expResult = 21;
        int result = Utility.binToInt(binaryString, false);
        assertEquals(expResult, result);
    }

    /**
     * Test of intToBits method, of class Utility.
     */
    @Test
    public void testIntToBits() {
        System.out.println("intToBits");
        
        //text exceeding
        int b = 1000;
        String expResult = "1111101000";
        String result = Utility.intToBits(b);
        assertEquals(expResult, result);
        
        //test maximum
        b = 255;
        expResult = "11111111";
        result = Utility.intToBits(b);
        assertEquals(expResult, result);
        
        //test normal
        b = 200;
        expResult = "11001000";
        result = Utility.intToBits(b);
        assertEquals(expResult, result);
    }

    /**
     * Test of bytesToBinString method, of class Utility.
     */
    @Test
    public void testBytesToBinString() {
        System.out.println("bytesToBinString");
        String s = "AAA";
        String expResult = "010000010100000101000001";
        String result = Utility.bytesToBinString(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of floatEndianess method, of class Utility.
     */
    @Test
    public void testFloatEndianess() {
        System.out.println("floatEndianess");
        String s = "10101010111100000000111101010101";
        String expResult = "01010101000011111111000010101010";
        String result = Utility.floatEndianess(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of stringToFloat method, of class Utility.
     */
    @Test
    public void testStringToFloat() {
        System.out.println("stringToFloat");
        String binaryString = "10101010111100000000111101010101";
        float expResult = 9.891487940608E12F;
        float result = Utility.stringToFloat(binaryString, false);
        assertEquals(expResult, result, 0.0);
    }
}
