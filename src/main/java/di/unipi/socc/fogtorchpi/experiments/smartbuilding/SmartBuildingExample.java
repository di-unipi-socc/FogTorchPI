package di.unipi.socc.fogtorchpi.experiments.smartbuilding;

import di.unipi.socc.fogtorchpi.deployment.Deployment;
import di.unipi.socc.fogtorchpi.deployment.MonteCarloSearch;
import di.unipi.socc.fogtorchpi.deployment.ParallelMonteCarloSearch;
import di.unipi.socc.fogtorchpi.utils.Couple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;

public class SmartBuildingExample {

    private static final int TIMES = 100000;
    private static final int THREADS = 2; //*Runtime.getRuntime().availableProcessors();


    public static void main(String[] args) {

        ParallelMonteCarloSearch search = new ParallelMonteCarloSearch(new SmartBuildingApplication(), new SmartBuildingInfrastructure(), TIMES, THREADS);
        search.start();

    }

}



