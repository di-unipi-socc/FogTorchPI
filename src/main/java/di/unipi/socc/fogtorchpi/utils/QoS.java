package di.unipi.socc.fogtorchpi.utils;

/**
 *
 * @author stefano
 */
public class QoS {
    private int latency;
    private double bandwidth;
     
    
    public QoS (int latency, double bandwidth){
        this.latency = latency;
        this.bandwidth = bandwidth;
    }
    
    public void setLatency (int latency){
        this.latency = latency;
    }
    
    public void setBandwidth(double bandwidth){
        this.bandwidth = bandwidth;
    }
    
    public int getLatency(){
        return this.latency;
    }
    
    public double getBandwidth(){
        return this.bandwidth;
    }
    
    @Override
    public String toString(){
        return "<" + latency + ", " + bandwidth + ">";
    }
    
    public double evaluateQoS(int maxLatency, double minBandwidth){
        double result = 0.0;
        
        double latencyEval = (((double) maxLatency) - ((double) this.latency))  / ((double) maxLatency);
        double bandwidthEval = (this.bandwidth - minBandwidth) / this.bandwidth;
        
        result = 0.5 * latencyEval + 0.5 * bandwidthEval;

        
        return result;
      
    }
    
    
    public boolean supports(QoS q){
        boolean result = false;
        if (latency <= q.getLatency() && bandwidth >= q.getBandwidth())
            result = true;
        return result;
    }

}
