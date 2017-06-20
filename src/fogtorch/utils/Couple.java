/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.utils;

import java.util.Objects;

/**
 *
 * @author stefano
 */

public class Couple<F, S> {
    private F a; //first member of pair
    private S b; //second member of pair

    public Couple(F first, S second) {
        this.a = first;
        this.b = second;
    }

    public void setA(F first) {
        this.a = first;
    }

    public void setB(S second) {
        this.b = second;
    }

    public F getA() {
        return a;
    }

    public S getB() {
        return b;
    }

    @Override
    public boolean equals(Object c){
        if (c.getClass() == Couple.class){
            Couple k = (Couple)c;
            return this.a.equals(k.getA()) && this.b.equals(k.getB());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.a);
        hash = 97 * hash + Objects.hashCode(this.b);
        return hash;
    }
    
    @Override
    public String toString(){
        return  a + ", "+ b ;
    }

}
