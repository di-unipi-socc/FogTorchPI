package di.unipi.socc.fogtorchpi.experiments;

import di.unipi.socc.fogtorchpi.application.Application;
import di.unipi.socc.fogtorchpi.application.ExactThing;
import di.unipi.socc.fogtorchpi.application.ThingRequirement;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class VRApplication {
    private int SMARTPHONES;
    private int GATEWAYS  ;

    public VRApplication(int GATEWAYS, int SMARTPHONES){
        this.GATEWAYS = GATEWAYS;
        this.SMARTPHONES = SMARTPHONES;
    }

    public Application createApp(){
        Application A = new Application();

        for (int i = 0; i < GATEWAYS; i++){
            for (int j = 0; j < SMARTPHONES; j++){
                String componentName = "client_"+i+"_"+j;
                ArrayList<ThingRequirement> neededThings = new ArrayList<>();
                // QoSProfile qNodeThing, QoSProfile qThingNode
                neededThings.add(new ExactThing("EEG"+i+"_"+j, new QoSProfile(6, 0), new QoSProfile(6, 0), 0));
                neededThings.add(new ExactThing("display"+i+"_"+j, new QoSProfile(1, 0), new QoSProfile(1, 0), 0));
                A.addComponent(componentName, asList("linux"), new Hardware(1,0.1, 0), neededThings);

                A.addLink("concentrator", "client_"+i+"_"+j, 8, 0, 0); //loop used to be 7 ms
                A.addLink("coordinator", "client_"+i+"_"+j, 100, 0, 0);
            }
        }

        A.addComponent("concentrator", asList("linux"), new Hardware(1,0.1, 0));
        A.addComponent("coordinator", asList("linux"), new Hardware(1,0.1, 0));

        A.addLink("concentrator", "coordinator", 100, 0,0);

        return A;

    }
}
