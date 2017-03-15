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
    private Influences m_influences;
    // private Accounts m_accounts;

    private Sigmoid m_needForChange;
    private Sigmoid m_viability;

    private double m_lastGain=0;
    private double m_lastSatisfaction=0;

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
        m_influences = new Influences(influences);
        // TODO voir comment inverser sur constructeur
        m_needForChange = new Sigmoid( 
                Sigmoid.p_default_yStart, 
                -1.*Sigmoid.p_default_xForYEqualMax, 
                -1.*Sigmoid.p_default_xForYEqualMin, 
                0.001,//Sigmoid.p_default_sigma, 
                Sigmoid.p_default_nbSteps
                );

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
        // FIXME a verif
        final int nbPeople = (int) Math.ceil(m_needForChange.getValue()*(popSize-1));
        return m_influences.getSomePeople(nbPeople);
    }

    public void discussWith( final Individual ind ){
        m_identity.setMaxYieldIfBigger( ind.m_practice.getYield() );
        m_identity.setMaxEnvIfBigger( ind.m_practice.getEnv() );

        // FIXME a verif
        m_influences.set(ind.m_id,m_identity.getDistFrom(ind.m_identity));
        m_norm.update(ind.m_practice);
    }

    public void iter( final double price ){

        double satisfaction = getSatisfaction( m_practice, m_identity, m_norm, m_viability );

        m_practice.update(m_identity,satisfaction-m_lastSatisfaction);
        // TODO m_accounts.update pour viability
        updateViability(price,m_practice);

        updateNeedForChange(satisfaction);

        //if( Math.random() <= m_needForChange.getValue() ) {
         ////m_alternative.setSearchMode(true);
         //if( m_alternative.isMajorChangeTriggered( m_identity, m_norm ) ){ m_practice.set(m_alternative); }
        //}
        m_lastSatisfaction = satisfaction;
        m_influences.stepDownForAll();

    }

    private double getSatisfaction(final RealPractice pr, final Identity id, final NormPractice norm, final Sigmoid m_viability){

        // TODO verif comportement attitude
        double attitude = 1.-id.getDistFrom(pr);
        System.err.println( "attitude = " + attitude ); 
        double subjectiveNorm = 1.-norm.getDistFrom(pr,id.getRefMaxYield(),id.getRefMaxEnv());
        System.err.println( "subjectiveNorm = " + subjectiveNorm ); 
        double pcb = m_viability.getValue();
        System.err.println( "pcb = " + pcb ); 

        // TODO dans longtemps : voir dynamique a,b,c
        double satisfaction = (1./3.) * ( attitude + subjectiveNorm + pcb ) ;

        return satisfaction;
    }

    private void updateViability( final double price, final RealPractice pr){
        // TODO
        // price = correctPriceFromEnv(price, m_practice);
        double gain = price*pr.getYield();
        m_viability.stepFromSigmoid(Math.signum(gain-m_lastGain));
        m_lastGain = gain;
    }

    private void updateNeedForChange( final double satisfaction ){
        m_needForChange.stepFromSigmoid(Math.signum(satisfaction-0.5));
    }

    public void printHeaders(final FileWriter fw){
        m_practice.printHeaders(fw,m_id); 
        m_identity.printHeaders(fw,m_id); 
        m_influences.printHeaders(fw,m_id); 
        m_alternative.printHeaders(fw,m_id); 
        m_norm.printHeaders(fw,m_id); 
        try{
            fw.append("needForChange_"+m_id+"_"+",");
            fw.append("viability_"+m_id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        m_practice.print(fw); 
        m_identity.print(fw); 
        m_influences.print(fw); 
        m_alternative.print(fw); 
        m_norm.print(fw); 
        try{
            fw.append(m_needForChange.getValue() + ",");
            fw.append(m_viability.getValue() + ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};


