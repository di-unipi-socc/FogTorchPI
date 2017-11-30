package di.unipi.socc.fogtorchpi.experiments.VR;

import di.unipi.socc.fogtorchpi.infrastructure.Infrastructure;
import di.unipi.socc.fogtorchpi.utils.Couple;
import di.unipi.socc.fogtorchpi.utils.Hardware;
import di.unipi.socc.fogtorchpi.utils.QoSProfile;

import static java.util.Arrays.asList;

public class VRInfrastructure {

    private int SMARTPHONES;
    private int GATEWAYS  ;

    public VRInfrastructure(int GATEWAYS, int SMARTPHONES){
        this.GATEWAYS = GATEWAYS;
        this.SMARTPHONES = SMARTPHONES;
    }

    public Infrastructure createInfrastructure(){
        Infrastructure I = new Infrastructure();
        I.addCloudDatacentre("cloud", asList(
                new Couple("linux", 0.0)),
                52.195097, 3.0364791,
                new Hardware(0, 0, 0, 3.0, 0.05, 0.01)
        );

        //Fog ISP proxy
        I.addFogNode("isp", asList(new Couple("linux", 0.0)),
                new Hardware(3, 4, 64, 3.0, 0.05, 0.01),
                43.740186, 10.364619);

        //link from isp-cloud
        I.addLink("cloud", "isp", new QoSProfile(100, 15, 10, 2, true));

        //Fog Gateways
        for (int i = 0; i < GATEWAYS; i++){
            I.addFogNode("gw_"+i, asList(new Couple("linux", 0.0)),
                    new Hardware(3, 4, 32, 3.0, 0.05, 0.01),
                    43.740186, 10.364619);

            //link gw-isp
            I.addLink("gw_"+i, "isp", new QoSProfile(4, 1, 10,2, true));
            //link isp-cloud
            I.addLink("gw_"+i, "cloud", new QoSProfile(104, 16, 10, 2, true) );
            //link gw-gw
            if (i > 0){
                for (int k = 0; k < i; k++){
                    I.addLink("gw_"+i, "gw_"+k, new QoSProfile(8, 1, 10, 2, true) );
                }
            }


            //Smartphones
            for (int j = 0; j < SMARTPHONES; j++){
                I.addFogNode("sp_"+i+"_"+j, asList(new Couple("linux", 0.0)),
                        new Hardware(2, 1, 16, 0.0, 0.00, 0.00),
                        43.740186, 10.364619);

                //link sp-gw
                for (int q = 0; q < GATEWAYS; q++){
                    if (q != i) {
                        I.addLink("sp_" + i + "_" + j, "gw_" + q, new QoSProfile(10, 2, 10, 2, true));
                        //System.out.println("sp_" + i + "_" + j + "-- gw" + q);
                    }
                }
                I.addLink("sp_"+i+"_"+j, "gw_"+i, new QoSProfile(2, 1, 10, 2, true));
                //link sp-isp
                I.addLink("sp_"+i+"_"+j, "isp", new QoSProfile(6, 1, 10, 2, true));
                //link sp-cloud
                I.addLink("sp_"+i+"_"+j, "cloud", new QoSProfile(106, 16, 10, 2, true));

                I.addThing("EEG"+i+"_"+j, "EEG_sensor",0,0, "sp_"+i+"_"+j, 0.01);
                I.addThing("display"+i+"_"+j, "display",0,0, "sp_"+i+"_"+j, 0.01);
            }
        }
        return I;
    }
}
