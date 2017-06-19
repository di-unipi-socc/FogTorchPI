package fogtorch.utils;

import fogtorch.application.SoftwareComponent;
import java.util.Objects;

public class Software {
    private String name;
    private Cost cost;
    
    public Software (String name){
        this.name = name;
        this.cost = new Cost(0.0);
    }
    
    public Software (Software software2){
        this.name = software2.getName();
        this.cost = new Cost(software2.getCost());
    }

    public Software(String r, double cost) {
        this.name = r;
        this.cost = new Cost(cost);
    }

    @Override
    public boolean equals(Object o){
        Software s = (Software) o;
        return s.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString(){
        return this.name;
    }

    
    public void setCost(double cost){
        this.cost = new Cost(cost);
    }
    
    public double getCost(){
        return cost.getCost();
    }
    
    public double getMonthlyCost(SoftwareComponent s){
        if (s.getSoftwareRequirements().contains(s.getId())){
            return cost.getCost();
        }
        return 0.0;
    }
}
