/*
 * Copyright (C) 2016 Irstea
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

import java.util.*;
import java.io.*;

/**
 *
 * @author
 */
public class Economy {

    private double m_price;
    private Scanner m_scan;

    public Economy( final String fileName ) {
        try {
            m_scan = new Scanner(new File(fileName));
            m_scan.useLocale(Locale.US);
        } catch (FileNotFoundException e1) { e1.printStackTrace(); }
        m_price = -1;
    }

    public void iter(){
        if(m_scan.hasNextDouble()) {
            m_price = m_scan.nextDouble();
        }
        else {
            throw new RuntimeException("Error during reading prices file");
        }
    } 

    public void printHeaders(final FileWriter fw){
        try{
            fw.append("price"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public void print(final FileWriter fw){
        try{
            fw.append(m_price+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

    public double getPrice(){ return m_price; }

};

