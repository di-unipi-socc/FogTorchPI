package di.unipi.socc.fogtorchpi.infrastructure;
import di.unipi.socc.fogtorchpi.utils.SecurityTaxonomy;

public class SecurityCounterMeasure {
    private String name;
    private String type;
    private int weight;

    public SecurityCounterMeasure(String securityMeasure){
        this.setName(securityMeasure);
        this.setType(SecurityTaxonomy.getType(securityMeasure));
        this.setWeight(1);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getName(){
        return this.name;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public int getWeight(){
        return this.weight;
    }

    public boolean equals(Object o){
        return name.equals((String)o);
    }

    public String toString(){
        return this.name;
    }


}