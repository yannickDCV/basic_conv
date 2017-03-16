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
        m_influences.setOpening(m_needForChange.getValue());
        return m_influences.getSomePeople(nbPeople);
    }

    public void discussWith( final Individual ind ){
        m_identity.setMaxYieldIfBigger( ind.m_practice.getYield() );
        m_identity.setMaxEnvIfBigger( ind.m_practice.getEnv() );

        // FIXME a verif
        m_influences.set(ind.m_id,m_identity.getDistFrom(ind.m_identity));
        m_norm.update(ind.m_practice);
        if( m_alternative.isInquiring()) { m_alternative.update(ind.m_practice, ind.m_viability.getValue()); }
    }

    public void iter( final double price ){

        m_practice.update(m_identity);
        // TODO m_accounts.update pour viability pour prendre en compte seuil de viabilité
        updateViability(price,m_practice);

        double satisfaction = getSatisfaction( m_practice, m_identity, m_norm, m_viability );

        m_identity.update(m_practice, satisfaction-m_lastSatisfaction);
        updateNeedForChange(satisfaction);

        if( Math.random() <= m_needForChange.getValue() ) {
            m_alternative.setInquiringMode(true);
            if( m_alternative.isMajorChangeTriggered( m_needForChange.getValue(), m_identity, m_norm ) ){ 
                m_practice.copy(m_alternative); 
                m_alternative.reset();
                m_needForChange.setValueEqualMin();
            }
        }
        m_lastSatisfaction = satisfaction;
        m_influences.stepDownForAll();

    }

    private double getSatisfaction(final RealPractice pr, final Identity id, final NormPractice norm, final Sigmoid m_viability){

        // TODO verif comportement attitude
        double attitude = 1.-id.getDistFrom(pr);
        double subjectiveNorm = 1.-norm.getDistFrom(pr,id.getRefMaxYield(),id.getRefMaxEnv());
        double pcb = m_viability.getValue();

        System.out.println("--------------------------------------------"); 
        System.out.println( "m_id = " + m_id ); 
        System.out.println( "attitude = " + attitude ); 
        System.out.println( "subjectiveNorm = " + subjectiveNorm ); 
        System.out.println( "pcb = " + pcb ); 

        // TODO dans longtemps : voir dynamique a,b,c
        // FIXME pour test 2*pcb
        double satisfaction = (1./4.) * ( attitude + subjectiveNorm + 2*pcb ) ;
        System.out.println( "satisfaction = " + satisfaction ); 

        return satisfaction;
    }

    private void updateViability( final double price, final RealPractice pr){
        // TODO price = correctPriceFromEnv(price, m_practice);
        // FIXME pb si prix en marche d'escalier
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


