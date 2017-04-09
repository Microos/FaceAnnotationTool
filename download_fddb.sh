#!/bin/bash

echo "Downloading fddb images"

mkdir fddb && cd fddb
wget http://tamaraberg.com/faceDataset/originalPics.tar.gz
echo "Unzip..."
tar -xzf originalPics.tar.gz
rm -f originalPics.tar.gz

wget http://vis-www.cs.umass.edu/fddb/FDDB-folds.tgz
echo "Unzip..."
tar -xzf FDDB-folds.tgz
rm -f FDDB-folds.tgz

cd ..
python gen_absolute_path.py


