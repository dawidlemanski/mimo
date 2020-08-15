package com.lemanski_musztyfaga;

import java.io.*;
import java.util.*;

public class Main
{
    static double h_base_station = 70;  // m
    static double frequency = 1500; // MHz
    static double base_station_power = 45;  // dBm
    static double base_station_antenna_gain = 18;  // dBm
    static double base_station_cable_loss = 2;  // dBm
    static double h_mobile = 5; // m
    static double speed_wave = 2.997 * Math.pow(10,5); // km/s
    static double d_mobile1_multipath1 = 0.001*(303+722+109);   //km
    static double d_mobile1_multipath2 = 0.001*(613+642);   //km
    static double d_mobile2_multipath1 = 0.001*(301+577+147+373);   //km
    static double d_mobile2_multipath2 = 0.001*(383+566+324+144);   //km
    static double d_mobile3_multipath1 = 0.001*(726+592+309);   //km
    static double d_mobile3_multipath2 = 0.001*(542+728+187);   //km
    static double d_mobile4_multipath1 = 0.001*(1268+379);   //km
    static double d_mobile4_multipath2 = 0.001*(614+669+362);   //km
    static List<Multipath> multipaths = Arrays.asList(new Multipath(760,0, d_mobile1_multipath1),
        new Multipath(739,160, d_mobile1_multipath2),
        new Multipath(539,533, d_mobile2_multipath1),
        new Multipath(166,739, d_mobile2_multipath2),
        new Multipath(-174,736, d_mobile3_multipath1),
        new Multipath(-408,641, d_mobile3_multipath2),
        new Multipath(-501,567, d_mobile4_multipath1),
        new Multipath(-754,-14, d_mobile4_multipath2));

    public static void main(String[] args)
    {
        Device base_station = new Device();
        Antenna tx = new Antenna();
        base_station.addAntenna(tx);

        Device mobile1 = new Device("mobile1", multipaths.get(0), multipaths.get(1));
        Device mobile2 = new Device("mobile2", multipaths.get(2), multipaths.get(3));
        Device mobile3 = new Device("mobile3", multipaths.get(4), multipaths.get(5));
        Device mobile4 = new Device("mobile4", multipaths.get(6), multipaths.get(7));
        List<Device> mobiles;

        // scenariusz 1
        tx.setPower(mwToDbm(dbmToMw(base_station_power)/2) + base_station_antenna_gain - base_station_cable_loss);
        mobiles = Collections.singletonList(mobile1);
        scenario(mobiles, tx, 8, "scenario1");
        mobiles = Collections.singletonList(mobile2);
        scenario(mobiles, tx, 8, "scenario1");
        mobiles = Collections.singletonList(mobile3);
        scenario(mobiles, tx, 8, "scenario1");
        mobiles = Collections.singletonList(mobile4);
        scenario(mobiles, tx, 8, "scenario1");

        // scenariusz 2
        tx.setPower(mwToDbm(dbmToMw(base_station_power)/4) + base_station_antenna_gain - base_station_cable_loss);
        mobiles = Arrays.asList(mobile1, mobile2);
        scenario(mobiles, tx, 8, "scenario2");
        mobiles = Arrays.asList(mobile3, mobile4);
        scenario(mobiles, tx, 8, "scenario2");

        // scenariusz 3
        tx.setPower(mwToDbm(dbmToMw(base_station_power)/8) + base_station_antenna_gain - base_station_cable_loss);
        mobiles = Arrays.asList(mobile1, mobile2, mobile3, mobile4);
        scenario(mobiles, tx, 8,"scenario3");
    }

    public static void scenario(List<Device> mobiles, Antenna tx, double N, String scenario)
    {
        List<Antenna> antennas = new ArrayList<>();
        List<Multipath> multipaths_scenario = new ArrayList<>();
        for (int i = 0; i < mobiles.size(); i++)
        {
            for (int j = 0; j < 2; j++) // 2 anteny w każdym mobile
            {
                mobiles.get(i).addAntenna(new Antenna());
                antennas.add(mobiles.get(i).getAntennasList().get(j));
                multipaths_scenario.add(mobiles.get(i).getMultipaths().get(j));
            }
        }

        List<PathLossModel> models = Arrays.asList(new Hata(), new COST231Hata(), new Ericsson());
        for (PathLossModel model : models)
        {
            for (int i = 0; i < antennas.size(); i++)
            {
                Antenna rx = antennas.get(i);

                Iterator<Multipath> it = multipaths_scenario.listIterator(i);
                Multipath temp = (Multipath) it.next();
                double rotateAngle = temp.getAngle();
                it = multipaths_scenario.listIterator(i); // iterator reset

                for (int j = 0; j < antennas.size(); j++)
                {
                    if (!it.hasNext())
                        it = multipaths_scenario.iterator();
                    Multipath multipath = (Multipath) it.next();
                    double newAngle = multipath.rotate(multipath.x, multipath.y, rotateAngle);
//                    double arrayFactor = mwToDbm(rx.arrayFactor(N, 90 - rotateAngle, 90 - newAngle - rotateAngle));
                    double arrayFactor = mwToDbm(rx.arrayFactor(N, 90 - newAngle - rotateAngle, 90 - rotateAngle));
                    model.setPathLoss(h_base_station, multipath.getLength(), h_mobile, frequency);

                    Signal sig = new Signal(multipath.getLength() / speed_wave,  tx.getPower() + arrayFactor - model.getPathLoss());
                    System.out.println(scenario + " antena: " + (i + 1) + " długość: " + multipath.length
                            + " nachylenie wiązki: " + (90 - rotateAngle) + " kąt obserwacji " + (90 - newAngle - rotateAngle) + " współczynnik: " + arrayFactor + " model " + model.getName() + " " + model.getPathLoss());
                    rx.addSignal(sig);
                }
            }
            try
            {
                for (int i=0; i < mobiles.size(); i++)
                {
                    writeSignals(mobiles.get(i),scenario+"\\"+mobiles.get(i).getName()+model.getName()+".csv");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            // reset sygnałów dla kolejnego modelu
            for (int i=0; i < mobiles.size(); i++)
            {
                for (int j = 0; j < mobiles.get(i).getAntennasList().size(); j++)
                {
                    mobiles.get(i).getAntennasList().get(j).clearSignalList();
                }
            }
        }
        // reset anten dla kolejnego scenariusza
        for (int i=0; i < mobiles.size(); i++)
        {
            mobiles.get(i).getAntennasList().clear();
        }
    }

    public static void writeSignals(Device mobile, String fileName) throws IOException
    {
        try(FileWriter fw = new FileWriter("src/com/lemanski_musztyfaga/out/" + fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (int i = 0; i < mobile.getAntennasList().size(); i++)
            {
                Antenna rx1 = mobile.getAntennasList().get(i);
                out.println("Antenna" + (i+1));
                for (int j = 0; j < rx1.getSignalList().size(); j++)
                {
                    out.printf("%.2f\t", rx1.getSignalList().get(j).getTime() * Math.pow(10, 6));    // mikro s
                    out.printf("%.2f%n", rx1.getSignalList().get(j).getPower());    //dBm
                }
            }
        }
    }

    public static double mwToDbm(double mW)
    {
        if (mW == 0) return 0;
        return 10 * Math.log10(mW / 1);
    }

    public static double dbmToMw(double dBm)
    {
        return Math.pow(10, dBm / 10);
    }
}