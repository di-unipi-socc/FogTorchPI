package fogtorch.pricing;

import fogtorch.application.SoftwareComponent;
import fogtorch.utils.Software;

/**
 *
 * @author Stefano
 */
public class PricedSoftware extends Software {
    private Cost cost;
    
    public PricedSoftware (String r) {
        super(r);
    }
    
    public PricedSoftware(String r, double cost) {
        super(r);
        this.cost = new Cost(cost);
    }
    
    public void setCost(double cost){
        this.cost = new Cost(cost);
    }
    
    public double getCost(){
        return cost.getCost();
    }
    
    public double getMonthlyCost(SoftwareComponent s){
        if (s.getSoftwareRequirements().contains(s.getId())){
            return cost.getCost();
        }
        return 0.0;
    }
    
    
}
