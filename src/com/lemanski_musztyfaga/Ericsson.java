package com.lemanski_musztyfaga;

public class Ericsson implements PathLossModel
{
    double pathLoss;
    String name;

    public Ericsson()
    {
        this.name = "Ericsson";
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
        double a0 = 36.2, a1 = 30.2, a2 = 12.0, a3 = 0.1;  // teren miejski
//        double a0 = 43.20, a1 = 43.2, a2 = 45.95, a3 = 0.1;  // teren podmiejski
        double g;
        double a = 0;

        if (h_m !=0)
            a = 3.2 * Math.log10(Math.pow(11.75 * h_m, 2));
        g = 44.49 * Math.log10(f) - 4.78 * Math.pow(Math.log10(f), 2);
        this.pathLoss = a0 + a1 * Math.log10(d)
                + a2 * Math.log10(h_b)
                + a3 * Math.log10(h_b) * Math.log10(d)
                - a + g;
    }
}
