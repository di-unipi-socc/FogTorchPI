 package fogtorch.pricing;

import fogtorch.utils.Hardware;

/**
 *
 * @author Stefano
 */
public class PricedHardware extends Hardware {
    //unit monthly cost for each resource
    private Pricing ramCost;
    private Pricing cpuCost;
    private Pricing storageCost;
    
    public PricedHardware(Hardware r) {
        super(r);
    }
    public PricedHardware(Hardware r, double ramCost, double cpuCost, double storageCost) {
        super(r);
        this.ramCost = ramCost;
        this.cpuCost = cpuCost;
        this.storageCost = storageCost;
    }
    
    public double getMonthlyCost(Hardware h){
        
    }
    
}
