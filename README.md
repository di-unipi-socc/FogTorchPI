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