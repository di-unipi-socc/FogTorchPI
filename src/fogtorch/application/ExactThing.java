/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.application;

import fogtorch.utils.QoSProfile;

/**
 *
 * @author stefano
 */
public class ExactThing extends ThingRequirement{
    private String id;
    private int monthlyInvoke;

    public ExactThing(String type, QoSProfile qNodeThing, QoSProfile qThingNode) {
        this.id = type;
        this.monthlyInvoke = 0;
        super.setQNodeThing(qNodeThing);
        super.setQThingNode(qThingNode);
    }
    
    public ExactThing(String type, QoSProfile qNodeThing, QoSProfile qThingNode, int monthlyInvoke) {
        this.id = type;
        this.monthlyInvoke = monthlyInvoke;
        super.setQNodeThing(qNodeThing);
        super.setQThingNode(qThingNode);
    }
    
    public String getId(){
        return id;
    }
    
    
    
    public int getMonthlyInvoke(){
        return this.monthlyInvoke;
    }

    public String toString(){
        return "(" + id + ", "+super.getQNodeThing()+ " " + super.getQThingNode()+")" ;
    }
}
