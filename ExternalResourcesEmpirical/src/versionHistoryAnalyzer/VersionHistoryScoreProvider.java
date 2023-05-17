package versionHistoryAnalyzer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import utility.FileMapLoader;
import utility.GoldsetLoader;


public class VersionHistoryScoreProvider {

	HashMap<Integer, Date> brDateMap;
	HashMap<Date, ArrayList<Integer>> dateBFCommitMap;
	HashMap<Integer, String> fileKeyMap;
	String repoName;
	int bugID;
	//AmaLgam amal;
	HashMap<Integer, ArrayList<Integer>> goldmap;
	public VersionHistoryScoreProvider(int bugID, String repoName) {
		this.repoName = repoName;
		this.bugID = bugID;
		this.brDateMap = new BugReportDateLoader(repoName).loadBugReportDate();
		this.dateBFCommitMap = new BugFixCommitDateLoader(repoName)
				.loadDateCommitMap();
		this.fileKeyMap = FileMapLoader.loadKeyMap(repoName);
		//this.goldmap=new HashMap<>();
		this.goldmap=GoldsetLoader.getGoldsetWithFileIDs(repoName);
//		this.goldmap=GoldsetLoader.goldsetLoader(repoName, bugID);
	}

	/*public VersionHistoryScoreProvider(int bugID, String repoName, AmaLgam amal) {
		this.repoName = repoName;
		this.bugID = bugID;
		//this.amal = amal;
		this.brDateMap = new BugReportDateLoader(repoName).loadBugReportDate();
		//this.dateBFCommitMap = amal.dateBFCommitMap;
		//this.fileKeyMap = amal.fileKeyMap;
		this.dateBFCommitMap = new BugFixCommitDateLoader(repoName)
				.loadDateCommitMap();
		this.fileKeyMap = FileMapLoader.loadKeyMap(repoName);
	}*/

	protected HashMap<Integer, Integer> collectCandidateFixedBugTime() {
		HashMap<Integer, Integer> candidateBugTimeMap = new HashMap<>();
		
		//.out.println(this.bugID+":"+this.brDateMap);
		//System.out.println(this.bugID+"\n");
		//System.out.println(this.dateBFCommitMap);
		if (this.brDateMap.containsKey(bugID)) {
			Date reportDate = this.brDateMap.get(bugID);
			for (Date cdate : this.dateBFCommitMap.keySet()) {
				long diff = reportDate.getTime() - cdate.getTime();
				if (diff > 0) {
					long dayDiff = TimeUnit.DAYS.convert(diff,
							TimeUnit.MILLISECONDS);
					if (dayDiff > 0 && dayDiff <= 15) {
						ArrayList<Integer> bugIDs = dateBFCommitMap.get(cdate);
						for (int cbugID : bugIDs) {
							candidateBugTimeMap.put(cbugID, (int) dayDiff);
						}
					}
				}
			}
		}
		return candidateBugTimeMap;
	}

	protected HashSet<Integer> getGoldsetFileIDs(int cbugID) {
		ArrayList<String> files = GoldsetLoader.goldsetLoader(repoName, cbugID);
		HashSet<Integer> temp = new HashSet<>();
		for (int key : this.fileKeyMap.keySet()) {
			String canonical = this.fileKeyMap.get(key);
			for (String fileURL : files) {
				String nfileURL = fileURL.replaceAll("/", ".");
				if (canonical.endsWith(nfileURL)) {
					temp.add(key);
					break;
				}
			}
			if (files.size() == temp.size())
				break;
		}
		return temp;
	}

	protected HashSet<Integer> getGoldsetFileIDsOpt(int cbugID) {
		HashSet<Integer> golds = new HashSet<>();
		
		if (this.goldmap.containsKey(cbugID)) {
			golds.addAll(this.goldmap.get(cbugID));
		}
		return golds;
	}

	protected double calculateScore(int tc) {
		double k = 15;
		double expression = 12 * (1 - ((k - tc) / k));
		double totalExp = 1 + Math.exp(expression);
		if (totalExp != 0) {
			return 1.0 / totalExp;
		}
		return 0;
	}

	protected HashMap<Integer, Double> getSuspicionScores() {
		HashMap<Integer, Integer> candidateBugTimeMap = collectCandidateFixedBugTime();
		System.out.println(candidateBugTimeMap);
		HashMap<Integer, Double> scoreMap = new HashMap<>();
		for (int cbugID : candidateBugTimeMap.keySet()) {
			int dayDiff = candidateBugTimeMap.get(cbugID);
			HashSet<Integer> changedFiles = getGoldsetFileIDsOpt(cbugID);
			for (int fileID : changedFiles) {
				double suspicion = calculateScore(dayDiff);
				if (scoreMap.containsKey(fileID)) {
					double updated = scoreMap.get(fileID) + suspicion;
					scoreMap.put(fileID, updated);
				} else {
					scoreMap.put(fileID, suspicion);
				}
			}
		}
		return scoreMap;
	}

	public static void main(String[] args) {
		String repoName = "ecf";
		int bugID = 211585;
		System.out.println(new VersionHistoryScoreProvider(bugID, repoName)
				.getSuspicionScores());
	 }
}
