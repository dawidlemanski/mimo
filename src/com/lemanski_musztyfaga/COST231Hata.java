package com.lemanski_musztyfaga;

public class COST231Hata implements PathLossModel
{
    double pathLoss;
    String name;

    public COST231Hata()
    {
        this.name = "COST231Hata";
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
        double C_m;

        a = 3.2 * (Math.pow(Math.log10(11.75 * h_m), 2)) - 4.97;
        C_m = 3;
        this.pathLoss = 46.3 + 33.9 * Math.log10(f)
                - 13.82 * Math.log10(h_b)
                + (44.9 - 6.55 * Math.log10(h_b)) * Math.log10(d)
                - a + C_m;
    }
}
