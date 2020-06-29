package com.company;

import java.util.ArrayList;
import java.util.List;

public class Device
{
    List<Antenna> antennas = new ArrayList<>();
    double distance;
    List<Double> multiPaths;

    public Device()
    {
    }

    public Device(List<Antenna> Antennas)
    {
        this.antennas = Antennas;
    }

    public List<Antenna> getAntennasList()
    {
        return antennas;
    }

    public void setAntennasList(List<Antenna> antennnas)
    {
        this.antennas = antennnas;
    }

    public void addAntenna(Antenna antenna)
    {
        this.antennas.add(antenna);
    }

}
