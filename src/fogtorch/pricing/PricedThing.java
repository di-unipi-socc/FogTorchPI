/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.pricing;

import fogtorch.application.ExactThing;
import fogtorch.application.SoftwareComponent;
import fogtorch.application.ThingRequirement;
import fogtorch.infrastructure.Thing;

/**
 *
 * @author Stefano
 */
public class PricedThing extends Thing {
    private Cost cost;
    
    public PricedThing(String identifier, String type, double x, double y) {
        super(identifier, type, x, y);
        this.cost = new Cost(0.0);
    }

    public PricedThing(String identifier, String type, double x, double y, double cost) {
        super(identifier, type, x, y);
        this.cost = new Cost(cost);
    }
    
    public double getMonthlyCost(SoftwareComponent s){
        
        for (ThingRequirement t : s.getThingsRequirements()){
            if (t.getClass().equals(ExactThing.class)){
                if (((ExactThing)t).getId().equals(this.getId())){
                    return this.cost.getCost();
                }
            }
        }
        
        return 0.0;
    }
    
    
}
