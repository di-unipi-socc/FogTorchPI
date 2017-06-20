package di.unipi.socc.fogtorchpi.application;

import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;
import di.unipi.socc.fogtorchpi.application.SoftwareComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
    

}
