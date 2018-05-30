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

import com.ksatstuttgart.usoc.controller.MainController;
import com.ksatstuttgart.usoc.controller.communication.SerialComm;
import com.ksatstuttgart.usoc.gui.InitialWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <h1>Ground Segment</h1>
 * <p>
 * <p>
 * This is the main Wrapper class initiating the GUI.
 *
 * @author Valentin Starlinger
 * @version 1.0
 */
public class GroundSegment extends Application {

    /**
     * Starts Application
     *
     * @param stage main stage
     */
    @Override
    public void start(Stage stage) {
        //Setting the stage for the MainController
        MainController.getInstance().setStage(stage);

        stage.setScene(new Scene(new InitialWindow()));
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

        //close serial communication if it is open on window close
        if (SerialComm.getInstance().isOpen()) {
            SerialComm.getInstance().close();
        }
    }

    // Closes all threads on Apllication close
    // May not be the best solution in the long run,
    // since this force closes all active threads
    @Override
    public void stop() throws Exception {
        super.stop();
        //TODO temp fix for worldwind panels not closing on app stop
        //TODO system.exit force closes all threads, but it is non optimal
        System.exit(0);
    }
}
