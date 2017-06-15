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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
 
/**
 * @author Victor Hertel
 * 
 */
 
public class GetConfigData {
    
    String result = "";
    InputStream inputStream;
    OutputStream outputStream;

    public String getConfigValues() throws IOException {
        try {

            Properties config = new Properties();
            String configFileName = "config.properties";
            String configFilePath = "src/main/resources/config/";
            inputStream = new FileInputStream(configFilePath + configFileName);
            
            if (inputStream != null) {
                config.load(inputStream);
            } else {
                throw new FileNotFoundException("Configuration file '" + configFileName + "' not found");
            }
            
             
            String experimentName = config.getProperty("experimentName");
            int numberOfCharts = Integer.parseInt(config.getProperty("numbeOfCharts"));
            boolean serialPanel = Boolean.parseBoolean(config.getProperty("serialPanel"));
            boolean iridiumPanel = Boolean.parseBoolean(config.getProperty("iridiumPanel"));

            System.out.println("Name of experiment: " + experimentName);
            System.out.println("Number of charts: " + numberOfCharts);
            if (serialPanel == true) {
                System.out.println("Serial panel required: YES");
            } else {
                System.out.println("Serial panel required: NO");
            }
            if (iridiumPanel == true) {
                System.out.println("Iridium panel required: YES");
            } else {
                System.out.println("Iridium panel required: NO");
            }


            } catch (Exception e) {
                    System.out.println("Exception: " + e);

            } finally {
                    inputStream.close();
            }

        return result;
    }
    
    
    
    
    
    
    public String getLastMod() throws IOException {
        try {

            Properties lastMod = new Properties();
            String fileName = "lastConfigMod.properties";
            String filePath = "src/main/resources/config/";
            outputStream = new FileOutputStream(filePath + fileName);
            
            // initialize File object
            File file = new File("src/main/resources/config/config.properties");
            // get the last modified value
            String lastModDate = Long.toString(file.lastModified());
            
            lastMod.setProperty("lastModDate", lastModDate);
            lastMod.store(outputStream, null);
            
        } catch (Exception e) {
                System.out.println("Exception: " + e);
                
        } finally {
                inputStream.close();
        }

        return result;
    }
}