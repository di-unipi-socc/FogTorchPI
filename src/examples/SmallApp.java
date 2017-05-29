package examples;

import fogtorch.application.Application;
import fogtorch.application.ExactThing;
import fogtorch.application.ThingRequirement;
import fogtorch.deployment.MonteCarloSearch;
import fogtorch.utils.Hardware;
import fogtorch.utils.QoSProfile;
import java.util.ArrayList;
import static java.util.Arrays.asList;

/**
 *
 * @author Stefano
 */
public class SmallApp {
    private boolean video = true;
    private String quality = "SD";
    private boolean notFog3 = false;
    
    public Application SmallAppCreate() {
        Application A = new Application();
        ArrayList<ThingRequirement> neededThings = new ArrayList<>();
        //QoSProfile qNodeThing, QoSProfile qThingNode
        
        neededThings.add(new ExactThing("fire_sensor_1", new QoSProfile(100, 0.1), new QoSProfile(100, 0.5), 43200)); // 1 s and 1 Mbps
        neededThings.add(new ExactThing("lights_control_1", new QoSProfile(200, 0.9), new QoSProfile(200, 1.0), 2160)); // 110 ms and 1 Mbps
        neededThings.add(new ExactThing("thermostate_1", new QoSProfile(2000, 0.1), new QoSProfile(2000, 0.1), 1440)); // 0.5 s and 1 Mbps
        if (video) {
            neededThings.add(new ExactThing("videocamera_1", new QoSProfile(25, 0.1), new QoSProfile(50, 5), 1)); // 25 ms and 4Mbps for the HD videostreaming
        }
        neededThings.add(new ExactThing("weather_station_3", new QoSProfile(500, 0.1), new QoSProfile(5000, 0.2), 150));
        
        
        //components
        //A.addComponent("A", asList("linux"), new Hardware(1, 1.2, 8), neededThings);
        A.addComponent("A", asList("linux"), new Hardware("tiny", 0.0), neededThings);

        A.addComponent("B", asList("linux", "mySQL"), new Hardware(0, 0.0, 0)); //cores ram storage
        //A.addComponent("B", asList("linux", "mySQL"), new Hardware("small", 0.0));

        A.addComponent("C", asList("linux", "php"), new Hardware(0, 0.0, 0));

        if (quality.equals("HD")) {
            A.addLink("A", "B", 160, 0.5, 3.5); //160 ms and 10Mbps down and 1 Mbps up
        } else if (quality.equals("SD")) {
            A.addLink("A", "B", 160, 0.5, 0.7);
        }

        A.addLink("A", "C", 140, 0.4, 1.1);
        A.addLink("B", "C", 100, 0.8, 1);

        return A;
    }
}
