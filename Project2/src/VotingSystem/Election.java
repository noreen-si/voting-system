package VotingSystem;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.security.SecureRandom;


/**
 * This is an abstract class that represents what CPL and IR both have in common
 * @author Jonathan Haak
 */
public abstract class Election {
    protected FileWriter audit;
    protected ArrayList<FileReader> input;

    abstract public void processFile();
    /**
     * This method is used to break ties in the election. It takes in an arraylist of candidates
     * and the number of candidates to eliminate. It then randomly selects candidates to eliminate
     * @param candidates ArrayList of candidates to break the tie
     * @param numToEliminate number of candidates to eliminate
     * @return int[] of the candidates to eliminate
     */
    public int [] breakTie(ArrayList<Integer> candidates, int numToEliminate){
        if(candidates.size() <= numToEliminate){ // If there are less candidates than the number to eliminate
            return candidates.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
        }
        int[] eliminated = new int[numToEliminate]; // Array to store the eliminated candidates
        int n = numToEliminate; // Number of candidates to eliminate
        ArrayList<Integer> c = new ArrayList<Integer>(candidates); // Copy of the candidates
        while(n > 0){ // While there are still candidates to eliminate
            SecureRandom rand = new SecureRandom(); // Random number generator
            int index = rand.nextInt(c.size()); // Random index in range of the candidates
            eliminated[n - 1] = c.get(index); // Add the candidate to the eliminated array
            c.remove(index); // Remove the candidate from the candidates array
            n--; // Decrement the number of candidates to eliminate
        }
        return eliminated; // Return the array of eliminated candidates
    }

    /**
     * This is an abstract method meant to represent conducting an election
     */
    abstract public void conductAlgorithm();

    /**
     * This is an abstract method meant to represent printing the results for an election
     */
    abstract public void printResults();
    /**
     * This method is used to write the output to the audit file
     * @param output String[] of the output to write to the audit file
     */
    public void writeToAudit(String[] output){
        try {
            for (String s : output) { // Write the output to the audit file
                audit.write(s); 
                audit.write(", ");
            }
            audit.write("\n"); // Write a new line to the audit file
        } catch (Exception e) {
            System.out.println("Error writing to audit file");
        }

    }

    /**
     * This is an abstract method meant to represent parsing the header
     */
    abstract public void parseHeader();


}
