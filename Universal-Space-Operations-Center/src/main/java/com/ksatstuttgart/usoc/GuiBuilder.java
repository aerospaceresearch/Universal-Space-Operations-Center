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
 * This class builds the GUI FXML structure based on input parameters in the
 * properties file and rebuilds parts of it depending on changes.
 *
 *
 * @author Victor Hertel
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

        while ((line = bufferedReader.readLine()) != null) {

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
                stringBuilder.append(line).append(separator);
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
     * Method builds the FXML structure of the charts generically in a
     * scrollable GridPane with two columns
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
        writer.println("<BorderPane maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"400.0\" prefWidth=\"600.0\" xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">\n");
        writer.println("   <top>\n"
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
                + "   </top>\n");
        if (statePanel) {
            writer.println("   <left> \n"
                    + "      <fx:include source = \"/fxml/StatePanel.fxml\" /> \n"
                    + "   </left> \n");
        }
        writer.println("   <center> \n"
                + "      <fx:include source = \"/fxml/MainPanel.fxml\" /> \n"
                + "   </center> \n\n"
                + "   <right> \n"
                + "      <fx:include source = \"/fxml/LogPanel.fxml\" /> \n"
                + "   </right> \n\n"
                + "</BorderPane> \n");
        
        writer.close();
        
        // Prints status update
        System.out.println("MainFrame.fxml has been updated!");
    }

    /**
     * Method builds the FXML structure of the charts generically in a
     * scrollable GridPane with two columns
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

        // Initializes number of rows depending on required number of charts
        if (numberOfCharts % 2 == 0) {
            numberOfRows = numberOfCharts / 2;
        } else {
            numberOfRows = (numberOfCharts + 1) / 2;
        }
        
        if (config.getProperty("chartTitle[1]") == null) {
            chartTab = false;
        } else {
            chartTab = true;
        }

        // Writes data in ChartPanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("<?import javafx.scene.chart.*?> \n"
                + "<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        
        writer.println("<TabPane xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller = \"com.ksatstuttgart.usoc.gui.controller.MainPanelController\" tabClosingPolicy=\"UNAVAILABLE\" BorderPane.alignment=\"CENTER\"> \n"
                    + "   <tabs>");
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
            for (int i = 1; i <= (numberOfRows); i++) {
                writer.println("                <RowConstraints maxHeight=\"400.0\" minHeight=\"200.0\" prefHeight=\"300.0\" vgrow=\"SOMETIMES\" />");
            }
            writer.println("              </rowConstraints>\n"
                    + "              <children>");
            for (int counter = 1; counter <= numberOfCharts; counter++) {
                int[] position = getGridPosition(counter);
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
     * Method writes the controller of the chart panel generically depending on
     * input in the properties file
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

        // Writes data in LogController.java file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("package com.ksatstuttgart.usoc.gui.controller; \n");
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
                + "import javafx.scene.chart.XYChart;");
        if (GNSS3dView) {
            writer.println("import com.ksatstuttgart.usoc.gui.worldwind.GNSSPanel;\n"
                    + "import javafx.fxml.FXML;\n"
                    + "import javafx.scene.layout.StackPane;\n");
        }
        writer.println("/** \n"
                + " * \n"
                + " * @author Victor \n"
                + " */");
        writer.println("public class MainPanelController extends DataController implements Initializable { \n");
        for (int i = 1; i <= numberOfCharts; i++) {
            writer.println("    @FXML\n"
                    + "    public LineChart<Number, Number> lineChart" + i + ";");
        }
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
        if (GNSS3dView) {
            writer.println("    @FXML\n"
                    + "    private StackPane pane;\n");
        }
        writer.println("    @Override \n"
                + "    public void initialize(URL url, ResourceBundle rb) { \n"
                + "        // TODO\n"
                + "        MainController.getInstance().addDataUpdateListener(new UpdateListener());");
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
     * Method builds the FXML structure of the log panel generically in a
     * TabPane with an optional number of additional tabs.
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

        // Writes data in LogPanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("<?import java.lang.*?> \n"
                + "<?import javafx.geometry.*?> \n"
                + "<?import javafx.collections.*?> \n"
                + "<?import javafx.scene.*?> \n"
                + "<?import javafx.scene.control.*?> \n"
                + "<?import javafx.scene.layout.*?> \n");
        writer.println("<TabPane xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller = \"com.ksatstuttgart.usoc.gui.controller.LogPanelController\" maxWidth=\"265.0\" minWidth=\"265.0\" prefWidth=\"265.0\" tabClosingPolicy=\"UNAVAILABLE\" BorderPane.alignment=\"CENTER\"> \n"
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

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append(separator);
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

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append(separator);
            }
            writer.println(stringBuilder);

            writer.println("      </content> \n"
                    + "    </Tab>");
        }

        // Generates addiditional tabs
        for (int counter = 1; counter <= numberOfAddTabs; counter++) {
            writer.println("    <Tab text=\"" + config.getProperty("tabTitle[" + counter + "]") + "\"> \n"
                    + "      <content> \n"
                    + "<BorderPane> \n"
                    + "<padding><Insets top=\"5\" right=\"5\" bottom=\"5\" left=\"5\"/></padding> \n"
                    + "  <top> \n"
                    + "    <GridPane alignment=\"TOP_CENTER\" BorderPane.alignment=\"CENTER\">\n"
                    + "      <columnConstraints> \n"
                    + "        <ColumnConstraints hgrow=\"SOMETIMES\" /> \n"
                    + "        <ColumnConstraints hgrow=\"SOMETIMES\" /> \n"
                    + "      </columnConstraints> \n"
                    + "      <rowConstraints>");

            // Declares necessary parameters
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            int numberOfRows;

            // Sets number of rows depending on required number of control items
            if (numberOfControlItems % 2 == 0) {
                numberOfRows = numberOfControlItems / 2;
            } else {
                numberOfRows = (numberOfControlItems + 1) / 2;
            }

            // Writes FXML data
            for (int i = 1; i <= numberOfRows; i++) {
                writer.println("        <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />");
            }
            writer.println("     </rowConstraints> \n"
                    + "      <children>");

            // Writes FXML data of tab content
            for (int j = 1; j <= numberOfControlItems; j++) {

                // Gets grid position for control item
                int[] position = getGridPosition(j);
                String control = config.getProperty("control[" + counter + "][" + j + "]");

                // Checks for type of entered control item
                switch (control) {
                    case "button":
                        writer.println("        <Button text=\"" + config.getProperty("bText[" + counter + "][" + j + "]") + "\" onAction=\"#button" + counter + j + "\" "
                                + "mnemonicParsing=\"false\"" + " GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" maxWidth=\"-Infinity\" GridPane.valignment=\"CENTER\" GridPane.vgrow=\"SOMETIMES\" GridPane.halignment=\"RIGHT\" GridPane.hgrow=\"SOMETIMES\"/>");
                        break;
                    case "textField":
                        writer.println("        <TextField promptText=\"" + config.getProperty("promptText[" + counter + "][" + j + "]")
                                + "\" " + "GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" GridPane.halignment=\"RIGHT\" maxWidth=\"60.0\" prefWidth=\"60.0\" GridPane.hgrow=\"SOMETIMES\" GridPane.valignment=\"CENTER\" GridPane.vgrow=\"SOMETIMES\" />");
                        break;
                    case "label":
                        writer.println("        <Label text=\"" + config.getProperty("lText[" + counter + "][" + j + "]")
                                + "\" " + "GridPane.columnIndex=\"" + position[0] + "\" GridPane.rowIndex=\"" + position[1] + "\" GridPane.halignment=\"RIGHT\" GridPane.hgrow=\"SOMETIMES\" GridPane.valignment=\"CENTER\" GridPane.vgrow=\"SOMETIMES\" />");
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
        System.out.println("LogPanel.fxml has been updated!");
    }

    /**
     * Method writes the controller of the log panel generically depending on
     * input in the properties file
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

        for (int counter = 1; counter <= numberOfAddTabs; counter++) {
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            for (int j = 1; j <= numberOfControlItems; j++) {
                if (j > maxNumberOfItems) {
                    maxNumberOfItems = j;
                }
            }
        }

        StringBuilder[][] stringBuilder = new StringBuilder[numberOfAddTabs][maxNumberOfItems];
        FileReader fileReader = new FileReader(path + filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        for (int counter = 1; counter <= numberOfAddTabs; counter++) {
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            for (int j = 1; j <= numberOfControlItems; j++) {
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                if (control.equals("button")) {
                    stringBuilder[counter - 1][j - 1] = writeMethod(bufferedReader, counter, j);
                }
            }
        }

        // Writes data in LogController.java file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("package com.ksatstuttgart.usoc.gui.controller;\n");
        writer.println("import java.net.URL;\n"
                + "import java.util.ResourceBundle;\n"
                + "import javafx.event.ActionEvent;\n"
                + "import javafx.fxml.FXML;\n"
                + "import javafx.fxml.Initializable;\n");
        if (serialPanel == true || iridiumPanel == true) {
            writer.println("// Imports for serialLog or iridiumLog\n"
                    + "import com.ksatstuttgart.usoc.controller.MainController;\n"
                    + "import com.ksatstuttgart.usoc.controller.MessageController;\n"
                    + "import com.ksatstuttgart.usoc.data.DataSource;\n"
                    + "import com.ksatstuttgart.usoc.data.ErrorEvent;\n"
                    + "import com.ksatstuttgart.usoc.data.USOCEvent;\n"
                    + "import javafx.scene.control.TextArea;\n");
        }
        if (serialPanel) {
            writer.println("// Specific imports for serialLog\n"
                    + "import com.ksatstuttgart.usoc.controller.communication.SerialComm;\n"
                    + "import java.util.ArrayList;\n"
                    + "import com.ksatstuttgart.usoc.data.SerialEvent;\n"
                    + "import javafx.scene.control.ComboBox;\n");
        }
        if (iridiumPanel) {
            writer.println("// Specific imports for iridiumLog\n"
                    + "import com.ksatstuttgart.usoc.controller.communication.MailReceiver;\n"
                    + "import com.ksatstuttgart.usoc.data.MailEvent;\n"
                    + "import java.util.Date;\n"
                    + "import javafx.scene.control.Label;\n"
                    + "import javax.mail.Address;\n");
        }
        
        writer.println("/** \n"
                + " * \n"
                + " * @author Victor \n"
                + " */ \n");
        writer.print("public class LogPanelController ");
        if (serialPanel || iridiumPanel) {
            writer.print("extends DataController ");
        }
        writer.println("implements Initializable { \n");

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

        if (iridiumPanel) {
            writer.println("    @FXML private TextArea iridiumTextArea; \n"
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
                    + "        System.out.println(\"Reconnect button in iridium log has been pressed!\");\n"
                    + "        MailReceiver.getInstance().reconnect(); \n"
                    + "    } \n");
        }
        
        if (serialPanel == true && iridiumPanel == true) {
            
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

        for (int counter = 1; counter <= numberOfAddTabs; counter++) {
            int numberOfControlItems = ConfigHandler.countItems("control[" + counter + "]", configPath);
            for (int j = 1; j <= numberOfControlItems; j++) {
                String control = config.getProperty("control[" + counter + "][" + j + "]");
                if (control.equals("button")) {
                    writer.println(stringBuilder[counter - 1][j - 1]);
                }
            }
        }
        
        writer.println("    @Override \n"
                + "    public void initialize(URL url, ResourceBundle rb) { \n"
                + "        // TODO");
        
        if (serialPanel == true && iridiumPanel == true) {
            writer.println("        MainController.startPortThread(this);\n"
                    + "        MainController.getInstance().addDataUpdateListener(new UpdateListener());\n"
                    + "        setData();");
        } else if (serialPanel == true && iridiumPanel == false) {
            writer.println("        MainController.startPortThread(this);\n"
                    + "        MainController.getInstance().addDataUpdateListener(new UpdateListener());\n"
                    + "        setData();");
        } else if (serialPanel == false && iridiumPanel == true) {
            writer.println("        MainController.getInstance().addDataUpdateListener(new UpdateListener());");
        }

        writer.println("    } \n"
                + "}");

        writer.close();
        
        // Prints status update
        System.out.println("LogPanelController.java has been updated!");        
    }

    /**
     * Method builds the FXML structure of the current state panel generically
     * in a Scrollpane with an optional number of vertical boxes.
     *
     * @param filePath
     * @param configPath
     * @throws java.io.IOException
     */
    public static void StatePanelBuilder(String filePath, String configPath) throws IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        boolean statePanel = Boolean.parseBoolean(config.getProperty("statePanel"));
        int numberOfBoxes = ConfigHandler.countItems("boxTitle", configPath);
        String path = "src/main/resources/";

        // Writes data in CurrentStatePanel.fxml file
        PrintWriter writer = new PrintWriter(path + filePath);

        if (statePanel) {
            writer.println("<?import javafx.scene.text.*?>\n"
                    + "<?import javafx.scene.control.*?> \n"
                    + "<?import javafx.scene.layout.*?> \n"
                    + "<?import javafx.geometry.*?> \n");
            writer.println("<AnchorPane xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">\n"
                    + "  <children>\n"
                    + "    <ScrollPane fitToHeight=\"true\" fitToWidth=\"true\" prefHeight=\"200.0\" prefWidth=\"200.0\" AnchorPane.bottomAnchor=\"0.0\" AnchorPane.leftAnchor=\"0.0\" AnchorPane.rightAnchor=\"0.0\" AnchorPane.topAnchor=\"0.0\" BorderPane.alignment=\"CENTER\"> \n"
                    + "      <content> \n"
                    + "        <VBox> \n"
                    + "          <children>");

            // Generates boxes
            for (int counter = 1; counter <= numberOfBoxes; counter++) {
                writer.println("            <GridPane> \n"
                        + "              <columnConstraints> \n"
                        + "                <ColumnConstraints halignment=\"LEFT\" hgrow=\"SOMETIMES\" /> \n"
                        + "                <ColumnConstraints halignment=\"RIGHT\" hgrow=\"SOMETIMES\" /> \n"
                        + "              </columnConstraints> \n"
                        + "              <rowConstraints>");

                // Declares necessary parameters
                int numberOfRows = ConfigHandler.countItems("keyword[" + counter + "]", configPath);

                // Writes FXML data
                for (int i = 1; i <= numberOfRows; i++) {
                    writer.println("                <RowConstraints maxHeight=\"20.0\" minHeight=\"20.0\" prefHeight=\"20.0\" valignment=\"CENTER\" />");
                }
                writer.println("             </rowConstraints> \n"
                        + "              <children> \n"
                        + "                <Label text=\"" + config.getProperty("boxTitle[" + counter + "]") + "\" GridPane.columnIndex=\"0\" GridPane.rowIndex=\"0\"> \n"
                        + "                  <font> \n"
                        + "                     <Font name=\"System Bold\" size=\"14.0\" /> \n"
                        + "                  </font> \n"
                        + "                </Label>");

                // Writes FXML data of box content
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
     * Method writes the controller of the state panel generically depending on
     * input in the properties file
     *
     * @param filePath
     * @param configPath
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void statePanelControlBuilder(String filePath, String configPath) throws FileNotFoundException, IOException {

        // Declares necessary parameters
        Properties config = ConfigHandler.getAllValues(configPath);
        int numberOfBoxes = ConfigHandler.countItems("boxTitle", configPath);
        String path = "src/main/java/com/ksatstuttgart/usoc/";

        // Writes data in LogController.java file
        PrintWriter writer = new PrintWriter(path + filePath);
        writer.println("package com.ksatstuttgart.usoc.gui.controller; \n");
        writer.println("import java.net.URL; \n"
                + "import java.util.ResourceBundle; \n"
                + "import javafx.fxml.FXML; \n"
                + "import java.awt.Label; \n"
                + "import javafx.fxml.Initializable; \n");
        writer.println("/** \n"
                + " * \n"
                + " * @author Victor \n"
                + " */ \n");
        writer.println("public class StatePanelController implements Initializable { \n");
        
        for (int counter = 1; counter <= numberOfBoxes; counter++) {
            int numberOfRows = ConfigHandler.countItems("keyword[" + counter + "]", configPath);
            for (int i = 1; i <= numberOfRows; i++) {
                String keyword = config.getProperty("keyword[" + counter + "][" + i + "]");
                writer.println("    @FXML Label label" + keyword + ";");
            }
        }
        writer.println("\n"
                + "    public void updateStates() {");
        for (int counter = 1; counter <= numberOfBoxes; counter++) {
            int numberOfRows = ConfigHandler.countItems("keyword[" + counter + "]", configPath);
            for (int i = 1; i <= numberOfRows; i++) {
                String keyword = config.getProperty("keyword[" + counter + "][" + i + "]");
                writer.println("        label" + keyword + ".setText(\"label" + keyword + "\");");
            }
        }
        writer.println("    }\n");
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
