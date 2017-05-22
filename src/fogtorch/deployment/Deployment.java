/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.deployment;

import java.util.HashMap;
import java.util.HashSet;
import fogtorch.application.SoftwareComponent;
import fogtorch.infrastructure.CloudDatacentre;
import fogtorch.infrastructure.ComputationalNode;
import fogtorch.infrastructure.FogNode;
import fogtorch.infrastructure.Infrastructure;
import fogtorch.utils.Cost;
import fogtorch.utils.Couple;
import java.text.DecimalFormat;
import java.util.TreeMap;

/**
 *
 * @author stefano
 */
public class Deployment extends TreeMap<SoftwareComponent, ComputationalNode> {
    HashMap<String, HashSet<String>> businessPolicies;
    public Couple<Double, Double> consumedResources;
    public Cost deploymentMonthlyCost;
    
    public Deployment(){
        super();
        businessPolicies = new HashMap<>();   
        deploymentMonthlyCost = new Cost(0);
    }

    Deployment(Deployment deployment) {
        super(deployment);
    }
    
    @Override
    public Object clone(){
        Deployment d = new Deployment (this);
        d.deploymentMonthlyCost = new Cost(deploymentMonthlyCost.getCost());
        d.consumedResources = this.consumedResources;
        d.businessPolicies = this.businessPolicies;
        return d;
    }
    
    @Override
    public String toString(){
        String result ="";
        
        result += this.deploymentMonthlyCost + ": ";
        for (SoftwareComponent s : super.keySet()){
            result+="["+s.getId()+"->" +super.get(s).getId()+"]" ;
        }
        
        return result;   
    }
    
    @Override
    public boolean equals(Object o){
        boolean result = true;
        Deployment d = (Deployment) o;
        for (SoftwareComponent s : this.keySet()){
            if (!this.get(s).equals(d.get(s))){
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        String s = this.toString();
        hash = 47 * hash + s.hashCode();
        return hash;
    }

    void addCost(SoftwareComponent s, ComputationalNode n, Infrastructure I) {
        this.deploymentMonthlyCost.add(n.computeCost(s, I));
    }
    
    void removeCost(SoftwareComponent s, ComputationalNode n, Infrastructure I){
        this.deploymentMonthlyCost.remove(n.computeCost(s, I));
    }

}
