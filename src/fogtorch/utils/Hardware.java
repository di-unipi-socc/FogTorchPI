/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.utils;

/**
 *
 * @author Stefano
 */
public class Hardware {
    
    public int cores; //CPU cores
    public double ram; //needed RAM
    public int storage; //neededStorage
    
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
    
}
