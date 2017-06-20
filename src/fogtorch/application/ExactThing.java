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

    public ExactThing(String type, QoSProfile qNodeThing, QoSProfile qThingNode) {
        this.id = type;
        super.setQNodeThing(qNodeThing);
        super.setQThingNode(qThingNode);
    }
    
    public String getId(){
        return id;
    }

    public String toString(){
        return "(" + id + ", "+super.getQNodeThing()+ " " + super.getQThingNode()+")" ;
    }
}
