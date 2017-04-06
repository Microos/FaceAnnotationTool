package hk.microos.tools;

import java.awt.BasicStroke;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NoInitialContextException;

import hk.microos.data.Ellipse;
import hk.microos.data.Flags;
import hk.microos.data.Point_;

public class UniversalTool {
	private UniversalTool(){}
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
	public static boolean inBound(Point_ p, int minX, int minY, int maxX, int maxY){
		double x = p.x;
		double y = p.y;
		if( (x>=minX && x<=maxX) && (y>=minY && y<maxY)){
			return true;
		}else{
			return false;
		}
		
	}
	public static Point_ getInboundPoint(Point_ p, int minX, int minY, int maxX, int maxY){
		double px = p.x;
		double py = p.y;
		px = px>maxX? maxX:px;
		px = px<minX? minX:px;
		py = py>maxY? maxY:py;
		py = py<minY? minY:py;
		return new Point_(px,py);
	}
	public static BasicStroke getPreferableStroke(double distance){
		
		double factor = 0.03;
		double s = distance * factor;
		s = Math.min(Flags.maxStroke, s);
		s = Math.max(Flags.minStroke, s);
//		System.out.format("Distance:%.4f; S_:%s S:%s\n",distance, distance * factor, s);
		return new BasicStroke((float)s);
	}
	public static Point_ getSymmetricPoint(Point_ center, Point_ A){
		double cx = center.x;
		double cy = center.y;
		double ax = A.x;
		double ay = A.y;
		return new Point_(2*cx-ax,2*cy-ay);
	}
	public static double Pythagorean(double A, double B){
		return Math.sqrt(Math.pow(A, 2)+Math.pow(B, 2));
	}
	public static void printArray(Iterable<String> lst){
		for(String s: lst){
			System.out.println(s);
		}
	}
	public static String[] getPrefixAndName(String s){
		String[] sa = s.split("/");
//		System.out.println("input string "+s);
//		for(String t: sa){
//			System.out.println("\t"+t);
//		}
		String[] prefix = Arrays.copyOfRange(sa, 0, sa.length-1);
		String name = sa[sa.length-1];
		String pref = String.join("/",prefix);
		String[] ret = new String[2];
		ret[0] = pref;
		ret[1] = name;
		return ret;
		
		
	}
	
}
