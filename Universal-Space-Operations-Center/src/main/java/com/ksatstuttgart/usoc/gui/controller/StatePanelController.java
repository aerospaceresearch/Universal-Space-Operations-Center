package com.ksatstuttgart.usoc.gui.controller; 

import java.net.URL; 
import java.util.ResourceBundle; 
import javafx.fxml.FXML; 
import java.awt.Label; 
import javafx.fxml.Initializable; 

/** 
 * 
 * @author Victor 
 */ 

public class StatePanelController implements Initializable { 

    @FXML Label labelSafe;
    @FXML Label labelIdle;
    @FXML Label labelPreparation;
    @FXML Label labelExecution;
    @FXML Label labelObservation;
    @FXML Label labelDiagnosis;
    @FXML Label labelTestLabel1;
    @FXML Label labelTestLabel2;

    public void updateStates() {
        labelSafe.setText("labelSafe");
        labelIdle.setText("labelIdle");
        labelPreparation.setText("labelPreparation");
        labelExecution.setText("labelExecution");
        labelObservation.setText("labelObservation");
        labelDiagnosis.setText("labelDiagnosis");
        labelTestLabel1.setText("labelTestLabel1");
        labelTestLabel2.setText("labelTestLabel2");
    }

    @Override 
    public void initialize(URL url, ResourceBundle rb) { 
        // TODO 
        updateStates();
    } 
}
