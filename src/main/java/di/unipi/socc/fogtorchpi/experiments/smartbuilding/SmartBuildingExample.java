package di.unipi.socc.fogtorchpi.experiments.smartbuilding;

import di.unipi.socc.fogtorchpi.deployment.ParallelMonteCarloSearch;

public class SmartBuildingExample {

    private static final int TIMES = 100000;
    private static final int THREADS = 2; //*Runtime.getRuntime().availableProcessors();


    public static void main(String[] args) {

        ParallelMonteCarloSearch search = new ParallelMonteCarloSearch(new SmartBuildingApplication(), new SmartBuildingInfrastructure(), TIMES, THREADS);
        search.start();

    }

}



