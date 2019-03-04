package di.unipi.socc.fogtorchpi.deployment;

import di.unipi.socc.fogtorchpi.utils.AppFactory;
import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.InfrastructureFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;

public class ParallelMonteCarloSearch {
    AppFactory app;
    InfrastructureFactory infrastructure;
    int times, threads;
    ConcurrentHashMap<Deployment, Couple<Double, Double>> histogram ;
    ArrayList<MonteCarloSearch> MCSearches;
    BufferedWriter writter;

    public ParallelMonteCarloSearch(AppFactory app, InfrastructureFactory infrastructure, int times, int threads) {
        this.app = app;
        this.infrastructure = infrastructure;
        this.times = times;
        this.threads = threads;
        this.histogram = new ConcurrentHashMap<>();
        this.MCSearches = new ArrayList<>();
        this.writter = null;
        for (int i = 0; i < threads; i++) {
            MCSearches.add(new MonteCarloSearch(
                    times / threads,
                    app.createApp(),
                    infrastructure.createInfrastructure(),
                    asList(),
                    histogram
            ));
        }
    }

    public ParallelMonteCarloSearch(AppFactory app, InfrastructureFactory infrastructure, int times, int threads, BufferedWriter writter) {
        this.app = app;
        this.infrastructure = infrastructure;
        this.times = times;
        this.threads = threads;
        this.histogram = new ConcurrentHashMap<>();
        this.MCSearches = new ArrayList<>();
        this.writter = writter;
        for (int i = 0; i < threads; i++) {
            MCSearches.add(new MonteCarloSearch(
                    times / threads,
                    app.createApp(),
                    infrastructure.createInfrastructure(),
                    asList(),
                    histogram
            ));
        }
    }

    public void addBusinessPolicies(String component, List<String> allowedNodes) {
        for (MonteCarloSearch s : MCSearches) {
            s.addBusinessPolicies(component, allowedNodes);
        }
    }

    public void start() {

        List<Thread> threadList = new ArrayList<>(threads);

        for (MonteCarloSearch s : MCSearches) {
            threadList.add(new Thread(s));
        }

        long timeStart = System.currentTimeMillis();

        for (Thread t : threadList) {
            t.start();
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        long timeEnd = System.currentTimeMillis();

        int j = 0;
        for (Deployment dep : histogram.keySet()) {
            histogram.replace(dep, new Couple((100 * histogram.get(dep).getA() / ((double) times)),
                    100 * (dep.consumedResources.getA() + dep.consumedResources.getB()) / 2));

//            System.out.println(j + " - " + dep + "; " + histogram.get(dep).getA() + "; "
//                    + histogram.get(dep).getB() + "; " + dep.deploymentMonthlyCost);
            try {
                this.writter.write("EX;"+j + ";" + dep + ";" + histogram.get(dep).getA() + ";"
                        + histogram.get(dep).getB() + ";" + dep.deploymentMonthlyCost +
                        ";\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
            j++;
        }

        //System.out.println("------------------\n***Simulation ended in " + ((timeEnd - timeStart) / 1000) + "s!");
        //try {
//            this.writter.write("------------------\n***Simulation ended in " + ((timeEnd - timeStart) / 1000) + "s!");
       // } catch (IOException e) {
        //    e.printStackTrace();
       // }
    }
    

}
