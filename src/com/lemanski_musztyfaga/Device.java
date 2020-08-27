package com.lemanski_musztyfaga;

import java.util.ArrayList;
import java.util.List;

public class Device
{
    List<Antenna> antennas = new ArrayList<>();
    List<Multipath> multipaths = new ArrayList<>();
    String name;

    public Device()
    {
    }

    public Device(String name, Multipath path1, Multipath path2)
    {
        this.name = name;
        this.multipaths.add(path1);
        this.multipaths.add(path2);
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

    public String getName()
    {
        return name;
    }

    public List<Multipath> getMultipaths()
    {
        return multipaths;
    }
}
