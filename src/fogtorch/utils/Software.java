package fogtorch.utils;

import java.util.Objects;

public class Software {
    private String name;
    
    public Software (String name){
        this.name = name;
    }
    
    public Software (Software software2){
        this.name = software2.getName();
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
}
