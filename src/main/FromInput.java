/**
 * Original file of paper revised according to FT refactoring with cost.
 */
package main;

import examples.SmallApp;
import fogtorch.application.Application;
import fogtorch.application.ExactThing;
import fogtorch.application.SoftwareComponent;
import fogtorch.application.ThingRequirement;
import fogtorch.deployment.Deployment;
import fogtorch.deployment.MonteCarloSearch;
import fogtorch.infrastructure.Infrastructure;
import fogtorch.utils.Couple;
import fogtorch.utils.Hardware;
import fogtorch.utils.QoS;
import fogtorch.utils.QoSProfile;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileReader;

import java.io.FileNotFoundException;
import com.google.gson.*;
import java.util.List;


public class FromInput {

    public static void main(String[] args) {

        if (args.length < 1) {
          System.out.println("ERROR: first argument must be a json file");
          System.exit(1);
        }

        JsonObject rootJson = null;
        try {
            final JsonParser parser = new JsonParser();
            final JsonElement jsonElement = parser.parse(new FileReader(args[0]));
            rootJson = jsonElement.getAsJsonObject();
        } catch(FileNotFoundException e){
            System.out.println("ERROR: file not found");
            System.exit(2);
        }

        Infrastructure I = jsonToInfrastructure(rootJson);
        Application A = jsonToApplication(rootJson);

        HashMap<Deployment, Couple<Double, Double>> histogram = new HashMap<>();
        MonteCarloSearch s = new MonteCarloSearch(100000, A, I); //new Coordinates(43.740186, 10.364619));

        //s.addBusinessPolicies("C", asList("cloud2", "cloud1"));
        //s.addKeepLightNodes(asList("fog3"));

        String filename = args.length > 1 ? args[1] : null;

        String csv = "";
        boolean over = false;
        int i = 0;
        while (!over) {
            csv += String.join(";", new String[]{"Deployment",
                                                 "QoS-assurance",
                                                 "Hardware %",
                                                 "Cost"}) + "\n";

            histogram = s.startSimulation(asList());

            int j = 0;
            for (Deployment dep : histogram.keySet()) {
                // histogram.replace(dep, new Couple((100 * histogram.get(dep).getA() / ((double) TIMES)), (100 * histogram.get(dep).getB() / (double) TIMES)));
                String[] data = new String[]{dep.toString(),
                                             histogram.get(dep).getA().toString(),
                                             histogram.get(dep).getB().toString(),
                                             dep.deploymentMonthlyCost.toString()};
                csv += String.join(";", data) + "\n";
                System.out.println("" + j + " - " + String.join("\t", data)); // + " - "+dep + "; " + histogram.get(dep).getA() + "; " + histogram.get(dep).getB() + "; " + dep.deploymentMonthlyCost);
                j++;
            }

            if (filename != null){
                String name = filename + i + ".csv";
                System.out.println("Deployment write on file " + name);
                try (PrintWriter writer = new PrintWriter(name, "UTF-8")) {
                    writer.println(csv);
                } catch (IOException e) {
                    System.out.println("ERROR: cannot create csv file");
                }
            }
            csv = "";

            i++;

            if (!histogram.isEmpty()) {
                ArrayList<Deployment> l = new ArrayList(histogram.keySet());
                Scanner reader = new Scanner(System.in);  // Reading from System.in
                // System.out.println(l);
                System.out.println("Enter a deployment number: ");
                int n = reader.nextInt();

                Deployment chosenDeployment = l.get(n);
                System.out.println(chosenDeployment);
                s.executeDeployment(chosenDeployment);
//              s.addBusinessPolicies("B", asList(chosenDeployment.get(new SoftwareComponent("B")).getId()));
//              s.addBusinessPolicies("C", asList(chosenDeployment.get(new SoftwareComponent("C")).getId()));

            } else {
                over = true;
            }
        }
    }

    private static Infrastructure jsonToInfrastructure(JsonObject root){
        JsonObject infrastructure =  root.getAsJsonObject("infrastructure");
        JsonObject nodes = infrastructure.getAsJsonObject("nodes");
        JsonArray cloud = nodes.getAsJsonArray("cloud");

        Infrastructure I = new Infrastructure();

        for (JsonElement e : cloud){
            JsonObject c = e.getAsJsonObject();
            String name = c.getAsJsonPrimitive("name").getAsString();
            Double x = c.getAsJsonPrimitive("x").getAsDouble();
            Double y = c.getAsJsonPrimitive("y").getAsDouble();


            List<Couple<String,Double>> software = new ArrayList<>();
            JsonArray softwareJson = c.getAsJsonArray("software");
            for (JsonElement e1 : softwareJson){
                JsonArray s = e1.getAsJsonArray();
                software.add(new Couple(s.get(0).getAsString(),
                             s.get(1).getAsDouble()));
            }

            JsonObject hardwareJson = c.getAsJsonObject("hardware");
            Hardware hardware = new Hardware(
                hardwareJson.getAsJsonPrimitive("cores").getAsInt(),
                hardwareJson.getAsJsonPrimitive("ram").getAsInt(),
                hardwareJson.getAsJsonPrimitive("storage").getAsInt(),
                hardwareJson.getAsJsonPrimitive("cores_cost").getAsDouble(),
                hardwareJson.getAsJsonPrimitive("ram_cost").getAsDouble(),
                hardwareJson.getAsJsonPrimitive("storage_cost").getAsDouble()
            );

            List<Couple<String,Double>> vmTypes = new ArrayList<>();
            JsonArray vmTypesJson = c.getAsJsonArray("vm_types");
            if (vmTypesJson != null)
                for (JsonElement e2 : vmTypesJson){
                    JsonArray t = e2.getAsJsonArray();
                    vmTypes.add(new Couple(t.get(0).getAsString(),
                                            t.get(1).getAsDouble()));
                }

            I.addCloudDatacentre(name, software, x, y, hardware, vmTypes);
        }

        JsonArray fog = nodes.getAsJsonArray("fog");

        for (JsonElement e : fog){
            JsonObject f = e.getAsJsonObject();
            String name = f.getAsJsonPrimitive("name").getAsString();
            Double x = f.getAsJsonPrimitive("x").getAsDouble();
            Double y = f.getAsJsonPrimitive("y").getAsDouble();


            List<Couple<String,Double>> software = new ArrayList<>();
            JsonArray softwareJson = f.getAsJsonArray("software");
            for (JsonElement e1 : softwareJson){
                JsonArray s = e1.getAsJsonArray();
                software.add(new Couple(s.get(0).getAsString(),
                             s.get(1).getAsDouble()));
            }

            JsonObject hardwareJson = f.getAsJsonObject("hardware");
            Hardware hardware = new Hardware(
                hardwareJson.getAsJsonPrimitive("cores").getAsInt(),
                hardwareJson.getAsJsonPrimitive("ram").getAsInt(),
                hardwareJson.getAsJsonPrimitive("storage").getAsInt(),
                hardwareJson.getAsJsonPrimitive("cores_cost").getAsDouble(),
                hardwareJson.getAsJsonPrimitive("ram_cost").getAsDouble(),
                hardwareJson.getAsJsonPrimitive("storage_cost").getAsDouble()
            );

            I.addFogNode(name, software, hardware, x, y);
        }


        JsonArray links =  infrastructure.getAsJsonArray("links");

        for (JsonElement e : links){
            JsonObject l = e.getAsJsonObject();
            String from = l.getAsJsonPrimitive("from").getAsString();
            String to = l.getAsJsonPrimitive("to").getAsString();

            List<Couple<QoS,Double>> downloadList = new ArrayList<>();
            JsonArray downloadJson = l.getAsJsonArray("download");
            for (JsonElement e1 : downloadJson){
                JsonObject eo = e1.getAsJsonObject();
                JsonObject qos = eo.getAsJsonObject("qos");

                JsonPrimitive latencyObj = qos.getAsJsonPrimitive("latency");
                Integer latency = latencyObj.isString() ? Integer.MAX_VALUE : latencyObj.getAsInt();
                downloadList.add(new Couple(
                    new QoS(
                        latency,
                        qos.getAsJsonPrimitive("bandwidth").getAsDouble()),
                    eo.getAsJsonPrimitive("percentage").getAsDouble()));
            }

            JsonArray uploadJson = l.getAsJsonArray("upload");
            if (uploadJson != null && uploadJson.size() > 0){
                List<Couple<QoS,Double>> uploadList = new ArrayList<>();

                for (JsonElement e2 : uploadJson){
                    JsonObject eo = e2.getAsJsonObject();
                    JsonObject qos = eo.getAsJsonObject("qos");

                    JsonPrimitive latencyObj = qos.getAsJsonPrimitive("latency");
                    Integer latency = latencyObj.isString() ? Integer.MAX_VALUE : latencyObj.getAsInt();
                    uploadList.add(new Couple(
                        new QoS(
                            latency,
                            qos.getAsJsonPrimitive("bandwidth").getAsDouble()),
                        eo.getAsJsonPrimitive("percentage").getAsDouble()));
                }

                I.addLink(from, to,
                    new QoSProfile(downloadList),
                    new QoSProfile(uploadList)
                );
            } else{
                I.addLink(from, to, new QoSProfile(downloadList));
            }
        }

        JsonArray things =  infrastructure.getAsJsonArray("things");

        for (JsonElement e : things){
            JsonObject t = e.getAsJsonObject();
            I.addThing(
                t.getAsJsonPrimitive("name").getAsString(),
                t.getAsJsonPrimitive("type").getAsString(),
                t.getAsJsonPrimitive("x").getAsDouble(),
                t.getAsJsonPrimitive("y").getAsDouble(),
                t.getAsJsonPrimitive("fog_node").getAsString(),
                t.getAsJsonPrimitive("cost").getAsDouble()
            );
        }

        return I;
    }

    static private Application jsonToApplication(JsonObject root){
        Application A = new Application();

        JsonObject application =  root.getAsJsonObject("application");
        JsonArray components =  application.getAsJsonArray("components");

        for (JsonElement e : components){
            JsonObject c = e.getAsJsonObject();
            String name = c.getAsJsonPrimitive("name").getAsString();

            List<String> software = new ArrayList<>();
            JsonArray softwareJson = c.getAsJsonArray("software");
            for (JsonElement e1 : softwareJson){
                software.add(e1.getAsString());
            }

            JsonObject hardwareJson = c.getAsJsonObject("hardware");
            Hardware hardware = new Hardware(
                hardwareJson.getAsJsonPrimitive("vm_type").getAsString(),
                hardwareJson.getAsJsonPrimitive("cost").getAsDouble()
            );

            JsonArray thingsJson = c.getAsJsonArray("things");
            if (thingsJson != null && thingsJson.size() > 0){
                ArrayList<ThingRequirement> neededThings = new ArrayList<>();

                for (JsonElement e2 : thingsJson){
                    JsonObject eo = e2.getAsJsonObject();
                    String thingName = eo.getAsJsonPrimitive("name").getAsString();

                    JsonObject qos = eo.getAsJsonObject("qos");

                    JsonObject to = qos.getAsJsonObject("to");
                    QoSProfile toQos = new QoSProfile(
                        to.getAsJsonPrimitive("latency").getAsInt(),
                        to.getAsJsonPrimitive("bandwidth").getAsDouble()
                    );

                    JsonObject from = qos.getAsJsonObject("from");
                    QoSProfile fromQos = new QoSProfile(
                        to.getAsJsonPrimitive("latency").getAsInt(),
                        to.getAsJsonPrimitive("bandwidth").getAsDouble()
                    );

                    neededThings.add(new ExactThing(
                        thingName, fromQos, toQos,
                        eo.getAsJsonPrimitive("invokes").getAsInt()
                    ));
                }

                A.addComponent(name, software, hardware, neededThings);

            } else
                A.addComponent(name, software, hardware);
        }

        JsonArray app_links = application.getAsJsonArray("links");

        for (JsonElement e : app_links){
            JsonObject l = e.getAsJsonObject();
            A.addLink(
                l.getAsJsonPrimitive("from").getAsString(),
                l.getAsJsonPrimitive("to").getAsString(),
                l.getAsJsonPrimitive("latency").getAsInt(),
                l.getAsJsonPrimitive("up").getAsDouble(),
                l.getAsJsonPrimitive("down").getAsDouble()
            );
        }
        return A;
    }
}
