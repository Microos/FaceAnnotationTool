package hk.microos.frames;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.omg.CORBA.Bounds;

import hk.microos.data.Ellipse;
import hk.microos.data.Flags;
import hk.microos.data.LinearLine;
import hk.microos.data.MyImage;
import hk.microos.data.Point_;
import hk.microos.tools.UniverseTool;

public class MyImagePanel extends JPanel{
	MyImage mImg = null;
	JScrollPane fatherPanel= null;
	private boolean inited = false;
	public int minX ;
	public int minY;
	public int maxX;
	public int maxY;
	
	//used in 2-point live draw
	public boolean waitLastPoint = false;
	private LinearLine ll = null;
	public double liveX, liveY;
	private Point_ projPoint;
	private ArrayList<Point_> unfinished = new ArrayList<>();
	
	public MyImagePanel(JScrollPane fatherPanel){
		this.fatherPanel = fatherPanel;
	}
	public void setCurrentImage(MyImage myImg){
		this.mImg = myImg;
		this.inited = true;
		this.repaint();
	}
	public void setLiveXY(double x, double y){
		this.liveX = x;
		this.liveY = y;
		this.repaint();
	}
	public void paint(Graphics g){
		super.paint(g);
		if (mImg != null){
			this.minX = (mImg.w < MainFrame.defaultScrollW)? (MainFrame.defaultScrollW-mImg.w)/2 : 0;
			this.minY = (mImg.h < MainFrame.defaultScrollH)? (MainFrame.defaultScrollH-mImg.h)/2 : 0;
			this.maxX = this.minX + mImg.w; this.maxY = this.minY + mImg.h;
			g.drawImage(mImg.getImage(), this.minX, this.minY, this);
			Graphics2D g2d = (Graphics2D)g;
			drawElps(g2d, mImg.getElpses());
			drawUnfinishedPoints(g2d);
			drawLiveAssist(g2d);
		}
	}
	public void drawLiveAssist(Graphics2D g2d){
		if(!waitLastPoint) return; //make sure we are waiting the last point here
		
		//draw line between points mjA mjB
		Point_ p1 = unfinished.get(0);
		Point_ p2 = unfinished.get(1);
		g2d.setColor(Color.WHITE);
		g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		
		//draw line go throw the mid point
		Point_ midPoint = UniverseTool.midPoint(p1,p2);
		double disP1P2 = UniverseTool.distance(p1,p2);
		Point_[] ends = ll.getLineEndPoints(midPoint.x, midPoint.y,disP1P2/2);
		g2d.setColor(Color.CYAN);
		g2d.setStroke(new BasicStroke(5));
		g2d.drawLine((int)ends[0].x, (int)ends[0].y, (int)ends[1].x, (int)ends[1].y);
		
		//Project on the perpendicular line
		LinearLine pll = ll.getPependicularLinearLineAt(midPoint.x, midPoint.y);
		this.projPoint = pll.projectOnLine(liveX, liveY);
		g2d.setColor(Color.YELLOW);
		g2d.draw(UniverseTool.getPointOval(projPoint, 5));
		
		
	}
	public void drawUnfinishedPoints(Graphics2D g2d){
		Color oldColor = g2d.getColor();
		g2d.setColor(Color.CYAN);
		for(Point_ p : unfinished){
			int pointWidth = 5; //point width
			g2d.fill(UniverseTool.getPointOval(p.x, p.y, pointWidth));
		}
		g2d.setColor(oldColor);
	}
	public void drawElps(Graphics2D g2d, ArrayList<Ellipse> elpses){
		AffineTransform old = g2d.getTransform();
		//-----------------------------------------//
		
		g2d.setStroke(new BasicStroke(5));
		for (Ellipse e : elpses){
			
			
			
			//do rotation
			g2d.rotate(e.angle,e.x,e.y);
			//draw elps
			g2d.setColor(Color.GREEN);
			Ellipse2D.Double ed = e.getErectedEllipse2D();
			g2d.draw(ed);
			
			
			
			//reset transform
			g2d.setTransform(old);
			//draw keypoints
			g2d.setColor(Color.RED);
			for (Point_ p : e.getKeyPoints()){
				g2d.fill(UniverseTool.getPointOval(p, 5));
			}
			
		}
		
		
	}
	public void addUnfinishedPoint(Point_ p){
		ll = null;waitLastPoint = false;
		unfinished.add(p);
		if (unfinished.size() == 2){
			waitLastPoint = true;
			ll = new LinearLine(unfinished.get(0),unfinished.get(1));
			repaint();
			
		}
		if (unfinished.size() == 3){
			mImg.addElps(new Ellipse(unfinished.get(0), unfinished.get(1), this.projPoint));
			unfinished = new ArrayList<>();
		}
	}
	public boolean isInited(){
		return inited;
	}
}
