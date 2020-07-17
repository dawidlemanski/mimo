package com.company;

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

    public double arrayFactor(double N, double angle)
    {
        if (angle > 180 || angle < 0) return 0;
        double phi = Math.toRadians(angle);
        return Math.abs(Math.sin(N * Math.PI * Math.cos(phi) / 2) / (N * Math.sin(Math.PI * Math.cos(phi) / 2)));
    }
}


