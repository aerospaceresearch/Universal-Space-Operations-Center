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
package com.ksatstuttgart.usoc;

import java.util.Properties;
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
public class ConfigHandlerTest {
    
    public ConfigHandlerTest() {
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
     * Test of getAllValues method, of class ConfigHandler.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetAllValues() throws Exception {
        System.out.println("getAllValues() has been tested");
        String expResult = "experimentTestName";
        Properties config = ConfigHandler.getAllValues( "testData/test.properties" );
        String result = config.getProperty("experimentName");
        assertEquals(expResult, result);
    }

    /**
     * Test of updateConfigMod method, of class ConfigHandler.
     * @throws java.lang.Exception
     */
    @Test
    public void testUpdateConfigMod() throws Exception {
        System.out.println("updateConfigMod() has been tested");
        ConfigHandler.updateConfigMod("testData/test.properties", "testData/testMod.properties");
        Properties config = ConfigHandler.getAllValues( "testData/test.properties" );
        Properties configMod = ConfigHandler.getAllValues( "testData/testMod.properties" );
        String expResult = config.getProperty("experimentName");
        String result = configMod.getProperty("experimentName");
        assertEquals(expResult, result);
    }

    /**
     * Test of valueMod method, of class ConfigHandler.
     * @throws java.lang.Exception
     */
    @Test
    public void testValueMod() throws Exception {
        System.out.println("valueMod() has been tested");
        boolean expResult = false;
        boolean result = ConfigHandler.valueMod("experimentName", "testData/test.properties", "testData/testMod.properties");
        assertEquals(expResult, result);
    }
    
    /**
     * Test of experimentNameMod method, of class ConfigHandler.
     * @throws java.lang.Exception
     */
    @Test
    public void testExperimentNameMod() throws Exception {
        System.out.println("experimentNameMod() has been tested");
        boolean expResult = false;
        boolean result = ConfigHandler.experimentNameMod("testData/test.properties", "testData/testMod.properties");
        assertEquals(expResult, result);
    }

    /**
     * Test of chartMod method, of class ConfigHandler.
     * @throws java.lang.Exception
     */
    @Test
    public void testChartMod() throws Exception {
        System.out.println("chartMod() has been tested");
        boolean expResult = false;
        boolean result = ConfigHandler.chartMod("testData/test.properties", "testData/testMod.properties");
        assertEquals(expResult, result);
    }

    /**
     * Test of logMod method, of class ConfigHandler.
     * @throws java.lang.Exception
     */
    @Test
    public void testLogMod() throws Exception {
        System.out.println("logMod() has been tested");
        boolean expResult = false;
        boolean result = ConfigHandler.logMod("testData/test.properties", "testData/testMod.properties");
        assertEquals(expResult, result);
    }

    /**
     * Test of stateMod method, of class ConfigHandler.
     * @throws java.lang.Exception
     */
    @Test
    public void testStateMod() throws Exception {
        System.out.println("stateMod() has been tested");
        boolean expResult = false;
        boolean result = ConfigHandler.stateMod("testData/test.properties", "testData/testMod.properties");
        assertEquals(expResult, result);
    }

    /**
     * Test of fileMod method, of class ConfigHandler.
     * @throws java.lang.Exception
     */
    @Test
    public void testFileMod() throws Exception {
        System.out.println("fileMod() has been tested");
        boolean experimentNameMod = false;
        boolean chartMod = false;
        boolean logMod = true;
        boolean stateMod = false;
        boolean result = ConfigHandler.fileMod(experimentNameMod, chartMod, logMod, stateMod);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of rebuildGui method, of class ConfigHandler.
     * @throws java.lang.Exception
     */
    @Test
    public void testRebuildGui() throws Exception {
        System.out.println("rebuildGui() has been tested");
        boolean result = ConfigHandler.rebuildGui("testData/test.properties", "testData/testMod.properties");
        boolean expResult = false;
        assertEquals(expResult, result);
    }
}
