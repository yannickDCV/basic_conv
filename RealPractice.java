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

    private int m_lastYield;
    private int m_lastEnv;

    RealPractice( final int yield, final int env ){ 
        super(yield,env); 
        m_lastYield = yield;
        m_lastEnv = env;
    }

    RealPractice(final AbstractPractice ap){ 
        super(ap); 
        m_lastYield = ap.m_yield_lvl;
        m_lastEnv = ap.m_env_lvl;
    }

    public void update( Identity id, final double diffSatisfaction){ 
    
        // FIXME changer id ailleurs
        if( diffSatisfaction > 0. ){
           id.addStepYield(m_yield_lvl-m_lastYield); 
           id.addStepEnv(m_env_lvl-m_lastEnv); 
        }
        m_lastYield = m_yield_lvl;
        m_lastEnv = m_env_lvl;

        // TODO ajouter probaNoStrat dans id
        if( Math.random() <= id.getProbaIncreaseYield() ) { m_yield_lvl++; }
        else { 
            m_yield_lvl--;
            m_yield_lvl = (m_yield_lvl>0) ? m_yield_lvl : 0; 
        }

        if( Math.random() <= id.getProbaIncreaseEnv() ) { m_env_lvl++; }
        else { 
            m_env_lvl--;
            m_env_lvl = (m_env_lvl>0) ? m_env_lvl : 0; 
        }
    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("RealPractice_yield_"+id+"_"+",");
            fw.append("RealPractice_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};

