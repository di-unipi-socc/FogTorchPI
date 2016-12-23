/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fogtorch.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import fogtorch.application.Application;
import fogtorch.application.SoftwareComponent;
import fogtorch.application.ExactThing;
import fogtorch.application.ThingRequirement;
import fogtorch.infrastructure.CloudDatacentre;
import fogtorch.infrastructure.FogNode;
import fogtorch.infrastructure.Infrastructure;
import fogtorch.utils.Couple;
import fogtorch.infrastructure.ComputationalNode;
import fogtorch.utils.QoSProfile;
import java.util.Collections;
import java.util.List;

public class Search {

    private Application A;
    private Infrastructure I;
    private HashMap<String, ArrayList<ComputationalNode>> K;
    private HashMap<String, HashSet<String>> businessPolicies;
    //private ArrayList<String> keepLight;
    int steps;
    public ArrayList<Deployment> D;
   // public Coordinates deploymentLocation;

    public Search(Application A, Infrastructure I) { //, Coordinates deploymentLocation
        this.A = A;
        this.I = I;
        K = new HashMap<>();
        D = new ArrayList<>();
        businessPolicies = new HashMap<String, HashSet<String>>();
        //keepLight = new ArrayList<>();
        for (SoftwareComponent s: A.S){
             K.put(s.getId(), new ArrayList<>());
        }
        //this.deploymentLocation = deploymentLocation;
    }

    private boolean findCompatibleNodes() {
        for (SoftwareComponent s : A.S) {
            for (CloudDatacentre n : I.C.values()) {
                if (n.isCompatible(s) && checkThings(s, n)
                        && ((businessPolicies.containsKey(s.getId()) && businessPolicies.get(s.getId()).contains(n.getId()))
                        || !businessPolicies.containsKey(s.getId()))) {
                  
                    K.get(s.getId()).add(n);
                }
            }
        }

        for (SoftwareComponent s : A.S) {
            for (FogNode n : I.F.values()) {
                //System.out.println(s.getId() + " " + n.getId() + " "+ checkThings(s,n));
                if (n.isCompatible(s) && checkThings(s, n)
                        && ((businessPolicies.containsKey(s.getId()) && businessPolicies.get(s.getId()).contains(n.getId()))
                        || !businessPolicies.containsKey(s.getId()))) {
                    K.get(s.getId()).add(n);
                }
            }
        }

        for (SoftwareComponent s : A.S) {
            if (K.get(s.getId()).isEmpty())
                return false;
            bestFirst(K.get(s.getId()), s);
        }

        return true;
    }

    public Deployment findDeployments(boolean exhaustive) {
        Deployment deployment = new Deployment();
        
        //System.out.println(A.S);
        findCompatibleNodes();
        Collections.sort(A.S, (Object o1, Object o2) -> {
            SoftwareComponent s1 = (SoftwareComponent) o1;
            SoftwareComponent s2 = (SoftwareComponent) o2;
            return Integer.compare(K.get(s1.getId()).size(), K.get(s2.getId()).size());             
        });
        
        if (exhaustive)
            exhaustiveSearch(deployment);
        else
            search(deployment);

        return deployment;
    }
    
    /**
     * It prunes the search space till the first result. 
     * Uses the heuristics.
     * @param deployment
     * @return an eligible deployment.
     */
    private Deployment search(Deployment deployment) {
        if (isComplete(deployment)) {
            D.add(deployment);
            //System.out.println(deployment);
            return deployment;
        }
        SoftwareComponent s = selectUndeployedComponent(deployment);
        if (K.get(s.getId()) != null) {
            for (ComputationalNode n : K.get(s.getId())) { // for all nodes compatible with s
                //System.out.println(steps + " Checking " + s.getId() + " onto node " + n.getId());
                if (isValid(deployment, s, n)) {
                    //System.out.println(steps + " Deploying " + s.getId() + " onto node " + n.getId());
                    deploy(deployment, s, n);
                    Deployment result = search(deployment);
                    if (result != null) {
                        return deployment;
                    }
                    undeploy(deployment, s, n);
                }
                //System.out.println(steps + " Undeploying " + s.getId() + " from node " + n.getId());
            }
        }
        return null;
    }
    /**
     * It returns all existing eligible deployment plans.
     * Results are sorted according to the heuristics (no pruning).
     * @param deployment 
     */
    private void exhaustiveSearch(Deployment deployment) {
        if (isComplete(deployment)) {
            D.add((Deployment) deployment.clone());
            //System.out.println(deployment);
            return;
        }
        SoftwareComponent s = selectUndeployedComponent(deployment);
        System.out.println("Selected :" + s);
        ArrayList<ComputationalNode> Ks = bestFirst(K.get(s.getId()),s);
        //System.out.println(K.get(s.getId()));
        for (ComputationalNode n : Ks) { // for all nodes compatible with s 
            steps++;
            //System.out.println(steps + " Checking " + s.getId() + " onto " + n.getId());
            if (isValid(deployment, s, n)) {
                //System.out.println(steps + " Deploying " + s.getId() + " onto " + n.getId());
                deploy(deployment, s, n);
                exhaustiveSearch(deployment);
                undeploy(deployment, s, n);
            }
            //System.out.println(steps + " Undeploying " + s.getId() + " from " + n.getId());
        }

    }
    
    private ArrayList<ComputationalNode> bestFirst(ArrayList<ComputationalNode> Ks, SoftwareComponent s) {
        for (ComputationalNode n : Ks) {
            n.computeHeuristic(s);//, this.deploymentLocation);
        }
        Collections.sort(Ks);
        return Ks;
    }

    private boolean checkLinks(Deployment deployment, SoftwareComponent s, ComputationalNode n) {
        for (SoftwareComponent c : deployment.keySet()) {
            ComputationalNode m = deployment.get(c); // nodo deployment c
            Couple couple1 = new Couple(c.getId(), s.getId());
            Couple couple2 = new Couple(s.getId(), c.getId());
            if (A.L.containsKey(couple1)) {
                QoSProfile req1 = A.L.get(couple1);
                QoSProfile req2 = A.L.get(couple2);
                Couple c1 = new Couple(m.getId(), n.getId());
                Couple c2 = new Couple(n.getId(), m.getId());
                //System.out.println("Finding a link for " + couple1 + " between " + c1);
                if (I.L.containsKey(c1)) {
                    QoSProfile off1 = I.L.get(c1);
                    QoSProfile off2 = I.L.get(c2);
                    
                    if (!off1.supports(req1) || !off2.supports(req2)) {
                        return false;
                    }
                } else {
                    //System.out.println("It does not exist");
                    return false;
                }
            }
        }
        return true;
    }

    private void deployLinks(Deployment deployment, SoftwareComponent s, ComputationalNode n) {
        for (SoftwareComponent c : deployment.keySet()) {
            ComputationalNode m = deployment.get(c);
            Couple couple1 = new Couple(c.getId(), s.getId());
            Couple couple2 = new Couple(s.getId(), c.getId());

            if (A.L.containsKey(couple1) && A.L.containsKey(couple2)) {
                QoSProfile req1 = A.L.get(couple1); //c,s
                QoSProfile req2 = A.L.get(couple2); //s,c
                Couple c1 = new Couple(m.getId(), n.getId()); // m,n
                Couple c2 = new Couple(n.getId(), m.getId()); // n,m
                if (I.L.containsKey(c1)) {
                    QoSProfile pl1 = I.L.get(c1);
                    QoSProfile pl2 = I.L.get(c2);
                    pl1.setBandwidth(pl1.getBandwidth() - req1.getBandwidth());
                    pl2.setBandwidth(pl2.getBandwidth() - req2.getBandwidth());
                }
            }
        }

        for (ThingRequirement t : s.Theta) {
            ExactThing e = (ExactThing) t;
            if (n.isReachable(e.getId(), I, e.getQNodeThing(), e.getQThingNode())) {
                Couple c1 = new Couple(n.getId(), e.getId()); //c1 nodeThing

                QoSProfile pl1 = I.L.get(c1);
                QoSProfile pl2 = I.L.get(new Couple(e.getId(), n.getId()));

                pl1.setBandwidth(pl1.getBandwidth() - e.getQNodeThing().getBandwidth());
                pl2.setBandwidth(pl2.getBandwidth() - e.getQThingNode().getBandwidth());

            }
        }
    }

    private boolean checkThings(SoftwareComponent s, ComputationalNode n) {
        //System.out.println("Checking things for "+ s.getId() + " -- " + n);
        for (ThingRequirement r : s.getThingsRequirements()) {
            //System.out.println(s.getId() + " " + r.toString());
            if (r.getClass() == ExactThing.class) {
                ExactThing e = (ExactThing) r;
                if (!n.isReachable(e.getId(), I, e.getQNodeThing(), e.getQThingNode())) { // directly or remotely
                    //System.out.println("Not reachable "+e.getId() +" from " + n.getId());
                    return false;
                }
            }
        }

        return true;
    }

    private void undeployLinks(Deployment deployment, SoftwareComponent s, ComputationalNode n) {
        for (SoftwareComponent c : deployment.keySet()) {
            ComputationalNode m = deployment.get(c);
            Couple couple1 = new Couple(c.getId(), s.getId());
            Couple couple2 = new Couple(s.getId(), c.getId());

            if (A.L.containsKey(couple1) && A.L.containsKey(couple2)) {
                QoSProfile al1 = A.L.get(couple1);
                QoSProfile al2 = A.L.get(couple2);
                Couple c1 = new Couple(m.getId(), n.getId());
                Couple c2 = new Couple(n.getId(), m.getId());
                if (I.L.containsKey(c1)) {
                    QoSProfile pl1 = I.L.get(c1);
                    QoSProfile pl2 = I.L.get(c2);

                    pl1.setBandwidth(pl1.getBandwidth() + al1.getBandwidth());
                    pl2.setBandwidth(pl2.getBandwidth() + al2.getBandwidth());
                }
            }

        }

        for (ThingRequirement t : s.Theta) {
            ExactThing e = (ExactThing) t;
            //System.out.println("Request" + e);
            if (n.isReachable(e.getId(), I, e.getQNodeThing(), e.getQThingNode())) {
                Couple c1 = new Couple(n.getId(), e.getId());

                QoSProfile pl1 = I.L.get(c1);
                QoSProfile pl2 = I.L.get(new Couple(e.getId(), n.getId()));

                pl1.setBandwidth(pl1.getBandwidth() + e.getQNodeThing().getBandwidth());
                pl2.setBandwidth(pl2.getBandwidth() + e.getQThingNode().getBandwidth());

            }
        }
    }

    private boolean isValid(Deployment deployment, SoftwareComponent s, ComputationalNode n) {
        //System.out.println(n.getId() + " is Compatible " + n.isCompatible(s)  );
        //System.out.println(n.getId() + " links " + checkLinks(deployment, s, n)  );
        //System.out.println(n.getId() + " things " + checkThings(s, n));
        return n.isCompatible(s) && checkLinks(deployment, s, n) && checkThings(s, n);
    }

    private void deploy(Deployment deployment, SoftwareComponent s, ComputationalNode n) {
        deployment.put(s, n);
        //System.out.println(deployment + " " + deployment.size());
        n.deploy(s);
        deployLinks(deployment, s, n);
    }

    private void undeploy(Deployment deployment, SoftwareComponent s, ComputationalNode n) {
        if (deployment.containsKey(s)) {
            deployment.remove(s);
            n.undeploy(s);
            undeployLinks(deployment, s, n);
        }
       // System.out.println("UNDEP"+deployment);
    }

    private SoftwareComponent selectUndeployedComponent(Deployment deployment) {
        //System.out.println(deployment.size());
        return A.S.get(deployment.size());
    }

    private boolean isComplete(Deployment deployment) {
        return deployment.size() == A.S.size();
    }

    public void addBusinessPolicies(String component, List<String> allowedNodes) {
        HashSet<String> policies = new HashSet<>();
        policies.addAll(allowedNodes);
        this.businessPolicies.put(component, policies);
    }
    
    public void addKeepLightNodes(List<String> keepLightNodes) {
        for (String n : keepLightNodes){
            if (I.C.containsKey(n)){
                I.C.get(n).setKeepLight(true);
            }
            if(I.F.containsKey(n)){
                I.F.get(n).setKeepLight(true);
            }
                
        }
    }

    

}
