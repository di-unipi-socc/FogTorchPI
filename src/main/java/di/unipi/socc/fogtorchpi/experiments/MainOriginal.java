/**
 * Original file of paper revised according to FT refactoring with cost.
 */
package di.unipi.socc.fogtorchpi.experiments;

import di.unipi.socc.fogtorchpi.application.Application;
import di.unipi.socc.fogtorchpi.application.ExactThing;
import di.unipi.socc.fogtorchpi.application.ThingRequirement;
import di.unipi.socc.fogtorchpi.deployment.Deployment;
import di.unipi.socc.fogtorchpi.deployment.Search;
import di.unipi.socc.fogtorchpi.infrastructure.Infrastructure;
import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;
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
public class MainOriginal {

    public static Random rnd = new Random();

    public static QoSProfile samplingFunction(double probability, QoSProfile q1, QoSProfile q2) {

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
        int TIMES = 100000;
        HashMap<Deployment, Couple<Double, Double>> histogram = new HashMap<>();
        
        String filename =
                "C:\\Users\\Stefano\\Dropbox\\dddd.csv";
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

        for (int i = 0; i < TIMES; i++) {

            Infrastructure I = new Infrastructure();
            //fog1 43.7464449,10.4615923 fog2 43.7381285,10.4552213
            //double coreCost, double ramCost, double storageCost
            I.addCloudDatacentre("cloud1", asList(
                    new Couple("linux", 0.0), 
                    new Couple("php", 0.0), 
                    new Couple("mySQL", 45.0), 
                    new Couple("python", 0.0)), 
                    52.195097, 3.0364791,
                    new Hardware(0,0,0, 4.0, 9.0, 2.0)
            );
            I.addCloudDatacentre("cloud2", asList(
                     new Couple("linux", 45.0),
                     new Couple("php", 0.0),
                     new Couple("mySQL", 60.0),
                     new Couple("java", 0.0)),
                    44.123896, -122.781555,
                    new Hardware(0,0,0, 4.0, 8.0, 1.0),
                    asList(
                            new Couple("tiny", 7.0),
                            new Couple("small", 10.0),
                            new Couple("medium", 15.0),
                            new Couple("large", 20.0),
                            new Couple("xlarge", 25.0)
                    )
                    );
            //Fog nodes
            I.addFogNode("fog1", asList(
                    new Couple("linux", 0.0), 
                    new Couple("php", 0.0), 
                    new Couple("mySQL", 15.0)), 
                    new Hardware(2, 2, 32, 4.0, 5.0, 3.0), 
                    43.740186, 10.364619);
            if (fog2up) {
                I.addFogNode("fog2", asList(
                    new Couple("linux", 0.0), 
                    new Couple("php", 0.0)), 
                        new Hardware(2, 2, 32, 0.0, 0.0, 0.0), //owned by us 0 $
                        43.7464449, 10.4615923);
            }
            
            I.addFogNode("fog3", asList(
                    new Couple("linux", 0.0), 
                    new Couple("mySQL", 0.0)), 
                    new Hardware(4, 2, 64, 5.0, 6.0, 2.0), 
                    43.7381285, 10.4552213);

            //Links
            if (profile.equals("30M")) {
                QoSProfile fogtoCloudDownload = samplingFunction(1, new QoSProfile(80, 21), null);
                QoSProfile fogtoCloudUpload = samplingFunction(1, new QoSProfile(80, 1.2), null);
                //fog1
                I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                //fog3
                //I.addLink("fog3", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                //I.addLink("fog3", "cloud2", fogtoCloudDownload, fogtoCloudUpload);           

            } else if (profile.equals("20M")) {
                QoSProfile fogtoCloudDownload = samplingFunction(0.98, new QoSProfile(40, 10.5), new QoSProfile(Integer.MAX_VALUE, 0.0));
                QoSProfile fogtoCloudUpload = samplingFunction(0.98, new QoSProfile(40, 4.5), new QoSProfile(Integer.MAX_VALUE, 0.0));
                //fog1
                I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                //fog3
                //I.addLink("fog3", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                //I.addLink("fog3", "cloud2", fogtoCloudDownload, fogtoCloudUpload); 
            } else if (profile.equals("6M")) {
                QoSProfile fogtoCloudDownload = samplingFunction(0.98, new QoSProfile(70, 6), new QoSProfile(Integer.MAX_VALUE, 0.0));
                QoSProfile fogtoCloudUpload = samplingFunction(0.98, new QoSProfile(70, 0.75), new QoSProfile(Integer.MAX_VALUE, 0.0));
                //fog1
                I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                //fog3
                //I.addLink("fog3", "cloud1",fogtoCloudDownload, fogtoCloudUpload);
                //I.addLink("fog3", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
            }

            QoSProfile fogtoCloudDownloadVoda = samplingFunction(1, new QoSProfile(80, 21), null);
            QoSProfile fogtoCloudUploadVoda = samplingFunction(1, new QoSProfile(80, 1.2), null);

            I.addLink("fog3", "cloud1", fogtoCloudDownloadVoda, fogtoCloudUploadVoda);
            I.addLink("fog3", "cloud2", fogtoCloudDownloadVoda, fogtoCloudUploadVoda);

            if (profile2.equals("3G")) {
                QoSProfile fogtoCloudDownload = samplingFunction(0.9957, new QoSProfile(54, 9.61), new QoSProfile(Integer.MAX_VALUE, 0.0));
                QoSProfile fogtoCloudUpload = samplingFunction(0.9959, new QoSProfile(54, 2.89), new QoSProfile(Integer.MAX_VALUE, 0.0));
                if (fog2up) {
                    I.addLink("fog2", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                    I.addLink("fog2", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                }
            } else if (profile2.equals("4G")) {
                QoSProfile fogtoCloudDownload = samplingFunction(0.9926, new QoSProfile(53, 22.67), new QoSProfile(Integer.MAX_VALUE, 0.0));
                QoSProfile fogtoCloudUpload = samplingFunction(0.9937, new QoSProfile(53, 16.97), new QoSProfile(Integer.MAX_VALUE, 0.0));
                if (fog2up) {
                    I.addLink("fog2", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
                    I.addLink("fog2", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
                }
            }

            //WLAN  
            if (fog2up) {
                I.addLink("fog1", "fog2", samplingFunction(0.9, new QoSProfile(15, 32), new QoSProfile(15, 4)));
                I.addLink("fog2", "fog3", samplingFunction(0.76, new QoSProfile(15, 18), new QoSProfile(15, 5.5)));
            }
            I.addLink("fog1", "fog3", samplingFunction(0.64, new QoSProfile(25, 14), new QoSProfile(25, 3)));

            //inter-cloud
            I.addLink("cloud1", "cloud2", samplingFunction(1, new QoSProfile(5, 1000), null));

            //Things for local
            I.addThing("water0", "water", 43.7464449, 10.4615923, "fog1", 3.50);
            I.addThing("video0", "video", 43.7464449, 10.4615923, "fog1", 30.0);
            I.addThing("moisture0", "moisture", 43.7464449, 10.4615923, "fog1", 0.01 );
            I.addThing("temperature0", "temperature", 43.7464449, 10.4615923, "fog3", 0.001);

            // System.out.println(I.L);
            Application A = new Application();
            ArrayList<ThingRequirement> neededThings = new ArrayList<>();
            //QoSProfile qNodeThing, QoSProfile qThingNode
            neededThings.add(new ExactThing("water0", new QoSProfile(1000, 0.1), new QoSProfile(1000,0.1), 60)); // 1 s and 1 Mbps
            if (video) {
                neededThings.add(new ExactThing("video0", new QoSProfile(25,0.1), new QoSProfile(25, 5), 1)); // 25 ms and 4Mbps for the HD videostreaming
            }
            neededThings.add(new ExactThing("moisture0", new QoSProfile(500,0.1), new QoSProfile(500, 0.1), 1440)); // 0.5 s and 1 Mbps
            neededThings.add(new ExactThing("temperature0", new QoSProfile(65,0.1), new QoSProfile(65, 0.1), 43200)); // 110 ms and 1 Mbps

            //components
            A.addComponent("A", asList("linux"), new Hardware(1, 1.2, 8), neededThings);
            //A.addComponent("A", asList("linux"), new Hardware("tiny", 0.0), neededThings);
            
            A.addComponent("B", asList("linux", "mySQL"), new Hardware(1, Bram, Bstorage)); //cores ram storage
            //A.addComponent("B", asList("linux", "mySQL"), new Hardware("small", 0.0));
            
            A.addComponent("C", asList("linux", "php"), new Hardware(2, 0.7, 4));

            if (quality.equals("HD")) {
                A.addLink("A", "B", 160, 0.5, 3.5); //160 ms and 10Mbps down and 1 Mbps up
            } else if (quality.equals("SD")) {
                A.addLink("A", "B", 160, 0.5, 0.7);
            }

            A.addLink("A", "C", 140, 0.4, 1.1);
            A.addLink("B", "C", 100, 0.8, 1);

            Search s = new Search(A, I); //new Coordinates(43.740186, 10.364619));
            
            //s.addBusinessPolicies("C", asList("cloud2", "cloud1"));
            //s.addKeepLightNodes(asList("fog3"));
            
            if(notFog3){
                s.addBusinessPolicies("A", asList("cloud2", "cloud1", "fog1", "fog2"));
                s.addBusinessPolicies("B", asList("cloud2", "cloud1", "fog1", "fog2"));
                s.addBusinessPolicies("C", asList("cloud2", "cloud1", "fog1", "fog2"));
            }
            s.findDeployments(true);
            double pos = s.D.size();
            double size = s.D.size();
            
            for (Deployment d : s.D) {
                d.consumedResources = I.consumedResources(d); //asList("fog1", "fog2")); //computes over all fog nodes ...
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
            
            //System.out.println(I);
        }

        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println("Deployment ; QoS-assurance; Hardware %;Cost");
            System.out.println("Deployment ; QoS-assurance ; Hardware %;Cost");
            for (Deployment dep : histogram.keySet()) {
                histogram.replace(dep,  new Couple((100 * histogram.get(dep).getA() / ((double) TIMES)), (100*histogram.get(dep).getB() / (double) TIMES)));
                writer.println(dep + "; " + histogram.get(dep).getA() + ";"  + 100*(dep.consumedResources.getA() + dep.consumedResources.getB())/2 + "; " + dep.deploymentMonthlyCost );
                System.out.println(dep + "; " + histogram.get(dep).getA() + "; "  + 100*(dep.consumedResources.getA() + dep.consumedResources.getB())/2 + "; " + dep.deploymentMonthlyCost);
                
            }
            writer.close();
        } catch (IOException e) {
        }
        
        

    }
}
