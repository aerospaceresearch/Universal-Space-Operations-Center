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
package com.ksatstuttgart.usoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
/**
 * @author Victor Hertel
 * 
 */
 
public class GetPropertyValues {
    
    String result = "";
    InputStream inputStream;

    public String getPropValues() throws IOException {
        try {

            Properties prop = new Properties();
            String propFileName = "config.properties";
            String propFilePath = "src/main/resources/config/";
                inputStream = new FileInputStream(propFilePath + propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            
        String experimentName = prop.getProperty("experimentName");
	String numberOfCharts = prop.getProperty("numbeOfCharts");
        String serialPanel = prop.getProperty("serialPanel");
        String iridiumPanel = prop.getProperty("iridiumPanel");
        System.out.println("Name of experiment: " + experimentName);
        System.out.println("Number of charts: " + numberOfCharts);
        System.out.println("Serial panel requested: " + serialPanel);
        System.out.println("Iridium panel requested: " + iridiumPanel);
            
            
        } catch (Exception e) {
                System.out.println("Exception: " + e);
                
        } finally {
                inputStream.close();
        }

        return result;
    }
}