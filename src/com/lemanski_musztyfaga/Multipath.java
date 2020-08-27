package com.lemanski_musztyfaga;

public class Multipath
{
    double x;
    double y;
    double length;
    double angle;

    public Multipath(double x, double y, double length)
    {
        this.x = x;
        this.y = y;
        this.length = length;
        this.angle = Math.toDegrees(Math.atan2(y,x));
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getAngle()
    {
        return angle;
    }

    public double getLength()
    {
        return length;
    }

    public double rotate(double angle)
    {
        double angle_rad = Math.toRadians(angle);
        double x_new = this.x * Math.cos(angle_rad) + this.y * Math.sin(angle_rad);
        double y_new = -this.x * Math.sin(angle_rad) + this.y * Math.cos(angle_rad);
        return Math.toDegrees(Math.atan2(y_new, x_new));
    }
}
