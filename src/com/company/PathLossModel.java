package com.company;

import java.util.Random;

public interface PathLossModel
{
    String getName();
    double getPathLoss();
    void setPathLoss(double h_b, double d, double h_m, double f);
    static double randomShadowing()
    {
        Random r = new Random();
        double s;
        s = 8.2 + (10.6 - 8.2) * r.nextGaussian();

        return s;
    }
}