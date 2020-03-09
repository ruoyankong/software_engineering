package voting_system;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import voting_system.*;

public class StatisticsTest {

    private static List<Party> partyList;
    private static Integer seatNumber;
    private static Integer ballotNumber;

    @BeforeClass
    public static void setup() {
        seatNumber = 7;
        ballotNumber = 100;
        Map<String, List<String>> parties = new HashMap<>() {{
            put("D", Arrays.asList("Pike", "Foster", "Floyd", "Jones", "Mallory"));
            put("R", Arrays.asList("Deutsch", "Wong", "Walters", "Keller", "Borg"));
            put("G", Arrays.asList("Jones", "Smith", "Lewis", "Smith", "Li"));
            put("I", Arrays.asList("Perez"));
        }};
        partyList = new CopyOnWriteArrayList<>();
        for (Map.Entry<String, List<String>> entry : parties.entrySet()) {
            CopyOnWriteArrayList<Candidate> candidateList = new CopyOnWriteArrayList<>();
            for (int i = 0; i < entry.getValue().size(); ++i) {
                candidateList.add( new Candidate(entry.getKey(), entry.getValue().get(i), i+1) );
            }
            partyList.add( new Party(candidateList, entry.getKey()) );
        }
    }
 

    @Test
    public void testVoteForParty() {
        String format = "CPL";        
        Statistics statistics = new Statistics(format, partyList, seatNumber, ballotNumber);

        for (int i = 0; i < ballotNumber; ++i) {
            int idx = (int) (Math.random() * partyList.size());
            Party p = partyList.get(idx);
            int countBefore = statistics.getPartyCount().get(p);
            statistics.voteForParty(p);
            assertEquals((int) statistics.getPartyCount().get(p), countBefore + 1);
        }
    }

    @Test
    public void testVoteForCandidate() {
        String format = "OPL";
        Statistics statistics = new Statistics(format, partyList, seatNumber, ballotNumber);
        CopyOnWriteArrayList<Candidate> candidateList = new CopyOnWriteArrayList<>();
        for (Party p : partyList) {
            candidateList.addAll(p.candidateList);
        }

        for (int i = 0; i < ballotNumber; ++i) {
            int idx = (int) (Math.random() * candidateList.size());
            Candidate c = candidateList.get(idx);
            int countBefore = statistics.getCandidateCount().get(c);
            statistics.voteForCandidate(c);
            assertEquals((int) statistics.getCandidateCount().get(c), countBefore + 1);
        }
    }

    @Test
    public void testGenerateAuditFile() {
        String format = "CPL";        
        Statistics statistics = new Statistics(format, partyList, seatNumber, ballotNumber);
        for (int i = 0; i < ballotNumber; ++i) {
            int idx = (int) (Math.random() * partyList.size());
            Party p = partyList.get(idx);
            statistics.voteForParty(p);
        }

        Audit audit = statistics.generateAuditFile();
        assertEquals(audit, statistics.getAuditList().get(audit.getTime()));
    }

    @Test
    public void testGenerateWinner() {
        String format = "CPL";        
        Statistics statistics = new Statistics(format, partyList, seatNumber, ballotNumber);
        int idx = (int) (Math.random() * partyList.size());
        Party p = partyList.get(idx);
        for (int i = 0; i < ballotNumber; ++i) {
            statistics.voteForParty(p);
        }
        Party winner = statistics.generateWinner();
        assertEquals(winner, p);
    }

    @Test
    public void testShareToMedia() {
        String format = "CPL";        
        Statistics statistics = new Statistics(format, partyList, seatNumber, ballotNumber);
        for (int i = 0; i < ballotNumber; ++i) {
            int idx = (int) (Math.random() * partyList.size());
            Party p = partyList.get(idx);
            statistics.voteForParty(p);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        statistics.generateAuditFile();
        System.out.flush();
        String s1 = baos.toString();

        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        System.setOut(ps);
        statistics.shareToMedia();
        System.out.flush();
        String s2 = baos.toString();
        System.setOut(old);
        assertEquals(s1, s2);
    }
}