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

    Percentage m_satisfaction, m_lastSatisfaction;
    Sigmoid m_needForChange;
    Intention m_intention;

    Evaluation(){
        m_satisfaction = new Percentage(0.5);
        m_lastSatisfaction = new Percentage(m_satisfaction);
        m_intention = new Intention();
        // FIXME calibration
        m_needForChange = new Sigmoid( 
                Sigmoid.p_limit_min, 
                -1.*Sigmoid.p_default_xForYEqualMax, 
                -1.*Sigmoid.p_default_xForYEqualMin, 
                0.001,//Sigmoid.p_default_sigma, 
                3//Sigmoid.p_default_nbSteps
                );

    }

    public void update( final RealPractice pr, final Identity id, final NormPractice norm , final Accounts acc, final References ref){
        m_lastSatisfaction.set(m_satisfaction);
        m_intention.update(pr,id,norm,acc.getViability(),ref);
        m_satisfaction.set(m_intention.get());
        m_needForChange.stepFromSigmoid(getDiffSatisfaction());
    }

    public double getProbaMinorChange(){ return 1.-m_satisfaction.get(); }
    public double getNeedForChange(){ return m_needForChange.get(); }
    public double getDiffSatisfaction(){ return m_satisfaction.get()-m_lastSatisfaction.get(); }

    public boolean isMajorChangeTriggered( final AlternativePractice alt, final Identity id, final NormPractice norm, final References ref){
        Intention intentionToAdoptAlternative = new Intention(alt,id,norm,alt.getViability(),ref);
        final double resistance = (1. - intentionToAdoptAlternative.get());

        return (m_needForChange.get() > resistance);
    }

    private class Intention{
        Percentage m_attitude, m_subjectiveNorm, m_pbc;
        Percentage m_intention;

        Intention(){
            m_attitude = new Percentage();
            m_subjectiveNorm = new Percentage();
            m_pbc = new Percentage();
            m_intention = new Percentage();
        }

        Intention(final AbstractPractice ap, final Identity id, final NormPractice norm, final double via, final References ref){
            this();
            update(ap,id,norm,via,ref);
        }

        // FIXME via pas classe... Accounts?
        public void update(final AbstractPractice ap, final Identity id, final NormPractice norm, final double via, final References ref){
            m_attitude.set(1.-id.getPerceivedDistFrom(ap,ref));
            m_subjectiveNorm.set(1.-norm.getPerceivedDistFrom(ap,ref));
            m_pbc.set(via);

            // TODO dans longtemps : voir dynamique a,b,c
            m_intention.set((1./3.) * ( m_attitude.get() + m_subjectiveNorm.get() + m_pbc.get() ) );
            // m_intention.set(m_attitude.get());
            // m_intention.set(m_subjectiveNorm.get());
            // m_intention.set(0.5*(m_subjectiveNorm.get()+m_attitude.get()));
            // m_intention.set(m_pbc.get());
        }

        public double get(){ return m_intention.get(); }

        public void printHeaders(final FileWriter fw, final int id){
            try{
                fw.append("intention_"+id+"_"+",");
                fw.append("attitude_"+id+"_"+",");
                fw.append("subjectiveNorm_"+id+"_"+",");
                fw.append("pbc_"+id+"_"+",");
            }catch(IOException e){ e.printStackTrace(); }
        }

        public void print(final FileWriter fw){
            m_intention.print(fw);
            m_attitude.print(fw);
            m_subjectiveNorm.print(fw);
            m_pbc.print(fw);
        }

    };

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("satisfaction_"+id+"_"+",");
            fw.append("needForChange_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
        m_intention.printHeaders(fw,id);
    }

    public void print(final FileWriter fw){
        m_satisfaction.print(fw); 
        m_needForChange.print(fw); 
        m_intention.print(fw);
    }

};

