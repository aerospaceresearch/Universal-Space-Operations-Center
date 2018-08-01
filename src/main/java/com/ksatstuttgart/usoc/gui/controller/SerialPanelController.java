/*
 * MIT License
 *
 * Copyright (c) 2017 KSat e.V. and AerospaceResearch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
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
import javafx.scene.control.ComboBox;

// Specific imports for serialLog
import com.ksatstuttgart.usoc.controller.communication.SerialComm;
import com.ksatstuttgart.usoc.controller.communication.SerialCommand;
import com.ksatstuttgart.usoc.data.SerialEvent;
import java.util.List;

/** 
 * <h1>LogPanelController</h1>
 * This class ensures the functionality of the LogPanel.
 * <p>
 * Apart from pre-programmed methods for the SerialLog and IridiumLog,
 * a dummy method is written for each button of the individually designed
 * tabs. These methods can be supplemented manually. Those method contents are
 * recognized and will not be lost if the button label has been changed and the
 * corresponding FXML structure and controller regenerated.
 * The LogPanelController is generated automatically.
 * 
 * @author Victor Hertel
 * @version 1.0
*/
public class SerialPanelController extends DataController implements Initializable { 

    @FXML private ComboBox baudrateBox; 
    @FXML private ComboBox portBox; 
    @FXML private ComboBox commandBox; 
    @FXML private TextArea serialTextArea; 

    @FXML 
    public void setData() { 
        baudrateBox.getItems().setAll(SerialComm.getInstance().getAvailableBaudrates()); 
        commandBox.getItems().setAll(SerialComm.getInstance().getAvailableCommands()); 
    } 

    @FXML 
    public void updatePortList(List<String> portList) { 
        if (portBox != null) { 
            portBox.getItems().setAll(portList); 
        } 
    } 

    @FXML 
    private void serialConnect(ActionEvent event) { 
        System.out.println("Connect button in serial log has been pressed!");
        
        if(!portBox.getSelectionModel().isEmpty() && !baudrateBox.getSelectionModel().isEmpty()){
            String port = portBox.getSelectionModel().getSelectedItem().toString();
            int baudrate = Integer.parseInt(baudrateBox.getSelectionModel().getSelectedItem().toString());

            SerialComm.getInstance().start(port, baudrate);
        }
    } 

    @FXML
    private void serialSendCommand(ActionEvent event) { 
        System.out.println("Send Command button in serial log has been pressed!"); 
        if(!commandBox.getSelectionModel().isEmpty()){
            String output = commandBox.getSelectionModel().getSelectedItem().toString();
            output = SerialCommand.valueOf(output).getCommand();
            SerialComm.getInstance().send(output);
        }
    } 

    @Override
    public void updateData(MessageController msgController, USOCEvent ue) {
        if (ue instanceof SerialEvent) {
            serialTextArea.setText(serialTextArea.getText() + "\n" 
                        + ((SerialEvent) ue).getMsg());
        } else if (ue instanceof ErrorEvent) {
            if (DataSource.SERIAL == ue.getDataSource()) {
                serialTextArea.setText(serialTextArea.getText() + "\n" 
                        + ((ErrorEvent) ue).getErrorMessage());
            }
        } else {
            serialTextArea.setText(msgController.getData().toString());
        }
    }
    
    @Override 
    public void initialize(URL url, ResourceBundle rb) { 
        MainController.startPortThread(this);
        MainController.getInstance().addDataUpdateListener(new DataController.UpdateListener());
        setData();
    } 
}
