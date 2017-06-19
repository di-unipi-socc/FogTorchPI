/**
 * Original file of paper revised according to FT refactoring with cost.
 */
package di.unipi.socc.fogtorchpi.experiments;

import di.unipi.socc.fogtorchpi.examples.SmallApp;
import di.unipi.socc.fogtorchpi.application.Application;
import di.unipi.socc.fogtorchpi.application.ExactThing;
import di.unipi.socc.fogtorchpi.application.SoftwareComponent;
import di.unipi.socc.fogtorchpi.application.ThingRequirement;
import di.unipi.socc.fogtorchpi.deployment.Deployment;
import di.unipi.socc.fogtorchpi.deployment.MonteCarloSearch;
import di.unipi.socc.fogtorchpi.infrastructure.Infrastructure;
import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoS;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Stefano
 */
public class MainOriginal_Links_MSSearch {

    public static void main(String[] args) {
        HashMap<Deployment, Couple<Double, Double>> histogram = new HashMap<>();


        //Change the access to Internet
        String profile = "20M";

        //String profile2 = "4G";
        String profile2 = "4G";


        Infrastructure I = new Infrastructure();
        //fog1 43.7464449,10.4615923 fog2 43.7381285,10.4552213
        //double coreCost, double ramCost, double storageCost
        I.addCloudDatacentre("cloud1", asList(
                new Couple("linux", 0.0),
                new Couple("php", 0.0),
                new Couple("mySQL", 45.0),
                new Couple("python", 0.0)),
                52.195097, 3.0364791,
                new Hardware(0, 0, 0, 2.0, 3.0, 1.0)
        );
        I.addCloudDatacentre("cloud2", asList(
                new Couple("linux", 50.0),
                new Couple("php", 0.0),
                new Couple("mySQL", 60.0),
                new Couple("java", 0.0)),
                44.123896, -122.781555,
                new Hardware(0, 0, 0, 4.0, 6.0, 1.0),
                asList(
                        new Couple("tiny", 7.0),
                        new Couple("small", 25.0),
                        new Couple("medium", 50.0),
                        new Couple("large", 100.0),
                        new Couple("xlarge", 200.0)
                )
        );
        //Fog nodes
        I.addFogNode("fog1", asList(
                new Couple("linux", 0.0),
                new Couple("php", 0.0),
                new Couple("mySQL", 15.0)),
                new Hardware(2, 4, 32, 4.0, 5.0, 3.0),
                43.740186, 10.364619);

        I.addFogNode("fog2", asList(
                new Couple("linux", 0.0),
                new Couple("php", 0.0)),
                new Hardware(2, 2, 32, 0.0, 0.0, 0.0), //owned by us 0 $
                43.7464449, 10.4615923);


        I.addFogNode("fog3", asList(
                new Couple("linux", 0.0),
                new Couple("mySQL", 0.0)),
                new Hardware(4, 12, 128, 5.0, 6.0, 2.0),
                43.7381285, 10.4552213);

        //Links
        if (profile.equals("20M")) {
            //fog1
            I.addLink("fog1", "cloud1", new QoSProfile(asList(
                    //download
                    new Couple(new QoS(40, 10.5 / 2.0), 0.98),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))),
                    //upload
                    new QoSProfile(asList(
                            new Couple(new QoS(40, 4.5 / 2.0), 0.98),
                            new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))));
            I.addLink("fog1", "cloud2", new QoSProfile(asList(
                    //download
                    new Couple(new QoS(40, 10.5 / 2.0), 0.98),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))),
                    //upload
                    new QoSProfile(asList(
                            new Couple(new QoS(40, 4.5 / 2.0), 0.98),
                            new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))));

//                I.addLink("fog1", "fog3", new QoSProfile(asList(
//                        //download
//                        new Couple(new QoS(40, 10.5/2.0), 0.98),
//                        new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))),
//                        //upload
//                        new QoSProfile(asList(
//                        new Couple(new QoS(40, 4.5/2.0), 0.98),
//                        new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))));
        } else if (profile.equals("6M")) {
            //fog1
            I.addLink("fog1", "cloud1", new QoSProfile(asList(
                    //download
                    new Couple(new QoS(70, 3), 0.98),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))),
                    //upload
                    new QoSProfile(asList(
                            new Couple(new QoS(70, 0.375), 0.98),
                            new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))));
            I.addLink("fog1", "cloud2", new QoSProfile(asList(
                    //download
                    new Couple(new QoS(70, 3), 0.98),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))),
                    //upload
                    new QoSProfile(asList(
                            new Couple(new QoS(70, 0.375), 0.98),
                            new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))));
                
//                I.addLink("fog1", "fog3", new QoSProfile(asList(
//                        //download
//                        new Couple(new QoS(70, 3), 0.98),
//                        new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))),
//                        //upload
//                        new QoSProfile(asList(
//                        new Couple(new QoS(70, 0.375), 0.98),
//                        new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.02))));

        }

        I.addLink("fog3", "cloud1", new QoSProfile(60, 60 / 2.0), new QoSProfile(80, 6 / 2.0));
        I.addLink("fog3", "cloud2", new QoSProfile(80, 60 / 2.0), new QoSProfile(80, 6 / 2.0));

        if (profile2.equals("3G")) {

            I.addLink("fog2", "cloud1", new QoSProfile(asList(
                    new Couple(new QoS(54, 9.61 / 2.0), 0.9957),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0043))),
                    new QoSProfile(asList(
                            new Couple(new QoS(54, 2.89 / 2.0), 0.9959),
                            new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0041))));
            I.addLink("fog2", "cloud2", new QoSProfile(asList(
                    new Couple(new QoS(54, 9.61 / 2.0), 0.9957),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0043))),
                    new QoSProfile(asList(
                            new Couple(new QoS(54, 2.89 / 2.0), 0.9959),
                            new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0041))));
//
//                I.addLink("fog2", "fog3", new QoSProfile(asList(
//                        new Couple(new QoS(54, 9.61/2.0), 0.9957),
//                        new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0043))), 
//                        new QoSProfile(asList(
//                        new Couple(new QoS(54, 2.89/2.0), 0.9959),
//                        new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0041))));
        } else if (profile2.equals("4G")) {
            I.addLink("fog2", "cloud1", new QoSProfile(asList(
                    new Couple(new QoS(53, 22.67 / 2.0), 0.9926),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0074))),
                    new QoSProfile(asList(
                            new Couple(new QoS(53, 16.97 / 2.0), 0.9937),
                            new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0063))));
            I.addLink("fog2", "cloud2", new QoSProfile(asList(
                    new Couple(new QoS(53, 22.67 / 2.0), 0.9926),
                    new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0074))),
                    new QoSProfile(asList(
                            new Couple(new QoS(53, 16.97 / 2.0), 0.9937),
                            new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0063))));
//
//                I.addLink("fog2", "fog3", new QoSProfile(asList(
//                        new Couple(new QoS(53, 22.67/2.0), 0.9926),
//                        new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0074))),
//                        new QoSProfile(asList(
//                        new Couple(new QoS(53, 16.97/2.0), 0.9937),
//                        new Couple(new QoS(Integer.MAX_VALUE, 0.0), 0.0063))));
        }

        //WLAN
        I.addLink("fog1", "fog2", new QoSProfile(asList(
                new Couple(new QoS(15, 32), 0.9),
                new Couple(new QoS(15, 16), 0.1)
               // ,new Couple(new QoS(15, 0), 0.01)
        )));
        
        I.addLink("fog1", "fog3", new QoSProfile(asList(
                new Couple(new QoS(15, 32), 0.9),
                new Couple(new QoS(15, 16), 0.1)
                //,new Couple(new QoS(15, 0), 0.01)
        )));
        
        I.addLink("fog2", "fog3", new QoSProfile(asList(
                new Couple(new QoS(15, 32), 0.9),
                new Couple(new QoS(15, 16), 0.1)
                //,new Couple(new QoS(15, 0), 0.01)
        )));
//
//        I.addLink("fog2", "fog3", new QoSProfile(asList(
//                new Couple(new QoS(15, 18), 0.76),
//                new Couple(new QoS(15, 5.5), 0.24)
//        //,new Couple(new QoS(15, 0), 0.01)
//        )));
//
//        I.addLink("fog1", "fog3", new QoSProfile(asList(
//                new Couple(new QoS(25, 14), 0.64),
//                new Couple(new QoS(25, 3), 0.36)
//        //, new Couple(new QoS(25, 0), 0.01)
//        )));

        //inter-cloud
        I.addLink("cloud1", "cloud2", new QoSProfile(5, 1000));

        //Things for local
        I.addThing("fire_sensor_1", "fire", 43.7464449, 10.4615923, "fog1", 0.01);
        I.addThing("lights_control_1", "lights_control", 43.7464449, 10.4615923, "fog1", 0.03);
        I.addThing("thermostate_1", "thermostate", 43.7464449, 10.4615923, "fog1", 0.01);
        I.addThing("videocamera_1", "videocamera", 43.7464449, 10.4615923, "fog1", 30);
        
        I.addThing("fire_sensor_2", "fire", 43.7464449, 10.4615923, "fog2", 0.01);
        I.addThing("lights_control_2", "lights_control", 43.7464449, 10.4615923, "fog2", 0.03);
        I.addThing("thermostate_2", "thermostate", 43.7464449, 10.4615923, "fog2", 0.01);
        I.addThing("videocamera_2", "videocamera", 43.7464449, 10.4615923, "fog2", 30);
        
        I.addThing("weather_station_3", "weather_station", 43.7464449, 10.4615923, "fog3", 0.01);


        // System.out.println(I.L);
        Application A = new Application();
        ArrayList<ThingRequirement> neededThings = new ArrayList<>();
        //QoSProfile qNodeThing, QoSProfile qThingNode
        
        neededThings.add(new ExactThing("fire_sensor_1", new QoSProfile(100, 0.1), new QoSProfile(100, 0.5), 43200)); // 1 s and 1 Mbps
        neededThings.add(new ExactThing("lights_control_1", new QoSProfile(200, 0.9), new QoSProfile(200, 1.0), 2160)); // 110 ms and 1 Mbps
        neededThings.add(new ExactThing("thermostate_1", new QoSProfile(2000, 0.1), new QoSProfile(2000, 0.1), 1440)); // 0.5 s and 1 Mbps
        neededThings.add(new ExactThing("videocamera_1", new QoSProfile(50, 0.1), new QoSProfile(50, 5), 1)); // 25 ms and 4Mbps for the HD videostreaming
        neededThings.add(new ExactThing("weather_station_3", new QoSProfile(5000, 0.1), new QoSProfile(5000, 0.5), 150));
        
        
        //components
        //A.addComponent("A", asList("linux"), new Hardware(1, 1.2, 8), neededThings);
        A.addComponent("A", asList("linux"), new Hardware("tiny", 0.0), neededThings);

        A.addComponent("B", asList("linux", "mySQL"), new Hardware("large", 0.0)); //cores ram storage
        //A.addComponent("B", asList("linux", "mySQL"), new Hardware("small", 0.0));

        A.addComponent("C", asList("linux", "php"), new Hardware("small", 0.0));

        A.addLink("A", "B", 160, 0.5, 3.5); //160 ms and 10Mbps down and 1 Mbps up
        A.addLink("A", "C", 140, 0.4, 0.9);
        A.addLink("B", "C", 100, 0.3, 1.5);

        MonteCarloSearch s = new MonteCarloSearch(100000, A, I); //new Coordinates(43.740186, 10.364619));

        //s.addBusinessPolicies("C", asList("cloud2", "cloud1"));
        //s.addKeepLightNodes(asList("fog3"));

        String filename
                = "C:\\Users\\Stefano\\Dropbox\\_Dottorato\\2017_Fog_World_Congress\\results\\deployment_";

        boolean over = false;
        int i = 0;
        while (!over) {

            histogram = s.startSimulation(asList());
            String name = filename + i + ".csv";
            try {
                PrintWriter writer = new PrintWriter(name, "UTF-8");
                writer.println("Deployment ; QoS-assurance; Hardware %;Cost");
                System.out.println("Deployment ; QoS-assurance ; Hardware %;Cost");
                int j = 0;
                for (Deployment dep : histogram.keySet()) {
                    // histogram.replace(dep, new Couple((100 * histogram.get(dep).getA() / ((double) TIMES)), (100 * histogram.get(dep).getB() / (double) TIMES)));
                    writer.println(dep + "; " + histogram.get(dep).getA() + ";" + histogram.get(dep).getB() + "; " + dep.deploymentMonthlyCost);
                    System.out.println(j + " - "+dep + "; " + histogram.get(dep).getA() + "; " + histogram.get(dep).getB() + "; " + dep.deploymentMonthlyCost);
                    j++;
                }
                writer.close();
            } catch (IOException e) {
            }

            i++;

            if (!histogram.isEmpty()) {

                Scanner reader = new Scanner(System.in);  // Reading from System.in
                ArrayList<Deployment> l = new ArrayList(histogram.keySet());
                System.out.println(l);
                System.out.println("Enter a deployment number: ");
                int n = reader.nextInt();
                
                
                Deployment chosenDeployment = l.get(n);
                System.out.println(chosenDeployment);
                s.executeDeployment(chosenDeployment);
//                s.addBusinessPolicies("B", asList(chosenDeployment.get(new SoftwareComponent("B")).getId()));
//                s.addBusinessPolicies("C", asList(chosenDeployment.get(new SoftwareComponent("C")).getId()));

            } else {
                over = true;
            }

           // s.A = new SmallApp().SmallAppCreate();

        }

    }
}
