'''
(By default, this script will be automatically invoked by download_fddb.sh)

Manually invoke this script to generate image list and annotation files with
absolute paths.

Please make sure this script stays aside with a folder fddb which contains
sub-folders: 2002, 2003, FDDB-folds.

Invoke this script again after you move the directory of images.

'''
import os


assert os.path.exists("fddb/2002"), "fddb iamge folder 'fddb/2002' not found"
assert os.path.exists("fddb/2003"), "fddb iamge folder 'fddb/2003' not found"
assert os.path.exists("fddb/FDDB-folds"), "fddb annotation folder 'fddb/FDDB-folds' not found"

os.chdir('fddb')
prefix = os.getcwd()
suffix = ".jpg"
x=os.popen("pwd | sed 's/ /\\ /g'").read().strip()
img_list_files = os.popen("find '{}/FDDB-folds' -name '*[0-9].txt'".format(x)).read().split('\n')
anot_list_files = os.popen("find '{}/FDDB-folds' -name '*List.txt'".format(x)).read().split('\n')


s = ""
for p in img_list_files:
    if p == '' :
        continue
    with open(p,'r') as f:
      lines = f.readlines()
    lines = [os.path.join(prefix, l.replace('\n','')+suffix) for l in lines]  
    s += "\n".join(lines)+'\n'

with open("./MergedImagePath.txt",'w') as f:
    f.write(s)

s = ""
for p in anot_list_files:
    if p == '' :
        continue    
    with open(p,'r') as f:
        lines = f.readlines()

    lines = [l.replace('\n','') for l in lines]
    for i in range(len(lines)):
        l = lines[i]
        if(len(l.split("/")) == 5):
            lines[i] = os.path.join(prefix, l+suffix)
    s += '\n'.join(lines)+'\n'

with open("./MergedAnnotations.txt",'w') as f:
    f.write(s)

print "Merged image path wrote to: "+os.path.join(os.getcwd(),"MergedImagePath.txt")
print "Merged annotations wrote to: "+os.path.join(os.getcwd(),"MergedAnnotations.txt")


