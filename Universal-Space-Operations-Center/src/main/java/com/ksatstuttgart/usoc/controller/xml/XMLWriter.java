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
package com.ksatstuttgart.usoc.controller.xml;

import com.ksatstuttgart.usoc.data.message.SBD340;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXB;

/**
 *
 * @author valentinstarlinger
 */
public class XMLWriter {

    private static XMLWriter instance;

    public static XMLWriter getInstance() {
        if(instance == null){
            instance = new XMLWriter();
        }
        return instance;
    }

    public void saveMessageStructure(SBD340 message) throws IOException {
        XMLWriter.this.saveMessageStructure(message, getDefaultFileName());
    }

    public void saveMessageStructure(SBD340 message, String filename) throws IOException {
        writeXML(message,new File("protocols",filename));
    }
    
    /**
     * TODO implement default file name
     * @return 
     */
    private String getDefaultFileName(){
        return "";
    }
    
    private void writeXML(Object o, File f) throws IOException {
        
        if (!f.exists()) {
            f.createNewFile();
        }
        
        JAXB.marshal(o, f);
    }

}
