/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.unipi.socc.fogtorchpi.ExampleSMALL;

import di.unipi.socc.fogtorchpi.infrastructure.Infrastructure;
import di.unipi.socc.fogtorchpi.utils.*;

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

        I.addCloudDatacentre("cloud2", asList(
                new Couple("linux", 2.0)),
                52.195097, 3.0364791,
                new Hardware(0, 0, 0, 2.0, 3.0, 5.0)
        );

        I.addFogNode("fog1", asList(
                new Couple("linux", 2.5)),
                new Hardware(2, 2, 10, 4.0, 5.0, 3.0),
                43.740186, 10.364619);

        I.addFogNode("fog2", asList(
                new Couple("linux", 2.5)),
                new Hardware(2, 2, 10, 4.0, 5.0, 3.0),
                43.740186, 10.364619);

        I.addFogNode("fog3", asList(
                new Couple("linux", 2.5)),
                new Hardware(2, 2, 10, 4.0, 5.0, 3.0),
                43.740186, 10.364619);


        /*
        I.addFogNode("fog2", asList(
                new Couple("linux", 2.5)),
                new Hardware(1, 1, 5, 2.0, 2.5, 1.5),
                23.7464449, 10.364619);
        */

//        I.addLink("fog", "cloud",
//                new QoSProfile(asList(
//                        new Couple(new QoS(40, 8), 1.0))),
//                new QoSProfile(asList(
//                        new Couple(new QoS(40, 6), 1.0)))
//                );

        I.addLink("fog1", "cloud",
                new    QoSProfile(asList(
                    new Couple(new QoS(50, 16), 0.9), //Download link
                    new Couple(new QoS(50, 8), 0.1))),
                new QoSProfile(asList(
                        new Couple(new QoS(50, 16), 0.9), //upload link
                        new Couple(new QoS(50, 8), 0.1)))
                );

        I.addLink("fog2", "cloud",
                new    QoSProfile(asList(
                        new Couple(new QoS(30, 16), 0.99), //Download link
                        new Couple(new QoS(30, 8), 0.01))),
                new QoSProfile(asList(
                    new Couple(new QoS(30, 16), 0.99), //upload link
                    new Couple(new QoS(30, 8), 0.01)))
        );


        I.addLink("fog3", "cloud",
                new    QoSProfile(asList(
                        new Couple(new QoS(30, 16), 0.9), //Download link
                        new Couple(new QoS(30, 8), 0.1))),
                new QoSProfile(asList(
                        new Couple(new QoS(30, 16), 0.9), //upload link
                        new Couple(new QoS(30, 8), 0.1)))
        );

        I.addLink("fog1", "fog2",
                new    QoSProfile(asList(
                        new Couple(new QoS(15, 16), 0.8), //Download link
                        new Couple(new QoS(15, 8), 0.2))),
                new QoSProfile(asList(
                        new Couple(new QoS(15, 16), 0.8), //upload link
                        new Couple(new QoS(15, 8), 0.2)))
        );

        I.addLink("fog1", "fog3",
                new    QoSProfile(asList(
                        new Couple(new QoS(15, 16), 0.8), //Download link
                        new Couple(new QoS(15, 8), 0.2))),
                new QoSProfile(asList(
                        new Couple(new QoS(15, 16), 0.8), //upload link
                        new Couple(new QoS(15, 8), 0.2)))
        );

        I.addLink("fog2", "fog3",
                new    QoSProfile(asList(
                        new Couple(new QoS(15, 16), 0.8), //Download link
                        new Couple(new QoS(15, 8), 0.2))),
                new QoSProfile(asList(
                        new Couple(new QoS(15, 16), 0.8), //upload link
                        new Couple(new QoS(15, 8), 0.2)))
        );


        I.addLink("fog1", "cloud2",
                new    QoSProfile(asList(
                        new Couple(new QoS(80, 6), 1.0))), //Download link
                new QoSProfile(asList(
                        new Couple(new QoS(80, 6), 1.0))) //upload link
        );

        I.addLink("fog2", "cloud2",
                new    QoSProfile(asList(
                        new Couple(new QoS(80, 6), 1.0))), //Download link
                new QoSProfile(asList(
                        new Couple(new QoS(80, 6), 1.0))) //upload link
        );
        I.addLink("fog3", "cloud2",
                new    QoSProfile(asList(
                        new Couple(new QoS(80, 6), 1.0))), //Download link
                new QoSProfile(asList(
                        new Couple(new QoS(80, 6), 1.0))) //upload link
        );




       /* I.addLink("fog2", "cloud",
                new QoSProfile(asList(
                        new Couple(new QoS(40, 8), 0.9),
                        new Couple(new QoS(60, 3), 0.1))),
                new QoSProfile(asList(
                        new Couple(new QoS(40, 6), 0.9),
                        new Couple(new QoS(60, 1), 0.1))
                ));
*/

        I.addThing("t0", "type", 43.7464449, 10.4615923, "fog1", 0.01);
        I.addThing("t1", "type", 23.7464449, 10.4615923, "fog2", 0.01);
        I.addThing("t2", "type", 23.7464449, 10.4615923, "fog3", 0.01);

        I.addThing("t3", "type", 23.7464449, 10.4615923, "fog1", 0.01);
        I.addThing("t4", "type", 23.7464449, 10.4615923, "fog2", 0.01);


        return I;
    }
    
}
