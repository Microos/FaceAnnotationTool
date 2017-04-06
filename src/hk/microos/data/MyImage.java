package hk.microos.data;

import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import hk.microos.tools.ImageTool;

public class MyImage {
	private int id;
	private String path;
	private BufferedImage bi;
	public int w, h;
	private ArrayList<Ellipse> elpses = new ArrayList<>();
	private ArrayList<Ellipse> elpsesStatic = new ArrayList<>();
	public MyImage(File f, int id) {
		this.id = id;
		this.path = f.getAbsolutePath();
		this.bi = ImageTool.openImage(f);
		this.w = bi.getWidth();
		this.h = bi.getHeight();
	}

	public void setElpsFromString(String s) {
		// load annotation from string with a specific format
	}

	public BufferedImage getImage() {
		return bi;
	}

	public void addElps(Ellipse e) {
		elpses.add(e);
	}

	public ArrayList<Ellipse> getElpses() {
		return elpses;
	}
	public ArrayList<Ellipse> getEllipseStatic(){
		return elpsesStatic;
	}
	
	@Override
	public boolean equals(Object obj) {
		MyImage img = (MyImage)obj;
		return img.path.equals(this.path) && img.id == this.id;
	}
	public String getMarkNumString(){
		return String.format("%d+%d", elpsesStatic.size(), elpses.size());
	}
	
}
