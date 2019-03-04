/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.unipi.socc.fogtorchpi.ExampleBIG;

import di.unipi.socc.fogtorchpi.application.Application;
import di.unipi.socc.fogtorchpi.application.ExactThing;
import di.unipi.socc.fogtorchpi.application.ThingRequirement;
import di.unipi.socc.fogtorchpi.utils.AppFactory;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;

import java.util.ArrayList;

import static java.util.Arrays.asList;


public class SimpleApp extends AppFactory {

    SimpleApp() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Application createApp() {
        Application A = new Application();
        
        int N = 10; // number of apps
        
        for (int i = 0; i < N; i++){
            
            ArrayList<ThingRequirement> neededThings = new ArrayList<>();
            neededThings.add(new ExactThing("t" + i, new QoSProfile(45, 0.1), new QoSProfile(45, 3), 30)); // 1 s and 1 Mbps

            A.addComponent("A"+i, asList("linux"), new Hardware(1, 0.5, 1), neededThings);
            A.addComponent("B"+i, asList("linux"), new Hardware(1, 1, 5)); //cores ram storage
            A.addLink("A"+i, "B"+i, 80, 1, 5);

            A.addComponent("C"+i, asList("linux"), new Hardware(1, 0.5, 1), neededThings);
            A.addComponent("D"+i, asList("linux"), new Hardware(1, 1, 5)); //cores ram storage
            A.addLink("B"+i, "C"+i, 80, 1, 5);
            A.addLink("B"+i, "D"+i, 80, 1, 5);

        }
        
        return A;
    }

    
}
