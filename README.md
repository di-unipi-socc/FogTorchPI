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

```