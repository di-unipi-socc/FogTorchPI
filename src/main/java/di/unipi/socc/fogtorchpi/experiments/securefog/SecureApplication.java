package di.unipi.socc.fogtorchpi.experiments.securefog;

import di.unipi.socc.fogtorchpi.application.Application;
import di.unipi.socc.fogtorchpi.application.ExactThing;
import di.unipi.socc.fogtorchpi.application.ThingRequirement;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;
import di.unipi.socc.fogtorchpi.utils.SecurityParameters;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class SecureApplication {

    public SecureApplication(){

    }

    public Application createApp(){
        Application A = new Application();
        ArrayList<ThingRequirement> neededThings = new ArrayList<>();
        //QoSProfile qNodeThing, QoSProfile qThingNode

        neededThings.add(new ExactThing("fire_sensor_1", new QoSProfile(100, 0.1), new QoSProfile(100, 0.5), 43200)); // 1 s and 1 Mbps
        neededThings.add(new ExactThing("lights_control_1", new QoSProfile(200, 0.9), new QoSProfile(200, 1.0), 2160)); // 110 ms and 1 Mbps
        neededThings.add(new ExactThing("thermostate_1", new QoSProfile(2000, 0.1), new QoSProfile(2000, 0.1), 1440)); // 0.5 s and 1 Mbps
        neededThings.add(new ExactThing("videocamera_1", new QoSProfile(50, 0.1), new QoSProfile(50, 5), 1)); // 25 ms and 4Mbps for the HD videostreaming
        neededThings.add(new ExactThing("weather_station_3", new QoSProfile(5000, 0.1), new QoSProfile(5000, 0.5), 150));


        //components
        //A.addComponent("IoTController", asList("linux"), new Hardware(1, 1.2, 8), neededThings);
        A.addComponent("IoTController", asList("linux"), new Hardware("tiny", 0.0), neededThings,
                asList(SecurityParameters.ANTI_TAMPERING,
                        SecurityParameters.WIRELESS_SECURITY,
                        SecurityParameters.ENCRYPTION));

        A.addComponent("DataStorage", asList("linux", "mySQL"), new Hardware("large", 0.0)
                 ,asList(
                        SecurityParameters.ENCRYPTED_STORAGE,
                        SecurityParameters.AUDIT,
                        SecurityParameters.RESTORE_POINTS,
                        SecurityParameters.BACKUP,
                        SecurityParameters.ENCRYPTION
                )
        ); //cores ram storage
        //A.addComponent("DataStorage", asList("linux", "mySQL"), new Hardware("small", 0.0));

        A.addComponent("Dashboard", asList("linux", "php"), new Hardware("small", 0.0)
                ,
                asList(
                        SecurityParameters.ACCESS_LOGS,
                        SecurityParameters.FIREWALL,
                        SecurityParameters.IDS_NETWORK,
                        SecurityParameters.ENCRYPTION,
                        SecurityParameters.PERMISSION_MODEL,
                        SecurityParameters.AUTHENTICATION
                )
        );

        A.addLink("IoTController", "DataStorage", 160, 0.5, 3.5); //160 ms and 10Mbps down and 1 Mbps up
        A.addLink("IoTController", "Dashboard", 140, 0.4, 0.9);
        A.addLink("DataStorage", "Dashboard", 100, 0.3, 1.5);


        return A;

    }
}
