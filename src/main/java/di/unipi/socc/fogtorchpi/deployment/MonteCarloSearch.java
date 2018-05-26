package di.unipi.socc.fogtorchpi.deployment;

import di.unipi.socc.fogtorchpi.application.Application;
import di.unipi.socc.fogtorchpi.infrastructure.Infrastructure;
import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Stefano
 */
public class MonteCarloSearch implements Runnable {

    private int times;
    public Application A;
    private Infrastructure I;

    List<String> fogNodes;
    ConcurrentHashMap<Deployment, Couple<Double, Double>> histogram;

    private HashMap<String, HashSet<String>> businessPolicies;
    private Search search;

    public  HashMap<String, HashSet<String>> getBusinessPolicies(){
        return this.businessPolicies;
    }

    public MonteCarloSearch(
            int times,
            Application A,
            Infrastructure I,
            List<String> fogNodes,
            ConcurrentHashMap<Deployment, Couple<Double, Double>> histogram) {

        this.times = times;
        this.A = A;
        this.I = I;
        this.fogNodes = fogNodes;
        this.histogram = histogram;
        businessPolicies = new HashMap<String, HashSet<String>>();
    }

    public void addBusinessPolicies(String component, List<String> allowedNodes) {
        HashSet<String> policies = new HashSet<>();
        policies.addAll(allowedNodes);
        this.businessPolicies.put(component, policies);
    }

    public void startSimulation(List<String> fogNodes, ConcurrentHashMap<Deployment, Couple<Double, Double>>  histogram) {

        for (int j = 0; j < times; j++) {

            search = new Search(A, I, businessPolicies);

            for (Couple c: I.L.keySet()) {
                I.L.get(c).sampleQoS();
            }


            search.findDeployments(true);

            double pos = search.D.size();
            double size = search.D.size();

            for (Deployment d : search.D) {
                if (histogram.containsKey(d)) {
                    Double newCount = histogram.get(d).getA() + 1.0; //montecarlo frequency
                    Double newPos = histogram.get(d).getB() + (pos / size); //
                    histogram.replace(d, new Couple(newCount, newPos));
                } else {
                    histogram.put(d, new Couple(1.0, pos / size));
                }
                pos--;
            }
        }

        for (Deployment dep : histogram.keySet()) {
            if (!fogNodes.isEmpty()) {
                dep.consumedResources = I.consumedResources(dep, fogNodes);
            } else {
                dep.consumedResources = I.consumedResources(dep);
            }

        }

    }

    public void executeDeployment(Deployment d) {
        search.executeDeployment(d);
    }

    @Override
    public void run() {
        startSimulation(fogNodes, histogram);
    }
}
