package hk.microos.data;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Ellipse {
	private ArrayList<Point_> keyPts = new ArrayList<>(); // 3 key points + 1
															// center
	public double major, minor, angle, x, y; // half mj mi

	public Ellipse(ArrayList<Double> v) {
		this.major = v.get(0);
		this.minor = v.get(1);
		this.angle = v.get(2);
		this.x = v.get(3);
		this.y = v.get(4);
	}
	public Ellipse(double major, double minor, double angle, double x, double y) {
		this.major = major;
		this.minor = minor;
		this.angle = angle;
		this.x = x;
		this.y = y;
		// calculate 3 key points
	}

	public Ellipse(Point_ mjA, Point_ mjB, Point_ miC) {

		double mjA_x = mjA.x;
		double mjA_y = mjA.y;
		double mjB_x = mjB.x;
		double mjB_y = mjB.y;
		double miC_x = miC.x;
		double miC_y = miC.y;
		if (Math.abs(mjA_x - mjB_x) < 0.0000001) {
			this.angle = -90 * Math.PI / 180.0;
		} else {
			this.angle = (mjA_y - mjB_y) / (mjA_x - mjB_x);
			this.angle = Math.atan(this.angle);
		}
		this.x = (mjA_x + mjB_x) / 2.0;
		this.y = (mjA_y + mjB_y) / 2.0;
		this.major = 0.5 * Math.sqrt(
				Math.abs(mjA_x - mjB_x) * Math.abs(mjA_x - mjB_x) + Math.abs(mjA_y - mjB_y) * Math.abs(mjA_y - mjB_y));
		this.minor = Math.sqrt(Math.abs(miC_x - x) * Math.abs(miC_x - x) + Math.abs(miC_y - y) * Math.abs(miC_y - y));
		keyPts.add(mjA);
		keyPts.add(mjB);
		keyPts.add(miC);
		keyPts.add(new Point_(this.x, this.y));
	}

	public ArrayList<Point_> getKeyPoints() {
		return keyPts;
	}

	public Point_ getCenter() {
		return new Point_(this.x, this.y);
	}

	public Ellipse2D.Double getErectedEllipse2D() {
		return new Ellipse2D.Double(x - major, y - minor, major * 2, minor * 2);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("Ellipse At: (" + this.x + ", " + this.y + ")\n");
		sb.append(" Major,Minor: " + this.major + ", " + this.minor + "\n");
		sb.append(" Angle: " + this.angle + " (" + this.angle * 180 / Math.PI + "dgr)\n");
		return sb.toString();
	}
	public Ellipse offset(double x, double y){
		return new Ellipse(major, minor, angle, this.x+x, this.y+y);
	}
	public String toRowFormatString() {
		String fmt = "%.2f";
		String[] strs = new String[5];

		strs[0] = String.format(fmt, this.major);
		strs[1] = String.format(fmt, this.minor);
		strs[2] = String.format(fmt, this.angle);
		strs[3] = String.format(fmt, this.x);
		strs[4] = String.format(fmt, this.y);

		return String.join(",", strs);
	}

}
