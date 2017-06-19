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
public abstract class ThingRequirement {
    private QoSProfile qThingNode;
    private QoSProfile qNodeThing;
    
    public void ThingRequirement(QoSProfile qNodeThing , QoSProfile qThingNode ) {
        this.qThingNode = qThingNode;
        this.qNodeThing = qNodeThing;
    }
    
    public QoSProfile getQThingNode() {
        return qThingNode;
    }

    public QoSProfile getQNodeThing() {
        return qNodeThing;
    }
    
    public void setQThingNode(QoSProfile qThingNode ) {
       this.qThingNode = qThingNode;
    }

    public void setQNodeThing(QoSProfile qNodeThing) {
        this.qNodeThing = qNodeThing;   
    }
}
