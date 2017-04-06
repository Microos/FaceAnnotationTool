package hk.microos.data;

import java.util.ArrayList;

import hk.microos.tools.UniversalTool;

public class LinearLine {
	private boolean vertical = false;
	private boolean horizontal = false;
	public double k, b;

	public LinearLine(double k, double b) {
		this.k = k;
		this.b = b;
	}

	public LinearLine(double k, double b, boolean vertical, boolean horizontal) {
		this.k = k;
		this.b = b;
		this.vertical = vertical;
		this.horizontal = horizontal;
	}

	public LinearLine(Point_ a, Point_ b) {
		if (Math.abs(a.y-b.y) < 0.0000001) {
			horizontal = true;
		}else if (Math.abs(a.x-b.x) < 0.0000001) {
			vertical = true;
		} else {
			this.k = (a.y - b.y) / (a.x - b.x);
			this.b = a.y - this.k * a.x;
		}
	}
	public double calY(double X){
		return this.k*X+b;
	}
	public double calX(double Y){
		return (Y-this.b)/this.k;
	}
	public Point_ projectOnLine(double x0, double y0, int minX, int minY, int maxX, int maxY){
		if(this.vertical){
			return new Point_(this.b, y0);
		}
		if(this.horizontal){
			return new Point_(x0, this.b);
		}
		Point_ p = Math.abs(this.k) < 1?new Point_(x0, this.calY(x0)): new Point_(this.calX(y0),y0);
		if(UniversalTool.inBound(p, minX, minY, maxX, maxY)){
			return p;
		}else{
			double imgTan = (maxY-minY)/(maxX-minX);
			Point_ intersect1, intersect2;
			if(Math.abs(this.k) >= imgTan){
				intersect1 = new Point_(this.calX(minY),minY);
				intersect2 = new Point_(this.calX(maxY),maxY);
			}else{
				intersect1 = new Point_(minX, calY(minX));
				intersect2 = new Point_(maxX, calY(maxX));
			}
			return (UniversalTool.distance(p,intersect1) < UniversalTool.distance(p,intersect2))?intersect1:intersect2;
		}

		
	}
	public Point_[] getPerpendicularLineEndPoints(double x0, double y0, double halfLong) {
		if(halfLong < 0.5) {
			System.out.println("HL "+halfLong);
			halfLong = 5;
		}
		Point_[] ends = new Point_[2];
		if(this.horizontal){
			ends[0] = new Point_(x0, y0+halfLong);
			ends[1] = new Point_(x0, y0-halfLong);
			return ends;
		}
		if(this.vertical){
			ends[0] = new Point_(x0-halfLong, y0);
			ends[1] = new Point_(x0+halfLong, y0);
			return ends;
		}
		LinearLine pll = this.getPependicularLinearLineAt(x0, y0);
		double offsetX = Math.sqrt((halfLong*halfLong)/(1+pll.k*pll.k));
		double x1 = x0+offsetX;
		double y1 = pll.calY(x1);
		double x2 = x0-offsetX;
		double y2 = pll.calY(x2);
		ends[0] = new Point_(x1, y1);
		ends[1] = new Point_(x2, y2);
		return ends;
	}

	public LinearLine getPependicularLinearLineAt(double x0, double y0) {
		// if this line is vertical
		if (this.vertical) {
			//return a horizontal line
			return new LinearLine(0, y0, false, true);
		}
		//if this line is horizontal
		if (this.horizontal) {
			//return a vertical line
			return new LinearLine(0, x0, true, false);
		}
		double k_ = -1 / this.k;
		double b_ = y0 - k_ * x0;
		return new LinearLine(k_, b_);
	}

	public String toString() {
		String s = "LinearLine: ";
		s += " k=" + this.k;
		s += " b=" + this.b;
		s += " V=" + this.vertical;
		s += " H=" + this.horizontal;
		return s;
	}
}
