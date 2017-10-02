/*
 * Class Name: CounterInfo
 *
 * Version : Version 1.0
 *
 * Date: October 2, 2017
 *
 * Copyright 2017 Eshna Sengupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eshna.eshna_CounterBook;



import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *Represents a counter
 *
 * @author eshna
 * @version 1.0
 * @see ActivityCounter
 * @see MainActivity
 * @see ViewCounterDetails
 * @since 1.0
 */


public class CounterInfo
{
    private String name;
    private String date;
    private Integer current;
    private Integer initial;
    private String comment;

    /**
     * Constructs a counter object
     * @param name name of the counter
     */
    public CounterInfo(String name)
    {
        this.name = name;
    }
    //getters
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
    public Integer getCurrent() {
        return current;
    }

    public Integer getInitial() {
        return initial;
    }

    public String getComment() {
        return comment;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }


    public void setDate() {
        Date today_date = new Date();
        this.date =  new SimpleDateFormat("yyyy-MM-dd").format(today_date);
    }


    public void setCurrent(String current) throws InputNumberException {
        try
        {
            if (Integer.parseInt(current) < 0)
                throw new InputNumberException();
            this.current = Integer.parseInt(current);
        }
        catch (NumberFormatException e) {
            throw new InputNumberException();
        }
    }

    public void setInitial(String initial) throws InputNumberException {
        try
        {

            if (Integer.parseInt(initial) < 0)
                throw new InputNumberException();
            this.initial = Integer.parseInt(initial);
        }
        catch (NumberFormatException e) {
            throw new InputNumberException();

        }

    }


    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     * @return outStr: String with information about the counter
     */
    @Override
    public String toString(){
        String outStr = "Name: "+this.name;

        outStr+= "\nDate Created/Changed: "+ this.date;

        outStr+= "\nCurrent value: "+ this.current;

        return outStr;
    }

    /**
     * puts information in a bundle
     *
     * @return bundle object
     */
    public Bundle sendBundle ()
    {
        Bundle bundle = new Bundle();
        bundle.putString("name", this.getName());
        bundle.putString("current_date", String.valueOf(this.getDate()));
        bundle.putString("current_value",String.valueOf(this.getCurrent()));
        bundle.putString("initial_value",String.valueOf(this.getInitial()));
        bundle.putString("comment", this.getComment());

        return bundle;
    }
}
