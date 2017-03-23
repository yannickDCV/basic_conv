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

public class Identity{

    Sigmoid m_importanceYield, m_importanceEnv;

    Identity(final double percYield, final double percEnv ){
        //TODO test pourcentage
        m_importanceYield = new Sigmoid(percYield);
        m_importanceEnv = new Sigmoid(percEnv);
    }

    Identity( final Identity id ){
        this(id.m_importanceYield.get(), id.m_importanceEnv.get());
    }

    Identity( final AbstractPractice ap ){
        this(ap.getPercentageYield(), ap.getPercentageEnv());
    }

    public void update( final RealPractice rp, final Evaluation eval, final References ref ){ 

        int coef = ( eval.getDiffSatisfaction() >= 0. ) ? 1 : -1;

        m_importanceYield.stepFromSigmoid(coef*rp.getStratYield()); 
        m_importanceEnv.stepFromSigmoid(coef*rp.getStratEnv()); 
    }

    public double getProbaIncreaseYield(){ return m_importanceYield.get(); }
    public double getProbaIncreaseEnv(){ return m_importanceEnv.get(); }
    private IdealPractice getIdealPractice(final References ref){ return (new IdealPractice(m_importanceYield,m_importanceEnv,ref)); }

    public double getDistFrom( final Identity id, final References ref ){
        double distYield = Math.abs(m_importanceYield.get()-id.m_importanceYield.get());
        double distEnv = Math.abs(m_importanceEnv.get()-id.m_importanceEnv.get());
        // TODO à changer. Pour test juste yield
        //return 0.5*(distYield + distEnv);
        return distYield;
    }

    public double getPerceivedDistFrom( final AbstractPractice ap, final References ref ){
        return getIdealPractice(ref).getPerceivedDistFrom(ap,ref);
    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("identity_yield_"+id+"_"+",");
            fw.append("identity_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        try{
            fw.append(m_importanceYield.get() + ",");
            fw.append(m_importanceEnv.get() + ",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};

