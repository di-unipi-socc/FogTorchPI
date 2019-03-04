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
wk = abspath[0:abspath.index("src/graphs/fitnessFigure.py")]

parser = argparse.ArgumentParser(
    formatter_class=argparse.ArgumentDefaultsHelpFormatter)

parser.add_argument(
    '--file',
    type=str,
    default="simpleExample1", #n=10
#    default="BigExample1_30", #n=40
    help='File name')

#parser.add_argument(
#    '--gen ',
#    type=int,
#    default=30,
#    help='generations')

args, pipeline_args = parser.parse_known_args()

deployments={}

pathExperiment = wk+"results/IEServices19/"
pathfile = pathExperiment+ args.file +"_fit.txt"
n = 10


df = pd.read_csv(pathfile,header=None)

s1 = df[1][:n]
s2 = df[1][n:n*2]
#s3 = df[1][n*2:]


fig = plt.figure(figsize=(8, 4))
ax = fig.add_subplot(111)
s1.reset_index()[1].plot()
s2.reset_index()[1].plot()
#s3.reset_index()[1].plot()
ax.set_xlabel('Generation',size=16) 
ax.set_ylabel('Fitness',size=16) 
ax.legend(('Best QoS Profile', 'Worst QoS Profile'), fontsize=12,  ) #loc='lower right'
plt.savefig(pathExperiment+args.file+"_fit.pdf")
plt.show()

print pathExperiment+args.file+"_fit.pdf"







