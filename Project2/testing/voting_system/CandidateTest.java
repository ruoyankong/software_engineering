package voting_system;

import org.junit.Test;

import voting_system.Candidate;

import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CandidateTest {

	String partyID = "A";
	public String name = "John";
	int ranks = 1;
   
   @Test
   public void testCandidate() {	
      System.out.println("test candidate");
      Candidate cand = new Candidate(partyID, name, ranks);
      assertEquals(cand.partyID, "A");
   }
}
