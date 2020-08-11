package com.lemanski_musztyfaga;

public class Erceg implements PathLossModel
{
    double pathLoss;
    String name;

    public Erceg()
    {
        this.name = "Erceg";
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
        double d0 = 0.1;
        double gamma;
        double L_f = 0;
        double L_m = 0;
        double a = 4.6, b = 0.0075, c = 12.6;   // typ A
//        double a = 4, b = 0.0065, c = 17.1; // typ B
//        double a = 3.6, b = 0.005, c = 20;   // typ C
        double S = PathLossModel.randomShadowing();

        gamma = a - b * h_b + c / h_b;
        if (f > 2000)
            L_f = 6 * Math.log10(f / 2000);
        if (h_m > 2)
            L_m = -10.8 * Math.log10(h_m / 2000); // typ A i B
        // L_m = -20 * Math.log10(h_m / 2000);  // typ C

        this.pathLoss = 20 * Math.log10(4 * Math.PI * d0 / gamma)
                + 10 * gamma * Math.log10(d / d0)
                + S + L_f + L_m;
    }
}
