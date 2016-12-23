/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.application;

import fogtorch.utils.Coordinates;
import fogtorch.utils.QoSProfile;

/**
 *
 * @author stefano
 */
public class AnyThing extends ThingRequirement{
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
}
