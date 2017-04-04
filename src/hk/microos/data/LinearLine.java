package hk.microos.data;

import java.util.ArrayList;

import hk.microos.tools.UniverseTool;

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
		}
		if (Math.abs(a.x-b.x) < 0.0000001) {
			vertical = true;
		} else {
			this.k = (a.y - b.y) / (a.x - b.x);
			this.b = a.y - this.k * a.x;
		}
	}
	public double calY(double X){
		
		return this.k*X+b;
	}
	public Point_ projectOnLine(double x0, double y0){
		return new Point_(x0, this.calY(x0));
	}
	public Point_[] getLineEndPoints(double x0, double y0, double halfLong) {
		
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
		double x1 = x0+halfLong;
		double y1 = pll.calY(x1);
		double x2 = x0-halfLong;
		double y2 = pll.calY(x2);
		
		ends[0] = new Point_(x1, y1);
		ends[1] = new Point_(x2, y2);
		return ends;
	}

	public LinearLine getPependicularLinearLineAt(double x0, double y0) {
		// if vertical
		if (this.vertical) {
			return new LinearLine(0, y0, false, true);
		}
		if (this.horizontal) {
			return new LinearLine(0, 0, true, false);
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
