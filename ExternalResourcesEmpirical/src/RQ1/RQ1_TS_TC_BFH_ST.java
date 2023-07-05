package RQ1;

import java.util.ArrayList;
import java.util.HashMap;

import utility.ContentLoader;




public class RQ1_TS_TC_BFH_ST {

	public static HashMap<String, ArrayList<String>> hmResultStorageBLtop10=new HashMap<>();	
    public static HashMap<String, ArrayList<String>> hmResultStorageB$BLtop10=new HashMap<>();
    

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String corpus="ecf";
	    String alpha="0.0";
	    // for ecf=553, eclipse.jdt.core=989, eclipse.jdt.debug=557, eclipse.jdt.ui=1115,  eclipse.pde.ui=872, tomcat70=1053
        int no_of_bug_report=553;
        //for ecf=71, eclipse.jdt.core=159, eclipse.jdt.debug=126, eclipse.jdt.ui=130, eclipse.pde.ui=123, tomcat70=217
        int no_of_bug_report_ST=71;
        
        //collect id-source map
      	//String idSourceMapFilePath="DataSets//Corpus//"+corpus+".ckeys";
        //HashMap<String, String> hmIDsource=getIDsouceMap(idSourceMapFilePath);
        //base for optimal query
	    String base="DataSets\\BugLocatorResults\\";
	    //String resultFileName=corpus+"_output_"+alpha+"_"+no_of_bug_report+".txt";
	    String resultFileNameB4BL=base+corpus+"_output_"+alpha+"_B4BL"+".txt";
	    //String resultFilePath=base+resultFileName;
	    
	    String resultFilePathBLUiR="DataSets\\BLUiRResults\\"+corpus+".txt";
	    //String resultFilePath=base+resultFileName;
	    
	    String goldsetFolderPath="DataSets//Goldset//"+corpus+"//";
		String resultPathST="DataSets//StackTrace//"+corpus+"//STbasedResult.txt";
		
	    RQ1_TS_TC_BFH_ST obj=new RQ1_TS_TC_BFH_ST();
	    
	    System.out.println("TC performance: ");
	    //obj.BugLocatorPerformance(resultFilePath, no_of_bug_report);
	    //collect id-source map
      	String idSourceMapFilePath="DataSets//Corpus//"+corpus+".ckeys";
        HashMap<String, String> hmIDsource=obj.getIDsouceMap(idSourceMapFilePath);
      	//Performance for TC
        obj.BugLocatorPerformanceB4BL(resultFileNameB4BL, goldsetFolderPath, no_of_bug_report, corpus, hmIDsource);
	   
        System.out.println("BFH performance: ");
        alpha="1.0";
        resultFileNameB4BL=base+corpus+"_output_"+alpha+"_B4BL"+".txt";
        obj.BugLocatorPerformanceB4BL(resultFileNameB4BL, goldsetFolderPath, no_of_bug_report, corpus, hmIDsource);
 	   
	    //start - Testing code 
	    //RQ1_TS_TC_BFH_ST.hmResultStorageBLtop10=getTop10resultsForBL(resultFilePath);
	    //RQ1_TS_TC_BFH_ST.hmResultStorageB$BLtop10=getTop10resultsForB4BL(resultFileNameB4BL, hmIDsource,goldsetFolderPath, corpus);
	    //obj.compareBLresuts(RQ1_TS_TC_BFH_ST.hmResultStorageBLtop10, RQ1_TS_TC_BFH_ST.hmResultStorageB$BLtop10);
	    //end - Testing code
	    
	    System.out.println("TS performance:  ");
	    obj.BLUIRperformance(resultFilePathBLUiR,  goldsetFolderPath, no_of_bug_report, corpus);
	    
	    System.out.println("ST performance: ");
	    obj.StackTraceperformance(resultPathST, goldsetFolderPath, no_of_bug_report_ST, corpus, no_of_bug_report);
	}
	
	private void compareBLresuts(HashMap<String, ArrayList<String>> hmResultStorageBLtop102,
			HashMap<String, ArrayList<String>> hmResultStorageB$BLtop102) {
		// TODO Auto-generated method stub
		int count=0;
		int i=0;
		for(String bugID:hmResultStorageB$BLtop102.keySet())
		{
			if(hmResultStorageBLtop102.containsKey(bugID))count++;
			else System.out.println(++i +" "+bugID);
			//ArrayList<String> rankList=hmResultStorage.get(bugID);
		}
	}

	private static HashMap<String, ArrayList<String>> getTop10resultsForB4BL(String resultFilePathBL_B4BL, HashMap<String,String> hmIDsource, String goldsetFolderPath, String corpus) {
		// TODO Auto-generated method stub
		HashMap<String, ArrayList<String>> hmResultStorage=new HashMap<>();
		ArrayList<String> list=ContentLoader.getAllLinesList(resultFilePathBL_B4BL);
		//System.out.println(list);
		for(String line:list)
		{
			String[] spilter=line.split("\\t");
			//System.out.println(spilter[2]);
			String bugID=spilter[0];
			
			/*String rank=spilter[3];
			ArrayList<String> rankList=new ArrayList<>();
			if(hmResultStorage.containsKey(bugID))
			{
				rankList=hmResultStorage.get(bugID);
			}
			rankList.add(rank);
			hmResultStorage.put(bugID, rankList);*/
			
			String file=spilter[3];
			ArrayList<String> rankList=new ArrayList<>();
			String [] filespilter=file.split("\\.");
			String fileID=filespilter[filespilter.length-2];
			String fileName=hmIDsource.get(fileID);
			String file2save="";
			for(int i=0;i<filespilter.length-2;i++)
			{
				file2save=file2save+filespilter[i]+".";
			}
			file2save=file2save+fileName+".java";
			if(hmResultStorage.containsKey(bugID))
			{
				rankList=hmResultStorage.get(bugID);
			}
			rankList.add(file2save);
			hmResultStorage.put(bugID, rankList);
			
			//String scoreStr=spilter[2];
			//HashMap<String, String> hmFileInfo=new HashMap<>();
			//if(hmResultStorage.containsKey(bugID))
			//{
				//hmFileInfo=hmResultStorage.get(bugID);
			//}
			//hmFileInfo.put(file2save, scoreStr);
			//hmResultStorage.put(bugID, hmFileInfo);

		}
		HashMap<String, ArrayList<String>> hmResultBL_B4BLtop10=new HashMap<>();
		HashMap<String, ArrayList<String>> hmGoldset=getTruthSet(goldsetFolderPath, hmResultStorage, corpus);
		
        for(String bugID:hmResultStorage.keySet())
		{
        	//if(bugID.equalsIgnoreCase("259389"))
			ArrayList<String> fileList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			if(hmGoldset.containsKey(bugID))goldList=hmGoldset.get(bugID);
			
			int count=0;
			for(String file:fileList)
			{
				
				if(count>=10) break;
				if(goldList.contains(file))
				{
					ArrayList<String> rankList=new ArrayList<>();
					if(hmResultBL_B4BLtop10.containsKey(bugID))rankList=hmResultBL_B4BLtop10.get(bugID);
					rankList.add(String.valueOf(count));
					hmResultBL_B4BLtop10.put(bugID, rankList);
					//break;
				}
				count++;
			}
			
		}
        System.out.println(hmResultBL_B4BLtop10);
		return hmResultBL_B4BLtop10;
		

	}
	
	private static HashMap<String, ArrayList<String>> getTop10resultsForBL(String resultFilePath) {
		// TODO Auto-generated method stub
		HashMap<String, ArrayList<String>> hmResultStorage=new HashMap<>();
		ArrayList<String> list=ContentLoader.getAllLinesList(resultFilePath);
		//System.out.println(list);
		for(String line:list)
		{
			String[] spilter=line.split(",");
			//System.out.println(spilter[3]);
			String bugID=spilter[0];
			String rank=spilter[2];
			ArrayList<String> rankList=new ArrayList<>();
			if(hmResultStorage.containsKey(bugID))
			{
				rankList=hmResultStorage.get(bugID);
			}
			rankList.add(rank);
			hmResultStorage.put(bugID, rankList);
		}
	
		HashMap<String, ArrayList<String>> hmResultBugLocatortop10=new HashMap<>();
        for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> rankList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			
			int count=0;
			for(String rank:rankList)
			{
				
				//if(count>10) break;
				
				int rankInt=Integer.valueOf(rank);
				if(rankInt<10)
				{
					ArrayList<String> rankListTop10=new ArrayList<>();
					if(hmResultBugLocatortop10.containsKey(bugID))rankListTop10=hmResultBugLocatortop10.get(bugID);
					rankListTop10.add(String.valueOf(rankInt));
					hmResultBugLocatortop10.put(bugID, rankListTop10);
					//break;
				}
				count++;
			}
			
		}
        System.out.println(hmResultBugLocatortop10);
        return hmResultBugLocatortop10;
	}

	public void compareBLresuts(String resultFilePath, String resultFileNameB4BL, HashMap<String, String> hmIDsource)
	{
		HashMap<String, ArrayList<String>> hmResultStorage=getResultStorageBugLocator(resultFilePath);
		//HashMap<String, ArrayList<String>> hmResultBugLocatortop10=new HashMap<>();
        
		
		HashMap<String, ArrayList<String>> hmResultStorageB4BL=getResultStorageBugLocatorB4BL(resultFileNameB4BL, hmIDsource) ;
		int count=0;
		for(String bugID:hmResultStorage.keySet())
		{
			if(hmResultStorageB4BL.containsKey(bugID))count++;
			//System.out.println(count);
			//ArrayList<String> rankList=hmResultStorage.get(bugID);
		}
	}
	public HashMap<String, String> getIDsouceMap(String filePath)
	{
		HashMap<String,String> hmMap=new HashMap<>();
		ArrayList<String> list=ContentLoader.readContent(filePath);
		//System.out.println(list);
		for(String line:list)
		{
			String[] spilter=line.split(":");
			String id=spilter[0];
			String fileWithBackSlash=spilter[2];
			String[] fileSpilter=fileWithBackSlash.split("\\\\");
			//System.out.println(line+"\n"+fileSpilter[fileSpilter.length-1]);
			String file=fileSpilter[fileSpilter.length-1];
			String file2save=file.substring(0,file.length()-5);
			//System.out.println(file2save);
			hmMap.put(id, file2save);
		}
		return hmMap;
	}
	public HashMap<String, ArrayList<String>> getResultStorageBugLocatorB4BL(String resultFilePathBL_B4BL, HashMap<String, String> hmIDsource)
	{
		HashMap<String, ArrayList<String>> hmResultStorage=new HashMap<>();
		ArrayList<String> list=ContentLoader.getAllLinesList(resultFilePathBL_B4BL);
		//System.out.println(list);
		for(String line:list)
		{
			String[] spilter=line.split("\\t");
			//System.out.println(spilter[2]);
			String bugID=spilter[0];
			
			/*String rank=spilter[3];
			ArrayList<String> rankList=new ArrayList<>();
			if(hmResultStorage.containsKey(bugID))
			{
				rankList=hmResultStorage.get(bugID);
			}
			rankList.add(rank);
			hmResultStorage.put(bugID, rankList);*/
			
			String file=spilter[3];
			ArrayList<String> rankList=new ArrayList<>();
			String [] filespilter=file.split("\\.");
			String fileID=filespilter[filespilter.length-2];
			String fileName=hmIDsource.get(fileID);
			String file2save="";
			for(int i=0;i<filespilter.length-2;i++)
			{
				file2save=file2save+filespilter[i]+".";
			}
			file2save=file2save+fileName+".java";
			if(hmResultStorage.containsKey(bugID))
			{
				rankList=hmResultStorage.get(bugID);
			}
			rankList.add(file2save);
			hmResultStorage.put(bugID, rankList);
			
			//String scoreStr=spilter[2];
			//HashMap<String, String> hmFileInfo=new HashMap<>();
			//if(hmResultStorage.containsKey(bugID))
			//{
				//hmFileInfo=hmResultStorage.get(bugID);
			//}
			//hmFileInfo.put(file2save, scoreStr);
			//hmResultStorage.put(bugID, hmFileInfo);

		}
		
		//System.out.println(hmResultStorage);
		
		return hmResultStorage;
	}

	public void BugLocatorPerformanceB4BL(String resultFilePathBL_B4BL, String goldsetFolderPath, int no_of_bug_report, String corpus, HashMap<String, String> hmIDsource)
	{
		HashMap<String, ArrayList<String>> hmResultStorage=getResultStorageBugLocatorB4BL(resultFilePathBL_B4BL, hmIDsource) ;
		
		HashMap<String, ArrayList<String>> hmGoldset=getTruthSet(goldsetFolderPath, hmResultStorage, corpus);
		
		//System.out.println(hmResultStorage);
        double averageRecall=0.0;
        HashMap<String, ArrayList<String>> hmResultBL_B4BLtop10=new HashMap<>();
        for(String bugID:hmResultStorage.keySet())
		{
        	//if(bugID.equalsIgnoreCase("303030"))
        	{
			ArrayList<String> fileList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			if(hmGoldset.containsKey(bugID))goldList=hmGoldset.get(bugID);
			//System.out.println("Testing for 206528");
			//System.out.println(fileList);
		//	System.out.println(hmGoldset.get(bugID));
			int count=0;
			for(String file:fileList)
			{
				
				if(count>=10) break;
				if(goldList.contains(file))
				{
					ArrayList<String> rankList=new ArrayList<>();
					if(hmResultBL_B4BLtop10.containsKey(bugID))rankList=hmResultBL_B4BLtop10.get(bugID);
					rankList.add(String.valueOf(count));
					hmResultBL_B4BLtop10.put(bugID, rankList);
					//break;
				}
				count++;
			}
			
		}}
        System.out.println(hmResultBL_B4BLtop10);
		System.out.println(no_of_bug_report+" "+hmResultBL_B4BLtop10.size());
		//Top-1
        ComputeTopkBugLocator(1, hmResultBL_B4BLtop10, no_of_bug_report);
		//Top-2
        ComputeTopkBugLocator(2, hmResultBL_B4BLtop10, no_of_bug_report);
		//Top-3
        ComputeTopkBugLocator(3, hmResultBL_B4BLtop10, no_of_bug_report);
		//Top-4
        ComputeTopkBugLocator(4, hmResultBL_B4BLtop10, no_of_bug_report);
		//Top-5
		ComputeTopkBLUiR(5, hmResultStorage,hmGoldset,no_of_bug_report);
		//Top-6
		ComputeTopkBLUiR(6, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-7
		ComputeTopkBLUiR(7, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-8
		ComputeTopkBLUiR(8, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-9
		ComputeTopkBugLocator(9, hmResultBL_B4BLtop10, no_of_bug_report);
		//Top-10
		ComputeTopkBLUiR(10, hmResultStorage,hmGoldset,no_of_bug_report);
		//MRR
		ComputeMRR_BLUiR(hmResultBL_B4BLtop10,no_of_bug_report);
		//MAP
		ComputeMAP(hmResultBL_B4BLtop10, no_of_bug_report);
	}
	
	public void BugLocatorPerformance(String resultFilePath,  int no_of_bug_report)
	{
		
		HashMap<String, ArrayList<String>> hmResultStorage=getResultStorageBugLocator(resultFilePath);
		HashMap<String, ArrayList<String>> hmResultBugLocatortop10=new HashMap<>();
        for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> rankList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			
			int count=0;
			for(String rank:rankList)
			{
				
				//if(count>10) break;
				
				int rankInt=Integer.valueOf(rank);
				if(rankInt<10)
				{
					ArrayList<String> rankListTop10=new ArrayList<>();
					if(hmResultBugLocatortop10.containsKey(bugID))rankListTop10=hmResultBugLocatortop10.get(bugID);
					rankListTop10.add(String.valueOf(rankInt));
					hmResultBugLocatortop10.put(bugID, rankListTop10);
					//break;
				}
				count++;
			}
			
		}
        System.out.println(hmResultBugLocatortop10);
        System.out.println(no_of_bug_report+" "+hmResultBugLocatortop10.size());
		
		//Top-1
		ComputeTopkBugLocator(1, hmResultStorage, no_of_bug_report);
		//Top-2
		ComputeTopkBugLocator(2, hmResultStorage, no_of_bug_report);
		//Top-3
		ComputeTopkBugLocator(3, hmResultStorage, no_of_bug_report);
		//Top-4
		ComputeTopkBugLocator(4, hmResultStorage, no_of_bug_report);		
		//Top-5
		ComputeTopkBugLocator(5, hmResultStorage, no_of_bug_report);
		//Top-6
		ComputeTopkBugLocator(6, hmResultStorage, no_of_bug_report);
		//Top-7
		ComputeTopkBugLocator(7, hmResultStorage, no_of_bug_report);
		//Top-8
		ComputeTopkBugLocator(8, hmResultStorage, no_of_bug_report);
		//Top-9
		ComputeTopkBugLocator(9, hmResultStorage, no_of_bug_report);
		//Top-10
		ComputeTopkBugLocator(10, hmResultStorage, no_of_bug_report);
		//MRR
		ComputeMRRBugLocator(hmResultBugLocatortop10, no_of_bug_report);
		//MAP
		ComputeMAP(hmResultBugLocatortop10, no_of_bug_report);
	}
	
	public HashMap<String, ArrayList<String>> getResultStorageBugLocator(String resultFilePath)
	{
		HashMap<String, ArrayList<String>> hmResultStorage=new HashMap<>();
		ArrayList<String> list=ContentLoader.getAllLinesList(resultFilePath);
		//System.out.println(list);
		for(String line:list)
		{
			String[] spilter=line.split(",");
			//System.out.println(spilter[3]);
			String bugID=spilter[0];
			String rank=spilter[2];
			ArrayList<String> rankList=new ArrayList<>();
			if(hmResultStorage.containsKey(bugID))
			{
				rankList=hmResultStorage.get(bugID);
			}
			rankList.add(rank);
			hmResultStorage.put(bugID, rankList);
		}
		
		//System.out.println(hmResultStorage);
		
		return hmResultStorage;
	}
	
	public static double ComputeMRRBugLocator(HashMap<String, ArrayList<String>> hmResultBugLocatortop10, int no_of_bug)
    {
		//System.out.println(hmResultStorage);
		double averageRecall=0.0;
        for(String queryID: hmResultBugLocatortop10.keySet())
        {
            ArrayList<String> rankList=hmResultBugLocatortop10.get(queryID);
            //System.out.println(rankList);
            averageRecall+=get1stRecall(rankList);
        }
        double MRR=averageRecall/Double.valueOf(no_of_bug);
        System.out.println("averageRecall: "+averageRecall+" no_of_bug: "+no_of_bug+" MRR: "+MRR);
        return MRR;
    }
	public static double ComputeMRR_BLUiR(HashMap<String, ArrayList<String>> hmResultBLUiRtop10, int no_of_bug)
    {
		double averageRecall=0.0;
        for(String queryID: hmResultBLUiRtop10.keySet())
        {
            ArrayList<String> rankList=hmResultBLUiRtop10.get(queryID);
            averageRecall+=get1stRecall(rankList);
        }
        double MRR=averageRecall/Double.valueOf(no_of_bug);
        System.out.println("averageRecall: "+averageRecall+" no_of_bug: "+no_of_bug+" MRR: "+MRR);
        return MRR;
    }
    public static double get1stRecall(ArrayList<String> rankList)
    {
        double recall1st=0.0;
        int count =0;
        int length=rankList.size();
        int rank1st=Integer.valueOf(rankList.get(0));
        //System.out.println(rank1st);
        double lowerPart=0.0;
        lowerPart=Double.valueOf(rank1st)+1.00;
        recall1st=1/lowerPart;
        
        
        return recall1st;
        
    }
    
    public static double ComputeMAP(HashMap<String, ArrayList<String>> finalRankedResult, int no_of_bug)
	{
		double averagePrecision=0.0;
		for(String queryID: finalRankedResult.keySet())
		{
			ArrayList<String> rankList=finalRankedResult.get(queryID);
			averagePrecision+=getAvgPrecisionEachQuery(rankList, queryID);
			//System.out.println(rankList);
			//System.out.println(getAvgPrecisionEachQuery(rankList));
		}
	
		System.out.println("averagePrecision: "+averagePrecision);
		double MAP=averagePrecision/Double.valueOf(no_of_bug);
		System.out.println("Total Query: "+no_of_bug+" MAP: "+MAP*100+"%");
		return MAP;
	}
	
	public static double getAvgPrecisionEachQuery(ArrayList<String> rankList, String queryID)
	{
		double Precision=0.0;
		int count =0;
		for(String rankStr:rankList)
		{
			//System.out.println(rankList);
			count++;
			int rank=Integer.valueOf(rankStr);
			//System.out.println(queryID);
			Precision+=Double.valueOf(count)/Double.valueOf(rank+1);
		}
		//int length=obj.gitResultsMap.get(queryID).size();
		double AvgPrecision=Precision/Double.valueOf(count);
		//double AvgPrecision=Precision/Double.valueOf(length);
		return AvgPrecision;
		
	}
	public HashMap<String, ArrayList<String>> getResultStorageBLUiR(String resultFilePathBLUiR)
	{
		HashMap<String, ArrayList<String>> hmResultStorage=new HashMap<>();
		ArrayList<String> list=ContentLoader.getAllLinesList(resultFilePathBLUiR);
		//System.out.println(list);
		for(String line:list)
		{
			String[] spilter=line.split(" ");
			//System.out.println(spilter[2]);
			String bugID=spilter[0];
			String rank=spilter[2];
			ArrayList<String> rankList=new ArrayList<>();
			if(hmResultStorage.containsKey(bugID))
			{
				rankList=hmResultStorage.get(bugID);
			}
			rankList.add(rank);
			hmResultStorage.put(bugID, rankList);
		}
		
		//System.out.println(hmResultStorage);
		
		return hmResultStorage;
	}
	
	public static HashMap<String, ArrayList<String>> getTruthSet(String goldsetPath, 	HashMap<String, ArrayList<String>> hmResultStorage, String corpus)
	{
		HashMap<String, ArrayList<String>> hmGoldset=new HashMap<>();
		for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> goldList=new ArrayList<>();
			ArrayList<String> content=ContentLoader.getAllLinesList(goldsetPath+bugID+".txt");
			for(String line:content)
			{
				int discard=0;
				//System.out.println(line);
				String[] slash_spilter=line.split("/");
			
				String afterDiscard="";
				if(corpus=="ecf")
				{
					if(slash_spilter.length>6&&slash_spilter[6].equalsIgnoreCase("ch"))for(int i=6;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
					else if(slash_spilter[0].equalsIgnoreCase("applications"))for(int i=5;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
					else if(slash_spilter[0].equalsIgnoreCase("server-side")&&slash_spilter[1].equalsIgnoreCase("examples"))for(int i=5;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
					else for(int i=4;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
				}
				else if(corpus=="eclipse.jdt.ui"|| corpus=="eclipse.jdt.debug" || corpus=="eclipse.jdt.core")
				{
					for(int i=2;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
				}
				else if(corpus=="eclipse.pde.ui")
				{
					for(int i=3;i<slash_spilter.length-1;i++)afterDiscard=afterDiscard+slash_spilter[i]+"/";
				}
				else if(corpus=="tomcat70")
				{
					if(line.contains("org"))
					{
						//System.out.println(line);
						int index=line.indexOf("org");
						afterDiscard=line.substring(index);
					}
					else discard=1;
				}
				if(corpus!="tomcat70")afterDiscard=afterDiscard+slash_spilter[slash_spilter.length-1];
				String filePathWithDot=afterDiscard.replace("/", ".");
				//if(hmKeys.containsKey(filePathWithDot)) System.out.println(filePathWithDot);
				String processedFile="";
			
				int index=0;
				String processedFileWithDot=filePathWithDot;
			
				if(discard==0)
				{
					if(hmGoldset.containsKey(bugID))
					{
						goldList=hmGoldset.get(bugID);
					}
					goldList.add(processedFileWithDot);
					hmGoldset.put(bugID, goldList);
				}
			}
		}
		return hmGoldset;
	}
	
	public void BLUIRperformance(String resultFilePathBLUiR, String goldsetFolderPath, int no_of_bug_report, String corpus)
	{
		HashMap<String, ArrayList<String>> hmResultStorage=getResultStorageBLUiR(resultFilePathBLUiR) ;
		
		HashMap<String, ArrayList<String>> hmGoldset=getTruthSet(goldsetFolderPath, hmResultStorage, corpus);
		
		System.out.println(hmResultStorage.size()+" "+hmGoldset.size());
        double averageRecall=0.0;
        HashMap<String, ArrayList<String>> hmResultBLUiRtop10=new HashMap<>();
        for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> fileList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			if(hmGoldset.containsKey(bugID))goldList=hmGoldset.get(bugID);
			int count=0;
			for(String file:fileList)
			{
				
				if(count>=10) break;
				if(goldList.contains(file))
				{
					ArrayList<String> rankList=new ArrayList<>();
					if(hmResultBLUiRtop10.containsKey(bugID))rankList=hmResultBLUiRtop10.get(bugID);
					rankList.add(String.valueOf(count));
					hmResultBLUiRtop10.put(bugID, rankList);
					//break;
				}
				count++;
			}
			
		}
        System.out.println(hmResultBLUiRtop10);
		
		//Top-1
		ComputeTopkBLUiR(1, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-2
		ComputeTopkBLUiR(2, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-3
		ComputeTopkBLUiR(3, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-4
		ComputeTopkBLUiR(4, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-5
		ComputeTopkBLUiR(5, hmResultStorage,hmGoldset,no_of_bug_report);
		//Top-6
		ComputeTopkBLUiR(6, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-7
		ComputeTopkBLUiR(7, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-8
		ComputeTopkBLUiR(8, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-9
		ComputeTopkBLUiR(9, hmResultStorage,hmGoldset, no_of_bug_report);
		//Top-10
		ComputeTopkBLUiR(10, hmResultStorage,hmGoldset,no_of_bug_report);
		//MRR
		ComputeMRR_BLUiR(hmResultBLUiRtop10,no_of_bug_report);
		//MAP
		ComputeMAP(hmResultBLUiRtop10, no_of_bug_report);
	}
	
	
	public void ComputeTopkBLUiR(int topK, HashMap<String, ArrayList<String>> hmResultStorage, HashMap<String, ArrayList<String>> hmGoldset, int total_bug_report)
	{
		//System.out.println("Total Bug Reports: "+total_bug_report);
		int no_of_topK=0;
		topK=topK-1;
		for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> fileList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			if(hmGoldset.containsKey(bugID))goldList=hmGoldset.get(bugID);
			int count=0;
			for(String file:fileList)
			{
				
				if(count>topK)break;
				count++;
				if(goldList.contains(file))
				{
					no_of_topK++;
					break;
				}
			}
			
		}
		System.out.println("% of Top-"+(topK+1)+" : "+Double.valueOf(no_of_topK)/Double.valueOf(total_bug_report)*100);
	}

	
	
	public void ComputeTopkBugLocator(int topK, HashMap<String, ArrayList<String>> hmResultStorage, int no_of_bug_report)
	{
		//System.out.println("Total Bug Reports: "+no_of_bug_report);
		int no_of_topK=0;
		topK=topK-1;
		for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> resultList=hmResultStorage.get(bugID);
			for(String rank:resultList)
			{
				int rankResult=Integer.valueOf(rank);
				if(rankResult<=topK)
				{
					no_of_topK++;
					break;
				}
			}
			
		}
		System.out.println("% of Top-"+topK+" : "+Double.valueOf(no_of_topK)/Double.valueOf(no_of_bug_report)*100);
	}
	
	public void StackTraceperformance(String resultFilePathST, String goldsetFolderPath, int no_of_bug_report_ST, String corpus, int total_bug_report)
	{
		HashMap<String, ArrayList<String>> hmResultStorage=getResultStorageST(resultFilePathST) ;
		
		HashMap<String, ArrayList<String>> hmGoldset=getTruthSet(goldsetFolderPath, hmResultStorage, corpus);
		
		//System.out.println(hmResultStorage);
        double averageRecall=0.0;
        HashMap<String, ArrayList<String>> hmResultSTtop10=new HashMap<>();
        for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> fileList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			ArrayList<String> rawGoldList=new ArrayList<>();
			if(hmGoldset.containsKey(bugID))
			{
				rawGoldList=hmGoldset.get(bugID);
				goldList=processGoldList(rawGoldList);
			}
			int count=0;
			for(String file:fileList)
			{
				
				if(count>=10) break;
				if(goldList.contains(file))
				{
					ArrayList<String> rankList=new ArrayList<>();
					if(hmResultSTtop10.containsKey(bugID))rankList=hmResultSTtop10.get(bugID);
					rankList.add(String.valueOf(count));
					hmResultSTtop10.put(bugID, rankList);
					//break;
				}
				count++;
			}
			
		}
        System.out.println(hmResultSTtop10);
		
		//Top-1
		ComputeTopkST(1, hmResultStorage,hmGoldset, total_bug_report, no_of_bug_report_ST);
		//Top-2
		ComputeTopkST(2, hmResultStorage,hmGoldset, total_bug_report, no_of_bug_report_ST);
		//Top-3
		ComputeTopkST(3, hmResultStorage,hmGoldset, total_bug_report, no_of_bug_report_ST);
		//Top-4
		ComputeTopkST(4, hmResultStorage,hmGoldset, total_bug_report, no_of_bug_report_ST);
		//Top-5
		ComputeTopkST(5, hmResultStorage,hmGoldset,total_bug_report, no_of_bug_report_ST);
		//Top-6
		ComputeTopkST(6, hmResultStorage,hmGoldset, total_bug_report, no_of_bug_report_ST);
		//Top-7
		ComputeTopkST(7, hmResultStorage,hmGoldset, total_bug_report, no_of_bug_report_ST);
		//Top-8
		ComputeTopkST(8, hmResultStorage,hmGoldset, total_bug_report, no_of_bug_report_ST);
		//Top-9
		ComputeTopkST(9, hmResultStorage,hmGoldset, total_bug_report, no_of_bug_report_ST);
		//Top-10
		ComputeTopkST(10, hmResultStorage,hmGoldset,total_bug_report, no_of_bug_report_ST);
		//MRR
		ComputeMRR_BLUiR(hmResultSTtop10,total_bug_report);
		//MAP
		ComputeMAP(hmResultSTtop10, total_bug_report);
	}


	private void ComputeTopkST(int topK, HashMap<String, ArrayList<String>> hmResultStorage,
			HashMap<String, ArrayList<String>> hmGoldset, int no_of_bug_report, int no_of_bug_ST) {
		// TODO Auto-generated method stub
		int no_of_topK=0;
		topK=topK-1;
		for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> fileList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			//ArrayList<String> goldList=new ArrayList<>();
			ArrayList<String> rawGoldList=new ArrayList<>();
			if(hmGoldset.containsKey(bugID))
			{
				rawGoldList=hmGoldset.get(bugID);
				goldList=processGoldList(rawGoldList);
			}
			int count=0;
			for(String file:fileList)
			{
				
				if(count>topK)break;
				count++;
				if(goldList.contains(file))
				{
					no_of_topK++;
					break;
				}
			}
			
		}
		//System.out.println("% 		of ST: "+Double.valueOf(no_of_bug_ST)/Double.valueOf(no_of_bug_report)*100);
		System.out.println("% of Top-"+(topK+1)+" : "+Double.valueOf(no_of_topK)/Double.valueOf(no_of_bug_report)*100+"%");
		//System.out.println("% 		of Top-"+(topK+1)+" in ST: "+Double.valueOf(no_of_topK)/Double.valueOf(no_of_bug_ST)*100+"%");

	}


	private ArrayList<String> processGoldList(ArrayList<String> rawGoldList) {
		// TODO Auto-generated method stub
		ArrayList<String> processList=new ArrayList<>();
		for(String line:rawGoldList)
		{
			String[] spilter=line.split("\\.");
			String processedLine="";
			for(int i=0;i<spilter.length-2;i++)processedLine=processedLine+spilter[i]+".";
			processedLine=processedLine+spilter[spilter.length-2];
			processList.add(processedLine);
		}
		return processList;
	}


	private HashMap<String, ArrayList<String>> getResultStorageST(String resultFilePathST) {
		// TODO Auto-generated method stub
		HashMap<String, ArrayList<String>> hmResultStorage=new HashMap<>();
		ArrayList<String> list=ContentLoader.getAllLinesList(resultFilePathST);
		//System.out.println(list);
		for(String line:list)
		{
			String[] spilter=line.split(":");
			//System.out.println(spilter[2]);
			String bugID=spilter[0];
			String file=spilter[1];
			ArrayList<String> fileList=new ArrayList<>();
			if(hmResultStorage.containsKey(bugID))
			{
				fileList=hmResultStorage.get(bugID);
			}
			fileList.add(file);
			hmResultStorage.put(bugID, fileList);
		}
		
		//System.out.println(hmResultStorage);
		
		return hmResultStorage;
	}
	
}
