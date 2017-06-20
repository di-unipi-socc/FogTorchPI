/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import fogtorch.utils.Couple;
import fogtorch.utils.Hardware;
import fogtorch.utils.QoSProfile;

/**
 *
 * @author stefano
 */
public class Application {
    public ArrayList<SoftwareComponent> S;
    public HashMap<Couple, QoSProfile> L;
    
    public Application(){
        S = new ArrayList<>();
        L = new HashMap<>();
    }

    public void addLink(String a, String b, int latency, double bandwidth) {
        L.put(new Couple(a,b), new QoSProfile(latency,bandwidth));
        L.put(new Couple(b,a), new QoSProfile(latency,bandwidth));
    }

    public void addLink(String a, String b, int latency, double downlink, double uplink) {
        L.put(new Couple(a,b), new QoSProfile(latency,uplink));
        L.put(new Couple(b,a), new QoSProfile(latency,downlink));
    }

    public void addComponent(String id, List<String> softwareReqs, Hardware hardwareReqs, ArrayList<ThingRequirement> Theta) {
        S.add(new SoftwareComponent(id, softwareReqs, hardwareReqs, Theta));   
    }
    
    public void addComponent(String id, List<String> softwareReqs, Hardware hardwareReqs) {
        S.add(new SoftwareComponent(id, softwareReqs, hardwareReqs, new ArrayList<>()));   
    }
    
    @Override
    public String toString(){
        String result = "S = {\n";
        
        for (SoftwareComponent s: S){
            result+="\t"+s;
            result+="\n";
        }
        
        result+="}\n\nLambda = {\n";
        
        for (QoSProfile l : L.values()){
            result+="\t"+l;
            result+="\n";
        }
        
        result+="}";
        
        return result;
    }

//    public void addLink(String a, String b, QoSProfile q) {
//        L.put(new Couple(a,b), q);
//        L.put(new Couple(b,a), q);
//    }
//
//    public void addLink(String a, String b, QoSProfile q1, QoSProfile q2) {
//        L.put(new Couple(b,a), q1);
//        L.put(new Couple(a,b), q2);
//    }
}
