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

    Population( final int popSize ){
        m_popSize = popSize;
        m_individuals = new Individual[popSize];

        RealPractice practice = new RealPractice(100,100);
        Identity identity = new Identity(practice);

        for(int i=0; i<m_individuals.length; i++){
            Influences influences = new Influences(i,popSize, 0.1);
            Individual indToAdd = new Individual(i, practice, identity, influences);
            m_individuals[i] = new Individual(indToAdd);
        }

    }

    public void iter(final double price) {

        for(int i=0; i<m_individuals.length; i++){
            int[] ids = m_individuals[i].getPeopleToDiscussWith(m_individuals.length);
            for(int j=0; j<ids.length; j++){
                m_individuals[i].discussWith( m_individuals[ids[j]] );
            }
        }

          for(int i=0; i<m_individuals.length; i++){
              m_individuals[i].iter(price);
          }

    }

    public void printHeaders(final FileWriter fw){
         for(int i=0; i<m_individuals.length; i++){
             m_individuals[i].printHeaders(fw);
         }
    }

    public void print(final FileWriter fw){
        for(int i=0; i<m_individuals.length; i++){
            m_individuals[i].print(fw);
        }
    }

};
