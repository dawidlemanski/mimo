package com.company;

import java.io.*;
import java.util.Random;

public class Main
{
    public static double shannonCapacity(double E, double h)
    {
        double capacity;
        double omega = 1;
        capacity = Math.log10(1 + E / Math.pow(omega, 2) * Math.pow(h, 2));

        return capacity;
    }

    static double h_base_station = 70;          // m
    static double frequency = 2000;             // MHz
    static double power_base_station = 45;       // dBm
    static double h_mobile = 5;                 // m
    static double speed_wave = 2.997 * Math.pow(10,5); // km/s
    public static void main(String[] args) throws IOException
    {
        double d_mobile1 = 1.091;   //km
        double d_mobile2 = 1.377;
        double d_mobile3 = 1.441;
        double d_mobile4 = 1.623;

        double d_mobile1_multipath1 = 0.001*(303+722+109);  //km
        double d_mobile1_multipath2 = 0.001*(613+642);
        double d_mobile1_multipath3 = 0.001*(687+415);

        double d_mobile2_multipath1 = 0.001*(301+577+147+373);
        double d_mobile2_multipath2 = 0.001*(383+566+324+144);
        double d_mobile2_multipath3 = 0.001*(824+323+301);

        double d_mobile3_multipath1 = 0.001*(726+592+309);
        double d_mobile3_multipath2 = 0.001*(542+728+187);
        double d_mobile3_multipath3 = 0.001*(797+459+191);

        double d_mobile4_multipath1 = 0.001*(1268+379);
        double d_mobile4_multipath2 = 0.001*(614+669+362);
        double d_mobile4_multipath3 = 0.001*(872+430+362);

        Device base_station = new Device();
        Antenna tx1 = new Antenna();
        Antenna tx2 = new Antenna();
        base_station.addAntenna(tx1);
        base_station.addAntenna(tx2);
        tx1.setPower(power_base_station/2);
        tx2.setPower(power_base_station/2);

        Device mobile1 = new Device();
        Device mobile2 = new Device();
        Device mobile3 = new Device();
        Device mobile4 = new Device();

        MIMO2x2(base_station, mobile1, d_mobile1_multipath1, d_mobile1_multipath2);
        MIMO2x2(base_station, mobile2, d_mobile2_multipath1, d_mobile2_multipath2);
        MIMO2x2(base_station, mobile3, d_mobile3_multipath1, d_mobile3_multipath2);
        MIMO2x2(base_station, mobile4, d_mobile4_multipath1, d_mobile4_multipath2);

        writeSignals(mobile1,"mimo2x2\\mobile1.csv");
        writeSignals(mobile2,"mimo2x2\\mobile2.csv");
        writeSignals(mobile3,"mimo2x2\\mobile3.csv");
        writeSignals(mobile4,"mimo2x2\\mobile4.csv");
    }

    public static void MIMO2x2(Device base_station, Device mobile, double d_mobile_multipath1, double d_mobile_multipath2)
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

        Signal rx1s1 = new Signal(d_mobile_multipath1 / speed_wave, 0.98 * (tx1.getPower() - erceg1.getPathLoss()));
        Signal rx1s2 = new Signal(d_mobile_multipath2 / speed_wave, 0.02 * (tx2.getPower() - erceg2.getPathLoss()));
        rx1.addSignal(rx1s1);
        rx1.addSignal(rx1s2);

        Signal rx2s1 = new Signal(d_mobile_multipath1 / speed_wave, 0.02 * (tx1.getPower() - erceg1.getPathLoss()));
        Signal rx2s2 = new Signal(d_mobile_multipath2 / speed_wave, 0.98 * (tx2.getPower() - erceg2.getPathLoss()));
        rx2.addSignal(rx2s1);
        rx2.addSignal(rx2s2);

        // COST231Hata
        COST231Hata cost231hata1 = new COST231Hata();
        COST231Hata cost231hata2 = new COST231Hata();
        cost231hata1.setPathLoss(h_base_station, d_mobile_multipath1, h_mobile, frequency);
        cost231hata2.setPathLoss(h_base_station, d_mobile_multipath2, h_mobile, frequency);

        Signal rx1s3 = new Signal(d_mobile_multipath1 / speed_wave, 0.98 * (tx1.getPower() - cost231hata1.getPathLoss()));
        Signal rx1s4 = new Signal(d_mobile_multipath2 / speed_wave, 0.02 * (tx2.getPower() - cost231hata2.getPathLoss()));
        rx1.addSignal(rx1s3);
        rx1.addSignal(rx1s4);

        Signal rx2s3 = new Signal(d_mobile_multipath1 / speed_wave, 0.02 * (tx1.getPower() - cost231hata1.getPathLoss()));
        Signal rx2s4 = new Signal(d_mobile_multipath2 / speed_wave, 0.98 * (tx2.getPower() - cost231hata2.getPathLoss()));
        rx2.addSignal(rx2s3);
        rx2.addSignal(rx2s4);

        // Ericsson
        Ericsson ericsson1 = new Ericsson();
        Ericsson ericsson2 = new Ericsson();
        ericsson1.setPathLoss(h_base_station, d_mobile_multipath1, h_mobile, frequency);
        ericsson2.setPathLoss(h_base_station, d_mobile_multipath2, h_mobile, frequency);

        Signal rx1s5 = new Signal(d_mobile_multipath1 / speed_wave, 0.98 * (tx1.getPower() - ericsson1.getPathLoss()));
        Signal rx1s6 = new Signal(d_mobile_multipath2 / speed_wave, 0.02 * (tx2.getPower() - ericsson2.getPathLoss()));
        rx1.addSignal(rx1s5);
        rx1.addSignal(rx1s6);

        Signal rx2s5 = new Signal(d_mobile_multipath1 / speed_wave, 0.02 * (tx1.getPower() - ericsson1.getPathLoss()));
        Signal rx2s6 = new Signal(d_mobile_multipath2 / speed_wave, 0.98 * (tx2.getPower() - ericsson2.getPathLoss()));
        rx2.addSignal(rx2s5);
        rx2.addSignal(rx2s6);
    }

    public static void writeSignals(Device mobile, String fileName) throws IOException
    {
        try(FileWriter fw = new FileWriter("src/com/company/out/" + fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            Antenna rx1 = mobile.getAntennasList().get(0);
            Antenna rx2 = mobile.getAntennasList().get(1);

            out.println("Antenna 1");
            for (int i = 0; i < rx1.getSignalList().size(); i++)
            {
                out.print(rx1.getSignalList().get(i).getTime() * Math.pow(10,6) + " ");  // mikro s
                out.println(dbmToMw(rx1.getSignalList().get(i).getPower()));    //mW
            }

            out.println("Antenna 2");
            for (int i = 0; i < rx2.getSignalList().size(); i++)
            {
                out.print(rx2.getSignalList().get(i).getTime() * Math.pow(10,6) + " ");
                out.println(dbmToMw(rx2.getSignalList().get(i).getPower()));
            }
        }
    }

    public static double mwToDbm(double mW)
    {
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