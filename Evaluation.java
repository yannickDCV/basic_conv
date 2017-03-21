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

public class Evaluation{

    Percentage m_satisfaction;
    Percentage m_lastSatisfaction;
    Sigmoid m_needForChange;

    Evaluation(){
        m_satisfaction = new Percentage(0.5);
        m_lastSatisfaction = new Percentage(m_satisfaction);
        // FIXME calibration
        m_needForChange = new Sigmoid( 
                0.5, 
                -1.*Sigmoid.p_default_xForYEqualMax, 
                -1.*Sigmoid.p_default_xForYEqualMin, 
                0.001,//Sigmoid.p_default_sigma, 
                2//Sigmoid.p_default_nbSteps
                );

    }

    public void update( final RealPractice pr, final Identity id, final NormPractice norm , final Accounts acc, final References ref){
        m_lastSatisfaction.set(m_satisfaction);
        final double intention = getIntention(pr,id,norm,acc,ref);
        m_satisfaction.set(intention);
        m_needForChange.stepFromSigmoid(getDiffSatisfaction());
    }

    private double getIntention(final AbstractPractice ap, final Identity id, final NormPractice norm, final Accounts acc, final References ref){

        final double attitude = 1.-id.getDistFrom(ap,ref);
        final double subjectiveNorm = 1.-norm.getDistFrom(ap,ref);
        final double pbc = acc.getViability();

        // TODO dans longtemps : voir dynamique a,b,c
         final double intention = (1./3.) * ( attitude + subjectiveNorm + pbc ) ;
          // final double intention = attitude;
          // final double intention = subjectiveNorm;
          // final double intention = pbc;

        return intention;
    }

    public double getProbaMinorChange(){ return 1.-m_satisfaction.getValue(); }
    public double getNeedForChange(){ return m_needForChange.getValue(); }
    public double getDiffSatisfaction(){ return m_satisfaction.getValue()-m_lastSatisfaction.getValue(); }

    public boolean isMajorChangeTriggered( final AlternativePractice alt, final Identity id, final NormPractice norm , final Accounts acc, final References ref){
        final double intention = getIntention(alt,id,norm,acc,ref);
        final double resistance = (1. - intention);

        return (m_needForChange.getValue() > resistance);
    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("satisfaction_"+id+"_"+",");
            fw.append("needForChange_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        m_satisfaction.print(fw); 
        m_needForChange.print(fw); 
    }

};

