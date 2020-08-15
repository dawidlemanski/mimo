package com.lemanski_musztyfaga;

public class Hata implements PathLossModel
{
    double pathLoss;
    String name;

    public Hata()
    {
        this.name = "Hata";
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public double getPathLoss()
    {
        return this.pathLoss;
    }

    @Override
    public void setPathLoss(double h_b, double d, double h_m, double f)
    {
        double a;

        a = 3.2 * (Math.pow(Math.log10(11.75 * h_m), 2)) - 4.97;
        this.pathLoss = 69.55 + 26.16 * Math.log10(f)
                - 13.82 * Math.log10(h_b)
                + (44.9 - 6.55 * Math.log10(h_b)) * Math.log10(d)
                - a;
    }
}
