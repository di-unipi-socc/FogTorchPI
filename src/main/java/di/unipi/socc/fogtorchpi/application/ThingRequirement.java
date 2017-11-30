package di.unipi.socc.fogtorchpi.application;

import di.unipi.socc.fogtorchpi.utils.QoSProfile;

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
