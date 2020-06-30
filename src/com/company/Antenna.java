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

    public void addSignal(Signal signal)
    {
        this.signalList.add(signal);
    }
}
