package com.example.eshna.eshna_CounterBook;



import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eshna on 9/25/17.
 * Assignment 1
 */

public class CounterInfo
{
    private String name;
    private String date;
    private Integer current;
    private Integer initial;
    private String comment;

    //constructor
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

    @Override
    public String toString(){
        String outStr = "Name: "+this.name;

        outStr+= "\nDate Created/Changed: "+ this.date;

        outStr+= "\nCurrent value: "+ this.current;

        return outStr;
    }


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
