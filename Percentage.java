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

public class Percentage{

    double m_value;

    Percentage( final double value ){ set(value); }
    Percentage( final Percentage p ){ this(p.m_value); }

    public void set( final double value ){
        errorRange(value);
        m_value = value;
    }

    public void set( final Percentage p ){
        m_value = p.m_value;
    }

    public double getValue(){ return m_value; }

    private void errorRange( final double x ){
        if ( x < 0. ) { throw new RuntimeException("In Percentage: x can't be inferior than 0"); };
        if ( x > 1. ) { throw new RuntimeException("In Percentage: x can't be superior than 1"); };
    }

    public void print(final FileWriter fw){
        try{
            fw.append(m_value + ",");
        }catch(IOException e){ e.printStackTrace(); }
    }


};





