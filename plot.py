#!/usr/bin/python

import numpy as np
import matplotlib.pyplot as plt
import argparse

import pandas as pd
import sys, getopt

from matplotlib.font_manager import FontProperties

parser = argparse.ArgumentParser(description='columns to plot')
parser.add_argument('columns', nargs='+')
parser.add_argument('-file', nargs='?', default='data.txt')
parser.add_argument('-sep', nargs='?', default=',')
#parser.add_argument('-legend', nargs='?', default='false')

args = parser.parse_args()
#print "args = " , args 

df = pd.read_csv(args.file,sep=args.sep)
#df = pd.read_csv('data.txt',sep=',')

fontP = FontProperties()
fontP.set_size('small')

plt.figure(1)
for item in args.columns:
    df[item].plot(figsize=(6, 6));
#plt.legend(prop = fontP)
plt.show(block=True)
