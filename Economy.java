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
package model.redQueen;

import java.util.*;
import java.io.*;

/**
 *
 * @author
 */
public class Economy {

    private double m_price;

    public Economy(final double priceStart) {
        m_price = priceStart;
    }

    // FIXME abstraction 
    void public iter(){

        m_price = Utils.stepFromSigmoide(

    } 

    double public getPrice(){ return m_price; }


}

