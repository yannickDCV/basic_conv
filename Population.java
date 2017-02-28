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

import java.util.*;
import java.io.*;

/**
 *
 * @author
 */

public class Population {

    private int m_popSize;
    private final Individual[] m_individuals;
    private Influences m_influences;

    // TODO faire classe parameter pour initialisation
    public Population(final int popSize) {

        m_popSize = popSize;
        m_individuals = new Individual[popSize];
        m_influences = new Influences(popSize, 0.1);

        Individual.Practice practice = new Individual.Practice(0, 0);
        Individual.Accounts accounts = new Individual.Accounts(100., 100.);

        for(int i=0; i<m_individuals.length; i++){
            double yieldIdStart = Math.random()*0.1 + 0.45;
            double envIdStart = Math.random()*0.1 + 0.45;
            Individual.Identity identity = new Individual.Identity(yieldIdStart, envIdStart);
            Individual indToAdd = new Individual(i, identity, practice, accounts);

            m_individuals[i] = new Individual(indToAdd);
        }

    }

    public void printHeaders(final FileWriter fw){
        try{
            for(int i=0; i<m_individuals.length; i++){
                fw.append("yieldId_"+i+"_"+",");
                fw.append("envId_"+i+"_"+",");
                fw.append("yieldPractice_"+i+"_"+",");
                fw.append("envPractice_"+i+"_"+",");
            }
            m_influences.printHeaders(fw);
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        for(int i=0; i<m_individuals.length; i++){
             m_individuals[i].print(fw);
        }
        m_influences.print(fw);
    }

    public void iter(final double price) {
        updatePractices();
        updateIdentities();
        updateInfluences();
    }

    private void updatePractices(){
        for(int i=0; i<m_individuals.length; i++){
            m_individuals[i].updatePractice();
        }
    }

/*/*
    // l'identité s'actualise avec la proportion env/yield chez les autres
    // Somme pondérée par l'influence
    private void updateIdentities(){
        for(int i=0; i<m_individuals.length; i++){
            double sumWeights = 0.;
            double envIdRef = 0.;
            double yieldIdRef = 0.;
            // TODO bien caler coef sigmoide car idRef < 1
            for(int j=0; j<m_individuals.length; j++){
                double envOther = m_individuals[j].getPractice().getEnv();
                double yieldOther = m_individuals[j].getPractice().getYield();

                if( envOther+yieldOther == 0. ){ break; }

                double propEnv = envOther/(envOther+yieldOther);
                double propYield = envOther/(envOther+yieldOther);
                double influence = m_influences.getInf(i,j);

                envIdRef += influence*propEnv;
                yieldIdRef += influence*propYield;
                sumWeights += influence;
            }
        envIdRef = envIdRef/sumWeights;
        yieldIdRef = yieldIdRef/sumWeights;
        m_individuals[i].updateIdentity(yieldIdRef, envIdRef);
        }
    }

    // Plus un agent est fort par rapport à moi sur les choses qui me sont importantes, plus je suis influencé par lui
    private void updateInfluences(){
        Sigmoid sig = new Sigmoid();
        for(int i=0; i<m_individuals.length; i++){
            for(int j=0; j<m_individuals.length; j++){

                double yieldId = m_individuals[i].getIdentity().getYield();
                double envId = m_individuals[i].getIdentity().getEnv();
                double yieldPr = m_individuals[i].getPractice().getYield();
                double envPr = m_individuals[i].getPractice().getEnv();
                double yieldPrOt = m_individuals[j].getPractice().getYield();
                double envPrOt = m_individuals[j].getPractice().getEnv();
                double sigDiffYield = sig.getValue(yieldPrOt-yieldPr);
                double sigDiffEnv = sig.getValue(envPrOt-envPr);

                double inf = Math.max(yieldId*sigDiffYield, envId*sigDiffEnv);

                m_influences.setInf(i,j,inf);
            }
        }
    }
    // */

/**/
    private void updateIdentities(){
        // l'identité s'actualise avec le positionnement par rapport aux autres sur le résultat obtenu.
        // Somme pondérée par l'influence
        for(int i=0; i<m_individuals.length; i++){
            double sumWeights = 0.;
            double envRef = 0.;
            double yieldRef = 0.;
            for(int j=0; j<m_individuals.length; j++){
                if (i != j){ 
                    envRef += m_influences.getInf(i,j) * m_individuals[j].getPractice().getEnv();
                    yieldRef += m_influences.getInf(i,j) * m_individuals[j].getPractice().getYield();
                    sumWeights += m_influences.getInf(i,j);
                }
            }
            envRef = envRef/sumWeights;
            yieldRef = yieldRef/sumWeights;
            m_individuals[i].updateIdentity(yieldRef, envRef);
        }
    }

    private void updateAlternative(){
        // l'alternative est obtenue en faisant la moyenne des proportions de pr.env et pr.yield des individus viables
        for(int i=0; i<m_individuals.length; i++){
            double sumWeights = 0.;
            double sumRef = 0.;
            double envRef = 0.;
            double yieldRef = 0.;
            for(int j=0; j<m_individuals.length; j++){
                if ( m_individuals[j].isViable() && i != j){ 
                    envRef += m_individuals[j].getPractice().getEnv();
                    yieldRef += m_individuals[j].getPractice().getYield();
                    sumWeights++;
                }
            }
            if ( sumWeights != 0. ){
                envRef = envRef/sumWeights;
                yieldRef = yieldRef/sumWeights;
            }

            // Proportion
            sumRef = yieldRef + envRef;
            if ( sumRef != 0. ){
                yieldRef = yieldRef/sumRef;
                envRef = envRef/sumRef;
                m_individuals[i].updateAlternative(yieldRef, envRef);
            }

        }
    }

    private void updateInfluences(){
        // Plus un agent met de l'importance sur les mêmes choses que moi, plus je suis influencé par lui (?!?)
        for(int i=0; i<m_individuals.length; i++){
            for(int j=0; j<m_individuals.length; j++){
                if (i != j){ 
                    double distId = m_individuals[i].distId(m_individuals[j]);
                    m_influences.setInf(i,j,distId);
                }
            }
        }
    }
    // */

};

