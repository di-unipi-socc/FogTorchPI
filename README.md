# FogTorchΠ
A probabilistic prototype for deployment of Fog applications.

##Intro

**FogTorchΠ** is an open source prototype, developed in Java, based on a model for Fog computing infrastructures and applications.

It takes into account non-functional parameters within the model (i.e., hardware, software, latency and bandwidth) to determine, compare and contrast different eligible deployments of a given application over a Fog infrastructure.

In the case of hardware capabilities, it considers CPU cores, RAM and storage available at a given node or required by a given software component. 

Software capabilities are represented by a list of software names (operating system, programming languages, frameworks etc).

It considers latency, and both download and upload bandwidths as QoS attributes. Latency is measured in milliseconds (ms), while bandwidth is given in Megabits per second (Mbps). 

## Quick User Guide
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

Then, links are added to the infrastructure as shown in the example below.

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
//parameters: identifier, type, latitude, longitude, directly connected Fog node
I.addThing("water0", "water", 43.7464449, 10.4615923, "fog1");
            I.addThing("video0", "video", 43.7464449, 10.4615923, "fog1");
            I.addThing("moisture0", "moisture", 43.7464449, 10.4615923, "fog1");
            I.addThing("temperature0", "temperature", 43.7464449, 10.4615923, "fog3");
``` 