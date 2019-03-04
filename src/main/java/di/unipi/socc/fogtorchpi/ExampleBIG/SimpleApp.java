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
    public Application createApp(){
     Application A = new Application();
        ArrayList<ThingRequirement> neededThings = new ArrayList<>();

        neededThings.add(new ExactThing("t0", new QoSProfile(45, 0.1), new QoSProfile(45, 3), 30)); // 1 s and 1 Mbps

       A.addComponent("A", asList("linux"), new Hardware(1,0.5,1), neededThings);
        A.addComponent("B", asList("linux"), new Hardware(1,1,5)); //cores ram storage

        A.addLink("A", "B", 50, 1, 5);


        A.addComponent("C", asList("linux"), new Hardware(1,0.5,1), neededThings);
        A.addComponent("D", asList("linux"), new Hardware(1,1,5)); //cores ram storage
        A.addLink("B", "C", 80, 1, 5);
        A.addLink("B", "D", 80, 1, 5);


        A.addComponent("A1", asList("linux"), new Hardware(1,0.5,1), neededThings);
        A.addComponent("B1", asList("linux"), new Hardware(1,1,5)); //cores ram storage
        A.addLink("A1", "B1", 80, 1, 5);

        A.addComponent("C1", asList("linux"), new Hardware(1,0.5,1), neededThings);
        A.addComponent("D1", asList("linux"), new Hardware(1,1,5)); //cores ram storage
        A.addLink("B1", "C1", 80, 1, 5);
        A.addLink("B1", "D1", 80, 1, 5);


        A.addComponent("A2", asList("linux"), new Hardware(1,0.5,1), neededThings);
        A.addComponent("B2", asList("linux"), new Hardware(1,1,5)); //cores ram storage
        A.addLink("A2", "B2", 80, 1, 5);

        A.addComponent("C2", asList("linux"), new Hardware(1,0.5,1), neededThings);
        A.addComponent("D2", asList("linux"), new Hardware(1,1,5)); //cores ram storage
        A.addLink("B2", "C2", 80, 1, 5);
        A.addLink("B2", "D2", 80, 1, 5);


        A.addComponent("A3", asList("linux"), new Hardware(1,0.5,1), neededThings);
        A.addComponent("B3", asList("linux"), new Hardware(1,1,5)); //cores ram storage
        A.addLink("A3", "B3", 80, 1, 5);

        A.addComponent("C3", asList("linux"), new Hardware(1,0.5,1), neededThings);
        A.addComponent("D3", asList("linux"), new Hardware(1,1,5)); //cores ram storage
        A.addLink("B3", "C3", 80, 1, 5);
        A.addLink("B3", "D3", 80, 1, 5);


//
//        neededThings.add(new ExactThing("t0", new QoSProfile(45, 0.1), new QoSProfile(45, 3), 30)); // 1 s and 1 Mbps
//        A.addComponent("A", asList("linux"), new Hardware(1,0.5,1), neededThings);
//        A.addComponent("B", asList("linux"), new Hardware(1,1,5)); //cores ram storage
//        A.addLink("A", "B", 50, 1, 5);
//

        return A;
    }
    
}
