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
package com.ksatstuttgart.usoc.gui.setup;

import com.ksatstuttgart.usoc.gui.controller.DataController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Victor
 */
public class USOCTabPane extends TabPane {
    
    public USOCTabPane(){
        super();
    }
    
    public void addFXMLTab(String resourcePath){
        try {
            Tab tab = new Tab();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(resourcePath));
            Pane pane = fxmlLoader.load();
            tab.setContent(pane);
            this.getTabs().add(tab);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void addFXMLTab(String resourcePath, DataController controller) {
        try {
            Tab tab = new Tab();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourcePath));
            fxmlLoader.setController(controller);
            Pane pane = fxmlLoader.load();
            tab.setContent(pane);
            this.getTabs().add(tab);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
