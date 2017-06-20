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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * This class builds the GUI FXML structure based on input 
 * parameters in the config.properties file and rebuilds parts
 * of it depending on changes.
 * 
 *
 * @author  Victor Hertel
 * @version 1.0
*/

public class GuiBuilder {
        

    
    
    /**
     * 
    */  
    public static void setExperimentName() {
        System.out.println("setExperimentName activeted!");
    }
    
    
    
    /**
     * Defines array 'position' with two values ​​for a clear positional
     * representation of the corresponding chart in the GridPane.
     * 
     * @param input
     * @return 
    */  
    public static int[] getGridPosition( int input ) {
        // column: position[0]
        // row: position[1]
        int[] position = new int[2];
        if (input%2 == 0) {
            position[0] = 1;
            position[1] = (input - 2)/2;
        } else {
            position[0] = 0;
            position[1] = (input - 1)/2;
        }

        return position;
    }
    
    
    
    
    /**
     * This method builds the FXML structure of the charts generically
     * in a scrollable GridPane with two columns
     * 
     * @throws java.io.IOException
    */  
    public static void chartBuilder() throws IOException {
        
        // Generates GridPane for charts   
        
        // Stores values of config.properties file into properties object 'config'
        Properties config = ConfigHandling.getAllValues();
        int numberOfCharts = Integer.parseInt(config.getProperty("numberOfCharts"));
        int numberOfRows;
        
        // Sets number of rows depending on required number of charts
        if (numberOfCharts%2 == 0) {
            numberOfRows = numberOfCharts/2;
        } else {
            numberOfRows = (numberOfCharts + 1)/2;
        }
        
        // Initialize file object
        String fileName = "Charts.fxml";
        String filePath = "src/main/resources/fxml/";
        
        // Writes data in Charts.fxml file
        PrintWriter writer = new PrintWriter(filePath + fileName);
        writer.println("<?import javafx.scene.chart.*?>\n"
                + "<?import javafx.scene.layout.*?> \n");
        writer.println("<GridPane> \n"
                + "  <columnConstraints> \n"
                + "    <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" /> \n"
                + "    <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" /> \n"
                + "  </columnConstraints> \n"
                + "  <rowConstraints>");
        for (int i=1; i<=(numberOfRows); i++) {
            writer.println("    <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />");
        }
        writer.println("  </rowConstraints>");
        
        // Generates FXML for charts
        writer.println("  <children>");
        for (int counter=1; counter<=numberOfCharts; counter++) {
            int[] position = getGridPosition(counter);
            writer.println("    <LineChart title=\"" + config.getProperty("title[" + counter + "]")
                    + "\" GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\""
                    + position[1] + "\">");  
            writer.println("      <xAxis> \n"
                    + "        <CategoryAxis label=\"" + config.getProperty("x[" + counter + "]") + "\" side=\"BOTTOM\" /> \n"
                    + "      </xAxis> \n"
                    + "      <yAxis> \n"
                    + "        <NumberAxis label=\"" + config.getProperty("y[" + counter + "]") + "\" side=\"LEFT\" /> \n"
                    + "      </yAxis> \n"
                    + "    </LineChart>");
        }
        writer.println("  </children> \n"
                + "</GridPane>");
        writer.close();      

        // Prints status update
        System.out.println("Number of charts has been updated!");     
    }
    
    
    
    /**
     *  
     * @throws java.io.IOException
    */  
    public static void logBuilder() throws IOException {
        
        // Stores values of config.properties file into properties object 'config'
        Properties config = ConfigHandling.getAllValues();  
        int numberOfAddTabs = Integer.parseInt(config.getProperty("numberOfAddTabs"));
        
        // Initialize file object
        String fileName = "LogPanel.fxml";
        String filePath = "src/main/resources/fxml/";
        
        // Writes data in Charts.fxml file
        PrintWriter writer = new PrintWriter(filePath + fileName);
        
        writer.println("<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        writer.println("<TabPane prefHeight=\"200.0\" prefWidth=\"200.0\" tabClosingPolicy=\"UNAVAILABLE\" BorderPane.alignment=\"CENTER\"> \n "
                + "  <tabs>");
        
        if (Boolean.parseBoolean(config.getProperty("serialPanel"))) {
            writer.println("    <Tab text=\"Serial\"> \n"
                    + "      <content> \n"
                    + "        <fx:include source = \"logTabs/SerialPanel.fxml\"/> \n"
                    + "      </content> \n"
                    + "    </Tab>");
        }
        if (Boolean.parseBoolean(config.getProperty("iridiumPanel"))) {
            writer.println("    <Tab text=\"Iridium\"> \n"
                    + "      <content> \n"
                    + "        <fx:include source = \"logTabs/IridiumPanel.fxml\"/> \n"
                    + "      </content> \n"
                    + "    </Tab>");        
        }

        for (int counter=1; counter<=numberOfAddTabs; counter++) {
            writer.println("    <Tab text=\"" + config.getProperty("tabTitle[" + counter + "]") + "\"> \n"
                    + "      <content> \n"
                    + "        <fx:include source = \"logTabs/" + config.getProperty("tabFileName[" + counter + "]") + "\"/> \n"
                    + "      </content> \n"
                    + "    </Tab>");             
        }
        
        writer.println("  </tabs> \n"
                + "</TabPane>");
        writer.close();  
        
        // Prints status update
        System.out.println("Log panel has been updated!"); 
    }
}
