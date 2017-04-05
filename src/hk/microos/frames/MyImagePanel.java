package hk.microos.frames;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.synth.SynthSpinnerUI;

import hk.microos.data.Ellipse;
import hk.microos.data.LinearLine;
import hk.microos.data.MyImage;
import hk.microos.data.Point_;
import hk.microos.tools.UniversalTool;

/**
 * 
 * TODO: 
 *  the 3rd point may point	at out of bound when IMAGE is small 
 * 
 * @author rick
 *
 */

public class MyImagePanel extends JPanel {
	MyImage mImg = null;
	JScrollPane fatherPanel = null;
	private boolean inited = false;
	public int minX;
	public int minY;
	public int maxX;
	public int maxY;

	// used in 2-point live draw
	public boolean waitLastPoint = false;
	private LinearLine ll = null;
	private double perpendicularConstrainX, perpendicularConstrainY;
	private Point_ projPoint;
	private ArrayList<Point_> unfinished = new ArrayList<>();
	// closest
	private int activedEllipseIdx = -1;

	public MyImagePanel(JScrollPane fatherPanel) {
		this.fatherPanel = fatherPanel;
	}

	public void setCurrentImage(MyImage myImg) {
		this.mImg = myImg;
		this.inited = true;
		this.repaint();
	}

	public void setLiveXY(double x, double y) {
		this.perpendicularConstrainX = x;
		this.perpendicularConstrainY = y;
		this.repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (mImg != null) {
			this.minX = (mImg.w < MainFrame.defaultScrollW) ? (MainFrame.defaultScrollW - mImg.w) / 2 : 0;
			this.minY = (mImg.h < MainFrame.defaultScrollH) ? (MainFrame.defaultScrollH - mImg.h) / 2 : 0;
			this.maxX = this.minX + mImg.w;
			this.maxY = this.minY + mImg.h;
			g.drawImage(mImg.getImage(), this.minX, this.minY, this);
			
            
			Graphics2D g2d = (Graphics2D) g;
			
			//enable higher quality
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			
			
			drawElps(g2d, mImg.getElpses());
			drawUnfinishedPoints(g2d);
			drawLiveAssist(g2d);
		}
	}


	public void drawLiveAssist(Graphics2D g2d) {
		if (!waitLastPoint)
			return; // make sure we are waiting the last point here

		// @ Draw line between points mjA mjB
		Point_ p1 = unfinished.get(0);
		Point_ p2 = unfinished.get(1);
		BasicStroke bs = UniversalTool.getPreferableStroke(0.6*UniversalTool.distance(p1, p2));
		
		//-> Draw the thicker line
		g2d.setStroke(bs);
		g2d.setColor(Color.WHITE);
		g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
		
		//-> Draw the thinner line
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(bs.getLineWidth()/3));
		g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
		
		 
		// @ Draw line go throw the mid point
		Point_ midPoint = UniversalTool.midPoint(p1, p2);
		double disP1P2 = UniversalTool.distance(p1, p2);
		Point_[] ends = ll.getPerpendicularLineEndPoints(midPoint.x, midPoint.y, disP1P2 / 2);
		g2d.setColor(Color.CYAN);
		g2d.drawLine((int) ends[0].x, (int) ends[0].y, (int) ends[1].x, (int) ends[1].y);

		// @ Draw the mouse points that projected on the perpendicular line
		LinearLine pll = ll.getPependicularLinearLineAt(midPoint.x, midPoint.y);
		this.projPoint = pll.projectOnLine(perpendicularConstrainX, perpendicularConstrainY,minX, minY, maxX, maxY);
		g2d.setColor(Color.YELLOW);
		double pointWidth = bs.getLineWidth()*2.5; // point width
		
		//-> Bigger points
		g2d.fill(UniversalTool.getPointOval(projPoint, pointWidth));
		g2d.fill(UniversalTool.getPointOval(UniversalTool.getSymmetricPoint(midPoint, projPoint), pointWidth));
		
		//-> Outline points
		pointWidth = pointWidth*1.1;
		g2d.setColor(Color.black);
		g2d.draw(UniversalTool.getPointOval(projPoint, pointWidth));
		g2d.draw(UniversalTool.getPointOval(UniversalTool.getSymmetricPoint(midPoint, projPoint), pointWidth));

	}

	public void drawUnfinishedPoints(Graphics2D g2d) {
		Color oldColor = g2d.getColor();
		g2d.setColor(Color.CYAN);
		for (Point_ p : unfinished) {
			int pointWidth = 5; // point width
			g2d.fill(UniversalTool.getPointOval(p.x, p.y, pointWidth));
		}
		g2d.setColor(oldColor);
	}

	public void drawElps(Graphics2D g2d, ArrayList<Ellipse> elpses) {
		if(!unfinished.isEmpty()) this.activedEllipseIdx = -1;
		AffineTransform old = g2d.getTransform();
		// -----------------------------------------//
		
		int i = 0;
		for (Ellipse e : elpses) {
			ArrayList<Point_> keyPoints = e.getKeyPoints();
			
			BasicStroke bs = UniversalTool.getPreferableStroke(Math.max(e.major, e.minor));
			
			g2d.setStroke(bs);
			// do rotation
			g2d.rotate(e.angle, e.x, e.y);
			// draw elps
			
			if(i == this.activedEllipseIdx){
				g2d.setColor(Color.magenta);
			}else{
				g2d.setColor(Color.GREEN);
			}
			
			
			Ellipse2D.Double ed = e.getErectedEllipse2D();
			g2d.draw(ed);

			// reset transform
			g2d.setTransform(old);
			// draw keypoints
			
			if(i == this.activedEllipseIdx){
				g2d.setColor(Color.white);
			}else{
				g2d.setColor(Color.red);
			}
			for (Point_ p : keyPoints) {
				g2d.fill(UniversalTool.getPointOval(p, 2.2*bs.getLineWidth()));
			}
			i++;
		}
	}

	public void updateStatus() {
		ll = null;
		waitLastPoint = false;
		if (unfinished.size() == 2) {
			waitLastPoint = true;
			ll = new LinearLine(unfinished.get(0), unfinished.get(1));
		}
		if (unfinished.size() == 3) {
			mImg.addElps(new Ellipse(unfinished.get(0), unfinished.get(1), this.projPoint));
			unfinished = new ArrayList<>();
			this.activedEllipseIdx = mImg.getElpses().size()-1;
		}
		repaint();
	}

	public void addUnfinishedPoint(Point_ p) {

		unfinished.add(p);
		updateStatus();
	}

	public void activateClosest(int x, int y) {
		// try to remove the closest one, if the unfinished is clear
		if (unfinished.size() != 0 || mImg.getElpses().size() == 0) {
			return;
		}
		// when it indeed is clear
		Point_ p = new Point_(x, y);
		this.activedEllipseIdx = findTheClosestEllipse(p);
		this.repaint();
	}

	public void removeActived() {
		if (unfinished.size() != 0) {
			// canont remove any ellipse, just remove unfinished
			unfinished.remove(unfinished.size() - 1);
			
		} else {
			ArrayList<Ellipse> elpses = mImg.getElpses();
			if (elpses.size() != 0 && this.activedEllipseIdx != -1) {
				Ellipse e = elpses.remove(this.activedEllipseIdx);
				ArrayList<Point_> keyPoints = e.getKeyPoints();
				unfinished.add(keyPoints.get(0));
				unfinished.add(keyPoints.get(1));
			}
		}
		updateStatus();
	}

	public int findTheClosestEllipse(Point_ p) {
		// return index
		double minDis = Double.MAX_VALUE;
		int minIdx = -1;
		ArrayList<Ellipse> elpses = mImg.getElpses();
		for (int i = 0; i < elpses.size(); i++) {
			Point_ c = elpses.get(i).getCenter();
			double dis = UniversalTool.distance(c, p);
			if (dis < minDis) {
				minDis = dis;
				minIdx = i;
			}
		}
		return minIdx;
	}

	public boolean isInited() {
		return inited;
	}
}
