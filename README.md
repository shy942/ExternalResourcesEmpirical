# Dataset Collection
Source corpus and goldset are collected from https://github.com/masud-technope/BLIZZARD-Replication-Package-ESEC-FSE2018/tree/master (Improving IR-Based Bug Localization with Context-Aware Query Reformulation)

AuthorHistory.zip contains the authors' information for all six subject systems

StackTraces.zip contains the stack trace information for all six subject systems

VersionHistory.zip contains version history information for all six subject systems

# Results produced by six information sources

#RQ1

TS -  Unzip TS.zip. The result file for a given system will be as follows: system.txt (e.g., ecf.txt)

TC -  Unzip TC-BFH.zip. The result file for a given system will be as follows: system_output_0.0_no_of_bug.txt (e.g., ecf_output_0.0_553.txt)

BFH -  Unzip TC-BFH.zip. The result file for a given system will be as follows: system_output_1.0_no_of_bug.txt (e.g., ecf_output_1.0_553.txt)

ST - Unzip ST.zip. There are six folders for all subject systems. The stack trace result file format is as follows: STbasedResult.txt

VHS - Unzip VHS.zip. There are six folders for all subject systems. The version history score file format for each system is as follows: VHbasedResult.txt

RI - Unzip RI.zip. There are six folders for all subject systems. The reporter information file format for each system is as follows: RIbasedResult.txt

RQ2

Combined Results - Unzip RQ2.zip. The results for eleven combinations of information sources can be found here.
