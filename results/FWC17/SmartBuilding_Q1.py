# #data for Q1
ann = ['Δ2','Δ3','Δ4','Δ7','Δ10']
x = [99.2,100.0,100.0,100.0,100.0]
y = [48.4,48.4,59.2,59.2,59.2]
z = [798.7,829.7,844.7,801.7,809.7]

import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from mpl_toolkits.mplot3d import proj3d
import seaborn as sns
import matplotlib.cm as cm
import numpy as np

sns.set_style(style='white')
color = sns.color_palette("Set2", 10)
colmap = cm.ScalarMappable(cmap=cm.viridis)
colmap.set_array(y)



############
# 3d scatter plot
############
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
lab =  ["QoS-assurance" ,"Fog Resource Consumption",  "Cost"]
ax.set_xlabel(lab[0], labelpad=10)
ax.set_ylabel(lab[1], labelpad=10,rotation=-45)
ax.set_zlabel(lab[2], labelpad=10, rotation=90)
ax.scatter(x, y, z, marker='o',s=120, c=np.array(y)/max(y), cmap = plt.get_cmap("viridis"), edgecolor='gray', linewidth='1')

t=0
for x1,x2,x3 in zip(x,y,z):
    ax.text(x1,x2,x3,  '%s' % (ann[t]), size=12, zorder=1)
    ax.text(x1,x2,x3,  '%s' % (ann[t]), size=12, zorder=1)
    t+=1


cb = fig.colorbar(colmap)
plt.tight_layout()
#plt.savefig("Q1_3D.pdf", format="pdf", dpi=300)
plt.show()