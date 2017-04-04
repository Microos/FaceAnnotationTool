package hk.microos.tools;

import java.util.ArrayList;

import hk.microos.data.Point_;
import hk.microos.frames.MyImagePanel;

public class ClickManager {
	private MyImagePanel panel;

	public ClickManager(MyImagePanel panel) {
		this.panel = panel;
	}
	public void rightClick(int globalX, int globalY){
		if (!panel.isInited())
			return;
		if (!UniverseTool.inBound(panel.minX, panel.minY, panel.maxX, panel.maxY, globalX, globalY))
			return;
		panel.removeActived();
	}
	public void leftClick(int globalX, int globalY) {
		if (!panel.isInited())
			return;
		if (!UniverseTool.inBound(panel.minX, panel.minY, panel.maxX, panel.maxY, globalX, globalY))
			return;
		Point_ p = new Point_(globalX, globalY);
		panel.addUnfinishedPoint(p);
	}

	public void mouseAt(int globalX, int globalY) {
		if (!panel.isInited())
			return;
		if (!UniverseTool.inBound(panel.minX, panel.minY, panel.maxX, panel.maxY, globalX, globalY))
			return;
		if (panel.waitLastPoint) {
			panel.setLiveXY(globalX, globalY);
			panel.repaint();
		}
	}
	public void mouseShiftMoveAt(int globalX, int globalY){
		if (!panel.isInited())
			return;
		if (!UniverseTool.inBound(panel.minX, panel.minY, panel.maxX, panel.maxY, globalX, globalY))
			return;
		panel.activateClosest(globalX, globalY);
		
	}
}
