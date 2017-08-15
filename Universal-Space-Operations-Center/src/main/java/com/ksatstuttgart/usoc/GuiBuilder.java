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
import javafx.stage.Stage;

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
     * @param stage
     * @param path
     * @return 
     * @throws java.io.IOException 
    */  
    public static int setExperimentName(Stage stage, String path) throws IOException {
        System.out.println("Experiment name has been updated!");
        Properties config = ConfigHandler.getAllValues(path);
        stage.setTitle(config.getProperty("experimentName"));
        
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
     * @param bufferedReader
     * @param counter
     * @param j
     * @return 
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */  
    public static StringBuilder writeMethod(BufferedReader bufferedReader, int counter, int j) throws FileNotFoundException, IOException {
                
        String separator = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();        
        String line;
        // set the mark at the beginning of the buffer
        bufferedReader.mark(100000000);
        
        boolean tokenFound = false;
        boolean newMethod = true;
        stringBuilder.append("    @FXML \n");
        stringBuilder.append("    private void button").append(counter).append(j).append("(ActionEvent event) { \n");
                
        while ( (line=bufferedReader.readLine()) != null ) {
                        
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

        // reset to the last mark; in this case, it's the beginning of the buffer
        bufferedReader.reset();
                
        if (newMethod) {
            stringBuilder.append("        // Automatically generated method button").append(counter).append(j).append("() \n");
            stringBuilder.append("        System.out.println(\"Button").append(counter).append(j).append(" was pressed!\"); \n");
        }
        stringBuilder.append("    } \n");
        
        return stringBuilder;        
    }

    
    
    /**
     * Method builds the FXML structure of the charts generically
     * in a scrollable GridPane with two columns
     * 
     * @param filePath
     * @param configPath
     * @throws java.io.IOException
    */  
    public static void mainFrameBuilder(String filePath, String configPath) throws IOException {
    
        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        boolean statePanel = Boolean.parseBoolean(config.getProperty("statePanel"));
        String path = "src/main/resources/";
        // Writes data in ChartPanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
        writer.println("<?import javafx.scene.text.*?> \n"
                + "<?import java.lang.*?> \n"
                + "<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        writer.println("<BorderPane maxHeight=\"1.7976931348623157E308\" maxWidth=\"1.7976931348623157E308\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"700.0\" prefWidth=\"1180.0\" xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller = \"com.ksatstuttgart.usoc.gui.controller.LogController\"> \n\n"
                + "   <top> \n"
                + "      <MenuBar BorderPane.alignment=\"CENTER\"> \n"
                + "        <menus> \n"
                + "          <Menu mnemonicParsing=\"false\" text=\"File\"> \n"
                + "            <items> \n"
                + "              <MenuItem mnemonicParsing=\"false\" text=\"Close\" /> \n"
                + "            </items> \n"
                + "          </Menu> \n"
                + "          <Menu mnemonicParsing=\"false\" text=\"Edit\"> \n"
                + "            <items> \n"
                + "              <MenuItem mnemonicParsing=\"false\" text=\"Delete\" /> \n"
                + "            </items> \n"
                + "          </Menu> \n"
                + "          <Menu mnemonicParsing=\"false\" text=\"Help\"> \n"
                + "            <items> \n"
                + "              <MenuItem mnemonicParsing=\"false\" text=\"About\" /> \n"
                + "            </items> \n"
                + "          </Menu> \n"
                + "        </menus> \n"
                + "      </MenuBar> \n"
                + "   </top> \n");
        if (statePanel) {
            writer.println("   <left> \n"
                    + "      <fx:include source = \"/fxml/CurrentStatePanel.fxml\" /> \n"
                    + "   </left> \n");
        }
        writer.println("   <center> \n"
                + "      <fx:include source = \"/fxml/ChartPanel.fxml\" /> \n"
                + "   </center> \n\n"
                + "   <right> \n"
                + "      <fx:include source = \"/fxml/LogPanel.fxml\" /> \n"
                + "   </right> \n\n"
                + "</BorderPane> \n");
        
        writer.close();
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
        boolean GNSS3dView = Boolean.parseBoolean(config.getProperty("GNSS3dView"));
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
                + "<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        
        if (GNSS3dView) {
            writer.println("<TabPane prefHeight=\"200.0\" prefWidth=\"200.0\" tabClosingPolicy=\"UNAVAILABLE\" BorderPane.alignment=\"CENTER\"> \n"
                    + "   <tabs> \n"
                    + "    <Tab text=\"Graphs\"> \n"
                    + "      <content> \n");
        }
        
        writer.println("<GridPane xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller = \"com.ksatstuttgart.usoc.gui.controller.ChartController\"> \n"
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
            writer.println("    <LineChart fx:id=\"lineChart" + counter + "\" title=\"" + config.getProperty("chartTitle[" + counter + "]")
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
        
        if (GNSS3dView) {
            writer.println("      </content> \n"
                    + "    </Tab> \n"
                    + "    <Tab text=\"GNSS 3D View\"> \n"
                    + "      <content> \n"
                    + "      </content> \n"
                    + "    </Tab> \n"
                    + "  </tabs> \n"
                    + "</TabPane> \n");
        }

        writer.close();      

        // Prints status update
        System.out.println("Chart panel has been updated!");     
    }
    
        
    
    /**
     * Method writes the controller of the chart panel generically
     * depending on input in the properties file
     * 
     * @param filePath
     * @param configPath
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */  
    public static void chartControlBuilder(String filePath, String configPath) throws FileNotFoundException, IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfCharts = ConfigHandler.countItems("chartTitle", configPath);
        String path = "src/main/java/com/ksatstuttgart/usoc/";
        
        // Writes data in LogController.java file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("package com.ksatstuttgart.usoc.gui.controller; \n");
        writer.println("import java.net.URL; \n"
                + "import java.util.ResourceBundle; \n"
                + "import javafx.fxml.Initializable; \n"
                + "import javafx.scene.chart.LineChart; \n"
                + "import javafx.scene.chart.XYChart; \n");
        writer.println("/** \n"
                + " * \n"
                + " * @author Victor \n"
                + " */ \n");
        writer.println("public class ChartController implements Initializable { \n");
        for (int i=1; i<=numberOfCharts; i++) {
            writer.println("    public LineChart<Integer, Integer> lineChart" + i + ";");
        }
        writer.println("\n    public void updateData() {");
        for (int i=1; i<=numberOfCharts; i++) {
            writer.println("        lineChart" + i + ".getXAxis().setAutoRanging(true); \n"
                    + "        lineChart" + i + ".getYAxis().setAutoRanging(true); \n"
                    + "        XYChart.Series series" + i + " = new XYChart.Series<>(); \n"
                    + "        series" + i + ".getData().add(new XYChart.Data<>(5, 23)); \n"
                    + "        series" + i + ".getData().add(new XYChart.Data<>(6, 15)); \n"
                    + "        lineChart" + i + ".getData().add(series" + i + "); \n");
        }
        writer.println("    } \n");
        writer.println("    @Override \n"
                + "    public void initialize(URL url, ResourceBundle rb) { \n"
                + "        // TODO");
        writer.println("    } \n"
                + "}");
        
        writer.close();  
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
        writer.println("<?import java.lang.*?> \n"
                + "<?import javafx.geometry.*?> \n"
                + "<?import javafx.collections.*?> \n"
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
        int maxNumberOfItems = 0;
        String path = "src/main/java/com/ksatstuttgart/usoc/";
        
        for (int counter=1; counter<=numberOfAddTabs; counter++) {
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            for (int j=1; j<=numberOfControlItems; j++) {
                if (j > maxNumberOfItems) {
                    maxNumberOfItems = j;
                }
            }
        }
        
        StringBuilder[][] stringBuilder = new StringBuilder[numberOfAddTabs][maxNumberOfItems];
        FileReader fileReader = new FileReader(path + filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        for (int counter=1; counter<=numberOfAddTabs; counter++) {
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            for (int j=1; j<=numberOfControlItems; j++) {
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                if (control.equals("button")) {
                    stringBuilder[counter-1][j-1] = writeMethod(bufferedReader, counter, j);
                }
            }
        }
        
        // Writes data in LogController.java file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("package com.ksatstuttgart.usoc.gui.controller; \n");
        writer.println("import java.net.URL; \n"
                + "import java.util.ResourceBundle; \n"
                + "import javafx.event.ActionEvent; \n"
                + "import javafx.fxml.FXML; \n"
                + "import com.ksatstuttgart.usoc.controller.communication.SerialComm;\n"
                + "import com.ksatstuttgart.usoc.controller.MainController;\n"
                + "import java.util.ArrayList;\n"
                + "import javafx.fxml.Initializable; \n"
                + "import javafx.scene.control.ComboBox; \n"
                + "import javafx.scene.control.TextArea; \n");
        writer.println("/** \n"
                + " * \n"
                + " * @author Victor \n"
                + " */ \n");
        writer.println("public class LogController implements Initializable { \n");
        
        if (Boolean.parseBoolean(config.getProperty("serialPanel"))) {
            writer.println("    @FXML private ComboBox comboBox1; \n"
                    + "    @FXML private ComboBox comboBox2; \n"
                    + "    @FXML private ComboBox comboBox3; \n"
                    + "    @FXML private TextArea serialTextArea; \n\n"
                    + "    public void setData() { \n"
                    + "        comboBox3.getItems().setAll(\"A\", \"B\", \"C\"); \n"
                    + "        //comboBox1.getItems().setAll(SerialComm.getInstance().getAvailableCommands()); \n"
                    + "    } \n");
            writer.println("    public void updatePortList(ArrayList<String> portList) { \n"
                    + "        if (comboBox2 != null) { \n"
                    + "            comboBox2.getItems().setAll(portList); \n"
                    + "        } \n"
                    + "    } \n");
            writer.println("    public void serialWriteLog() { \n"
                    + "        serialTextArea.setText(\"Test\"); \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    private void serialConnect(ActionEvent event) { \n"
                    + "        System.out.println(\"Connect button in serial log has been pressed!\"); \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    private void serialSendCommand(ActionEvent event) { \n"
                    + "        System.out.println(\"Send Command button in serial log has been pressed!\"); \n"
                    + "        String output = comboBox1.getSelectionModel().getSelectedItem().toString(); \n"
                    + "        if (output != null) { \n" 
                    + "            System.out.println(output); \n" 
                    + "        } else { \n" 
                    + "            System.out.println(\"null\"); \n" 
                    + "        } \n"
                    + "    } \n");
        }
        
        if (Boolean.parseBoolean(config.getProperty("iridiumPanel"))) {
            writer.println("    @FXML \n"
                    + "    private void iridiumOpen(ActionEvent event) { \n"
                    + "        System.out.println(\"Open button in iridium log has been pressed!\"); \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    private void iridiumClearData(ActionEvent event) { \n"
                    + "        System.out.println(\"Clear Data button in iridium log has been pressed!\"); \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    private void iridiumExportCSV(ActionEvent event) { \n"
                    + "        System.out.println(\"Export CSV button in iridium log has been pressed!\"); \n"
                    + "    } \n");
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
                    writer.println(stringBuilder[counter-1][j-1]);
                }
            }
        }
        
        writer.println("    @Override \n"
                + "    public void initialize(URL url, ResourceBundle rb) { \n"
                + "        // TODO \n"
                + "        MainController.startPortThread(this); \n"
                + "        //setData(); \n"
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
        boolean statePanel = Boolean.parseBoolean(config.getProperty("statePanel"));
        int numberOfBoxes = ConfigHandler.countItems("boxTitle", configPath);
        String path = "src/main/resources/";
        
        // Writes data in CurrentStatePanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        
        if (statePanel) {
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

            // Prints status update
            System.out.println("Data panel has been updated!");
        } else {
            writer.println("");
        }
    writer.close();
    }
}