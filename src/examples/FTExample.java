/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.util.ArrayList;
import fogtorch.deployment.Search;
import static java.util.Arrays.asList;
import fogtorch.application.Application;
import fogtorch.application.ExactThing;
import fogtorch.application.ThingRequirement;
import fogtorch.infrastructure.Infrastructure;
import fogtorch.utils.QoSProfile;

/**
 *
 * @author stefano
 */
public class FTExample {

    /**
     * @param args the command line arguments
     */
    public Search start() {
        //Application
        Application A = FTApplication.createApplication();
        Infrastructure I = FTInfrastructure.createInfrastructure();
        
        Search s = new Search(A, I);
        s.addBusinessPolicies("mlengine", asList("cloud_2", "cloud_1"));
        s.findDeployments(true);

        return s;
    }


}
