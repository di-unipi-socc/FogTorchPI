/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.infrastructure;

import fogtorch.application.ExactThing;
import java.util.Collection;
import java.util.List;
import fogtorch.application.SoftwareComponent;
import fogtorch.application.ThingRequirement;
import fogtorch.utils.Cost;
import fogtorch.utils.Couple;
import fogtorch.utils.Hardware;
import fogtorch.utils.Software;
import java.util.HashMap;

/**
 *
 * @author stefano
 */
public class CloudDatacentre extends ComputationalNode {
    
    HashMap<String, Hardware> virtualMachines;
    
    public CloudDatacentre(String identifier, List<Couple<String, Double>> software,  double x, double y, Hardware h){
        super.setHardware(h);
        super.setId(identifier);
        super.setSoftware(software);
        super.setCoordinates(x,y);
        super.setKeepLight(false);
        virtualMachines =  new HashMap<>();
    }
    
    public CloudDatacentre(String identifier, List<Couple<String, Double>> software,  double x, double y, Hardware h, List<Couple<String, Double>> vmTypes){
        super.setHardware(h);
        super.setId(identifier);
        super.setSoftware(software);
        super.setCoordinates(x,y);
        super.setKeepLight(false);
        
        this.virtualMachines = new HashMap<>();
        
        for ( Couple c : vmTypes ){
            this.virtualMachines.put(identifier, new Hardware((String)c.getA(), (Double)(c.getB())));
        }
        
        
    }

    @Override
    public boolean isCompatible(SoftwareComponent component) {
        List<Software> softwareRequirements = component.getSoftwareRequirements();

        for (Software s : softwareRequirements){
            //System.out.println(s);
            if (!(this.getSoftware().containsValue(s)))
                return false;
        }
        
        return true;
        //return softwareRequirements.stream().noneMatch((s) 
              //  -> (!super.getSoftware().contains(s)));
    }

    @Override
    public void deploy(SoftwareComponent s) {
    }

    @Override
    public void undeploy(SoftwareComponent s) {
    }
    
    @Override
    public String toString(){
        String result = "<";
        result = result + getId() + ", " + this.getSoftware()+ ", "+ this.getCoordinates();        
        result += ">";
        return result; 
    }
    
    @Override
    public double computeHeuristic(SoftwareComponent s) { //, Coordinates deploymentLocation
        this.heuristic = 4;//+ 1/(deploymentLocation.distance(this.getCoordinates()));
        //System.out.println(this.getId() + " " + this.heuristic);
        if (super.getKeepLight()){
            this.heuristic = 0;
        }
        return heuristic;
    }
    
    
    @Override
    public Cost computeCost(SoftwareComponent s, Infrastructure I) {

        double cost = 0.0;
        Hardware sHardwareReqs = s.getHardwareRequirements();
        
        if (sHardwareReqs.stringDescribed && !this.virtualMachines.isEmpty()){
            cost += this.virtualMachines.get(sHardwareReqs.vmType).getMonthlyCost(s);
        } else {
            cost += this.getHardware().getMonthlyCost(s);
        }

        for (Software soft : s.getSoftwareRequirements()) {
            cost += super.getSoftware().get(soft.getName()).getCost();
        }

        for (ThingRequirement thing : s.getThingsRequirements()) {
            cost += ((ExactThing) thing).getMonthlyInvoke() * I.T.get(((ExactThing) thing).getId()).getMonthlyCost(s);
        }

        return new Cost(cost);
    }
    
}
