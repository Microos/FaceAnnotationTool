package hk.microos.test;


import java.util.ArrayList;

import hk.microos.data.*;
import hk.microos.tools.UniverseTool;

public class test {

	public static void main(String[] args) {
		ArrayList<Double> x1x2 = UniverseTool.rootQudratic(5,9,1);
		System.out.println(x1x2.get(0));
		System.out.println(x1x2.get(1));
		
	}

}
