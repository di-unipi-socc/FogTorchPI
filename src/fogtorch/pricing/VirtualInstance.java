/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.pricing;

import fogtorch.pricing.Pricing;
import fogtorch.utils.Hardware;
import fogtorch.utils.Software;
import java.util.ArrayList;

/**
 *
 * @author Stefano
 */
public class VirtualInstance {
    public Hardware hardware;
    public ArrayList<Software> software;
    //subscription based cost to run the VI during a month
    public Pricing prices;
}
