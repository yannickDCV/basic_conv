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
    private Accounts m_accounts;
    private Alternative m_alternative;
    private Identity m_identity;

    private Sigmoid m_doubt;
    private Sigmoid m_probaAcceptEnv;
    private Sigmoid m_probaAcceptYield;

    public Individual(final int id, final Identity identity, final Practice practice, final Accounts accounts){
        m_id = id;
        m_identity = new Identity(identity);
        m_practice = new Practice(practice);
        m_accounts = new Accounts(accounts);
        m_alternative = new Alternative();

        m_doubt = new Sigmoid(Sigmoid.p_min);
        m_probaAcceptEnv = new Sigmoid(m_identity.getEnv());
        m_probaAcceptYield = new Sigmoid(m_identity.getYield());
    }

    public Individual(final Individual toClone){
        this(toClone.m_id, toClone.m_identity, toClone.m_practice, toClone.m_accounts);
    }

    public Identity getIdentity(){ return m_identity; }
    public Practice getPractice(){ return m_practice; }
    public boolean isViable(){ return m_accounts.isViable(); }

    public void printHeaders(final FileWriter fw){
        m_identity.printHeaders(fw,m_id);
        m_practice.printHeaders(fw,m_id);
        m_alternative.printHeaders(fw,m_id);
        m_accounts.printHeaders(fw,m_id);
        try{
            fw.append("doubt_"+m_id+"_"+",");
            fw.append("probaAcceptEnv_"+m_id+"_"+",");
            fw.append("probaAcceptYield_"+m_id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        m_identity.print(fw);
        m_practice.print(fw);
        m_alternative.print(fw);
        m_accounts.print(fw);
        m_doubt.print(fw);
        m_probaAcceptEnv.print(fw);
        m_probaAcceptYield.print(fw);
    }

    public void updateIdentity(final double yieldRef, final double envRef){
        double diffYield = m_practice.getYield() - yieldRef;
        double diffEnv = m_practice.getEnv() - envRef;

        m_identity.addStepYield(diffYield*Sigmoid.p_tinyStep);
        m_identity.addStepEnv(diffEnv*Sigmoid.p_tinyStep);
    }

    public double distId( final Individual ind ){
        return 0.5*( Math.abs(ind.getIdentity().getYield() - m_identity.getYield()) + Math.abs(ind.getIdentity().getEnv() - m_identity.getEnv()) );
    }

    public void updateAlternative(final double yieldRef, final double envRef){
        m_alternative.update(yieldRef,envRef);
    }

    public void updateAccounts(final double price){
        m_accounts.update(m_practice,price);
    }

    public void updatePractice(){

        // double stepDoubt = Sigmoid.p_tinyStep;
        // FIXME
        double stepDoubt = 0.1;
        if( isViable() ){ stepDoubt = -1.*stepDoubt; }
        m_doubt.stepFromSigmoid(stepDoubt);

        m_probaAcceptYield.setValue(m_doubt.getValue()*m_alternative.getYield() + (1-m_doubt.getValue())*m_identity.getYield());
        m_probaAcceptEnv.setValue(m_doubt.getValue()*m_alternative.getEnv() + (1-m_doubt.getValue())*m_identity.getEnv());

        if ( Math.random() <= m_probaAcceptYield.getValue() ){
            m_practice.increaseYield();
        }
        if ( Math.random() <= m_probaAcceptEnv.getValue() ){
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

        public void printHeaders(final FileWriter fw, final int id){
            try{
                fw.append("yieldId_"+id+"_"+",");
                fw.append("envId_"+id+"_"+",");
            }catch(IOException e){ e.printStackTrace(); }
        }

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

        Practice(final int yield, final int env){
            m_yield_lvl = yield;
            m_env_lvl = env;
            m_coef_yield = new Sigmoid(Sigmoid.p_max);
            m_coef_env = new Sigmoid(Sigmoid.p_max);
            computeCoef();
        }

        Practice(final Practice pr){ this(pr.m_yield_lvl,pr.m_env_lvl); }

        private void computeCoef(){
            // TODO direct sigmoid
        }

        public void increaseYield(){ m_yield_lvl++; this.computeCoef(); }
        public void increaseEnv(){ m_env_lvl++; computeCoef(); }

        public double getYield(){ return (m_coef_yield.getValue()*m_yield_lvl); }
        public double getEnv(){ return (m_coef_env.getValue()*m_env_lvl); }
        public int getYieldLevel(){ return m_yield_lvl; }
        public int getEnvLevel(){ return m_env_lvl; }

        public void printHeaders(final FileWriter fw, final int id){
            try{
                fw.append("yieldPractice_"+id+"_"+",");
                fw.append("envPractice_"+id+"_"+",");
            }catch(IOException e){ e.printStackTrace(); }
        }

        public void print(final FileWriter fw){
            try{
                fw.append(getYield() + ",");
                fw.append(getEnv()+ ",");
            }catch(IOException e){ e.printStackTrace(); }
        }

    };

    public static class Accounts{
        private double m_income;
        private double m_costs;
        // private Sigmoid m_coef_costs;
        private double m_gain;
        private double m_last_gain;
        private boolean m_isViable;

        Accounts(final double income, final double costs){
            m_income = income;
            m_costs = costs;
            // m_coef_costs = new Sigmoid(Sigmoid.p_max);
            m_gain = income - costs;
            m_last_gain = m_gain;
            m_isViable = true;
        }

        Accounts(final Accounts toClone){
            this(toClone.m_income,toClone.m_costs);
        }

        public void update( final Practice pr, final double price ){
            m_costs = computeCosts( pr );
            m_income = computeIncome( pr, price );
            m_last_gain = m_gain;
            m_gain = m_income - m_costs;
            if( m_last_gain > m_gain ){ m_isViable = false; }
            else { m_isViable = true; }
        }

        public double computeCosts( final Practice pr ){
            // TODO faire une vrai fonction ici
            // Sigmoid sig = new Sigmoid(0.5,0.,5000.,0.001);
            // return pr.getYieldLevel()*(sig.getValue(pr.getYieldLevel())*3.+1.);
            return 0.;

        }

        public double computeIncome( final Practice pr, final double price ){
            return pr.getYield()*price;
        }

        public boolean isViable() { return m_isViable; }

        public void printHeaders(final FileWriter fw, final int id){
            try{
                fw.append("gain_"+id+"_"+",");
                fw.append("costs_"+id+"_"+",");
            }catch(IOException e){ e.printStackTrace(); }
        }

        public void print(final FileWriter fw){
            try{
                fw.append(m_gain+",");
                fw.append(m_costs+",");
            }catch(IOException e){ e.printStackTrace(); }
        }

    };

    public class Alternative{
        private Sigmoid m_yield;
        private Sigmoid m_env;

        Alternative(){
            m_yield = new Sigmoid();
            m_env = new Sigmoid();
        }

        public void update(final double yield, final double env){
            m_yield.setValue(yield);
            m_env.setValue(env);
        }

        public double getYield(){ return m_yield.getValue(); }
        public double getEnv(){ return m_env.getValue(); }

        public void printHeaders(final FileWriter fw, final int id){
            try{
                fw.append("yieldAlt_"+id+"_"+",");
                fw.append("envAlt_"+id+"_"+",");
            }catch(IOException e){ e.printStackTrace(); }
        }

        public void print(final FileWriter fw){
            m_yield.print(fw);
            m_env.print(fw);
        }
    };

};
