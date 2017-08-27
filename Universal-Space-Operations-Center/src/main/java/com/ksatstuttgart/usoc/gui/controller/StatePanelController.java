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
package com.ksatstuttgart.usoc.gui.controller; 

import java.net.URL; 
import java.util.ResourceBundle; 
import javafx.fxml.FXML; 
import java.awt.Label; 
import javafx.fxml.Initializable; 

/** 
 * <h1>StatePanelController</h1>
 * This class ensure the functionality of the StatePanel.
 * <p>
 * It contains a method for setting and
 * updating the labels in the StatePanel. A label is automatically
 * set for every entered keyword.
 * 
 * @author Victor Hertel
 * @version 1.0
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
