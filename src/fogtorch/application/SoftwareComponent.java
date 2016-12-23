/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.application;

import fogtorch.utils.Hardware;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author stefano
 */
public class SoftwareComponent implements Comparable {
    String identifier;
    List<String> softwareReqs;
    Hardware hw;
    public ArrayList<ThingRequirement> Theta;

    public SoftwareComponent(String identifier, List<String> softwareReqs, Hardware hardwareReqs, ArrayList<ThingRequirement> Theta){
        this.identifier = identifier;
        this.softwareReqs = softwareReqs;
        this.Theta = new ArrayList<>(Theta);
        this.hw = new Hardware(hardwareReqs);
    }

    public List<String> getSoftwareRequirements() {
        return softwareReqs;
    }

    public Hardware getHardwareRequirements() {
        return hw;
    }

    public String getId() {
        return this.identifier;
    }
    
    @Override
    public String toString(){
        String result = "<";
        result = result + this.identifier + ", " + this.softwareReqs + ", "+ hw + ", "+  Theta;        
        result += ">";
        return result; 
    }

    public Iterable<ThingRequirement> getThingsRequirements() {
        return this.Theta;
    }
    
    @Override
    public boolean equals(Object o){
        SoftwareComponent s2 = (SoftwareComponent) o;
        return this.identifier.equals(s2.getId());
    }

    @Override
    public int compareTo(Object o) {
        SoftwareComponent s2 = (SoftwareComponent) o;
        return this.identifier.compareTo(s2.identifier);
    }
    
}
