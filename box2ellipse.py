'''
By placing "fold-??-out.txt" files under "./fddb/out-fold" directory, this
script will be able to convert rectangle output into ellipse output.
Then these converted output can be used as annotations inspected in
our F.A.T..
'''
import os
import numpy as np

erect_angle =  -90 * np.pi / 180

this_dir = os.path.dirname(__file__)
assert os.path.exists(this_dir+"/fddb/out-fold"), "fddb output folder 'out-fold' not found"
os.chdir(this_dir+'/fddb')
prefix = os.getcwd()
suffix = ".jpg"
x=os.popen("pwd | sed 's/ /\\ /g'").read().strip()

out_list_files = os.popen("find '{}/out-fold' -name '*out.txt'".format(x)).read().split('\n')


s = ""
for p in out_list_files:
    if p == '' :
        continue    
    with open(p,'r') as f:
        lines = f.readlines()

    lines = [l.replace('\n','') for l in lines]
    for i in range(len(lines)):
        l = lines[i]
        if(len(l.split("/")) == 5):
            lines[i] = os.path.join(prefix, l+suffix)
            continue
        coords = l.split(" ")
        if(len(coords) >= 5):
            xmin = float(coords[0])
            ymin = float(coords[1])
            w = float(coords[2])
            h = float(coords[3])
            mj = h/2
            mi = w/2
            x = xmin + mi
            y = ymin + mj
            lines[i] = "{} {} {} {} {}  {}".format(mj,mi,erect_angle,x,y,1)
            
            
    s += '\n'.join(lines)+'\n'

with open("./MergedOutputs.txt",'w') as f:
    f.write(s)


print "Merged outputs wrote to: "+os.path.join(os.getcwd(),"MergedOutputs.txt")

