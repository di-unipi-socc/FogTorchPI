/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import fogtorch.application.SoftwareComponent;
import fogtorch.utils.Coordinates;
import fogtorch.utils.Couple;
import fogtorch.utils.QoSProfile;
import java.util.Objects;

/**
 *
 * @author stefano
 */
public abstract class ComputationalNode implements Comparable{
    private String identifier;
    private ArrayList<String> software;
    private Coordinates coords;
    public double heuristic;
    private boolean keepLight;

    
    public void setSoftware(Collection<String> software){
        this.software = new ArrayList<>(software);
    }
    
    public ArrayList<String> getSoftware(){
        return this.software;
    }
    
    public void setKeepLight(boolean kl){
        this.keepLight = kl;
    }
    
    public boolean getKeepLight(){
        return this.keepLight;
    }
    
    public String getId(){
        return this.identifier;
    }
    
    public void setId(String identifier){
        this.identifier = identifier;
    }
    
    public void setCoordinates(double x, double y){
        this.coords = new Coordinates(x,y);
    }
    
    public Coordinates getCoordinates(){
        return this.coords;
    }
    
    public double distance(ComputationalNode n){
        return getCoordinates().distance(n.getCoordinates());
    }
    
    public abstract boolean isCompatible(SoftwareComponent s);

    public abstract void deploy(SoftwareComponent s);
     
    public abstract void undeploy(SoftwareComponent s);
    
    public abstract double computeHeuristic(SoftwareComponent s); //, Coordinates deploymentLocation
    
    @Override
    public boolean equals(Object o){
        ComputationalNode n = (ComputationalNode) o;
        return n.identifier.equals(this.identifier);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.identifier);
        hash = 73 * hash + Objects.hashCode(this.software);
        hash = 73 * hash + Objects.hashCode(this.coords);
        return hash;
    }
    
    public boolean isReachable(String t, Infrastructure I, QoSProfile qNodeThing, QoSProfile qThingNode) {
        boolean reach = false;
        
        if (I.L.containsKey(new Couple<String,String>(this.getId(), t))){
            
            QoSProfile q1 = I.L.get(new Couple(this.getId(), t)); //nodeThing
            QoSProfile q2 = I.L.get(new Couple(t, this.getId())); //thingNode
            reach = q1.supports(qNodeThing) && q2.supports(qThingNode);
            //System.out.println(new Couple(this.getId(), t) + " "+ reach);
        }
        
        return reach; 
    }
    
    @Override
    public int compareTo(Object o) {
        ComputationalNode s2 = (ComputationalNode) o;
        return Double.compare(s2.heuristic, this.heuristic);
    }

        
}
