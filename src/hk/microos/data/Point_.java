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
}
