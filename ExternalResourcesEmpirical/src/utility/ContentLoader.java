package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ContentLoader {
	public static ArrayList<Integer> getAllLinesInt(String fileName) {
		ArrayList<String> lines = getAllLinesOptList(fileName);
		ArrayList<Integer> temp = new ArrayList<>();
		for (String line : lines) {
			if(line.trim().isEmpty())continue;
			temp.add(Integer.parseInt(line.trim()));
		}
		return temp;
	}
	public static String loadFileContent(String fileName) {
		// code for loading the file name
		String fileContent = new String();
		try {
			File f = new File(fileName);
			BufferedReader bufferedReader = new BufferedReader(
					new FileReader(f));
			while (bufferedReader.ready()) {
				String line = bufferedReader.readLine().trim();
				if (line.trim().isEmpty())
					continue;
				fileContent += line + "\n";
			}
			bufferedReader.close();
		} catch (Exception ex) {
			// handle the exception
		}
		return fileContent;
	}

	public static ArrayList<String> readContent(String inFile) {
		// save the content
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader breader = new BufferedReader(new FileReader(inFile));
			while (breader.ready()) {
				String line = breader.readLine().trim();
				if (!line.isEmpty()) {
					lines.add(line);
				}
			}
			breader.close();

		} catch (Exception exc) {
			exc.printStackTrace();

		}
		return lines;
	}

	public static ArrayList<String> getAllLinesOptList(String fileName) {
		ArrayList<String> temp = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File(fileName));
			while (scanner.hasNextLine()) {
				temp.add(scanner.nextLine());
			}
			scanner.close();

		} catch (Exception exc) {
			// handle the exception
		}
		return temp;
	}

	
	
	public static String readContentSimple(String inFile) {
		// save the content
		String content = new String();
		try {
			BufferedReader breader = new BufferedReader(new FileReader(inFile));
			while (breader.ready()) {
				String line = breader.readLine().trim();
				if (!line.isEmpty()) {
					content += line + "\n";
				}
			}
			breader.close();

		} catch (Exception exc) {
			exc.printStackTrace();

		}
		return content;

	}

	// loading all lines from a file using arraylist
	public static ArrayList<String> getAllLinesList(String fileName) {
		ArrayList<String> lines = new ArrayList<>();
		try {
			File f = new File(fileName);
			BufferedReader bufferedReader = new BufferedReader(
					new FileReader(f));
			while (bufferedReader.ready()) {
				String line = bufferedReader.readLine();
				lines.add(line.trim());
			}
			bufferedReader.close();
		} catch (Exception ex) {
			// handle the exception
			ex.printStackTrace();
		}
		return lines;
	}

	
		public static HashMap<String, ArrayList<String>> returnHashMapFromFileContentForQuery(
			String inFile) {
		HashMap<String, ArrayList<String>> hm = new HashMap<String, ArrayList<String>>();
		ArrayList<String> fileContent = ContentLoader.readContent(inFile);
		for (String line : fileContent) {
			String[] spilter = line.split(" ");
			String idPart = spilter[0];
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 1; i < spilter.length; i++) {
				list.add(spilter[i]);
			}
			hm.put(idPart, list);
		}
		return hm;
	}

	
}
