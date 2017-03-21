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

import java.util.*;
import java.io.*;
import java.util.Random;
/**
 *
 * @author
 */

// TODO Mettre une sigmoid pour chaque inf de m_influences?
// TODO Inner classe influence?
public class Influences {

    // FIXME pas classe
    private final double p_threshold = 0.5;
    private final double p_distIdForMaxInfluence = 0.;
    private final double p_distIdForMinInfluence = 1.;
    private final int p_nbSteps = 50;

    private final ArrayList<Double> m_influences;
    private Sigmoid m_influence;
    private int m_id;

    Influences( final int myId, final int nbInfluences, final double valueStart ){

        if ( valueStart < 0. ) { throw new RuntimeException("In Influence: value can't be inferior than 0"); }
        if ( valueStart > 1. ) { throw new RuntimeException("In Influence: value can't be superior than 1"); }
        if ( myId >= nbInfluences  ) { throw new RuntimeException("In Influence: myId can't be superior or equal than popSize"); }
        if ( myId < 0 ) { throw new RuntimeException("In Influence: myId can't be inferior than 0"); }

        m_influences = new ArrayList<Double>();
        for(int i=0; i<nbInfluences; i++){
            m_influences.add(valueStart);
        }
        m_influences.set(myId,0.);
        m_id = myId;
        m_influence = new Sigmoid(0.1, p_distIdForMaxInfluence, p_distIdForMinInfluence, 0.1, p_nbSteps);
    }

    Influences( final Influences toClone ){
        m_influences = new ArrayList<Double>(toClone.m_influences);
        m_influence = new Sigmoid(toClone.m_influence);
        m_id = toClone.m_id;
    }

    public void set( final int id, final double distId){

        if ( id >= m_influences.size() ) { throw new RuntimeException("In Influence: myId can't be superior or equal than popSize"); }
        if ( id < 0 ) { throw new RuntimeException("In Influence: myId can't be inferior than 0"); }
        if ( id == m_id ) { throw new RuntimeException("In Influence: attempt to set influence from myself"); }
        if ( distId < 0. ) { throw new RuntimeException("In Influence: distId can't be inferior than 0"); }
        if ( distId > 1. ) { throw new RuntimeException("In Influence: distId can't be superior than 1"); }

        m_influences.set(id,m_influence.getFromDirectSigmoid(distId));
    }

    public double get( final int id ){
        if ( id >= m_influences.size() ) { throw new RuntimeException("In Influence: myId can't be superior or equal than popSize"); }
        if ( id < 0 ) { throw new RuntimeException("In Influence: myId can't be inferior than 0"); }
        return m_influences.get(id); 
    }

    public int[] getSomePeople(final int nbPeople, final double needForChange){
        if ( needForChange < 0. ) { throw new RuntimeException("In Influence: needForChange can't be inferior than 0"); }
        if ( needForChange > 1. ) { throw new RuntimeException("In Influence: needForChange can't be superior than 1"); }
        if ( nbPeople >= m_influences.size() ) { throw new RuntimeException("In Influence: nbPeople superior or equal than popSize"); }
        int[] people = new int[nbPeople];

        ArrayList<Integer> peopleInf = new ArrayList<Integer>(getSomePeopleWithInf(nbPeople));
        ArrayList<Integer> peopleRandom = new ArrayList<Integer>(getSomePeopleRandomly(nbPeople));

        // FIXME peut parler 2 fois  la même personne
        for(int i=0; i<nbPeople; i++){
            if( Math.random() <= needForChange ){
                people[i] = peopleRandom.get(0);
            }
            else{
                people[i] = peopleInf.get(0);
            }
        }


        return people;
    }

    private ArrayList<Integer> getSomePeopleRandomly(final int nbPeople){
        if ( nbPeople >= m_influences.size() ) { throw new RuntimeException("In Influence: nbPeople superior or equal than popSize"); }
        ArrayList<Integer> people = new ArrayList<Integer>(nbPeople);
        for(int i=0; i<nbPeople; i++){
            int rand = new Random().nextInt(m_influences.size());
            if( rand == m_id ) { i--; continue; }
            people.add(i,rand);
        }
        return people;
    }

    private ArrayList<Integer> getSomePeopleWithInf(final int nbPeople){
        if ( nbPeople >= m_influences.size() ) { throw new RuntimeException("In Influence: nbPeople superior or equal than popSize"); }
        ArrayList<Integer> people = new ArrayList<Integer>(nbPeople);

        ArrayList<Double> probaArray = new ArrayList<Double>(m_influences);

        // System.out.println( "m_influences = " + m_influences ); 
        // System.out.println( "m_id = " + m_id ); 

        for(int i=0; i<nbPeople; i++){
            double sum = getSumInf(probaArray);
            double sumProba = 0.;
            double rand = Math.random();
            boolean isSet = false;
            for(int j=0; j<probaArray.size(); j++){
                // calcul proba selon influence
                probaArray.set(j,probaArray.get(j)/sum);
                sumProba += probaArray.get(j);
                if( !isSet && rand <= sumProba ){
                    people.add(i,j);
                    // System.out.println( "people["+i+"] = " + people.get(i) ); 
                    probaArray.set(j,0.);
                    isSet = true;
                }
                else{ probaArray.set(j,probaArray.get(j)*sum);}
            }
        }

        return people;
    }

    private int getNbInfUpThan( final double threshold ){
        if ( threshold < 0. ) { throw new RuntimeException("In Influence: threshold can't be inferior than 0"); }
        if ( threshold > 1. ) { throw new RuntimeException("In Influence: threshold can't be superior than 1"); }

        int sum=0;
        for(int i=0; i<m_influences.size(); i++){
            if ( m_influences.get(i) >= threshold ){ sum++; }
        }
        return sum;
    }

    private double getSumInf(final ArrayList<Double> infs){
        if ( infs.size() == 0) { throw new RuntimeException("In Influence: nb inf = 0... Impossible to compute mean"); }
        double sum=0.;
        for(int i=0; i<infs.size(); i++){
            sum += infs.get(i);
        }
        return sum;
    }

    private double getMeanInf(){
        return getSumInf(m_influences)/m_influences.size();
    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("nbInf_"+id+"_"+",");
            fw.append("moyInf_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        try{
            fw.append(getNbInfUpThan(p_threshold) + ",");
            fw.append(getMeanInf() + ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};

