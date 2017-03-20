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

public class References{

    int m_maxYieldEverSeen, m_maxEnvEverSeen;
    int m_minYieldEverSeen, m_minEnvEverSeen;

    References(final int maxYield, final int minYield, final int maxEnv, final int minEnv){
        if( minYield <=0 ) { throw new RuntimeException("In References : minYield can't be inferior or equal to 0"); }
        if( minEnv <=0 ) { throw new RuntimeException("In References : minEnv can't be inferior or equal to 0"); }
        if( maxYield < minYield ) { throw new RuntimeException("In References : maxYield can't be inferior than minYield"); }
        if( maxEnv < minEnv ) { throw new RuntimeException("In References : maxEnv can't be inferior than minEnv"); }
        m_maxYieldEverSeen = maxYield;
        m_minYieldEverSeen = minYield;
        m_maxEnvEverSeen = maxEnv;
        m_minEnvEverSeen = minEnv;
    }

    References(final RealPractice pr){ this(pr.getYield(),pr.getYield(),pr.getEnv(),pr.getEnv()); }

    public void update( final RealPractice pr ){
        int yield = pr.getYield();
        int env = pr.getEnv();
        if( m_maxYieldEverSeen < yield ){ m_maxYieldEverSeen = yield; }
        if( m_minYieldEverSeen > yield ){ m_minYieldEverSeen = yield; }
        if( m_maxEnvEverSeen < env ){ m_maxEnvEverSeen = env; }
        if( m_minEnvEverSeen > env ){ m_minEnvEverSeen = env; }
    }

    public int getMaxYield(){ return m_maxYieldEverSeen; }
    public int getMaxEnv(){ return m_maxEnvEverSeen; }
    public int getMinYield(){ return m_minYieldEverSeen; }
    public int getMinEnv(){ return m_minEnvEverSeen; }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("ref_max_yield_"+id+"_"+",");
            fw.append("ref_max_env_"+id+"_"+",");
            fw.append("ref_min_yield_"+id+"_"+",");
            fw.append("ref_min_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        try{
            fw.append(m_maxYieldEverSeen + ",");
            fw.append(m_maxEnvEverSeen + ",");
            fw.append(m_minYieldEverSeen + ",");
            fw.append(m_minEnvEverSeen + ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};

