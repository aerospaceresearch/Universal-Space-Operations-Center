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
package com.ksatstuttgart.usoc.controller;

import com.ksatstuttgart.usoc.data.message.Var;
import org.json.JSONObject;

/**
 * <h1>Util</h1>
 * This class provides Utility methods that are used all over the program
 * <p>
 *
 *
 * @author Valentin Starlinger
 * @version 1.0
 */
public class Utility {

    /**
     * converts a String of 1's and 0's to an unsigned integer
     *
     * @param binString : String - binary String
     * @param isLittleEndian : boolean - TRUE if the UINT value is in little
     * endian.
     * @return unsigned integer
     */
    public static int binToUInt(String binString, boolean isLittleEndian) {
        int num = 0;

        //necessary due to different conversion between MIRKA2-RX microcontrollers 
        //and Java
        if (isLittleEndian) {
            binString = changeEndianess(binString);
        }
        for (int i = 0; i < binString.length(); i++) {
            if (binString.charAt(i) == '1') {
                num += Math.pow(2, binString.length() - (i + 1));
            }
        }

        return num;
    }

    /**
     * converts a binary String to a signed integer.
     *
     * @param binString -
     * @param isLittleEndian -
     * @return
     */
    public static int binToInt(String binString, boolean isLittleEndian) {
        int num = 0;
        if (isLittleEndian) {
            binString = changeEndianess(binString);
        }
        for (int i = 1; i < binString.length(); i++) {
            if (binString.charAt(i) == '1') {
                num += Math.pow(2, binString.length() - (i + 1));
            }
        }

        if (binString.charAt(0) == '1') {
            //using two's complement to get the negative value.
            int max = (int) Math.pow(2, binString.length() - 1);
            return num - max;
        } else {
            return num;
        }
    }

    /**
     * Converts a String of 1's and 0's to an "unsigned" Long
     *
     * @param binString : String - binary String
     * @param isLittleEndian : boolean - TRUE if the UINT value is in little
     * endian.
     * @return a variable of time long
     */
    public static long binToULong(String binString, boolean isLittleEndian) {
        long num = 0;

        //necessary due to different conversion between MIRKA2-RX microcontrollers 
        //and Java
        if (isLittleEndian) {
            binString = changeEndianess(binString);
        }
        for (int i = 0; i < binString.length(); i++) {
            if (binString.charAt(i) == '1') {
                num += Math.pow(2, binString.length() - (i + 1));
            }
        }

        return num;
    }

    /**
     * Converts a binary String into a float value
     *
     * @param binaryString : String
     * @param isLittleEndian
     * @return
     */
    public static float binToFloat(String binaryString, boolean isLittleEndian) {

        //necessary due to different conversion between MIRKA2-RX microcontrollers 
        //and Java
        if (isLittleEndian) {
            binaryString = changeEndianess(binaryString);
        }

        //false because endianess already switched!
        int ii = Utility.binToInt(binaryString, false);
        float f = Float.intBitsToFloat(ii);
        return f;
    }

    /**
     * Changes the endianess of a binary String
     *
     * @param binaryString : String
     * @return A binary String with a changed endianess
     */
    public static String changeEndianess(String binaryString) {
        switch (binaryString.length()) {
            case 16:
                return binaryString.substring(8) + binaryString.substring(0, 8);
            case 32:
                return binaryString.substring(24) + binaryString.substring(16, 24)
                        + binaryString.substring(8, 16) + binaryString.substring(0, 8);
            default:
                //changing endianess not supported for this String length 
                return binaryString;
        }
    }

    /**
     * converts an integer value to a String with 1's and 0's of length 8
     *
     * @param b
     * @return
     */
    public static String intToBits(int b) {
        String s = Integer.toBinaryString(b);
        while (s.length() < 8) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * Converts a String into a String of 1's and 0's using the
     * String.getBytes() method.
     *
     * @param s : String
     * @return A String of 1's and 0's representing the binary values of the
     * input String
     */
    public static String bytesToBinString(String s) {
        String text = "";
        for (byte b : s.getBytes()) {
            text += intToBits(b);
        }
        return text;
    }

    static Object getVariableValue(Var dataPoint, String dataContent) {
        switch (dataPoint.getDataType()) {
            case INT8:
            case INT16:
                return Utility.binToInt(dataContent, dataPoint.isLittleEndian());
            case UINT8:
            case UINT16:
                return Utility.binToUInt(dataContent, dataPoint.isLittleEndian());
            case UINT32:
                return Utility.binToULong(dataContent, dataPoint.isLittleEndian());
            //TODO: maybe need to replace with separate methods for FLOAT16 and FLOAT32
            case FLOAT16:
            case FLOAT32:
                return new Double(Utility.binToFloat(dataContent, dataPoint.isLittleEndian()));
            case BIT:
            case BIT3:
            case BIT10:
                return Utility.binToUInt(dataContent, false);
            case STRING:
                return dataContent;
        }

        //TODO: replace with throw new DataTypeNotSupportedException();
        return null;
    }
    
    public static int byteToByte(int[] a){
        int rb = 0;
        for (int i = 0; i < 8; i++) {
            rb = rb | (a[i] == 0 ? 0 : (int)Math.pow(2, 7-i)); 
        }
        return rb;
    }
    
    public static int nibblesToByte(int[] a, int[] b){
        int rb = 0;
        for (int i = 0; i < b.length; i++) {
            rb = rb | (b[i] == 0 ? 0 : (int)Math.pow(2, 3-i)); 
        }
        for (int i = 0; i < a.length; i++) {
            rb = rb | (a[i] == 0 ? 0 : (int)Math.pow(2, 7-i));
        }
        return rb;
    }

    public static Object getVarFromJSON(Var var, JSONObject jsonObject) {
        if(!jsonObject.has(var.getDataName())){
            return null;
        }
        Object o = null;
        
        switch(var.getDataType()){
            case BOOLEAN:
                try{
                    o = jsonObject.getBoolean(var.getDataName());
                }catch(Exception e){
                    o = jsonObject.getInt(var.getDataName()) == 1;
                }
                break;
            case INTEGER:
                o = jsonObject.getInt(var.getDataName());
                break;
            case FLOAT:
                o = jsonObject.getFloat(var.getDataName());
                break;
        }
        
        return o;
    }
}
