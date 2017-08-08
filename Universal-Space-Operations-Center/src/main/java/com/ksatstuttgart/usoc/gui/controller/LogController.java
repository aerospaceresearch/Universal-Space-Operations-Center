package com.ksatstuttgart.usoc.gui.controller; 

import java.net.URL; 
import java.util.ResourceBundle; 
import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
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
        comboBox1.getItems().clear(); 
        comboBox2.getItems().clear(); 
        comboBox3.getItems().clear(); 
        comboBox1.getItems().addAll("H", "Ha", "Hal", "Hall", "Hallo"); 
        comboBox2.getItems().addAll("T", "Te", "Tes", "Test"); 
        comboBox3.getItems().addAll("A", "B", "C"); 
    } 

    @FXML 
    private void serialConnect(ActionEvent event) { 
        System.out.println("Connect button in serial log has been pressed!"); 
        setData(); 
    } 

    @FXML 
    private void serialSendCommand(ActionEvent event) { 
        System.out.println("Send Command button in serial log has been pressed!"); 
        String output = comboBox1.getSelectionModel().getSelectedItem().toString(); 
        System.out.println(output); 
    } 

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
    } 
}
