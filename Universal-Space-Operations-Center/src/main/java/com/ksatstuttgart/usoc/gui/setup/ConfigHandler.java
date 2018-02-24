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
package com.ksatstuttgart.usoc.gui.setup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class communicates with the properties file.
 *
 *
 * @author Victor Hertel
 * @version 2.0
 */
public class ConfigHandler {

    /**
     * The number of elements of the transmitted keyword is counted according to the USOC syntax in the associated
     * .properties file and is returned as an integer.
     *
     * @param keyword
     * @param path
     * @return
     */
    public static int countItems(String keyword, String path) {

        // Declares necessary parameters
        int numberOfItems = 1;
        Properties config = getAllValues(path);
        while ((config.getProperty(keyword + "[" + numberOfItems + "]")) != null) {
            numberOfItems++;
        }
        numberOfItems = numberOfItems - 1;

        return numberOfItems;
    }

     
    /**
     * All values ​​of the configuration file are read out and returned as a Java Properties structure.
     *
     * @param path
     * @return
     */
    public static Properties getAllValues(String path) {

        // Declares necessary parameters
        Properties config = new Properties();
        try {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return config;
    }


    /**
     * The syntax of the information in the configuration file is checked for correctness.
     * A corresponding boolean is returned.
     *
     * @param path
     * @return
     */
    public static boolean syntaxCheck(String path) {

        // Declares necessary parameters
        Properties config = getAllValues(path);
        String GNSS3dView = config.getProperty("GNSS3dView");
        String statePanel = config.getProperty("statePanel");
        boolean syntaxCheck = true;

        // Check whether all necessary parameters have been passed for the charts
        if (!(countItems("chartTitle", path) == countItems("x", path)
                && countItems("chartTitle", path) == countItems("y", path))) {
            System.out.println("Syntax of CHART PROPERTIES is not accurate.");
            syntaxCheck = false;
        }
        // Checks if the value of the GNSS3dView keyword has been specified correctly
        if (!(GNSS3dView.equals("true") || GNSS3dView.equals("false"))) {
            System.out.println("Syntax of GNSS3dView property is not accurate.");
            syntaxCheck = false;
        }
        // Checks if the value of the statePanel keyword has been specified correctly
        if (!(statePanel.equals("true") || statePanel.equals("false"))) {
            System.out.println("Syntax of statePanel property is not accurate.");
            syntaxCheck = false;
        }

        // Checks syntax of LOG PROPERTIES
        String serialPanel = config.getProperty("serialPanel");
        String iridiumPanel = config.getProperty("iridiumPanel");
        // Checks if the value of the serialPanel keyword has been specified correctly
        if (!(serialPanel.equals("true") || serialPanel.equals("false"))) {
            System.out.println("Syntax of serialPanel property is not accurate.");
            syntaxCheck = false;
        }
        // Checks if the value of the iridiumPanel keyword has been specified correctly
        if (!(iridiumPanel.equals("true") || iridiumPanel.equals("false"))) {
            System.out.println("Syntax of iridiumPanel property is not accurate.");
            syntaxCheck = false;
        }

        return syntaxCheck;
    }
}
