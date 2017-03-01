/*
 * Copyright (C) 2016 Irstea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package model.basic_conv;

import java.io.*;

/**
 *
 * @author 
 */
public final class Sigmoid {

    static final double p_tinyStep = 0.0001;
    static final double p_min = 0.00001;
    static final double p_max = 0.99999;

    private double m_x;
    private double m_coef_steep;
    private double m_coef_offset;

    public Sigmoid (){
        m_x=0.5;
        m_coef_steep=1.;
        m_coef_offset=0.;
    } 

    public Sigmoid (final double xStart){

        errorRangeX(xStart, "sigmoid");

        m_x=xStart;
        m_coef_steep=1.;
        m_coef_offset=0.;
    } 

    public Sigmoid (final double xStart, final double xForYMax, final double xForYMin, final double precision){

        errorRangeX(xStart, "sigmoid");

        m_x=xStart;
        setCoef(xForYMax,xForYMin,precision);
    }

    public void stepFromSigmoid( final double step ){

        m_x = m_x + (1./(1.+Math.exp(-(logit(m_x)+step))))-(1./(1.+Math.exp(-logit(m_x))));

        if (m_x<p_min){ m_x=p_min; }
        if (m_x>p_max){ m_x=p_max; }
    };

    public double logit(double x){

        errorRangeX(x, "logit");

        return Math.log(x/(1-x));
    }

    // Les coefficients de 1/( 1 + exp(steep*x+offset))
    private void setCoef( final double xForYMax, final double xForYMin, final double precision){

        if ( precision >= 1. || precision <= 0. ) { throw new RuntimeException("pb in Sigmoid.setCoef() on precision"); };

        m_coef_steep = (1./(xForYMax-xForYMin))*Math.log((1-precision)*(1-precision)/(precision*precision));
        m_coef_offset = -m_coef_steep*xForYMin - Math.log( (1./precision)-1);
    }

    public void print(final FileWriter fw){
        try{
            fw.append(m_x+ ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void setValue(final double x){ 
        errorRangeX(x,"sigmoid");
        m_x = x; 
    }

    public double getValue(){ return m_x; }

    public double getValue(final double x){ 
        return (1. / (1. + Math.exp(-( m_coef_steep*x + m_coef_offset )))); 
    }

    private void errorRangeX( final double x, final String s ){
        if ( x <= 0. ) { throw new RuntimeException("In "+s+": x can't be inferior or equal to 0"); };
        if ( x >= 1. ) { throw new RuntimeException("In "+s+": x can't be superior or equal to 1"); };
    }

};

