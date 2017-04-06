package hk.microos.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hk.microos.data.MyImage;

public class IOTool {
	private IOTool(){}
	public static boolean isTextFile(File f){
        String type = null;
		try {
			type = Files.probeContentType(f.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        if(type == null) return false;
        if(type.startsWith("text")) return true;
        return false;
    }
	public static ArrayList<String> readText(File f){
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
	public static HashMap filterImageList(ArrayList<String> list, JFrame dialogFatherFrame){
		HashMap<String, MyImage> map = new HashMap<>();
		ArrayList<String> failed = new ArrayList<>();
		for(String l: list){
			if (!new File(l).exists()){
				failed.add(l);
				continue;
			}
			map.put(l, null);
		}
		
		if(failed.size() != 0){
			String s = String.join("\n", failed);
			JOptionPane.showMessageDialog(dialogFatherFrame, String.format("Existing check failed files:\n%s", s),"Existing check failed files",JOptionPane.WARNING_MESSAGE);
		}
		return map;
	}
}
