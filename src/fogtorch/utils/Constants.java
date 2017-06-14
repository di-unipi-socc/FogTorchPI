/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.utils;

/**
 *
 * @author Stefano
 */
public class Constants {
    public static final int MAX_RAM = 4;
    public static final int MAX_HDD = 128;
    public static final int MAX_CORES = 8;
    
    
   public static Hardware getVMHardwareSpec(String vmName){  
        Hardware result = null;
        switch (vmName) {
                case "tiny": 
                    result = new Hardware(1, 1, 10);
                    break;
                case "small": 
                    result = new Hardware(1, 2, 20);
                    break;
                case "medium": 
                    result = new Hardware(2, 4, 40);
                    break;
                case "large": 
                    result = new Hardware(4, 8, 80);
                    break;
                case "xlarge": 
                    result = new Hardware(8, 16, 160);
                    break;
                default:
                         break;
            }
        return result;
   }
    
}
