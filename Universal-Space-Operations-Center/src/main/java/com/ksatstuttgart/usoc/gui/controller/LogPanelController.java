package com.ksatstuttgart.usoc.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// Imports for serialLog or iridiumLog
import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.data.DataSource;
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import javafx.scene.control.TextArea;

// Specific imports for serialLog
import com.ksatstuttgart.usoc.controller.communication.SerialComm;
import java.util.ArrayList;
import com.ksatstuttgart.usoc.data.SerialEvent;
import javafx.scene.control.ComboBox;

// Specific imports for iridiumLog
import com.ksatstuttgart.usoc.controller.communication.MailReceiver;
import com.ksatstuttgart.usoc.data.MailEvent;
import java.util.Date;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.mail.Address;

/** 
 * 
 * @author Victor 
 */ 

public class LogPanelController extends DataController implements Initializable { 

    @FXML private ComboBox comboBox1; 
    @FXML private ComboBox comboBox2; 
    @FXML private ComboBox comboBox3; 
    @FXML private TextArea serialTextArea; 

    @FXML 
    public void setData() { 
        comboBox1.getItems().setAll(SerialComm.getInstance().getAvailableBaudrates()); 
        comboBox3.getItems().setAll(SerialComm.getInstance().getAvailableCommands()); 
    } 

    @FXML 
    public void updatePortList(ArrayList<String> portList) { 
        if (comboBox2 != null) { 
            comboBox2.getItems().setAll(portList); 
        } 
    } 

    @FXML 
    private void serialConnect(ActionEvent event) { 
        System.out.println("Connect button in serial log has been pressed!");
        String port = comboBox2.getSelectionModel().getSelectedItem().toString();
        int baudrate = Integer.parseInt(comboBox1.getSelectionModel().getSelectedItem().toString());
        SerialComm.getInstance().start(port, baudrate); 
    } 

    @FXML 
    private void serialSendCommand(ActionEvent event) { 
        System.out.println("Send Command button in serial log has been pressed!"); 
        String output = comboBox3.getSelectionModel().getSelectedItem().toString(); 
        SerialComm.getInstance().send(output);
    } 

    @FXML private TextArea iridiumTextArea; 
    @FXML private Label iridiumLastFrom; 
    @FXML private Label iridiumLastSubject; 
    @FXML private Label iridiumLastFilename; 
    @FXML private Label iridiumLastTimestamp; 
    @FXML private TextField iridiumReconnectField; 

    @FXML 
    private void iridiumOpen(ActionEvent event) { 
        System.out.println("Open button in iridium log has been pressed!");
        MainController.getInstance().openBinaryFile(); 
    } 

    @FXML 
    private void iridiumClearData(ActionEvent event) { 
        System.out.println("Clear Data button in iridium log has been pressed!");
        MainController.getInstance().clearData(); 
    } 

    @FXML 
    private void iridiumExportCSV(ActionEvent event) { 
        System.out.println("Export CSV button in iridium log has been pressed!");
        MainController.getInstance().exportCSV(); 
    } 

    @FXML 
    private void iridiumReconnect(ActionEvent event) { 
        System.out.println("Reconnect button in iridium log has been pressed!");
        ((Button)event.getSource()).setText("Reconnect");
        int numMessages = 0;
        try {
            numMessages = Integer.parseInt(iridiumReconnectField.getText());
        } catch(NumberFormatException ex){
            //do nothing;
        }
        MailReceiver.getInstance().setMessagesOnReconnect(numMessages);
        MailReceiver.getInstance().reconnect(); 
    } 

    @Override
    public void updateData(MessageController msgController, USOCEvent ue) {
        if (ue instanceof MailEvent) {
            MailEvent e = (MailEvent) ue;
            String s = "";
            for (Address from : e.getFrom()) {
                s += "," + from.toString();
            }
            //Setting label texts
            iridiumLastFrom.setText(s.substring(1));
            iridiumLastSubject.setText(e.getSubject());
            iridiumLastFilename.setText(e.getFilename());
            iridiumLastTimestamp.setText(new Date(e.getTimeStampGmail()).toString());
            iridiumTextArea.setText(msgController.getData().toString());
        } else if (ue instanceof SerialEvent) {
            serialTextArea.setText(((SerialEvent)ue).getMsg());
        } else if (ue instanceof ErrorEvent) {
            if (DataSource.MAIL == ue.getDataSource()) {
                iridiumTextArea.setText(((ErrorEvent) ue).getErrorMessage());
            } else if (DataSource.SERIAL == ue.getDataSource()) {
                serialTextArea.setText(((ErrorEvent) ue).getErrorMessage());
            }
        } else {
            serialTextArea.setText(msgController.getData().toString());
            iridiumTextArea.setText(msgController.getData().toString());
        }
    }

    @FXML 
    private void button11(ActionEvent event) { 
        // Automatically generated method button11() 
        System.out.println("Button11 was pressed!"); 
    } 

    @FXML 
    private void button14(ActionEvent event) { 
        // Automatically generated method button14() 
        System.out.println("Button14 was pressed!"); 
    } 

    @FXML 
    private void button21(ActionEvent event) { 
        // Automatically generated method button21() 
        System.out.println("Button21 was pressed!"); 
    } 

    @FXML 
    private void button26(ActionEvent event) { 
        // Automatically generated method button26() 
        System.out.println("Button26 was pressed!"); 
    } 

    @Override 
    public void initialize(URL url, ResourceBundle rb) { 
        // TODO
        MainController.startPortThread(this);
        MainController.getInstance().addDataUpdateListener(new UpdateListener());
        setData();
    } 
}
