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

public abstract class AbstractPractice{
    protected int m_yield_lvl;
    protected int m_env_lvl;

    AbstractPractice(final int yield, final int env){
        if ( yield < 0 ) { throw new RuntimeException("In Practice : yield can't be inferior than 0"); };
        if ( env < 0 ) { throw new RuntimeException("In Practice : env can't be inferior than 0"); };
        m_yield_lvl = yield;
        m_env_lvl = env;
    }

    AbstractPractice(final AbstractPractice pr){ 
        this(pr.m_yield_lvl, pr.m_env_lvl); 
    }

    public void copy( final AbstractPractice toCopy ){ 
        m_yield_lvl = toCopy.m_yield_lvl; 
        m_env_lvl = toCopy.m_env_lvl; 
    }

    public double getDistFrom(final AbstractPractice ap, final int refYield, final int refEnv){
        if( refYield <=0 ) { throw new RuntimeException("In Practice : refYield can't be inferior or equal to 0"); }
        if( refEnv <=0 ) { throw new RuntimeException("In Practice : refEnv can't be inferior or equal to 0"); }
        
        double dist = 0.5*(Math.abs(ap.m_yield_lvl-m_yield_lvl)/((double) refYield) + Math.abs(ap.m_env_lvl-m_env_lvl)/((double) refEnv));
        dist = (dist>1.) ? 1. : dist;

        return dist;
    }

    public double getPercentageYield(){
        if( m_yield_lvl == 0 ) { return 0.5; }
        else { return (double) m_yield_lvl/(m_yield_lvl+m_env_lvl); }
    }

    public double getPercentageEnv(){
        if( m_env_lvl == 0 ) { return 0.5; }
        else { return (double) m_env_lvl/(m_yield_lvl+m_env_lvl); }
    }

    public int getYield(){ return m_yield_lvl; }
    public int getEnv(){ return m_env_lvl; }

    public abstract void printHeaders(final FileWriter fw, final int id);

    public void print(final FileWriter fw){
        try{
            fw.append(m_yield_lvl + ",");
            fw.append(m_env_lvl + ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};

