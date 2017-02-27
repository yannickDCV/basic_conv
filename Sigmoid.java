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
    static final double p_min = p_tinyStep;
    static final double p_max = 1.-p_tinyStep;

    private double m_x;
    private double m_coef_steep;
    private double m_coef_offset;

    public Sigmoid (final double xStart){

        if ( xStart < 0. ) { throw new RuntimeException("x in sigmoid can't be inferior or equal to 0"); };
        if ( xStart > 1. ) { throw new RuntimeException("x in sigmoid can't be superior or equal to 1"); };

        m_x=xStart;
        m_coef_steep=1.;
        m_coef_offset=0.;
    } 

    public Sigmoid (final double xStart, final double yMax, final double yMin, final double precision){

        if ( xStart < 0. ) { throw new RuntimeException("x in sigmoid can't be inferior or equal to 0"); };
        if ( xStart > 1. ) { throw new RuntimeException("x in sigmoid can't be superior or equal to 1"); };

        m_x=xStart;
        setCoef(yMax,yMin,precision);
    }

    public void stepFromSigmoid( final double step ){

        m_x = m_x + (1./(1.+Math.exp(-(logit(m_x)+step))))-(1./(1.+Math.exp(-logit(m_x))));

        if (m_x<p_min){ m_x=p_min; }
        if (m_x>p_max){ m_x=p_max; }
    };

    public double logit(double x){

        if ( x < 0. ) { throw new RuntimeException("x in sigmoid can't be inferior or equal to 0"); };
        if ( x > 1. ) { throw new RuntimeException("x in sigmoid can't be superior or equal to 1"); };

        return Math.log(x/(1-x));
    }

    // Les coefficients de 1/( 1 + exp(steep*x+offset))
    private void setCoef( final double yMax, final double yMin, final double precision){

        if ( precision >= 1. || precision <= 0. ) { throw new RuntimeException("pb in Sigmoid.setCoef() on precision"); };

        m_coef_steep = (1./(yMax-yMin))*Math.log((1-precision)*(1-precision)/(precision*precision));
        m_coef_offset = -m_coef_steep*yMin - Math.log( (1./precision)-1);
    }

    private void setOffset(final double newOffset){
        m_coef_offset = newOffset;
    }

    public void print(final FileWriter fw){
        try{
            fw.append(m_x+ ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public double getValue(){ return m_x; }

    public double getValue(final double x){ 
        return (1. / (1. + Math.exp(-( m_coef_steep*x + m_coef_offset )))); 
    }

};

