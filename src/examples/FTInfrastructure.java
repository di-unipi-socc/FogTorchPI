/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import fogtorch.infrastructure.Infrastructure;
import fogtorch.utils.Hardware;
import static java.util.Arrays.asList;

/**
 *
 * @author Stefano
 */
public class FTInfrastructure {
    public static Infrastructure createInfrastructure(){
        Infrastructure I = new Infrastructure();
        //fog1 43.7464449,10.4615923 fog2 43.7381285,10.4552213
        I.addCloudDatacentre("cloud_1", asList("java", ".NETcore", "ruby", "mySQL"), 52.195097, 3.0364791);
        I.addCloudDatacentre("cloud_2", asList("spark", "mySQL", "linux", "windows", "python", "c++", ".NETcore"), 44.123896, -122.781555);
        //Fog nodes
        I.addFogNode("fog_3", asList("python", "c++", "mySQL", ".NETcore", "linux"), new Hardware(0, 10, 0), 43.740186, 10.364619);
        I.addFogNode("fog_1", asList("c++", "linux", "python"), new Hardware(0, 2, 0), 43.7464449, 10.4615923);
        I.addFogNode("fog_2", asList("c++", "linux", "python"), new Hardware(0, 4, 0), 43.7381285, 10.4552213);
        //Links
        I.addLink("fog_1", "fog_2", 1, 100);
        I.addLink("fog_1", "fog_3", 5, 20);
        I.addLink("fog_2", "fog_3", 5, 20);
        I.addLink("fog_1", "cloud_1", 130, 8, 6);
        I.addLink("fog_1", "cloud_2", 200, 12, 10);
        I.addLink("fog_2", "cloud_1", 100, 12, 8);
        I.addLink("fog_2", "cloud_2", 180, 15, 11);
        I.addLink("fog_3", "cloud_1", 35, 60, 18);
        I.addLink("fog_3", "cloud_2", 45, 65, 18);
        //Things for local
        I.addThing("videocamera0", "videocamera", 43.7464449, 10.4615923, "fog_1");
        I.addThing("fire0", "fire", 43.7464449, 10.4615923, "fog_1");
        I.addThing("extinguisher0", "extinguisher", 43.7464449, 10.4615923, "fog_1");

        
        I.addThing("wind0", "wind", 43.740186, 10.364619, "fog_3");
        I.addThing("pressure0", "pressure", 43.740186, 10.364619, "fog_3");
        I.addThing("temperature0", "temperature", 43.740186, 10.364619, "fog_3");

        I.addThing("water1", "water", 43.7381285, 10.4552213, "fog_2");
        I.addThing("fertiliser1", "fertiliser", 43.7381285, 10.4552213, "fog_2");
        I.addThing("extinguisher1", "extinguisher", 43.7381285, 10.4552213, "fog_2");
        I.addThing("fire1", "fire", 43.7381285, 10.4552213, "fog_2");
        
        return I;
    }
}
