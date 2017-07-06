/*
 * The MIT License
 *
 * Copyright 2017 KSat e.V.
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

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <h1>Ground Segment</h1>
 * 
 * <p>
 * This is the main Wrapper class initiating the GUI.
 * 
 *
 * @author  Valentin Starlinger
 * @version 1.0
*/
public class GroundSegment extends Application {

    @Override
    public void start(Stage stage) {

        try {
            /**
             * Checks whether a regeneration of the FXML structure is
             * necessary and carries it out in case it is
            */
            ConfigHandler.rebuildGui("config/config.properties", "config/configMod.properties");

            // JavaFX GUI
            String fxmlFile = "/fxml/MainFrame.fxml";
            URL location = getClass().getResource(fxmlFile);

            FXMLLoader loader = new FXMLLoader( );
            loader.setLocation( location );
            Parent root = (Parent) loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}