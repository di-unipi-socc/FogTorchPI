package examples;

import edu.princeton.cs.introcs.StdOut;
import fogtorch.utils.Couple;
import fogtorch.utils.QoS;
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
       QoSProfile q = new QoSProfile(asList(new Couple(new QoS(10, 5.0), 0.3), 
               new Couple(new QoS(15, 6.0), 0.2), 
               new Couple(new QoS(30, 3.0), 0.5))); 
       
       
       QoSProfile q1 = new QoSProfile(10, 5.0);
       StdOut.println(q1);
       
       for (int i =0; i<15; i++){
           q.sampleQoS();
           StdOut.println(q);
           
       }
    }
    
}
