package hk.microos.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import hk.microos.tools.ImageTool;

public class MyImage {
	private String path;
	private BufferedImage bi;
	private int w = -1, h = -1;
	private ArrayList<Ellipse> elpses = new ArrayList<>();
	private ArrayList<Ellipse> elpsesStatic = null;

	public MyImage(File f) {
		this.path = f.getAbsolutePath();
		// this.bi = ImageTool.openImage(f);
		// this.w = bi.getWidth();
		// this.h = bi.getHeight();
	}

	public int w() {
		if (w == -1)
			w = getImage().getWidth();
		return w;
	}

	public int h() {
		if (h == -1)
			h = getImage().getHeight();

		return h;
	}

	public void setElpsFromString(String s) {
		// load annotation from string with a specific format
	}

	public BufferedImage getImage() {
		bi =  bi == null ? ImageTool.openImage(new File(path)) : bi;
		return bi;
	}

	public void addElps(Ellipse e) {
		elpses.add(e);
	}

	public ArrayList<Ellipse> getElpses() {
		return elpses;
	}

	public ArrayList<Ellipse> getEllipseStatic() {
		return elpsesStatic;
	}

	public ArrayList<String> getElpsesStrings() {
		ArrayList<String> strs = new ArrayList<>();
		for (Ellipse e : elpses) {
			strs.add(e.toRowFormatString());
		}
		return strs;
	}

	public ArrayList<String> getStaticElpsesStrings() {
		if (elpsesStatic == null) {
			return null;
		}
		ArrayList<String> strs = new ArrayList<>();
		for (Ellipse e : elpsesStatic) {
			strs.add(e.toRowFormatString());
		}
		return strs;
	}

	public ArrayList<String> getAllElpsesStrings() {
		ArrayList<String> strs = new ArrayList<>();
		for (Ellipse e : elpses) {
			strs.add(e.toRowFormatString());
		}
		if (elpsesStatic != null) {
			for (Ellipse e : elpsesStatic) {
				strs.add(e.toRowFormatString());
			}
		}

		return strs;
	}
	public String getOutputString(boolean withBoth){
		//imgPath
		//detNum
		//coord1 1
		//coord2 1
		//...
		
		ArrayList<Ellipse> elpse4Output = new ArrayList<>();
		if(withBoth && elpsesStatic!=null){
			elpse4Output.addAll(elpsesStatic);
		}
		elpse4Output.addAll(elpses);
		if(elpse4Output.size() == 0) return "";
		StringBuilder sb = new StringBuilder();
		sb.append(path+"\n");
		sb.append(elpse4Output.size()+"\n");
		for(Ellipse e : elpse4Output){
			sb.append(e.toOutputFormatString()+"\n");
		}
		return sb.toString();
		
		
		
		
	}
	public String getPath() {
		return this.path;
	}

	@Override
	public boolean equals(Object obj) {
		MyImage img = (MyImage) obj;
		return img.path.equals(this.path);
	}

	public String getMarkNumString() {
		int elpsesStaticSize = (elpsesStatic == null) ? 0 : elpsesStatic.size();
		return String.format("%d+%d", elpsesStaticSize, elpses.size());
	}

	public void setElpsesStatic(ArrayList<Ellipse> elps) {
		this.elpsesStatic = elps;
	}

}
