package VotingSystem;

import java.util.ArrayList;

/**
 * This class represents candidates in an IR election.
 * Each Candidate stores key information, such as a name, a party, elimination status, ballots, and more.
 * @author Jonathan Haak
 */
public class Candidate {
    private String name;
    private String party;
    private Tree ballots;
    private boolean eliminated;
    private ArrayList<String> voteInfoPerRound;
    private int voteDifference;

    /**
     * This is the constructor for the Candidate class.
     * @param name a String that represents the name of the Candidate.
     * @param party a String that represents the party of the Candidate.
     * @param ballots a Tree that stores the ballot information for the Candidate.
     */
    public Candidate(String name, String party, Tree ballots, ArrayList<String> voteInfo){
        this.name = name;
        this.party = party;
        this.ballots = ballots;
        eliminated = false;
        this.voteInfoPerRound = voteInfo;
        this.voteDifference = 0;
    }

    /**
     * This is the getter method for the name.
     * @return a String that is the name of the Candidate.
     */
    public String getName(){return name;}

    /**
     * This is the getter method for the party.
     * @return a String that is the name of the party.
     */
    public String getParty(){return party;}

    /**
     * This is the getter method for the current number of votes for the candidate.
     * @return an int that represents the number of votes for the candidate.
     */
    public int getNumVotes(){return ballots.root.numVotes;}

    /**
     * This is a method that can be used to access a candidate's elimination status.
     * @return a boolean that is true if the candidate is elimintated, and false otherwise.
     */
    public boolean isEliminated(){return eliminated;}

    /**
     * This is the setter method for the candidate's elimination status.
     * @param out a boolean that is true if eliminated should be true, and false otherwise.
     */
    public void setEliminated(boolean out){
        eliminated = out;
    }

    /**
     * This is the getter method for the candidate's ballots Tree.
     * @return a Tree that is the Tree associated with the candidate.
     */
    public Tree getBallots(){return ballots;}

    public ArrayList<String> getVoteInfoPerRound() { return this.voteInfoPerRound;}
    public void setVoteInfoPerRound(ArrayList<String> list) { this.voteInfoPerRound = list;}

    public void incrementVoteDifference() {
        this.voteDifference += 1;
    }

    public int getVoteDifference() { return this.voteDifference; }
    public void resetVoteDifference() { this.voteDifference = 0;}

}
