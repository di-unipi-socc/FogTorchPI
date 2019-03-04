package di.unipi.socc.fogtorchpi.experiments.VR;

import di.unipi.socc.fogtorchpi.deployment.Deployment;
import di.unipi.socc.fogtorchpi.deployment.MonteCarloSearch;
import di.unipi.socc.fogtorchpi.deployment.ParallelMonteCarloSearch;
import di.unipi.socc.fogtorchpi.experiments.smartbuilding.SmartBuildingApplication;
import di.unipi.socc.fogtorchpi.experiments.smartbuilding.SmartBuildingInfrastructure;
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
    private static final int GATEWAYS = 8; // 1, 2, 4, 8, 16
    private static final int TIMES = 100000;
    private static final int THREADS = 2;//*Runtime.getRuntime().availableProcessors();


    public static void main(String[] args) {

        ParallelMonteCarloSearch search = new ParallelMonteCarloSearch(new VRApplication(GATEWAYS, SMARTPHONES), new VRInfrastructure(GATEWAYS, SMARTPHONES), TIMES, THREADS);
        for (int i = 0; i < GATEWAYS; i++) {
                for (int j = 0; j < SMARTPHONES; j++) {
                    search.addBusinessPolicies("client_" + i + "_" + j, asList("sp_" + i + "_" + j));
                }
            }
        
        search.start();

    }

}



