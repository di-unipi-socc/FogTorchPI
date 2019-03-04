/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.unipi.socc.fogtorchpi.simpleexample;

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

        neededThings.add(new ExactThing("t", new QoSProfile(45, 0.1), new QoSProfile(45, 3), 30)); // 1 s and 1 Mbps
       
        A.addComponent("A", asList("linux"), new Hardware(1,0.5,1), neededThings);
        A.addComponent("B", asList("linux"), new Hardware(1,1,5)); //cores ram storage

        A.addLink("A", "B", 50, 1, 5); 

        return A;
    }
    
}
