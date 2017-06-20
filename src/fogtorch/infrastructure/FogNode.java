/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.infrastructure;

import fogtorch.application.ExactThing;
import java.util.Collection;
import java.util.HashSet;
import fogtorch.application.SoftwareComponent;
import fogtorch.application.ThingRequirement;
import fogtorch.deployment.Deployment;
import fogtorch.utils.Constants;
import fogtorch.utils.Coordinates;
import fogtorch.utils.Couple;
import fogtorch.utils.Hardware;

/**
 *
 * @author stefano
 */
public class FogNode extends ComputationalNode{
    public HashSet<String> connectedThings;
    private Hardware hw;
    
    public FogNode(String identifier, Collection<String> software, Hardware hw, double x, double y){
        super.setId(identifier);
        this.hw = new Hardware(hw);
        super.setSoftware(software);
        super.setCoordinates(x,y);
        connectedThings = new HashSet<>();
        super.setKeepLight(false);
    }


    public void setHardware(Hardware h){
       hw = new Hardware(h);
    }
    

    
    public Hardware getHardware(){
        return hw;
    }
    
    @Override
    public boolean isCompatible(SoftwareComponent component){
        Hardware hardwareRequest = component.getHardwareRequirements();
        Collection<String> softwareRequest = component.getSoftwareRequirements();
        
        return hw.supports(hardwareRequest) && 
                softwareRequest.stream().noneMatch(
                        (s) -> (!super.getSoftware().contains(s))
                );
    }
   

    @Override
    public void deploy(SoftwareComponent s) {
        hw.deploy(s.getHardwareRequirements());
    }

    @Override
    public void undeploy(SoftwareComponent s) {
        hw.undeploy(s.getHardwareRequirements());
    }
    
        @Override
    public String toString(){
        String result = "<";
        result = result + getId() + ", " + super.getSoftware() + ", "+ hw +", "+this.getCoordinates();        
        result += ">";
        return result; 
    }


    public double distance(Thing t) {
        return t.getCoordinates().distance(super.getCoordinates());
    }
    
    /**
     * Returns a couple with the percentage of used RAM and storage at
     * this Fog node.
     * @param d a deployment
     * @return a couple with the percentage of used RAM and storage.
     */
    public Couple<Double, Double> consumedResources(SoftwareComponent s){
        Couple<Double,Double> result = new Couple<>(0.0,0.0);
  
        Hardware used = s.getHardwareRequirements();
        result.setA(result.getA() + used.ram);
        result.setB(result.getB() + used.storage);

        //result.setA(result.getA()/this.getHardware().ram);
        //result.setB(result.getB()/this.getHardware().storage);
        
        return result;
    }

    @Override
    public double computeHeuristic(SoftwareComponent s) { //Coordinates deploymentLocation
        
        this.heuristic = this.hw.cores/Constants.MAX_CORES + 
                this.hw.ram/Constants.MAX_RAM + 
                this.hw.storage/Constants.MAX_HDD; //+ 1/(deploymentLocation.distance(this.getCoordinates()));
        
        if (this.getKeepLight()){
            heuristic = heuristic - 4;
        }

        return heuristic;
    }
}


