package di.unipi.socc.fogtorchpi.application;

import di.unipi.socc.fogtorchpi.utils.Coordinates;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;

/**
 *
 * @author stefano
 */
public class AnyThing extends ThingRequirement implements Cloneable{
    private Coordinates coord;
    private double maxDist;
    private String type;
    
    public AnyThing(String type, double x, double y, double maxDist, QoSProfile q){
        this.type = type;
        coord = new Coordinates(x,y);
        this.maxDist = maxDist;
        //super.setQ(q);
    }

    
    public Coordinates getCoordinates(){
        return this.coord;
    }
    
    public double getDistance(){
        return maxDist;
    }
    
    public String getType(){
        return this.type;
    }
    
    @Override
    public String toString(){
        return type +", " + coord + ", " + maxDist;
    }

    public Object clone() throws CloneNotSupportedException {
        return new AnyThing(this.type, this.coord.getLatitude(), this.coord.getLongitude(), this.maxDist, new QoSProfile(10, 4,4,2));
    }

}
