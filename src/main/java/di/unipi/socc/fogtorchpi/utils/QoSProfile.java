package di.unipi.socc.fogtorchpi.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stefano
 */
public class QoSProfile {
    private int latency;
    private double bandwidth;

    private final String DISCRETE = "discrete";
    private final String UNIFORM = "uniform";
    private final String GAUSSIAN = "gaussian";


    public int latencyMin, latencyMax;
    public double bandwidthMin, bandwidthMax;
    public double latencyAvg, latencyVar;
    public double bandwidthAvg, bandwidthVar;
    public String distribType;

    public ArrayList<QoS> QoS;
    public double[] probabilities;





    private QoS bestQoS, worstQoS, avgQoS;

    public QoSProfile(int latency, double bandwidth) {
        distribType = DISCRETE; //Ok. according with the init.function the distribution changes....
        probabilities = new double[1];
        QoS = new ArrayList<>(1);

        this.latency = latency;
        this.bandwidth = bandwidth;

        this.latencyMin = latency;
        this.bandwidthMin = bandwidth;

        this.latencyMax = latency;
        this.bandwidthMax = bandwidth;

        this.bandwidthAvg = bandwidth;
        this.latencyAvg = latency;


        this.bestQoS = new QoS(latency, bandwidth);
        this.worstQoS = new QoS(latency, bandwidth);


        QoS.add(0, new QoS(latency, bandwidth));
        probabilities[0] = 1;

    }


    public QoSProfile(QoSProfile q) {
        this.QoS = q.QoS;
        this.probabilities = q.probabilities;
        this.distribType = q.distribType;
        this.bandwidthMin = q.bandwidthMin;
        this.bandwidthMax = q.bandwidthMax;
        this.latencyMin = q.latencyMin;
        this.latencyMax = q.latencyMax;
        this.bandwidthVar = q.bandwidthVar;
        this.bandwidthAvg = q.bandwidthAvg;
        this.latencyVar = q.latencyVar;
        this.latencyAvg = q.latencyAvg;

        this.bestQoS = q.bestQoS;
        this.worstQoS = q.worstQoS;

        this.sampleQoS();
    }

    public QoSProfile(List<Couple<QoS, Double>> qos) {

        distribType = DISCRETE;
        int size = qos.size();
        probabilities = new double[size];
        QoS = new ArrayList<>(size);

        //GA - changes
        int i = 0;
        this.latencyMin = Integer.MIN_VALUE;
        this.latencyMax = Integer.MAX_VALUE;
        this.bandwidthMin = Double.MIN_VALUE;
        this.bandwidthMax = Double.MAX_VALUE;

        for (Couple<QoS, Double> c : qos) {

            int lat = c.getA().getLatency();
            double bw = c.getA().getBandwidth();

            if (bandwidthMin > bw) {
                this.bandwidthMin = bw;
            }

            if (bandwidthMax < bw) {
                this.bandwidthMax = bw;
            }

            if (latencyMin > lat) {
                this.latencyMin = lat;
            }

            if (latencyMax < lat) {
                this.latencyMax = lat;
            }


            this.latencyAvg += lat * c.getB();
            this.bandwidthAvg += bw * c.getB();


            QoS.add(i, qos.get(i).getA());
            probabilities[i] = qos.get(i).getB();
            i++;
        }

        this.bestQoS = this.findBestQoS();
        this.worstQoS = this.findWorstQoS();

    }

    private QoS findBestQoS() {
        QoS bestQoS = this.QoS.get(0);

        for (int i = 1; i < this.QoS.size(); i++) {
            QoS currentQoS = this.QoS.get(i);
            if (currentQoS.evaluateQoS(this.latencyMax, this.bandwidthMin) > bestQoS.evaluateQoS(this.latencyMax, this.bandwidthMin)) {
                bestQoS = currentQoS;
            }
        }
        return bestQoS;
    }

    private QoS findWorstQoS() {
        QoS worstQoS = this.QoS.get(0);
        for (int i = 1; i < this.QoS.size(); i++) {
            QoS currentQoS = this.QoS.get(i);
            if (currentQoS.evaluateQoS(this.latencyMax, this.bandwidthMin) < worstQoS.evaluateQoS(this.latencyMax, this.bandwidthMin)) {
                worstQoS = currentQoS;
            }
        }
        return worstQoS;
    }


    public final void bestQoS() {
        this.latency = bestQoS.getLatency();
        this.bandwidth = bestQoS.getBandwidth();
    }

    public final void worstQoS() {
        this.latency = worstQoS.getLatency();
        this.bandwidth = worstQoS.getBandwidth();
    }

    public final void avgQoS() {

        latency = (int) latencyAvg;
        bandwidth = bandwidthAvg;
    }


    public QoSProfile(int latencyMin, int latencyMax, double bwMin, double bwMax) {
        distribType = UNIFORM;
        this.latencyMin = latencyMin;
        this.latencyMax = latencyMax;
        this.bandwidthMin = bwMin;
        this.bandwidthMax = bwMax;

        this.sampleQoS();
    }

    public QoSProfile(int latencyAvg, int latencyVar, double bwAvg, double bwVar, boolean g) {
        distribType = GAUSSIAN;
        this.latencyAvg = latencyAvg;
        this.latencyVar = latencyVar;
        this.bandwidthAvg = bwAvg;
        this.bandwidthVar = bwVar;
        this.sampleQoS();
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getLatency() {
        return this.latency;
    }

    public double getBandwidth() {
        return this.bandwidth;
    }


    public final void sampleQoS() {


        if (distribType.equals(DISCRETE)) {

            int sample = StdRandom.discrete(probabilities);

            latency = QoS.get(sample).getLatency();
            bandwidth = QoS.get(sample).getBandwidth();

            //how does both values (lat,bw) affect into the generation of a deployment??? :S

        } else if (distribType.equals(UNIFORM)) {
            latency = StdRandom.uniform(latencyMin, latencyMax + 1);
            bandwidth = new Double(StdRandom.uniform((int) bandwidthMin, (int) bandwidthMax + 1));
        } else if (distribType.equals(GAUSSIAN)) {
            latency = (int) (StdRandom.gaussian(latencyAvg, latencyVar));
            bandwidth = StdRandom.gaussian(bandwidthAvg, bandwidthVar);
        }
        //System.out.println("Sampling: " + this);
    }


    @Override
    public String toString() {
        return "<" + latency + ", " + bandwidth + ">";
    }

    public boolean supports(QoSProfile q) {
        boolean result = false;
        if (latency <= q.getLatency() && bandwidth >= q.getBandwidth())
            result = true;
        return result;
    }

}
