# FAT- Face Annotation Tool
This is an Java implementation of Face Annotation Tool. It helps you to mark faces 
using **ellipses** ([FDDB](http://vis-www.cs.umass.edu/fddb/) style) in stead of common bounding boxes.


### Usage:
#### 1. Load Images
Choose a txt file contains absolute paths of images.

#### 2. Load Image Annotations (Optional)
Choose a txt file has the following format:

```...
<image name i>
<number of faces in this image =im>
<face i1>
<face i2>
...
<face im>
...
```

Here, each face is denoted by:\
`<major_axis_radius minor_axis_radius angle center_x center_y 1>`.\
(The 1 in the last of each face can be any value since it won't be read.)

The image names have to be identical to the absolute paths of images you loaded in previous step.\
Or you can have relative paths in the annotation file but with **prefix** and **suffix**\
set up in prompts.

#### 3. Mark Faces
1. **Left click** to add a key point. Three key points to form an ellipse mark.
2. **Shift + Mouse Move** to activate a new marked ellipse.
3. **Right click** to cancel a key point of an activated ellipse or an unfinished 
key point.
4. Click the coordinates on the right table to highlight the corresponding ellipse.

#### 4. Output Your Work
Output your work to a txt file with an identical format of input annotation file.

### Testing with FDDB images:
Get fddb images and annotations by executing `download_fddb.sh`

### Demo:

1. **Left click** to add key points.
  ![](./gif/leftclicktoadd.gif)

  

  

  

2. Click a coordinate on the right table to highlight an ellipse.
![](gif/selectOntable.gif)

3. **Shift + Mouse Move** to active a new marked ellipse.
![](gif/shiftSelect.gif)

4. **Right click** to cancel a key point of an activated ellipse or an unfinished 
key point.
![](gif/rightClick.gif)

### TODO:
- [x]  basic functions
- [x]  ask for prefix and suffix when load imglist
- [x]  shift choose on ellipse -> highlight right table row
- [x]  click on right table row(if static) -> enlarge stroke
- [x]  click on right table row(if non-static) -> set as activated ellipse
- [x]  load imglist -> disable load img button
- [x]  one click after load -> disable load annotation button
- [x]  load annotation -> disable load annotation button
- [x]  output file function 
- [x]  switch to another image -> clear the unfinished key point stack.
