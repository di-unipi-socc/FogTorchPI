package di.unipi.socc.fogtorchpi.application;

import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.Software;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 *
 * @author stefano
 */
public class SoftwareComponent implements Comparable {
    String identifier;
    List<Software> softwareReqs;
    Hardware hw;
    public ArrayList<ThingRequirement> Theta;

    public SoftwareComponent(String identifier, List<String> softwareReqs, Hardware hardwareReqs, ArrayList<ThingRequirement> Theta){
        this.identifier = identifier;
        this.setSoftware(softwareReqs);
        this.Theta = new ArrayList<>(Theta);
        this.hw = new Hardware(hardwareReqs);
    }
    
    public SoftwareComponent(String id){
        this.identifier  = id;
    }
    
    public void setSoftware(Collection<String> software){
        this.softwareReqs = new ArrayList<>();
        for (String s : software){
            boolean add = this.softwareReqs.add(new Software(s));
        }
    }

    public List<Software> getSoftwareRequirements() {
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
