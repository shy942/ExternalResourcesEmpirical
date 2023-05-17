package versionHistoryAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;

import utility.ContentLoader;
import utility.ContentWriter;
import utility.SortMapByValue;

public class Main_VHSprovider {

	public Main_VHSprovider() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String corpus="ecf";
		
		Main_VHSprovider obj=new Main_VHSprovider();
		obj.master(corpus);
	}
	
	public void master(String corpus)
	{
		String bugIDpath="DataSets//BugIDs//"+corpus+"//BugIDdateBased.txt";
		ArrayList<String> bugIDlist=ContentLoader.getAllLinesList(bugIDpath);
		
		String VHScoreOutputFile="DataSets//versionhistory//VersionHistoryScore//"+corpus+"//VHbasedResult.txt";
		
		
		//collect file-Id maps
		HashMap<Integer, String> hmKeys=getKeys(corpus);
		//System.out.println(hmKeys);
		
		HashMap<String, HashMap<String, Double>> hmVersionHistoryScore=new HashMap<>();
		
		for(String bugIDstr:bugIDlist)
		{
			int bugID=Integer.valueOf(bugIDstr);
			System.out.println(bugIDstr);
			
			VersionHistoryScoreProvider objVHSP=new VersionHistoryScoreProvider(bugID, corpus);
			HashMap<Integer, Double> scoreMap=objVHSP.getSuspicionScores();
			HashMap<String, Double> score2Store=new HashMap<>();
			System.out.println(scoreMap);
			for(int fileInt:scoreMap.keySet())
			{
				//System.out.println(hmKeys.get(fileInt)+" : "+scoreMap.get(fileInt));
				score2Store.put(hmKeys.get(fileInt), scoreMap.get(fileInt));
			}
			
			hmVersionHistoryScore.put(bugIDstr, score2Store);
		}
		HashMap <String, HashMap<String, Double>> sortedHmVHresult=SortedSTresult(hmVersionHistoryScore);
		//System.out.println(hmVersionHistoryScore);
		ContentWriter.writeContentForSTscore(VHScoreOutputFile, sortedHmVHresult);
		
	}
	public HashMap<String, HashMap<String, Double>> SortedSTresult(HashMap <String, HashMap<String, Double>> hmSTresult)
	{
		HashMap <String, HashMap<String, Double>> sortedHmSTresult=new HashMap<>();
		int total_bug_report=0;
		int VHS_bug_report=0;
		for(String bugID: hmSTresult.keySet())
		{ 
			//System.out.println(bugID);
			total_bug_report++;
			HashMap<String, Double> contentHM=new HashMap<>();
			contentHM=hmSTresult.get(bugID);
			if(contentHM.size()<=0)	
			{
				contentHM.put("0", 100.0);
				sortedHmSTresult.put(bugID, contentHM);
			}
			else
			{
			VHS_bug_report++;
			for(String file:contentHM.keySet())
			{
				
				boolean DESC = false;
				HashMap<String, Double> sortedContentHM=(HashMap<String, Double>) SortMapByValue.sortByComparator(contentHM, DESC);
				//if(sortedContentHM.size()==2)System.out.println("After Sorting: "+sortedContentHM);
				sortedHmSTresult.put(bugID, sortedContentHM);
			}}
		}
		System.out.println(total_bug_report+" "+VHS_bug_report);
		//this.total_Bug_Report=this.total_Bug_Report+total_bug_report;
		//this.toal_Bug_Report_StackTrace=this.toal_Bug_Report_StackTrace+ST_bug_report;
		return sortedHmSTresult;
	}

	
	public HashMap< Integer, String> getKeys(String corpus)
	{
		String keysPath="DataSets//Corpus//"+corpus+".ckeys";
		ArrayList<String> content=ContentLoader.getAllLinesList(keysPath);
		HashMap< Integer, String> hmKeys=new HashMap<>();
		//System.out.println(content);
		for(String line:content)
		{
			String[] spilter=line.split(":");
			System.out.println(spilter[0]);
			System.out.println(spilter[2]);
			String filePath=spilter[2];
			String[] fileSpilter=filePath.split("\\\\");
			String processedFilePath="";
			if(corpus=="ecf")for(int i=12;i<fileSpilter.length-1;i++)processedFilePath=processedFilePath+fileSpilter[i]+".";
			//else if(corpus=="eclipse.jdt.ui")for(int i=10;i<fileSpilter.length-1;i++)processedFilePath=processedFilePath+fileSpilter[i]+".";
			else if(corpus=="eclipse.jdt.debug"||corpus=="eclipse.jdt.ui"||corpus=="eclipse.jdt.core")for(int i=10;i<fileSpilter.length-1;i++)processedFilePath=processedFilePath+fileSpilter[i]+".";
			else if (corpus=="eclipse.pde.ui")for(int i=11;i<fileSpilter.length-1;i++)processedFilePath=processedFilePath+fileSpilter[i]+".";
			else if (corpus=="tomcat70")
			{
				 System.out.println(line);
				 int index=0;
				 if(line.contains("org"))
				 {
					 index=line.indexOf("org");
					 String processedFilePathWithBackslash=line.substring(index);
					 String[] spilterTomcat70=processedFilePathWithBackslash.split("\\\\");
					 for(int i=0;i<spilterTomcat70.length-1;i++)processedFilePath=processedFilePath+spilterTomcat70[i]+".";
				 }
				 System.out.println(processedFilePath);
				/* else 
				 {
					 for(int i=9;i<fileSpilter.length-1;i++)processedFilePath=processedFilePath+fileSpilter[i]+".";
				 }*/
			}
			
			//if(corpus!="tomcat70")
			processedFilePath=processedFilePath+fileSpilter[fileSpilter.length-1];
			//System.out.println(processedFilePath);
			hmKeys.put(Integer.valueOf(spilter[0]), processedFilePath);
		}
		//System.out.println(hmKeys);
		return hmKeys;
	}


}
