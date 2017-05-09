package fogtorch.pricing;

import fogtorch.utils.Hardware;
import fogtorch.utils.Software;
import java.util.ArrayList;

/**
 *
 * @author Stefano
 */
public class VirtualInstance {
    public String identifier;
    public Hardware hardware;
    public ArrayList<Software> software;
    //subscription based cost to run the VI during a month
    public Pricing prices;
}
