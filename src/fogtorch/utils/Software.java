/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.utils;

import java.util.Objects;

/**
 *
 * @author Stefano
 */
public class Software {
    public String name;
    
    public Software (String name){
        this.name = name;
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
}
