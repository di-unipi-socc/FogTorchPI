<img src="https://github.com/di-unipi-socc/FogTorchPI/blob/master/img/logoftpi.PNG" width="300">

_A prototype for deployment of Fog applications and probabilistic QoS-assurance estimation of eligible deployments._

FogTorch is based upon the work described in

> [Antonio Brogi]((http://pages.di.unipi.it/brogi), [Stefano Forti](http://pages.di.unipi.it/forti), [Ahmad Ibrahim]((http://pages.di.unipi.it/ibrahim))
> **How to best deploy your Fog applications, probably.** 
> _Accepted at_ [IEEE International Conference on Fog and Edge Computing (ICFEC’2017)](http://fec-conf.gforge.inria.fr/index.html), May 2017, Madrid, Spain

If you wish to reuse source code in this repo, please cite the above mentioned article.

##Intro

**FogTorchΠ** is an open source prototype, developed in Java, based on a model for Fog computing infrastructures and applications.

It takes into account non-functional parameters within the model (i.e., hardware, software, latency and bandwidth) to determine, compare and contrast different eligible deployments of a given application over a Fog infrastructure.

In the case of hardware capabilities, it considers CPU cores, RAM and storage available at a given node or required by a given software component. 

Software capabilities are represented by a list of software names (operating system, programming languages, frameworks etc).

It considers latency, and both download and upload bandwidths as QoS attributes. Latency is measured in milliseconds (ms), while bandwidth is given in Megabits per second (Mbps). 

## Quick User Guide
FogTorchΠ can be run by importing the project in any IDE (e.g., NetBeans or Eclipse).

To start with FogTorchΠ, simply create a main file and declare a new Fog infrastructure and application:

``` java
Infrastructure I = new Infrastructure();
Application A = new Application();
```

Starting by the infrastructure, one can add Fog and Cloud nodes (which are assumed to have unbounded hardware capabilities) as shown below:

``` java
// parameters: node_id, software capabilities, latitude, longitude
I.addCloudDatacentre("cloud1", asList("linux", "php", "mySQL", "python"), 52.195097, 3.0364791);
I.addCloudDatacentre("cloud2", asList("linux", "php", "mySQL", "java"), 44.123896, -122.781555);
// parameters: node_id, software capabilities, hardware capabilities latitude, longitude
I.addFogNode("fog1", asList("linux", "php", "mySQL"), new Hardware(2, 2, 32), 43.740186, 10.364619);
I.addFogNode("fog2", asList("linux", "php"), new Hardware(2, 2, 32), 43.7464449, 10.4615923);
I.addFogNode("fog3", asList("linux", "mySQL"), new Hardware(4,2,64), 43.7381285, 10.4552213);
```

Communication links in the infrastructure are instantiated by specifying (or by sampling) their QoS profile in terms of latency and bandwidth. In the main file, one can specify a sampling function, for instance:

``` java
//Bernoulli sampling function.
public static Random rnd = new Random();
public static QoSProfile samplingFunction(double probability, QoSProfile q1, QoSProfile q2) {
    double rand = rnd.nextDouble();
    if (probability == 1) {
        return q1;
    }
    if (rand < probability) {
        return q1;
    } else {
        return q2;
    }
}
```

Then, links are added to the infrastructure as shown in the (partial) example below.

``` java
// QoSprofile(int latency, double bandwidth)
QoSProfile fogtoCloudDownload = samplingFunction(0.98, new QoSProfile(40, 10.5), new QoSProfile(Integer.MAX_VALUE, 0.0));
QoSProfile fogtoCloudUpload = samplingFunction(0.98, new QoSProfile(40, 4.5), new QoSProfile(Integer.MAX_VALUE, 0.0));
// parameters: endpoint1, endpoint2, bandwidth2->1, bandwidth1->2
I.addLink("fog1", "cloud1", fogtoCloudDownload, fogtoCloudUpload);
I.addLink("fog1", "cloud2", fogtoCloudDownload, fogtoCloudUpload);
``` 

Things to the infrastructure are added as follows:

``` java
//parameters: thing_id, type, latitude, longitude, directly connected Fog node
I.addThing("water0", "water", 43.7464449, 10.4615923, "fog1");
            I.addThing("video0", "video", 43.7464449, 10.4615923, "fog1");
            I.addThing("moisture0", "moisture", 43.7464449, 10.4615923, "fog1");
            I.addThing("temperature0", "temperature", 43.7464449, 10.4615923, "fog3");
``` 

Now let's go back to the application. Suppose component A requires to exploit some Things in the infrastructure. We specify them as:

``` java
//parameters: identifier, type, latitude, longitude, directly connected Fog node
ArrayList<ThingRequirement> neededThings = new ArrayList<>();
//parameters: thing_id, needed fog-to-thing QoS, needed thing-to-Fog QoS
neededThings.add(new ExactThing("moisture0", new QoSProfile(500,0.1), new QoSProfile(500, 0.1))); // 0.5 s and 1 Mbps
neededThings.add(new ExactThing("temperature0", new QoSProfile(65,0.1), new QoSProfile(65, 0.1))); // 110 ms and 1 Mbps
``` 
And then component A is specified as follows (with other components).

``` java
//parameters: id, software requirements, hardware requirements, (neededThingsList)*
A.addComponent("A", asList("linux"), new Hardware(1, 1.2, 8), neededThings);
A.addComponent("B", asList("linux", "mySQL"), new Hardware(1, Bram, Bstorage)); //cores ram storage
A.addComponent("C", asList("linux", "php"), new Hardware(2, 0.7, 4));
```

Links in between components are specified as:

``` java
//parameters: endpoint1, endpoint2, latency, bandwidth2->1, bandwidth1->2
A.addLink("A", "B", 160, 0.5, 3.5);
A.addLink("A", "C", 140, 0.4, 1.1);
A.addLink("B", "C", 100, 0.8, 1);
```
To look for eligible deployments of A over I, the class ``` java Search``` is instantiated, adding (if needed) the list of nodes upon which a component can be deployed (i.e., business policies).

``` java
//parameters: id, software requirements, hardware requirements, (neededThingsList)*
Search s = new Search(A, I);
s.addBusinessPolicies("C", asList("cloud2", "cloud1"));
```

Finally, to start the search:

``` java
s.findDeployments(true); // true is to perform exhaustive search instead of heuristics
``` 

To repeat FogTorchΠ execution and perform Monte Carlo simulations, one may simply insert the previous code in the for-loop of our class ``` Main.java```. The output is a CSV file where each line represents an eligible deployment along with its QoS-assurance, average position in the list of returned deployment (
``` heuristic rank``` ), consumed RAM and storage in the Fog layer, sum of the last two.

``` 
Deployment, QoS-assurance, Heuristic Rank, Consumed RAM, Consumed HDD, Sum Hardware
[A->fog2][B->cloud2][C->cloud1];99.122;54.75732778;0.2;0.0625;0.13125
[A->fog2][B->cloud2][C->cloud2];99.122;59.73526667;0.2;0.0625;0.13125
[A->fog2][B->cloud1][C->cloud2];99.122;44.80145;0.2;0.0625;0.13125
[A->fog2][B->cloud1][C->cloud1];99.122;39.82351111;0.2;0.0625;0.13125
[A->fog3][B->cloud1][C->fog2];99.122;94.14406111;0.316666667;0.09375;0.205208333
[A->fog2][B->cloud2][C->fog2];99.122;49.77938889;0.316666667;0.09375;0.205208333
[A->fog3][B->cloud2][C->fog2];99.122;99.122;0.316666667;0.09375;0.205208333
[A->fog2][B->cloud1][C->fog2];99.122;34.84557222;0.316666667;0.09375;0.205208333
[A->fog1][B->cloud2][C->fog2];95.191;76.1528;0.316666667;0.09375;0.205208333
[A->fog1][B->cloud1][C->fog2];95.191;71.39325;0.316666667;0.09375;0.205208333
[A->fog2][B->fog3][C->cloud2];99.122;29.86763333;0.366666667;0.125;0.245833333
[A->fog2][B->fog3][C->cloud1];99.122;24.88969444;0.366666667;0.125;0.245833333
[A->fog2][B->fog3][C->fog1];100;20.35075556;0.483333333;0.15625;0.319791667
[A->fog1][B->fog3][C->fog2];100;70.34964444;0.483333333;0.15625;0.319791667
[A->fog1][B->fog3][C->fog1];100;65.26195556;0.483333333;0.15625;0.319791667
[A->fog2][B->fog3][C->fog2];100;15.26306667;0.483333333;0.15625;0.319791667
[A->fog2][B->fog1][C->fog1];100;10.17537778;0.483333333;0.15625;0.319791667
[A->fog2][B->fog1][C->fog2];100;5.087688889;0.483333333;0.15625;0.319791667
[A->fog3][B->fog1][C->fog2];100;90.04412222;0.483333333;0.15625;0.319791667
[A->fog3][B->fog1][C->fog1];100;84.95643333;0.483333333;0.15625;0.319791667

``` 

To specify upon which Fog nodes QoS-assurance and resources consumption are evaluated, it is sufficient to add the line:

``` java
s.addKeepLightNodes(asList("fog1", "fog2"));
``` 
# Example
A full example is in file [Main.java](https://github.com/di-unipi-socc/FogTorchPI/blob/master/src/main/Main.java) and related results can be found in [resultsplotwithHD.xlsx](https://github.com/di-unipi-socc/FogTorchPI/tree/master/results).


