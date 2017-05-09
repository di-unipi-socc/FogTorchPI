package fogtorch.utils;

import edu.princeton.cs.introcs.StdRandom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stefano
 */
public class QoSProfile {
    public int latency;
    public double bandwidth;
    
    public ArrayList<QoS> QoS;
    public double[] probabilities;
    
    public QoSProfile(int latency, double bandwidth) {
        probabilities = new double[1];
        probabilities[0] = 1;
    }
    
    public QoSProfile(List<Couple<QoS, Double>> qos){
        int size = qos.size();
        probabilities = new double[size];
        QoS = new ArrayList<>(size);
        int i = 0;
        for (Couple c : qos){
            QoS.add(i, qos.get(i).getA());
            probabilities[i] = qos.get(i).getB();
            i++;
        }
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
    
    
    public void sampleQoS(){
        int sample = StdRandom.discrete(probabilities);
        latency = QoS.get(sample).getLatency();
        bandwidth = QoS.get(sample).getBandwidth();
    }
    
    @Override
    public String toString(){
        return "<" + latency + ", " + bandwidth + ">";
    }
    
    public boolean supports(QoS q){
        boolean result = false;
        if (latency <= q.getLatency() && bandwidth >= q.getBandwidth())
            result = true;
        return result;
    }
    
}
