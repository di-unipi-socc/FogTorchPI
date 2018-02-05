package di.unipi.socc.fogtorchpi.experiments.securefog;

import di.unipi.socc.fogtorchpi.application.SoftwareComponent;
import di.unipi.socc.fogtorchpi.deployment.Deployment;
import di.unipi.socc.fogtorchpi.deployment.MonteCarloSearch;
import di.unipi.socc.fogtorchpi.infrastructure.SecurityCounterMeasure;
import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.SecurityParametersTypes;
import di.unipi.socc.fogtorchpi.utils.SecurityTaxonomy;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;


/**
 * Created by Stefano on 21/10/2017.
 */
public class SecureExample {

    private static final int TIMES = 2;
    private static final int THREADS = 2; //*Runtime.getRuntime().availableProcessors();
    private static int COMPONENTS = 3; // number of app components

    public static void main(String[] args) {
        ConcurrentHashMap<Deployment, Couple<Double, Double>> histogram = new ConcurrentHashMap<>();
        ArrayList<MonteCarloSearch> MCSearches = new ArrayList<>();

        for (int i = 0; i < THREADS ; i++){
            MCSearches.add(new MonteCarloSearch(
                    TIMES / THREADS,
                    new SecureApplication().createApp(),
                    new SecureInfrastructure().createInfrastructure(),
                    asList(),
                    histogram
            ));
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
        DecimalFormat df = new DecimalFormat("#.00");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));

        boolean details = false;

        System.out.println("\n***** Security Assessment *****\n");
        j = 0;
        for (Deployment dep : histogram.keySet()){
            System.out.println(j + " " + dep + "\n - QoS-assurance: " +  histogram.get(dep).getA() + "\n - Fog Resource Consumption: " + df.format(histogram.get(dep).getB()) + "\n - Cost(€): " + dep.deploymentMonthlyCost + "\n - Overall Security Score: " + df.format(dep.computeDeploymentSecurityScore()/45.0*100.0));
            for (String type : (SecurityParametersTypes.getParametersTypes())){
                double result = 100*dep.computeDeploymentSecurityScoreByType(type)/(SecurityTaxonomy.getSecurityMeasuresByType(type).size()*COMPONENTS);
                System.out.println("\t - " + type + ": " + df.format(result));
            }
            if (details) {
                for (SoftwareComponent component : dep.keySet()) {
                    System.out.println("\t\t " + component.getId() + "  - Component Security Score: " + dep.computeDeploymentSecurityScoreByComponent(component));
                    for (String type : (SecurityParametersTypes.getParametersTypes())) {
                        ArrayList<String> active_measures = dep.get(component).getSecurityMeasuresByType(type);
                        List<String> allMeasures = SecurityTaxonomy.getSecurityMeasuresByType(type);
                        double size = allMeasures.size();
                        allMeasures.removeAll(active_measures);
                        System.out.println("\t\t\t - " + type + ": " + dep.computeDeploymentSecurityScoreByComponentAndType(component, type) + " missing " + allMeasures);
                    }
                }
            }

            j++;
        }


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



