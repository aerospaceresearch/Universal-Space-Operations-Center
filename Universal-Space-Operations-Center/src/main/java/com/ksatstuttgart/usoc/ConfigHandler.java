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
     * @param path
     * @return
     * @throws java.io.FileNotFoundException
    */
    public static Properties getAllValues(String path) throws FileNotFoundException, IOException {
        
        // Creates new properties object 'config'
        Properties config = new Properties();
        // Creates FileInputStream from config.properties
        String filePath = "src/main/resources/";
        InputStream inputStream = new FileInputStream(filePath + path);
       
        // Checks if config.properties exists
        if (inputStream != null) {
            config.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file not found.");
        }
        inputStream.close();
        
        return config;
    }

    
        

    /**
     * Method stores values of config.properties file and the date
     * of it's last modification into configMod.properties file.
     * 
     * @param origin
     * @param destination
     * @throws java.io.IOException
    */  
    public static void updateConfigMod(String origin, String destination) throws IOException {

        // Stores values of config.properties file into properties object 'config'
        Properties config = getAllValues( origin );
    
        // Initialize file object
        String filePath = "src/main/resources/";
        
        // Writes data in configMod.propertes file
        PrintWriter writer = new PrintWriter(filePath + destination);
        writer.println("# This document serves as the basis for comparisons");
        writer.println("# to determine modified values in the config file.");
        writer.println("# The structure is fixed and manual interventions");
        writer.println("# and modifications are not intended!");
        config.list(writer);
        writer.close();       
    }

    
    
    
    /**
     * Method checks if value of property files are the same.
     * 
     * @param keyword
     * @param path
     * @param pathMod
     * @return 
     * @throws java.io.IOException 
    */  
    public static boolean valueMod(String keyword, String path, String pathMod) throws IOException {
        // Stores values from config.properties file into properties object 'configMods'
        Properties config = getAllValues( path );
        Properties configMod = getAllValues( pathMod );
        boolean valueMod;
        
        if (config.getProperty(keyword).equals(configMod.getProperty(keyword))) {
            valueMod = false;
        } else {
            valueMod = true;
        }
        
        return valueMod;
    }
    
    
    
        
    /**
     * Method checks if name of experiment has been modified.
     * 
     * @param path
     * @param pathMod
     * @return 
     * @throws java.io.IOException 
    */   
    public static boolean experimentNameMod(String path, String pathMod) throws IOException {
        boolean experimentNameMod = valueMod("experimentName", path, pathMod);
        return experimentNameMod;
    }
    
    
    
    
    /**
     * Method checks if properties of the charts has been modified.
     * 
     * @param path
     * @param pathMod
     * @return 
     * @throws java.io.IOException 
    */   
    public static boolean chartMod(String path, String pathMod) throws IOException {
        
        // Stores values from config.properties file into properties object 'configMods'
        Properties config = getAllValues( path );
        int numberOfCharts = Integer.parseInt(config.getProperty("numberOfCharts"));
        boolean chartMod = false;
        
        if ( valueMod("numberOfCharts", path, pathMod) ) {
            chartMod = true;
        }
        
        for (int counter=1; counter<=numberOfCharts; counter++) {
            if ( valueMod("chartTitle[" + counter + "]", path, pathMod) ||
                    valueMod("x[" + counter + "]", path, pathMod) ||
                    valueMod("y[" + counter + "]", path, pathMod) ) {
                chartMod = true;
            }
        }
             
        return chartMod;
    }
    
    
    
    
    /**
     * Method checks if properties of the log panel has been modified.
     * 
     * @param path
     * @param pathMod
     * @return
     * @throws java.io.IOException
    */   
    public static boolean logMod(String path, String pathMod) throws IOException {
        
        // Stores values from config.properties file into properties object 'configMods'
        Properties config = getAllValues( path );
        int numberOfAddTabs = Integer.parseInt(config.getProperty("numberOfAddTabs"));
        boolean logMod = false;
        
        if ( valueMod("serialPanel", path, pathMod) ||
                valueMod("iridiumPanel", path, pathMod) ||
                valueMod("numberOfAddTabs", path, pathMod) ) {
            logMod = true;
        }
        
        for (int counter=1; counter<=numberOfAddTabs; counter++) {
            if ( valueMod("tabTitle[" + counter + "]", path, pathMod) ) {
                logMod = true;
            }
            if ( valueMod("numberOfControlItems[" + counter + "]", path, pathMod) ) {      
                logMod = true;
            }
            if ( valueMod("textArea[" + counter + "]", path, pathMod) ) {  
                logMod = true;
            }
            
            // Declares necessary parameters
            int numberOfControlItems = Integer.parseInt(config.getProperty("numberOfControlItems[" + counter + "]"));
            for (int j=1; j<=numberOfControlItems; j++) {
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                
                // Checks for type of entered control item
                switch (control) {
                    case "button":
                        if ( valueMod("bText[" + counter + "][" + j + "]", path, pathMod) ) { 
                            logMod = true;
                        }
                        break;
                    case "textField":
                        if ( valueMod("promptText[" + counter + "][" + j + "]", path, pathMod) ) {
                            logMod = true;
                        }
                        break;
                    case "label":
                        if ( valueMod("lText[" + counter + "][" + j + "]", path, pathMod) ) {
                            logMod = true;
                        }
                        break;
                }
            }
        }

        return logMod;
    }
    
    
    
    
    /**
     * Method checks if properties of the state panel has been modified.
     * 
     * @param path
     * @param pathMod
     * @return 
     * @throws java.io.IOException 
    */   
    public static boolean stateMod (String path, String pathMod) throws IOException {
        
        // Stores values from config.properties file into properties object 'configMods'
        Properties config = getAllValues( path );
        int numberOfBoxes = Integer.parseInt(config.getProperty("numberOfBoxes"));
        boolean stateMod = false;
        
        if ( valueMod("numberOfBoxes", path, pathMod) ) {
            stateMod = true;
        }
        
        for (int counter=1; counter<=numberOfBoxes; counter++) {
            if ( valueMod("boxTitle[" + counter + "]", path, pathMod) ) {
                stateMod = true;
            }
            if ( valueMod("numberOfValues[" + counter + "]", path, pathMod) ) {
                stateMod = true;
            }
            
            // Declares necessary parameters
            int numberOfValues = Integer.parseInt(config.getProperty("numberOfValues[" + counter + "]"));
            for (int j=1; j<=numberOfValues; j++) {
                if ( valueMod("keyword[" + counter + "][" + j + "]", path, pathMod) ) {
                    stateMod = true;
                }
            }
        }
        
        return stateMod;
    }
    
    
    
    
    /**
     * Method checks if config.properties has been modified.
     * 
     * @param experimentNameMod
     * @param chartMod
     * @param logMod
     * @param stateMod
     * @return
     * @throws java.io.IOException 
    */    
    public static boolean fileMod(boolean experimentNameMod, boolean chartMod, boolean logMod, boolean stateMod) throws IOException {
        boolean fileMod;
        
        if ( !experimentNameMod && !chartMod && !logMod && !stateMod ) {
            fileMod = false;
        } else {
            fileMod = true;
        }
                
        return fileMod;
    }
    

    
    
    /**
     * Method checks for modified parameters and rebuilds
     * the corresponding GUI structure.
     * 
     * @param path
     * @param pathMod
     * @return 
     * @throws java.io.IOException 
    */    
    public static boolean rebuildGui(String path, String pathMod) throws IOException {
            
        boolean experimentNameMod = experimentNameMod(path, pathMod);
        boolean chartMod = chartMod(path, pathMod);
        boolean logMod = logMod(path, pathMod);
        boolean stateMod = stateMod(path, pathMod);
        boolean rebuildGui;
                
        // If config.properties has been modified and syntax of config.properties file
        // is accurate, the FXML structure will be regenerated 
        if ( fileMod(experimentNameMod, chartMod, logMod, stateMod) ) {
            
            // Checks if 'experimentName' has been modified since last compilation
            if ( experimentNameMod ) {
                GuiBuilder.setExperimentName();
            }
            // Checks if 'numberOfCharts' has been modified since last compilation
            if ( chartMod ) {
                GuiBuilder.chartBuilder("fxml/ChartPanel.fxml", "config/config.properties");
            }
            // Checks if log panel properties has been modified since last compilation
            if ( logMod ) {
                GuiBuilder.logBuilder("fxml/LogPanel.fxml", "config/config.properties");
            }
            // Checks if log panel properties has been modified since last compilation
            if ( stateMod ) {
                GuiBuilder.currentStateBuilder("fxml/CurrentStatePanel.fxml", "config/config.properties");
            }
            rebuildGui = true;
            
        } else {
            // Checks if config.properties file has been modified
            if ( !fileMod(experimentNameMod, chartMod, logMod, stateMod) ) {
                System.out.println("No FXML regeneration necessary.");
            }
            // Checks for correct syntax in the config.properties file
            //if ( !syntaxCheck() ) {
            //    System.out.println("Syntax of parameter tranfer in config.properties file is not accurate.");
            //}
            rebuildGui = false;
        }
        
        // Updates values in configMod.properties file after GUI regeneration
        updateConfigMod("config/config.properties", "config/configMod.properties");
        
        return rebuildGui;
    }
}