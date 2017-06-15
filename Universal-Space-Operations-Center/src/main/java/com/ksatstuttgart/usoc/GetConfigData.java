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
* This class communicates with the config.properties file and
* monitors modifications.
* 
*
* @author  Victor Hertel
* @version 1.0
*/
 
public class GetConfigData {
    
    String lastModDate;
    boolean fileMod;
    Properties config;
    InputStream inputStream;
    OutputStream outputStream;
    
    
    
    
    /**
    * Method gets values from config.properties.
    * Output: property object 'config'
    */
    public Properties getValues() throws IOException {
        
        try {
            // Creates new properties object 'config'
            config = new Properties();
            // Creates FileInputStream from config.properties
            String fileName = "config.properties";
            String filePath = "src/main/resources/config/";
            inputStream = new FileInputStream(filePath + fileName);
            
            // Checks if config.properties exists
            if (inputStream != null) {
                config.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + fileName + "' not found.");
            }
            
        } catch (Exception e) {
            System.out.println("Exception: " + e);      
        } finally {
            inputStream.close();
        }

        return config;
    }
    
    
    
    
    /**
    * Method gets date of config.properties's last modification.
    * Output: String 'lastModDate' with date of last modification
    */    
    public String getLastMod() {

        // Initialize file object
        String fileName = "config.properties";
        String filePath = "src/main/resources/config/";
        File file = new File(filePath + fileName);
        
        // Get date of last modification
        lastModDate = Long.toString(file.lastModified());

        return lastModDate;
    }
    
    
    
    
    /**
    * Method stores date of config.properties's last modification
    * into lastConfigMod.properties.
    * Output: void
    */  
    public void setModDate() throws IOException {
                
        try {
            // Stores date of last modification into 'lastModDate'
            lastModDate = getLastMod();
            
            // Creates new properties object 'config'
            config = new Properties();
            // Creates FileOutputStream to lastConfigMod.properties
            String fileName = "lastConfigMod.properties";
            String filePath = "src/main/resources/config/";
            outputStream = new FileOutputStream(filePath + fileName);
            
            // Updates date of last modification in lastConfigMod.properties
            config.setProperty("lastConfigMod", lastModDate);
            config.store(outputStream, null);
            
        } catch (Exception e) {
            System.out.println("Exception: " + e);      
        } finally {
            outputStream.close();
        }
    }
    
    
    
    
    /**
    * Method gets values from config.properties.
    * Output: property object 'config'
    */    
    public boolean fileMod() throws IOException {    
        
        try {
            // Creates new properties object 'config'
            config = new Properties();
            // Creates FileInputStream from lastConfigMod.properties
            String fileName = "lastConfigMod.properties";
            String filePath = "src/main/resources/config/";
            inputStream = new FileInputStream(filePath + fileName);
            
            // Checks if lastConfigMod.properties exists
            if (inputStream != null) {
                config.load(inputStream);
                // Stores date of last modification into 'lastModDate'
                lastModDate = getLastMod();
                // Reads 'lastConfigMod' from lastConfigMod.properties
                String lastConfigMod = config.getProperty("lastConfigMod");
        
                // Checks if config.properties was modified
                if(lastModDate.equals(lastConfigMod)) {
                    fileMod = false;
                } else {
                    fileMod = true;
                    setModDate();
                }               
                 
            } else {
                throw new FileNotFoundException("Property file '" + fileName + "' not found.");
            }
            
        } catch (Exception e) {
            System.out.println("Exception: " + e);      
        } finally {
            inputStream.close();
        }
        
        return fileMod;
    }
}