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
    }

    public Hardware(Hardware r) {
        this.cores = r.cores;
        this.ram = r.ram;
        this.storage = r.storage;
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
