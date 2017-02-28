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
    // Comment je m'évalue par rapport aux autres
    private Identity m_identity;

    public Individual(final int id, final Identity identity, final Practice practice, final Accounts accounts){
        m_id = id;
        m_identity = new Identity(identity);
        m_practice = new Practice(practice);
        m_accounts = new Accounts(accounts);
    }

    public Individual(final Individual toClone){
        this(toClone.m_id, toClone.m_identity, toClone.m_practice, toClone.m_accounts);
    }

    public Identity getIdentity(){ return m_identity; }
    public Practice getPractice(){ return m_practice; }

    public void print(final FileWriter fw){
        m_identity.print(fw);
        m_practice.print(fw);
    }

    /**/
    public void updateIdentity(final double yieldRef, final double envRef){
        double diffYield = m_identity.getYield() - yieldRef;
        double diffEnv = m_identity.getEnv() - envRef;

        // FIXME 0.1
        m_identity.addStepYield(diffYield*0.1);
        m_identity.addStepEnv(diffEnv*0.1);
    }
    // */

    /*/*
    public void updateIdentity(final double yieldRef, final double envRef){
        double diffYield = m_practice.getYield() - yieldRef;
        double diffEnv = m_practice.getEnv() - envRef;

        m_identity.addStepYield(diffYield*Sigmoid.p_tinyStep);
        m_identity.addStepEnv(diffEnv*Sigmoid.p_tinyStep);
    }

    public double distId( final Individual ind ){
        return 0.5*( Math.abs(ind.getIdentity().getYield() - m_identity.getYield()) + Math.abs(ind.getIdentity().getEnv() - m_identity.getEnv()) );
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

    /*
    private int computeStrategy(final double probaQuestion, final double probaAcceptProd){

        int strategy = m_strategy;

        // Est-ce que tu vas adopter l'augmentation d'intensification?
        double rand = Math.random();
        double probaNeutral = 1 - Math.abs(1-2*probaAcceptProd);
        if ( rand <= probaNeutral ){
            strategy = 0;
        }
        else {
            if ( rand  <= probaAcceptProd ){
                strategy = 1;
            }
            else { 
                strategy = -1;
            }
        }

        if ( Math.random() <= m_probaQuestion ){
            strategy = 0;
        }

        return strategy;
    }
    */

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

        Practice(final int yield, final int env){
            m_yield_lvl = yield;
            m_env_lvl = env;
            m_coef_yield = new Sigmoid(1.);
            m_coef_env = new Sigmoid(1.);
            computeCoef();
        }

        Practice(final Practice pr){ this(pr.m_yield_lvl,pr.m_env_lvl); }

        // l'idée ici est de lier env et yield. Plus env baisse, plus CoefYield baisse. Plus yield augmente, plus coefEnv baisse
        private void computeCoef(){
            // TODO direct sigmoid
        }

        public void increaseYield(){ m_yield_lvl++; this.computeCoef(); }
        public void increaseEnv(){ m_env_lvl++; computeCoef(); }

        public double getYield(){ return (m_coef_yield.getValue()*m_yield_lvl); }
        public double getEnv(){ return (m_coef_env.getValue()*m_env_lvl); }

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
        private double m_profit;
        private double m_last_profit;

        Accounts(final double income, final double costs){
            m_income = income;
            m_costs = costs;
            m_profit = income - costs;
            m_last_profit = m_profit;
        }

        Accounts(final Accounts toClone){
            this(toClone.m_income,toClone.m_costs);
        }

        public void updateAccounts( final Practice pr, final Economy eco ){
            m_costs = computeCosts( pr );
            m_income = computeIncome( pr, eco );
            m_last_profit = m_profit;
            m_profit = m_income - m_costs;
        }

        public double computeCosts( final Practice pr ){
            // TODO
            return m_costs;
        }

        public double computeIncome( final Practice pr, final Economy eco ){
            // TODO
            return m_income;
        }

        public double getProfit() { return m_profit; }
        public double getLastProfit() { return m_last_profit; }

    };

};
