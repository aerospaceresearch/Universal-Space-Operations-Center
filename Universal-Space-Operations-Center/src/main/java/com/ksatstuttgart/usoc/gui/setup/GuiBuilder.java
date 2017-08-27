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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javafx.stage.Stage;

/**
 * This class builds the GUI FXML structure based on input parameters in the
 * properties file and rebuilds parts of it depending on changes.
 *
 *
 * @author Victor Hertel
 * @version 1.0
 */
public class GuiBuilder {

    
    /**
     * The title of the generated ground station is updated and reseted.
     * 
     * @param stage
     * @param path
     * @return
     * @throws java.io.IOException
    */
    public static int setExperimentName(Stage stage, String path) throws IOException {
        Properties config = ConfigHandler.getAllValues(path);
        stage.setTitle(config.getProperty("experimentName"));
        System.out.println("Experiment name has been updated!");

        return 0;
    }

    
    /**
     * 
     * Method defines array 'position' with two values ​​for a clear positional
     * representation of the corresponding item in the GridPane.
     * 
     * Both in the MainPanel, as well as in the LogPanel and StatePanel, the elements
     * of the content are filled in a two-column grid. For this purpose, the number of
     * the element from the configuration file must be transformed into a two-dimensional
     * array with line and column specification.
     *
     * @param input
     * @return
    */
    public static int[] getGridPosition(int input) {
        // column: position[0]
        // row: position[1]
        int[] position = new int[2];
        if (input % 2 == 0) {
            position[0] = 1;
            position[1] = (input - 2) / 2;
        } else {
            position[0] = 0;
            position[1] = (input - 1) / 2;
        }

        return position;
    }
    
    
    /**
     * 
     * Method writes the MIT licence into the automatically generated files
     * depending on the target file type
     *
     * @param type
     * @param writer
    */
    public static void writeLicence(String type, PrintWriter writer) {
        if (type.equals(".fxml")) {
            writer.println("<!--\n"
                    + "  The MIT License\n"
                    + "\n"
                    + "  Copyright 2017 KSat Stuttgart e.V..\n"
                    + "\n"
                    + "  Permission is hereby granted, free of charge, to any person obtaining a copy\n"
                    + "  of this software and associated documentation files (the \"Software\"), to deal\n"
                    + "  in the Software without restriction, including without limitation the rights\n"
                    + "  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"
                    + "  copies of the Software, and to permit persons to whom the Software is\n"
                    + "  furnished to do so, subject to the following conditions:\n"
                    + "\n"
                    + "  The above copyright notice and this permission notice shall be included in\n"
                    + "  all copies or substantial portions of the Software.\n"
                    + "\n"
                    + "  THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"
                    + "  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"
                    + "  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"
                    + "  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"
                    + "  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"
                    + "  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n"
                    + "  THE SOFTWARE.\n"
                    + "  -->");
        } else if (type.equals(".java")) {
            writer.println("/*\n"
                    + " * The MIT License\n"
                    + " *\n"
                    + " * Copyright 2017 KSat Stuttgart e.V..\n"
                    + " *\n"
                    + " * Permission is hereby granted, free of charge, to any person obtaining a copy\n"
                    + " * of this software and associated documentation files (the \"Software\"), to deal\n"
                    + " * in the Software without restriction, including without limitation the rights\n"
                    + " * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"
                    + " * copies of the Software, and to permit persons to whom the Software is\n"
                    + " * furnished to do so, subject to the following conditions:\n"
                    + " *\n"
                    + " * The above copyright notice and this permission notice shall be included in\n"
                    + " * all copies or substantial portions of the Software.\n"
                    + " *\n"
                    + " * THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"
                    + " * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"
                    + " * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"
                    + " * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"
                    + " * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"
                    + " * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n"
                    + " * THE SOFTWARE.\n"
                    + " */");
        }
    }

    
    /**
     * This method recognizes manually added method content concerning additional
     * tabs in the LogPanel (or items of the StatePanel), to prevent their overwriting
     * when the configuration file is changed.
     *
     * @param bufferedReader
     * @param counter
     * @param j
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */
    public static StringBuilder writeMethod(BufferedReader bufferedReader, int counter, int j) throws FileNotFoundException, IOException {

        // Declares necessary parameters
        String separator = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        // set the mark at the beginning of the buffer
        bufferedReader.mark(100000000);

        boolean tokenFound = false;
        boolean newMethod = true;
        stringBuilder.append("    @FXML \n");
        stringBuilder.append("    private void button").append(counter).append(j).append("(ActionEvent event) { \n");

        // Reads each line of the document
        while ((line = bufferedReader.readLine()) != null) {
            // Recognizes the method for a button in an additional tab
            if (line.contains("private void button" + counter + j + "(ActionEvent event) {")) {
                line = bufferedReader.readLine();
                // Checks whether the method was written manually
                if (line.contains("// Automatically generated method button" + counter + j + "()")) {
                    // No manually added code
                    tokenFound = false;
                    newMethod = true;
                } else {
                    // Manually added coded
                    tokenFound = true;
                    newMethod = false;
                }
            // Seeks the end of the method
            } else if (line.equals("    } ")) {
                tokenFound = false;
            }
            // If tokenfound is true, the current line of the manually added code is added to the stringBuilder
            if (tokenFound) {
                stringBuilder.append(line).append(separator);
            }
        }

        // reset to the last mark; in this case, it's the beginning of the buffer
        bufferedReader.reset();

        // If newMethod is true, then the standardized button method is written
        if (newMethod) {
            stringBuilder.append("        // Automatically generated method button").append(counter).append(j).append("() \n");
            stringBuilder.append("        System.out.println(\"Button").append(counter).append(j).append(" was pressed!\"); \n");
        }
        stringBuilder.append("    } \n");

        return stringBuilder;
    }

    
    /**
     * The MainFrame.fxml file is regenerated according to the settings of the
     * configurationfile when the method is called.
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
        String licenceType = ".fxml";
        
        // Writes data in ChartPanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        
        // XML declaration
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
        // Writes MIT Licence
        writeLicence(licenceType, writer);  
        // Java imports
        writer.println("<?import javafx.scene.text.*?> \n"
                + "<?import java.lang.*?> \n"
                + "<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        // BoarderPane of main window
        writer.println("<BorderPane maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"400.0\" prefWidth=\"600.0\" xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">\n");
        // MenuBar at BoarderPane -> top
        writer.println("<!--\n"
                + "   <top>\n"
                + "      <MenuBar BorderPane.alignment=\"CENTER\">\n"
                + "        <menus>\n"
                + "          <Menu mnemonicParsing=\"false\" text=\"File\">\n"
                + "            <items>\n"
                + "              <MenuItem mnemonicParsing=\"false\" text=\"Close\" />\n"
                + "            </items>\n"
                + "          </Menu>\n"
                + "          <Menu mnemonicParsing=\"false\" text=\"Edit\">\n"
                + "            <items>\n"
                + "              <MenuItem mnemonicParsing=\"false\" text=\"Delete\" />\n"
                + "            </items>\n"
                + "          </Menu>\n"
                + "          <Menu mnemonicParsing=\"false\" text=\"Help\">\n"
                + "            <items>\n"
                + "              <MenuItem mnemonicParsing=\"false\" text=\"About\" />\n"
                + "            </items>\n"
                + "          </Menu>\n"
                + "        </menus>\n"
                + "      </MenuBar>\n"
                + "   </top>\n"
                + "  -->\n");
        // Checks whether the statePanel is needed
        if (statePanel) {
            // StatePanel at BoarderPane -> left
            writer.println("   <left> \n"
                    + "      <fx:include source = \"/fxml/StatePanel.fxml\" /> \n"
                    + "   </left> \n");
        }
        // MainPanel at BoarderPane -> center
        writer.println("   <center> \n"
                + "      <fx:include source = \"/fxml/MainPanel.fxml\" /> \n"
                + "   </center> \n\n"
                // LogPanel at BoarderPane -> right
                + "   <right> \n"
                + "      <fx:include source = \"/fxml/LogPanel.fxml\" /> \n"
                + "   </right> \n\n"
                + "</BorderPane> \n");
        
        writer.close();
        
        // Prints status update
        System.out.println("MainFrame.fxml has been updated!");
    }

    
    /**
     * The MainPanel.fxml file is regenerated according to the settings of the
     * configuration file when the method is called.
     *
     * @param filePath
     * @param configPath
     * @throws java.io.IOException
    */
    public static void mainPanelBuilder(String filePath, String configPath) throws IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        boolean GNSS3dView = Boolean.parseBoolean(config.getProperty("GNSS3dView"));
        boolean chartTab;
        int numberOfCharts = ConfigHandler.countItems("chartTitle", configPath);
        int numberOfRows;
        String path = "src/main/resources/";
        String licenceType = ".fxml";


        // Initializes number of rows depending on required number of charts
        if (numberOfCharts % 2 == 0) {
            numberOfRows = numberOfCharts / 2;
        } else {
            numberOfRows = (numberOfCharts + 1) / 2;
        }
        
        // Checks if charts were specified
        if (config.getProperty("chartTitle[1]") == null) {
            chartTab = false;
        } else {
            chartTab = true;
        }

        // Writes data in MainPanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        // Writes MIT Licence
        writeLicence(licenceType, writer);        // Java imports
        writer.println("<?import javafx.scene.chart.*?> \n"
                + "<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        // TabPane of MainPanel
        writer.println("<TabPane xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller = \"com.ksatstuttgart.usoc.gui.controller.MainPanelController\" tabClosingPolicy=\"UNAVAILABLE\" BorderPane.alignment=\"CENTER\"> \n"
                    + "   <tabs>");
        // Writes the FXML code of charts, if specified
        if (chartTab) {
            writer.println("    <Tab text=\"Graphs\"> \n"
                    + "      <content> \n"
                    + "        <ScrollPane fitToHeight=\"true\" fitToWidth=\"true\">\n"
                    + "          <content>\n"
                    + "            <GridPane>\n"
                    + "              <columnConstraints> \n"
                    + "                <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"200.0\" prefWidth=\"400.0\" /> \n"
                    + "                <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"200.0\" prefWidth=\"400.0\" /> \n"
                    + "              </columnConstraints> \n"
                    + "              <rowConstraints>");
            // Loops through number of rows required for the number of charts
            for (int i = 1; i <= (numberOfRows); i++) {
                writer.println("                <RowConstraints maxHeight=\"400.0\" minHeight=\"200.0\" prefHeight=\"300.0\" vgrow=\"SOMETIMES\" />");
            }
            writer.println("              </rowConstraints>\n"
                    + "              <children>");
            // Loops through charts
            for (int counter = 1; counter <= numberOfCharts; counter++) {
                int[] position = getGridPosition(counter);
                // Writes FXML representation of chart
                writer.println("                <LineChart fx:id=\"lineChart" + counter + "\" title=\"" + config.getProperty("chartTitle[" + counter + "]")
                        + "\" GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\""
                        + position[1] + "\" maxHeight=\"400.0\" minHeight=\"200.0\" GridPane.halignment=\"CENTER\" GridPane.valignment=\"CENTER\" "
                        + "GridPane.hgrow=\"SOMETIMES\" GridPane.vgrow=\"SOMETIMES\">");
                writer.println("                  <xAxis>\n"
                        + "                    <NumberAxis label=\"" + config.getProperty("x[" + counter + "]") + "\" side=\"BOTTOM\" />\n"
                        + "                  </xAxis>\n"
                        + "                  <yAxis>\n"
                        + "                    <NumberAxis label=\"" + config.getProperty("y[" + counter + "]") + "\" side=\"LEFT\" />\n"
                        + "                  </yAxis>\n"
                        + "                </LineChart>");
            }
            writer.println("              </children>\n"
                    + "            </GridPane>\n"
                    + "          </content>\n"
                    + "        </ScrollPane>\n"
                    + "      </content>\n"
                    + "    </Tab>");
        }
        // Writes FXML code for the 'GNSS 3D View'-tab, if specified
        if (GNSS3dView) {
            writer.println("    <Tab text=\"GNSS 3D View\">\n"
                    + "      <content>\n"
                    + "        <StackPane fx:id=\"pane\" maxHeight=\"1.7976931348623157E308\" maxWidth=\"1.7976931348623157E308\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"180.0\" prefWidth=\"200.0\">\n"
                    + "\n"
                    + "        </StackPane>\n"
                    + "      </content>\n"
                    + "    </Tab>");
        }
        writer.println("  </tabs>\n"
                    + "</TabPane>\n");
        
        writer.close();

        // Prints status update
        System.out.println("MainPanel.fxml has been updated!");
    }

    
    /**
     * The MainPanelController.java file is regenerated according to the settings of the
     * configuration file when the method is called.
     *
     * @param filePath
     * @param configPath
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */
    public static void mainPanelControlBuilder(String filePath, String configPath) throws FileNotFoundException, IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfCharts = ConfigHandler.countItems("chartTitle", configPath);
        boolean GNSS3dView = Boolean.parseBoolean(config.getProperty("GNSS3dView"));
        String path = "src/main/java/com/ksatstuttgart/usoc/";
        String licenceType = ".java";

        // Writes data in MainPanelController.java file
        PrintWriter writer = new PrintWriter(path + filePath);
        // Writes MIT Licence
        writeLicence(licenceType, writer);        // Java package
        writer.println("package com.ksatstuttgart.usoc.gui.controller; \n");
        // Java imports
        writer.println("import com.ksatstuttgart.usoc.controller.DataModification;\n"
                + "import java.net.URL; \n"
                + "import com.ksatstuttgart.usoc.controller.MainController;\n"
                + "import com.ksatstuttgart.usoc.controller.MessageController;\n"
                + "import com.ksatstuttgart.usoc.data.ErrorEvent;\n"
                + "import com.ksatstuttgart.usoc.data.USOCEvent;\n"
                + "import com.ksatstuttgart.usoc.data.message.Var;\n"
                + "import com.ksatstuttgart.usoc.data.message.dataPackage.Sensor;\n"
                + "import java.util.ResourceBundle;\n"
                + "import javafx.fxml.FXML;\n"
                + "import javafx.fxml.Initializable;\n"
                + "import javafx.scene.chart.LineChart;\n"
                + "import javafx.scene.chart.XYChart;\n");
        // Specific imports for 'GNSS 3D View'-tab
        if (GNSS3dView) {
            writer.println("import com.ksatstuttgart.usoc.gui.worldwind.GNSSPanel;\n"
                    + "import javafx.fxml.FXML;\n"
                    + "import javafx.scene.layout.StackPane;\n");
        }
        // Javadoc class header
        writer.println("/** \n"
                + " * <h1>MainPanelController</h1>\n"
                + " * This class ensure the functionality of the MainPanel.\n"
                + " * <p>\n"
                + " * The charts are declared and records are assigned.\n"
                + " * The MainPanelController is generated automatically\n"
                + " * \n"
                + " * @author Victor Hertel\n"
                + " * @version 1.0\n"
                + "*/");
        writer.println("public class MainPanelController extends DataController implements Initializable { \n");
        // Loops through charts
        for (int i = 1; i <= numberOfCharts; i++) {
            // Declares all specified charts
            writer.println("    @FXML\n"
                    + "    public LineChart<Number, Number> lineChart" + i + ";");
        }
        // Writes updateData() method
        writer.println("\n    @Override\n"
                + "    public void updateData(MessageController mc, USOCEvent e) {\n");
        for (int i = 1; i <= numberOfCharts; i++) {
            writer.println("        lineChart" + i + ".getXAxis().setAutoRanging(true);\n"
                    + "        lineChart" + i + ".getYAxis().setAutoRanging(true);\n");
        }
        writer.println("        //in case this is an error event, ignore it\n"
                + "        if (e instanceof ErrorEvent) {\n"
                + "            return;\n"
                + "        }\n"
                + "\n"
                + "        //go through the data and update the charts\n"
                + "        for (Sensor sensor : mc.getData().getSensors()) {\n"
                + "            //adjust values for the sensor specific to the current experiment\n"
                + "            Sensor adjusted = DataModification.adjustSensorData(sensor);\n"
                + "\n"
                + "            //search for thermocouple sensors\n"
                + "            if (adjusted.getSensorName().startsWith(\"Thermocouple\")) {\n"
                + "                //thermocouple sensors have only one variable with the current\n"
                + "                //data scheme it uses the sensor name as variable name \n"
                + "                for (Var var : adjusted.getVars()) {\n"
                + "                    addVarToChart(var, lineChart1);\n"
                + "                }\n"
                + "            }\n"
                + "\n"
                + "            //search for pressure sensors\n"
                + "            if (adjusted.getSensorName().contains(\"Pressure\")) {\n"
                + "                //pressure sensors have only one variable with the current\n"
                + "                //data scheme and it uses the sensor name as variable name \n"
                + "                for (Var var : adjusted.getVars()) {\n"
                + "                    addVarToChart(var, lineChart2);\n"
                + "                }\n"
                + "            }\n"
                + "\n"
                + "            //search for pressure sensors\n"
                + "            if (adjusted.getSensorName().contains(\"GNSS TIME\")) {\n"
                + "                //pressure sensors have only one variable with the current\n"
                + "                //data scheme and it uses the sensor name as variable name \n"
                + "                for (Var var : adjusted.getVars()) {\n"
                + "                    addVarToChart(var, lineChart3);\n"
                + "                }\n"
                + "            }\n"
                + "\n"
                + "            //search for voltage sensors\n"
                + "            if (adjusted.getSensorName().startsWith(\"Battery\")) {\n"
                + "                //voltage sensors have only one variable with the current\n"
                + "                //data scheme and it uses the sensor name as variable name \n"
                + "                for (Var var : adjusted.getVars()) {\n"
                + "                    addVarToChart(var, lineChart4);\n"
                + "                }\n"
                + "            }\n"
                + "\n"
                + "            //search for IMU data\n"
                + "            if (adjusted.getSensorName().contains(\"IMU\")) {\n"
                + "                for (Var var : adjusted.getVars()) {\n"
                + "                    //only visualize quaternion data ignore calibration data\n"
                + "                    //for chart\n"
                + "                    addVarToChart(var, lineChart5);\n"
                + "                }\n"
                + "            }\n"
                + "        }\n"
                + "    }\n");
        writer.println("    private void addVarToChart(Var var, LineChart<Number, Number> chart) {\n"
                + "        XYChart.Series series = getSeriesForChart(var, chart);\n"
                + "        for (Long time : var.getValues().keySet()) {\n"
                + "            series.getData().add(new XYChart.Data<>(time, var.getValues().get(time)));\n"
                + "        }\n"
                + "    }\n");
        writer.println("    private XYChart.Series getSeriesForChart(Var var, LineChart<Number, Number> chart) {\n"
                + "        for (XYChart.Series<Number, Number> series : chart.getData()) {\n"
                + "            if (series.getName().equals(var.getDataName())) {\n"
                + "                return series;\n"
                + "            }\n"
                + "        }\n"
                + "        XYChart.Series series = new XYChart.Series<>();\n"
                + "        series.setName(var.getDataName());\n"
                + "        chart.getData().add(series);\n"
                + "        return series;\n"
                + "    }\n");
        // Declares StackPane for 'GNSS 3D View'-tab, if specified
        if (GNSS3dView) {
            writer.println("    @FXML\n"
                    + "    private StackPane pane;\n");
        }
        // Writes initialize method
        writer.println("    @Override \n"
                + "    public void initialize(URL url, ResourceBundle rb) { \n"
                + "        // TODO\n"
                + "        MainController.getInstance().addDataUpdateListener(new UpdateListener());");
        // Adds the GNSSPanel to the initialize method
        if (GNSS3dView) {
            writer.println("        GNSSPanel.addGNSSPaneltoStackPane(pane);");
        }
        writer.println("    }\n"
                + "}");
        
        writer.close();
        
        // Prints status update
        System.out.println("mainPanelController.java has been updated!");
    }

    
    /**
     * The LogPanel.fxml file is regenerated according to the settings of the
     * configuration file. The method builds the FXML structure generically
     * in a TabPane with an optional number of additional tabs.
     *
     * @param filePath
     * @param configPath
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */
    public static void logPanelBuilder(String filePath, String configPath) throws FileNotFoundException, IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfAddTabs = ConfigHandler.countItems("tabTitle", configPath);
        String path = "src/main/resources/";
        String licenceType = ".fxml";

        // Writes data in LogPanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        // Writes MIT Licence
        writeLicence(licenceType, writer);
        // Java imports
        writer.println("<?import java.lang.*?> \n"
                + "<?import javafx.geometry.*?> \n"
                + "<?import javafx.collections.*?> \n"
                + "<?import javafx.scene.*?> \n"
                + "<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        // TabPane
        writer.println("<TabPane xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller = \"com.ksatstuttgart.usoc.gui.controller.LogPanelController\" maxWidth=\"270.0\" minWidth=\"270.0\" prefWidth=\"270.0\" tabClosingPolicy=\"UNAVAILABLE\" BorderPane.alignment=\"CENTER\"> \n"
                + "  <tabs>");

        // Writes FXML structure if the serial panel is required
        if (Boolean.parseBoolean(config.getProperty("serialPanel"))) {
            // Declares necessary parameters
            FileReader fileReader = new FileReader(path + "fxml/logTabs/SerialPanel.fxml");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String separator = System.getProperty("line.separator");

            writer.println("    <Tab text=\"Serial\"> \n"
                    + "      <content> \n");
            // Reads out SerialPanel.fxml and adds every line to stringBuilder
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append(separator);
            }
            writer.println(stringBuilder);

            writer.println("      </content> \n"
                    + "    </Tab>");
        }

        // Writes FXML structure if the iridium panel is required
        if (Boolean.parseBoolean(config.getProperty("iridiumPanel"))) {
            // Declares necessary parameters
            FileReader fileReader = new FileReader(path + "fxml/logTabs/IridiumPanel.fxml");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String separator = System.getProperty("line.separator");

            writer.println("    <Tab text=\"Iridium\"> \n"
                    + "      <content> \n");
            // Reads out SerialPanel.fxml and adds every line to stringBuilder
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append(separator);
            }
            writer.println(stringBuilder);

            writer.println("      </content> \n"
                    + "    </Tab>");
        }

        // Generates addiditional tabs
        // Loops through additional tabs
        for (int counter = 1; counter <= numberOfAddTabs; counter++) {
            writer.println("    <Tab text=\"" + config.getProperty("tabTitle[" + counter + "]") + "\"> \n"
                    + "      <content> \n"
                    + "        <BorderPane> \n"
                    + "        <padding><Insets top=\"5\" right=\"5\" bottom=\"5\" left=\"5\"/></padding> \n"
                    + "          <top> \n"
                    + "            <GridPane alignment=\"TOP_CENTER\" BorderPane.alignment=\"CENTER\">\n"
                    + "              <columnConstraints> \n"
                    + "                <ColumnConstraints hgrow=\"SOMETIMES\" /> \n"
                    + "                <ColumnConstraints hgrow=\"SOMETIMES\" /> \n"
                    + "              </columnConstraints> \n"
                    + "              <rowConstraints>");

            // Declares necessary parameters
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            int numberOfRows;

            // Sets number of rows depending on required number of control items
            if (numberOfControlItems % 2 == 0) {
                numberOfRows = numberOfControlItems / 2;
            } else {
                numberOfRows = (numberOfControlItems + 1) / 2;
            }

            // Loops through number of rows required for the number of control items
            for (int i = 1; i <= numberOfRows; i++) {
                writer.println("                <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />");
            }
            writer.println("             </rowConstraints> \n"
                    + "              <children>");

            // Loops through control items within the tab
            for (int j = 1; j <= numberOfControlItems; j++) {

                // Gets grid position for control item
                int[] position = getGridPosition(j);
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                
                // Checks if control item has to be aligned on the left or right side of the panel
                if (j % 2 == 0) {
                    // Checks for type of entered control item
                    switch (control) {
                        case "button":
                            writer.println("                <Button text=\"" + config.getProperty("bText[" + counter + "][" + j + "]") + "\" onAction=\"#button" + counter + j + "\" "
                                    + "mnemonicParsing=\"false\"" + " GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" maxWidth=\"-Infinity\" GridPane.valignment=\"CENTER\" GridPane.vgrow=\"SOMETIMES\" GridPane.halignment=\"RIGHT\" GridPane.hgrow=\"SOMETIMES\"/>");
                            break;
                        case "textField":
                            writer.println("                <TextField promptText=\"" + config.getProperty("promptText[" + counter + "][" + j + "]")
                                    + "\" " + "GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" GridPane.halignment=\"RIGHT\" maxWidth=\"-Infinity\" GridPane.hgrow=\"SOMETIMES\" GridPane.valignment=\"CENTER\" GridPane.vgrow=\"SOMETIMES\" />");
                            break;
                        case "label":
                            writer.println("                <Label text=\"" + config.getProperty("lText[" + counter + "][" + j + "]")
                                    + "\" " + "GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" GridPane.halignment=\"RIGHT\" GridPane.hgrow=\"SOMETIMES\" GridPane.valignment=\"CENTER\" GridPane.vgrow=\"SOMETIMES\" />");
                            break;
                    }   
                } else {
                    // Checks for type of entered control item
                    switch (control) {
                        case "button":
                            writer.println("                <Button text=\"" + config.getProperty("bText[" + counter + "][" + j + "]") + "\" onAction=\"#button" + counter + j + "\" "
                                    + "mnemonicParsing=\"false\"" + " GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" maxWidth=\"-Infinity\" GridPane.valignment=\"CENTER\" GridPane.vgrow=\"SOMETIMES\" GridPane.halignment=\"LEFT\" GridPane.hgrow=\"SOMETIMES\"/>");
                            break;
                        case "textField":
                            writer.println("                <TextField promptText=\"" + config.getProperty("promptText[" + counter + "][" + j + "]")
                                    + "\" " + "GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" GridPane.halignment=\"LEFT\" maxWidth=\"-Infinity\" GridPane.hgrow=\"SOMETIMES\" GridPane.valignment=\"CENTER\" GridPane.vgrow=\"SOMETIMES\" />");
                            break;
                        case "label":
                            writer.println("                <Label text=\"" + config.getProperty("lText[" + counter + "][" + j + "]")
                                    + "\" " + "GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" GridPane.halignment=\"LEFT\" GridPane.hgrow=\"SOMETIMES\" GridPane.valignment=\"CENTER\" GridPane.vgrow=\"SOMETIMES\" />");
                            break;
                    } 
                }
            }
            writer.println("              </children> \n"
                    + "            </GridPane> \n"
                    + "          </top>");
            // Decides about textarea within the tab
            if (Boolean.parseBoolean(config.getProperty("textArea[" + counter + "]"))) {
                writer.println("          <center> \n"
                        + "            <TextArea prefHeight=\"200.0\" prefWidth=\"200.0\" BorderPane.alignment=\"CENTER\" /> \n"
                        + "          </center>");
            }
            writer.println("        </BorderPane> \n"
                    + "      </content> \n"
                    + "    </Tab>");
        }

        writer.println("  </tabs> \n"
                + "</TabPane>");
        writer.close();

        // Prints status update
        System.out.println("LogPanel.fxml has been updated!");
    }

    
    /**
     * The LogPanelController.java file is regenerated according to the settings of the
     * configuration file when the method is called.
     *
     * @param filePath
     * @param configPath
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */
    public static void logPanelControlBuilder(String filePath, String configPath) throws FileNotFoundException, IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfAddTabs = ConfigHandler.countItems("tabTitle", configPath);
        int maxNumberOfItems = 0;
        boolean serialPanel = Boolean.parseBoolean(config.getProperty("serialPanel"));
        boolean iridiumPanel = Boolean.parseBoolean(config.getProperty("iridiumPanel"));
        String path = "src/main/java/com/ksatstuttgart/usoc/";
        String licenceType = ".java";

        // Loops through additional tabs
        for (int counter = 1; counter <= numberOfAddTabs; counter++) {
            // Loops through control items within the tab
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            for (int j = 1; j <= numberOfControlItems; j++) {
                if (j > maxNumberOfItems) {
                    // Sets the maximum of the control items of all tabs
                    maxNumberOfItems = j;
                }
            }
        }

        // Declares StringBuilder-array with the dimension of 'numberOfAddTabs' and 'maxNumberOfItems'
        StringBuilder[][] stringBuilder = new StringBuilder[numberOfAddTabs][maxNumberOfItems];
        FileReader fileReader = new FileReader(path + filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // Loops through additional tabs
        for (int counter = 1; counter <= numberOfAddTabs; counter++) {
            // Loops through control items within the tab
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            for (int j = 1; j <= numberOfControlItems; j++) {
                // Calls the writeMethod()-method to rewrite either manually written methods or to
                // write dummy methods for the buttons
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                if (control.equals("button")) {
                    stringBuilder[counter - 1][j - 1] = writeMethod(bufferedReader, counter, j);
                }
            }
        }

        // Writes data in LogPanelController.java file
        PrintWriter writer = new PrintWriter(path + filePath);
        // Writes MIT Licence
        writeLicence(licenceType, writer);
        // Java package
        writer.println("package com.ksatstuttgart.usoc.gui.controller;\n");
        // Java general imports
        writer.println("import java.net.URL;\n"
                + "import java.util.ResourceBundle;\n"
                + "import javafx.event.ActionEvent;\n"
                + "import javafx.fxml.FXML;\n"
                + "import javafx.fxml.Initializable;\n");
        // Java specific imports for serialPanel or iridiumPanel
        if (serialPanel == true || iridiumPanel == true) {
            writer.println("// Imports for serialLog or iridiumLog\n"
                    + "import com.ksatstuttgart.usoc.controller.MainController;\n"
                    + "import com.ksatstuttgart.usoc.controller.MessageController;\n"
                    + "import com.ksatstuttgart.usoc.data.DataSource;\n"
                    + "import com.ksatstuttgart.usoc.data.ErrorEvent;\n"
                    + "import com.ksatstuttgart.usoc.data.USOCEvent;\n"
                    + "import javafx.scene.control.TextArea;\n");
        }
        // Java specific imports for serialPanel
        if (serialPanel) {
            writer.println("// Specific imports for serialLog\n"
                    + "import com.ksatstuttgart.usoc.controller.communication.SerialComm;\n"
                    + "import java.util.ArrayList;\n"
                    + "import com.ksatstuttgart.usoc.data.SerialEvent;\n"
                    + "import javafx.scene.control.ComboBox;\n");
        }
        // Java specific imports for iridiumPanel
        if (iridiumPanel) {
            writer.println("// Specific imports for iridiumLog\n"
                    + "import com.ksatstuttgart.usoc.controller.communication.MailReceiver;\n"
                    + "import com.ksatstuttgart.usoc.data.MailEvent;\n"
                    + "import java.util.Date;\n"
                    + "import javafx.scene.control.Button;\n"
                    + "import javafx.scene.control.Label;\n"
                    + "import javafx.scene.control.TextField;\n"
                    + "import javax.mail.Address;\n");
        } 
        // Javadoc class header
        writer.println("/** \n"
                + " * <h1>LogPanelController</h1>\n"
                + " * This class ensures the functionality of the LogPanel.\n"
                + " * <p>\n"
                + " * Apart from pre-programmed methods for the SerialLog and IridiumLog,\n"
                + " * a dummy method is written for each button of the individually designed\n"
                + " * tabs. These methods can be supplemented manually. Those method contents are\n"
                + " * recognized and will not be lost if the button label has been changed and the\n"
                + " * corresponding FXML structure and controller regenerated.\n"
                + " * The LogPanelController is generated automatically.\n"
                + " * \n"
                + " * @author Victor Hertel\n"
                + " * @version 1.0\n"
                + "*/");
        writer.print("public class LogPanelController ");
        // Extends the class name for SerialPanel or IridiumPanel
        if (serialPanel || iridiumPanel) {
            writer.print("extends DataController ");
        }
        writer.println("implements Initializable { \n");
        
        // Writes methods for the functionality of the SerialPanel
        if (serialPanel) {
            writer.println("    @FXML private ComboBox comboBox1; \n"
                    + "    @FXML private ComboBox comboBox2; \n"
                    + "    @FXML private ComboBox comboBox3; \n"
                    + "    @FXML private TextArea serialTextArea; \n\n"
                    + "    @FXML \n"
                    + "    public void setData() { \n"
                    + "        comboBox1.getItems().setAll(SerialComm.getInstance().getAvailableBaudrates()); \n"
                    + "        comboBox3.getItems().setAll(SerialComm.getInstance().getAvailableCommands()); \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    public void updatePortList(ArrayList<String> portList) { \n"
                    + "        if (comboBox2 != null) { \n"
                    + "            comboBox2.getItems().setAll(portList); \n"
                    + "        } \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    private void serialConnect(ActionEvent event) { \n"
                    + "        System.out.println(\"Connect button in serial log has been pressed!\");\n"
                    + "        String port = comboBox2.getSelectionModel().getSelectedItem().toString();\n"
                    + "        int baudrate = Integer.parseInt(comboBox1.getSelectionModel().getSelectedItem().toString());\n"
                    + "        SerialComm.getInstance().start(port, baudrate); \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    private void serialSendCommand(ActionEvent event) { \n"
                    + "        System.out.println(\"Send Command button in serial log has been pressed!\"); \n"
                    + "        String output = comboBox3.getSelectionModel().getSelectedItem().toString(); \n"
                    + "        SerialComm.getInstance().send(output);\n"
                    + "    } \n");
        }

        // Writes methods for the functionality of the IridiumPanel
        if (iridiumPanel) {
            writer.println("    @FXML private TextArea iridiumTextArea; \n"
                    + "    @FXML private TextField iridiumReconnectField; \n"
                    + "    @FXML private Label iridiumLastFrom; \n"
                    + "    @FXML private Label iridiumLastSubject; \n"
                    + "    @FXML private Label iridiumLastFilename; \n"
                    + "    @FXML private Label iridiumLastTimestamp; \n\n"
                    + "    @FXML \n"
                    + "    private void iridiumOpen(ActionEvent event) { \n"
                    + "        System.out.println(\"Open button in iridium log has been pressed!\");\n"
                    + "        MainController.getInstance().openBinaryFile(); \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    private void iridiumClearData(ActionEvent event) { \n"
                    + "        System.out.println(\"Clear Data button in iridium log has been pressed!\");\n"
                    + "        MainController.getInstance().clearData(); \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    private void iridiumExportCSV(ActionEvent event) { \n"
                    + "        System.out.println(\"Export CSV button in iridium log has been pressed!\");\n"
                    + "        MainController.getInstance().exportCSV(); \n"
                    + "    } \n");
            writer.println("    @FXML \n"
                    + "    private void iridiumReconnect(ActionEvent event) { \n"
                    + "        System.out.println(\"Reconnect button in iridium log has been pressed!\"); \n"
                    + "        ((Button)event.getSource()).setText(\"Reconnect\"); \n"
                    + "        int numMessages = 0;\n"
                    + "        try { \n"
                    + "            numMessages = Integer.parseInt(iridiumReconnectField.getText()); \n"
                    + "        } catch(NumberFormatException ex){ \n"
                    + "            //do nothing; \n"
                    + "        } \n"
                    + "        //MailReceiver.getInstance().setMessagesOnReconnect(numMessages); \n"
                    + "        MailReceiver.getInstance().reconnect(); \n"
                    + "    } \n");
        }
        
        // Writes updateData()-method
        if (serialPanel == true && iridiumPanel == true) {
        // For the case serialPanel = true, iridiumPanel = true
            writer.println("    @Override\n"
                    + "    public void updateData(MessageController msgController, USOCEvent ue) {\n"
                    + "        if (ue instanceof MailEvent) {\n"
                    + "            MailEvent e = (MailEvent) ue;\n"
                    + "            String s = \"\";\n"
                    + "            for (Address from : e.getFrom()) {\n"
                    + "                s += \",\" + from.toString();\n"
                    + "            }\n"
                    + "            //Setting label texts\n"
                    + "            iridiumLastFrom.setText(s.substring(1));\n"
                    + "            iridiumLastSubject.setText(e.getSubject());\n"
                    + "            iridiumLastFilename.setText(e.getFilename());\n"
                    + "            iridiumLastTimestamp.setText(new Date(e.getTimeStampGmail()).toString());\n"
                    + "            iridiumTextArea.setText(msgController.getData().toString());\n"
                    + "        } else if (ue instanceof SerialEvent) {\n"
                    + "            serialTextArea.setText(((SerialEvent)ue).getMsg());\n"
                    + "        } else if (ue instanceof ErrorEvent) {\n"
                    + "            if (DataSource.MAIL == ue.getDataSource()) {\n"
                    + "                iridiumTextArea.setText(((ErrorEvent) ue).getErrorMessage());\n"
                    + "            } else if (DataSource.SERIAL == ue.getDataSource()) {\n"
                    + "                serialTextArea.setText(((ErrorEvent) ue).getErrorMessage());\n"
                    + "            }\n"
                    + "        } else {\n"
                    + "            serialTextArea.setText(msgController.getData().toString());\n"
                    + "            iridiumTextArea.setText(msgController.getData().toString());\n"
                    + "        }\n"
                    + "    }\n");
            
        } else if (serialPanel == true && iridiumPanel == false) {
            // For the case serialPanel = true, iridiumPanel = false
            writer.println("    @Override\n"
                    + "    public void updateData(MessageController msgController, USOCEvent ue) {\n"
                    + "        if (ue instanceof SerialEvent) {\n"
                    + "            serialTextArea.setText(((SerialEvent)ue).getMsg());\n"
                    + "        } else if (ue instanceof ErrorEvent) {\n"
                    + "            if (DataSource.SERIAL == ue.getDataSource()) {\n"
                    + "                serialTextArea.setText(((ErrorEvent) ue).getErrorMessage());\n"
                    + "            }\n"
                    + "        } else {\n"
                    + "            serialTextArea.setText(msgController.getData().toString());\n"
                    + "        }\n"
                    + "    }\n");
            
        } else if (serialPanel == false && iridiumPanel == true) {
        // For the case serialPanel = false, iridiumPanel = true
            writer.println("    @Override\n"
                    + "    public void updateData(MessageController msgController, USOCEvent ue) {\n"
                    + "        if (ue instanceof MailEvent) {\n"
                    + "            MailEvent e = (MailEvent) ue;\n"
                    + "            String s = \"\";\n"
                    + "            for (Address from : e.getFrom()) {\n"
                    + "                s += \",\" + from.toString();\n"
                    + "            }\n"
                    + "            //Setting label texts\n"
                    + "            iridiumLastFrom.setText(s.substring(1));\n"
                    + "            iridiumLastSubject.setText(e.getSubject());\n"
                    + "            iridiumLastFilename.setText(e.getFilename());\n"
                    + "            iridiumLastTimestamp.setText(new Date(e.getTimeStampGmail()).toString());\n"
                    + "            iridiumTextArea.setText(msgController.getData().toString());\n"
                    + "        } else if (ue instanceof ErrorEvent) {\n"
                    + "            if (DataSource.MAIL == ue.getDataSource()) {\n"
                    + "                iridiumTextArea.setText(((ErrorEvent) ue).getErrorMessage());\n"
                    + "            }\n"
                    + "        } else {\n"
                    + "            iridiumTextArea.setText(msgController.getData().toString());\n"
                    + "        }\n"
                    + "    }\n");
        }

        // Loops through additional tabs
        for (int counter = 1; counter <= numberOfAddTabs; counter++) {
            // Loops through control items within the tab
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            for (int j = 1; j <= numberOfControlItems; j++) {
                // Writes dummy methods for buttons
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                if (control.equals("button")) {
                    writer.println(stringBuilder[counter - 1][j - 1]);
                }
            }
        }
        
        // Initialize()-method
        writer.println("    @Override \n"
                + "    public void initialize(URL url, ResourceBundle rb) { \n"
                + "        // TODO");
        // Writes initialize content
        if (serialPanel == true && iridiumPanel == true) {
            // For the case serialPanel = true, iridiumPanel = true
            writer.println("        MainController.startPortThread(this);\n"
                    + "        MainController.getInstance().addDataUpdateListener(new UpdateListener());\n"
                    + "        setData();");
        } else if (serialPanel == true && iridiumPanel == false) {
            // For the case serialPanel = true, iridiumPanel = false
            writer.println("        MainController.startPortThread(this);\n"
                    + "        MainController.getInstance().addDataUpdateListener(new UpdateListener());\n"
                    + "        setData();");
        } else if (serialPanel == false && iridiumPanel == true) {
            // For the case serialPanel = false, iridiumPanel = true
            writer.println("        MainController.getInstance().addDataUpdateListener(new UpdateListener());");
        }

        writer.println("    } \n"
                + "}");

        writer.close();
        
        // Prints status update
        System.out.println("LogPanelController.java has been updated!");        
    }

    
    /**
     * The StatePanel.fxml file is regenerated according to the settings of the
     * configuration file. The method builds the FXML structure of the generically
     * in a ScrollPane with an optional number of vertically stacked segments.
     *
     * @param filePath
     * @param configPath
     * @throws java.io.IOException
    */
    public static void StatePanelBuilder(String filePath, String configPath) throws IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        boolean statePanel = Boolean.parseBoolean(config.getProperty("statePanel"));
        int numberOfSegments = ConfigHandler.countItems("segmentTitle", configPath);
        String path = "src/main/resources/";
        String licenceType = ".fxml";

        // Writes data in CurrentStatePanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        // Writes MIT Licence
        writeLicence(licenceType, writer);
        if (statePanel) {
            // Java imports
            writer.println("<?import javafx.scene.text.*?>\n"
                    + "<?import javafx.scene.control.*?> \n"
                    + "<?import javafx.scene.layout.*?> \n"
                    + "<?import javafx.geometry.*?> \n");
            // AnchorPane
            writer.println("<AnchorPane xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">\n"
                    + "  <children>\n"
                    + "    <ScrollPane fitToHeight=\"true\" fitToWidth=\"true\" prefHeight=\"200.0\" prefWidth=\"200.0\" AnchorPane.bottomAnchor=\"0.0\" AnchorPane.leftAnchor=\"0.0\" AnchorPane.rightAnchor=\"0.0\" AnchorPane.topAnchor=\"0.0\" BorderPane.alignment=\"CENTER\"> \n"
                    + "      <content> \n"
                    + "        <VBox> \n"
                    + "          <children>");

            // Loops through segments
            for (int counter = 1; counter <= numberOfSegments; counter++) {
                writer.println("            <GridPane> \n"
                        + "              <columnConstraints> \n"
                        + "                <ColumnConstraints halignment=\"LEFT\" hgrow=\"SOMETIMES\" /> \n"
                        + "                <ColumnConstraints halignment=\"RIGHT\" hgrow=\"SOMETIMES\" /> \n"
                        + "              </columnConstraints> \n"
                        + "              <rowConstraints>");

                // Declares necessary parameters
                int numberOfRows = ConfigHandler.countItems("keyword[" + counter + "]", configPath);
                // Loops through number of rows required for the number of keywords
                for (int i = 1; i <= numberOfRows; i++) {
                    writer.println("                <RowConstraints maxHeight=\"20.0\" minHeight=\"20.0\" prefHeight=\"20.0\" valignment=\"CENTER\" />");
                }
                // Writes segment title
                writer.println("             </rowConstraints> \n"
                        + "              <children> \n"
                        + "                <Label text=\"" + config.getProperty("segmentTitle[" + counter + "]") + "\" GridPane.columnIndex=\"0\" GridPane.rowIndex=\"0\"> \n"
                        + "                  <font> \n"
                        + "                     <Font name=\"System Bold\" size=\"14.0\" /> \n"
                        + "                  </font> \n"
                        + "                </Label>");

                // Writes content of the segment
                for (int j = 1; j <= numberOfRows; j++) {
                    String keyword = config.getProperty("keyword[" + counter + "][" + j + "]");
                    writer.println("                <Label text=\"" + keyword + ":\" GridPane.columnIndex=\"0\" GridPane.rowIndex=\"" + j + "\"> \n"
                            + "                  <font>\n"
                            + "                     <Font size=\"12.0\" />\n"
                            + "                  </font>\n"
                            + "                </Label>\n"
                            + "                <Label fx:id=\"label" + keyword + "\" GridPane.columnIndex=\"1\" GridPane.rowIndex=\"" + j + "\"> \n"
                            + "                  <font>\n"
                            + "                     <Font size=\"12.0\" />\n"
                            + "                  </font>\n"
                            + "                </Label>");
                }
                writer.println("              </children> \n"
                        + "              <padding> \n"
                        + "                <Insets bottom=\"20.0\" /> \n"
                        + "              </padding> \n"
                        + "            </GridPane>");
            }
            writer.println("          </children> \n"
                    + "        </VBox> \n"
                    + "      </content> \n"
                    + "      <padding> \n"
                    + "        <Insets bottom=\"10.0\" left=\"7.0\" right=\"7.0\" top=\"10.0\" /> \n"
                    + "      </padding> \n"
                    + "    </ScrollPane> \n"
                    + "  </children> \n"
                    + "</AnchorPane>");
        } else {
            writer.println("");
        }
        writer.close();
        
        // Prints status update
        System.out.println("StatePanel.fxml has been updated!");
    }
    
    
    /**
     * The StatePanelController.java file is regenerated according to the
     * settings of the configuration file when the method is called.
     *
     * @param filePath
     * @param configPath
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
    */
    public static void statePanelControlBuilder(String filePath, String configPath) throws FileNotFoundException, IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfSegments = ConfigHandler.countItems("segmentTitle", configPath);
        String path = "src/main/java/com/ksatstuttgart/usoc/";
        String licenceType = ".java";

        // Writes data in StatePanelController.java file
        PrintWriter writer = new PrintWriter(path + filePath);
        // Writes MIT Licence
        writeLicence(licenceType, writer);
        // Java package
        writer.println("package com.ksatstuttgart.usoc.gui.controller; \n");
        // Java imports
        writer.println("import java.net.URL; \n"
                + "import java.util.ResourceBundle; \n"
                + "import javafx.fxml.FXML; \n"
                + "import java.awt.Label; \n"
                + "import javafx.fxml.Initializable; \n");
        // Javadoc class header
        writer.println("/** \n"
                + " * <h1>StatePanelController</h1>\n"
                + " * This class ensure the functionality of the StatePanel.\n"
                + " * <p>\n"
                + " * It contains a method for setting and\n"
                + " * updating the labels in the StatePanel. A label is automatically\n"
                + " * set for every entered keyword.\n"
                + " * \n"
                + " * @author Victor Hertel\n"
                + " * @version 1.0\n"
                + "*/");
        writer.println("public class StatePanelController implements Initializable { \n");
        // Loops through segments
        for (int counter = 1; counter <= numberOfSegments; counter++) {
            // Loops through keywords within the segment
            int numberOfRows = ConfigHandler.countItems("keyword[" + counter + "]", configPath);
            for (int i = 1; i <= numberOfRows; i++) {
                // Declares labels for all specified keywords
                String keyword = config.getProperty("keyword[" + counter + "][" + i + "]");
                writer.println("    @FXML Label label" + keyword + ";");
            }
        }
        // Writes updateStates() method
        writer.println("\n    public void updateStates() {");
        // Loops through segments
        for (int counter = 1; counter <= numberOfSegments; counter++) {
            // Loops through keywords within the segment
            int numberOfRows = ConfigHandler.countItems("keyword[" + counter + "]", configPath);
            for (int i = 1; i <= numberOfRows; i++) {
                // Writes dummy command to set the labels of the keywords
                String keyword = config.getProperty("keyword[" + counter + "][" + i + "]");
                writer.println("        label" + keyword + ".setText(\"label" + keyword + "\");");
            }
        }
        writer.println("    }\n");
        // Initialize method
        writer.println("    @Override \n"
                + "    public void initialize(URL url, ResourceBundle rb) { \n"
                + "        // TODO \n"
                + "        updateStates();\n"
                + "    } \n"
                + "}");

        writer.close();
        
        // Prints status update
        System.out.println("StatePanelController.java has been updated!");        
    }
}
