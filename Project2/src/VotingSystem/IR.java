package VotingSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Arrays;

/**
 * This is the IR Class that handles the reading of an input election file and conduction of the IR election
 * The class conducts the IR algorithm to determine the winner of an IR election with provided information
 * @author Noreen Si
 * @author Jonathan Haak
 */
public class IR extends Election{
    private Candidate [] candidates;
    private int numBallots;
    private BufferedReader br;
    private int curNumCandidates;
    private ArrayList<String> exhausted;
    private int transferredThisRound;
    private int exhaustedThisRound;
    private int totalExhausted;
    private ArrayList<String> totals;
    private int totalRounds;
    private int filesRead;

    /**
     * This is the constructor for PO.
     * @param input An ArrayList of FileReader objects that represent the election input files to process
     * @param audit A FileWriter that represents the audit file to write to
     * @param br Is the BufferReader that this class will use to parse the input file
     */
    public IR(ArrayList<FileReader> input, FileWriter audit, BufferedReader br){
        super.input = input;
        super.audit = audit;
        this.br = br;
        this.exhausted = new ArrayList<>();
        this.exhausted.add("EXHAUSTED PILE");
        this.exhausted.add("---");
        this.transferredThisRound = 0;
        this.exhaustedThisRound = 0;
        this.totalExhausted = 0;
        this.totals = new ArrayList<>();
        this.totals.add("TOTALS");
        this.totals.add("---");
        this.totalRounds = 1;
    }

    /**
     * Eliminates a single candidate and handles the distribution of ballots from the eliminated candidate.
     * @param index an int that represents the index of the candidate to eliminate.
     */
    public void eliminateCandidate(int index){
        int i;
        Tree eliminatedTree = this.candidates[index].getBallots();
        String[] eliminatingAnnouncement = {"Commence transferring of to-be eliminated candidate " +
                this.candidates[index].getName() + " votes to other Candidates"};
        writeToAudit(eliminatingAnnouncement);

        // Make a list of eliminated candidates
        ArrayList<Integer> eliminated = new ArrayList<>();
        for (i = 0; i < this.candidates.length; i++) {
            if (this.candidates[i].isEliminated()) {
                eliminated.add(i);
            }
        }
        // For the eliminated candidate tree, reassign its second place spots to the appropriate candidates
        if (eliminatedTree.root.children != null) {
            for (i = 0; i < eliminatedTree.root.children.length; i++) {
                Node secondPlace = eliminatedTree.root.children[i];
                if (secondPlace != null) {
                    ArrayList<ArrayList<Integer>> ballots = eliminatedTree.getBallots(secondPlace);
                    if (eliminated.size() == 0) {
                        reassignVotesNoneEliminated(ballots, i);
                    }
                    else {
                        reassignVotes(eliminated, ballots);
                    }
                }
            }
        }
        String[] eliminatedAnnouncement = {"\nCandidate " + this.candidates[index].getName()
                + " has been eliminated."};
        writeToAudit(eliminatedAnnouncement);
        this.candidates[index].setEliminated(true);
        this.curNumCandidates -= 1;
        // Write this negative vote difference in the eliminated candidate's vote totals
        int numVotes = this.candidates[index].getNumVotes();
        this.candidates[index].getVoteInfoPerRound().add("0");
        this.candidates[index].getVoteInfoPerRound().add("-" + (numVotes));
        // Record the number of votes exhausted this round
        // Which is the difference between the votes of the eliminated candidate and the number actually transferred
        this.exhaustedThisRound = numVotes - this.transferredThisRound;
    }

    /**
     * Handles reassignment of votes when no candidate has been eliminated thus far in the election.
     * @param ballots an ArrayList of ballots to insert.
     * @param index an int that represents the index of the candidate to transfer the votes to.
     */
    public void reassignVotesNoneEliminated(ArrayList<ArrayList<Integer>> ballots, int index) {
        // When there are no eliminated candidates yet, we do not need to mutate the ballot
        // Instead, the ballots may all be directly inserted into the corresponding candidate's tree
        Tree toInsert = this.candidates[index].getBallots();
        // Increment the number of ballots transferred this round
        this.transferredThisRound += ballots.size();
        int i;
        for (i = 0; i < ballots.size(); i++) {
            ArrayList<Integer> curBallot = ballots.get(i);
            String ballotRepresentation = curBallot.toString();
            String[] transferAnnouncement = {"Transferring ballot " + ballotRepresentation +
                    " to candidate " + this.candidates[index].getName()};
            writeToAudit(transferAnnouncement);
            toInsert.insert(curBallot);
            // Increment this candidate's vote difference for this round
            this.candidates[index].incrementVoteDifference();
        }
    }

    /**
     * Handles the reassignment of votes when at least one candidate has been eliminated already.
     * @param eliminated an ArrayList that represents the indices of the previously eliminated candidates.
     * @param ballots an ArrayList that represents all the ballots to transfer.
     */
    public void reassignVotes(ArrayList<Integer> eliminated, ArrayList<ArrayList<Integer>> ballots){
        int i;
        HashMap<Integer, Integer> ballotsMap = new HashMap<>();
        int j;
        int k;
        int curElimCount;
        ArrayList<Integer> curBallot;
        // Loop over the ballots
        for (i = 0; i < ballots.size(); i++) {
            ballotsMap.clear();
            curElimCount = 0;
            curBallot = new ArrayList<Integer>();
            curBallot = ballots.get(i);
            // Make a list of eliminated candidate ranks
            ArrayList<Integer> eliminatedRanks = new ArrayList<>();
            for (k = 0; k < eliminated.size(); k++) {
                if (curBallot.get(eliminated.get(k)) != 0) {
                    eliminatedRanks.add(curBallot.get(eliminated.get(k)));
                }
            }
            // Sort the list such that the eliminated ranks are sorted in increasing order
            eliminatedRanks.sort(null);
            // Fill in the ballotsMap -- Each rank between 1 and the number of candidates in the election
            // is mapped to a value that represents how many eliminated candidates precede them in the race
            for (j = 1; j < this.candidates.length + 1; j++) {
                if (curElimCount < eliminatedRanks.size() && j == eliminatedRanks.get(curElimCount)) {
                    curElimCount += 1;
                } else {
                    ballotsMap.put(j, curElimCount);
                }
            }
            // Mutate the ballot, subtracting by how many eliminated candidates there were
            // before each rank, referring to the ballotsMap to calculate the difference
            // This is to ensure that no ballots with eliminated candidates ranked are inserted
            for (j = 0; j < curBallot.size(); j++) {
                if (this.candidates[j].isEliminated() || curBallot.get(j) <= 0) {
                    curBallot.set(j, 0);
                } else {
                    curBallot.set(j, curBallot.get(j) - ballotsMap.get(curBallot.get(j)));
                }
            }
            // Insert into the Tree of the candidate that corresponds to the new number 1 ranking
            // It's possible to end up with a ballot with all 0s, so only insert if a 1 exists in ballot
            int indexToInsert = curBallot.indexOf(1);
            if (indexToInsert != -1) {
                // Write this transfer of the ballot to the audit file
                String ballotRepresentation = curBallot.toString();
                String[] transferAnnouncement = {"Transferring ballot " + ballotRepresentation +
                        " to candidate " + this.candidates[indexToInsert].getName()};
                writeToAudit(transferAnnouncement);
                Tree toInsert = this.candidates[indexToInsert].getBallots();
                toInsert.insert(curBallot);
                // Increment this candidate's vote gain for this round by one
                this.candidates[indexToInsert].incrementVoteDifference();
                // Increment the number of ballots transferred this round
                this.transferredThisRound += 1;
            }
        }
    }

    /**
     * A method that updates each candidate's round vote information and the exhausted ballots per round.
     * This method is used for gathering and formatting the necessary information for the IR table.
     */
    public void updateVoteDifferenceAfterRound() {
        this.totalExhausted += this.exhaustedThisRound;
        // show the total number of ballots in this election
        if (this.totalRounds == 1) {
            int totalVotes = this.numBallots;
            this.totals.add(Integer.toString(totalVotes));
            this.totals.add("+" + totalVotes);
        }
        // Loop over all the candidates
        // For each candidate, add their current total votes to their VoteInfoPerRound list as a string
        for (int i = 0; i < this.candidates.length; i++) {
            if (this.totalRounds == 1) {
                int numVotes = this.candidates[i].getNumVotes();
                this.candidates[i].getVoteInfoPerRound().add(Integer.toString(numVotes));
                this.candidates[i].getVoteInfoPerRound().add("+" + (numVotes));
            } else if (!this.candidates[i].isEliminated()){
                // Also add their votes gained or lost (candidate.voteDifference) (if eliminated, their loss value is all their current votes)
                this.candidates[i].getVoteInfoPerRound().add(Integer.toString(this.candidates[i].getNumVotes()));
                this.candidates[i].getVoteInfoPerRound().add("+" + (this.candidates[i].getVoteDifference()));
            }
            this.candidates[i].resetVoteDifference();
        }
        // update the exhausted ballots information
        this.exhausted.add(Integer.toString(this.totalExhausted));
        this.exhausted.add(("+" + this.exhaustedThisRound));
        this.transferredThisRound = 0;
        this.exhaustedThisRound = 0;
    }


    /**
     * A method that is called to display the final IR table that has all the tracked votes and exhausted ballots information.
     * A JTable will appear on the user's screen.
     */
    public void exportToTable() {
        // sort candidates
        Arrays.sort(candidates, Comparator.comparingInt(Candidate::getNumVotes).reversed());
        // get arraylists of each candidate's VoteInfoPerRound and put them in a list of lists
        ArrayList<ArrayList<String>> allCandidateInfo = new ArrayList<>();
        for (Candidate candidate : this.candidates) {
            allCandidateInfo.add(candidate.getVoteInfoPerRound());
        }
        // Make a table
        new Table(allCandidateInfo, this.exhausted, this.totals, this.totalRounds);
    }

    /**
     * Determines the candidate with a majority.
     * @return null if no candidate has a majority, or the Candidate object that does.
     */
    public Candidate majorityCandidate(){
        int i;
        int totalVotes = 0;
        int curMaxVotes = Integer.MIN_VALUE;
        Candidate curCandidate = null;
        Candidate highestCandidate = null;
        // Get the total number of votes and the max votes a candidate has to see if there's a majority
        for (i = 0; i < this.candidates.length; i++) {
            curCandidate = candidates[i];
            // Add to the total number of votes only if the candidate is not eliminated
            if (!curCandidate.isEliminated()) {
                totalVotes += curCandidate.getNumVotes();
            }
            // This is finding the new maximum number of votes
            if (curCandidate.getNumVotes() > curMaxVotes) {
                curMaxVotes = curCandidate.getNumVotes();
                highestCandidate = curCandidate;
            }
        }
        // If there is no majority, reset the returned value back to null.
        if (curMaxVotes <= totalVotes / 2) {
            highestCandidate = null;
        }
        return highestCandidate;
    }

    /**
     * Processes the ballots in the election input file and stores them in the candidates' trees accordingly.
     * Each ballot is inserted into the candidate's tree that corresponds with the number one ranking in the ballot.
     */
    public void processFile(){
        filesRead = 0;
        String [] nextBallot;
        String ballotString;
        for(int j = 0; j < input.size(); j++ ) {
            filesRead += 1;
            try {
                while ((ballotString = br.readLine()) != null) {
                    nextBallot = ballotString.split(",", -1);
                    writeToAudit(nextBallot); // Write the ballot to the audit file
                    ArrayList<Integer> ballot = new ArrayList<Integer>(); // Arraylist to hold ballot
                    int specialIndex = -1; // Variable to hold index of candidate with rank 1
                    for (int i = 0; i < nextBallot.length; i++) { // Loop through ballot
                        int curRank = 0; // Set default rank to 0, will be overwritten if not empty
                        if (!nextBallot[i].equals("")) { // If the rank is not empty
                            curRank = Integer.parseInt(nextBallot[i]); // Parse the rank
                            if (curRank == 1) {
                                specialIndex = i;
                            }
                        }
                        ballot.add(curRank); // Add the rank to the ballot
                    }

                    Tree candidateTree = this.candidates[specialIndex].getBallots(); // Get tree of candidate with rank 1
                    candidateTree.insert(ballot); // Insert the ballot into the tree
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
                            this.numBallots += Integer.parseInt(br.readLine());
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
     * Prints the results of the election the screen.
     * This includes information such as the total number of ballots, final vote totals for the candidates,
     * and the winner with the percentage of votes received.
     */
    public void printResults(){
        System.out.println("-------------------------   ELECTION RESULTS   -------------------------");
        System.out.println("Total number of ballots: " + this.numBallots);
        System.out.println("Final Vote Totals after candidate elimination and ballot transfers:");
        Arrays.sort(candidates, Comparator.comparingInt(Candidate::getNumVotes).reversed());
        for(Candidate c : candidates){
            System.out.println(c.getName() + ": " + c.getNumVotes());
        }
        Candidate winner = candidates[0];
        System.out.println("-----------------------------------------------------------------------\n\n");
        System.out.println("The winner is: " + winner.getName() + " with " + winner.getNumVotes() + " votes.");
        System.out.printf("%s received %.2f%% of the votes.\n\n", winner.getName(), (double)winner.getNumVotes() / (double)this.numBallots * 100);



    }

    /**
     * Parses the header of the input and stores election and candidate information.
     */
    public void parseHeader() {
        // Read the first 4 lines, which is the header, of the input file
        int i;
        String nextRecord;
        for (i = 0; i < 3; i++) {
            try {
                nextRecord = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // read the number of candidates
            if (i == 0) {
                int numCandidates = Integer.parseInt(nextRecord);
                this.candidates = new Candidate[numCandidates];
                this.curNumCandidates = numCandidates;
            }
            // create the candidate objects
            else if (i == 1) {
                String[] candidates = nextRecord.split(", ");
                initializeCandidates(candidates);
            }
            // set the numBallots field
            else if (i == 2) {
                this.numBallots = Integer.parseInt(nextRecord);
            }
        }
        String[] headerInformation = {"The header has been read. There are: " +
                this.curNumCandidates + " candidates at the start of the election, " +
                this.numBallots + " number of ballots in this election."};
        writeToAudit(headerInformation);
    }

    /**
     * Initializes the candidates array with initialized Candidate objects.
     * @param candidatesList a String array of candidate names and parties.
     */
    public void initializeCandidates(String[] candidatesList) {
        String[] initializationAnnouncement = {"Commence initialization of candidate information: "};
        writeToAudit(initializationAnnouncement);
        int i;
        for (i = 0; i < candidatesList.length; i++) {
            candidatesList[i] = candidatesList[i].trim();
            // Split the string by spaces, as the first word is the name and the second is the party
            String[] candidateInfo = candidatesList[i].split(" ");
            Tree newTree = new Tree(i, this.curNumCandidates);
            // Initialize a Candidate with name, party, tree, and initialized vote info (to keep track of vote info per round)
            ArrayList<String> voteInfo = new ArrayList<>();
            voteInfo.add(candidateInfo[0]);
            voteInfo.add(candidateInfo[1]);
            // Make a new Candidate object
            Candidate newCandidate = new Candidate(candidateInfo[0], candidateInfo[1], newTree, voteInfo);
            this.candidates[i] = newCandidate;
            String[] candidateRecord = {"Candidate " + i + " name: " + candidateInfo[0] +
                    " Party: " + candidateInfo[1]};
            writeToAudit(candidateRecord);
        }
    }

    /**
     * Conducts the IR algorithm. Determines the winner of the election, deciding if there are ties and who
     * to eliminate if necessary.
     */
    public void conductAlgorithm() {
        int smallestVotes;
        int toEliminate;
        Candidate winner;
        // update vote totals with the INITIAL first round
        updateVoteDifferenceAfterRound();
        // Continue conducting eliminations as long as there is no majority and there are more than two candidates.
        while ((winner = majorityCandidate()) == null && this.curNumCandidates > 2) {
            // Find the index of the candidate to eliminate (the one with the fewest votes)
            ArrayList<Integer> tiedCandidates = new ArrayList<>();
            smallestVotes = Integer.MAX_VALUE;
            for (int i = 0; i < this.candidates.length; i++) {
                // If there is a new minimum, clear the list of the smallest votes
                // Add the candidate's index in our tiedCandidates to show how it has the fewest number so far
                if (!candidates[i].isEliminated() && candidates[i].getNumVotes() < smallestVotes) {
                    tiedCandidates.clear();
                    tiedCandidates.add(i);
                    smallestVotes = candidates[i].getNumVotes();
                } else if (!candidates[i].isEliminated() && candidates[i].getNumVotes() == smallestVotes) {
                    // Add a candidate's index without clearing the list when there's a tie for the fewest number of votes
                    tiedCandidates.add(i);
                }
            }
            if (tiedCandidates.size() > 1) {
                // There is more than one candidate with the fewest number of votes
                // Write tie status to audit file
                String[] tieAnnouncement = {"There is currently a tie between candidates: "};
                writeToAudit(tieAnnouncement);
                String[] tiedCandidatesNames = new String[tiedCandidates.size()];
                for (int i = 0; i < tiedCandidates.size(); i++) {
                    tiedCandidatesNames[i] = this.candidates[tiedCandidates.get(i)].getName() + " ";
                }
                writeToAudit(tiedCandidatesNames);
                toEliminate = (breakTie(tiedCandidates, 1)[0]);
                String[] tieBreakResult = {"Result of tie break: " +
                        this.candidates[toEliminate].getName()};
                writeToAudit(tieBreakResult);
            } else {
                // There is only one candidate with the fewest number of votes
                toEliminate = tiedCandidates.get(0);
            }
            eliminateCandidate(toEliminate);
            // Increment total rounds
            this.totalRounds += 1;
            // Make sure to update the vote differences/totals
            updateVoteDifferenceAfterRound();
        }
        // There are 2 candidates left. Choose the winner by popularity.
        if (winner == null) {
            int first_total = -1;
            for (Candidate finalist : this.candidates) {
                if (!finalist.isEliminated()) {
                    first_total = finalist.getNumVotes();
                    winner = finalist;
                } else if (!finalist.isEliminated() && (finalist.getNumVotes() > first_total)) {
                    winner = finalist;
                }
            }
        }
        // A winner has been chosen.
        String winnerAnnouncement = "Candidate " + winner.getName() + " has won with " +
                winner.getNumVotes() + " votes.";
        String[] announcement = {winnerAnnouncement};
        writeToAudit(announcement);
        try {
            if (this.audit != null) {
                audit.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // show a table
        exportToTable();
    }
    // testing purposes
    /**
     * getter that returns Candidate array
     * @return Candidate array of Candidate objects
     */
    public Candidate[] getCandidates() {return this.candidates;}

    /**
     * setter for candidates
     * @param candidates A Candidate Array to set
     */
    public void setCandidates(Candidate[] candidates) {this.candidates = candidates;}

    /**
     * getter for curNumCandidates
     * @return int that represents curNumCandidates
     */
    public int getCurNumCandidates() {return this.curNumCandidates;}

    /**
     * getter for numBallots
     * @return int that represents numBallots
     */
    public int getNumBallots() {return this.numBallots;}

    /**
     * getter for totals
     * @return ArrayList that is this IR instance's totals (votes)
     */
    public ArrayList<String> getTotals() {return this.totals;}

    /**
     * method for incrementing the total rounds
     */
    public void incrementTotalRounds() {this.totalRounds += 1;}

    /**
     * @return ArrayList returns the IR exhausted votes list
     */
    public ArrayList<String> getTotalExhausted() {return this.exhausted;}

    /**
     * @param newTotals an ArrayList to set the new totals.
     */
    public void setTotals(ArrayList<String> newTotals) {this.totals = newTotals;}

    /**
     * @param newExhausted an ArrayList that represents the new exhausted list.
     */
    public void setExhausted(ArrayList<String> newExhausted) {this.exhausted = newExhausted;}

    /**
     * @param newRounds an int that represents the new rounds for IR.
     */
    public void setRounds(int newRounds) {this.totalRounds = newRounds;}

    @Override
    public String toString(){
        String ret = "";
        ret += "Number of Candidates: " + this.curNumCandidates + "\n";
        ret += "Number of Ballots: " + this.numBallots + "\n";
        ret += "Total Files read: " + this.filesRead + "\n";
        return ret;
    }

}
