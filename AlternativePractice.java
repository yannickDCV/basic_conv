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
    // TODO mettre role uncertainty
    private final double p_uncertainty_start = 0.99;

    private Sigmoid m_uncertainty;
    private double m_viability=0.;
    private boolean m_isInquiring=false;

    AlternativePractice(){ this (0,0); }
    AlternativePractice( final int yield, final int env ){ 
        super(yield,env); 
        m_uncertainty = new Sigmoid( 
                p_uncertainty_start, 
                -1.*Sigmoid.p_default_xForYEqualMax, 
                -1.*Sigmoid.p_default_xForYEqualMin, 
                Sigmoid.p_default_sigma, 
                Sigmoid.p_default_nbSteps
                );
    }

    // FIXME a reref
    public void update(final RealPractice rp, final double viability){
        if ( viability < 0. ) { throw new RuntimeException("In AltPractice.update: y can't be inferior than 0"); };
        if ( viability > 1. ) { throw new RuntimeException("In AltPractice.update: y can't be superior than 1"); };
        if ( m_viability+viability == 0. ) { throw new RuntimeException("In AltPractice.update: division by 0"); };

        m_yield_lvl = (int) ((viability*rp.m_yield_lvl+m_viability*m_yield_lvl)/(viability+m_viability));
        m_env_lvl = (int) ((viability*rp.m_env_lvl+m_viability*m_env_lvl)/(viability+m_viability));
        m_viability = (m_viability*m_viability+viability*viability)/(viability+m_viability);
    }

    public void reset(){
        m_yield_lvl=0;
        m_env_lvl=0;
        m_uncertainty.setValue(p_uncertainty_start);
        m_viability=0.;
        m_isInquiring=false;
    }

    public void setInquiringMode( final boolean isInquiring ){ m_isInquiring = isInquiring; }
    public boolean isInquiring(){ return m_isInquiring; }

    public boolean isMajorChangeTriggered( final double needForChange, final Identity id, final NormPractice norm , final References ref){

        double attitude = 1.-id.getDistFrom(this,ref);
        double subjectiveNorm = 1.-norm.getDistFrom(this,ref);
        double pcb = m_viability;

        double intention = (1./3.) * ( attitude + subjectiveNorm + pcb ) ;
        double resistance = (1. - intention);

        return (needForChange > resistance);
    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("altPractice_yield_"+id+"_"+",");
            fw.append("altPractice_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};



