package utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;




public class GoldsetLoader {
	public static ArrayList<String> goldsetLoader(String repoName, int bugID) {
		// loading the gold set for any repo and bug ID
		ArrayList<String> goldset = new ArrayList<>();
		//String goldFile = StaticData.BRICK_EXP + "/goldset/" + repoName
			//	+ "/gold/" + bugID + ".txt";
		String goldFile="DataSets/Goldset/"+repoName+"/"
				+bugID+".txt";
		File f = new File(goldFile);
		if (f.exists()) { // if the solution file exists
			String content = ContentLoader.loadFileContent(goldFile);
			String[] items = content.split("\n");
			for (String item : items) {
				if (!item.trim().isEmpty()) {
					goldset.add(item);
				}
			}
		}
		return goldset;
	}

	public static HashMap<Integer, ArrayList<Integer>> getGoldsetWithFileIDs(
			String repoName) {
		HashMap<Integer, String> fileKeyMap = FileMapLoader
				.loadKeyMap(repoName);
		ArrayList<Integer> selectedBugs = SelectedBugs
				.getSelectedBugs(repoName);
		HashMap<Integer, ArrayList<Integer>> goldmap = new HashMap<>();
		for (int bugID : selectedBugs) {
			ArrayList<String> goldfiles = goldsetLoader(repoName, bugID);
			ArrayList<String> ngoldfiles = new ArrayList<>();
			for (String fileURL : goldfiles) {
				String nFileURL = fileURL.replace('/', '.');
				nFileURL = nFileURL.replace('\\', '.');
				ngoldfiles.add(nFileURL);
			}
			ArrayList<Integer> temp = new ArrayList<>();
			for (int fileID : fileKeyMap.keySet()) {
				String canonical = fileKeyMap.get(fileID);
				for (String goldfile : ngoldfiles) {
					if (canonical.endsWith(goldfile)) {
						temp.add(fileID);
						break;
					}
				}
			}
			goldmap.put(bugID, temp);
		}
		return goldmap;
	}
}
