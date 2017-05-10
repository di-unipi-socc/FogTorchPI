 package fogtorch.pricing;

import fogtorch.application.SoftwareComponent;
import fogtorch.utils.Hardware;

/**
 *
 * @author Stefano
 */
public class PricedHardware extends Hardware {
    //unit monthly cost for each resource
    private Cost ramCost;
    private Cost cpuCost;
    private Cost storageCost;
    
    public PricedHardware(Hardware r) {
        super(r);
    }
    public PricedHardware(Hardware r, double ramCost, double cpuCost, double storageCost, String currency) {
        super(r);
        this.ramCost = new Cost(ramCost, currency);
        this.cpuCost = new Cost(cpuCost, currency);
        this.storageCost = new Cost(storageCost, currency);
    }
    
    public double getMonthlyCost(SoftwareComponent s){
        Hardware requiredHardware = s.getHardwareRequirements();
        
        return requiredHardware.ram * this.ramCost.getCost() +
                requiredHardware.cores * this.cpuCost.getCost() +
                requiredHardware.storage * this.storageCost.getCost();
        
    }
    
}
