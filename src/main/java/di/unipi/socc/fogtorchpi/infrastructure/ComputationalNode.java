/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.unipi.socc.fogtorchpi.infrastructure;

import java.util.Collection;
import di.unipi.socc.fogtorchpi.application.SoftwareComponent;
import di.unipi.socc.fogtorchpi.utils.*;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author stefano
 */
public abstract class ComputationalNode implements Comparable {

    private String identifier;
    private HashMap<String, Software> software;
    private Coordinates coords;
    private Hardware hw;
    public double heuristic;
    private boolean keepLight;

    public ComputationalNode() {

    }

    public void setSoftware(Collection<Couple<String, Double>> software) {
        this.software = new HashMap<>();
        for (Couple s : software) {
            Software value = new Software(((Couple) s).getA().toString(), (double) ((Couple) s).getB());
            this.software.put(((Couple) s).getA().toString(), value);
        }
    }

    public void setHardware(Hardware h) {
        hw = new Hardware(h);
    }

    public Hardware getHardware() {
        return hw;
    }

    public HashMap<String, Software> getSoftware() {
        return this.software;
    }

    public void setKeepLight(boolean kl) {
        this.keepLight = kl;
    }

    public boolean getKeepLight() {
        return this.keepLight;
    }

    public String getId() {
        return this.identifier;
    }

    public void setId(String identifier) {
        this.identifier = identifier;
    }

    public void setCoordinates(double x, double y) {
        this.coords = new Coordinates(x, y);
    }

    public Coordinates getCoordinates() {
        return this.coords;
    }

    public double distance(ComputationalNode n) {
        return getCoordinates().distance(n.getCoordinates());
    }

    public abstract boolean isCompatible(SoftwareComponent s);

    public abstract void deploy(SoftwareComponent s);

    public abstract void undeploy(SoftwareComponent s);
    
    public abstract Cost computeCost(SoftwareComponent s, Infrastructure I);

    public abstract double computeHeuristic(SoftwareComponent s); //, Coordinates deploymentLocation

    @Override
    public boolean equals(Object o) {
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

        if (I.L.containsKey(new Couple<String, String>(this.getId(), t))) {

            QoSProfile q1 = I.L.get(new Couple(this.getId(), t)); //nodeThing
            QoSProfile q2 = I.L.get(new Couple(t, this.getId())); //thingNode
            reach = q1.supports(qNodeThing) && q2.supports(qThingNode);
        }

        return reach;
    }

    @Override
    public int compareTo(Object o) {
        ComputationalNode s2 = (ComputationalNode) o;
        return Double.compare(s2.heuristic, this.heuristic);
    }
    
    public abstract boolean connectsToThing(String thingId);


}
