/*
 * Copyright (C) 2017 Irstea
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

import java.util.Arrays;
import java.io.*;
import java.util.Random;
/**
 *
 * @author
 */

public class AlternativePractice extends AbstractPractice{

    // FIXME pas classe
    private final double p_coef_update = 10.;

    private Sigmoid m_uncertainty;
    private double m_viability=0.;

    AlternativePractice( final int yield, final int env ){ 
        super(yield,env); 
        m_uncertainty = new Sigmoid( 
                0.99999, 
                -1.*Sigmoid.p_default_xForYEqualMax, 
                -1.*Sigmoid.p_default_xForYEqualMin, 
                Sigmoid.p_default_sigma, 
                Sigmoid.p_default_nbSteps
                );
    }

    AlternativePractice(){ this (0,0); }

    // FIXME a reref
    public void update(final RealPractice rp, final double viability){
        if ( viability < 0. ) { throw new RuntimeException("In AltPractice.update: y can't be inferior than 0"); };
        if ( viability > 1. ) { throw new RuntimeException("In AltPractice.update: y can't be superior than 1"); };
        if( viability > m_viability ){
            m_yield_lvl = (int) Math.ceil( (rp.m_yield_lvl+p_coef_update*m_yield_lvl)/(p_coef_update+1.) );
            m_env_lvl = (int) Math.ceil( (rp.m_env_lvl+p_coef_update*m_env_lvl)/(p_coef_update+1.) );
            m_viability = viability;
        }

    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("altPractice_yield_"+id+"_"+",");
            fw.append("altPractice_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};



