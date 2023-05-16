package main;

import java.io.File;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("I am here");
		File directory = new File("./");
		   System.out.println(directory.getAbsolutePath());
	}

}
