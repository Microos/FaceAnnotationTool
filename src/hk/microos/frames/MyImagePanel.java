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
import hk.microos.data.Flags;
import hk.microos.data.LinearLine;
import hk.microos.data.MyImage;
import hk.microos.data.Point_;
import hk.microos.tools.UniversalTool;

/**
 * 
 * TODO: the 3rd point may point at out of bound when IMAGE is small
 * 
 * @author rick
 *
 */

public class MyImagePanel extends JPanel {
	private MyImage mImg = null;
	MainFrame mainFrame = null;
	JScrollPane fatherPanel = null;
	private boolean inited = false;
	public double minX;
	public double minY;
	public double maxX;
	public double maxY;

	// used in 2-point live draw
	public boolean waitLastPoint = false;
	private LinearLine ll = null;
	private double perpendicularConstrainX, perpendicularConstrainY;
	private Point_ projPoint;
	private ArrayList<Point_> unfinished = new ArrayList<>();
	// closest
	private int activedEllipseIdx = -1;
	private int highlightedStaticEllipseIdx = -1;
	public MyImagePanel(MainFrame mainFrame, JScrollPane fatherPanel) {
		this.mainFrame = mainFrame;

		this.fatherPanel = fatherPanel;
	}

	public MyImagePanel(JScrollPane fatherPanel) {
		this.fatherPanel = fatherPanel;
	}

	public boolean setCurrentImage(MyImage myImg) {
		if (this.mImg != null && myImg.equals(this.mImg)) {
			// no change on img
			return false;
		}
		this.mImg = myImg;
		this.inited = true;
		activedEllipseIdx = -1;
		highlightedStaticEllipseIdx = -1;
		this.repaint();
		return true;
	}

	public void setLiveXY(double x, double y) {
		this.perpendicularConstrainX = x;
		this.perpendicularConstrainY = y;
		this.repaint();
	}

	private void setOffset(double[] offset) {
		this.minX = offset[0];
		this.minY = offset[1];
		this.maxX = offset[2];
		this.maxY = offset[3];
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (mImg != null) {
			setOffset(UniversalTool.getOffset(mImg));
			g.drawImage(mImg.getImage(), (int) this.minX, (int) this.minY, this);

			Graphics2D g2d = (Graphics2D) g;

			// enable higher quality
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
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
			drawStaticEllipses(g2d);
		}
	}

	private int getStaticEllipsesCount() {
		ArrayList<Ellipse> es = mImg.getEllipseStatic();
		return es == null ? 0 : es.size();
	}

	public void drawStaticEllipses(Graphics2D g2d) {
		AffineTransform old = g2d.getTransform();
		// -----------------------------------------//
		ArrayList<Ellipse> staticEllipses = mImg.getEllipseStatic();
		if (staticEllipses == null)
			return;
		int i = 0;
		for (Ellipse e : staticEllipses) {
			e = e.offset(minX, minY);
			BasicStroke bs = UniversalTool.getPreferableStroke(Math.max(e.major, e.minor) * 2);

			// do rotation
			g2d.rotate(e.angle, e.x, e.y);
			// draw elps

			Ellipse2D.Double ed = e.getErectedEllipse2D();
			if(i == highlightedStaticEllipseIdx){
				g2d.setColor(Color.red);
			}else{
				g2d.setColor(Color.black);
			}
			
			g2d.setStroke(bs);
			g2d.draw(ed);

			g2d.setColor(Color.yellow);
			g2d.setStroke(new BasicStroke(bs.getLineWidth() / 2));

			g2d.draw(ed);

			// reset transform
			g2d.setTransform(old);
			i++;
		}
	}

	public void drawLiveAssist(Graphics2D g2d) {
		if (!waitLastPoint)
			return; // make sure we are waiting the last point here

		// @ Draw line between points mjA mjB
		Point_ p1 = unfinished.get(0);
		Point_ p2 = unfinished.get(1);
		BasicStroke bs = UniversalTool.getPreferableStroke(0.6 * UniversalTool.distance(p1, p2));
		bs = new BasicStroke((float) (bs.getLineWidth() * 1.1));

		// -> Draw the thicker line
		g2d.setStroke(bs);
		g2d.setColor(Color.WHITE);
		g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);

		// -> Draw the thinner line
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(bs.getLineWidth() / 3));
		g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);

		// @ Draw line go throw the mid point
		Point_ midPoint = UniversalTool.midPoint(p1, p2);
		double disP1P2 = UniversalTool.distance(p1, p2);
		Point_[] ends = ll.getPerpendicularLineEndPoints(midPoint.x, midPoint.y, disP1P2 / 2);
		g2d.setColor(Color.CYAN);
		g2d.setStroke(new BasicStroke((float) (bs.getLineWidth() * 1.3)));
		g2d.drawLine((int) ends[0].x, (int) ends[0].y, (int) ends[1].x, (int) ends[1].y);

		// @ Draw the mouse points that projected on the perpendicular line
		LinearLine pll = ll.getPependicularLinearLineAt(midPoint.x, midPoint.y);
		this.projPoint = pll.projectOnLine(perpendicularConstrainX, perpendicularConstrainY, minX, minY, maxX, maxY);

		double pointWidth = bs.getLineWidth() * 1.5; // point width

		// -> Outline points

		g2d.setColor(Color.black);
		g2d.draw(UniversalTool.getPointOval(projPoint, pointWidth));
		g2d.draw(UniversalTool.getPointOval(UniversalTool.getSymmetricPoint(midPoint, projPoint), pointWidth));

		pointWidth = pointWidth * 1.1;
		// -> points
		g2d.setColor(Color.YELLOW);
		g2d.fill(UniversalTool.getPointOval(projPoint, pointWidth));
		g2d.fill(UniversalTool.getPointOval(UniversalTool.getSymmetricPoint(midPoint, projPoint), pointWidth));

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
		if (!unfinished.isEmpty()){
			this.activedEllipseIdx = -1;
			mainFrame.coordListTH.deselect();
		}
			
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

			if (i == this.activedEllipseIdx) {
				g2d.setColor(Color.magenta);
			} else {
				g2d.setColor(Color.GREEN);
			}

			Ellipse2D.Double ed = e.getErectedEllipse2D();
			g2d.draw(ed);

			// reset transform
			g2d.setTransform(old);
			// draw keypoints

			if (i == this.activedEllipseIdx) {
				g2d.setColor(Color.white);
			} else {
				g2d.setColor(Color.red);
			}
			for (Point_ p : keyPoints) {

				g2d.fill(UniversalTool.getPointOval(p, 2.2 * bs.getLineWidth()));
			}
			i++;
		}
	}

	public void updateStatus() {
		ll = null;
		waitLastPoint = false;
		int unfinishedSize = unfinished.size();
		if (unfinishedSize == 2) {
			waitLastPoint = true;
			ll = new LinearLine(unfinished.get(0), unfinished.get(1));
		}
		int idx = -1;
		if (unfinishedSize == 3) {
			Ellipse e = new Ellipse(unfinished.get(0), unfinished.get(1), this.projPoint);
			e.setOffsetForTableDisplay(this.minX, this.minY);
			mImg.addElps(e);
			Flags.numNewEllipse++;
			unfinished = new ArrayList<>();
			this.activedEllipseIdx = mImg.getElpses().size() - 1;
			
			idx  = this.activedEllipseIdx + getStaticEllipsesCount();
			mainFrame.marksUpdatedAtSelectedImage(this.mImg);
			mainFrame.coordListTH.rightPanelSetSelectedLine(idx);
		}
		
		
		repaint();
	}

	public void addUnfinishedPoint(Point_ p) {
		mainFrame.freezeReadAnnotationBtn();
		this.highlightedStaticEllipseIdx = -1;
		unfinished.add(p);
		updateStatus();
	}

	public void activateClosest(int x, int y) {
		// if found that we still have jobs to be done OR no ellipse can be
		// deleted --> abort
		if (unfinished.size() != 0 || mImg.getElpses().size() == 0) {
			return;
		}
		Point_ p = new Point_(x, y);
		this.activedEllipseIdx = findTheClosestEllipse(p);
		this.highlightedStaticEllipseIdx = -1;
		mainFrame.coordListTH.rightPanelSetSelectedLine(this.activedEllipseIdx + getStaticEllipsesCount());
		this.repaint();
	}

	public void removeActived() {
		
		if (unfinished.size() != 0) {
			// cannot remove any ellipse, just remove unfinished point
			unfinished.remove(unfinished.size() - 1);

		} else {
			Flags.numNewEllipse--;
			ArrayList<Ellipse> elpses = mImg.getElpses();
			if (elpses.size() != 0 && this.activedEllipseIdx != -1) {
				Ellipse e = elpses.remove(this.activedEllipseIdx);
				ArrayList<Point_> keyPoints = e.getKeyPoints();
				unfinished.add(keyPoints.get(0));
				unfinished.add(keyPoints.get(1));
			}
		}
		
		updateStatus();
		mainFrame.marksUpdatedAtSelectedImage(mImg);
		
		this.highlightedStaticEllipseIdx = -1;
		this.repaint();
	}

	public int findTheClosestEllipse(Point_ p) {
		// return the closet ellipse's index
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

	public void setActivatedIndex(int rowIdx) {
		if(rowIdx < 0) {
			highlightedStaticEllipseIdx =-1;
			repaint();
			return;
		}
		highlightedStaticEllipseIdx = -1;
		int seCount = getStaticEllipsesCount();
		if (rowIdx -seCount >= 0 && unfinished.size() == 0) {
			activedEllipseIdx = rowIdx-seCount;
			
		}else{
			
			highlightedStaticEllipseIdx =rowIdx;
			activedEllipseIdx = -1;
		}
		this.repaint();
	}


	public boolean isInited() {
		return inited;
	}
}
