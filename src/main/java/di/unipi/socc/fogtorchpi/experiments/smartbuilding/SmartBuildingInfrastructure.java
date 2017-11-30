package di.unipi.socc.fogtorchpi.experiments.smartbuilding;

import di.unipi.socc.fogtorchpi.infrastructure.Infrastructure;
import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoS;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;

import static java.util.Arrays.asList;

public class SmartBuildingInfrastructure {

    public SmartBuildingInfrastructure(){
    }

    public Infrastructure createInfrastructure(){
        Infrastructure I = new Infrastructure();
        //Change the access to Internet
        String profile = "20M";
        //String profile2 = "4G";
        String profile2 = "3G";

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

        I.addLink("fog3", "cloud1", new QoSProfile(60, 60 / 2.0), new QoSProfile(60, 6 / 2.0));
        I.addLink("fog3", "cloud2", new QoSProfile(60, 60 / 2.0), new QoSProfile(60, 6 / 2.0));

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

        I.addThing("fire_sensor_2", "fire", 43.7464449, 10.4615923, "fog2", 0.00);
        I.addThing("lights_control_2", "lights_control", 43.7464449, 10.4615923, "fog2", 0.00);
        I.addThing("thermostate_2", "thermostate", 43.7464449, 10.4615923, "fog2", 0.00);
        I.addThing("videocamera_2", "videocamera", 43.7464449, 10.4615923, "fog2", 0);

        I.addThing("weather_station_3", "weather_station", 43.7464449, 10.4615923, "fog3", 0.01);

        return I;
    }
}
