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

    IdealPractice( final Sigmoid percentYield, final Sigmoid percentEnv, final References ref ){
        super(
        (int) Math.round( (ref.getMaxYield()-ref.getMinYield())*percentYield.get()+ref.getMinYield() ), // m_yield_lvl
        (int) Math.round( (ref.getMaxEnv()-ref.getMinEnv())*percentEnv.get()+ref.getMinEnv() ) // m_env_lvl
        );
    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("idealPractice_yield_"+id+"_"+",");
            fw.append("idealPractice_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};

