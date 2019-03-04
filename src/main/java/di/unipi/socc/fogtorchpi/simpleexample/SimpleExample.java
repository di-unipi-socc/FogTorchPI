package di.unipi.socc.fogtorchpi.simpleexample;

import di.unipi.socc.fogtorchpi.deployment.ParallelMonteCarloSearch;

public class SimpleExample {
    
    private static final int TIMES = 1;
    private static final int THREADS = 1; //*Runtime.getRuntime().availableProcessors();


    public static void main(String[] args) {

        ParallelMonteCarloSearch search = new ParallelMonteCarloSearch(new SimpleApp(), new SimpleInfrastructure(), TIMES, THREADS);
        search.start();

    }
}
