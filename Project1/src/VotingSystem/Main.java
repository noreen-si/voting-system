package VotingSystem;

/**
 * This is what a user uses to run our Voting System program
 * It then hands off control to the RunElection instance
 * @author Jonathan Haak
 */
public class Main {
/**
 * The main method of the program.
 *
 * @param args The command line arguments.
 */
public static void main(String args[]) {
    String holder = null;

    // Check if command line argument(s) are provided
    if (args.length > 0) {
        holder = args[0];
    }

    // Create a new RunElection object and start the election
    RunElection newElection = new RunElection(holder);
    newElection.start();
}

}
