package examples;

import edu.princeton.cs.introcs.StdOut;
import fogtorch.utils.Couple;
import fogtorch.utils.PQoSProfile;
import fogtorch.utils.QoSProfile;
import static java.util.Arrays.asList;

/**
 *
 * @author Stefano
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       PQoSProfile q = new PQoSProfile(asList(
               new Couple(new QoSProfile(10, 5.0), 0.5), 
               //new Couple(new QoSProfile(15, 6.0), 0.2), 
               new Couple(new QoSProfile(30, 3.0), 0.5))); 
       
       for (int i =0; i<15; i++){
           q.sampleQoS();
           StdOut.println(q);
       }
    }
    
}
