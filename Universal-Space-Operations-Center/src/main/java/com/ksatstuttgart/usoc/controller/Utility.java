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
package com.ksatstuttgart.usoc.controller;

/**
* <h1>Util</h1>
* This class provides Utility methods that are used all over the program
* <p>
* 
*
* @author  Valentin Starlinger
* @version 1.0
*/

public class Utility {
     
    
    public static int binToUInt(String t){
        int num = 0;
        
        String b = switchIntEndian(t);
        for (int i = 0; i < b.length(); i++) {
            if(b.charAt(i)=='1'){
                num+=Math.pow(2, b.length()-(i+1));
            }
        }
        
        return num;
    }
    
    public static String switchIntEndian(String i){
        return i.substring(8) + i.substring(0, 8);
    }
    
    public static int binToInt(String t){
        int num = 0;
        //System.out.println("bintoint: "+t);
        String b = (t.length()==16) ? switchIntEndian(t) : t;
        //System.out.println("aftercon: "+b);
        for (int i = 1; i < b.length(); i++) {
            if(b.charAt(i)=='1'){
                num+=Math.pow(2, b.length()-(i+1));
            }
        }
        //System.out.println("result: "+(b.charAt(0) == '1' ? num*(-1):num));
        return b.charAt(0) == '1' ? num*(-1):num;
    }
    
    public static String intToBits(int b){
        String s = Integer.toBinaryString(b);
        while(s.length()<8){
            s = "0"+s;
        }
        return s;
    }
    
    public static String bytesToBinString(String s) {
        String text = "";
        for (byte b : s.getBytes()) {
            text+=intToBits(b);
        }
        return text;
    }
    
    public static String floatEndianess(String s){
        
        return s.substring(24)+s.substring(16,24)+s.substring(8, 16)+s.substring(0, 8);
    }
    
    public static float stringToFloat(String s){
        String b = floatEndianess(s);
        int ii = Utility.binToInt(b);
        float f = Float.intBitsToFloat(ii);
        return f;
    }
    
    public static float stringToUFloat(String s){
        return Float.intBitsToFloat(Integer.parseInt(s,2));
    }
    
    public static byte[] getContentFromPos(int x, int y, byte[] content){
        byte[] msg = new byte[y-x+1];
        for (int i = 0; i < msg.length; i++) {
            msg[i] = content[x+i];
        }
        return msg;
    }
    

}
