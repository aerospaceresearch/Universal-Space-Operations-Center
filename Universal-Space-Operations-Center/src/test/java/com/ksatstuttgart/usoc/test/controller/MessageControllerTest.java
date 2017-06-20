/*
 * The MIT License
 *
 * Copyright 2017 KSat Stuttgart e.V..
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

import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.controller.Utility;
import com.ksatstuttgart.usoc.controller.xml.XMLReader;
import com.ksatstuttgart.usoc.data.message.SBD340;
import com.ksatstuttgart.usoc.data.message.Var;
import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author valentinstarlinger
 */
public class MessageControllerTest {

    /**
     * Test of addSBD340Message method, of class MessageController.
     */
    @Test
    public void testAddSBD340Message() throws IOException {
        System.out.println("test add and parse of Iridium message");
        SBD340 structure = XMLReader.getInstance()
                .getMessageStructure("protocols/messageProtocol.xml");
        MessageController controller = new MessageController(structure);

        FileInputStream fs = new FileInputStream("protocols" + File.separator + "msg_0");

        //System.out.println("");
        //System.out.println("- binary message - ");
        StringBuilder message = new StringBuilder();
        int nextByte;
        while ((nextByte = fs.read()) != -1) {
            String t = Utility.intToBits(nextByte);
            //System.out.println(t);
            message.append(t);
        }
        
        controller.addSBD340Message(message.toString());

        BufferedReader br = new BufferedReader(new FileReader("protocols" + File.separator + "logfile.txt"));

        String expected = "";
        String nextLine;
        while ((nextLine = br.readLine()) != null) {
            expected += nextLine;
        }
        String prettyResults = controller.getData().toString();
        System.out.println("");
        System.out.println("- results - ");
        System.out.println(prettyResults);
        
        String result = "";
        for (Sensor sensor : controller.getData().getData().getSensors()) {
            for (Var variable : sensor.getVars()) {
                for (Object value : variable.getValues().values()) {
                    result+=(value)+"\r\n";
                }
            }
        }

        //replace all whitespaces, otherwise difficult with comparison
        assertEquals(result.replaceAll("\\s", ""), expected.replaceAll("\\s", ""));
    }

}
