package com.lemanski_musztyfaga;

public interface PathLossModel
{
    String getName();
    double getPathLoss();
    void setPathLoss(double h_b, double d, double h_m, double f);
}