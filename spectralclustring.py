# -*- coding: utf-8 -*-
"""
Created on Mon Aug 13 20:34:15 2018

@author: mayi
"""

from sklearn.cluster import SpectralClustering
from sklearn.cluster import KMeans
from sklearn import metrics
import csv
import numpy as np

if __name__ == "__main__":   
    #X是训练集
    
    f1=open(r"C:\Users\mayi\Desktop\论文二次数据-图表\实验数据\55-24h-1m.csv","rb")
    
    data_read=np.loadtxt(f1,delimiter=",",skiprows=0)
    f1.close()
    X=np.array(data_read)
    raw_label=(1,2,1,0,0,2,2,1,0,2,2,1,2,0,2,1,1,1,1,0,0,0,2,1,0,2,1,1,0,0,0,1,1,0,2,2,2,2,1,2,2,1,1,2,2,0,1,0,0,2,0,0,2,0,0)
#    for index, neig in enumerate((8,10,15,20)):
    spectral=SpectralClustering(n_clusters=3,affinity='nearest_neighbors',
                                        gamma=1,n_neighbors=8,assign_labels='discretize').fit_predict(X)
    print(spectral)
#        count=0
#        idx=0
#        if(raw_label[idx] == spectral[idx] and idx<55):
#            count+=1
#            idx+=1
#        count/=55;
#        print(count)
#    
    #km=KMeans(n_clusters=3,init='k-means++',n_init=10,max_iter=300,tol=0.0001).fit_predict(X)
    #print(km)