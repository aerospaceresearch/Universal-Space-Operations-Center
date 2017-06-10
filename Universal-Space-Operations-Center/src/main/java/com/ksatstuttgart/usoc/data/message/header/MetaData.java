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
package com.ksatstuttgart.usoc.data.message.header;

import com.ksatstuttgart.usoc.data.message.DataPoint;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author valentinstarlinger
 */
public class MetaData {

    private MetaDataType type;
    private String metaDataName;
    private ArrayList<DataPoint> datapoints;

    public MetaData() {
    }

    @XmlAttribute(name = "metadatatype")
    public MetaDataType getType() {
        return type;
    }

    public void setType(MetaDataType type) {
        this.type = type;
    }

    @XmlAttribute(name = "metadataname")
    public String getMetaDataName() {
        if(metaDataName == null) return "";
        return metaDataName;
    }

    public void setMetaDataName(String metaDataName) {
        this.metaDataName = metaDataName;
    }

    @XmlElement(name = "datapoint")
    public ArrayList<DataPoint> getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(ArrayList<DataPoint> datapoints) {
        this.datapoints = datapoints;
    }

    public void addDataPoint(DataPoint dataPoint) {
        if (datapoints == null) {
            datapoints = new ArrayList<>();
        }
        datapoints.add(dataPoint);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MetaData) {
            MetaData md = (MetaData) o;
            return (md.getDatapoints().equals(this.datapoints)
                    && md.getMetaDataName().equals(this.getMetaDataName())
                    && md.getType() == this.type);
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        String s = "Name: "+this.getMetaDataName()+"\nType: "+this.type+"\nDatapoints:\n";
        for (DataPoint datapoint : datapoints) {
            s+=datapoint.toString();
        }
        return s;
    }
}
