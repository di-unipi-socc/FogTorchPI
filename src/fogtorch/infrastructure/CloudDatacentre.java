/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.infrastructure;

import java.util.Collection;
import java.util.List;
import fogtorch.application.SoftwareComponent;

/**
 *
 * @author stefano
 */
public class CloudDatacentre extends ComputationalNode {
    
    public CloudDatacentre(String identifier, Collection<String> software, double x, double y){
        super.setId(identifier);
        super.setSoftware(software);
        super.setCoordinates(x,y);
        super.setKeepLight(false);
    }

    @Override
    public boolean isCompatible(SoftwareComponent component) {
        List<String> softwareRequirements = component.getSoftwareRequirements();
        return softwareRequirements.stream().noneMatch((s) 
                -> (!super.getSoftware().contains(s)));
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
    
}
