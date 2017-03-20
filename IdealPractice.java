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

public class IdealPractice extends AbstractPractice{

    IdealPractice( final int yield, final int env ){ super(yield,env); }
    IdealPractice(final AbstractPractice ap){ super(ap); }

    public void addYield( final int step ){
        m_yield_lvl += step;
        m_yield_lvl = (m_yield_lvl>0) ? m_yield_lvl : 0; 
    }

    public void addEnv( final int step ){
        m_env_lvl += step;
        m_env_lvl = (m_env_lvl>0) ? m_env_lvl : 0; 
    }

    public void update( final double percentYield, final double percentEnv, final References ref ){
        if ( percentYield < 0. ) { throw new RuntimeException("In IdealPractice: percentYield can't be inferior than 0"); };
        if ( percentYield > 1. ) { throw new RuntimeException("In IdealPractice: percentYield can't be superior than 1"); };
        if ( percentEnv < 0. ) { throw new RuntimeException("In IdealPractice: percentEnv can't be inferior than 0"); };
        if ( percentEnv > 1. ) { throw new RuntimeException("In IdealPractice: percentEnv can't be superior than 1"); };

        final int maxYield = ref.getMaxYield();
        final int minYield = ref.getMinYield();
        final int maxEnv = ref.getMaxEnv();
        final int minEnv = ref.getMinEnv();

        m_yield_lvl = (int) Math.round( (maxYield-minYield)*percentYield+minYield );
        m_env_lvl = (int) Math.round( (maxEnv-minEnv)*percentEnv+minEnv );
    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("idealPractice_yield_"+id+"_"+",");
            fw.append("idealPractice_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};

