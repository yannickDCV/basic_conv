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

public class Individual {

    private int m_id;

    private RealPractice m_practice;
    private AlternativePractice m_alternative;
    private NormPractice m_norm;

    private Identity m_identity;
    private References m_references;
    private Influences m_influences;
    private Evaluation m_evaluation;

    // private Accounts m_accounts;
    private Sigmoid m_viability;
    private double m_lastGain=0;

    Individual( 
            final int id,
            final RealPractice practice,
            final Identity identity,
            final Influences influences
            ){
        m_id = id;
        m_practice = new RealPractice(practice);
        m_identity = new Identity(identity);
        m_norm = new NormPractice(practice);
        m_alternative = new AlternativePractice();
        m_references = new References(practice);
        m_influences = new Influences(influences);
        m_evaluation = new Evaluation();
        m_viability = new Sigmoid();
            }

    Individual( final Individual toClone ){
        this(
                toClone.m_id, 
                toClone.m_practice, 
                toClone.m_identity, 
                toClone.m_influences
                );
    }

    public int[] getPeopleToDiscussWith(final int popSize){
        final int nbPeople = (int) Math.ceil(m_evaluation.getNeedForChange()*(popSize-1));
        // TODO a revoir
        // m_influences.setOpening(m_needForChange.getValue());
        return m_influences.getSomePeople(nbPeople);
    }

    public void discussWith( final Individual ind ){
        m_references.update( ind.m_practice );

        // FIXME a verif
        m_influences.set(ind.m_id,m_identity.getDistFrom(ind.m_identity, m_references));
        // TODO à ref: rajouter norme dans calcul
        m_norm.update(ind.m_practice);
        if( m_alternative.isInquiring()) { 
            m_alternative.update(ind.m_practice, ind.m_viability.getValue()); 
            m_alternative.setInquiringMode(false);
        }
    }

    public void iter( final double price ){

        m_practice.update(m_identity, m_evaluation);
        // m_accounts.update()
        updateViability(price,m_practice);
        m_evaluation.update( m_practice, m_identity, m_norm, m_viability, m_references);
        m_identity.update(m_practice, m_evaluation, m_references);
        m_references.update(m_practice);

        if( Math.random() <= m_evaluation.getNeedForChange() ) {
            m_alternative.setInquiringMode(true);
            if( m_evaluation.isMajorChangeTriggered( m_alternative, m_identity, m_norm, m_viability, m_references ) ){ 
                m_practice.copy(m_alternative); 
                m_alternative.reset();
                // TODO m_needForChange.setValueEqualMin();
                m_alternative.setInquiringMode(false);
            }
        }
        m_influences.stepDownForAll();


    }

    private void updateViability( final double price, final RealPractice pr){
        // TODO price = correctPriceFromEnv(price, m_practice);
        // FIXME pb si prix en marche d'escalier
        double gain = price*pr.getYield();
        m_viability.stepFromSigmoid(Math.signum(gain-m_lastGain));
        m_lastGain = gain;
    }

    public void printHeaders(final FileWriter fw){
        m_practice.printHeaders(fw,m_id); 
        m_identity.printHeaders(fw,m_id); 
        m_influences.printHeaders(fw,m_id); 
        m_alternative.printHeaders(fw,m_id); 
        m_norm.printHeaders(fw,m_id); 
        m_evaluation.printHeaders(fw,m_id); 
        // m_accounts.printHeaders(fw,m_id); 
        try{
            fw.append("viability_"+m_id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        m_practice.print(fw); 
        m_identity.print(fw); 
        m_influences.print(fw); 
        m_alternative.print(fw); 
        m_norm.print(fw); 
        m_evaluation.print(fw); 
        // m_accounts.print(fw); 
        try{
            fw.append(m_viability.getValue() + ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};


