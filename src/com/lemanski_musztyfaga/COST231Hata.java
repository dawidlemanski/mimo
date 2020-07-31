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
        double s = PathLossModel.randomShadowing();
        double a;

        a = (1.11 * Math.log10(f) - 0.7) * h_m - (1.56 * Math.log10(f) - 0.8);
        this.pathLoss = 46.3 + 33.9 * Math.log10(f)
                - 13.82 * Math.log10(h_b)
                + (44.9 - 6.55 * Math.log10(h_b)) * Math.log10(d)
                + s - a;
    }
}