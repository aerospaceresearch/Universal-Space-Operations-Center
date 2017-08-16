package com.ksatstuttgart.usoc.gui.controller; 

import java.net.URL; 
import java.util.ResourceBundle; 
import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
import com.ksatstuttgart.usoc.controller.communication.SerialComm;
import com.ksatstuttgart.usoc.controller.communication.MailReceiver;
import com.ksatstuttgart.usoc.controller.MainController;
import java.util.ArrayList;
import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.data.DataSource;
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.MailEvent;
import com.ksatstuttgart.usoc.data.SerialEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import javafx.fxml.Initializable; 
import javafx.scene.control.ComboBox; 
import javafx.scene.control.TextArea;
import javax.mail.Address; 

/** 
 * 
 * @author Victor 
 */ 

public class LogController extends DataController implements Initializable { 

    @FXML private ComboBox comboBox1; 
    @FXML private ComboBox comboBox2; 
    @FXML private ComboBox comboBox3; 
    @FXML private TextArea serialTextArea; 

    public void setData() { 
        comboBox1.getItems().setAll(SerialComm.getInstance().getAvailableBaudrates()); 
        comboBox3.getItems().setAll(SerialComm.getInstance().getAvailableCommands()); 
    } 

    public void updatePortList(ArrayList<String> portList) { 
        if (comboBox2 != null) { 
            comboBox2.getItems().setAll(portList); 
        } 
    } 

    public void serialWriteLog() { 
        serialTextArea.setText("Test"); 
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
        MailReceiver.getInstance().reconnect(); 
    } 

    @Override 
    public void initialize(URL url, ResourceBundle rb) { 
        // TODO 
        MainController.startPortThread(this);
        MainController.getInstance().addDataUpdateListener(new UpdateListener());   
        //setData(); 
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
            //lastFrom.setText(s.substring(1));
            //lastSubject.setText(e.getSubject());
            //lastFilename.setText(e.getFilename());
            //lastTimestamp.setText(new Date(e.getTimeStampGmail()).toString());

            //TODO: change this to iridiumTextArea
            serialTextArea.setText(msgController.getData().toString());
        } else if (ue instanceof SerialEvent) {
            serialTextArea.setText(((SerialEvent)ue).getMsg());
        } else if (ue instanceof ErrorEvent) {
            if (DataSource.MAIL == ue.getDataSource()) {
                //TODO: change this to iridiumTextArea
                serialTextArea.setText(((ErrorEvent) ue).getErrorMessage());
            } else if (DataSource.SERIAL == ue.getDataSource()) {
                serialTextArea.setText(((ErrorEvent) ue).getErrorMessage());
            }
        } else {
            serialTextArea.setText(msgController.getData().toString());
        }
    }
}
