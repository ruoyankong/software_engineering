/*******************************************************************
*  FileIO
*
* FileIO is the GUI that prompts the user to choose the ballot file
* as well as the path to save the audit file.
*
* Author: Jingfan Guo
*******************************************************************/
package voting_system;

import javafx.application.Application;
import java.io.FileWriter;  
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileIO extends Application{

    /**
     * The main method.
     *
     * @param   args the argument list
     * @author  Jingfan Guo 
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main entry point for the JavaFX application.
     *
     * @param   primaryStage the primary stage for this application
     * @author  Jingfan Guo 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
    	Election election = new Election();
    	Statistics statistics ;
    	String file_path;
    	File ballotFile;
    	while(true) {
	    	System.out.println("Welcome to Voting System. Please select your ballot file first.\n");
	        FileChooser ballotFileChooser = new FileChooser();
	        ballotFileChooser.getExtensionFilters().addAll(
	            new FileChooser.ExtensionFilter("Comma-Separated Values Files (*.csv)", "*.csv")
	        );
	        ballotFileChooser.setTitle("Select Ballot File");
	        ballotFile = ballotFileChooser.showOpenDialog(null);
	        if (ballotFile == null) {
	            System.out.println("No file selected. Exiting the VS.");
	            System.exit(0);
	        }
	        file_path = ballotFile.getAbsolutePath();
	        System.out.println("You choose "+file_path);
			try {
    	 		boolean result = election.setup(file_path);
    	 		if (result) {
	     			System.out.println("Successfully setup.");
	     			break;
    	 		}else {
    	 			System.out.println("Setup election failed. Please check your file.");
    	 		}

		 	}catch(Exception e) {
		 		e.printStackTrace();
		 		System.out.println("Setup election failed. Please check your file.");				
		 	}

    	}

        statistics = election.statistics;
        try {
	    	System.out.println("Begin running election.");
			Thread t = new Thread(new RunElection(election));
			t.start();        
			String auditFileContent = election.runElection();
			System.out.println("Please input where to save audit file.");    		
	        FileChooser auditFileChooser = new FileChooser();
	        auditFileChooser.getExtensionFilters().addAll(
	            new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt")
	        );
	        auditFileChooser.setTitle("Save Audit File");
	        auditFileChooser.setInitialFileName("audit.txt");
	        File auditFile = auditFileChooser.showSaveDialog(null);
	
	        if (auditFile == null) {
				System.out.println("Saving canceled. Audit will NOT be saved");
			} else {
				FileWriter fw=new FileWriter(auditFile);    
				fw.write(auditFileContent);    
				fw.close();
				System.out.println("Audit has been saved to "+auditFile.getAbsolutePath()); 
			}
        }catch(Exception e) {
			e.printStackTrace(); 
			System.out.println("Run election failed. Please check your file.");        	
        }
        
		try {
			Thread t2 = new Thread(new RunStatistics(statistics, "getWinner"));
			t2.start();
			System.out.println("Begin generating winner.");     
		}catch(Exception e) {
			
			e.printStackTrace(); 
			System.out.println("Generate winner failed. Please check your file.");
		}
		
		try {
			Thread t3_p = new Thread(new RunStatistics(statistics, "generateAuditFile"));
			t3_p.start();
			System.out.println("Begin generating audit file.");  
		}catch(Exception e) {
			
			e.printStackTrace();    
			System.out.println("Generate audit file failed. Please check the message returned.");
		}
		
		try {
			Thread t3 = new Thread(new RunStatistics(statistics, "shareToMedia"));
			t3.start();
			System.out.println("Begin sharing to media.");  
		}catch(Exception e) {
			
			e.printStackTrace();    
			System.out.println("Share to media failed. Please check the message returned.");
		}
		Thread.sleep(1000*5);
		System.out.println("Ctrl-C to exit, or the program will close in 5 minutes automatically.");
		Thread.sleep(1000*300);
        System.exit(0);
    	}
    }
    
