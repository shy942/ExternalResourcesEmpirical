package reporterInformationAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import utility.FileMapLoader;
import utility.GoldsetLoader;


public class AuthorScoreProvider {

	String repoName;
	int bugID;
	HashMap<Integer, String> bugAuthorMap;
	HashMap<String, ArrayList<Integer>> authorMap;
	HashMap<Integer, String> fileKeyMap;

	public AuthorScoreProvider(String repoName, int bugID) {
		this.repoName = repoName;
		this.bugID = bugID;
		BugReporterLoader brloader = new BugReporterLoader(repoName);
		this.bugAuthorMap = brloader.loadBugAuthors();
		this.authorMap = brloader.loadAuthorMap();
		this.fileKeyMap = FileMapLoader.loadKeyMap(repoName);
	}



	protected ArrayList<Integer> collectPastBugs() {
		ArrayList<Integer> temp = new ArrayList<>();
		if (this.bugAuthorMap.containsKey(bugID)) {
			String author = this.bugAuthorMap.get(bugID);
			if (authorMap.containsKey(author)) {
				ArrayList<Integer> allbugs = authorMap.get(author);
				for (int cbugID : allbugs) {
					if (cbugID < bugID) {
						temp.add(cbugID);
					}
				}
			}
		}
		return temp;
	}

	protected HashSet<String> extractPackageNames(ArrayList<String> fileURLs) {
		HashSet<String> temp = new HashSet<>();
		for (String fileURL : fileURLs) {
			File f = new File(fileURL);
			String dir = f.getParent();
			try {
				String packageName = dir.replace('\\', '.');
				packageName = packageName.replace('/', '.');
				temp.add(packageName);
			} catch (Exception exc) {
				// handle the exception
			}
		}
		return temp;
	}

	protected HashSet<String> collectProblematicPackages(
			ArrayList<Integer> pastBugs) {
		HashSet<String> packages = new HashSet<>();
		for (int bugID : pastBugs) {
			ArrayList<String> goldset = GoldsetLoader.goldsetLoader(repoName,
					bugID);
			packages.addAll(extractPackageNames(goldset));
		}
		return packages;
	}

	protected HashMap<Integer, Double> collectAuthorScores() {
		HashMap<Integer, Double> scoreMap = new HashMap<>();
		ArrayList<Integer> pastBugs = collectPastBugs();
		HashSet<String> pastpackages = collectProblematicPackages(pastBugs);
		for (String packageName : pastpackages) {
			for (int fileKey : this.fileKeyMap.keySet()) {
				String fullFilePath = this.fileKeyMap.get(fileKey);
				if (fullFilePath.contains(packageName)) {
					scoreMap.put(fileKey, 1.0);
				}
			}
		}
		return scoreMap;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String repoName = "ecf";
		int bugID = 337643;
		System.out.println(new AuthorScoreProvider(repoName, bugID)
				.collectAuthorScores());
	}
}
