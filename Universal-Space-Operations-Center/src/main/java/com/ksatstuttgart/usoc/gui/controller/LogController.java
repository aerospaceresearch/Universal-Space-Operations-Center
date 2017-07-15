package com.ksatstuttgart.usoc.gui.controller; 

import java.net.URL; 
import java.util.ResourceBundle; 
import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
import javafx.fxml.Initializable; 

/** 
 * 
 * @author Victor 
 */ 

public class LogController implements Initializable { 

    @FXML 
    private void serialConnect(ActionEvent event) { 
        System.out.println("Connect button in serial log has been pressed!"); 
    } 

    @FXML 
    private void serialSendCommand(ActionEvent event) { 
        System.out.println("Send Command button in serial log has been pressed!"); 
    } 

    @FXML 
    private void iridiumReconnect(ActionEvent event) { 
        System.out.println("Reconnect button in iridium log has been pressed!"); 
    } 

    @FXML 
    private void button11(ActionEvent event) { 
        System.out.println("Button was pressed!"); 
    } 

    @FXML 
    private void button14(ActionEvent event) { 
        System.out.println("Button was pressed!"); 
    } 

    @FXML 
    private void button21(ActionEvent event) { 
        System.out.println("Button was pressed!"); 
    } 

    @Override 
    public void initialize(URL url, ResourceBundle rb) { 
        // TODO 
    } 
}
