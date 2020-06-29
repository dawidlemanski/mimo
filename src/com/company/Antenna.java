package com.company;

import java.util.ArrayList;
import java.util.List;

public class Antenna
{
//    List<Double> powerList = new ArrayList<Double>();
//    List<Double> timeList = new ArrayList<Double>();
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



//    public List<Double> getPowerList()
//    {
//        return powerList;
//    }
//
//    public void setPowerList(List<Double> powerList)
//    {
//        this.powerList = powerList;
//    }
//
//    public List<Double> getTimeList()
//    {
//        return timeList;
//    }
//
//    public void setTimeList(List<Double> timeList)
//    {
//        this.timeList = timeList;
//    }
//
//    public void addPower(double power)
//    {
//        this.powerList.add(power);
//    }
//
//    public void addTime(double time)
//    {
//        this.timeList.add(time);
//    }
//
//    public void clearPowerList()
//    {
//        List<Double> newList = new ArrayList<Double>();
//        this.powerList = newList;
//    }
//
//    public void clearTimeList()
//    {
//        List<Double> newList = new ArrayList<Double>();
//        this.timeList = newList;
//    }
}
