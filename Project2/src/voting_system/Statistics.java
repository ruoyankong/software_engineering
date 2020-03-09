/*******************************************************************
*  Statistics
*
* Statistics is responsible for counting ballots for parties and candidates,
* generating winner, and sharing to media
*
* Author: Jingfan Guo
*******************************************************************/
package voting_system;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;

public class Statistics {
    private String format;
    private List<Party> partyList;
    private Integer seatNumber;
    private Integer ballotNumber;
    private HashMap<Party, Integer> partyCount;
    private HashMap<Candidate, Integer> candidateCount;
    private HashMap<Candidate, Party> partyForCandidate;
    private HashMap<LocalDateTime, Audit> auditList;

    /**
     * Construct a new Statistics object.
     * 
     * @param   format the election format
     * @param   partyList the complete list of parties
     * @param   seatNumber the total number of seats
     * @param   ballotNumber the total number of ballots
     * @author  Jingfan Guo
     */
    public Statistics(String format, List<Party> partyList, Integer seatNumber, Integer ballotNumber) {
        this.format = format;
        this.partyList = partyList;
        this.seatNumber = seatNumber;
        this.ballotNumber = ballotNumber;
        this.partyCount = new HashMap<>();
        this.candidateCount = new HashMap<>();
        this.partyForCandidate = new HashMap<>();
        for (Party party : partyList) {
            this.partyCount.put(party, 0);
            for (Candidate candidate : party.candidateList) {
                this.candidateCount.put(candidate, 0);
                this.partyForCandidate.put(candidate, party);
            }
        }
        this.auditList = new HashMap<>();
    }

    /**
     * Cast a ballot for a party (CPL).
     * 
     * @param   party   the party that receives the ballot
     * @author  Jingfan Guo
     */
    public void voteForParty(Party party) {
        this.partyCount.replace(party, this.partyCount.get(party) + 1);
    }

    /**
     * Cast a ballot for a candidate (OPL).
     * 
     * @param   candidate   the candidate that receives the ballot
     * @author  Jingfan Guo
     */
    public void voteForCandidate(Candidate candidate) {
        this.candidateCount.replace(candidate, this.candidateCount.get(candidate) + 1);
        Party party = this.partyForCandidate.get(candidate);
        this.partyCount.replace(party, this.partyCount.get(party) + 1);
    }

    /**
     * Generate an audit file for the current statistics.
     *
     * @return  the generated {@code Audit} object
     * @author  Jingfan Guo
     */
    public Audit generateAuditFile() {
        HashMap<Party, Integer> partyCountSnapshot = this.deepCopyHashMap(this.partyCount);
        HashMap<Candidate, Integer> candidateCountSnapshot = this.deepCopyHashMap(this.candidateCount);
        if (this.format.equals("CPL")) {
            for (Map.Entry<Party, Integer> entry : partyCountSnapshot.entrySet()) {
                for (Candidate c: entry.getKey().candidateList) {
                    candidateCountSnapshot.put(c, entry.getValue());
                }
            }
        }
        Audit audit = new Audit(this.format, this.seatNumber, partyList, partyCountSnapshot, candidateCountSnapshot);
        audit.displayScreen();
        Integer currentBallots = 0;
        for (Integer f : candidateCountSnapshot.values()) {
            currentBallots += f;
        }
        if (currentBallots > this.seatNumber) {
            this.auditList.put(audit.getTime(), audit);
        }
        return audit;
    }

    /**
     * Generate the winner for the election.
     *
     * @return  the name of the winner
     * @author  Jingfan Guo
     */
    public Party generateWinner() {
        HashMap<Party, Integer> partyCountSnapshot = this.deepCopyHashMap(this.partyCount);
        HashMap<Candidate, Integer> candidateCountSnapshot = this.deepCopyHashMap(this.candidateCount);
        if (this.format.equals("CPL")) {
            for (Map.Entry<Party, Integer> entry : partyCountSnapshot.entrySet()) {
                for (Candidate c: entry.getKey().candidateList) {
                    candidateCountSnapshot.put(c, entry.getValue());
                }
            }
        }
        Audit audit = new Audit(this.format, this.seatNumber, partyList, partyCountSnapshot, candidateCountSnapshot);
        Party winner = audit.getWinner();
        System.out.println("The winner is " + winner.ID);
        return winner;
    }

    /**
     * Share the election result to media.
     *
     * @author  Jingfan Guo
     */
    public void shareToMedia() {
    	
        if (this.auditList.size() > 0) {
            List<LocalDateTime> auditTimeList = new ArrayList<LocalDateTime>(this.auditList.keySet());
            Collections.sort(auditTimeList);
            LocalDateTime recentAuditTime = auditTimeList.get(auditTimeList.size() - 1);
            Audit recentAudit = this.auditList.get(recentAuditTime);
            
            recentAudit.displayScreen();
        }
    }

    /**
     * Getter method for partyCount;
     * 
     * @return  partyCount
     */
    public HashMap<Party, Integer> getPartyCount() {
        return this.partyCount;
    }

    /**
     * Getter method for candidateCount;
     * 
     * @return  candidateCount
     */
    public HashMap<Candidate, Integer> getCandidateCount() {
        return this.candidateCount;
    }

    /**
     * Getter method for auditList;
     * 
     * @return  auditList
     */
    public HashMap<LocalDateTime, Audit> getAuditList() {
        return this.auditList;
    }

    /**
     * Deep copy a HashMap.
     * 
     * @param   original   the HashMap to copy from
     * @return  a deep copy of the original HashMap
     * @author  Jingfan Guo
     */
    private <K, V> HashMap<K, V> deepCopyHashMap(HashMap<K, V> original) {
        HashMap<K, V> copy = new HashMap<>();
        for (Map.Entry<K, V> entry : original.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    /**
     * Sort a HashMap by values in descending order.
     * 
     * @param   hashMap   the original HashMap to be sorted
     * @return  a LinkedHashMap sorted by values in descending order
     * @author  Jingfan Guo
     */
    private <K> LinkedHashMap<K, Integer> sortHashMapByValue(HashMap<K, Integer> hashMap) {
        List<Map.Entry<K, Integer>> list = new ArrayList<Map.Entry<K, Integer>>(hashMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, Integer>>() {
            public int compare(Map.Entry<K, Integer> o1, Map.Entry<K, Integer> o2) { 
                return - o1.getValue().compareTo(o2.getValue());
            }
        });
        LinkedHashMap<K, Integer> sortedHashMap = new LinkedHashMap<>();
        for (Map.Entry<K, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    } 
}
