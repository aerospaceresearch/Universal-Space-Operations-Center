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
import javafx.stage.Stage;
 
/**
 * This class communicates with the properties file and
 * monitors modifications to regenerate the GUI.
 * 
 *
 * @author  Victor Hertel
 * @version 1.0
*/
public class ConfigHandler {
    
    
    /**
     * Method counts number of items like charts.
     * 
     * @param keyword
     * @param path
     * @return
     * @throws java.io.IOException
    */
    public static int countItems(String keyword, String path) throws IOException {
        
        Properties config = getAllValues(path);
        int numberOfItems = 1;

        while( (config.getProperty(keyword + "[" + numberOfItems + "]")) != null ) {
            numberOfItems++;
        }
        numberOfItems = numberOfItems - 1;

        return numberOfItems;
    }
    
    
    
    
    /**
     * Method gets values from properties file.
     * 
     * @param path
     * @return
     * @throws java.io.FileNotFoundException
    */
    public static Properties getAllValues(String path) throws FileNotFoundException, IOException {
        
        Properties config = new Properties();
        // Creates FileInputStream from properties
        String filePath = "src/main/resources/";
        InputStream inputStream = new FileInputStream(filePath + path);
       
        // Checks if property file exists
        if (inputStream != null) {
            config.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file not found.");
        }
        inputStream.close();
        
        return config;
    }

    
        

    /**
     * Method stores values of properties file and the date
     * of it's last modification into configMod.properties file.
     * 
     * @param origin
     * @param destination
     * @throws java.io.IOException
    */  
    public static void updateConfigMod(String origin, String destination) throws IOException {

        Properties config = getAllValues(origin);
        // Initialize file object
        String filePath = "src/main/resources/";
        
        // Writes data in propertes file
        PrintWriter writer = new PrintWriter(filePath + destination);
        writer.println("# This document serves as the basis for comparisons");
        writer.println("# to determine modified values in the config file.");
        writer.println("# The structure is fixed and manual interventions");
        writer.println("# and modifications are not intended!");
        config.list(writer);
        writer.close();       
    }

    
    
    
    /**
     * Method checks if value of properties files are the same.
     * 
     * @param keyword
     * @param path
     * @param pathMod
     * @return 
     * @throws java.io.IOException 
    */  
    public static boolean valueMod(String keyword, String path, String pathMod) throws IOException {

        Properties config = getAllValues(path);
        Properties configMod = getAllValues(pathMod);
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
    public static boolean mainPanelMod(String path, String pathMod) throws IOException {
        
        int numberOfCharts = countItems("chartTitle", path);
        boolean mainPanelMod = false;
        
        if (countItems("chartTitle", path) != countItems("chartTitle", pathMod)) {
            mainPanelMod = true;
        }
        
        for (int counter=1; counter<=numberOfCharts; counter++) {
            if ( valueMod("chartTitle[" + counter + "]", path, pathMod) ||
                    valueMod("x[" + counter + "]", path, pathMod) ||
                    valueMod("y[" + counter + "]", path, pathMod) ) {
                mainPanelMod = true;
            }
        }
        
        if ( valueMod("GNSS3dView", path, pathMod) ) {
            mainPanelMod = true;
        }
             
        return mainPanelMod;
    }
    
    
    
    
    /**
     * Method checks if properties of the log panel has been modified.
     * 
     * @param path
     * @param pathMod
     * @return
     * @throws java.io.IOException
    */   
    public static boolean logPanelMod(String path, String pathMod) throws IOException {
        
        Properties config = getAllValues(path);
        int numberOfAddTabs = countItems("tabTitle", path);
        boolean logPanelMod = false;
        
        if ( valueMod("serialPanel", path, pathMod) || valueMod("iridiumPanel", path, pathMod) ) {
            logPanelMod = true;
        }
        
        if (countItems("tabTitle", path) != countItems("tabTitle", pathMod) ||
                countItems("textArea", path) != countItems("textArea", pathMod)) {
            logPanelMod = true;
        }
        
        for (int counter=1; counter<=numberOfAddTabs; counter++) {
            
            if ( valueMod("tabTitle[" + counter + "]", path, pathMod) ||
                    valueMod("textArea[" + counter + "]", path, pathMod) ) {
                logPanelMod = true;
            }

            if (countItems("control[" + counter + "]", path) != countItems("control[" + counter + "]", pathMod)) {
                logPanelMod = true;
            }
            
            // Declares necessary parameters
            int numberOfControlItems = countItems("control[" + counter + "]", path);
            for (int j=1; j<=numberOfControlItems; j++) {
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                
                // Checks for type of entered control item
                switch (control) {
                    case "button":
                        if ( valueMod("bText[" + counter + "][" + j + "]", path, pathMod) ) { 
                            logPanelMod = true;
                        }
                        break;
                    case "textField":
                        if ( valueMod("promptText[" + counter + "][" + j + "]", path, pathMod) ) {
                            logPanelMod = true;
                        }
                        break;
                    case "label":
                        if ( valueMod("lText[" + counter + "][" + j + "]", path, pathMod) ) {
                            logPanelMod = true;
                        }
                        break;
                }
            }
        }

        return logPanelMod;
    }
    
    
    
    
    /**
     * Method checks if properties of the state panel has been modified.
     * 
     * @param path
     * @param pathMod
     * @return 
     * @throws java.io.IOException 
    */   
    public static boolean statePanelMod(String path, String pathMod) throws IOException {
        
        int numberOfBoxes = countItems("boxTitle", path);
        boolean statePanelMod = false;
        
        boolean statePanel = valueMod("statePanel", path, pathMod);
        
        if (countItems("boxTitle", path) != countItems("boxTitle", pathMod) || statePanel) {
            statePanelMod = true;
        }
        
        for (int counter=1; counter<=numberOfBoxes; counter++) {
            
            if (valueMod("boxTitle[" + counter + "]", path, pathMod)) {
                statePanelMod = true;
            }
            
            if (countItems("keyword[" + counter + "]", path) != countItems("keyword[" + counter + "]", pathMod)) {
                statePanelMod = true;
            }
            
            // Declares necessary parameters
            int numberOfValues = countItems("keyword[" + counter + "]", path);
            for (int j=1; j<=numberOfValues; j++) {
                if ( valueMod("keyword[" + counter + "][" + j + "]", path, pathMod) ) {
                    statePanelMod = true;
                }
            }
        }
        
        return statePanelMod;
    }
    
    
    
    /**
     * Method checks if properties file has been modified.
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
     * Method checks if syntax of properties file is accurate.
     * 
     * @param path
     * @return
     * @throws java.io.IOException 
    */    
    public static boolean syntaxCheck(String path) throws IOException {
        
        Properties config = getAllValues(path);
        String GNSS3dView = config.getProperty("GNSS3dView");
        String statePanel = config.getProperty("statePanel");
        boolean syntaxCheck = true;

        // Checks syntax of CHART PROPERTIES
        if ( !(countItems("chartTitle", path) == countItems("x", path) && 
                countItems("chartTitle", path) == countItems("y", path)) ) {
            System.out.println("Syntax of CHART PROPERTIES is not accurate.");
            syntaxCheck = false;
        }
        if ( !(GNSS3dView.equals("true") || GNSS3dView.equals("false")) ) {
            System.out.println("Syntax of GNSS3dView property is not accurate.");
            syntaxCheck = false;
        }
        if ( !(statePanel.equals("true") || statePanel.equals("false")) ) {
            System.out.println("Syntax of statePanel property is not accurate.");
            syntaxCheck = false;
        }
        
        // Checks syntax of LOG PROPERTIES
        int numberOfAddTabs = countItems("tabTitle", path);
        String serialPanel = config.getProperty("serialPanel");
        String iridiumPanel = config.getProperty("iridiumPanel");
        if ( !(serialPanel.equals("true") || serialPanel.equals("false")) ) {
            System.out.println("Syntax of serialPanel property is not accurate.");
            syntaxCheck = false;
        }
        if ( !(iridiumPanel.equals("true") || iridiumPanel.equals("false")) ) {
            System.out.println("Syntax of iridiumPanel property is not accurate.");
            syntaxCheck = false;
        }
        for (int counter=1; counter<=numberOfAddTabs; counter++) {
            String textArea = config.getProperty("textArea[" + counter + "]");
            int numberOfControlItems = countItems("control[" + counter + "]", path);

            if ( !(textArea.equals("true") || textArea.equals("false")) ) {
                System.out.println("Syntax of textArea property is not accurate.");
                syntaxCheck = false;
            }
            
            for (int j=1; j<=numberOfControlItems; j++) {
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                if ( !(control.equals("button") || control.equals("label") || control.equals("textField")) ) {
                    System.out.println("Syntax of control item properties is not accurate.");
                    syntaxCheck = false;
                }
            }
        }
                
        return syntaxCheck;
    }
    
    
    
    /**
     * Method checks for modified parameters and rebuilds
     * the corresponding GUI structure.
     * 
     * @param path
     * @param pathMod
     * @param stage
     * @return 
     * @throws java.io.IOException 
    */    
    public static boolean rebuildGui(String path, String pathMod, Stage stage) throws IOException {
            
        Properties config = getAllValues(path);
        boolean experimentNameMod = experimentNameMod(path, pathMod);
        boolean mainPanelMod = mainPanelMod(path, pathMod);
        boolean logPanelMod = logPanelMod(path, pathMod);
        boolean statePanelMod = statePanelMod(path, pathMod);
        boolean rebuildGui;
                
        // Regenerats the entire GUI if RESET is set to true
        if (Boolean.parseBoolean(config.getProperty("RESET"))) {
            GuiBuilder.setExperimentName(stage, "config/config.properties");
            GuiBuilder.mainPanelBuilder("fxml/MainPanel.fxml", path);
            GuiBuilder.mainPanelControlBuilder("gui/controller/MainPanelController.java", path);
            GuiBuilder.logPanelBuilder("fxml/LogPanel.fxml", path);
            GuiBuilder.logPanelControlBuilder("gui/controller/LogPanelController.java", path);
            GuiBuilder.mainFrameBuilder("fxml/MainFrame.fxml", path);
            GuiBuilder.StatePanelBuilder("fxml/StatePanel.fxml", path);
            GuiBuilder.statePanelControlBuilder("gui/controller/StatePanelController.java", path);
            
            System.out.println("FXML has been reseted and regenerated completely.");
            
            return true;
        }
        
        // If properties has been modified and syntax of properties file
        // is accurate, the FXML structure will be regenerated 
        if ( fileMod(experimentNameMod, mainPanelMod, logPanelMod, statePanelMod) && syntaxCheck(path) ) {
            
            // Checks if 'experimentName' has been modified since last compilation
            if (experimentNameMod) {
                GuiBuilder.setExperimentName(stage, "config/config.properties");
            }
            // Checks if CHART PROPERTIES has been modified since last compilation
            if (mainPanelMod) {
                GuiBuilder.mainPanelBuilder("fxml/MainPanel.fxml", path);
                GuiBuilder.mainPanelControlBuilder("gui/controller/MainPanelController.java", path);
            }
            // Checks if LOG PROPERTIES has been modified since last compilation
            if (logPanelMod) {
                GuiBuilder.logPanelBuilder("fxml/LogPanel.fxml", path);
                GuiBuilder.logPanelControlBuilder("gui/controller/LogPanelController.java", path);
            }
            // Checks if STATE PROPERTIES has been modified since last compilation
            if (statePanelMod) {
                GuiBuilder.mainFrameBuilder("fxml/MainFrame.fxml", path);
                GuiBuilder.StatePanelBuilder("fxml/StatePanel.fxml", path);
                GuiBuilder.statePanelControlBuilder("gui/controller/StatePanelController.java", path);
            }
            rebuildGui = true;
            
        } else {
            // Checks if config file has been modified
            if ( !fileMod(experimentNameMod, mainPanelMod, logPanelMod, statePanelMod) ) {
                System.out.println("No FXML regeneration necessary.");
            }
            
            rebuildGui = false;
        }
        
        // Updates values in configMod.properties file after GUI regeneration
        updateConfigMod(path, pathMod);
        
        return rebuildGui;
    }
}