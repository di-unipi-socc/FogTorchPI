/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.unipi.socc.fogtorchpi.deployment;

import java.util.HashMap;
import java.util.HashSet;
import di.unipi.socc.fogtorchpi.application.SoftwareComponent;
import di.unipi.socc.fogtorchpi.infrastructure.CloudDatacentre;
import di.unipi.socc.fogtorchpi.infrastructure.ComputationalNode;
import di.unipi.socc.fogtorchpi.infrastructure.FogNode;
import di.unipi.socc.fogtorchpi.infrastructure.Infrastructure;
import di.unipi.socc.fogtorchpi.utils.Cost;
import di.unipi.socc.fogtorchpi.utils.Couple;
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

    public double computeDeploymentSecurityScore(){
        double score = 0;
        for (ComputationalNode n : this.values()){
            score += n.computeNodeSecurityScore();
        }
        return score;
    }

    public double computeDeploymentSecurityScoreByType(String type){
        double score = 0;
        for (ComputationalNode n : this.values()){
            score += n.computeNodeSecurityScoreByType(type);
        }
        return score;
    }

    public double computeDeploymentSecurityScoreByComponent(SoftwareComponent c){
        double score = 0;

        score += this.get(c).computeNodeSecurityScore();

        return score;
    }

    public double computeDeploymentSecurityScoreByComponentAndType(SoftwareComponent c, String type){
        double score = 0;

        score += this.get(c).computeNodeSecurityScoreByType(type);

        return score;
    }
    

}
