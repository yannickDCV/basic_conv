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

    final static public double p_default_yStart = 0.5;
    final static public int p_default_xForYEqualMax = 8000;
    final static public int p_default_xForYEqualMin = -8000;
    final static public double p_default_sigma = 0.005;
    final static public int p_default_nbSteps = 1000;

    final private double p_limit_min = 0.000001;
    final private double p_limit_max = 0.999999;

    private double m_y;
    private double m_coef_steep;
    private double m_coef_offset;

    private double m_step;
    private double m_maxY, m_minY;

    public Sigmoid (){ 
        this( 
                p_default_yStart, 
                p_default_xForYEqualMax, 
                p_default_xForYEqualMin, 
                p_default_sigma, 
                p_default_nbSteps
            ); 
    } 

    public Sigmoid (final double yStart){ 
        this( 
                yStart, 
                p_default_xForYEqualMax, 
                p_default_xForYEqualMin, 
                p_default_sigma, 
                p_default_nbSteps
            ); 
    } 

    public Sigmoid (final Sigmoid toClone){
        m_y = toClone.m_y;
        m_coef_steep = toClone.m_coef_steep;
        m_coef_offset = toClone.m_coef_offset;
        m_step = toClone.m_step;
        m_minY = toClone.m_minY;
        m_maxY = toClone.m_maxY;
    } 

    public Sigmoid (final double yStart, final double xForYEqualMax, final double xForYEqualMin, final double sigma, final int nbStep){

        errorRangeY(yStart, "sigmoid");
        if ( nbStep <= 0 ) { throw new RuntimeException("pb in sigmoid with nbStep"); };

        m_y = yStart;
        setCoef(xForYEqualMax,xForYEqualMin,sigma);
        setNumericalParameters(xForYEqualMax,xForYEqualMin,nbStep);
    }

    // Les coefficients de 1/( 1 + exp(steep*x+offset))
    public void setCoef( final double xForYEqualMax, final double xForYEqualMin, final double sigma){

        if ( sigma >= 1. || sigma <= 0. ) { throw new RuntimeException("pb in Sigmoid.setCoef() on sigma"); };

        m_coef_steep = (1./(xForYEqualMax-xForYEqualMin))*Math.log((1.-sigma)*(1.-sigma)/(sigma*sigma));
        m_coef_offset = -m_coef_steep*xForYEqualMin - Math.log( (1./sigma)-1.);
    }

    private void setNumericalParameters( final double xForYEqualMax, final double xForYEqualMin, final int nbStep){
        // FIXME si parametres pas initialisés
        m_step = Math.abs(xForYEqualMax-xForYEqualMin)/nbStep;
        m_maxY = getFromDirectSigmoid(xForYEqualMax);
        m_minY = getFromDirectSigmoid(xForYEqualMin);
    }

    public double getFromDirectSigmoid(final double x){ 
        double res =(1. / (1. + Math.exp(-( m_coef_steep*x + m_coef_offset )))); 
        if (res<p_limit_min){ res=p_limit_min; }
        if (res>p_limit_max){ res=p_limit_max; }
        return res;
    }

    public double getStepFromDirectSigmoid( final double y, final double coef ){
        if ( y == 0. || y == 1. ) { return 0.; };
        errorRangeY(y, "getStepFromDirectSigmoid");
        return getFromDirectSigmoid(logit(y)+coef*m_step)-y;
    }

    public double logit(double y){
        errorRangeY(y, "logit");
        if ( m_coef_steep == 0. ) { throw new RuntimeException("pb in logit with steep coef"); };
        return (1./m_coef_steep)*Math.log(y/(1.-y))-(m_coef_offset/m_coef_steep);
    }

    public void stepFromSigmoid( final double coef ){

        m_y = getFromDirectSigmoid(logit(m_y)+coef*m_step);

        if (m_y<m_minY){ m_y=m_minY; }
        if (m_y>m_maxY){ m_y=m_maxY; }
    }

    public void stepUp(){ stepFromSigmoid(1.); }
    public void stepDown(){ stepFromSigmoid(-1.); }
    public void setValueEqualMin(){ m_y=m_minY; }
    public void setValueEqualMax(){ m_y=m_maxY; }

    public void setValue(final double y){ 
        errorRangeY(y,"sigmoid");
        m_y = y; 
    }

    public double getValue(){ return m_y; }

    private void errorRangeY( final double y, final String s ){
        if ( y <= 0. ) { throw new RuntimeException("In "+s+": y can't be inferior or equal to 0"); };
        if ( y >= 1. ) { throw new RuntimeException("In "+s+": y can't be superior or equal to 1"); };
    }

    public void print(final FileWriter fw){
        try{
            fw.append(m_y+ ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};

