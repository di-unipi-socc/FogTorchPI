package di.unipi.socc.fogtorchpi.infrastructure;

import di.unipi.socc.fogtorchpi.application.ExactThing;
import java.util.Collection;
import java.util.HashSet;
import di.unipi.socc.fogtorchpi.application.SoftwareComponent;
import di.unipi.socc.fogtorchpi.application.ThingRequirement;
import di.unipi.socc.fogtorchpi.utils.Constants;
import di.unipi.socc.fogtorchpi.utils.Cost;
import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.Software;

/**
 *
 * @author stefano
 */
public class FogNode extends ComputationalNode{
    public HashSet<String> connectedThings;

    
    public FogNode(String identifier, Collection<Couple<String, Double>> software, Hardware hw, double x, double y){
        super.setId(identifier);
        super.setHardware(hw);
        super.setSoftware(software);
        super.setCoordinates(x,y);
        connectedThings = new HashSet<>();
        super.setKeepLight(false);
    }

    
    @Override
    public boolean isCompatible(SoftwareComponent component){
        Hardware hardwareRequest = component.getHardwareRequirements();
        Collection<Software> softwareRequest = component.getSoftwareRequirements();
        
        return super.getHardware().supports(hardwareRequest) && 
                softwareRequest.stream().noneMatch(
                        (s) -> (!super.getSoftware().containsValue(s))
                );
    }
   

    @Override
    public void deploy(SoftwareComponent s) {
        super.getHardware().deploy(s.getHardwareRequirements());
    }

    @Override
    public void undeploy(SoftwareComponent s) {
        super.getHardware().undeploy(s.getHardwareRequirements());
    }
    
        @Override
    public String toString(){
        String result = "<";
        result = result + getId() + ", " + super.getSoftware() + ", "+ super.getHardware() +", "+this.getCoordinates();        
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
        
        return result;
    }

    @Override
    public double computeHeuristic(SoftwareComponent s) { //Coordinates deploymentLocation
        
        this.heuristic = super.getHardware().cores/Constants.MAX_CORES + 
                super.getHardware().ram/Constants.MAX_RAM + 
                super.getHardware().storage/Constants.MAX_HDD; //+ 1/(deploymentLocation.distance(this.getCoordinates()));
        
        if (this.getKeepLight()){
            heuristic = heuristic - 4;
        }

        return heuristic;
    }
    
    @Override
        public Cost computeCost(SoftwareComponent s, Infrastructure I) {
        double cost = 0.0;
        Hardware sHardwareReqs = s.getHardwareRequirements();

        cost += this.getHardware().getMonthlyCost(s);

        for (Software soft : s.getSoftwareRequirements()) {
            cost += super.getSoftware().get(soft.getName()).getCost();
        }

        for (ThingRequirement thing : s.getThingsRequirements()) {
            cost += ((ExactThing) thing).getMonthlyInvoke() * I.T.get(((ExactThing) thing).getId()).getMonthlyCost(s);
        }

        return new Cost(cost);
    }
    
    
}


