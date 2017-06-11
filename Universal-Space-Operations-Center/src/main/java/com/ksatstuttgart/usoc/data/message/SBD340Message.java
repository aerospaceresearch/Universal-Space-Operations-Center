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
package com.ksatstuttgart.usoc.data.message;

import com.ksatstuttgart.usoc.data.message.dataPackage.DataPackage;
import com.ksatstuttgart.usoc.data.message.header.Header;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author valentinstarlinger
 */

@XmlRootElement (namespace = "usoc/")
public class SBD340Message {
    private Header header;
    private DataPackage dataPackage;
    private ProtocolType protocol;

    public SBD340Message() {
        header = new Header();
        dataPackage = new DataPackage();
    }

    @XmlElement ( name = "header" )
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @XmlElement ( name = "datapackage" )
    public DataPackage getDataPackage() {
        return dataPackage;
    }

    public void setDataPackage(DataPackage dataPackage) {
        this.dataPackage = dataPackage;
    }

    @XmlAttribute (name = "protocol")
    public ProtocolType getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolType protocol) {
        System.out.println("set protocol to: "+protocol);
        this.protocol = protocol;
    }

    /**
     * Only shows whether 
     * @param o 
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof SBD340Message){
            SBD340Message sbd = (SBD340Message)o;
            return (sbd.getDataPackage().equals(this.dataPackage) && 
                    sbd.getHeader().equals(this.header) &&
                    sbd.getProtocol() == this.protocol);
        } else{
            return false;
        }
    }

    @Override
    public String toString(){
        String s = "SBD340Message:\nType: "+this.protocol
                +"\nHeader:\n"+this.header.toString()
                +"\nDataPackage\n"+this.dataPackage.toString();
        
        return s;
    }
    
}
