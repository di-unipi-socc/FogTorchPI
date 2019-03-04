package di.unipi.socc.fogtorchpi.application;

import di.unipi.socc.fogtorchpi.utils.QoSProfile;

/**
 *
 * @author stefano
 */
public class ExactThing extends ThingRequirement implements Cloneable{
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

    public Object clone() throws CloneNotSupportedException{
        return new ExactThing(this.id, new QoSProfile(this.getQNodeThing()), new QoSProfile(this.getQThingNode()), this.monthlyInvoke);
    }
}
