/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.unipi.socc.fogtorchpi.simpleexample;

import di.unipi.socc.fogtorchpi.infrastructure.Infrastructure;

import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.InfrastructureFactory;
import di.unipi.socc.fogtorchpi.utils.QoS;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;
import static java.util.Arrays.asList;

/**
 *
 * @author stefa
 */
public class SimpleInfrastructure extends InfrastructureFactory {
    
    public SimpleInfrastructure(){}

    @Override
    public Infrastructure createInfrastructure(){
        Infrastructure I = new Infrastructure();

        I.addCloudDatacentre("cloud", asList(
                new Couple("linux", 2.0)),
                52.195097, 3.0364791,
                new Hardware(0, 0, 0, 2.0, 3.0, 1.0)
        );

        I.addFogNode("fog", asList(
                new Couple("linux", 2.5)),
                new Hardware(2, 2, 10, 4.0, 5.0, 3.0),
                43.740186, 10.364619);

        /*
        I.addFogNode("fog2", asList(
                new Couple("linux", 2.5)),
                new Hardware(1, 1, 5, 2.0, 2.5, 1.5),
                23.7464449, 10.364619);
        */
        

        I.addLink("fog", "cloud", 
                new QoSProfile(asList(
                    new Couple(new QoS(40, 8), 0.9),
                    new Couple(new QoS(60, 3), 0.1))),
                new QoSProfile(asList(
                    new Couple(new QoS(40, 6), 0.9),
                    new Couple(new QoS(60, 1), 0.1))
                ));

       /* I.addLink("fog2", "cloud",
                new QoSProfile(asList(
                        new Couple(new QoS(40, 8), 0.9),
                        new Couple(new QoS(60, 3), 0.1))),
                new QoSProfile(asList(
                        new Couple(new QoS(40, 6), 0.9),
                        new Couple(new QoS(60, 1), 0.1))
                ));
*/

        I.addThing("t", "type", 43.7464449, 10.4615923, "fog", 0.01);
        //I.addThing("t2", "type", 23.7464449, 10.4615923, "fog2", 0.01);

        return I;
    }
    
}
