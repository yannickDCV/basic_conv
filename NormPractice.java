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

public class NormPractice extends AbstractPractice{

    NormPractice( final int yield, final int env ){ super(yield,env); }
    NormPractice(final AbstractPractice ap){ super(ap); }

    // TODO
    public void update(final Influences inf){
    }

    public void printHeaders(final FileWriter fw, final int id){
        try{
            fw.append("normPractice_yield_"+id+"_"+",");
            fw.append("normPractice_env_"+id+"_"+",");
        }catch(IOException e){ e.printStackTrace(); }
    }

};


