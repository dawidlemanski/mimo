package com.lemanski_musztyfaga;

public class Signal
{
    double time;
    double power;


    public Signal(double time, double power)
    {
        this.time = time;
        this.power = power;
    }

    public double getTime()
    {
        return time;
    }

    public void setTime(double time)
    {
        this.time = time;
    }

    public double getPower()
    {
        return power;
    }

    public void setPower(double power)
    {
        this.power = power;
    }
}
