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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
 
/**
 * This class communicates with the config.properties file and
 * monitors modifications to regenerate the GUI.
 * 
 *
 * @author  Victor Hertel
 * @version 1.0
*/
 
public class GetConfigData {
    
    static String value;
    static String lastModDate;
    static boolean fileModBool;
    static Properties config;
    static InputStream inputStream;   
    
    

    
    /**
     * Method gets values from config.properties file.
     * 
     * @return 
     * @throws java.io.IOException
    */
    public static Properties getAllValues() throws IOException {
        
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
            
        } catch (IOException e) {
            System.out.println("Exception: " + e);      
        } finally {
            inputStream.close();
        }

        return config;
    }
    
    
   
    
    /**
     * Method gets value of input keyword from 
     * conigMod.properties file.
     * 
     * @param key
     * @return 
     * @throws java.io.IOException
    */         
    public static String getConfigModValue( String key ) throws IOException {
        
        try {
            // Creates new properties object 'config'
            Properties getConfigModValue = new Properties();
            // Creates FileInputStream from config.properties
            String fileName = "configMod.properties";
            String filePath = "src/main/resources/config/";
            inputStream = new FileInputStream(filePath + fileName);
            
            // Checks if config.properties exists
            if (inputStream != null) {
                getConfigModValue.load(inputStream);
                value = getConfigModValue.getProperty(key);
            } else {
                throw new FileNotFoundException("Property file '" + fileName + "' not found.");
            }
            
        } catch (IOException e) {
            System.out.println("Exception: " + e);      
        } finally {
            inputStream.close();
        }

        return value;
    }
    
    
    
    
    /**
     * Method gets date of config.properties's last modification.
     * 
     * @return 
    */    
    public static String getLastModDate() {

        // Initialize file object
        String fileName = "config.properties";
        String filePath = "src/main/resources/config/";
        File file = new File(filePath + fileName);
        
        // Get date of last modification
        lastModDate = Long.toString(file.lastModified());

        return lastModDate;
    }
    
    
    

    /**
     * Method stores values of config.properties file and the date
     * of it's last modification into configMod.properties file.
     * 
     * @throws java.io.IOException
    */  
    public static void updateConfigMod() throws IOException {
        
        try {
            
            // Stores date of last modification into String 'lastModDate'
            lastModDate = getLastModDate();
            // Stores values of config.properties file into properties object 'config'
            Properties updateConfigMod = getAllValues();
        
            // Initialize file object
            String fileName = "configMod.properties";
            String filePath = "src/main/resources/config/";
            
            // Writes data in configMod.propertes file
            PrintWriter writer = new PrintWriter(filePath + fileName);
            writer.println("# This document serves as the basis for comparisons");
            writer.println("# to determine modified values in the config file.");
            writer.println("# The structure is fixed and manual interventions");
            writer.println("# and modifications are not intended!");
            writer.println("lastModDate="+lastModDate);
            updateConfigMod.list(writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }         
    }




    /**
     * Method checks if config.properties has been modified.
     * 
     * @return
     * @throws java.io.IOException 
    */    
    public static boolean fileMod() throws IOException {    
        
        try {
            // Creates new properties object 'config'
            Properties fileMod = new Properties();
            // Creates FileInputStream from configMod.properties
            String fileName = "configMod.properties";
            String filePath = "src/main/resources/config/";
            inputStream = new FileInputStream(filePath + fileName);
            fileMod.load(inputStream);
            
            // Stores date of last modification into 'lastModDate'
            lastModDate = getLastModDate();
            // Reads 'lastModDate' from configMod.properties
            String configModDate = fileMod.getProperty("lastModDate");
    
            // Checks if config.properties was modified
            if(lastModDate.equals(configModDate)) {
                fileModBool = false;
            } else {
                fileModBool = true;
            }               
                 
        } catch (IOException e) {
            System.out.println("Exception: " + e);      
        } finally {
            inputStream.close();
        }
        
        return fileModBool;
    }
    
    
    
    
    /**
     * Method checks for modified parameters and rebuilds
     * the corresponding GUI structure.
     * 
     * @throws java.io.IOException 
    */    
    public static void rebuildGui() throws IOException {
        
        // Checks if config.properties has been modified since last compilation
        fileModBool = fileMod();
        // If config.properties has been modified, the FXML structure will be regenerated 
        if (fileModBool) {
            
            // Stores values from config.properties file into properties object 'configMods'
            Properties configMods = getAllValues();

            // Checks if 'experimentName' has been modified since last compilation
            if (! configMods.getProperty("experimentName").equals(getConfigModValue("experimentName"))) {
                GuiBuilder.setExperimentName();
            }
            // Checks if 'numberOfCharts' has been modified since last compilation
            if (! configMods.getProperty("numberOfCharts").equals(getConfigModValue("numberOfCharts"))) {
                GuiBuilder.chartBuilder();
            }
            
        } else {
            System.out.println("No FXML regeneration necessary.");
        }
        
        // Updates values in configMod.properties file after GUI regeneration
        updateConfigMod();
    }
}