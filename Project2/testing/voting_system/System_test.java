package voting_system;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Before;
import org.junit.Test;

import voting_system.Election;
public class System_test {
    
    String oplFile = "./testing/sampleBallot1.csv";
    String cplFile = "./testing/sampleBallot2.csv";
    
    @Test
    public void testRunOPLElection() {
    	System.out.println("System Test OPL -setup-run-generateauditfile-getwinner.");
    	long startTime = System.currentTimeMillis();
    	Election election = new Election(); 
        election.setup(oplFile);
        System.out.println("test setup.");
        assertEquals("OPL", election.getFormat());
        assertEquals(oplFile, election.getFilePath());
        System.out.println("test generate audit file.");
        String auditcontent = election.runElection();
        String trueauditcontent = "Candidate Pike got a vote. \n" + 
        		"Candidate Pike got a vote. \n" + 
        		"Candidate Foster got a vote. \n" + 
        		"Candidate Jones got a vote. \n" + 
        		"Candidate Smith got a vote. \n" + 
        		"Candidate Borg got a vote. \n" + 
        		"Candidate Borg got a vote. \n" + 
        		"Candidate Pike got a vote. \n" + 
        		"Candidate Foster got a vote. \n";
        assertEquals(auditcontent, trueauditcontent);
        System.out.println("test generate winner.");
        String ID = election.statistics.generateWinner().ID;
        
        assertEquals(ID, "D");
        
        
        // assertEquals(election.getCandidateNumber(), election.getTotalCandidateList().size());
    }
    

    @Test
    public void testRunCPLElection() {
    	System.out.println("System Test CPL -setup-run-generateauditfile-getwinner.");
    	long startTime = System.currentTimeMillis();
    	Election election = new Election(); 
        election.setup(cplFile);
        System.out.println("test setup.");
        assertEquals("CPL", election.getFormat());
        assertEquals(cplFile, election.getFilePath());
        System.out.println("test generate audit file.");
        String auditcontent = election.runElection();
        String trueauditcontent = "Party D got a vote. \n" + 
        		"Party D got a vote. \n" + 
        		"Party R got a vote. \n" + 
        		"Party R got a vote. \n" + 
        		"Party G got a vote. \n" + 
        		"Party I got a vote. \n" + 
        		"Party G got a vote. \n" + 
        		"Party D got a vote. \n" + 
        		"Party R got a vote. \n" + 
        		"Party G got a vote. \n" + 
        		"Party G got a vote. \n" + 
        		"Party R got a vote. \n" + 
        		"Party R got a vote. \n" + 
        		"Party D got a vote. \n" + 
        		"Party D got a vote. \n" + 
        		"Party R got a vote. \n" + 
        		"Party R got a vote. \n" + 
        		"Party G got a vote. \n" + 
        		"Party I got a vote. \n" + 
        		"Party G got a vote. \n" + 
        		"Party D got a vote. \n" + 
        		"Party R got a vote. \n" + 
        		"Party G got a vote. \n" + 
        		"Party G got a vote. \n" + 
        		"Party R got a vote. \n" + 
        		"Party R got a vote. \n";
        assertEquals(auditcontent, trueauditcontent);
        System.out.println("test generate winner.");
        String ID = election.statistics.generateWinner().ID;
        
        assertEquals(ID, "R");
        
        // assertEquals(election.getPartyNumber(), election.getPartyList().size());
    }
}
