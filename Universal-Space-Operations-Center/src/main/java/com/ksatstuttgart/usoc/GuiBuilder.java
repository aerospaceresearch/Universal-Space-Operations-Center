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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * This class builds the GUI FXML structure based on input 
 * parameters in the properties file and rebuilds parts
 * of it depending on changes.
 * 
 *
 * @author  Victor Hertel
 * @version 1.0
*/

public class GuiBuilder {
        

    
    
    /**
     * 
     * @return 
    */  
    public static int setExperimentName() {
        System.out.println("setExperimentName activated!");
        
        return 0;
    }
    
    
    
    
    /**
     * Method defines array 'position' with two values ​​for a clear positional
     * representation of the corresponding item in the GridPane.
     * 
     * @param input
     * @return 
    */  
    public static int[] getGridPosition(int input) {
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
     * Method defines array 'position' with two values ​​for a clear positional
     * representation of the corresponding item in the GridPane.
     * 
     * @param fileReader
     * @param writer
     * @param counter
     * @param j
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */  
    public static void writeMethod(FileReader fileReader, PrintWriter writer, int counter, int j) throws FileNotFoundException, IOException {
                
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String separator = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        
        String line = bufferedReader.readLine();
        System.out.println(line);
        
        boolean tokenFound = false;
        boolean newMethod = true;
        stringBuilder.append("    @FXML \n");
        stringBuilder.append("    private void button").append(counter).append(j).append("(ActionEvent event) { \n");
        
        while ( (line=bufferedReader.readLine()) != null ) {
            
            System.out.println("Zeilen...");
            
            if (line.contains("private void button" + counter + j + "(ActionEvent event) {")) {
                line = bufferedReader.readLine();
                
                if (line.contains("// Automatically generated method button" + counter + j + "()")) {                 
                    tokenFound = false;
                    newMethod = true;
                } else {
                    tokenFound = true;
                    newMethod = false;
                }

            } else if (line.equals("    } ")) {
                tokenFound = false;
            }

            if (tokenFound) {
                stringBuilder.append(line).append(separator) ;
            }
        }
                
        if (newMethod) {
            stringBuilder.append("        // Automatically generated method button").append(counter).append(j).append("() \n");
            stringBuilder.append("        System.out.println(\"Button").append(counter).append(j).append(" was pressed!\"); \n");
        }
        
        stringBuilder.append("    } \n");
        writer.println(stringBuilder);
    }

    
    
    
    /**
     * Method builds the FXML structure of the charts generically
     * in a scrollable GridPane with two columns
     * 
     * @param filePath
     * @param configPath
     * @throws java.io.IOException
    */  
    public static void chartBuilder(String filePath, String configPath) throws IOException {
                
        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfCharts = ConfigHandler.countItems("chartTitle", configPath);
        int numberOfRows;
        String path = "src/main/resources/";
        
        // Initializes number of rows depending on required number of charts
        if (numberOfCharts%2 == 0) {
            numberOfRows = numberOfCharts/2;
        } else {
            numberOfRows = (numberOfCharts + 1)/2;
        }
        
        // Writes data in ChartPanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("<?import javafx.scene.chart.*?> \n"
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
            writer.println("    <LineChart title=\"" + config.getProperty("chartTitle[" + counter + "]")
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
        System.out.println("Chart panel has been updated!");     
    }
    
    
    
    
    /**
     * Method builds the FXML structure of the log panel generically
     * in a TabPane with an optional number of additional tabs.
     * 
     * @param filePath
     * @param configPath
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */  
    public static void logBuilder(String filePath, String configPath) throws FileNotFoundException, IOException {
                
        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfAddTabs = ConfigHandler.countItems("tabTitle", configPath);     
        String path = "src/main/resources/";
        
        // Writes data in LogPanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("<?import javafx.geometry.*?> \n"
                + "<?import javafx.scene.*?> \n"
                + "<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        writer.println("<TabPane xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller = \"com.ksatstuttgart.usoc.gui.controller.LogController\" prefHeight=\"200.0\" prefWidth=\"200.0\" tabClosingPolicy=\"UNAVAILABLE\" BorderPane.alignment=\"CENTER\"> \n "
                + "  <tabs>");
        
        // Writes FXML structure if the serial panel is required
        if (Boolean.parseBoolean(config.getProperty("serialPanel"))) {
                       
            FileReader fileReader = new FileReader(path + "fxml/logTabs/SerialPanel.fxml");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String separator = System.getProperty("line.separator");
            
            writer.println("    <Tab text=\"Serial\"> \n"
                    + "      <content> \n");

            while( (line=bufferedReader.readLine()) != null ) {
                stringBuilder.append(line).append(separator) ;
            }
            writer.println(stringBuilder);

            writer.println("      </content> \n"
                    + "    </Tab>"); 
        }
        
        // Writes FXML structure if the iridium panel is required
        if (Boolean.parseBoolean(config.getProperty("iridiumPanel"))) {
            
            FileReader fileReader = new FileReader(path + "fxml/logTabs/IridiumPanel.fxml");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String separator = System.getProperty("line.separator");
            
            writer.println("    <Tab text=\"Iridium\"> \n"
                    + "      <content> \n");

            while( (line=bufferedReader.readLine()) != null ) {
                stringBuilder.append(line).append(separator) ;
            }
            writer.println(stringBuilder);

            writer.println("      </content> \n"
                    + "    </Tab>"); 
        }
        
        // Generates addiditional tabs
        for (int counter=1; counter<=numberOfAddTabs; counter++) {
            writer.println("    <Tab text=\"" + config.getProperty("tabTitle[" + counter + "]") + "\"> \n"
                    + "      <content> \n"
                    + "<BorderPane> \n"
                    + "<padding><Insets top=\"5\" right=\"5\" bottom=\"5\" left=\"5\"/></padding> \n"
                    + "  <top> \n"
                    + "    <GridPane> \n"
                    + "      <columnConstraints> \n"
                    + "        <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" /> \n"
                    + "        <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" /> \n"
                    + "      </columnConstraints> \n"
                    + "      <rowConstraints>");
            
            // Declares necessary parameters
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            int numberOfRows;
            
            // Sets number of rows depending on required number of control items
            if (numberOfControlItems%2 == 0) {
                numberOfRows = numberOfControlItems/2;
            } else {
                numberOfRows = (numberOfControlItems + 1)/2;
            } 
        
            // Writes FXML data
            for (int i=1; i<=numberOfRows; i++) {
                writer.println("        <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />");
            }
            writer.println("     </rowConstraints> \n"
                    + "      <children>");
            
            // Writes FXML data of tab content
            for (int j=1; j<=numberOfControlItems; j++) {
                
                // Gets grid position for control item
                int[] position = getGridPosition(j);
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                
                // Checks for type of entered control item
                switch (control) {
                    case "button":
                        writer.println("        <Button text=\"" + config.getProperty("bText[" + counter + "][" + j + "]") + "\" onAction=\"#button" + counter + j + "\" "
                                + "mnemonicParsing=\"false\"" + " GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" />");
                        break;
                    case "textField":
                        writer.println("        <TextField promptText=\"" + config.getProperty("promptText[" + counter + "][" + j + "]")
                                + "\" " + "GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" />");
                        break;
                    case "label":
                        writer.println("        <Label text=\"" + config.getProperty("lText[" + counter + "][" + j + "]")
                                + "\" " + "GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" />");
                        break;
                }
            }
            
            // Writes FXML data
            writer.println("      </children> \n"
                    + "    </GridPane> \n"
                    + "  </top>");
            if (Boolean.parseBoolean(config.getProperty("textArea[" + counter + "]"))) {
                writer.println("  <center> \n"
                        + "    <TextArea prefHeight=\"200.0\" prefWidth=\"200.0\" BorderPane.alignment=\"CENTER\" /> \n"
                        + "  </center>");
            }
            writer.println("</BorderPane> \n"
                    + "      </content> \n"
                    + "    </Tab>");          
        }
        
        writer.println("  </tabs> \n"
                + "</TabPane>");
        writer.close();  
        
        // Prints status update
        System.out.println("Log panel has been updated!"); 
    }
    
    
    
    
    /**
     * Method writes the controller of the log panel generically
     * depending on input in the properties file
     * 
     * @param filePath
     * @param configPath
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */  
    public static void logControlBuilder(String filePath, String configPath) throws FileNotFoundException, IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfAddTabs = ConfigHandler.countItems("tabTitle", configPath);     
        String path = "src/main/java/com/ksatstuttgart/usoc/";
        
        // Writes data in LogController.java file
        PrintWriter writer = new PrintWriter(path + filePath);
        FileReader fileReader = new FileReader(path + filePath);
        writer.println("package com.ksatstuttgart.usoc.gui.controller; \n");
        writer.println("import java.net.URL; \n"
                + "import java.util.ResourceBundle; \n"
                + "import javafx.event.ActionEvent; \n"
                + "import javafx.fxml.FXML; \n"
                + "import javafx.fxml.Initializable; \n");
        writer.println("/** \n"
                + " * \n"
                + " * @author Victor \n"
                + " */ \n");
        writer.println("public class LogController implements Initializable { \n");
        
        if (Boolean.parseBoolean(config.getProperty("serialPanel"))) {
            writer.println("    @FXML \n"
                    + "    private void serialConnect(ActionEvent event) { \n"
                    + "        System.out.println(\"Connect button in serial log has been pressed!\"); \n"
                    + "    } \n\n"
                    + "    @FXML \n"
                    + "    private void serialSendCommand(ActionEvent event) { \n"
                    + "        System.out.println(\"Send Command button in serial log has been pressed!\"); \n"
                    + "    } \n");
        }
        
        if (Boolean.parseBoolean(config.getProperty("iridiumPanel"))) {
            writer.println("    @FXML \n"
                    + "    private void iridiumReconnect(ActionEvent event) { \n"
                    + "        System.out.println(\"Reconnect button in iridium log has been pressed!\"); \n"
                    + "    } \n");
        }
        
        for (int counter=1; counter<=numberOfAddTabs; counter++) {
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            for (int j=1; j<=numberOfControlItems; j++) {
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                if (control.equals("button")) {
                    
                    
                    writeMethod(fileReader, writer, counter, j);
                    
                    
                    
                    
                    
                    
                    //writer.println("    @FXML \n"
                    //        + "    private void button" + counter + j + "(ActionEvent event) { \n"
                    //        + "        // Automatically generated method button" + counter + j + "() \n"
                    //        + "        System.out.println(\"Button" + counter + j + " was pressed!\"); \n"
                    //        + "    } \n");
                }
            }
        }

        writer.println("    @Override \n"
                + "    public void initialize(URL url, ResourceBundle rb) { \n"
                + "        // TODO \n"
                + "    } \n"
                + "}");
        writer.close();  
    }
    
    
    /**
     * Method builds the FXML structure of the current state panel generically
     * in a Scrollpane with an optional number of vertical boxes.
     * 
     * @param filePath
     * @param configPath
     * @throws java.io.IOException
    */  
    public static void currentStateBuilder(String filePath, String configPath) throws IOException {
        
        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfBoxes = ConfigHandler.countItems("boxTitle", configPath);
        String path = "src/main/resources/";
        
        // Writes data in CurrentStatePanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        writer.println("<ScrollPane prefHeight=\"200.0\" prefWidth=\"200.0\" BorderPane.alignment=\"CENTER\"> \n"
                + "  <content> \n"
                + "    <VBox prefHeight=\"200.0\" prefWidth=\"100.0\"> \n"
                + "      <children>");
        
        // Generates boxes
        for (int counter=1; counter<=numberOfBoxes; counter++) {
            writer.println("        <GridPane> \n"
                    + "          <columnConstraints> \n"
                    + "            <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" /> \n"
                    + "            <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" /> \n"
                    + "          </columnConstraints> \n"
                    + "          <rowConstraints>");
            
            // Declares necessary parameters
            int numberOfValues = ConfigHandler.countItems("keyword[" + counter + "]", configPath);
            int numberOfRows;
            
            // Sets number of rows depending on required number of control items
            if (numberOfValues%2 == 0) {
                numberOfRows = numberOfValues/2;
            } else {
                numberOfRows = (numberOfValues + 1)/2;
            } 
        
            // Writes FXML data
            for (int i=1; i<=numberOfRows; i++) {
                writer.println("            <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />");
            }
            writer.println("         </rowConstraints> \n"
                    + "          <children> \n"
                    + "            <Label text=\"" + config.getProperty("boxTitle[" + counter + "]") + "\" GridPane.columnIndex=\"0\" GridPane.rowIndex=\"0\" />");
            
            // Writes FXML data of box content
            for (int j=1; j<=numberOfValues; j++) {
                String keyword = config.getProperty("keyword[" + counter + "][" + j + "]");
                writer.println("            <Label text=\"" + keyword + "\" GridPane.columnIndex=\"0\" GridPane.rowIndex=\"" + j + "\" />");
            }
            writer.println("          </children> \n"
                    + "        </GridPane>");
        }
        
        writer.println("      </children> \n"
                + "    </VBox> \n"
                + "  </content> \n"
                + "</ScrollPane>");
        writer.close();
        
        // Prints status update
        System.out.println("Data panel has been updated!"); 
    }
}