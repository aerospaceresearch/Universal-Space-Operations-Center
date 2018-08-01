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

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.controller.MessageController;
import com.ksatstuttgart.usoc.controller.communication.MailReceiver;
import com.ksatstuttgart.usoc.data.DataSource;
import com.ksatstuttgart.usoc.data.ErrorEvent;
import com.ksatstuttgart.usoc.data.MailEvent;
import com.ksatstuttgart.usoc.data.USOCEvent;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.mail.Address;

/**
 *
 * @author Victor
 */
public class IridiumPanelController extends DataController {
    
    @FXML private TextArea iridiumTextArea; 
    @FXML private TextField iridiumReconnectField; 
    @FXML private Label iridiumLastFrom; 
    @FXML private Label iridiumLastSubject; 
    @FXML private Label iridiumLastFilename; 
    @FXML private Label iridiumLastTimestamp; 

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
        //MailReceiver.getInstance().setMessagesOnReconnect(numMessages); 
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
        } else if (ue instanceof ErrorEvent) {
            if (DataSource.MAIL == ue.getDataSource()) {
                iridiumTextArea.setText(((ErrorEvent) ue).getErrorMessage());
            }
        } else {
            iridiumTextArea.setText(msgController.getData().toString());
        }
    }
}
