/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.infrastructure;

import fogtorch.application.SoftwareComponent;
import fogtorch.deployment.Deployment;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import fogtorch.utils.Couple;
import fogtorch.utils.Hardware;
import fogtorch.utils.QoSProfile;

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

    public void addCloudDatacentre(String identifier, List<String> software, double x, double y) {
        C.put(identifier,new CloudDatacentre(identifier, software, x, y));
        L.put(new Couple(identifier,identifier), new QoSProfile(0, Double.MAX_VALUE));
    }

    public void addFogNode(String identifier, List<String> software, Hardware hardware, double x, double y) {
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
    
    public void addLink(String a, String b, int latency, double bandwidth) {
        L.put(new Couple(a,b), new QoSProfile(latency,bandwidth));
        L.put(new Couple(b,a), new QoSProfile(latency,bandwidth));
    }

    public void addLink(String a, String b, int latency, double bandwidthba, double bandwidthab) {
        L.put(new Couple(a,b), new QoSProfile(latency,bandwidthab));
        L.put(new Couple(b,a), new QoSProfile(latency,bandwidthba));
    }
    
    public void addLink(String a, String b, QoSProfile q) {
        L.put(new Couple(a,b), q);
        L.put(new Couple(b,a), new QoSProfile(q.getLatency(), q.getBandwidth()));
    }

    public void addLink(String a, String b, QoSProfile downlinkba, QoSProfile uplinkab) { //q1 dwn uplinkab upl
        L.put(new Couple(b,a), downlinkba);
        L.put(new Couple(a,b), uplinkab);
    }
    
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
