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

public class Influences {

    // FIXME Moyen de faire mieux que ça
    private final double p_threshold=0.5;

    private int m_popSize;
    private ConnectivityMatrix m_influences;

    public Influences(final int popSize, final double infStart) {

        m_popSize = popSize;
        m_influences = new ConnectivityMatrix(popSize, infStart);
    }

    public void printHeaders(final FileWriter fw){
        try{
            for(int i=0; i<m_popSize; i++){
                fw.append("influence_"+i+"_"+",");
                fw.append("nbInfluence_"+i+"_"+",");
            }
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        try{
            for(int i=0; i<m_popSize; i++){
                fw.append(m_influences.getMeanWeight(i)+",");
                fw.append(m_influences.getNbWeightSupThan(i,p_threshold)+",");
            }
        }catch(IOException e){ e.printStackTrace(); }
    }

        public void setInf( final int i, final int j, final double inf ){
            m_influences.setWeight(i,j,inf);
        }

        public double getInf( final int i, final int j){
            return m_influences.getWeight(i,j);
        }


    private class ConnectivityMatrix{

        private double[][] m_weights;
        private int m_size;

        ConnectivityMatrix(final int size, final double weightStart){
            if ( weightStart < 0. ) { throw new RuntimeException("weight in connectivity matrix can't be inferior or equal to 0"); };
            if ( weightStart > 1. ) { throw new RuntimeException("weight in connectivity matrix can't be superior or equal to 1"); };
            m_size = size;
            m_weights = new double[size][size];
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    m_weights[i][j] = weightStart;
                }
            }
        }

        public void setWeight( final int i, final int j, final double weight ){
            m_weights[i][j] = weight;
        }

        public double getWeight( final int i, final int j){
            return m_weights[i][j];
        }

        public double getMeanWeight( final int i ){
            double sum=0;
            if(m_size == 1){return 0.;};
            for(int j=0; j<m_size; j++){
                if( i!=j ){ sum += m_weights[i][j]; }
            }
            return sum/(m_size-1.);
        }

        public int getNbWeightSupThan( final int i, final double threshold ){
            if ( threshold < 0. ) { throw new RuntimeException("weight in connectivity matrix can't be inferior or equal to 0"); };
            if ( threshold > 1. ) { throw new RuntimeException("weight in connectivity matrix can't be superior or equal to 1"); };

            int sum=0;
            for(int j=0; j<m_size; j++){
                if( m_weights[i][j] >= threshold && i!=j ){ sum++; }
            }
            return sum;
        }

    };

};


