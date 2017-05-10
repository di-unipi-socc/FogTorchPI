package fogtorch.pricing;

import fogtorch.application.SoftwareComponent;
import fogtorch.utils.Software;

/**
 *
 * @author Stefano
 */
public class PricedSoftware extends Software {
    public double cost;
    
    public PricedSoftware (String r) {
        super(r);
    }
    
    public PricedSoftware(String r, double cost) {
        super(r);
        this.cost = cost;
    }
    
    public double getMonthlyCost(SoftwareComponent s){
        if (s.getSoftwareRequirements().contains(s.getId())){
            return cost;
        }
        return 0.0;
    }
    
    
}
