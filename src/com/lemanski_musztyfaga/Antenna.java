package com.lemanski_musztyfaga;

import java.util.ArrayList;
import java.util.List;

public class Antenna
{
    double power;
    List<Signal> signalList = new ArrayList<>();

    public Antenna()
    {
    }

    public double getPower()
    {
        return power;
    }

    public void setPower(double power)
    {
        this.power = power;
    }

    public List<Signal> getSignalList()
    {
        return signalList;
    }

    public void setSignalList(List<Signal> signalList)
    {
        this.signalList = signalList;
    }

    public void clearSignalList()
    {
        this.signalList = new ArrayList<>();
    }

    public void addSignal(Signal signal)
    {
        this.signalList.add(signal);
    }

//    wersja z Phased Array Antenna Patterns—Part 1: Linear Array Beam Characteristics and Array Factor
    public double arrayFactor(double N, double theta, double theta0)
    {
        theta = Math.toRadians(theta);
        theta0 = Math.toRadians(theta0);
        if (Math.sin(theta) - Math.sin(theta0) == 0) return 0;
        return Math.abs(Math.sin(N * Math.PI / 2 * (Math.sin(theta) - Math.sin(theta0))) / (N * Math.sin(Math.PI / 2 * (Math.sin(theta) - Math.sin(theta0)))));
    }
}


