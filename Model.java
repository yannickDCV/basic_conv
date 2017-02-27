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
public class Model {

    private final static String m_fileOut ="/home/yannick/model/basic_conv/data.txt";
    private Population m_population;

    public Model(final int popSize) {

        // Individual.Identity identity = new Individual.Identity(p_yieldStart, p_envStart);
        // Individual.Practice practice = new Individual.Practice(p_yieldStart, p_envStart);
        // Individual indToClone = new Individual(identity, practice);

        m_population = new Population(popSize);

        // Configuration config = new Configuration();
        // Population m_population = new Population(popSize, config, indToClone);
        // Economy m_economy = new Economy();

    }

    public void iter() {
        m_population.iter();
    }

    public void initPrint(final FileWriter fw){
        try{
            m_population.printHeaders(fw);
            fw.append("\n");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        try{
            m_population.print(fw);
            fw.append("\n");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public static void main(String[] args) {

        try{
            FileWriter fw = new FileWriter(m_fileOut);

            // TODO exceptions
            int nbStep = Integer.parseInt(args[0]);
            int popSize = Integer.parseInt(args[1]);

            Model model = new Model(popSize);
            model.initPrint(fw);
            model.print(fw);

            for (int i = 0; i < nbStep; i++) {
                System.out.println("Iteration " + i);
                model.iter();
                model.print(fw);
            }

            fw.close();
            System.out.println( "nbStep = " + nbStep ); 
            System.out.println( "popSize = " + popSize ); 
        }catch(IOException e){ e.printStackTrace(); }
    }

};
