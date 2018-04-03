package di.unipi.socc.fogtorchpi.experiments.VR;

import di.unipi.socc.fogtorchpi.deployment.Deployment;
import di.unipi.socc.fogtorchpi.deployment.MonteCarloSearch;
import di.unipi.socc.fogtorchpi.utils.Couple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;


/**
 * Created by Stefano on 21/10/2017.
 */
public class VRExample {

    private static final int SMARTPHONES = 4; //always 4
    private static final int GATEWAYS = 2; // 1, 2, 4, 8, 16
    private static final int TIMES = 100000;
    private static final int THREADS = 2;//*Runtime.getRuntime().availableProcessors();


    public static void main(String[] args) {
        ConcurrentHashMap<Deployment, Couple<Double, Double>> histogram = new ConcurrentHashMap<>();
        ArrayList<MonteCarloSearch> MCSearches = new ArrayList<>();

        for (int i = 0; i < THREADS ; i++){
            MCSearches.add(new MonteCarloSearch(
                    TIMES / THREADS,
                    new VRApplication(GATEWAYS, SMARTPHONES).createApp(),
                    new VRInfrastructure(GATEWAYS, SMARTPHONES).createInfrastructure(),
                    asList(),
                    histogram
            ));
        }


        for (MonteCarloSearch s : MCSearches) {
            for (int i = 0; i < GATEWAYS; i++) {
                for (int j = 0; j < SMARTPHONES; j++) {
                    s.addBusinessPolicies("client_" + i + "_" + j, asList("sp_" + i + "_" + j));
                }
            }
        }

        List<Thread> threads = new ArrayList<>(THREADS);

        for (MonteCarloSearch s : MCSearches){
            threads.add(new Thread(s));
        }

        long timeStart = System.currentTimeMillis();

        for (Thread t : threads) {
            t.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long timeEnd = System.currentTimeMillis();

        int j = 0;
        for (Deployment dep : histogram.keySet()) {
            histogram.replace(dep, new Couple((100 * histogram.get(dep).getA() / ((double) TIMES)), 100*(dep.consumedResources.getA() + dep.consumedResources.getB()) / 2));
            System.out.println(j + " - " + dep + "; " + histogram.get(dep).getA() + "; " + histogram.get(dep).getB() + "; " + dep.deploymentMonthlyCost);
            j++;
        }

        System.out.println("------------------\n***Simulation ended in " + ((timeEnd - timeStart)/1000) + "s!");
/*
        String filename
                = "C:\\Users\\Stefano\\Dropbox\\_Dottorato\\2017_Transactions_on_Service_Computing";


        s1.startSimulation(asList(), histogram);

        String name = filename + ".csv";
        try {
            PrintWriter writer = new PrintWriter(name, "UTF-8");
            writer.println("Deployment ; QoS-assurance; Hardware %;Cost");
            System.out.println("Deployment ; QoS-assurance ; Hardware %;Cost");
            int j = 0;

            for (Deployment dep : histogram.keySet()) {
                writer.println(dep + "; " + histogram.get(dep).getA() + ";" + histogram.get(dep).getB() + "; " + dep.deploymentMonthlyCost);
                System.out.println(j + " - " + dep + "; " + histogram.get(dep).getA() + "; " + histogram.get(dep).getB() + "; " + dep.deploymentMonthlyCost);
                j++;
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("HELLO");
            e.printStackTrace();
        }

*/


    }

}



