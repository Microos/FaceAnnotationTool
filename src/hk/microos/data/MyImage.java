package hk.microos.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import hk.microos.tools.ImageTool;

public class MyImage {
	private BufferedImage bi;
	public int w, h;
	private ArrayList<Ellipse> elpses = new ArrayList<>();

	public MyImage(File f) {
		bi = ImageTool.openImage(f);
		w = bi.getWidth();
		h = bi.getHeight();
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
}
