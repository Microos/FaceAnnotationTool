package hk.microos.tools;


import hk.microos.data.Point_;
import hk.microos.frames.MyImagePanel;

public class ClickHelper {
	private MyImagePanel panel;

	public ClickHelper(MyImagePanel panel) {
		this.panel = panel;
	}
	public void rightClick(int globalX, int globalY){
		if (!panel.isInited())
			return;
		if (!UniversalTool.inBound(panel.minX, panel.minY, panel.maxX, panel.maxY, globalX, globalY))
			return;
		panel.removeActived();
	}
	public void leftClick(int globalX, int globalY) {
		if (!panel.isInited())
			return;
		if (!UniversalTool.inBound(panel.minX, panel.minY, panel.maxX, panel.maxY, globalX, globalY))
			return;
		Point_ p = new Point_(globalX, globalY);
		panel.addUnfinishedPoint(p);
	}

	public void mouseAt(int globalX, int globalY) {
		if (!panel.isInited())
			return;
		if (!UniversalTool.inBound(panel.minX, panel.minY, panel.maxX, panel.maxY, globalX, globalY))
			return;
		if (panel.waitLastPoint) {
			panel.setLiveXY(globalX, globalY);
			panel.repaint();
		}
	}
	public void mouseShiftMoveAt(int globalX, int globalY){
		if (!panel.isInited())
			return;
		if (!UniversalTool.inBound(panel.minX, panel.minY, panel.maxX, panel.maxY, globalX, globalY))
			return;
		panel.activateClosest(globalX, globalY);
		
	}
	public void clickOnRightTable(int rowIdx){
		panel.setActivatedIndex(rowIdx);
	}
}
