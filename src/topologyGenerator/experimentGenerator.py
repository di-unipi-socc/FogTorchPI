#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Fri Nov 16 12:41:44 2018

@author: isaaclera
"""

import re
import networkx as nx
import random
import numpy as np


def getGraph(path):

    G = nx.Graph()

    #possible useful data  
    tiers = {}
    gatewaysDevices =[]
    #
    
    with open(path, "r") as f:
        for line in f.readlines():
            # There is no documented aShiip format but we assume that if the line
            # does not start with a number it is not part of the topology
            if line[0].isdigit():
                node_ids = re.findall("\d+", line)
                if len(node_ids) < 3:
                    raise ValueError('Invalid input file. Parsing failed while '\
                                     'trying to parse a line')
                node = int(node_ids[0])-1 
                # THE FIRST NODES is ZERO-id
                level = int(node_ids[1])
                tiers[node] = level
                if level >=4 and node != idcloud:
                    gatewaysDevices.append(node)
                    
                G.add_node(node, level=level)
                for i in range(2, len(node_ids)):
                    G.add_edge(node, int(node_ids[i])-1)
    
    return G


version_linux = ['2.5', '2.0', '1.0']
p_vl = [0.5, 0.4, 0.1]

cores   = [1, 2, 4, 6, 8, 16]
p_cores = [0.3,0.3,0.2,0.1, 0.05 ,0.05] 

ram =     [1, 2, 4,   8, 16,32]
p_ram = [0.2,0.4,0.2,0.1,0.05,0.05]

storage = [10,20,30]
p_stg = [0.3,0.4,0.3]

lat = [10,20,40,60,80]
bw = [3.,6.,9.,20.]


numberMaxOfCouplesInLink = 4

probThings = 0.1

def getCore():
    """
    return core, cost
    """
    c =  np.random.choice(cores,1,p=p_cores)
    #a factor 3.0 -fixed-
    return c, c*3.0

def getRAM():
    r =  np.random.choice(ram,1,p=p_ram)
    return r,r*4.0

def getStorage():
    stg = np.random.choice(storage,1,p=p_stg)
    return stg,stg*2.0


def getLinuxVersion():
    return np.random.choice(version_linux, 1,p=p_vl)

def getPoint():
   return np.random.uniform(-180,180), np.random.uniform(-90, 90)

def getLAT():
    return  np.random.choice(lat,1)
def getBW():
    return np.random.choice(bw,1)

def getCostThing():
    return np.random.random()

def getValidPSum(nc):
  while True:
        pnc = np.random.randint(1,10,size=nc) 
        pnc = pnc/float(pnc.sum())
        pnc = np.array([round(x,3) for x in pnc])
        pnc[0] = 1.0-pnc[1:].sum()
        if pnc.sum()==1.0:
            return pnc

        
def networkGeneration(G,idcloud,f): 
    f.write("// EDGES\n")
    for i in G.nodes:
        #CLUSTER
        if i == int(idcloud):
            st =  'I.addCloudDatacentre("cloud-%i",'%i
            st += 'asList(new Couple("linux", %f)),'%getLinuxVersion()
            st += "%f, %f,"%(getPoint())
            st += "new Hardware(0, 0, 0, 2.0, 3.0, 1.0));"
#            print st
            f.write(st+"\n")
        
        #FOG NODE
        else:      
           core,corecost = getCore()
           ram,ramcost = getRAM()
           storage,storagecost = getStorage()
           
           st  = 'I.addFogNode("fog-%i",'%i
           st += 'asList(new Couple("linux", %f)),'%getLinuxVersion()
           st += 'new Hardware(%i, %i, %i, %f, %f, %f),'%(core,ram,storage,corecost,ramcost,storagecost)
           st += '%f, %f);'%(getPoint())
#           print st
           f.write(st+"\n")

    #LINKS
    f.write("\n\n // LINKS \n")
    for e in G.edges:
        src = "fog-%i"%e[0]
        dst = "fog-%i"%e[1]
   
        if e[0] == idcloud:
            src = "cloud-%i"%e[0]
        if e[1] == idcloud:
            dst = "cloud-%i"%e[1]
            
        st   =  'I.addLink("%s", "%s",'%(src,dst)
        st += ' new QoSProfile(asList('
        
        nc = np.random.randint(1,numberMaxOfCouplesInLink)
        pnc = getValidPSum(nc)
               
        for c in range(nc):
            st+=  'new Couple(new QoS(%i, %i), %f)' %(getLAT(),getBW(),pnc[c])
            if c<nc-1:
                st+=","
        st+=')),' 
               
        nc = np.random.randint(1,numberMaxOfCouplesInLink)
        pnc = getValidPSum(nc)
           
           
        st += ' new QoSProfile(asList('
        for c in range(nc):
            st+=  'new Couple(new QoS(%i, %i), %f)' %(getLAT(),getBW(),pnc[c])
            if c<nc-1:
                st+=","
        st+=')));' 

#        print st
        f.write(st+"\n")
    
    
    
    #THINGS
    f.write("\n\n // THINGS \n")
    idT = 0
    for i in G.nodes:
        if np.random.random()<= probThings and i != idcloud:            
              st  =  'I.addThing("t%s", "type",'%(idT)
              st +=  "%f,%f,"%(getPoint())
              st += '"fog-%i"'%i
              st += ",%f);"%getCostThing()
              f.write(st+"\n")
              idT+=1
              
    return G

        
if __name__ == '__main__':
    random.seed(0)
    np.random.seed(0)
#    path = "GLP-400n-idc94.txt"
#    idcloud = 94

    path = "GLP-200n-idc153.txt"
    idcloud = 153

    G = getGraph(path)
    nx.write_gexf(G,"network-tmp.gexf")
    

    with open("JavaInfrastructure.json","w") as f:
        f.write("//Autogenerated INFRASTRUCTURE")
        networkGeneration(G,idcloud,f)
        f.write("//END INFRASTRUCTURE")


    
