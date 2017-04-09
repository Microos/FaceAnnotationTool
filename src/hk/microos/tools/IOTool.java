package hk.microos.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hk.microos.data.Ellipse;
import hk.microos.data.Flags;
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

	public static HashMap<String, MyImage> filterImageList(ArrayList<String> list, JFrame dialogFatherFrame) {
		HashMap<String, MyImage> map = new HashMap<>();
		ArrayList<String> failed = new ArrayList<>();
		int failedNum = 0;
		for (String l : list) {
			if (l.trim().equals("")) {
				continue;
			}
			if (!new File(l).exists()) {
				failedNum++;
				if (failed.size() < 10) {
					failed.add(l);
				}

				continue;
			}
			map.put(l, null);
		}
		if (map.size() == 0) {
			JOptionPane.showMessageDialog(dialogFatherFrame,
					"None of the images found from paths listed in the seletced file.\nPlease check your file and try again.",
					"All failed", JOptionPane.WARNING_MESSAGE);
			return map;
		}
		if (failed.size() != 0) {
			String s = "";
			if (failed.size() != failedNum) {
				s = String.format("Existence checking failed paths(%d listed, %d in total):\n", failed.size(),
						failedNum);
			} else {
				s = String.format("Existence checking failed paths(total: %d):\n", failed.size());
			}
			int i = 1;
			for (String f : failed) {
				s += String.format("%2d [%s]\n", i, f);
				i++;
			}
			JOptionPane.showMessageDialog(dialogFatherFrame, s, "Existence checking failed paths",
					JOptionPane.WARNING_MESSAGE);
		}
		return map;
	}

	private static void writeFile(File f, String content) {
		FileWriter fw;
		try {
			if (!f.exists())
				f.createNewFile();

			fw = new FileWriter(f);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, ArrayList<Ellipse>> readAnnotationFile(File f, String prefix, String suffix)
			throws Exception {
		HashMap<String, ArrayList<Ellipse>> map = new HashMap<>();
		ArrayList<String> lines = readText(f);

		if (Flags.GLOABAL_DEBUG) {
			// prefix = "/Users/microos/Downloads/originalPics/";
			// prefix = "/home/rick/Space/work/FDDB/data/images/";
			prefix = "";

			suffix = ".jpg";
			
		}
		System.out.println("prefix: "+prefix);
		System.out.println("suffix: "+suffix);
		// parse annotation files
		int at = 0;
		while (at < lines.size()) {
			String path = prefix + lines.get(at) + suffix;
			at++;
			int detNum = 0;
			try {
				detNum = Integer.parseInt(lines.get(at));

			} catch (Exception e) {
				throw new Exception(String.format("At line %d, failed to parse \"%s\" as number of annotations.\n", at,
						lines.get(at)));
				
			}
			at++;
			
			
			ArrayList<Ellipse> elpses = null;
			try {
				elpses = readEllipse(lines, at, detNum);

			} catch (Exception e) {
				throw new Exception(e.getMessage());
				
			}
			
			at += detNum;

			map.put(path, elpses);

		}

		return map;
	}

	private static ArrayList<Ellipse> readEllipse(ArrayList<String> lines, int start, int num) throws Exception {
		ArrayList<Ellipse> elps = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			int at = start + i;
			String line = lines.get(at);
			String[] splitStr = line.split(" +");
			if (splitStr.length < 5) {
				throw new Exception(String.format("At line %d: \"%s\"\ncontains less than 5 float values.\nExpected annotation format: major,minor,angle,x,y", at,line));
			}
			ArrayList<Double> splitFlt = new ArrayList<>();
			for (String s : splitStr) {
				splitFlt.add(Double.parseDouble(s));
			}
			elps.add(new Ellipse(splitFlt));
		}
		return elps;
	}

	public static void outputEllipse(HashMap<String, MyImage> pathImgPair, String outPath, boolean hasAnnotationLoaded,
			JFrame dialogFatherFrame) {
		outPath = !outPath.endsWith(".txt") ? outPath += ".txt" : outPath;
		System.out.println("OP: " + outPath);
		File f = new File(outPath);
		if (f.exists()) {
			int res = JOptionPane.showConfirmDialog(dialogFatherFrame,
					String.format("File %s exists, do you want to overwrite it?", f.getAbsolutePath()), "File exists",
					JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.NO_OPTION || res == -1)
				return;
		}
		boolean withBoth = false;
		if (hasAnnotationLoaded) {
			int res = JOptionPane.showOptionDialog(dialogFatherFrame,
					"Do you want to concatenate loaded annotations and annotations marked by you into the output?\n"
							+ "(Noted that a image without any annotation will not be included in the output file)",
					"", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
					new String[] { "both loaded and marked annotations", "only annotations marked by me" }, 0);
			if(res == -1)
				return;
			if (res == 0)
				withBoth = true;
			if (res == 1)
				withBoth = false;
		}
		int numImage = 0;
		int numAnnot = 0;
		StringBuffer sb = new StringBuffer();
		
		for (String p : pathImgPair.keySet()) {
			MyImage mim = UniversalTool.getMyImageFromPathImgPair(p, pathImgPair);
			String s = mim.getOutputString(withBoth);
			sb.append(s);
			if (!s.equals("")) {
				numImage++;
				numAnnot += s.split("\n").length - 2;
			}
		}
		writeFile(f, sb.toString());
		JOptionPane.showMessageDialog(dialogFatherFrame, String.format(
				"%d annotations from %d images \nhave been written to %s", numAnnot, numImage, f.getAbsolutePath()));
	}
}
