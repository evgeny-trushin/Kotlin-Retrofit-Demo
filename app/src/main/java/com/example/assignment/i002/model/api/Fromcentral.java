package com.example.assignment.i002.model.api;


public class Fromcentral
{
    private String car;

    private String train;

    public String getCar ()
    {
        return car;
    }

    public void setCar (String car)
    {
        this.car = car;
    }

    public String getTrain ()
    {
        return train;
    }

    public void setTrain (String train)
    {
        this.train = train;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [car = "+car+", train = "+train+"]";
    }
}

