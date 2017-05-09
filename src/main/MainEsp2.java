/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import edu.princeton.cs.introcs.StdDraw;
import fogtorch.application.Application;
import fogtorch.application.ExactThing;
import fogtorch.application.ThingRequirement;
import fogtorch.deployment.Deployment;
import fogtorch.deployment.Search;
import fogtorch.infrastructure.Infrastructure;
import fogtorch.utils.Couple;
import fogtorch.utils.Hardware;
import fogtorch.utils.QoS;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Random;


/**
 *
 * @author Stefano
 */
public class MainEsp2 {

    public static Random rnd = new Random();

    public static QoS samplingFunction(double probability, QoS q1, QoS q2) {

        double rand = rnd.nextDouble();
        if (probability == 1) {
            return q1;
        }
        if (rand < probability) {
            return q1;
        } else {
            return q2;
        }
    }

    public static void main(String[] args) {
        int TIMES = 100;
        HashMap<Deployment, Couple<Double, Double>> histogram = new HashMap<>();
        
        String filename = "C:\\Users\\Stefano\\Dropbox\\_Dottorato\\FogTorchMC\\FogTorchMC\\Q1test.csv";
        boolean notFog3 = false;
        //Change the access to Internet
        //String profile = "30M";
        String profile = "6M";
        //String profile = "6M";

        //String profile2 = "4G";
        String profile2 = "3G";

        boolean video = false;
        //String quality = "HD";
        String quality = "SD";

        int Bstorage = 8; //8, 10, 32, 1000

        boolean dell32 = true;

        boolean fog2up = true;

        Hardware fogHW;
        if (dell32) {
            //cores, ram, storage
            fogHW = new Hardware(2, 2, 32);
        } else {
            fogHW = new Hardware(2, 2, 1000);
        }

        for (int i = 0; i < TIMES; i++) {

            Infrastructure I = new Infrastructure();
            //fog1 43.7464449,10.4615923 fog2 43.7381285,10.4552213
            I.addCloudDatacentre("cloud1", asList("linux", "php", "mySQL", "python"), 52.195097, 3.0364791);
            I.addCloudDatacentre("cloud2", asList("linux", "php", "mySQL", "java"), 44.123896, -122.781555);
            //Fog nodes
            I.addFogNode("fog1", asList("linux", "php", "mySQL"), fogHW, 43.740186, 10.364619);
            if (fog2up) {
                I.addFogNode("fog2", asList("linux", "php"), fogHW, 43.7464449, 10.4615923);
            }
            I.addFogNode("fog3", asList("linux", "mySQL"), fogHW, 43.7381285, 10.4552213);

            //Links
            if (profile.equals("30M")) {
                QoS fogtoCloudDownload = samplingFunction(1, new QoS(80, 21), null);
                QoS fogtoCloudUpload = samplingFunction(1, new QoS(80, 1.2), null);
                //fog1
                I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                //fog3
                //I.addLink("fog3", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                //I.addLink("fog3", "cloud2", fogtoCloudDownload, fogtoCloudUpload);           

            } else if (profile.equals("20M")) {
                QoS fogtoCloudDownload = samplingFunction(0.95, new QoS(40, 16.022), new QoS(40, 10.2));
                QoS fogtoCloudUpload = samplingFunction(0.95, new QoS(40, 0.725), new QoS(40, 0.5));
                //fog1
                I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                //fog3
                //I.addLink("fog3", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                //I.addLink("fog3", "cloud2", fogtoCloudDownload, fogtoCloudUpload); 
            } else if (profile.equals("6M")) {
                QoS fogtoCloudDownload = samplingFunction(0.95, new QoS(40, 5.126), new QoS(40, 3.1));
                QoS fogtoCloudUpload = samplingFunction(0.95, new QoS(40, 0.725), new QoS(40, 0.5));
                //fog1
                I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                //fog3
                //I.addLink("fog3", "cloud1",fogtoCloudDownload, fogtoCloudUpload);
                //I.addLink("fog3", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
            }

            QoS fogtoCloudDownloadVoda = samplingFunction(1, new QoS(80, 21), null);
            QoS fogtoCloudUploadVoda = samplingFunction(1, new QoS(80, 1.2), null);

            I.addLink("fog3", "cloud1", fogtoCloudDownloadVoda, fogtoCloudUploadVoda);
            I.addLink("fog3", "cloud2", fogtoCloudDownloadVoda, fogtoCloudUploadVoda);

            if (profile2.equals("3G")) {
                QoS fogtoCloudDownload = samplingFunction(0.9957, new QoS(54, 9.61), new QoS(Integer.MAX_VALUE, 0.0));
                QoS fogtoCloudUpload = samplingFunction(0.9959, new QoS(54, 2.89), new QoS(Integer.MAX_VALUE, 0.0));
                if (fog2up) {
                    I.addLink("fog2", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                    I.addLink("fog2", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                }
            } else if (profile2.equals("4G")) {
                QoS fogtoCloudDownload = samplingFunction(0.9926, new QoS(53, 22.67), new QoS(Integer.MAX_VALUE, 0.0));
                QoS fogtoCloudUpload = samplingFunction(0.9937, new QoS(53, 16.97), new QoS(Integer.MAX_VALUE, 0.0));
                if (fog2up) {
                    I.addLink("fog2", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                    I.addLink("fog2", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                }
            }

            //WLAN  
            if (fog2up) {
                I.addLink("fog1", "fog2", samplingFunction(0.9, new QoS(15, 32), new QoS(15, 4)));
                I.addLink("fog2", "fog3", samplingFunction(0.76, new QoS(15, 18), new QoS(15, 5.5)));
            }
            I.addLink("fog1", "fog3", samplingFunction(0.64, new QoS(25, 14), new QoS(25, 3)));

            //inter-cloud
            I.addLink("cloud1", "cloud2", samplingFunction(1, new QoS(5, 1000), null));

            //Things for local
            I.addThing("water0", "water", 43.7464449, 10.4615923, "fog1");
            I.addThing("video0", "video", 43.7464449, 10.4615923, "fog1");
            I.addThing("moisture0", "moisture", 43.7464449, 10.4615923, "fog1");
            I.addThing("temperature0", "temperature", 43.7464449, 10.4615923, "fog3");

            // System.out.println(I.L);
            Application A = new Application();
            ArrayList<ThingRequirement> neededThings = new ArrayList<>();
            //QoSProfile qNodeThing, QoSProfile qThingNode
            neededThings.add(new ExactThing("water0", new QoS(1000, 0.1), new QoS(1000,0.1))); // 1 s and 1 Mbps
            if (video) {
                neededThings.add(new ExactThing("video0", new QoS(25,0.1), new QoS(25, 8))); // 25 ms and 4Mbps for the HD videostreaming
            }
            neededThings.add(new ExactThing("moisture0", new QoS(500,0.1), new QoS(500, 0.1))); // 0.5 s and 1 Mbps
            neededThings.add(new ExactThing("temperature0", new QoS(110,0.1), new QoS(110, 0.1))); // 110 ms and 1 Mbps

            //components
            A.addComponent("A", asList("linux"), new Hardware(1, 1, 8), neededThings);

            A.addComponent("B", asList("linux", "mySQL"), new Hardware(1, 1, Bstorage)); //cores ram storage

            A.addComponent("C", asList("linux", "php"), new Hardware(1, 1, 8));

            if (quality.equals("HD")) {
                A.addLink("A", "B", 160, 0.7, 18); //160 ms and 10Mbps down and 1 Mbps up
            } else if (quality.equals("SD")) {
                A.addLink("A", "B", 160, 0.5, 10);
            }

            A.addLink("A", "C", 140, 1, 3);
            A.addLink("B", "C", 100, 0.6, 4);

            Search s = new Search(A, I); //new Coordinates(43.740186, 10.364619));
            //s.addBusinessPolicies("C", asList("cloud2", "cloud1"));
            if(notFog3){
                s.addBusinessPolicies("A", asList("cloud2", "cloud1", "fog1", "fog2"));
                s.addBusinessPolicies("B", asList("cloud2", "cloud1", "fog1", "fog2"));
                s.addBusinessPolicies("C", asList("cloud2", "cloud1", "fog1", "fog2"));
            }
            s.findDeployments(true);
            double pos = s.D.size();
            double size = s.D.size();
            for (Deployment d : s.D) {
                if (histogram.containsKey(d)) {
                    Double newCount = histogram.get(d).getA() + 1.0; //montecarlo frequency
                    Double newPos = histogram.get(d).getB() + (pos/size);
                    //System.out.println(newPos + " " + pos/size);
                    histogram.replace(d, new Couple(newCount, newPos));
                } else {
                    histogram.put(d, new Couple(1.0, pos/size));
                }
                pos--;
            }
        }

        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println("Deployment, MonteCarlo %, Heuristic Rank");
            for (Deployment dep : histogram.keySet()) {
                Couple point = new Couple((100 * histogram.get(dep).getA() / ((double) TIMES)), (100*histogram.get(dep).getB() / (double) TIMES));
                histogram.replace(dep, point );
                writer.println(dep + ", " + histogram.get(dep));
                //System.out.println(dep + ", " + histogram.get(dep));
                StdDraw.setPenRadius(10.0);
                StdDraw.point((Double) point.getA(),(Double) point.getB());
                StdDraw.show();
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }

    }
}
