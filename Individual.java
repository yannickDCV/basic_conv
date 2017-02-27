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

import java.util.Arrays;
import java.io.*;
import java.util.Random;
/**
 *
 * @author
 */

public class Individual {

    private int m_id;
    private Practice m_practice;
    // Comment je m'évalue par rapport aux autres
    private Identity m_identity;

    public Individual(final int id, final Identity identity, final Practice practice){
        m_id = id;
        m_identity = new Identity(identity);
        m_practice = new Practice(practice);
    }

    public Individual(final Individual indToClone){
        this(indToClone.getId(), indToClone.getIdentity(), indToClone.getPractice());
    }

    public int getId(){ return m_id; }
    public Identity getIdentity(){ return m_identity; }
    public Practice getPractice(){ return m_practice; }

    public void print(final FileWriter fw){
        m_identity.print(fw);
        m_practice.print(fw);
    }

    public double distId( final Individual ind ){
        return 0.5*( Math.abs(ind.getIdentity().getYield() - m_identity.getYield()) + Math.abs(ind.getIdentity().getEnv() - m_identity.getEnv()) );
    }

    /*/*
    public void updateIdentity(final double yieldRef, final double envRef){
        double diffYield = m_identity.getYield() - yieldRef;
        double diffEnv = m_identity.getEnv() - envRef;

        m_identity.addStepYield(diffYield*0.1);
        m_identity.addStepEnv(diffEnv*0.1);
    }
     // */

    /**/
    public void updateIdentity(final double yieldRef, final double envRef){
        double diffYield = m_practice.getYield() - yieldRef;
        double diffEnv = m_practice.getEnv() - envRef;

        m_identity.addStepYield(diffYield*Sigmoid.p_tinyStep);
        m_identity.addStepEnv(diffEnv*Sigmoid.p_tinyStep);
    }
     // */

    public void updatePractice(){
        if ( Math.random() <= m_identity.getYield() ){
            m_practice.increaseYield();
        }
        if ( Math.random() <= m_identity.getEnv() ){
            m_practice.increaseEnv();
        }
    }

    public static class Identity{
        private Sigmoid m_yield;
        private Sigmoid m_env;

        Identity(final double yield, final double env){
            m_yield = new Sigmoid(yield);
            m_env = new Sigmoid(env);
        }

        Identity(final Identity id){ this(id.getYield(),id.getEnv()); }

        public void addStepYield( final double step){
            m_yield.stepFromSigmoid(step);
        }

        public void addStepEnv( final double step){
            m_env.stepFromSigmoid(step);
        }

        public double getYield(){ return m_yield.getValue(); }
        public double getEnv(){ return m_env.getValue(); }

        public void print(final FileWriter fw){
            m_yield.print(fw);
            m_env.print(fw);
        }
    };

    public static class Practice{
        private int m_yield_lvl;
        private int m_env_lvl;
        private Sigmoid m_coef_yield;
        private Sigmoid m_coef_env;

        // l'idée ici est de lier env et yield. Plus env baisse, plus yield baisse. Plus yield augmente, plus env baisse
        Practice(final int yield, final int env){
            m_yield_lvl = yield;
            m_env_lvl = env;
            m_coef_yield = new Sigmoid(1.);
            m_coef_env = new Sigmoid(1.);
        }

        Practice(final Practice pr){ this(pr.m_yield_lvl,pr.m_env_lvl); }

        private void computeCoef(){
            // TODO
        }

        public void increaseYield(){ m_yield_lvl++; this.computeCoef(); }
        public void increaseEnv(){ m_env_lvl++; this.computeCoef(); }

        public double getYield(){ return (m_coef_yield.getValue()*m_yield_lvl); }
        public double getEnv(){ return (m_coef_env.getValue()*m_env_lvl); }

        public void print(final FileWriter fw){
            try{
                fw.append(this.getYield() + ",");
                fw.append(this.getEnv()+ ",");
            }catch(IOException e){ e.printStackTrace(); }
        }

    };
};
