package fogtorch.infrastructure;

import fogtorch.application.ExactThing;
import fogtorch.application.SoftwareComponent;
import fogtorch.application.ThingRequirement;
import fogtorch.utils.Cost;
import fogtorch.utils.Coordinates;

/**
 *
 * @author stefano
 */
public class Thing {
    private final String identifier, type;
    private Coordinates coords;
    private Cost cost;
    
    public Thing(String identifier, String type, double x, double y){
        this.cost = new Cost(0.0);
        this.identifier = identifier;
        this.type = type;
        this.coords = new Coordinates(x,y);
    }
    
    
    public Thing(String identifier, String type, double x, double y, double cost) {
        this.cost = new Cost(cost);
        this.identifier = identifier;
        this.type = type;
        this.coords = new Coordinates(x,y);
    }
    
    public String getId(){
        return this.identifier;
    }
    
    public String getType(){
        return this.type;
    }
    
    public Coordinates getCoordinates(){
        return this.coords;
    }
    
    public void setCoordinates(Coordinates coords){
        this.coords = coords;
    }
    
    
    @Override
    public String toString(){
        String result = "<";
        result = result + this.identifier + ", " + this.type + ", "+ this.getCoordinates();        
        result += ">";
        return result; 
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
