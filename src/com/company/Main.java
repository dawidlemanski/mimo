package com.company;

import java.io.*;
import java.util.*;

public class Main
{
    public static double shannonCapacity(double E, double h)
    {
        double capacity;
        double omega = 1;
        capacity = Math.log10(1 + E / Math.pow(omega, 2) * Math.pow(h, 2));

        return capacity;
    }

    static double h_base_station = 70;  // m
    static double frequency = 2000; // MHz
    static double power_base_station = 45;  // dBm
    static double h_mobile = 5; // m
    static double speed_wave = 2.997 * Math.pow(10,5); // km/s
    static double d_mobile1 = 1.091;    //km
    static double d_mobile2 = 1.377;    //km
    static double d_mobile3 = 1.441;    //km
    static double d_mobile4 = 1.623;    //km
    static double d_mobile1_multipath1 = 0.001*(303+722+109);   //km
    static double d_mobile1_multipath2 = 0.001*(613+642);   //km
    static double d_mobile1_multipath3 = 0.001*(687+415);   //km
    static double d_mobile2_multipath1 = 0.001*(301+577+147+373);   //km
    static double d_mobile2_multipath2 = 0.001*(383+566+324+144);   //km
    static double d_mobile2_multipath3 = 0.001*(824+323+301);   //km
    static double d_mobile3_multipath1 = 0.001*(726+592+309);   //km
    static double d_mobile3_multipath2 = 0.001*(542+728+187);   //km
    static double d_mobile3_multipath3 = 0.001*(797+459+191);   //km
    static double d_mobile4_multipath1 = 0.001*(1268+379);   //km
    static double d_mobile4_multipath2 = 0.001*(614+669+362);   //km
    static double d_mobile4_multipath3 = 0.001*(872+430+362);   //km
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
        Antenna tx1 = new Antenna();
        Antenna tx2 = new Antenna();
        base_station.addAntenna(tx1);
        base_station.addAntenna(tx2);
        tx1.setPower(power_base_station / 2);
        tx2.setPower(power_base_station / 2);

        Device mobile1 = new Device("mobile1", multipaths.get(0), multipaths.get(1));
        Device mobile2 = new Device("mobile2", multipaths.get(2), multipaths.get(3));
        Device mobile3 = new Device("mobile3", multipaths.get(4), multipaths.get(5));
        Device mobile4 = new Device("mobile4", multipaths.get(6), multipaths.get(7));
        List<Device> mobiles;

        // scenariusz 1
        // stary scenariusz: 98% main, 2% side
//        MIMO2x2(base_station, mobile1, d_mobile1_multipath1, d_mobile1_multipath2, 0.98, 0.02);
//        MIMO2x2(base_station, mobile2, d_mobile2_multipath1, d_mobile2_multipath2, 0.98, 0.02);
//        MIMO2x2(base_station, mobile3, d_mobile3_multipath1, d_mobile3_multipath2, 0.98, 0.02);
//        MIMO2x2(base_station, mobile4, d_mobile4_multipath1, d_mobile4_multipath2, 0.98, 0.02);
        tx1.setPower(power_base_station / 2);
        tx2.setPower(power_base_station / 2);
        mobiles = Collections.singletonList(mobile1);
        scenario(mobiles, tx1, 8, "scenario1");
        mobiles = Collections.singletonList(mobile2);
        scenario(mobiles, tx1, 8, "scenario1");
        mobiles = Collections.singletonList(mobile3);
        scenario(mobiles, tx1, 8, "scenario1");
        mobiles = Collections.singletonList(mobile4);
        scenario(mobiles, tx1, 8, "scenario1");

        // scenariusz 2
        tx1.setPower(power_base_station / 4);
        tx2.setPower(power_base_station / 4);
        mobiles = Arrays.asList(mobile1, mobile2);
        scenario(mobiles, tx1, 8, "scenario2");
        mobiles = Arrays.asList(mobile3, mobile4);
        scenario(mobiles, tx1, 8, "scenario2");

        // scenariusz 3
        tx1.setPower(power_base_station / 8);
        tx2.setPower(power_base_station / 8);
        mobiles = Arrays.asList(mobile1, mobile2, mobile3, mobile4);
        scenario(mobiles, tx1, 8,"scenario3");
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

        List<PathLossModel> models = Arrays.asList(new Erceg(), new COST231Hata(), new Ericsson());
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
                    double arrayFactor = mwToDbm(rx.arrayFactor(N, 90 - rotateAngle, 90 - newAngle - rotateAngle));

                    model.setPathLoss(h_base_station, multipath.getLength(), h_mobile, frequency);

                    Signal sig = new Signal(multipath.getLength() / speed_wave,  tx.getPower() + arrayFactor - model.getPathLoss());
                    System.out.println(scenario + " antenna no " + (i + 1) + " multipath length: " + multipath.length
                            + " newAngle " + (90 - newAngle - rotateAngle) + " rotate_angle " + (90 - rotateAngle) + " arrayFactor: " + arrayFactor);
//                if (sig.power != 0) rx.addSignal(sig);    // pomijamy sygnał gdy poza zasięgiem anteny?
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

    public static void MIMO2x2(Device base_station, Device mobile, double d_mobile_multipath1, double d_mobile_multipath2,
                               double powerProportion_main, double powerProportion_side)
    {
        Antenna tx1 = base_station.getAntennasList().get(0);
        Antenna tx2 = base_station.getAntennasList().get(1);
        Antenna rx1 = new Antenna();
        Antenna rx2 = new Antenna();
        mobile.addAntenna(rx1);
        mobile.addAntenna(rx2);

        // Erceg
        Erceg erceg1 = new Erceg();
        Erceg erceg2 = new Erceg();
        erceg1.setPathLoss(h_base_station, d_mobile_multipath1, h_mobile, frequency);
        erceg2.setPathLoss(h_base_station, d_mobile_multipath2, h_mobile, frequency);

        Signal rx1s1 = new Signal(d_mobile_multipath1 / speed_wave, powerProportion_main * (tx1.getPower() - erceg1.getPathLoss()));
        Signal rx1s2 = new Signal(d_mobile_multipath2 / speed_wave, powerProportion_side * (tx2.getPower() - erceg2.getPathLoss()));
        rx1.addSignal(rx1s1);
        rx1.addSignal(rx1s2);

        Signal rx2s1 = new Signal(d_mobile_multipath1 / speed_wave, powerProportion_side * (tx1.getPower() - erceg1.getPathLoss()));
        Signal rx2s2 = new Signal(d_mobile_multipath2 / speed_wave, powerProportion_main * (tx2.getPower() - erceg2.getPathLoss()));
        rx2.addSignal(rx2s1);
        rx2.addSignal(rx2s2);

        // COST231Hata
        COST231Hata cost231hata1 = new COST231Hata();
        COST231Hata cost231hata2 = new COST231Hata();
        cost231hata1.setPathLoss(h_base_station, d_mobile_multipath1, h_mobile, frequency);
        cost231hata2.setPathLoss(h_base_station, d_mobile_multipath2, h_mobile, frequency);

        Signal rx1s3 = new Signal(d_mobile_multipath1 / speed_wave, powerProportion_main * (tx1.getPower() - cost231hata1.getPathLoss()));
        Signal rx1s4 = new Signal(d_mobile_multipath2 / speed_wave, powerProportion_side * (tx2.getPower() - cost231hata2.getPathLoss()));
        rx1.addSignal(rx1s3);
        rx1.addSignal(rx1s4);

        Signal rx2s3 = new Signal(d_mobile_multipath1 / speed_wave, powerProportion_side * (tx1.getPower() - cost231hata1.getPathLoss()));
        Signal rx2s4 = new Signal(d_mobile_multipath2 / speed_wave, powerProportion_main * (tx2.getPower() - cost231hata2.getPathLoss()));
        rx2.addSignal(rx2s3);
        rx2.addSignal(rx2s4);

        // Ericsson
        Ericsson ericsson1 = new Ericsson();
        Ericsson ericsson2 = new Ericsson();
        ericsson1.setPathLoss(h_base_station, d_mobile_multipath1, h_mobile, frequency);
        ericsson2.setPathLoss(h_base_station, d_mobile_multipath2, h_mobile, frequency);

        Signal rx1s5 = new Signal(d_mobile_multipath1 / speed_wave, powerProportion_main * (tx1.getPower() - ericsson1.getPathLoss()));
        Signal rx1s6 = new Signal(d_mobile_multipath2 / speed_wave, powerProportion_side * (tx2.getPower() - ericsson2.getPathLoss()));
        rx1.addSignal(rx1s5);
        rx1.addSignal(rx1s6);

        Signal rx2s5 = new Signal(d_mobile_multipath1 / speed_wave, powerProportion_side * (tx1.getPower() - ericsson1.getPathLoss()));
        Signal rx2s6 = new Signal(d_mobile_multipath2 / speed_wave, powerProportion_main * (tx2.getPower() - ericsson2.getPathLoss()));
        rx2.addSignal(rx2s5);
        rx2.addSignal(rx2s6);
    }

    public static void writeSignals(Device mobile, String fileName) throws IOException
    {
        try(FileWriter fw = new FileWriter("src/com/company/out/" + fileName);
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

    static double dbmToMw(double dBm)
    {
        return Math.pow(10, dBm / 10);
    }


//   z main
//        calculatePathLosses("Odbiornik1", d_mobile1);
//        calculatePathLosses("Odbiornik2", d_mobile2);
//        calculatePathLosses("Odbiornik3", d_mobile3);
//        calculatePathLosses("Odbiornik4", d_mobile4);
//
//        calculatePathLosses("Odbiornik1Ścieżka1", d_mobile1_multipath1);
//        calculatePathLosses("Odbiornik1Ścieżka2", d_mobile1_multipath2);
//        calculatePathLosses("Odbiornik1Ścieżka3", d_mobile1_multipath3);
//        System.out.println();
//        calculatePathLosses("Odbiornik2Ścieżka1", d_mobile2_multipath1);
//        calculatePathLosses("Odbiornik2Ścieżka2", d_mobile2_multipath2);
//        calculatePathLosses("Odbiornik2Ścieżka3", d_mobile2_multipath3);
//        System.out.println();
//        calculatePathLosses("Odbiornik3Ścieżka1", d_mobile3_multipath1);
//        calculatePathLosses("Odbiornik3Ścieżka2", d_mobile3_multipath2);
//        calculatePathLosses("Odbiornik3Ścieżka3", d_mobile3_multipath3);
//        System.out.println();
//        calculatePathLosses("Odbiornik4Ścieżka1", d_mobile4_multipath1);
//        calculatePathLosses("Odbiornik4Ścieżka2", d_mobile4_multipath2);
//        calculatePathLosses("Odbiornik4Ścieżka3", d_mobile4_multipath3);

//    static void calculatePathLosses(String path_name, double d_mobile)
//    {
//        double path_loss1_erceg = erceg(h_base_station, d_mobile, h_mobile, frequency);
//        double path_loss1_cost231hata = cost231hata(h_base_station, d_mobile, h_mobile, frequency);
//        double path_loss1_ericsson = ericsson(h_base_station, d_mobile, h_mobile, frequency);
//
//        System.out.println("Path loss Erceg: " + path_name + " " + d_mobile + " " + -path_loss1_erceg);
//        System.out.println("Path loss COST-231Hata: " + path_name + " " + d_mobile + " " + -path_loss1_cost231hata);
//        System.out.println("Path loss Ericsson: " + path_name + " " + d_mobile + " " + -path_loss1_ericsson);
//
//        double h = 1;
//        double capacity_erceg = shannonCapacity(-(power_base_station - path_loss1_erceg), h);
//        double capacity_cost231hata = shannonCapacity(-(power_base_station - path_loss1_cost231hata), h);
//        double capacity_ericsson= shannonCapacity(-(power_base_station - path_loss1_ericsson), h);
//
//        System.out.println("Capacity Erceg: " + path_name + " " + d_mobile + " " + capacity_erceg);
//        System.out.println("Capacity COST-231Hata: " + path_name + " " + d_mobile + " " + capacity_cost231hata);
//        System.out.println("Capacity Ericsson: " + path_name + " " + d_mobile + " " + capacity_ericsson);
//
//
//    }
}