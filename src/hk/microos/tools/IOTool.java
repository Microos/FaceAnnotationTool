package hk.microos.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hk.microos.data.Ellipse;
import hk.microos.data.MyImage;

public class IOTool {
	private IOTool() {
	}

	public static boolean isTextFile(File f) {
		// String type = null;
		// try {
		// type = Files.probeContentType(f.toPath());
		// } catch (IOException e) {
		// e.printStackTrace();
		// return false;
		// }
		// if(type == null) return false;
		// if(type.startsWith("text")) return true;
		// return false;
		return f.getAbsolutePath().endsWith(".txt");
	}

	public static ArrayList<String> readText(File f) {
		Path filePath = f.toPath();
		Charset charset = Charset.defaultCharset();
		ArrayList<String> stringList;
		try {
			stringList = (ArrayList<String>) Files.readAllLines(filePath, charset);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return stringList;
	}

	public static HashMap filterImageList(ArrayList<String> list, JFrame dialogFatherFrame) {
		HashMap<String, MyImage> map = new HashMap<>();
		ArrayList<String> failed = new ArrayList<>();
		for (String l : list) {
			if (!new File(l).exists()) {
				failed.add(l);
				continue;
			}
			map.put(l, null);
		}

		if (failed.size() != 0) {
			String s = String.join("\n", failed);
			JOptionPane.showMessageDialog(dialogFatherFrame, String.format("Existing check failed files:\n%s", s),
					"Existing check failed files", JOptionPane.WARNING_MESSAGE);
		}
		return map;
	}

	public static HashMap<String, ArrayList<Ellipse>> readAnnotationFile(File f) throws Exception {
		HashMap<String, ArrayList<Ellipse>> map = new HashMap<>();
		ArrayList<String> lines = readText(f);
		String prefix = "/Users/microos/Downloads/originalPics/";
		prefix = "/home/rick/Space/work/FDDB/data/images/";
		prefix = "";
		String suffix = ".jpg";
		
		// parse annotation files
		int at = 0;
		while (at < lines.size()) {
			String path = prefix+lines.get(at)+suffix;
			at++;
			
			int detNum = Integer.parseInt(lines.get(at));
			at++;
			
			ArrayList<Ellipse> elpses = readNEllipse(lines, at, detNum);
			at += detNum;
			
			if(new File(path).exists()){
				map.put(path, elpses);
			}else{
				System.err.println(path+" listed in annotation files not found!");
			}
			

		}

		return map;
	}

	private static ArrayList<Ellipse> readNEllipse(ArrayList<String> lines, int start, int num) throws Exception{
		ArrayList<Ellipse> elps = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			int at = start + i;
			String line = lines.get(at);
			String[] splitStr = line.split(" +");
			if(splitStr.length < 5){
				throw new Exception(String.format("Line %d: contains less than 5 float values.", at));
			}
			ArrayList<Double> splitFlt = new ArrayList<>();
			for(String s:splitStr){
				splitFlt.add(Double.parseDouble(s));
			}
			elps.add(new Ellipse(splitFlt));
		}
		return elps;
	}
}
