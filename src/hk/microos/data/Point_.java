package hk.microos.data;

public class Point_ {
	public double x,y;
	public Point_(double x,double y){
		this.x = x;
		this.y = y;
	}
	public Point_(int x,int y){
		this.x = x;
		this.y = y;
	}
	public Point_(float x,float y){
		this.x = x;
		this.y = y;
	}
	public String toString(){
		return "("+this.x+", "+this.y+")";
	}
//	public Point_ add(double x, double y){
//		return new Point_(this.x + x, this.y + y);
//	}
//	public Point_ add(Point_ p){
//		return new Point_(this.x + p.x, this.y + p.y);
//	}
//	public Point_ minus(double x, double y){
//		return new Point_(this.x - x, this.y - y);
//	}
//	public Point_ minu(Point_ p){
//		return new Point_(this.x - p.x, this.y - p.y);
//	}
}
