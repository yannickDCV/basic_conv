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

    IdealPractice m_idPractice;
    Sigmoid m_importanceYield, m_importanceEnv;

    Identity(final AbstractPractice ap, final double percYield, final double percEnv ){
        m_idPractice = new IdealPractice( ap );
        m_importanceYield = new Sigmoid(percYield);
        m_importanceEnv = new Sigmoid(percEnv);
    }

    Identity( final Identity id ){
        this(id.m_idPractice, id.m_importanceYield.getValue(), id.m_importanceEnv.getValue());
    }

    Identity( final AbstractPractice ap ){
        this(ap, ap.getPercentageYield(), ap.getPercentageEnv());
    }

    public void update( final RealPractice rp, final Evaluation eval, final References ref ){ 
        if( eval.getDiffSatisfaction() >= 0. ){
           addStepYield(rp.getStratYield()); 
           addStepEnv(rp.getStratEnv()); 
        }
        else{
           addStepYield(-1*rp.getStratYield()); 
           addStepEnv(-1*rp.getStratEnv()); 
        }
        m_idPractice.update(m_importanceYield.getValue(), m_importanceEnv.getValue(), ref);
    }

    public void addStepYield( final double coef ){ m_importanceYield.stepFromSigmoid(coef); }
    public void addStepEnv( final double coef ){ m_importanceEnv.stepFromSigmoid(coef); }

    public double getProbaIncreaseYield(){ return m_importanceYield.getValue(); }
    public double getProbaIncreaseEnv(){ return m_importanceEnv.getValue(); }
    public IdealPractice getIdealPractice(){ return m_idPractice; }

    // FIXME avec IdealPractice ou direct m_importance*?
    public double getDistFrom( final Identity id, final References ref ){
        return getIdealPractice().getDistFrom( id.getIdealPractice(), ref );
        // double distYield = Math.abs(m_importanceYield.getValue()-id.m_importanceYield.getValue());
        // double distEnv = Math.abs(m_importanceEnv.getValue()-id.m_importanceEnv.getValue());
        // return 0.5*(distYield + distEnv);
    }

    public double getDistFrom( final AbstractPractice ap, final References ref ){
        return m_idPractice.getDistFrom(ap,ref);
    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("identity_yield_"+id+"_"+",");
            fw.append("identity_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        try{
            fw.append(m_importanceYield.getValue() + ",");
            fw.append(m_importanceEnv.getValue() + ",");
        }catch(IOException e){ e.printStackTrace(); }
    }


};


