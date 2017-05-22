package fogtorch.utils;

import java.util.Objects;

/**
 *
 * @author Stefano
 */
public class Cost {
    private double monthlyCost;
    private String currency = "€";
    
    public Cost(double monthlyCost){
        this.monthlyCost = monthlyCost;
        this.currency = "€";
    }
    
    public Cost(double monthlyCost, String currency){
        this.monthlyCost = monthlyCost;
        this.currency = currency;
    }
    
    public double getCost(){
        return this.monthlyCost;
    }
    
    public String getCurrency(){
        return this.currency;
    }
    
    public void setCost(double newCost){
        this.monthlyCost = newCost;
    }
    
    public void setCurrency(String newCurrency){
        this.currency = newCurrency;
    }
    
    @Override
    public String toString(){
        return currency + " " + this.monthlyCost;
    }
    
    @Override
    public boolean equals(Object o){
        Cost cost = ((Cost)o);
        return cost.currency.equals(this.currency) 
                && cost.monthlyCost == this.monthlyCost;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.monthlyCost) ^ (Double.doubleToLongBits(this.monthlyCost) >>> 32));
        hash = 17 * hash + Objects.hashCode(this.currency);
        return hash;
    }
    
    public void add(Cost c){
        if (c.getCurrency().equals(this.currency)){
            this.monthlyCost += c.getCost();
        }
    }
}
