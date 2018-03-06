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
package com.ksatstuttgart.usoc.controller;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

/**
 * This class uses the GNSS data received to calculate the potential impact
 * location
 *
 * @author valentinstarlinger
 */
public class GNSSController {

    /**
     *
     * @param positions
     * @param g
     * @return
     */
    public static Position getEstimatedImpact(ArrayList<Position> positions, Globe g) {
        if (positions.size() < 3) {
            //TODO: throw exception
            return null;
        }

        int size = positions.size();
        //get latitude of the last three positions
        double gnss_lat1 = positions.get(size - 3).latitude.radians;
        double gnss_lat2 = positions.get(size - 2).latitude.radians;
        double gnss_lat3 = positions.get(size - 1).latitude.radians;
        //get longitude of the last three positions
        double gnss_lon1 = positions.get(size - 3).longitude.radians;
        double gnss_lon2 = positions.get(size - 2).longitude.radians;
        double gnss_lon3 = positions.get(size - 1).longitude.radians;
        //get altitude of the last three positions
        double gnss_alt1 = positions.get(size - 3).getAltitude();
        double gnss_alt2 = positions.get(size - 2).getAltitude();
        double gnss_alt3 = positions.get(size - 1).getAltitude();

        //create matrices of the last three position entries. 
        double[][] temp = new double[][]{
            {Math.pow(gnss_lat1, 2), gnss_lat1, 1},
            {Math.pow(gnss_lat2, 2), gnss_lat2, 1},
            {Math.pow(gnss_lat3, 2), gnss_lat3, 1}
        };
        BlockRealMatrix a_lat = new BlockRealMatrix(temp);

        temp = new double[][]{
            {Math.pow(gnss_lon1, 2), gnss_lon1, 1},
            {Math.pow(gnss_lon2, 2), gnss_lon2, 1},
            {Math.pow(gnss_lon3, 2), gnss_lon3, 1}
        };
        BlockRealMatrix a_lon = new BlockRealMatrix(temp);

        double[] y_temp = new double[]{gnss_alt1, gnss_alt2, gnss_alt3};
        ArrayRealVector y = new ArrayRealVector(y_temp);
        
        RealVector coef_lat = (MatrixUtils.inverse(a_lat)).preMultiply(y);
        RealVector coef_lon = (MatrixUtils.inverse(a_lon)).preMultiply(y);
        
        double impact_lat = getOptimum(coef_lat, g, positions.get(size - 1), gnss_lat3);
        double impact_lon = getOptimum(coef_lon, g, positions.get(size - 1), gnss_lon3);
        
        return Position.fromRadians(impact_lon, impact_lat, 0d);
    }
    
    /**
     * 
     * @param coef
     * @param g
     * @param lastPos
     * @param closest
     * @return 
     */
    private static double getOptimum(RealVector coef, Globe g, Position lastPos, double closest){
        double a = coef.getEntry(0);
        double b = coef.getEntry(1);
        double c = coef.getEntry(2)-
                g.getElevation(lastPos.longitude, lastPos.latitude);
        
        double opt1 = (-b+Math.sqrt(Math.pow(b,2)-4*a*c))/(2*a);
        double opt2 = (-b-Math.sqrt(Math.pow(b,2)-4*a*c))/(2*a);
        
        return Math.abs(Math.abs(closest) - Math.abs(opt1))  
                < Math.abs(Math.abs(closest) - Math.abs(opt2)) ? opt1 : opt2;
    }
}
