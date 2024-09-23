package VotingSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/** CPL Election that can read in data from an ballot file, execute the CPL election algorithm, and output the results, all while writing to an audit file
 * @author Lane Enget
 * @author Pyrenees Gavois
*/
public class CPL extends Election {
    private Party[] parties;
    private int voteTotal;
    private int totalSeats;
    private FileReader input;
    private FileWriter output;
    private BufferedReader br;

    private int[] globalVotes;
    private int globalSeats;

    /**
    * This is the constructor for CPL.
    * @param input A FileReader that represents the election input file to process
    * @param audit A FileWriter that represents the audit file to write to
    * @param br Is the BufferReader that this class will use to parse the input file
    */
    public CPL(FileReader input, FileWriter audit, BufferedReader br){
        super.input = input;
        super.audit = audit;
        this.br = br;
    }

    /**
     * Getter method for the total votes of the CPL election, primarily used for testing
     *
     * @return The int representing the total amount of votes
     */
    public int getVoteTotal() {
        return voteTotal;
    }

    /**
     * Getter method for the total number of open seats of the CPL election, primarily used for testing
     *
     * @return The int representing the number of seats
     */
    public int getTotalSeats() {
        return totalSeats;
    }

    /**
     * Getter method for the array of parties, primarily used for testing
     *
     * @return The array of party objects representing all parties in the CPL election
     */
    public Party[] getParties() {
        return parties;
    }

    /**
     * Redistributes excess seats randomly to other parties that are NOT "index", that is when a party "index" has won more seats than it has candidates
     * @param index Int that represents the index of the party that has too mant seats
     * @param seats Int that represents the number of excess seats said party has earned
     */
    private void drawLotto(int index, int seats){
        ArrayList<Integer> subIndexes = new ArrayList<Integer>();
        for(int i = 0; i < parties.length; i++) {
            if(i != index) {
                subIndexes.add(i);
            }
        }
        String[] announcement = {"Drawing Lottery (redistributing) on " + seats +
            " extra seats from the party " + parties[index].getName()};
        writeToAudit(announcement);
        for(int i = 0; i < seats; i++) {
            int[] party = breakTie(subIndexes, 1);
            parties[party[0]].incrementSeatCount();
            String[] subAnnouncement = {"One lotto seat has been given to " + parties[party[0]].getName()};
            writeToAudit(subAnnouncement);
        }
    }
    
    /**
    * Processes the input file to count votes for each party and writes the record to the audit file
    */
    public void processFile(){
        String ballot = "";
        for(int i = 0; i < voteTotal; i++) {
            try {
                ballot = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            parties[ballot.indexOf("1")].incrementVoteCount();
            globalVotes[ballot.indexOf("1")] = globalVotes[ballot.indexOf("1")] + 1;
            String[] announcement = {"Ballot " + ballot + " increments vote count of the '" + parties[ballot.indexOf("1")].getName() + "' party"};
            writeToAudit(announcement);
        }
    }

    /**
     * Conducts main algorithm for CPL election, that is populating the parties with the seats they've earned
     */
    public void conductAlgorithm(){
        
        //checks the edge case where there is the exact same amount of seats as candidates
        int totalCandidates = 0;
        for (int i = 0; i < parties.length; i++) {
            totalCandidates = totalCandidates + parties[i].getCandidates().length;
        }
        if(totalCandidates == totalSeats) {
            for (int i = 0; i < parties.length; i++) {
               parties[i].setSeatsReceived(parties[i].getCandidates().length);
            }
            return;
        }
        int leftover = voteTotal%totalSeats;
        int quota = (voteTotal + (totalSeats - leftover))/totalSeats; //forces quota to round up

        // First allocation of seats
        for (int i = 0; i < parties.length; i++) {
            int seatsWon = parties[i].getNumVotes()/quota;
            int prevVotes = parties[i].getNumVotes();
            parties[i].setNumVotes(parties[i].getNumVotes()%quota);

            for (int j = 0; j < seatsWon; j++) {
                parties[i].incrementSeatCount();
                totalSeats--;
            }
            String[] announcement = {parties[i].getName() + " gets " + seatsWon + " for the first round of seat allocation based on their " +
                prevVotes + " votes. " + parties[i].getNumVotes() + " votes will count for this party in the second round."};
            writeToAudit(announcement);
        }

        // Second allocation of seats
        while (totalSeats != 0) {
            ArrayList<Integer> maxIndexes = new ArrayList<Integer>();
            int maxIndex = 0;
            int maxVotes = -1;
            for (int i = 0; i < parties.length; i++) {
                if (parties[i].getNumVotes() > maxVotes) {
                    maxIndex = i;
                    maxVotes = parties[i].getNumVotes();
                } else if (parties[i].getNumVotes() == maxVotes) {
                    if(maxIndexes.contains(i) == false) {
                        maxIndexes.add(i);
                    }
                    if(maxIndexes.contains(maxIndex) == false) {
                        maxIndexes.add(maxIndex);
                    }
                }
            }
            if(maxIndexes.size() > 1) {
                int[] party = breakTie(maxIndexes, 1);
                String[] announcement = {parties[party[0]].getName() + " gets one extra seat in the second round thanks to their " +
                    parties[party[0]].getNumVotes() + " extra votes. They won a tie break case involving"};
                writeToAudit(announcement);
                for(int j = 0; j < maxIndexes.size(); j++) {
                    String[] subAnnouncement = {parties[maxIndexes.get(j)].getName()};
                    writeToAudit(subAnnouncement);
                }
                parties[party[0]].incrementSeatCount();
                parties[party[0]].setNumVotes(0);
                totalSeats--;
            } else {
                String[] announcement = {parties[maxIndex].getName() + " gets one extra seat in the second round thanks to their " +
                    parties[maxIndex].getNumVotes() + " extra votes."};
                writeToAudit(announcement);
                parties[maxIndex].incrementSeatCount();
                parties[maxIndex].setNumVotes(0);
                totalSeats--;
            }
        }

        //Redistributes seats among parties if some parties have excess seats
        for(int i = 0; i < parties.length; i++) {
            if(parties[i].getSeatsReceived() > parties[i].getCandidates().length) {
                int extra = parties[i].getSeatsReceived() - parties[i].getCandidates().length;
                parties[i].setSeatsReceived(parties[i].getCandidates().length);
                drawLotto(i, extra);
                i=0; //restart loop
            }
        }

        //Closing the audit file
        try {
            if (this.audit != null) {
                audit.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the header of the input file and creates the parties and candidates internally, as well as stores other auxillary information
     * like the number of votes and number of seats in the election
     */
    public void parseHeader(){

        String nextRecord = "";
        for (int i = 0; i < 2; i++) {
            try {
                nextRecord = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (i == 1) {
                String[] partyNames = nextRecord.split(", ");
                initializeParty(partyNames);
                globalVotes = new int[parties.length];
            }
        }

        for (int i = 0; i < this.parties.length; i++) {
            try {
                nextRecord = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parties[i].setCandidates(nextRecord.split(", "));
            String[] announcement = {"The candidates in the '" + parties[i].getName() + "' party are"};
            writeToAudit(announcement); 
            writeToAudit(parties[i].getCandidates()); 
        }

        for (int i = 0; i < 2; i++) {
            try {
                nextRecord = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i == 0) {
                this.totalSeats = Integer.parseInt(nextRecord);
                this.globalSeats = totalSeats;
                String[] announcement = {"Number of seats in this election is " + totalSeats};
                writeToAudit(announcement); 
            } else if (i == 1) {
                this.voteTotal = Integer.parseInt(nextRecord);
                String[] announcement = {"Number of votes in this elections is " + voteTotal};
                writeToAudit(announcement); 
            }
        }
    }

    /**
     * Helper function for parseHeader(). This function initializes the party objects and party array.
     * @param partyNames String[] that is an array of all the party names
     */
    public void initializeParty(String[] partyNames){
        parties = new Party[partyNames.length];
        for(int j = 0; j < partyNames.length; j++) {
            Party party = new Party(partyNames[j]);
            parties[j] = party;
            String[] announcement = {"Registering and initializing party '" + party.getName() + "'"};
            writeToAudit(announcement); 
        }
    }

    /**
     * Function that prints results of the CPL election to the terminal. Will first show all the parties and how many seats they've won,
     * then will show who exactly (candidate names) have won a seat and their associated party.
     */
    public void printResults(){
        System.out.println("-------------------------   ELECTION RESULTS   -------------------------");
        ArrayList<Party> partyArrayList = new ArrayList<Party>();
        
        for (int i = 0; i < this.parties.length; i++) {
            partyArrayList.add(this.parties[i]);
        }

        for (int i = 0; i < partyArrayList.size(); i++) {
            int percentSeats = (100 * partyArrayList.get(i).getSeatsReceived()) / globalSeats;
            int percentVotes = (100 * globalVotes[i]) / voteTotal;
            System.out.println(partyArrayList.get(i).getName() + "received: " + partyArrayList.get(i).getSeatsReceived() + "seats representing " + percentSeats + "% of the total seats from their " + percentVotes + "% of total vote share\n\n");
        }

        for (int i = 0; i < partyArrayList.size(); i++) {
            int winCount = partyArrayList.get(i).getSeatsReceived();
            if(winCount > 0) {
                System.out.print(partyArrayList.get(i).getName() + ": ");
                for (int j = 0; j < winCount; j++) {
                    System.out.print(partyArrayList.get(i).getCandidates()[j] + " ");
                }
                System.out.print("\n");
            }
            
        }

        System.out.println("-----------------------------------------------------------------------\n\n");
    }
}