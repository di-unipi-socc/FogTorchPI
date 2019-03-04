#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Wed Feb 27 16:30:51 2019

@author: isaaclera
"""

# This import registers the 3D projection, but is otherwise unused.
from mpl_toolkits.mplot3d import Axes3D  # noqa: F401 unused import


import numpy as np
import matplotlib.pyplot as plt
from matplotlib.lines import Line2D
import re
import pandas as pd
import matplotlib.patches as mpatches
import os
import argparse

abspath =  os.path.abspath(__file__)
wk = abspath[0:abspath.index("src/graphs/scatter3d.py")]

parser = argparse.ArgumentParser(
    formatter_class=argparse.ArgumentDefaultsHelpFormatter)

parser.add_argument(
    '--file',
    type=str,
#    default="simpleExample1",
#    default="BigExample1_30",
    default="simpleExample1_4apps",
    
    help='File name')

args, pipeline_args = parser.parse_known_args()

deployments={}

pathExperiment = wk+"results/IEServices19/"
pathfile = pathExperiment+ args.file +".txt"

rhead = '\d*\s-\s'
prog = re.compile(rhead)


MC = False
lcase,ldeploy,lqos,lrc,lcost = [],[],[],[],[]
with open(pathfile,"r") as f:
    data = f.readlines()
    for l in data:
#        print l
        if "MC" in l:
            MC = True            
        elif "GA" in l:
#            print "HERE"
            MC = False
            
        else:
            col =  l.split(";")
            if len(col)>3:
                deploy = re.split(rhead,col[0])[1]
    
                qos = float(col[1])
                rc = float(col[2])
                cost = float(col[3])
                #print deploy,qos,rc,cost
                napp=1.0
                if not MC:
#                    print len(col[4].split(":"))-1
                    napp = len(col[4].split(":"))-1

                lcase.append(MC)
                ldeploy.append(deploy)
                lqos.append(qos/float(napp))
                lrc.append(rc)
                lcost.append(cost)
                

                  
#print "Preparing data"
                
df = pd.DataFrame({"case":lcase,"deployment":ldeploy,"qos":lqos,"rc":lrc,"cost":lcost})

df["GA"]=(df.case==False)*1
df["MC"]=(df.case!=False)*1
#df["GAb"] = df["GA"]*-1
#df["MCb"] = df["MC"]
#df["count"]=np.ones(len(df))

dg = df.groupby("deployment").agg({'case':list,'qos':list,
               "rc":list,"cost":list}) #rc and cost are equals            
#dg["diff"]=  df.groupby("deployment")[("count")].diff().fillna(0)
#Only Results GA+MC 
#for i,l in enumerate(dg["case"]):
#    if np.array(l).sum()==0:
#        print i,l
        
#It relates the number of solutions with GA y MC 

dc = df.groupby(["qos","rc","cost"]).agg({"GA":sum,"MC":sum,})         
#dc["diff"] = df.groupby(["qos","rc","cost"])[("MCb")].diff().fillna(0)
dc  =dc.stack().reset_index()

marker_GAMC = dict(color='green',  marker='s',alpha=.5,s=180) #,label = "GA & MC")
#marker_both = dict(color='green',  marker='s',alpha=.1,s=100) #, label="Boths")
marker_MC = dict(color='red',  marker='o',s=30)# ,label="Only MC")

#TODO TEXTO con soluciones sobre cada punto

fig = plt.figure(figsize=(14, 8))
ax = fig.add_subplot(111, projection='3d')

for item in dg.iterrows():
    nmc=0
    ngc=0
#    print item
#    print len(item[1]["case"])

    x = item[1]["qos"][0]
    y = item[1]["rc"][0]
    z = item[1]["cost"][0]


    marker = marker_MC
    if len(item[1]["case"])==1 :
        if item[1]["case"][0]==False:
           pass
        else:
           marker = marker_MC
#           print "MC"
    else:
        marker= marker_GAMC
#        print "Boths"
    
#    print(item[1]["case"]) 

    
    
            
            
#    if item[1]["case"].count(False)==1: marker = marker_GAMC
#    if len(item[1]["case"])==2: marker = marker_both
#    print marker
  
#    nmc = dc.loc[(dc["level_3"]=="MC") & (dc["qos"]==x) & (dc["rc"]==y) & (dc["cost"]==z)][0].values[0]
    

    if marker == marker_GAMC:    
        nmc = dc.loc[(dc["level_3"]=="MC") & (dc["qos"]==x) & (dc["rc"]==y) & (dc["cost"]==z)][0].values[0]
        nga = dc.loc[(dc["level_3"]=="GA") & (dc["qos"]==x) & (dc["rc"]==y) & (dc["cost"]==z)][0].values[0]    


#        if nmc == 0:
#           ax.text(x+1,y,z+1, '(%i)' % (nga), size=10, zorder=1,  color='green') 
#        else:    
            #TODO CHECK some points not red?
        ax.text(x+1,y,z+1, '(%i)' % (nga), size=10, zorder=1,  color='green') 
        ax.scatter(x,y ,z, **marker)
        ax.scatter(x,y ,z, **marker_MC)
           
    if marker == marker_MC:  
        nmc = dc.loc[(dc["level_3"]=="MC") & (dc["qos"]==x) & (dc["rc"]==y) & (dc["cost"]==z)][0].values[0]
        ax.text(x+1,y,z-4, '%i' %(nmc), size=8, zorder=1,  color='red') 
        ax.scatter(x,y ,z, **marker)
#    if marker == marker_GAMC:  
#        nga = dc.loc[(dc["level_3"]=="GA") & (dc["qos"]==x) & (dc["rc"]==y) & (dc["cost"]==z)][0].values[0]    
#        ax.text(x-0.8,y+0.5,z, '%i' % (nga), size=18, zorder=1,  color='blue') 
#    if nmc!=0:
#        ax.scatter(x,y ,z, **marker)
#    break;

ax.set_xlabel('QoS assurance',size=16) 
ax.set_ylabel('Fog Resource Consumption',size=16) 
ax.set_zlabel('Cost',size=16)

patch = mpatches.Patch(color='green', label = "GA & MC")
#patch2 = mpatches.Patch(color='green', label='Boths')
patch3 = mpatches.Patch(color='red', label='MC')
fig.legend( [patch,patch3],['Genetic algorithm','Exhaustive Search'], loc = 'lower center', ncol=5, labelspacing=0.)
           

plt.savefig(pathExperiment+args.file+".pdf")
plt.show()
print pathExperiment+args.file+".pdf"