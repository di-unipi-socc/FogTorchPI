 package fogtorch.pricing;

import fogtorch.utils.Hardware;

/**
 *
 * @author Stefano
 */
public class PricedHardware extends Hardware {
    public Hardware hardware;
    public Pricing ramCost;
    public Pricing cpuCost;
    public Pricing storageCost;

    public PricedHardware(Hardware r) {
        super(r);
    }
}
