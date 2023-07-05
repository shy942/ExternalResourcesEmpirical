package RQ1;

import java.util.ArrayList;
import java.util.HashMap;

import utility.ContentLoader;

public class RQ1_VHS_RI {

	public RQ1_VHS_RI() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				String corpus="ecf";
			   // String alpha="0.0";
			    // for ecf=553, eclipse.jdt.core=989, eclipse.jdt.debug=557, eclipse.jdt.ui=1115,  eclipse.pde.ui=872, tomcat70=1053
		        int no_of_bug_report=553;
		        //for ecf=71, eclipse.jdt.core=159, eclipse.jdt.debug=126, eclipse.jdt.ui=130, eclipse.pde.ui=123, tomcat70=217
		        //int no_of_bug_report_ST=71;
		        
		        String goldsetFolderPath="DataSets//Goldset//"+corpus+"//";
				String resultPathVH="DataSets//versionhistory//VersionHistoryScore//"+corpus+"//VHbasedResult.txt";
				
		        
			    RQ1_VHS_RI obj=new RQ1_VHS_RI();
			    
			    System.out.println("VHS performance: ");
			    obj.VersioHistoryPerformance(resultPathVH,goldsetFolderPath, corpus, no_of_bug_report);	
			    
			    String resultPathAH="DataSets//AuthorHistory//"+corpus+"//RIbasedResult.txt";
			    System.out.println("RI performance: ");
			    obj.ReporterInformationPerformance(resultPathAH,goldsetFolderPath, corpus, no_of_bug_report);

				
	}
	
	public HashMap<String, ArrayList<String>> getTruthSet(int RI, String goldsetPath, 	HashMap<String, ArrayList<String>> hmResultStorage, String corpus)
	{
		HashMap<String, ArrayList<String>> hmGoldset=new HashMap<>();
		for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> goldList=new ArrayList<>();
			ArrayList<String> content=ContentLoader.getAllLinesList(goldsetPath+bugID+".txt");
			for(String line:content)
			{
				if(corpus=="ecf"&&RI==0)
				{
					String[] slash_spilter=line.split("/");
					String file="";
					for(int i=4;i<slash_spilter.length-1;i++)file=file+slash_spilter[i]+".";
					file=file+slash_spilter[slash_spilter.length-1];
					if(!goldList.contains(file))goldList.add(file);
				}
				
				else if(corpus=="ecf"&&RI==1)
				{
					String[] slash_spilter=line.split("/");
					String file="";
					for(int i=0;i<slash_spilter.length-1;i++)file=file+slash_spilter[i]+".";
					file=file+slash_spilter[slash_spilter.length-1];
					if(!goldList.contains(file))goldList.add(file);
				}
				
				else if(corpus=="eclipse.jdt.debug")
				{
					String[] slash_spilter=line.split("/");
					String file="";
					for(int i=2;i<slash_spilter.length-1;i++)file=file+slash_spilter[i]+".";
					file=file+slash_spilter[slash_spilter.length-1];
					if(!goldList.contains(file))goldList.add(file);
					
				}
				
				else if(corpus=="eclipse.jdt.core")
				{
					if(line.contains("org")&&line.contains("src"))
					 {
						 int index=line.indexOf("src");
						 String processedFilePathWithBackslash=line.substring(index+4);
						 String[] spilter=processedFilePathWithBackslash.split("/");
						 String processedFilePath="";
						 for(int i=0;i<spilter.length-1;i++)processedFilePath=processedFilePath+spilter[i]+".";
						 processedFilePath=processedFilePath+spilter[spilter.length-1];
						 if(!goldList.contains(processedFilePath))goldList.add(processedFilePath);
					 }
				}
				
				else if(corpus=="eclipse.jdt.ui")
				{
					if(line.contains("org"))
					{
						String[] slash_spilter=line.split("/");
						String file="";
						for(int i=2;i<slash_spilter.length-1;i++)file=file+slash_spilter[i]+".";
						file=file+slash_spilter[slash_spilter.length-1];
						if(!goldList.contains(file))goldList.add(file);
					}
				}
				else if(corpus=="eclipse.pde.ui")
				{
					if(line.contains("org")&&line.contains("src"))
					 {
						 int index=line.indexOf("src");
						 String processedFilePathWithBackslash=line.substring(index+4);
						 String[] spilter=processedFilePathWithBackslash.split("/");
						 String processedFilePath="";
						 for(int i=0;i<spilter.length-1;i++)processedFilePath=processedFilePath+spilter[i]+".";
						 processedFilePath=processedFilePath+spilter[spilter.length-1];
						 if(!goldList.contains(processedFilePath))goldList.add(processedFilePath);
					 }
				}
				else if(corpus=="tomcat70")
				{
					if(line.contains("org"))
					{
						//System.out.println(line);
						int index=line.indexOf("org");
						String processedFilePathWithBackslash=line.substring(index);
						String[] spilter=processedFilePathWithBackslash.split("/");
						String processedFilePath="";
						for(int i=0;i<spilter.length-1;i++)processedFilePath=processedFilePath+spilter[i]+".";
						processedFilePath=processedFilePath+spilter[spilter.length-1];
						if(!goldList.contains(processedFilePath))goldList.add(processedFilePath);
					}
				}
				/*
				int discard=0;
				//System.out.println(line);
				String[] slash_spilter=line.split("/");
			
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
				}*/
			}
		hmGoldset.put(bugID, goldList);
		}
		return hmGoldset;
	}
	/*
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
	}*/

	public void VersioHistoryPerformance(String resultPathVH, String goldsetFolderPath, String corpus, int total_bug_report)
	{
		HashMap<String, ArrayList<String>> hmResultStorage=getResultStorageVHS(resultPathVH) ;
		
		HashMap<String, ArrayList<String>> hmGoldset=getTruthSet(0,goldsetFolderPath, hmResultStorage, corpus);
		
		System.out.println(hmResultStorage.size());
        double averageRecall=0.0;
        HashMap<String, ArrayList<String>> hmResultSTtop10=new HashMap<>();
        for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> fileList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			ArrayList<String> rawGoldList=new ArrayList<>();
			if(hmGoldset.containsKey(bugID))
			{
				//rawGoldList=hmGoldset.get(bugID);
				goldList=hmGoldset.get(bugID);
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
		ComputeTopkST(1, hmResultStorage,hmGoldset, total_bug_report);
		//Top-2
		ComputeTopkST(2, hmResultStorage,hmGoldset, total_bug_report);
		//Top-3
		ComputeTopkST(3, hmResultStorage,hmGoldset, total_bug_report);
		//Top-4
		ComputeTopkST(4, hmResultStorage,hmGoldset, total_bug_report);
		//Top-5
		ComputeTopkST(5, hmResultStorage,hmGoldset,total_bug_report);
		//Top-6
		ComputeTopkST(6, hmResultStorage,hmGoldset, total_bug_report);
		//Top-7
		ComputeTopkST(7, hmResultStorage,hmGoldset, total_bug_report);
		//Top-8
		ComputeTopkST(8, hmResultStorage,hmGoldset, total_bug_report);
		//Top-9
		ComputeTopkST(9, hmResultStorage,hmGoldset, total_bug_report);
		//Top-10
		ComputeTopkST(10, hmResultStorage,hmGoldset,total_bug_report);
		//MRR
		ComputeMRR_BLUiR(hmResultSTtop10,total_bug_report);
		//MAP
		ComputeMAP(hmResultSTtop10, total_bug_report);
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
	private void ComputeTopkST(int topK, HashMap<String, ArrayList<String>> hmResultStorage,
			HashMap<String, ArrayList<String>> hmGoldset, int no_of_bug_report) {
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
				//rawGoldList=hmGoldset.get(bugID);
				goldList=hmGoldset.get(bugID);
			}
			int count=0;
			for(String file:fileList)
			{
				
				if(count>topK)break;
				count++;
				if(goldList.contains(file))
				{
					no_of_topK++;
					//System.out.println(no_of_topK+" "+bugID);
					break;
				}
			}
			
		}
		//System.out.println("% of ST: "+Double.valueOf(no_of_bug_ST)/Double.valueOf(no_of_bug_report)*100);
		System.out.println("% of Top-"+(topK+1)+" : "+Double.valueOf(no_of_topK)/Double.valueOf(no_of_bug_report)*100+"%");
		//System.out.println("% of Top-"+(topK+1)+" in ST: "+Double.valueOf(no_of_topK)/Double.valueOf(no_of_bug_ST)*100+"%");

	}


	


	private HashMap<String, ArrayList<String>> getResultStorageVHS(String resultFilePathST) {
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

	
	public void ReporterInformationPerformance(String resultPathVH, String goldsetFolderPath, String corpus, int total_bug_report)
	{
		HashMap<String, ArrayList<String>> hmResultStorage=getResultStorageVHS(resultPathVH) ;
		
		HashMap<String, ArrayList<String>> hmGoldset=getTruthSet(1, goldsetFolderPath, hmResultStorage, corpus);
		
		System.out.println(hmResultStorage);
		System.out.println(hmGoldset);
        double averageRecall=0.0;
        HashMap<String, ArrayList<String>> hmResultSTtop10=new HashMap<>();
        for(String bugID:hmResultStorage.keySet())
		{
			ArrayList<String> fileList=hmResultStorage.get(bugID);
			ArrayList<String> goldList=new ArrayList<>();
			ArrayList<String> rawGoldList=new ArrayList<>();
			if(hmGoldset.containsKey(bugID))
			{
				//rawGoldList=hmGoldset.get(bugID);
				goldList=hmGoldset.get(bugID);
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
       // System.out.println(hmResultSTtop10);
		
		//Top-1
		ComputeTopkST(1, hmResultStorage,hmGoldset, total_bug_report);
		//Top-2
		ComputeTopkST(2, hmResultStorage,hmGoldset, total_bug_report);
		//Top-3
		ComputeTopkST(3, hmResultStorage,hmGoldset, total_bug_report);
		//Top-4
		ComputeTopkST(4, hmResultStorage,hmGoldset, total_bug_report);
		//Top-5
		ComputeTopkST(5, hmResultStorage,hmGoldset,total_bug_report);
		//Top-6
		ComputeTopkST(6, hmResultStorage,hmGoldset, total_bug_report);
		//Top-7
		ComputeTopkST(7, hmResultStorage,hmGoldset, total_bug_report);
		//Top-8
		ComputeTopkST(8, hmResultStorage,hmGoldset, total_bug_report);
		//Top-9
		ComputeTopkST(9, hmResultStorage,hmGoldset, total_bug_report);
		//Top-10
		ComputeTopkST(10, hmResultStorage,hmGoldset,total_bug_report);
		//MRR
		ComputeMRR_BLUiR(hmResultSTtop10,total_bug_report);
		//MAP
		ComputeMAP(hmResultSTtop10, total_bug_report);
	}
}
