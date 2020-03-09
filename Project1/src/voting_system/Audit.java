/*******************************************************************
*  Audit
*
* Audit is responsible for generating election information at the
* time, such as Type of Voting, Number of Candidates, Candidates,
* Number of Ballots, calculations, how many votes a candidate or
* party had, etc.
*
* Author: Yuan Yao
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

public class Audit {
    private String format;
    private Integer seatNumber;
    private List<Party> partyList;
    private HashMap<Party, Integer> partyCount;
    private HashMap<Candidate, Integer> candidateCount;
    private LocalDateTime localTime;
    private Integer currentBallots;
    private Party winner;


    /**
     * Construct a new Audit object.
     *
     * @param   format the election format
     * @param   seatNumber the total number of seats
     * @param   partyList the complete list of parties
     * @param   partyCountSnapshot current party count list
     * @param   candidateCountSnapshot current candidate count list
     * @author  Yuan Yao
     */
    public Audit(String format, Integer seatNumber, List<Party> partyList, HashMap<Party, Integer> partyCountSnapshot, HashMap<Candidate, Integer> candidateCountSnapshot) {
        this.partyList = partyList;
        this.format = format;
        this.seatNumber = seatNumber;
        this.partyCount = partyCountSnapshot;
        this.candidateCount = candidateCountSnapshot;
        this.localTime = LocalDateTime.now();
        this.currentBallots = 0;
        for (Integer f : partyCountSnapshot.values()) {
          this.currentBallots += f;
        }
        this.winner = generateWinners();
    }

    /**
     * Getter method for the timestamp
     *
     * @return  timestamp
     * @author  Yuan Yao
     */
    public LocalDateTime getTime() {
        return this.localTime;
    }

    /**
     * Getter method for the winner
     *
     * @return  timestamp
     * @author  Yuan Yao
     */
    public Party getWinner() {
        return this.winner;
    }

    /**
     * Generate current seats.
     *
     * @return  seats for each party
     * @author  Yuan Yao
     */
    public LinkedHashMap<Party, Integer> generateSeats() {
        Integer quota = this.currentBallots / seatNumber;
        //----------------------------------------------------------------------
        // Largest Remainder Approach to Seat Allocation
        //----------------------------------------------------------------------
        LinkedHashMap<Party, Integer> sortedPartyCount = sortHashMapByValue(this.partyCount);
        // First allocation
        LinkedHashMap<Party, Integer> partySeatCount = new LinkedHashMap<>();
        HashMap<Party, Integer> partyRemainder = new HashMap<>();
        Integer seatsRemaining = seatNumber;
        for (Map.Entry<Party, Integer> entry : sortedPartyCount.entrySet()) {
            Integer seatsReceived = entry.getValue() / quota;
            partySeatCount.put(entry.getKey(), seatsReceived);
            partyRemainder.put(entry.getKey(), entry.getValue() % quota);
            seatsRemaining -= seatsReceived;
        }
        LinkedHashMap<Party, Integer> sortedPartyRemainder = sortHashMapByValue(partyRemainder);
        // Second allocation
        for (Map.Entry<Party, Integer> entry : sortedPartyRemainder.entrySet()) {
            if (seatsRemaining <= 0) {
                break;
            }
            partySeatCount.replace(entry.getKey(), partySeatCount.get(entry.getKey()) + 1);
            seatsRemaining -= 1;
        }
        return partySeatCount;
    }

    /**
     * Generate the current winner for the election.
     *
     * @return  the name of the current winner
     * @author  Yuan Yao
     */
    public Party generateWinners() {
        Party winner;
        LinkedHashMap<Party, Integer> partySeatCount = generateSeats();
        List<Map.Entry<Party, Integer>> partySeatCountList = new ArrayList<Map.Entry<Party, Integer>>(partySeatCount.entrySet());
        Integer numberForTie = 1;
        for ( ; numberForTie < partySeatCountList.size(); ++numberForTie) {
            if (partySeatCountList.get(numberForTie).getValue() < partySeatCountList.get(0).getValue()) {
                break;
            }
        }
        if (numberForTie == 1) {
            winner = partySeatCountList.get(0).getKey();
        }
        else {
            Integer randomIndex = (int) (Math.random() * numberForTie);
            winner = partySeatCountList.get(randomIndex).getKey();
        }
        //----------------------------------------------------------------------
        // (CPL only) Rank candidates
        //----------------------------------------------------------------------
        if (this.format.equals("CPL")) {
            for(Party party : this.partyList) {
                HashMap<Candidate, Integer> tempCandidateCount = new HashMap<>();
                for (Candidate candidate : party.candidateList) {
                    tempCandidateCount.put(candidate, this.candidateCount.get(candidate));
                }
                LinkedHashMap<Candidate, Integer> sortedTempCandidateCount = sortHashMapByValue(tempCandidateCount);
                int rank = 1;
                for (Map.Entry<Candidate, Integer> entry : sortedTempCandidateCount.entrySet()) {
                    entry.getKey().ranks = rank;
                    rank++;
                }
            }
        }
        return winner;
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
      /**
       * Display to the screen the winner(s) and election information
       *
       * @author  Yuan Yao
       */
    public void displayScreen() {
        LinkedHashMap<Party, Integer> partySeatCount = generateSeats();
        if (this.format.equals("CPL")) {
          System.out.println("Type of Voting: close");
        } else{
          System.out.println("Type of Voting: open");
        }
        System.out.println("Number of seats is " + seatNumber);
        System.out.println("Number of parties is " + this.partyCount.size());
        System.out.println("Number of Candidates is " + this.candidateCount.size());
        System.out.println("Number of current ballot cast is " + this.currentBallots);
        System.out.println("Current Winner is " + winner.ID);
        System.out.println("Number of votes each candidate receives: ");
        for (Map.Entry<Candidate, Integer> entry : this.candidateCount.entrySet()) {
          System.out.println(entry.getKey().name + " : " + entry.getValue());
        }
        System.out.println("Number of votes each party receives: ");
        for (Map.Entry<Party, Integer> entry : this.partyCount.entrySet()) {
          System.out.println(entry.getKey().ID + " : " + entry.getValue());
        }
        System.out.println("Number of seats each party receives: ");
        for (Map.Entry<Party, Integer> entry : partySeatCount.entrySet()) {
          System.out.println(entry.getKey().ID + " : " + entry.getValue());
        }
    }

  }
