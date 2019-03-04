package di.unipi.socc.fogtorchpi.ExampleBIG;

import di.unipi.socc.fogtorchpi.deployment.Deployment;
import di.unipi.socc.fogtorchpi.deployment.GAMonteCarloSearch;
import di.unipi.socc.fogtorchpi.deployment.ParallelMonteCarloSearch;
import di.unipi.socc.fogtorchpi.utils.Couple;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;

public class SimpleExample {
    
    private static final int TIMES = 10;
    private static final int THREADS = 1; //*Runtime.getRuntime().availableProcessors();

    static String nameExperiment = "BigExample";

    public static void main(String[] args) {

        BufferedWriter writer = null;
        BufferedWriter writerFit = null;
        BufferedWriter writertimes = null;
        try {
            File logFile = new File("results/IEServices19/"+nameExperiment+".txt");
            File logFile2 = new File("results/IEServices19/"+nameExperiment+"_fit.txt");
            File logFile3 = new File("results/IEServices19/"+nameExperiment+"_time.txt");

            System.out.println(logFile.getCanonicalPath());
            writer = new BufferedWriter(new FileWriter(logFile));
            writerFit = new BufferedWriter(new FileWriter(logFile2));
            writertimes = new BufferedWriter(new FileWriter(logFile3));

            System.out.println(" GA + MONTECARLO SEARCH ");
            ConcurrentHashMap<Deployment, Couple<Double, Double>> histogram = new ConcurrentHashMap<>();
            HashMap<String, List<String>> tracePopulation = new HashMap<>();
            GAMonteCarloSearch search = new GAMonteCarloSearch(TIMES*10, new SimpleApp().createApp(), new SimpleInfrastructure().createInfrastructure(), asList(), histogram, tracePopulation, 50, 100, 0.1,writerFit);

            long timeStart = System.currentTimeMillis();
            search.run();
            long timeEnd = System.currentTimeMillis();


            int j = 0;
            System.out.println("GA");

            for (Deployment dep : histogram.keySet()) {

                histogram.replace(dep, new Couple((100 * histogram.get(dep).getA() / ((double) TIMES)),
                        100 * (dep.consumedResources.getA() + dep.consumedResources.getB()) / 2));

                System.out.println(j + " - " + dep + "; " + histogram.get(dep).getA() + "; "
                        + histogram.get(dep).getB() + "; " + dep.deploymentMonthlyCost +
                        ";" + tracePopulation.get(dep.toString())
                );

                writer.write("GA;"+j + ";" + dep + ";" + histogram.get(dep).getA() + ";"
                        + histogram.get(dep).getB() + ";" + dep.deploymentMonthlyCost +
                        ";"+tracePopulation.get(dep.toString())+"\n");
                j++;
            }
            System.out.println(" GA - END: "+(timeEnd-timeStart));
            writertimes.write(" GA Search -  Simulation ends: "+(timeEnd-timeStart)+"\n");


            System.out.println("EX");
            timeStart = System.currentTimeMillis();

            ParallelMonteCarloSearch search2 = new ParallelMonteCarloSearch(new SimpleApp(), new SimpleInfrastructure(), TIMES, THREADS,writer);
            search2.start();

            timeEnd = System.currentTimeMillis();

            System.out.println(" EX - END: "+(timeEnd-timeStart));
            writertimes.write(" Exhaustive Search -  Simulation ends: "+(timeEnd-timeStart)+"\n");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
                writerFit.close();
                writertimes.close();
            } catch (Exception e) {
            }
        }

//        try {
//            String path = SimpleExample.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//            System.out.println("Runnning python  - Path "+path);
//            String ret = null;
//            Process p = Runtime.getRuntime().exec("python /Users/isaaclera/IntelIjProjects/FogTorchPI/src/graphs/scatter3d.py --file "+nameExperiment);
//            p.waitFor();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

}
