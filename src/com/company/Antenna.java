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

//    wersja z 15_arrays2.pdf
//    public double arrayFactor(double N, double theta, double alpha)
//    {
////        if (angle > 180 || angle < 0) return 999999;   // pomijamy sygnał gdy poza zasięgiem anteny?
//        theta = Math.toRadians(theta);
//        alpha= Math.toRadians(alpha);
//        double gamma = Math.PI * Math.cos(theta) + alpha;
//        return Math.abs(Math.sin(N * gamma / 2) / (N * Math.sin(gamma / 2)));
//    }

//    wersja z Phased Array Antenna Patterns—Part 1: Linear Array Beam Characteristics and Array Factor
    public double arrayFactor(double N, double theta, double phi)
    {
    //        if (angle > 180+90 || angle < -90) return 999999;   // pomijamy sygnał gdy poza zasięgiem anteny?
        theta = Math.toRadians(theta);
        phi = Math.toRadians(phi);
        if (Math.sin(theta) - Math.sin(phi) == 0) return 0;
        return Math.abs(Math.sin(N * (Math.PI / 2 * (Math.sin(theta) - Math.sin(phi)))) / (N * Math.sin(Math.PI / 2 * (Math.sin(theta) - Math.sin(phi)))));
    }
}


