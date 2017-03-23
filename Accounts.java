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

public class Accounts{

    private Sigmoid m_viability;
    private double m_gain=0., m_lastGain=0.;

    Accounts(){ m_viability = new Sigmoid(); }

    public void update( final RealPractice pr, final double price ){
        // TODO price = correctPriceFromEnv(price, m_practice);
        // FIXME pb si prix en marche d'escalier
        m_gain = price*pr.getYield();
        // TODO à reref
        m_viability.stepFromSigmoid(Math.signum(m_gain-m_lastGain));
        m_lastGain = m_gain;
    }

    public double getViability(){ return m_viability.get(); }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("viability_"+id+"_"+",");
            fw.append("gain_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        try{
            fw.append(m_viability.get() + ",");
            fw.append(m_gain + ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};


