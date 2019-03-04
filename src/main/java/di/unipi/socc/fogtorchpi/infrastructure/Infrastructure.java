package di.unipi.socc.fogtorchpi.infrastructure;

import di.unipi.socc.fogtorchpi.infrastructure.FogNode;
import di.unipi.socc.fogtorchpi.application.SoftwareComponent;
import di.unipi.socc.fogtorchpi.deployment.Deployment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;

/**
 *
 * @author stefano
 */
public class Infrastructure {
    public HashMap<String,CloudDatacentre> C;
    public HashMap<String,FogNode> F;
    public HashMap<String,Thing> T;
    public HashMap<Couple, QoSProfile> L;
    
    public Infrastructure(){
        C = new HashMap<>();
        F = new HashMap<>();
        T = new HashMap<>();
        L = new HashMap<>();
    }
    public Infrastructure clone(){
        Infrastructure n = new Infrastructure();
        n.C = new HashMap<String,CloudDatacentre>();
//        n.C.putAll(this.C);
        n.F = new HashMap<String, FogNode> ();
        Iterator it = this.C.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            n.C.put((String)pair.getKey(),((CloudDatacentre)pair.getValue()).clone());
//            it.remove(); // avoids a ConcurrentModificationException
        }

        n.T = new HashMap<String, Thing> (this.T);
        n.L = new HashMap<Couple, QoSProfile> (this.L);
        return n;


    }




    public void addCloudDatacentre(String identifier, List<Couple<String, Double>> software, double x, double y, Hardware h) {
        C.put(identifier,new CloudDatacentre(identifier, software, x, y, h));
        L.put(new Couple(identifier,identifier), new QoSProfile(0, Double.MAX_VALUE));
    }
    
    public void addCloudDatacentre(String identifier, List<Couple<String, Double>> software, double x, double y) {
        Hardware h = new Hardware(0,0,0);
        C.put(identifier,new CloudDatacentre(identifier, software, x, y, h));
        L.put(new Couple(identifier,identifier), new QoSProfile(0, Double.MAX_VALUE));
    }
    
    public void addCloudDatacentre(String identifier, List<Couple<String, Double>> software, double x, double y, Hardware h, List<Couple<String, Double>> vmTypes) {
        C.put(identifier,new CloudDatacentre(identifier, software, x, y, h, vmTypes));
        L.put(new Couple(identifier,identifier), new QoSProfile(0, Double.MAX_VALUE));
    }

    public void addFogNode(String identifier, List<Couple<String, Double>> software, Hardware hardware, double x, double y) {
        F.put(identifier,new FogNode(identifier, software, hardware, x, y));
        L.put(new Couple(identifier,identifier), new QoSProfile(0, Double.MAX_VALUE));
    }
    
    public void addThing(String identifier, String type, double x, double y, String fogNode) {   
        Thing t = new Thing(identifier, type, x, y);
        T.put(identifier, t);
        FogNode f = F.get(fogNode);
        Set<Couple> allLinks = new HashSet(L.keySet()); //all available links
        for (Couple l: allLinks){ 
            if (l.getA().equals(fogNode)){
                String fogNode2 = (String) l.getB();
                if(F.containsKey(fogNode2) && !fogNode2.equals(fogNode)){    
                    QoSProfile r = L.get(l);
                    L.put(new Couple(identifier, fogNode2), r);
                    QoSProfile r2 = L.get(new Couple(fogNode2, fogNode));
                    L.put(new Couple(fogNode2,identifier), r2);
                }
                if(C.containsKey(fogNode2) && !fogNode2.equals(fogNode)){  
                    QoSProfile r = L.get(l);
                    L.put(new Couple(identifier, fogNode2), r);
                    QoSProfile r2 = L.get(new Couple(fogNode2, fogNode));
                    L.put(new Couple(fogNode2,identifier), r2);
                }
            }
        } 
        addLink(identifier, fogNode, 0, Double.MAX_VALUE);
        addLink(fogNode, identifier, 0, Double.MAX_VALUE);
        f.connectedThings.add(identifier);
    }
    
    public void addThing(String identifier, String type, double x, double y, String fogNode, double cost) {   
        Thing t = new Thing(identifier, type, x, y, cost);
        T.put(identifier, t);
        FogNode f = F.get(fogNode);
        Set<Couple> allLinks = new HashSet(L.keySet()); //all available links
        for (Couple l: allLinks){ 
            if (l.getA().equals(fogNode)){
                String fogNode2 = (String) l.getB();
                if(F.containsKey(fogNode2) && !fogNode2.equals(fogNode)){    
                    QoSProfile r = L.get(l);
                    L.put(new Couple(identifier, fogNode2), r);
                    QoSProfile r2 = L.get(new Couple(fogNode2, fogNode));
                    L.put(new Couple(fogNode2,identifier), r2);
                }
                if(C.containsKey(fogNode2) && !fogNode2.equals(fogNode)){  
                    QoSProfile r = L.get(l);
                    L.put(new Couple(identifier, fogNode2), r);
                    QoSProfile r2 = L.get(new Couple(fogNode2, fogNode));
                    L.put(new Couple(fogNode2,identifier), r2);
                }
            }
        } 
        addLink(identifier, fogNode, 0, Double.MAX_VALUE);
        addLink(fogNode, identifier, 0, Double.MAX_VALUE);
        f.connectedThings.add(identifier);
    }
    
    public void addLink(String a, String b, int latency, double bandwidth) {
        L.put(new Couple(a,b), new QoSProfile(latency,bandwidth));
        L.put(new Couple(b,a), new QoSProfile(latency,bandwidth));
    }

    public void addLink(String a, String b, int latency, double bandwidthba, double bandwidthab) {
        L.put(new Couple(a,b), new QoSProfile(latency,bandwidthab));
        L.put(new Couple(b,a), new QoSProfile(latency,bandwidthba));
    }
    
    public void addLink(String a, String b, QoSProfile q) {
        L.put(new Couple(a,b), new QoSProfile(q));
        L.put(new Couple(b,a), new QoSProfile(q));
    }

    public void addLink(String a, String b, QoSProfile downlinkba, QoSProfile uplinkab) { 
        L.put(new Couple(b,a), new QoSProfile(downlinkba));
        L.put(new Couple(a,b), new QoSProfile(uplinkab));
    }

    /**
    It returns the percentage of RAM and HDD consumed by deployment d as a Couple.
    */
    public Couple<Double, Double> consumedResources(Deployment d){
        
        Couple<Double, Double> consumedResources = new Couple(0.0,0.0);
        Couple<Double, Double> allResources = new Couple(0.0,0.0);
        
        for (FogNode g : F.values()){
                allResources.setA(g.getHardware().ram + allResources.getA());
                allResources.setB(g.getHardware().storage + allResources.getB());     
        }
        
        for (SoftwareComponent s: d.keySet()){
            if (F.containsKey(d.get(s).getId())){
                FogNode f = (FogNode) d.get(s); 
                Couple<Double, Double> tmp = f.consumedResources(s);
                
                consumedResources.setA(tmp.getA() + consumedResources.getA());
                consumedResources.setB(tmp.getB() + consumedResources.getB());
            }
        }
        
        return new Couple(consumedResources.getA()/allResources.getA(),
                            consumedResources.getB()/allResources.getB());
    }
    
     /** It returns the percentage of RAM and HDD consumed by deployment d
    in the nodes indicated in a list.
    */
    public Couple<Double, Double> consumedResources(Deployment d, List<String> nodes){
        
        Couple<Double, Double> consumedResources = new Couple(0.0,0.0);
        Couple<Double, Double> allResources = new Couple(0.0,0.0);
        
        
        for (FogNode g : F.values()){
            if(nodes.contains(g.getId())){
                allResources.setA(g.getHardware().ram+allResources.getA());
                allResources.setB(g.getHardware().storage + allResources.getB());
            }
        }
        
        for (SoftwareComponent s: d.keySet()){
            ComputationalNode m = d.get(s);
            if (F.containsKey(m.getId()) && nodes.contains(m.getId())){
                //update consumed resources
                FogNode f = (FogNode) d.get(s);
                Couple<Double, Double> tmp = f.consumedResources(s);
                consumedResources.setA(tmp.getA() + consumedResources.getA());
                consumedResources.setB(tmp.getB() + consumedResources.getB());
            }
        }
        double consumed1, consumed2;
        if(allResources.getA() != 0)
             consumed1 = consumedResources.getA()/allResources.getA();
        else 
            consumed1 = 0.0;
        
        if (allResources.getB() != 0)
            consumed2 = consumedResources.getB()/allResources.getB();
        else
            consumed2 = 0.0;
            return new Couple(consumed1, consumed2);
    }

    @Override
    public String toString(){
        String result = "C = {\n";
        
        for (ComputationalNode c: C.values()){
            result+="\t"+c;
            result+="\n";
        }
        
        result+="}\n\nF = {\n";
        
        
        for (ComputationalNode f : F.values()){
            result+="\t"+f;
            result+="\n";
        }
        
        result+="}\n\nT = {\n";
        
        
        for (Thing t : T.values()){
            result+="\t"+t;
            result+="\n";
        }
        
        result+="}\n\nL = {\n";
        
        
        for (Entry l : L.entrySet()){
            result+="\t"+l;
            result+="\n";
        }
        
        result+="}";
        
        return result;
    }
    
}
