package hk.microos.tools;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import hk.microos.data.Ellipse;
import hk.microos.data.Point_;

public class UniverseTool {
	public static boolean inBound(int minX, int minY, int maxX, int maxY, int x, int y) {
		// System.out.format("inBound: %d %d %d %d\tXY: %d %d\n",
		// minX,minY,maxX,maxY,x,y);
		return (minX <= x && x <= maxX) && (minY <= y && y <= maxY);
	}
	public static Ellipse getTestEllipse(int f){
		double x = 100;
		double y = 100;
		Point_ mjA = new Point_(0*f+x,0*f+y);
		Point_ mjB = new Point_(8.66025403784*f+x,5*f+y);
		Point_ miC = new Point_(2.88675134595*f+x,5*f+y);
		Ellipse e = new Ellipse(mjA, mjB, miC);
		return e;
	}
	public static Ellipse2D.Double getPointOval(Point_ p, double pointWidth){
		double x = p.x; double y = p.y;
		return new Ellipse2D.Double(x-pointWidth/2, y-pointWidth/2, pointWidth, pointWidth);
	}
	public static Ellipse2D.Double getPointOval(double x, double y, double pointWidth){
		return new Ellipse2D.Double(x-pointWidth/2, y-pointWidth/2, pointWidth, pointWidth);
	}
	public static ArrayList<Double> rootQudratic(double a, double b, double c) {
		double m = b*b - 4*a*c;
		if(m>=0){
			ArrayList<Double> ret = new ArrayList<Double>();
			double r1 = (-1*b-Math.sqrt(m))/(2*a);
			double r2 = (-1*b+Math.sqrt(m))/(2*a);
			ret.add(r1);
			ret.add(r2);
			return ret;
		}else{
			System.out.println("no root!");
			return null;
		}
	}
	public static Point_ midPoint(Point_ a, Point_ b){
		return new Point_((a.x+b.x)/2, (a.y+b.y)/2);
	}
	public static double distance(Point_ a, Point_ b){
		double A2 = (a.x - b.x)*(a.x - b.x);
		double B2 = (a.y - b.y)*(a.y - b.y);
		return Math.sqrt(A2+B2);
	}
}
