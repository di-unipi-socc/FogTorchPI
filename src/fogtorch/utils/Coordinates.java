/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.utils;

public class Coordinates {
    private double lat, lng;

    public Coordinates (double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }
    
    public double distance (Coordinates coords){
        final int R = 6371; // Radius of the earth
        double lat1 = this.lat;
        double lat2 = coords.getLatitude();
        double lon1 = this.lng;
        double lon2 = coords.getLongitude();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    private double getLatitude() {
        return this.lat;
    }

    private double getLongitude() {
        return this.lng;
    }
    
    public String toString(){
        return "("+ this.lat + ", " + this.lng + ")";
    }
}
