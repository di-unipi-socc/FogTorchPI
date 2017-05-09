/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import fogtorch.application.Application;
import fogtorch.application.ExactThing;
import fogtorch.application.ThingRequirement;
import fogtorch.utils.Hardware;
import fogtorch.utils.QoS;
import java.util.ArrayList;
import static java.util.Arrays.asList;

/**
 *
 * @author Stefano
 */
public class FTApplication {
    
    public static Application createApplication(){
        Application A = new Application();
        ArrayList<ThingRequirement> fireThings = new ArrayList<>();
        QoS qThingNode = new QoS(10, 1);
        QoS qNodeThing = new QoS(10, 1);
        
                
        fireThings.add(new ExactThing("fire1", qNodeThing, qThingNode));
        fireThings.add(new ExactThing("extinguisher1", qNodeThing, qThingNode));

        //components
        A.addComponent("mlengine", asList("mySQL",".NETcore"), new Hardware(0, 2, 0));
        A.addComponent("insights", asList("python","mySQL"), new Hardware(0, 8, 0));
        A.addComponent("firemanager", asList("linux", "python"), new Hardware(0, 2, 0), fireThings);
        
        //links
        A.addLink("insights", "mlengine", 100, 1);
        A.addLink("insights", "firemanager", 30, 1);
        A.addLink("firemanager", "mlengine", 200, 2, 1);
    
        return A;
    }
}
