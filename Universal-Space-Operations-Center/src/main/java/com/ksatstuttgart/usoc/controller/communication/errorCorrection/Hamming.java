/*
 * The MIT License
 *
 * Copyright 2018 KSat Stuttgart e.V..
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
package com.ksatstuttgart.usoc.controller.communication.errorCorrection;

import com.ksatstuttgart.usoc.controller.Utility;

/**
 *
 * @author valentinstarlinger
 */
public class Hamming {
    
    public static String generate84Message(String msg){
        String returnMsg = "";
        for (byte b : msg.getBytes()) {
            int[] a = new int[8];
            String input = Utility.intToBits(b);
            for (int i = 0; i < a.length; i++) {
                a[i] = Integer.parseInt(input.charAt(i)+"");
            }
            int[] inputA = new int[4], inputB = new int[4];
            for (int i = 0; i < a.length/2; i++) {
                inputA[i] = a[i];
                inputB[i] = a[i+4];
            }
            returnMsg += (char) Utility.byteToByte(generateCode84(inputA));
            returnMsg += (char) Utility.byteToByte(generateCode84(inputB));
        }
        return returnMsg;
    }
    
    public static int[] generateCode84(int a[]) {
        int[] b = generateCode(a);
        int[] ret = new int[b.length+1];
        int count = 0;
        for (int i = 0; i < b.length; i++) {
            if(b[i]==0){
                count--;
            }else{
                count++;
            }
            ret[i] = b[i];
        }
        if(Math.abs(count)==7){
            ret[b.length] = b[0];
        } else if (count < 0) {
            ret[b.length] = 1;
        } else if (count > 0) {
            ret[b.length] = 0;
        }
        return ret;
    }
    
    public static int[] generateCode(int a[]) {
        // We will return the array 'b'.
        int b[];

        // We find the number of parity bits required:
        int i = 0, parity_count = 0, j = 0, k = 0;
        while (i < a.length) {
            // 2^(parity bits) must equal the current position
            // Current position is (number of bits traversed + number of parity bits + 1).
            // +1 is needed since array indices start from 0 whereas we need to start from 1.

            if (Math.pow(2, parity_count) == i + parity_count + 1) {
                parity_count++;
            } else {
                i++;
            }
        }

        // Length of 'b' is length of original data (a) + number of parity bits.
        b = new int[a.length + parity_count];

        // Initialize this array with '2' to indicate an 'unset' value in parity bit locations:
        for (i = 1; i <= b.length; i++) {
            if (Math.pow(2, j) == i) {
                // Found a parity bit location.
                // Adjusting with (-1) to account for array indices starting from 0 instead of 1.

                b[i - 1] = 2;
                j++;
            } else {
                b[k + j] = a[k++];
            }
        }
        for (i = 0; i < parity_count; i++) {
            // Setting even parity bits at parity bit locations:

            b[((int) Math.pow(2, i)) - 1] = getParity(b, i);
        }
        return b;
    }

    static int getParity(int b[], int power) {
        int parity = 0;
        for (int i = 0; i < b.length; i++) {
            if (b[i] != 2) {
                // If 'i' doesn't contain an unset value,
                // We will save that index value in k, increase it by 1,
                // Then we convert it into binary:

                int k = i + 1;
                String s = Integer.toBinaryString(k);

                //Nw if the bit at the 2^(power) location of the binary value of index is 1
                //Then we need to check the value stored at that location.
                //Checking if that value is 1 or 0, we will calculate the parity value.
                int x = ((Integer.parseInt(s)) / ((int) Math.pow(10, power))) % 10;
                if (x == 1) {
                    if (b[i] == 1) {
                        parity = (parity + 1) % 2;
                    }
                }
            }
        }
        return parity;
    }

    public static int[] receive(int a[], int parity_count) {
        // This is the receiver code. It receives a Hamming code in array 'a'.
        // We also require the number of parity bits added to the original data.
        // Now it must detect the error and correct it, if any.

        int power;
        // We shall use the value stored in 'power' to find the correct bits to check for parity.

        int parity[] = new int[parity_count];
        // 'parity' array will store the values of the parity checks.

        String syndrome = new String();
        // 'syndrome' string will be used to store the integer value of error location.

        for (power = 0; power < parity_count; power++) {
            // We need to check the parities, the same no of times as the no of parity bits added.

            for (int i = 0; i < a.length; i++) {
                // Extracting the bit from 2^(power):

                int k = i + 1;
                String s = Integer.toBinaryString(k);
                int bit = ((Integer.parseInt(s)) / ((int) Math.pow(10, power))) % 10;
                if (bit == 1) {
                    if (a[i] == 1) {
                        parity[power] = (parity[power] + 1) % 2;
                    }
                }
            }
            syndrome = parity[power] + syndrome;
        }
        // This gives us the parity check equation values.
        // Using these values, we will now check if there is a single bit error and then correct it.

        int error_location = Integer.parseInt(syndrome, 2);
        if (error_location != 0) {
            System.out.println("Error is at location " + error_location + ".");
            a[error_location - 1] = (a[error_location - 1] + 1) % 2;
            System.out.println("Corrected code is:");
            for (int i = 0; i < a.length; i++) {
                System.out.print(a[a.length - i - 1]);
            }
            System.out.println();
        } 

        // Finally, we shall extract the original data from the received (and corrected) code:
        power = parity_count - 1;
        int count = a.length-parity_count-1;
        int[] returnArray = new int[count+1];
        for (int i = a.length; i > 0; i--) {
            if (Math.pow(2, power) != i) {
                returnArray[count] = a[i - 1];
                count--;
            } else {
                power--;
            }
        }
        /*System.out.print("Original data sent was: ");
        for (int i : returnArray) {
            System.out.print(i);
        }
        System.out.println();*/
        return returnArray;
    }
    
    public static int[] get84Nibble(byte b){
        String bits = Utility.intToBits(b);
        if(bits.length()>8){
            bits = bits.substring(bits.length()-8);
        }
        //System.out.println("bits: "+bits);
        int count1s = 0;
        int count0s = 0;
        int[] input = new int[7];
        for (int i = 0; i < bits.length(); i++) {
            if(i < input.length){
                input[i] = Integer.parseInt(bits.charAt(i)+"");
            }
            if(bits.charAt(i)=='0'){
                count0s++;
            } else if(bits.charAt(i)=='1'){
                count1s++;
            }
        }
        
        if(count1s>5 || count0s>5){
            return receive(input,3);
        }
        if(Math.abs(count1s - count0s) < 2){
            return receive(input,3);
        }
        //System.out.println("not correct!");
        //throw excption
        return null;
    }
    
    public static int receive84(byte a, byte b){
        //System.out.println("decoding bytes: a:"+(char)a+", b:"+(char)b);
        int[] inta = get84Nibble(a);
        int[] intb = get84Nibble(b);
        
        if(inta == null || intb == null){
            //throw exception
            return 0;
        }
        
        return Utility.nibblesToByte(inta,intb);
    }
}
