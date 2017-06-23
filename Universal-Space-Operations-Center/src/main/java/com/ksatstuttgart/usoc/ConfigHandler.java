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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class ConfigHandler {
    
    
    /**
     * Method gets values from config.properties file.
     * 
     * @return
     * @throws java.io.FileNotFoundException
    */
    public static Properties getAllValues() throws FileNotFoundException, IOException {
        
        // Creates new properties object 'config'
        Properties config = new Properties();
        // Creates FileInputStream from config.properties
        String fileName = "config.properties";
        String filePath = "src/main/resources/config/";
        InputStream inputStream = new FileInputStream(filePath + fileName);
       
        // Checks if config.properties exists
        if (inputStream != null) {
            config.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file '" + fileName + "' not found.");
        }
        inputStream.close();
        
        return config;
        
    }
    
    
   
    
    /**
     * Method gets value of input keyword from 
     * conigMod.properties file.
     * 
     * @param key
     * @return 
     * @throws java.io.FileNotFoundException 
     * @throws java.io.IOException
    */         
    public static String getConfigModValue( String key ) throws FileNotFoundException, IOException {
        
        // Declares output variable 'value'
        String value;
        
        // Creates new properties object 'config'
        Properties config = new Properties();
        // Creates FileInputStream from config.properties
        String fileName = "configMod.properties";
        String filePath = "src/main/resources/config/";
        InputStream inputStream = new FileInputStream(filePath + fileName);
        
        // Checks if config.properties exists
        if (inputStream != null) {
            config.load(inputStream);
            value = config.getProperty(key);
        } else {
            throw new FileNotFoundException("Property file '" + fileName + "' not found.");
        }
        inputStream.close();

        return value;
    }
    
    
    
    

    /**
     * Method stores values of config.properties file and the date
     * of it's last modification into configMod.properties file.
     * 
     * @throws java.io.IOException
    */  
    public static void updateConfigMod() throws IOException {

        // Stores values of config.properties file into properties object 'config'
        Properties config = getAllValues();
    
        // Initialize file object
        String fileName = "configMod.properties";
        String filePath = "src/main/resources/config/";
        
        // Writes data in configMod.propertes file
        PrintWriter writer = new PrintWriter(filePath + fileName);
        writer.println("# This document serves as the basis for comparisons");
        writer.println("# to determine modified values in the config file.");
        writer.println("# The structure is fixed and manual interventions");
        writer.println("# and modifications are not intended!");
        config.list(writer);
        writer.close();       
    }

    
    
        
    /**
     * Method checks if name of experiment has been modified.
     * 
     * @return 
     * @throws java.io.IOException 
    */   
    public static boolean experimentNameMod() throws IOException {
        // Stores values from config.properties file into properties object 'configMods'
        Properties config = getAllValues();
        boolean experimentNameMod;
        
        if (config.getProperty("experimentName").equals(getConfigModValue("experimentName"))) {
                experimentNameMod = false;
        } else {
                experimentNameMod = true;    
        }
        
        return experimentNameMod;
    }
    
    
    
    
    /**
     * Method checks if properties of the charts has been modified.
     * 
     * @return 
     * @throws java.io.IOException 
    */   
    public static boolean chartMod() throws IOException {
        
        // Stores values from config.properties file into properties object 'configMods'
        Properties config = getAllValues();
        int numberOfCharts = Integer.parseInt(config.getProperty("numberOfCharts"));
        boolean chartMod = false;
        
        if (! config.getProperty("numberOfCharts").equals(getConfigModValue("numberOfCharts"))) {
            chartMod = true;
        }
        
        for (int counter=1; counter<=numberOfCharts; counter++) {
            if ((! config.getProperty("title[" + counter + "]").equals(getConfigModValue("title[" + counter + "]"))) ||
                    (! config.getProperty("x[" + counter + "]").equals(getConfigModValue("x[" + counter + "]"))) ||
                    (! config.getProperty("y[" + counter + "]").equals(getConfigModValue("y[" + counter + "]")))) {
                chartMod = true;
            }
        }
     
        return chartMod;
    }
    
    
    
    
    /**
     * Method checks if properties of the log panel has been modified.
     * 
     * @return 
     * @throws java.io.IOException 
    */   
    public static boolean logMod() throws IOException {
        
        // Stores values from config.properties file into properties object 'configMods'
        Properties config = getAllValues();
        int numberOfAddTabs = Integer.parseInt(config.getProperty("numberOfAddTabs"));
        boolean logMod = false;
        
        if ((! config.getProperty("serialPanel").equals(getConfigModValue("serialPanel"))) ||
                (! config.getProperty("iridiumPanel").equals(getConfigModValue("iridiumPanel"))) ||
                (! config.getProperty("numberOfAddTabs").equals(getConfigModValue("numberOfAddTabs")))) {
            logMod = true;
        }
        
        for (int counter=1; counter<=numberOfAddTabs; counter++) {
            if ((! config.getProperty("tabTitle[" + counter + "]").equals(getConfigModValue("tabTitle[" + counter + "]"))) ||
                    (! config.getProperty("tabFileName[" + counter + "]").equals(getConfigModValue("tabFileName[" + counter + "]")))) {
                logMod = true;
            }
        }
        
        return logMod;
    }
    
    
    
    
    /**
     * Method checks if config.properties has been modified.
     * 
     * @return
     * @throws java.io.IOException 
    */    
    public static boolean fileMod() throws IOException {
        boolean fileMod;
        
        if( !experimentNameMod() && !chartMod() && !logMod() ) {
            fileMod = false;
        } else {
            fileMod = true;
        }
        
        return fileMod;
    }
    
    
    
    
    /**
     * Method checks syntax of config.properties file.
     * 
     * @return
     * @throws java.io.IOException 
    */    
    public static boolean syntaxCheck() throws IOException {
        
        // Stores values from config.properties file into properties object 'configMods'
        Properties config = getAllValues();
        boolean syntaxCheck;
        int chartCounter = 1;
        int tabCounter = 1;
        
        // Checks if number of charts does match the number of defined chart labels
        while( (config.getProperty("title[" + chartCounter + "]")) != null ) {
            chartCounter++;
        }
        chartCounter = chartCounter - 1;
        if (chartCounter == Integer.parseInt(config.getProperty("numberOfCharts"))) {
            syntaxCheck = true;
        } else {
            syntaxCheck = false;
            System.out.println("The number of charts does not match the number of defined chart labels.");
        }
        
        // Checks if number of additional tabs does match the number of defined tab properties
        while( (config.getProperty("tabTitle[" + tabCounter + "]")) != null ) {
            tabCounter++;
        }
        tabCounter = tabCounter - 1;
        if (tabCounter == Integer.parseInt(config.getProperty("numberOfAddTabs"))) {
            syntaxCheck = true;
        } else {
            syntaxCheck = false;
            System.out.println("The number of additional tabs does not match the number of defined tab properties.");
        }
        
        return syntaxCheck;
    }
    
    
    
    
    /**
     * Method checks for modified parameters and rebuilds
     * the corresponding GUI structure.
     * 
     * @throws java.io.IOException 
    */    
    public static void rebuildGui() throws IOException {
        

        // If config.properties has been modified and syntax of config.properties file
        // is accurate, the FXML structure will be regenerated 
        if ( fileMod() && syntaxCheck() ) {
            
            // Checks if 'experimentName' has been modified since last compilation
            if ( experimentNameMod() ) {
                GuiBuilder.setExperimentName();
            }
            // Checks if 'numberOfCharts' has been modified since last compilation
            if ( chartMod() ) {
                GuiBuilder.chartBuilder();
            }
            // Checks if log panel properties has been modified since last compilation
            if ( logMod() ) {
                GuiBuilder.logBuilder();
            }
            
        } else {
            // Checks if config.properties file has been modified
            if ( !fileMod() ) {
                System.out.println("No FXML regeneration necessary.");
            }
            // Checks for correct syntax in the config.properties file
            if ( !syntaxCheck() ) {
                System.out.println("Syntax of parameter tranfer in config.properties file is not accurate.");
            }
        }
        
        // Updates values in configMod.properties file after GUI regeneration
        updateConfigMod();
    }
}