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

public class RealPractice extends AbstractPractice{

    private int m_strat_yield;
    private int m_strat_env;

    RealPractice( final int yield, final int env ){ 
        super(yield,env); 
        m_strat_yield = 0;
        m_strat_env = 0;
    }

    RealPractice(final AbstractPractice ap){ 
        this(ap.m_yield_lvl, ap.m_env_lvl); 
    }

    public void copy( final AbstractPractice toCopy ){ 
        m_yield_lvl = toCopy.m_yield_lvl;
        m_env_lvl = toCopy.m_env_lvl;
        m_strat_yield = 0;
        m_strat_env = 0;
    }

    public void update( final Identity id, final Evaluation eval ){ 

        if( Math.random() <= eval.getProbaMinorChange() ){

            if( Math.random() <= id.getProbaIncreaseYield() ) { m_strat_yield = 1; }
            else { m_strat_yield = -1; }
            m_yield_lvl += m_strat_yield;
            m_yield_lvl = (m_yield_lvl>0) ? m_yield_lvl : 0; 

            if( Math.random() <= id.getProbaIncreaseEnv() ) { m_strat_env = 1; }
            else { m_strat_env = -1; }
            m_env_lvl += m_strat_env;
            m_env_lvl = (m_env_lvl>0) ? m_env_lvl : 0; 
            
        }
        else{
            m_strat_yield = 0;
            m_strat_env = 0;
        }
    }

    public int getStratYield(){ return m_strat_yield; }
    public int getStratEnv(){ return m_strat_env; }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("realPractice_yield_"+id+"_"+",");
            fw.append("realPractice_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};

