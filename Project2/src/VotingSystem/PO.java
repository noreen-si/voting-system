package VotingSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/** PO Election that can read in data from ballot file(s), execute the PO election algorithm, and output the results, all while writing to an audit file
 * @author Jonathan Haak
 * @author Pyrenees Gavois
*/
public class PO extends Election {
    private String[] Candidates;
    private String[] Winners;
    private String Winner;
    private int id;
    private int[] votes;
    private int voteTotal;
    private BufferedReader br;
    private int totalElectionVotes;
    private int totalCandidates;
    private int filesRead;

    /**
    * This is the constructor for PO.
    * @param input An ArrayList of FileReader objects that represent the election input files to process
    * @param audit A FileWriter that represents the audit file to write to
    * @param br Is the BufferReader that this class will use to parse the input file
    */
    public PO(ArrayList<FileReader> input, FileWriter audit, BufferedReader br){
        super.input = input;
        super.audit = audit;
        this.br = br;
        totalElectionVotes = 0;
        totalCandidates = 0;
        voteTotal = 0;
    }

    /**
     * Getter method for the total votes of the PO election, primarily used for testing
     *
     * @return The int representing the total amount of votes
     */
    public int getVoteTotal() {
        return totalElectionVotes;
    }

    /**
     * Getter method for the total votes for each candidate of the PO election, primarily used for testing
     *
     * @return The int array representing the total amount of votes for each party
     */
    public int[] getVotes() {
        return votes;
    }

    /**
     * Getter method for the candidates of the PO election, primarily used for testing
     *
     * @return The string array representing the the candiates and their parties
     */
    public String[] getCandidates() {
        return Candidates;
    }

    /**
     * Getter method for the winners of the PO election, primarily used for testing (tie case)
     *
     * @return The string array representing the the candiates and their parties who won
     */
    public String[] getWinners() {
        return Winners;
    }


    /**
     * Getter method for the winner of the PO election, primarily used for testing (non-tie case)
     *
     * @return The string array representing the the candiate and his/her party
     */
    public String getWinner() {
        return Winner;
    }


    /**
    * Processes the input file to count votes for each party and writes the record to the audit file
    */
    public void processFile(){
        filesRead = 0;
        String [] nextBallot;
        String ballotString;
        votes = new int[totalCandidates];
        for(int j = 0; j < input.size(); j++ ) {
            filesRead += 1;
            try {
                while ((ballotString = br.readLine()) != null) {
                    nextBallot = ballotString.split(",", -1);
                    for(int k = 0; k < nextBallot.length; k++) {
                        if(nextBallot[k].equalsIgnoreCase("1")) {
                            votes[k] = votes[k] + 1;
                            voteTotal = voteTotal  + 1;
                            String[] announcement = {"Ballot " + ballotString + " increments vote count of the '" + Candidates[k]};
                            writeToAudit(announcement);
                            break;
                        }
                    }
                }
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
            if(j < input.size() - 1){
                try {
                    br.close();
                    br = new BufferedReader(input.get(j + 1));
                    int x;
                    for(int k = 0; k < 4; k++){
                        if(k == 3){
                            this.totalElectionVotes += Integer.parseInt(br.readLine());
                        }
                        else{
                            br.readLine();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Conducts main algorithm for PO election, determines the winner and writes the record to the audit file
     */
    public void conductAlgorithm(){
        boolean[] maxIndexes = new boolean[5];
        int curMax = 0;
        int valMax = votes[0];
        int winnerCount = 1;
        for(int i = 1; i < votes.length; i++) {
            if(valMax < votes[i]) {
                curMax = i;
                valMax = votes[i];
            } else if (valMax == votes[i]){
                maxIndexes[curMax] = true;
                curMax = i;
                valMax = votes[i];
                winnerCount++;
            }
        }
        maxIndexes[curMax] = true;

        Winners =  new String[winnerCount];
        ArrayList<Integer> Tie = new ArrayList<Integer>();

        int indexWinners = 0;
        for(int i = 0; i < maxIndexes.length; i++) {
            if(maxIndexes[i]) {
                Winners[indexWinners] = Candidates[i];
                Tie.add(indexWinners);
                indexWinners++;
            }
        }

        if(Winners.length > 1) {
            for(int i = 0; i < Winners.length; i++) {
                String[] announcement = {"Breaking a tie between the following candidates and associated party: " + Winners[i]};
                writeToAudit(announcement);
            }
        }

        Winner = Winners[((breakTie(Tie, 1))[0])];

        String[] announcement = {Winner + " has been deemed the winner of this election"};
        writeToAudit(announcement);

        for(int i = 0; i < totalCandidates; i++) {
            if(Winner == Candidates[i]) {
                id = i;
            }
        }

        try {
            if (this.audit != null) {
                audit.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the header of the input file and creates the parties and candidates internally, as well as stores other auxiliary information
     * like the number of votes and number of seats in the election
     */
    public void parseHeader() {

        String nextRecord = "";
        for (int i = 0; i < 3; i++) {
            try {
                nextRecord = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(i == 0){
                totalCandidates = Integer.parseInt(nextRecord);
            }
            else if (i == 1){
                Candidates = nextRecord.split("], ", -1);
            }
            else{
                totalElectionVotes = Integer.parseInt(nextRecord);
            }
        }
        String[] announcement1 = {"Header has been read: There are " + totalCandidates + " candidate(s) and " + totalElectionVotes + " total vote(s)"};
        writeToAudit(announcement1);

        for(int i = 0; i < Candidates.length - 1; i++) {
            Candidates[i] = Candidates[i] + "]";
            String[] announcement2 = {"Storing candidate and party " + Candidates[i] + " internally"};
            writeToAudit(announcement2);
        }
        String[] announcement3 = {"Storing candidate and party " + Candidates[Candidates.length - 1] + " internally"};
        writeToAudit(announcement3);

    }


    /**
     * Function that prints results of the PO election to the terminal. Will first show all the parties and how many seats they've won,
     * then will show who exactly (candidate names) have won a seat and their associated party.
     */
    public void printResults(){
        System.out.println("-------------------------   ELECTION RESULTS   -------------------------");
        System.out.println(Winners[0] + " has won the election!");
        int percent = (100 * votes[id]) / voteTotal;
        System.out.println("They received " + votes[id] + " votes, and " + percent + "% of the total votes.");
        System.out.println("Here are the losers!");
        for(int i = 0; i < Candidates.length; i++) {
            if(i != id) {
                int percent2 = (100 * votes[i]) / voteTotal;
                System.out.println(Candidates[i] + " received " + votes[i] + " votes, and " + percent2 + "% of the total votes.");
            }
        }
        System.out.println("-----------------------------------------------------------------------\n\n");
    }

    @Override
    public String toString() {
        String returnVal = "";
        returnVal += "Total Votes in election: " + this.voteTotal + "\n";
        returnVal += "Total files used: " + this.filesRead + "\n";
        return returnVal;
    }
}