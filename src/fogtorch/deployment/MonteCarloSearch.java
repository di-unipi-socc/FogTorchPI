package fogtorch.deployment;

import fogtorch.application.Application;
import fogtorch.infrastructure.Infrastructure;
import fogtorch.utils.Couple;
import fogtorch.utils.QoSProfile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Stefano
 */
public class MonteCarloSearch {

    private int times;
    private Application A;
    private Infrastructure I;

    private HashMap<String, HashSet<String>> businessPolicies;
    private Search search;

    public MonteCarloSearch(
            int times,
            Application A,
            Infrastructure I) {

        this.times = times;
        this.A = A;
        this.I = I;
        businessPolicies = new HashMap<String, HashSet<String>>();
    }

    public void addBusinessPolicies(String component, List<String> allowedNodes) {
        HashSet<String> policies = new HashSet<>();
        policies.addAll(allowedNodes);
        this.businessPolicies.put(component, policies);
    }

    public HashMap<Deployment, Couple<Double, Double>> startSimulation(List<String> fogNodes) {
        HashMap<Deployment, Couple<Double, Double>> histogram = new HashMap<>();

        for (int j = 0; j < times; j++) {

            search = new Search(A, I, businessPolicies);

            for (QoSProfile q : I.L.values()) {
                q.sampleQoS();
            }

            search.findDeployments(true);

            double pos = search.D.size();
            double size = search.D.size();

            for (Deployment d : search.D) {
                if (histogram.containsKey(d)) {
                    Double newCount = histogram.get(d).getA() + 1.0; //montecarlo frequency
                    Double newPos = histogram.get(d).getB() + (pos / size);
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

            histogram.replace(dep, new Couple((100 * histogram.get(dep).getA() / ((double) times)), 100*(dep.consumedResources.getA() + dep.consumedResources.getB()) / 2));
        }

        return histogram;
    }

    public void executeDeployment(Deployment d) {
        search.executeDeployment(d);
    }
}
