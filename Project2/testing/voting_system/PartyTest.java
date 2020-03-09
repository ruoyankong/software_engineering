package voting_system;

import org.junit.Test;

import voting_system.Candidate;
import voting_system.Party;

import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class PartyTest {
	Candidate cand1 = new Candidate("A", "John", 1);
	Candidate cand2 = new Candidate("A", "Mac", 2);
	public CopyOnWriteArrayList<Candidate> candidateList = new CopyOnWriteArrayList<Candidate>();
	public String ID = "A";
	
	
   
   @Test
   public void testCandidate() {	
      System.out.println("test party");
      candidateList.add(cand2);
      candidateList.add(cand1);
      Party party = new Party(candidateList, ID);
      assertEquals(party.candidateList.get(0).name, "Mac");
   }
}
