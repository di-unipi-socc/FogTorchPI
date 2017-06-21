package di.unipi.socc.fogtorchpi.utils;

import di.unipi.socc.fogtorchpi.application.SoftwareComponent;


/**
 *
 * @author Stefano
 */
public class Hardware {
    
    public boolean stringDescribed;
    public String vmType;
    
    public int cores; //CPU cores
    public double ram; //needed RAM
    public int storage; //neededStorage
    
    //unit monthly cost for each resource
    private Cost ramCost;
    private Cost cpuCost;
    private Cost storageCost;
    private Cost fixedCost;
    
    public Hardware(int cores, double ram, int storage){
        this.cores = cores;
        this.ram = ram;
        this.storage = storage;
        this.ramCost = new Cost(0.0);
        this.storageCost = new Cost(0.0);
        this.cpuCost = new Cost(0.0);
    }
    
    public Hardware(int cores, double ram, int storage, double coreCost, double ramCost, double storageCost){
        this.cores = cores;
        this.ram = ram;
        this.storage = storage;
        this.ramCost = new Cost(ramCost);
        this.storageCost = new Cost(storageCost);
        this.cpuCost = new Cost(coreCost);
    }

    public Hardware(Hardware r) {
        this.cores = r.cores;
        this.ram = r.ram;
        this.storage = r.storage;
        this.cpuCost = r.cpuCost;
        this.ramCost = r.ramCost;
        this.storageCost = r.storageCost;
    }
    
    public Hardware(String vmType){
        this.stringDescribed = true;
        this.vmType = vmType;
        
        Hardware h = Constants.getVMHardwareSpec(vmType);
        this.fixedCost = new Cost(0.0);
        this.cores = h.cores;
        this.ram = h.ram;
        this.storage = h.storage;
    }
    
    public Hardware(String vmType, double cost){
        this.stringDescribed = true;
        this.vmType = vmType;
        
        Hardware h = Constants.getVMHardwareSpec(vmType);
        this.fixedCost = new Cost(cost);
        this.cores = h.cores;
        this.ram = h.ram;
        this.storage = h.storage;
    }
    

    public Hardware() {
    }
    
    public void deploy(Hardware required){
        ram = ram - required.ram;
        storage = storage - required.storage;
    }
    
    public void undeploy(Hardware required){
        ram = ram + required.ram;
        storage = storage + required.storage;
    }  
    
    public boolean supports(Hardware required){
        return
                ram >= required.ram &&
                cores >= required.cores &&
                storage >= required.storage;
    }
    
    @Override
    public String toString(){
        return "CPU: " + this.cores + " RAM: " + this.ram + " HDD: " + this.storage +
                "\n"+
                "CPU cost: " + this.cpuCost + " RAM cost: " + this.ramCost + " HDD cost: " + this.storageCost;
    }
    
    public Hardware(Hardware r, double ramCost, double cpuCost, double storageCost, String currency) {
        this.cores = r.cores;
        this.ram = r.ram;
        this.storage = r.storage;
        this.ramCost = new Cost(ramCost, currency);
        this.cpuCost = new Cost(cpuCost, currency);
        this.storageCost = new Cost(storageCost, currency);
    }
    
    public double getMonthlyCost(SoftwareComponent s){
        Hardware requiredHardware = s.getHardwareRequirements();
        double cost = 0.0;
        
        if (this.stringDescribed){
            cost += this.fixedCost.getCost();
        } else {
            cost += requiredHardware.ram * this.ramCost.getCost() +
                requiredHardware.cores * this.cpuCost.getCost() +
                requiredHardware.storage * this.storageCost.getCost();
        }
        
        return cost;
        
    }
    
}
