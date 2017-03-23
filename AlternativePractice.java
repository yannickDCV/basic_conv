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

    // TODO mettre role uncertainty
    // private final double p_uncertainty_start = 0.99;

    // private Sigmoid m_uncertainty;
    private Percentage m_viability;
    private boolean m_isInquiring=false;

    AlternativePractice(){ this (0,0); }
    AlternativePractice( final int yield, final int env ){ 
        super(yield,env); 
        m_viability = new Percentage(0.);
        // m_uncertainty = new Sigmoid( 
        //         p_uncertainty_start, 
        //         -1.*Sigmoid.p_default_xForYEqualMax, 
        //         -1.*Sigmoid.p_default_xForYEqualMin, 
        //         Sigmoid.p_default_sigma, 
        //         Sigmoid.p_default_nbSteps
        //         );
    }

    // FIXME a reref
    public void update(final RealPractice rp, final Accounts acc){
        double viability = acc.getViability();
        double viability_ = m_viability.get();

        if ( viability_+viability == 0. ) { throw new RuntimeException("In AltPractice.update: division by 0"); };

        m_yield_lvl = (int) ((viability*rp.m_yield_lvl+viability_*m_yield_lvl)/(viability+viability_));
        m_env_lvl = (int) ((viability*rp.m_env_lvl+viability_*m_env_lvl)/(viability+viability_));

        m_viability.set((viability_*viability_+viability*viability)/(viability+viability_));
    }

    public void reset(){
        m_yield_lvl=0;
        m_env_lvl=0;
        // m_uncertainty.setValue(p_uncertainty_start);
        m_viability.set(0.);
        m_isInquiring=false;
    }

    public double getViability(){ return m_viability.get(); }

    public void setInquiringMode( final boolean isInquiring ){ m_isInquiring = isInquiring; }
    public boolean isInquiring(){ return m_isInquiring; }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("altPractice_yield_"+id+"_"+",");
            fw.append("altPractice_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};



