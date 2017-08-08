package com.ksatstuttgart.usoc.gui.controller; 

import java.net.URL; 
import java.util.ResourceBundle; 
import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
import com.ksatstuttgart.usoc.controller.communication.SerialComm;
import com.ksatstuttgart.usoc.controller.MainController;
import java.util.ArrayList;
import javafx.fxml.Initializable; 
import javafx.scene.control.ComboBox; 

/** 
 * 
 * @author Victor 
 */ 

public class LogController implements Initializable { 

    @FXML private ComboBox comboBox1; 
    @FXML private ComboBox comboBox2; 
    @FXML private ComboBox comboBox3; 

    public void setData() { 
        comboBox3.getItems().setAll("A", "B", "C");
        comboBox1.getItems().setAll(SerialComm.getInstance().getAvailableCommands());    } 

    @FXML 
    private void serialConnect(ActionEvent event) { 
        System.out.println("Connect button in serial log has been pressed!"); 
        setData(); 
    } 

    @FXML 
    private void serialSendCommand(ActionEvent event) { 
        System.out.println("Send Command button in serial log has been pressed!"); 
        String output = comboBox1.getSelectionModel().getSelectedItem().toString(); 
        if (output != null) {
            System.out.println(output);
        } else {
            System.out.println("null");
        }    } 

    @FXML 
    private void iridiumOpen(ActionEvent event) { 
        System.out.println("Open button in iridium log has been pressed!"); 
    } 

    @FXML 
    private void iridiumClearData(ActionEvent event) { 
        System.out.println("Clear Data button in iridium log has been pressed!"); 
    } 

    @FXML 
    private void iridiumExportCSV(ActionEvent event) { 
        System.out.println("Export CSV button in iridium log has been pressed!"); 
    } 

    @FXML 
    private void iridiumReconnect(ActionEvent event) { 
        System.out.println("Reconnect button in iridium log has been pressed!"); 
    } 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        MainController.startPortThread(this);
    }

    public void updatePortList(ArrayList<String> portList) {
        if (comboBox2 != null) {
            comboBox2.getItems().setAll(portList);
        }
    }
}
