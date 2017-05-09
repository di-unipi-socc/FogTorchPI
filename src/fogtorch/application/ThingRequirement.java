/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.application;

import fogtorch.utils.QoS;

/**
 *
 * @author stefano
 */
public abstract class ThingRequirement {
    private QoS qThingNode;
    private QoS qNodeThing;
    
    public void ThingRequirement(QoS qNodeThing , QoS qThingNode ) {
        this.qThingNode = qThingNode;
        this.qNodeThing = qNodeThing;
    }
    
    public QoS getQThingNode() {
        return qThingNode;
    }

    public QoS getQNodeThing() {
        return qNodeThing;
    }
    
    public void setQThingNode(QoS qThingNode ) {
       this.qThingNode = qThingNode;
    }

    public void setQNodeThing(QoS qNodeThing) {
        this.qNodeThing = qNodeThing;   
    }
}
