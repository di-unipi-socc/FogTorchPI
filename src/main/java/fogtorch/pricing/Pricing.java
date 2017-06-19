package fogtorch.pricing;

import fogtorch.utils.Cost;
import fogtorch.utils.Couple;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Stefano
 */
public class Pricing {
    public HashMap<Stakeholder, Cost> price;
    
    public Pricing(List<Couple<Stakeholder, Cost>> prices){
        price = new HashMap<>();
        for (Couple<Stakeholder, Cost>  p : prices){
            this.price.put(p.getA(), p.getB());
        }
    }
}
