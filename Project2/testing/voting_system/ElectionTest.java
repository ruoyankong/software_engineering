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

public class ElectionTest {
    Election election;
    String oplFile = "./testing/sampleBallot1.csv";
    String cplFile = "./testing/sampleBallot2.csv";

    @Before
    public void initElection() {
        election = new Election();
    }

    @Test
    public void testGetFormat() {
        String format = election.getFormat();
        assertEquals("", format);
    }

    @Test
    public void testGetFilePath() {
        String filePath = election.getFilePath();
        assertEquals("", filePath);
    }

    @Test
    public void testGetSeatNumber() {
        Integer seatNumber = election.getSeatNumber();
        assertEquals(null, seatNumber);
    }

    @Test
    public void testGetBallotNumber() {
        Integer ballotNumber = election.getBallotNumber();
        assertEquals(null, ballotNumber);
    }

    @Test
    public void testGetCandidateNumber() {
        Integer candidateNumber = election.getCandidateNumber();
        assertEquals(null, candidateNumber);
    }

    @Test
    public void testGetPartyNumber() {
        Integer partyNumber = election.getPartyNumber();
        assertEquals(null, partyNumber);
    }

    @Test
    public void testGetTotalCandidateList() {
        CopyOnWriteArrayList<Candidate> totalCandidateList = election.getTotalCandidateList();
        assertEquals(totalCandidateList.size(), 0);
    }

    @Test
    public void testGetPartyList() {
        CopyOnWriteArrayList<Party> partyList = election.getPartyList();
        assertEquals(partyList.size(), 0);
    }

    @Test
    public void testGetStatistics() {
        Statistics statistics = election.getStatistics();
        assertEquals(true, statistics.getPartyCount().isEmpty());
    }
    
    @Test
    public void testOPLSetup() {
        Integer seatNumberExpected = 3;
        Integer ballotNumberExpected = 9;
        Integer candNumberExpected = 6;
        election.setup(oplFile);
        assertEquals("OPL", election.getFormat());
        assertEquals(oplFile, election.getFilePath());
        assertEquals(seatNumberExpected, election.getSeatNumber());
        assertEquals(ballotNumberExpected, election.getBallotNumber());
        assertEquals(candNumberExpected, election.getCandidateNumber());
        assertEquals(null, election.getPartyNumber());
        assertEquals(6, election.getTotalCandidateList().size());
        assertEquals(3, election.getPartyList().size());
    }

    @Test
    public void testCPLSetup() {
        Integer seatNumberExpected = 7;
        Integer ballotNumberExpected = 26;
        Integer candNumberExpected = 16;
        Integer partyNumberExpected = 4;
        election.setup(cplFile);
        assertEquals("CPL", election.getFormat());
        assertEquals(cplFile, election.getFilePath());
        assertEquals(seatNumberExpected, election.getSeatNumber());
        assertEquals(ballotNumberExpected, election.getBallotNumber());
        assertEquals(candNumberExpected, election.getCandidateNumber());
        assertEquals(partyNumberExpected, election.getPartyNumber());
        assertEquals(16, election.getTotalCandidateList().size());
        assertEquals(4, election.getPartyList().size());
    }

    @Test
    public void testRunOPLElection() {
    	System.out.println("Test whether OPL finished in 5 minutes with 500000 Ballots.");
    	long startTime = System.currentTimeMillis();
        election.setup("./testing/oplBallot_500000.csv");
        election.runElection();
        double time_spent = (System.currentTimeMillis() -startTime)/1000.0;
        assertTrue("runElection (OPL) of  should be finished in 5 minutes: " +time_spent+ " seconds" , 300>time_spent);
        
        // assertEquals(election.getCandidateNumber(), election.getTotalCandidateList().size());
    }
    

    @Test
    public void testRunCPLElection() {
    	System.out.println("Test whether CPL finished in 5 minutes with 500000 Ballots.");
    	long startTime = System.currentTimeMillis();
        election.setup("./testing/cplBallot_500000.csv");
        election.runElection();
        double time_spent = (System.currentTimeMillis() -startTime)/1000.0;
        assertTrue("runElection (CPL) of  should be finished in 5 minutes: " +time_spent+ " seconds" , 300>time_spent);
        
        // assertEquals(election.getPartyNumber(), election.getPartyList().size());
    }
}