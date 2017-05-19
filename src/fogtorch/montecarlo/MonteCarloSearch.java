/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.montecarlo;

import fogtorch.application.Application;
import fogtorch.deployment.Search;
import fogtorch.infrastructure.Infrastructure;
import fogtorch.utils.QoSProfile;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Stefano
 */
public class MonteCarloSearch {
    private int times;
    private Application A;
    private Infrastructure I;
    private HashMap<String, HashSet<String>> deploymentPolicies;
    private Search search;
    
    
    public MonteCarloSearch(
            int times, 
            Application A,
            Infrastructure I,
            HashMap<String, HashSet<String>> deploymentPolicies){
        
        this.times = times;
        this.A = A;
        this.I = I;
        this.deploymentPolicies = deploymentPolicies;
        
        search = new Search(A, I);
        for (String component : deploymentPolicies.keySet()){
            List l = asList(deploymentPolicies.get(component));
            search.addBusinessPolicies(component, l);
        }    
    }
    
    public void estimateQoSAssurance(){
        for (QoSProfile q : I.L.values()){
            
            
        }
    }
}
