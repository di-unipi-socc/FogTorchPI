/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.unipi.socc.fogtorchpi.infrastructure;

import di.unipi.socc.fogtorchpi.application.ExactThing;
import java.util.ArrayList;
import java.util.Collection;
import di.unipi.socc.fogtorchpi.application.SoftwareComponent;
import di.unipi.socc.fogtorchpi.application.ThingRequirement;
import di.unipi.socc.fogtorchpi.utils.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
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

    private List<SecurityCounterMeasure> securityMeasures;

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

    public List<SecurityCounterMeasure> getSecurityMeasures() {
        return securityMeasures;
    }

    public void setSecurityMeasures(List<String> sm) {
        List<SecurityCounterMeasure> measures = new ArrayList<SecurityCounterMeasure>();
        for(String measure : sm){
            SecurityCounterMeasure newSecurityMeasure = new SecurityCounterMeasure(measure);
            measures.add(newSecurityMeasure);
        }
        this.securityMeasures = measures;
    }

    public ArrayList<String> getSecurityMeasuresByType(String type){
        ArrayList<String> result = new ArrayList<>();
        for (SecurityCounterMeasure s : this.securityMeasures){
            if (SecurityTaxonomy.getSecurityMeasuresByType(type).contains(s)){
                result.add(s.toString());
            }
        }
        return result;
    }

    public int computeNodeSecurityScore(){
        int result = 0;
        for (SecurityCounterMeasure s: this.securityMeasures){
            result+=s.getWeight();
        }
        return result;
    }

    public int computeNodeSecurityScoreByType(String type){
        int result = 0;
        List<String> measures = SecurityTaxonomy.getSecurityMeasuresByType(type);
        for (SecurityCounterMeasure s: this.securityMeasures) {
            if (measures.contains(s.getName())) {
                result += s.getWeight();
            }
        }
        return result;
    }

    public boolean supportsSecurityRequirements(List<String> requirements){
        ArrayList<String> measures = new ArrayList<>();
        for (SecurityCounterMeasure s : this.securityMeasures){
            measures.add(s.toString());
        }
        return measures.containsAll(requirements);
    }
}
