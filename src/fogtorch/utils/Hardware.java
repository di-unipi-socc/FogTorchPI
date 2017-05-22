/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.utils;

import fogtorch.application.SoftwareComponent;

/**
 *
 * @author Stefano
 */
public class Hardware {
    
    public int cores; //CPU cores
    public double ram; //needed RAM
    public int storage; //neededStorage
    
    //unit monthly cost for each resource
    private Cost ramCost;
    private Cost cpuCost;
    private Cost storageCost;
    
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

    public Hardware() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        return "CPU: " + this.cores + " RAM: " + this.ram + " HDD " + this.storage;
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
        return requiredHardware.ram * this.ramCost.getCost() +
                requiredHardware.cores * this.cpuCost.getCost() +
                requiredHardware.storage * this.storageCost.getCost();
        
    }
    
}
