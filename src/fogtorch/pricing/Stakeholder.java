package fogtorch.pricing;

/**
 *
 * @author Stefano
 */
public class Stakeholder {
    public String identifier;
    
    public Stakeholder(String identifier){
        this.identifier = identifier;
    }
    
    public String getIdentifier(){
        return this.identifier;
    }
}
