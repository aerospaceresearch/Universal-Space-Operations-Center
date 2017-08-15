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
package com.ksatstuttgart.usoc.test.gui;

import com.ksatstuttgart.usoc.GuiBuilder;
import java.io.IOException;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Victor
 */
public class GuiBuilderTest {
    
    public GuiBuilderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setExperimentName method, of class GuiBuilder.
     * @throws java.io.IOException
     */
    @Ignore 
    @Test
    public void testSetExperimentName() throws IOException {
        System.out.println("setExperimentName() has been tested");
        Stage stage = null;
        int expResult = 0;
        int result = GuiBuilder.setExperimentName(stage, "testData/test.properties");
        assertEquals(expResult, result);
    }

    /**
     * Test of getGridPosition method, of class GuiBuilder.
     */
    @Test
    public void testGetGridPosition() {
        System.out.println("getGridPosition() has been tested");
        int input = 7;
        int[] expResult = {0,3};
        int[] result = GuiBuilder.getGridPosition(input);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of chartBuilder method, of class GuiBuilder.
     * @throws java.lang.Exception
     */
    @Test
    public void testChartBuilder() throws Exception {
        System.out.println("chartBuilder() has been tested");
        GuiBuilder.chartBuilder("testData/ChartPanel.fxml", "testData/test.properties");
    }

    /**
     * Test of logBuilder method, of class GuiBuilder.
     * @throws java.lang.Exception
     */
    @Test
    public void testLogBuilder() throws Exception {
        System.out.println("logBuilder() has been tested");
        GuiBuilder.logBuilder("testData/LogPanel.fxml", "testData/test.properties");
    }

    /**
     * Test of currentStateBuilder method, of class GuiBuilder.
     * @throws java.lang.Exception
     */
    @Test
    public void testCurrentStateBuilder() throws Exception {
        System.out.println("currentStateBuilder() has been tested");
        GuiBuilder.currentStateBuilder("testData/CurrentStatePanel.fxml", "testData/test.properties");
    }
    
}
