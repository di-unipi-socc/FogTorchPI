/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import fogtorch.application.Application;
import fogtorch.application.ExactThing;
import fogtorch.application.ThingRequirement;
import fogtorch.deployment.Deployment;
import fogtorch.infrastructure.Infrastructure;
import fogtorch.deployment.MonteCarloSearch;
import fogtorch.utils.Couple;
import fogtorch.utils.Hardware;
import fogtorch.utils.QoS;
import fogtorch.utils.QoSProfile;
import fogtorch.utils.Software;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;

/**
 *
 * @author Stefano
 */
public class MainRefactoring {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean notFog3 = false;

        //Change the access to Internet
        String profile = "6M";

        //String profile2 = "4G";
        String profile2 = "3G";

        boolean video = false;
        //String quality = "HD";
        String quality = "SD";

        int Bstorage = 8; //8, 10, 32, 1000
        double Bram = 1;

        boolean dell32 = true;

        boolean fog2up = true;

        Hardware fogHW;
        if (dell32) {
            //cores, ram, storage
            fogHW = new Hardware(2, 2, 32);
        } else {
            fogHW = new Hardware(2, 2, 1000);
        }

        Infrastructure I = new Infrastructure();
        Hardware h = new Hardware(2, 2, 1000, 1.0, 1.0, 1.0);

        I.addCloudDatacentre("cloud1", asList(new Couple("linux", 0.0), new Couple("php", 0.0), new Couple("mySQL", 100.0),new Couple("python", 100.0)), 52.195097, 3.0364791, h);
        I.addCloudDatacentre("cloud2", asList(new Couple("linux", 0.0), new Couple("php", 0.0), new Couple("mySQL", 100.0), new Couple("java", 0.0)), 44.123896, -122.781555, h);

        I.addFogNode("fog1", asList(new Couple("linux", 0.0), new Couple("php", 0.0), new Couple("mySQL", 100.0)), fogHW, 43.740186, 10.364619);
        I.addFogNode("fog2", asList(new Couple("linux", 0.0), new Couple("php", 0.0)), fogHW, 43.7464449, 10.4615923);
        I.addFogNode("fog3", asList(new Couple("linux", 0.0), new Couple("mySQL", 100.0)), new Hardware(4, 2, 64), 43.7381285, 10.4552213);

        //Links
        if (profile.equals("30M")) {
            QoSProfile fogtoCloudDownload = new QoSProfile(80, 21);
            QoSProfile fogtoCloudUpload = new QoSProfile(80, 1.2);
            //fog1
            I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
            I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);

        } else if (profile.equals("20M")) {
            QoSProfile fogtoCloudDownload = new QoSProfile(asList(
                    new Couple(new QoS(40, 10.5), 0.98),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02)));  //samplingFunction(0.98, new QoSProfile(40, 10.5), new QoSProfile(Integer.MAX_VALUE, 0.0));
            QoSProfile fogtoCloudUpload = new QoSProfile(asList(
                    new Couple(new QoS(40, 4.5), 0.98),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02)));
            I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
            I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);

        } else if (profile.equals("6M")) {
            QoSProfile fogtoCloudDownload = new QoSProfile(asList(
                    new Couple(new QoS(70, 6), 0.98),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02)));
            QoSProfile fogtoCloudUpload = new QoSProfile(asList(
                    new Couple(new QoS(40, 0.75), 0.98),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02)));
            //fog1
            I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
            I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);

        }

        QoSProfile fogtoCloudDownloadVoda = new QoSProfile(80, 21);
        QoSProfile fogtoCloudUploadVoda = new QoSProfile(80, 1.2);

        I.addLink("fog3", "cloud1", fogtoCloudDownloadVoda, fogtoCloudUploadVoda);
        I.addLink("fog3", "cloud2", fogtoCloudDownloadVoda, fogtoCloudUploadVoda);

        if (profile2.equals("3G")) {
            QoSProfile fogtoCloudDownload = new QoSProfile(asList(
                    new Couple(new QoS(54, 9.61), 0.9957),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0043)));
            QoSProfile fogtoCloudUpload = new QoSProfile(asList(
                    new Couple(new QoS(54, 2.89), 0.9959),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0041)));

            I.addLink("fog2", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
            I.addLink("fog2", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
        } else if (profile2.equals("4G")) {
            QoSProfile fogtoCloudDownload = new QoSProfile(asList(
                    new Couple(new QoS(53, 22.67), 0.9926),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0074)));

            QoSProfile fogtoCloudUpload = new QoSProfile(asList(
                    new Couple(new QoS(53, 22.67), 0.9937),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0063)));
            I.addLink("fog2", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
            I.addLink("fog2", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
        }

        //WLAN
        I.addLink("fog1", "fog2", new QoSProfile(asList(
                new Couple(new QoS(15, 32), 0.9),
                new Couple(new QoS(15, 4), 0.1))));

        I.addLink("fog2", "fog3", new QoSProfile(asList(
                new Couple(new QoS(15, 18), 0.76),
                new Couple(new QoS(15, 5.5), 0.24))));

        I.addLink("fog1", "fog3", new QoSProfile(asList(
                new Couple(new QoS(25, 14), 0.64),
                new Couple(new QoS(25, 3), 0.36))));

        //inter-cloud
        I.addLink("cloud1", "cloud2", new QoSProfile(5, 1000));

        //Things 
        I.addThing("water0", "water", 43.7464449, 10.4615923, "fog1", 0.05);
        I.addThing("video0", "video", 43.7464449, 10.4615923, "fog1", 0.03);
        I.addThing("moisture0", "moisture", 43.7464449, 10.4615923, "fog1", 0.01);
        I.addThing("temperature0", "temperature", 43.7464449, 10.4615923, "fog3", 0.01);

        Application A = new Application();
        ArrayList<ThingRequirement> neededThings = new ArrayList<>();
        //QoSProfile qNodeThing, QoSProfile qThingNode
        neededThings.add(new ExactThing("water0", new QoSProfile(1000, 0.1), new QoSProfile(1000, 0.1), 500)); // 1 s and 1 Mbps
        if (video) {
            neededThings.add(new ExactThing("video0", new QoSProfile(25, 0.1), new QoSProfile(25, 5), 32)); // 25 ms and 4Mbps for the HD videostreaming
        }
        neededThings.add(new ExactThing("moisture0", new QoSProfile(500, 0.1), new QoSProfile(500, 0.1), 150)); // 0.5 s and 1 Mbps
        neededThings.add(new ExactThing("temperature0", new QoSProfile(65, 0.1), new QoSProfile(65, 0.1), 1)); // 110 ms and 1 Mbps

        //components
        
        A.addComponent("A", asList("linux"), new Hardware(1, 1.2, 8), neededThings);

        A.addComponent("B", asList("linux", "mySQL"), new Hardware(1, Bram, Bstorage)); //cores ram storage

        A.addComponent("C", asList("linux", "php"), new Hardware(2, 0.7, 4));

        if (quality.equals("HD")) {
            A.addLink("A", "B", 160, 0.5, 3.5); //160 ms and 10Mbps down and 1 Mbps up
        } else if (quality.equals("SD")) {
            A.addLink("A", "B", 160, 0.5, 0.7);
        }

        A.addLink("A", "C", 140, 0.4, 1.1);
        A.addLink("B", "C", 100, 0.8, 1);
        
        MonteCarloSearch s = new MonteCarloSearch(1, A, I); //new Coordinates(43.740186, 10.364619));
        
        if(notFog3){
            s.addBusinessPolicies("A", asList("cloud2", "cloud1", "fog1", "fog2"));
            s.addBusinessPolicies("B", asList("cloud2", "cloud1", "fog1", "fog2"));
            s.addBusinessPolicies("C", asList("cloud2", "cloud1", "fog1", "fog2"));
        }
        
        HashMap<Deployment, Couple<Double, Double>> histogram = s.startSimulation(asList());
        System.out.println(histogram);

    }
}
