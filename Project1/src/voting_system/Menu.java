/*******************************************************************
 *  Menu
 *  
 * Menu is the command line UI that interacts with the user.
 * run by java Menu
 * The available commands are
 * setup -- setup an election's format
 * runElection -- start running an election
 * generateAuditFile -- generate the audit file
 * getWinner -- get the winner
 * shareToMedia -- share the audit file to media
 * exit -- exit the menu
 * 
 * Author: ruoyan
*******************************************************************/
package voting_system;
import java.io.*;
import java.util.Scanner;

public class Menu {
	Scanner in;
	Election election;
	Statistics statistics;
	public int setup_flag;
	public int run_flag;
	
	public Menu() {
    	this.in = new Scanner(System.in);
    	this.election = new Election();
    	this.statistics = null;
    	System.out.println("Welcome to Voting System!");
    	this.setup_flag = 0;
    	this.run_flag = 0;		
	}
	
	/**
	 * @param s: String , the command
	 * @return: String, the result of the execution
	 */
	public String deal_with_command (String s) {
		// deal with the command
		if (s.equals("exit")) {
			System.out.println("Exit the VS.");
			return "break";
		}else if (s.equals("setup")) {
			try {
    			System.out.println("Please input your file path, press enter to finish:");
    			String file_path = in.nextLine();
    			boolean result = election.setup(file_path);
    			if (result) {
	    			// setup statistics class
	    			statistics = election.statistics;
	    			setup_flag = 1;	 
	    			return "Successly setup.";
   				
    			}else {
    				return "Setup election failed. Please check your file_path.";
    			}

			}catch(Exception e) {
				e.printStackTrace();
				return "Setup election failed. Please check your file_path.";				
			}
		}else if (s.equals("runElection")) {
			if (setup_flag == 0) {
				return "Please setup your election first.";
			}else {
    			try {
    				// start a new thread to run the election
	    			Thread t = new Thread(new RunElection(election));
	    			t.start();
	    			run_flag = 1;
	    			return "Begin running election."; 	    			
    			}catch(Exception e) {
    				
    				e.printStackTrace();
    				return "Run election failed. Please check your file format.";
    			}
			}
		}else if (s.equals("generateAuditFile")) {
			if (run_flag == 0) {
				return "Please run your election first.";
			}else {
    			try {
	    			Thread t = new Thread(new RunStatistics(statistics, "generateAuditFile"));
	    			t.start();
	    			return "Begin generating audit file."; 
    			}catch(Exception e) {
    				
    				e.printStackTrace();
    				return "Generate audit file failed. Please check the message returned.";
    			}
			}
		}else if (s.equals("getWinner")) {
			if (run_flag == 0) {
				return "Please run your election first.";
			}else {
    			try {
	    			Thread t = new Thread(new RunStatistics(statistics, "getWinner"));
	    			t.start();
	    			return "Begin generating winner.";     
    			}catch(Exception e) {
    				
    				e.printStackTrace(); 
    				return "Generate winner failed. Please check the message returned.";
    			}
			}
		}else if (s.equals("shareToMedia")) {
			if (run_flag == 0) {
				return "Please run your election first.";
			}else {
    			try {
	    			Thread t = new Thread(new RunStatistics(statistics, "shareToMedia"));
	    			t.start();
	    			return "Begin sharing to media.";  
    			}catch(Exception e) {
    				
    				e.printStackTrace();    
    				return "Share to media failed. Please check the message returned.";
    			}
			}
		}else {
			return "Command "+s+" not available.";
		}
	}
	
	/**
	 * start voting system.
	 */
	public void serve() {
    	while(true) {
    		// print the menu
    		System.out.println();
    		System.out.println("-----Menu-----");
    		System.out.println("setup");
    		System.out.println("runElection");
    		System.out.println("generateAuditFile");
    		System.out.println("getWinner");
    		System.out.println("shareToMedia");
    		System.out.println("exit");
    		
    		// read menu
    		String s = in.nextLine();
    		String res = this.deal_with_command(s);
    		if (res.contentEquals("break")) {
    			break;
    		}else {
    			System.out.println(res);
    		}
    	}    	
		
	}
		
	
    public static void main(String [] args) {
    	Menu menu = new Menu();
    	menu.serve();
    }

}

/**
 * A class to start the RunElection process
 * 
 * @author ruoyan
 */
class RunElection implements Runnable {
   Election election;
   public RunElection(Election e){
       this.election = e;
   }

   public void run() {
	   this.election.runElection();
   }
}

/**
 * A class to start the RunStatistics process
 * 
 * @author ruoyan
 */
class RunStatistics implements Runnable {
	Statistics statistics;
	String command;
	public RunStatistics(Statistics s, String c) {
		this.statistics = s;
		this.command = c;
	}
	
	public void run() {
		if (this.command.equals("generateAuditFile")){
			this.statistics.generateAuditFile();
		}else if (this.command.equals("getWinner")){
			this.statistics.generateWinner();
		}else if (this.command.equals("shareToMedia")){
			this.statistics.shareToMedia();
		}
		
	}
}

