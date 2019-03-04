package di.unipi.socc.fogtorchpi.deployment;

import di.unipi.socc.fogtorchpi.application.Application;
import di.unipi.socc.fogtorchpi.infrastructure.ComputationalNode;
import di.unipi.socc.fogtorchpi.infrastructure.Infrastructure;
import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoS;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;

/**
 * @author Stefano
 */
public class GAMonteCarloSearch implements Runnable {


    //start  GA modifications
    private void exit() {
        System.exit(-1);
    }

    private void print(Object s) {
        System.out.println("" + s);
    }


    private int times;
    public Application A;
    private Infrastructure I;

    List<String> fogNodes;
    ConcurrentHashMap<Deployment, Couple<Double, Double>> histogram;
    HashMap<String, List<String>> tracePopulation;

    private HashMap<String, HashSet<String>> businessPolicies;
    private GASearch search;

    private double maxCost = 10.0;
    private double minCost = 1.0;

    int numberOfGenerations;// = 10;
    private int populationSize;// = 10;
    private double MutationProbability;// = 0.1;
    private int seed;

    private BufferedWriter writer;

    public HashMap<String, HashSet<String>> getBusinessPolicies() {
        return this.businessPolicies;
    }

    public GAMonteCarloSearch(
            int times,
            Application A,
            Infrastructure I,
            List<String> fogNodes,
            ConcurrentHashMap<Deployment, Couple<Double, Double>> histogram,
            HashMap<String, List<String>> tracePopulation,
            int numberOfGenerations,
            int populationSize,
            double MutationProbability,
            BufferedWriter bf) {

        this.times = times;
        this.A = A;
        this.I = I;
        this.fogNodes = fogNodes;
        this.histogram = histogram;
        businessPolicies = new HashMap<String, HashSet<String>>();

        //THESE VAR. HAVE TO BE DEFINED



        this.writer = bf;


        this.numberOfGenerations = numberOfGenerations;
        this.populationSize = populationSize;
        this.MutationProbability = MutationProbability;

        this.tracePopulation = tracePopulation;
    }

    public void addBusinessPolicies(String component, List<String> allowedNodes) {
        HashSet<String> policies = new HashSet<>();
        policies.addAll(allowedNodes);
        this.businessPolicies.put(component, policies);
    }

    private void addTrace(HashMap<String, List<String>> tracePopulation, String deployment, String GAPopulation){
        if (!tracePopulation.containsKey(deployment)){
            tracePopulation.put(deployment,new ArrayList<String>());
        }
        tracePopulation.get(deployment).add(GAPopulation);
    }

    public void startSimulation(List<String> fogNodes, ConcurrentHashMap<Deployment, Couple<Double, Double>> histogram,HashMap<String, List<String>> tracePopulation ) {


        ArrayList<Deployment> globalPopulation = new ArrayList<Deployment>();
        ArrayList<Deployment> Population = new ArrayList<>();
        HashMap<Integer,Double> PopulationFitness = new HashMap<>();
        int id = 0;



        //double deploymentCost = deployment.deploymentMonthlyCost.getCost();
//        search = new GASearch(A, I, businessPolicies,this.maxCost,this.minCost);

        // 3 POPULATIONS -> GlobalPopulation
        // FIRST POPULATION WITH THE BEST INFRASTRUCTURE
        search = new GASearch(A, I, businessPolicies,this.numberOfGenerations,this.populationSize,this.MutationProbability,this.writer);
        for (Couple c2 : I.L.keySet()) {
            I.L.get(c2).bestQoS(); //BUT this, not ... ? Why?
        }

        System.out.println(" BEST");
        Couple<ArrayList<Deployment>,HashMap<Integer,Double>> c = search.findDeployments();
        Population = c.getA();
        PopulationFitness =c.getB();
        System.out.println(" END BEST");

//        System.out.println("BEST _ Population: " + Population);
        for (Deployment d : Population) {
            String key = "BEST-" + id +" Fit:"+ PopulationFitness.get(d.toString());
            globalPopulation.add(d);
            addTrace(tracePopulation,d.toString(),key);
            id++;
        }


        // SECOND POPULATION WITH THE WORST INFRASTRUCTURE
        search = new GASearch(A, I, businessPolicies,this.numberOfGenerations,this.populationSize,this.MutationProbability,this.writer);
        for (Couple c2 : I.L.keySet()) {
            I.L.get(c2).worstQoS();
//            print(i+" I.L "+I.L.get(c));
        }
        System.out.println(" WORST");
        c = search.findDeployments();
        Population = c.getA();
        PopulationFitness = c.getB();
        System.out.println(" END WORST");
//        System.out.println("WORST _ Population: " + Population);

        for (Deployment d : Population) {
            String key = "WORST-" + id+" Fit:"+ PopulationFitness.get(d.toString());
            globalPopulation.add(d);
            addTrace(tracePopulation,d.toString(),key);
            id++;
        }

//        System.out.println(" running MC Simulation ");

        for (int j = 0; j < times; j++) {
            //we test the set of populations in other infrastructure
            search = new GASearch(A, I, businessPolicies,this.numberOfGenerations,this.populationSize,this.MutationProbability,this.writer);

            for (Couple c2 : I.L.keySet()) {
                I.L.get(c2).sampleQoS(); //OK
            }

            //We need to validate the all deployments on that Infra.
            for (Deployment dep : globalPopulation) {
//                print("Size dep:"+dep.size());
//                if (dep.size()==16) { //CHECK COHERENCY OF DEPLOYMENTS according with APP.combinations
                    if (search.validDeployment(dep)) {
                        search.D.add(dep);
                    } else {
                          System.out.println(".");
                    }
//                }else{
//                    System.out.println("A deployment inconsistent");
//                }
            }


            double pos = search.D.size();
            double size = search.D.size();


            for (Deployment d : search.D) {
//                if (d.size()==16) {
                    if (histogram.containsKey(d)) {
                        Double newCount = histogram.get(d).getA() + 1.0; //montecarlo frequency
                        Double newPos = histogram.get(d).getB() + (pos / size); //
                        histogram.replace(d, new Couple(newCount, newPos));
                    } else {
                        histogram.put(d, new Couple(1.0, pos / size));
                    }
                    pos--;
//                }
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
        startSimulation(fogNodes, histogram,tracePopulation);
    }
}




