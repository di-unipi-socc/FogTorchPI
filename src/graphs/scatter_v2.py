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
import math

abspath =  os.path.abspath(__file__)
wk = abspath[0:abspath.index("src/graphs/scatter_v2.py")]

parser = argparse.ArgumentParser(
    formatter_class=argparse.ArgumentDefaultsHelpFormatter)

parser.add_argument(
    '--file',
    type=str,
    default="ExampleSMALL2apps",
#     default="BigExample",
#     default="BigExample10",

    help='File name')

args, pipeline_args = parser.parse_known_args()

deployments={}

pathExperiment = wk+"results/IEServices19/"
pathfile = pathExperiment+ args.file +".txt"

df = pd.read_csv(pathfile,header=None, sep=';')
#It controls the freq. of appear in GA
df[6]=df[6].astype(str)

df["GAfreq"]=df[6].apply(lambda x: len(x.split("."))-1)

df[3].loc[df[0]=="GA"]=df[3].loc[df[0]=="GA"]/df["GAfreq"]


df.head(3)
dg = df.groupby([3,4,5]).agg({0:list})


marker_GA = dict(color='green',  marker='s',alpha=.5,s=180) #,label = "GA & MC")
marker_EX = dict(color='red',  marker='o',s=30)# ,label="Only MC")


#FIGURE
fig = plt.figure(figsize=(14, 8))
ax = fig.add_subplot(111, projection='3d')

for index in dg.index:

    x = index[0]
    y = index[1]
    z = index[2]
    
    unique, counts = np.unique(dg.loc[index][0], return_counts=True)
    d = dict(zip(unique, counts))
    nGA = 0 
    nEX = 0
    if 'GA' in d.keys():
        nGA = d['GA']
    if 'EX' in d.keys():
        nEX = d['EX']

    if nGA >0:
        if x!=100.0 and y!=90.0	and z!=155.2: #CASE RRARO
            ax.text(x+2,y+0.7,z+1, '%i' % (nGA), size=10, zorder=1,  color='green') 
#        ax.text(x+0.5,y+0.007,z, '%i' % (nGA), size=10, zorder=1,  color='green') #CASE BIG
            ax.scatter(x,y ,z, **marker_GA)

     #MANUAL !!! CASE BIG 10APPS GA       
#    if x==100.0 and y==2.707156308851224	and z==5730.87:
#        print "HERE"
#        ax.scatter(x, y, z,s=880, facecolors='none', edgecolors='r')


        

    if nEX >0:
        if nEX>1000:
            nEX = str(nEX/1000)+"k"
#        ax.text(x+2,y,z-4, '%s' %(nEX), size=8, zorder=1,  color='red') 
        ax.scatter(x,y ,z, **marker_EX)    

ax.set_xlabel('QoS assurance',size=16) 
ax.set_ylabel('Fog Resource Consumption',size=16) 
ax.set_zlabel('Cost',size=16)

patch = mpatches.Patch(color='green', label = "GA & EX")
patch3 = mpatches.Patch(color='red', label='EX')


#leg = fig.legend( [patch,patch3],['Genetic algorithm','Exhaustive Search'], loc = 'lower center', ncol=2, labelspacing=0.,prop={'size':14})
leg = fig.legend( [patch],['Genetic algorithm'], loc = 'lower center', ncol=2, labelspacing=0.,prop={'size':14})
bb = leg.get_bbox_to_anchor().inverse_transformed(ax.transAxes)           
yOffset = 0.08
bb.y0 += yOffset
bb.y1 += yOffset
leg.set_bbox_to_anchor(bb, transform = ax.transAxes)

plt.savefig(pathExperiment+args.file+".pdf")
plt.show()
print pathExperiment+args.file+".pdf"