package voting_system;

import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;

import voting_system.*;

public class AuditTest {


    private static List<Party> partyList;
    private static HashMap<Party, Integer> partyCount;
    private static HashMap<Candidate, Integer> candidateCount;
    private static String format;
    private static Integer seatNumber;
    private static Integer currentBallots;
    private static LocalDateTime localTime;


    @BeforeClass
    public static void setUp() {
        format = "OPL";
        seatNumber = 2;
        currentBallots = 10;
        localTime = LocalDateTime.now();
        Map<String, List<String>> parties = new HashMap<String, List<String>>() {{
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

        partyCount = new HashMap<>();
        candidateCount = new HashMap<>();
        for (Party party : partyList) {
            if (party.ID.equals("D")){
                partyCount.put(party, 8);
            } else if (party.ID.equals("R")){
                partyCount.put(party, 2);
            } else{
                partyCount.put(party, 0);
            }

            for (Candidate candidate : party.candidateList) {
                if (candidate.name.equals("Pike")){
                    candidateCount.put(candidate, 8);
                } else if (candidate.name.equals("Deutsch")){
                    candidateCount.put(candidate, 2);
                } else {
                    candidateCount.put(candidate, 0);
                }
            }
        }


    }

    @Test
    public void testGetTime() {
        Audit audit = new Audit(format, seatNumber, partyList, partyCount, candidateCount);
        LocalDateTime testTime = audit.getTime();
        LocalDateTime timeNow = LocalDateTime.now();
        assertTrue(testTime.isBefore(timeNow) || testTime.isEqual(timeNow));
    }

    @Test
    public void testGenerateSeats() {
        Audit audit = new Audit(format, seatNumber, partyList, partyCount, candidateCount);
        LinkedHashMap<Party, Integer> partySeatCount = audit.generateSeats();
        int max = Collections.max(partySeatCount.values());
        assertTrue(2 == max);
    }

    @Test
    public void testGenerateWinners() {
        Audit audit = new Audit(format, seatNumber, partyList, partyCount, candidateCount);
        Party winner = audit.generateWinners();
        String testWinner = "D";
        assertTrue(testWinner.equals(winner.ID));
    }

    public void testDisplayScreen() {
        Audit audit = new Audit(format, seatNumber, partyList, partyCount, candidateCount);
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        audit.displayScreen();
        System.setOut(oldOut);
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("Current Winner is D"));
    }

}
