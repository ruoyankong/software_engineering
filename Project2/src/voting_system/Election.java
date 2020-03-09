/*******************************************************************
*  Election
*
* Election is used for reading ballot files
*
*
* Author: Kai Wang
*******************************************************************/
package voting_system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.concurrent.TimeUnit;


public class Election {
    String format;
    String filePath;
    Integer seatNumber;
    Integer ballotNumber;
    Integer candidateNumber;
    Integer partyNumber;
    CopyOnWriteArrayList<Candidate> totalCandidateList;
    CopyOnWriteArrayList<Party> partyList;
    Statistics statistics;
    long runtime;

    /**
     * Construct a new Election object.
     *
     * @author  Kai Wang
     */
    public Election() {
        this.format = new String();
        this.filePath = new String();
        this.seatNumber = null;
        this.ballotNumber = null;
        this.candidateNumber = null;
        this.partyNumber = null;
        this.totalCandidateList = new CopyOnWriteArrayList<Candidate>();
        this.partyList = new CopyOnWriteArrayList<Party>();
        this.statistics = new Statistics(this.format, this.partyList, this.seatNumber, this.ballotNumber);
        this.runtime = 1000000000;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @return the seatNumber
     */
    public Integer getSeatNumber() {
        return seatNumber;
    }

    /**
     * @return the ballotNumber
     */
    public Integer getBallotNumber() {
        return ballotNumber;
    }

    /**
     * @return the candidateNumber
     */
    public Integer getCandidateNumber() {
        return candidateNumber;
    }

    /**
     * @return the partyNumber
     */
    public Integer getPartyNumber() {
        return partyNumber;
    }

    /**
     * @return the totalCandidateList
     */
    public CopyOnWriteArrayList<Candidate> getTotalCandidateList() {
        return totalCandidateList;
    }

    /**
     * @return the partyList
     */
    public CopyOnWriteArrayList<Party> getPartyList() {
        return partyList;
    }

    /**
     * @return the statistics
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Setup election parameters.
     *
     * @param   filePath the file path of ballot file
     * @return  successfully setup
     * @author  Kai Wang
     */
    public boolean setup(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.printf("%s does not exist!\n", filePath);
            return false;
        }
        boolean res = true;
        this.filePath = filePath;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String lineContext = new String();
            // Read the first line, which determines the voting format.
            lineContext = bufferedReader.readLine();
            if (lineContext != null) {
                this.format = lineContext;
            } else {
                System.out.printf("%s is empty.", filePath);
                return false;
            }

            HashMap<String, CopyOnWriteArrayList<Candidate>> candMap = new HashMap<>();

            if (this.format.equals("OPL")) {
                lineContext = bufferedReader.readLine();
                this.seatNumber = Integer.parseInt(lineContext);

                lineContext = bufferedReader.readLine();
                this.ballotNumber = Integer.parseInt(lineContext);

                lineContext = bufferedReader.readLine();
                this.candidateNumber = Integer.parseInt(lineContext);

                for (int i = 0; i < this.candidateNumber; i++) {
                    lineContext = bufferedReader.readLine();
                    lineContext = lineContext.replaceAll("[\\[\\]]", "").trim();
                    String[] candPair = lineContext.split(",");
                    String partyID = candPair[1].trim();
                    String candName = candPair[0].trim();
                    if (!candMap.containsKey(partyID)) {
                        CopyOnWriteArrayList<Candidate> candList = new CopyOnWriteArrayList<Candidate>();
                        Candidate candTemp = new Candidate(partyID, candName, 1);
                        candList.add(candTemp);
                        candMap.put(partyID, candList);
                        this.totalCandidateList.add(candTemp);
                    } else {
                        CopyOnWriteArrayList<Candidate> candList = candMap.get(partyID);
                        Candidate candTemp = new Candidate(partyID, candName, 1);
                        candList.add(candTemp);
                        candMap.replace(partyID, candList);
                        this.totalCandidateList.add(candTemp);
                    }
                }

                for (Map.Entry<String, CopyOnWriteArrayList<Candidate>> entry : candMap.entrySet()) {
                    Party partyTemp = new Party(entry.getValue(), entry.getKey());
                    this.partyList.add(partyTemp);
                }
            }

            if (this.format.equals("CPL")) {
                lineContext = bufferedReader.readLine();
                this.partyNumber = Integer.parseInt(lineContext);

                lineContext = bufferedReader.readLine();
                String[] partyIDs = lineContext.replaceAll("[\\[\\]]", "").split(",");

                lineContext = bufferedReader.readLine();
                this.seatNumber = Integer.parseInt(lineContext);

                lineContext = bufferedReader.readLine();
                this.ballotNumber = Integer.parseInt(lineContext);

                lineContext = bufferedReader.readLine();
                this.candidateNumber = Integer.parseInt(lineContext);

                for (int i = 0; i < this.candidateNumber; i++) {
                    lineContext = bufferedReader.readLine();
                    lineContext = lineContext.replaceAll("[\\[\\]]", "").trim();
                    String[] candPair = lineContext.split(",");
                    String partyID = candPair[1].trim();
                    String candName = candPair[0].trim();
                    int candRank = Integer.parseInt(candPair[2]);
                    if (!candMap.containsKey(partyID)) {
                        CopyOnWriteArrayList<Candidate> candList = new CopyOnWriteArrayList<Candidate>();
                        Candidate candTemp = new Candidate(partyID, candName, candRank);
                        candList.add(candTemp);
                        candMap.put(partyID, candList);
                        this.totalCandidateList.add(candTemp);
                    } else {
                        CopyOnWriteArrayList<Candidate> candList = candMap.get(partyID);
                        Candidate candTemp = new Candidate(partyID, candName, candRank);
                        candList.add(candTemp);
                        candMap.replace(partyID, candList);
                        this.totalCandidateList.add(candTemp);
                    }
                }

                for (String partyID : partyIDs) {
                    Party partyTemp = new Party(candMap.get(partyID), partyID);
                    this.partyList.add(partyTemp);
                }
            }
            this.statistics = new Statistics(this.format, this.partyList, this.seatNumber, this.ballotNumber);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            res =  false;
        } catch (IOException e) {
            e.printStackTrace();
            res = false;
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return res;
    }

    /**
     * count ballots for voting
     *
     * @return  audit text
     * @author  Kai Wang
     */
    public String runElection() {
    	long startTime = System.currentTimeMillis();
        if (this.filePath.equals("")) {
            System.out.println("No Ballot File has be allocated!");
            return "";
        }
        StringBuilder toAudit = new StringBuilder();
        //String toAudit = "";
        File file = new File(this.filePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String lineContext = new String();
            if (this.format.equals("OPL")) {
                assert(this.candidateNumber == this.totalCandidateList.size());
                int fmtLineNumber = 4 + this.candidateNumber;
                for (int i = 0; i < fmtLineNumber; i++) {
                    bufferedReader.readLine();
                }
                // Start reading ballot
                for (int i = 0; i < this.ballotNumber; i++) {
                    lineContext = bufferedReader.readLine();
                    if (lineContext != null) {
                        List<String> ballotList = Arrays.asList(lineContext.split(","));
                        int candInd = ballotList.indexOf("1");
                        this.statistics.voteForCandidate(this.totalCandidateList.get(candInd));
                        toAudit.append("Candidate " + this.totalCandidateList.get(candInd).name  + " got a vote. \n");
                    } else {
                        System.out.println("Ballots are less than the ballot number!");
                        return "";
                    }
                }
            }

            if (this.format.equals("CPL") ) {
                assert(this.partyNumber == this.partyList.size());
                int fmtLineNumber = 6 + this.candidateNumber;
                for (int i = 0; i < fmtLineNumber; i++) {
                    bufferedReader.readLine();
                }
                // Start reading ballot
                for (int i = 0; i < this.ballotNumber; i++) {
                    lineContext = bufferedReader.readLine();
                    if (lineContext != null) {
                        List<String> ballotList = Arrays.asList(lineContext.split(","));
                        int partyInd = ballotList.indexOf("1");
                        this.statistics.voteForParty(this.partyList.get(partyInd));
                        toAudit.append("Party " + this.partyList.get(partyInd).ID  + " got a vote. \n");
                    } else {
                        System.out.println("Ballots are less than the ballot number!");
                        return "";
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        this.runtime = endTime - startTime;
        System.out.println("Election finished in "+this.runtime/1000.0+ " seconds.");
        return toAudit.toString();
    }

    // /**
    //  * show fields for testing
    //  *
    //  * @author  Kai Wang
    //  */
    // public void showFields() {
    //     System.out.printf("format: %s\n", this.format);
    //     System.out.printf("file path: %s\n", this.filePath);
    //     System.out.printf("seat number: %d\n", this.seatNumber);
    //     System.out.printf("ballot number: %d\n", this.ballotNumber);
    //     System.out.printf("candidate number: %d\n", this.candidateNumber);
    //     System.out.printf("partyNumber: %d\n", this.partyNumber);
    //     System.out.println("Candidate list:");
    //     for (Candidate cand : this.totalCandidateList) {
    //         System.out.printf("candidate party %s, name %s, rank %d \n", cand.partyID, cand.name, cand.ranks);
    //     }
    //     System.out.println("Party list:");
    //     for (Party party : this.partyList) {
    //         System.out.printf("party id: %s length %d \n", party.ID, party.candidateList.size());
    //     }
    // }
}
