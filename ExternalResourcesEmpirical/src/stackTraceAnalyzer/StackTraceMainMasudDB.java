package stackTraceAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


//import CorpusCreator.BugExtractorForBugLocator;
import utility.ContentLoader;
import utility.ContentWriter;
import utility.SortMapByValue;

public class StackTraceMainMasudDB {
	ArrayList<String> bugIDlist;
	
	String corpus;
	int no_of_bug;
	public StackTraceMainMasudDB(String corpus) {
		// TODO Auto-generated constructor stub
		this.corpus=corpus;
		this.bugIDlist=new ArrayList<String>();
		
		this.no_of_bug=0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String corpus="ecf";
		String bugIDpath="DataSets//StackTrace//"+corpus+"//stBugIDdateBased.txt";
		File directory = new File(bugIDpath);
		   System.out.println(directory.getAbsolutePath());
		
		String STbugReportFolderPath="DataSets//BR-ST-StackTraces//"+corpus+"//";
		
		String goldsetFolderPath="DataSets//Goldset//"+corpus+"//";
		
		String STscroreOutputFile="DataSets//StackTrace//"+corpus+"//STbasedResult.txt";
		
		StackTraceMainMasudDB obj=new StackTraceMainMasudDB(corpus);
		obj.Manager(bugIDpath, STbugReportFolderPath, corpus, goldsetFolderPath, STscroreOutputFile);
	}

	private void Manager(String bugIDpath, String sTbugReportFolderPath, String corpus2, String goldsetFolderPath, String STscroreOutputFile) {
		// TODO Auto-generated method stub
		//collect bug data that contain stack trace
		getBugIDs(bugIDpath,sTbugReportFolderPath);
		System.out.println(no_of_bug);
		System.out.println("No of Bug Reports: "+bugIDlist.size());
		//collect gold set data
		HashMap<String, ArrayList<String>> hmGoldsetData=getTruthSet(goldsetFolderPath);
		System.out.println(hmGoldsetData);
		//collect stack trace data
		HashMap<String, ArrayList<String>> hmBugDataStackTrace=collectBugDataStackTrace(sTbugReportFolderPath);
		//System.out.println(hmBugDataStackTrace);
		//compute stack trace based similarity
		HashMap<String, HashMap<String, Double>> hmStackTraceScore=calculateStackTraceScore(hmBugDataStackTrace);
		System.out.println(hmStackTraceScore);
		HashMap <String, HashMap<String, Double>> sortedHmSTresult=SortedSTresult(hmStackTraceScore);
		System.out.println(sortedHmSTresult);
		ContentWriter.writeContentForSTscore(STscroreOutputFile, sortedHmSTresult);
		//localize bugs based on stack trace based similarity
		//compute performance
		
	}
	
	public HashMap<String, HashMap<String, Double>> SortedSTresult(HashMap <String, HashMap<String, Double>> hmSTresult)
	{
		HashMap <String, HashMap<String, Double>> sortedHmSTresult=new HashMap<>();
		int total_bug_report=0;
		int ST_bug_report=0;
		for(String bugID: hmSTresult.keySet())
		{ 
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
			ST_bug_report++;
			for(String file:contentHM.keySet())
			{
				
				boolean DESC = false;
				HashMap<String, Double> sortedContentHM=(HashMap<String, Double>) SortMapByValue.sortByComparator(contentHM, DESC);
				//System.out.println("After Sorting: "+sortedContentHM);
				sortedHmSTresult.put(bugID, sortedContentHM);
			}}
		}
		System.out.println(total_bug_report+" "+ST_bug_report);
		//this.total_Bug_Report=this.total_Bug_Report+total_bug_report;
		//this.toal_Bug_Report_StackTrace=this.toal_Bug_Report_StackTrace+ST_bug_report;
		return sortedHmSTresult;
	}

	public HashMap<String, HashMap<String, Double>> calculateStackTraceScore(HashMap<String, ArrayList<String>> hmBugDataStackTrace)
	{
		HashMap<String, HashMap<String, Double>> hmStackTraceScore=new HashMap<>();
		for(String bugID:hmBugDataStackTrace.keySet())
		{
			ArrayList<String> STlist=hmBugDataStackTrace.get(bugID);
			//ArrayList<String> STlist=this.hmStackTraceInfo.get(bugID);
            int size=STlist.size();
            int i=0;
            double score=0;
            ArrayList<String> listToSave=new ArrayList<>();
            HashMap<String,Double> stacktracefile=new HashMap<>();
            //System.out.println(STlist);
            for(String file:STlist)
            {
                i++;
                if(i>10) break;
                if(i<=10)
                {
                    score=(1/Double.valueOf(i));
                    listToSave.add(file+":"+score);
                    stacktracefile.put(file, score);
                }
                else if(i>10)
                {
                    score=0.1;
                    listToSave.add(file+":"+score);
                    stacktracefile.put(file, score);
                }
                
            }
            //this.hmStackTraceScore.put(bugID, listToSave);
            hmStackTraceScore.put(bugID, stacktracefile);
		}
		return hmStackTraceScore;
	}
	
	public HashMap<String,ArrayList<String>> getTruthSet(String goldsetPath)
	{
		HashMap<String, ArrayList<String>> hmGoldsetData=new HashMap<>();
		for(String bugID:bugIDlist)
		{
		//System.out.println("..........."+hmKeys);
		ArrayList<String> goldList=new ArrayList<>();
		ArrayList<String> content=ContentLoader.getAllLinesList(goldsetPath+"//"+bugID+".txt");
		for(String line:content)
		{
			//if(line.endsWith(".java")&& bugID.equalsIgnoreCase("59923")==true)
			if(line.endsWith(".java")&&line.contains("org"))
			{
				
				System.out.println(line);
				String[] slash_spilter=line.split("/");
				int discard=0;
				if(corpus=="eclipse.jdt.core"&&!line.contains("org"))discard=1;
				
				if(corpus=="eclipse.pde.ui"&&!line.contains("pde"))discard=1;
				
				if(corpus=="tomcat70"&&!line.contains("org"))discard=1;
				
				String afterDiscard="";
				if(corpus=="ecf")
				{
					for(int i=4;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
				}
				else if(corpus=="eclipse.jdt.ui"|| corpus=="eclipse.jdt.debug" || corpus=="eclipse.jdt.core")
				{
					for(int i=2;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
				}
				else if(corpus=="eclipse.pde.ui")
				{
					for(int i=3;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
				}
				else if(corpus=="tomcat70"&&discard==0)
				{
					int index=line.indexOf("org");
					afterDiscard=line.substring(index);
					//for(int i=index+4;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
				}
				if(corpus!="tomcat70")afterDiscard=afterDiscard+slash_spilter[slash_spilter.length-1];
				String filePathWithDot=afterDiscard.replace("/", ".");
				//if(hmKeys.containsKey(filePathWithDot)) System.out.println(filePathWithDot);
				String processedFile="";
			
				int index=0;
				String processedFileWithDot=filePathWithDot;
				
				
				String[] dotSpilter=processedFileWithDot.split("\\.");
			
				for(int i=0; i<dotSpilter.length-2;i++){
					processedFile=processedFile+dotSpilter[i]+".";
				}
				processedFile=processedFile+dotSpilter[dotSpilter.length-2
				                                       ];
				/*
				if(corpus=="eclipse.pde.ui" &&dotSpilter[0].equalsIgnoreCase("org")==false) discard=1;
				
				if(corpus=="tomcat70" &&dotSpilter[0].equalsIgnoreCase("org")==false) discard=1;
				
				//System.out.println("bdasg"+hmKeys);
				if(discard==0)
				{
							
					if(processedFile.contains("main")) processedFile=processedFile.substring(10);
			
					
					
					if(corpus=="eclipse.jdt.core"&&processedFile.contains("test")&&!processedFile.contains("org"))
					{
						String [] spilterTest=processedFile.split("\\.");
						
						String testProcessedFile="";
						for(int j=2;j<spilterTest.length-1;j++)testProcessedFile=testProcessedFile+spilterTest[j]+".";
						testProcessedFile=testProcessedFile+spilterTest[spilterTest.length-1];
						//System.out.println(processedFile);
						processedFile=testProcessedFile;
						//System.out.println(processedFile);
					}
					
					*/
					if(!goldList.contains(processedFile))
					{
						//System.out.println(processedFile);
						goldList.add(processedFile);
					}
					
					
	//			}
			}
			
		}
		hmGoldsetData.put(bugID, goldList);
		//return goldList;
		}
		return hmGoldsetData;
	}
	public HashMap<String, ArrayList<String>> collectBugDataStackTrace(String sTbugReportFolderPath)
	{
		HashMap<String, ArrayList<String>> hmStackTrace=new HashMap<>();
		for(String bugID:bugIDlist)
		{
			ArrayList<String> bugreportContent=ContentLoader.getAllLinesList(sTbugReportFolderPath+"//"+bugID+".txt");
			//System.out.println(bugreportContent);
			ArrayList<String> processList=getProcessedList(bugreportContent);
			ArrayList<String> list=new ArrayList<>();
			list.addAll(processList);
			hmStackTrace.put(bugID, list);
			
		}
		return hmStackTrace;
	}
	public ArrayList<String> getProcessedList(ArrayList<String> bugContent)
	{
		ArrayList<String> processList=new ArrayList<>();
		for(String line:bugContent)
		{
			if(line.startsWith("org"))
			{
				//System.out.println(line);
				String[] spilter=line.split("\\.");
				String file="";
				for(int i=0;i<spilter.length-2;i++)
				{
					file=file+spilter[i]+".";
				}
				file=file+spilter[spilter.length-2];
				if(processList.contains(file)==false)processList.add(file);
			}
		}
		
		return processList;
	}
	public void getBugIDs(String bugIDpath2Write,String bugReportFolderPath)
	{
		File fp=new File (bugReportFolderPath);
		loadBugData(fp);
		//System.out.println(this.bugIDlist);
		ContentWriter.writeContent(bugIDpath2Write, bugIDlist);
	}
	public void loadBugData(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				loadBugData(fileEntry);
			} else {
				 //System.out.println(fileEntry.getAbsolutePath());
				//if (fileEntry.getName().endsWith(".txt")) {
					//this.javaFilePaths.add(fileEntry.getAbsolutePath());
					String bugIdwithTXT=fileEntry.getName();
					String bugID=bugIdwithTXT.substring(0,bugIdwithTXT.length()-4);
					this.bugIDlist.add(no_of_bug++,
							bugID);
					//System.out.println(no_of_bug);
				//}
			}
		}
	}
}
