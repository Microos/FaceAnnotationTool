package hk.microos.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import hk.microos.data.MyImage;

public class ImageTool {
	static int  getTestImageRound =0;
	public static MyImage getTestImage(){
		getTestImageRound++;
//		File f = getTestImageRound%2==0?new File("/home/rick/Downloads/a.jpg"):new File("/home/rick/Downloads/b.jpg");
		File f = getTestImageRound%2==0?new File("/Users/microos/Downloads/timg-3.jpeg"):new File("/Users/microos/Downloads/timg.jpg");
		return new MyImage(f);
	}
	public static BufferedImage openImage(File f){
		
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bi;
	}

}
