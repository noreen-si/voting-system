package VotingSystem;

import java.io.*;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.fail;

public class IRtests {
    RunElection runElection;
    IR ir;
    String[] candidateNames = {"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)"};
    IR irInitialize;
    IR irMajority1;
    IR irMajority2;
    IR irMajority3;
    IR irReassignNoneEliminated1;
    IR irReassign1;
    IR irReassign2;
    IR irEliminate1;
    IR irEliminate2;
    IR irConductAlgorithm1;
    IR irConductAlgorithm2;
    IR irConductAlgorithm3;
    IR irConductAlgorithm4;
    IR irConductAlgorithm5;
    IR irConductAlgorithm6;

    @Before
    public void setUp() throws IOException {
         runElection = new RunElection("test.txt");

         FileReader input = new FileReader((IRtests.class.getResource("IRHeader1.csv")).getFile());
         BufferedReader brIR = new BufferedReader(input);
         brIR.readLine();
         ir = new IR(input, null, brIR);
         ir.parseHeader();

         irInitialize = new IR(input, null, brIR);
         irInitialize.setCandidates(new Candidate[4]);
         // Please note that initializeCandidates is now Private
        // Since this line runs before any test, please change initializeCandidates back to public for testing
         irInitialize.initializeCandidates(candidateNames);

    }

    @Test
    public void runTests(){
        Assert.assertTrue("test.txt" == runElection.filename);
    }

    @Test
    public void test_initialize_candidates_1(){
        Assert.assertTrue(irInitialize.getCandidates()[0].getName().equals("Rosen"));
    }
    @Test
    public void test_initialize_candidates_2(){
        Assert.assertTrue(irInitialize.getCandidates()[0].getParty().equals("(D)"));
    }

    @Test
    public void test_initialize_candidates_3(){
        Assert.assertTrue(irInitialize.getCandidates()[3].getName().equals("Royce"));
    }
    @Test
    public void test_initialize_candidates_4(){
        Assert.assertTrue(irInitialize.getCandidates()[3].getParty().equals("(L)"));
    }

    @Test
    public void test_majority_candidate_1() throws IOException {
        // Input: Two Candidates
        // Inserted ballots for Candidate A: [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Inserted ballots for Candidate B: [2, 1]  [0, 1]
        // Candidate A total first-place votes: 4
        // Candidate B total first-place votes: 2
        // Non-Zero vote total
        FileReader majority1Input = new FileReader((IRtests.class.getResource("IRMajority1.csv")).getFile());
        BufferedReader brMajority1Input = new BufferedReader(majority1Input);
        brMajority1Input.readLine();
        irMajority1 = new IR(majority1Input, null, brMajority1Input);
        irMajority1.parseHeader();
        irMajority1.processFile();
        Assert.assertTrue(irMajority1.majorityCandidate() == irMajority1.getCandidates()[0]);
    }

    @Test
    public void test_majority_candidate_2() throws IOException {
        // Testing when exact 50-50
        // Input: Two Candidates
        // Inserted ballots for Candidate A: [1, 0]  [1, 2]
        // Inserted ballots for Candidate B: [2, 1]  [0, 1]
        // Candidate A total first-place votes: 2
        // Candidate B total first-place votes: 2
        // Non-Zero vote total
        FileReader majority2Input = new FileReader((IRtests.class.getResource("IRMajority2.csv")).getFile());
        BufferedReader brMajority2Input = new BufferedReader(majority2Input);
        brMajority2Input.readLine();
        irMajority2 = new IR(majority2Input, null, brMajority2Input);
        irMajority2.parseHeader();
        irMajority2.processFile();

        Assert.assertTrue(irMajority2.majorityCandidate() == null);
    }

    @Test
    public void test_majority_candidate_3() throws IOException {
        // Input: Three Candidates
        // Inserted ballots for Candidate A: [1, 0, 0]  [1, 2, 3]
        // Inserted ballots for Candidate B: [2, 1, 3]  [3, 1, 2]
        // Inserted ballots for Candidate C: [3, 2, 1]  [2, 3, 1]   [0, 0, 1]   [2, 0, 1]
        // Candidate A total first-place votes: 2
        // Candidate B total first-place votes: 2
        // Candidate C total first-place votes: 4
        // Nobody wins majority
        FileReader majority3Input = new FileReader((IRtests.class.getResource("IRMajority3.csv")).getFile());
        BufferedReader brMajority3Input = new BufferedReader(majority3Input);
        brMajority3Input.readLine();
        irMajority3 = new IR(majority3Input, null, brMajority3Input);
        irMajority3.parseHeader();
        irMajority3.processFile();
        Assert.assertTrue(irMajority3.majorityCandidate() == null);
    }

    @Test
    public void test_parseHeader_1() {
        Assert.assertTrue(ir.getCurNumCandidates() == 4);
    }

    @Test
    public void test_parseHeader_2() {
        Assert.assertTrue(ir.getNumBallots() == 6);
    }

    @Test
    public void test_parseHeader_3() {
        Assert.assertTrue(ir.getCandidates()[2].getName().equals("Chou"));
    }

    @Test
    public void test_reassignVotesNoneEliminated_1() throws IOException {
        // Simple case where no candidates have yet been eliminated
        // Reassign Candidate A ballots to Candidate B ballots
        // Candidate A ballots:          [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Candidate B ballots:          [2, 1]  [0, 1]
        // Inserting:                            [0, 1]  [0, 1]
        // (end) Candidate B Tree:       [2, 1]  [0, 1]  [0, 1]  [0, 1]
        // BTree1 numVotes: 4
        FileReader reassignNoneEliminated1Input = new FileReader((IRtests.class.getResource("IRReassignNoneEliminated1.csv")).getFile());
        BufferedReader brReassignNoneEliminated1Input = new BufferedReader(reassignNoneEliminated1Input);
        brReassignNoneEliminated1Input.readLine();
        FileWriter reassignNoneEliminated1Audit = new FileWriter("Audit_IRReassignNoneEliminated1.txt");
        irReassignNoneEliminated1 = new IR(reassignNoneEliminated1Input, reassignNoneEliminated1Audit, brReassignNoneEliminated1Input);
        irReassignNoneEliminated1.parseHeader();
        irReassignNoneEliminated1.processFile();

        Node BNode = irReassignNoneEliminated1
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[1];
        ArrayList<ArrayList<Integer>> toInsert = irReassignNoneEliminated1
                .getCandidates()[0]
                .getBallots()
                .getBallots(BNode);
        irReassignNoneEliminated1.reassignVotesNoneEliminated(toInsert, 1);
        Assert.assertTrue(irReassignNoneEliminated1.getCandidates()[1].getNumVotes() == 4);
    }

    @Test
    public void test_reassignVotes_1() throws IOException {
        // Intermediate case where there is one previously eliminated candidate
        // Testing double reassignment
        // ORIGINAL ballots:
        // Candidate A ballots: [1,2,3]  [1,0,2]  [1,3,2]  [1,0,0]
        // Candidate B ballots: [0,1,0]  [0,1,2]  [2,1,3]  [3,1,2]
        // Candidate C ballots: [0,0,1]  [2,3,1]  [2,0,1]  [2,0,1]
        //                      [0,1,2]  <--- when getting 2nd place for B
        //                               [0,0,1]  [0,2,1]  <--- when getting 2nd place for C

        // First, eliminate A. State of Candidate B, Candidate C after:
        // Candidate B ballots: [0,1,0] *[0,1,2] [2,1,3] [3,1,2]     [0,1,2]*
        // Candidate C ballots: [0,0,1] [2,3,1] [2,0,1] [2,0,1]     [0,0,1] [0,2,1]
        // Next, eliminate B. State of Candidate C after:
        // Candidate C ballots: [0,0,1] [2,3,1] [2,0,1] [2,0,1]     [0,0,1] [0,2,1]     [0,0,1] [0,0,1] [0,0,1] [0,0,1]
        // Candidate C numVotes = 10
        //                              [0,0,1]         [2,0,1]     [0,0,1] <-- when getting 2nd place C nodes
        //                                      [1,0,2]                     <-- when getting 2nd place A nodes

        FileReader reassign1Input = new FileReader((IRtests.class.getResource("IRReassign1.csv")).getFile());
        BufferedReader brReassign1Input = new BufferedReader(reassign1Input);
        brReassign1Input.readLine();
        irReassign1 = new IR(reassign1Input, null, brReassign1Input);
        irReassign1.parseHeader();
        irReassign1.processFile();

        ArrayList<Integer> eliminated = new ArrayList<>();
        Node BNode_wave1 = irReassign1
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[1];
        ArrayList<ArrayList<Integer>> toInsert_B_wave1 = irReassign1
                .getCandidates()[0]
                .getBallots()               // A Tree
                .getBallots(BNode_wave1);   // Should be the B ballot subtree

        Node CNode_wave1 = irReassign1
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[2];
        ArrayList<ArrayList<Integer>> toInsert_C_wave1 = irReassign1
                .getCandidates()[0]
                .getBallots()
                .getBallots(CNode_wave1);
        irReassign1.reassignVotesNoneEliminated(toInsert_B_wave1, 1);
        irReassign1.reassignVotesNoneEliminated(toInsert_C_wave1, 2);
        irReassign1.getCandidates()[0].setEliminated(true);
        eliminated.add(0);
        // SECOND Reassignment wave (reassign B to C)
        Node CNode_wave2 = irReassign1
                .getCandidates()[1]
                .getBallots()
                .getRoot()
                .getChildren()[2];
        ArrayList<ArrayList<Integer>> toInsert_C_wave2 = irReassign1
                .getCandidates()[1]
                .getBallots()
                .getBallots(CNode_wave2);
        Node ANode_wave2 = irReassign1
                .getCandidates()[1]
                .getBallots()
                .getRoot()
                .getChildren()[0];
        ArrayList<ArrayList<Integer>> toInsert_A_wave2 = irReassign1
                .getCandidates()[1]
                .getBallots()
                .getBallots(ANode_wave2);

        irReassign1.reassignVotes(eliminated, toInsert_C_wave2);
        irReassign1.reassignVotes(eliminated, (toInsert_A_wave2));
        Assert.assertTrue(irReassign1.getCandidates()[2].getNumVotes() == 10);
    }

    @Test
    public void test_eliminate_1() throws IOException {
        // This tests out the exact same election as test_reassignVotes_2
        // Except directly through elimination
        FileReader eliminate1Input = new FileReader((IRtests.class.getResource("IREliminate1.csv")).getFile());
        // Same as reassign2Input
        BufferedReader brEliminate1Input = new BufferedReader(eliminate1Input);
        brEliminate1Input.readLine();
        irEliminate1 = new IR(eliminate1Input, null, brEliminate1Input);
        irEliminate1.parseHeader();
        irEliminate1.processFile();
        irEliminate1.eliminateCandidate(0);
        irEliminate1.eliminateCandidate(1);

        Assert.assertTrue(irEliminate1.getCandidates()[2].getNumVotes() == 10);
    }

    @Test
    public void test_reassignVotes_2() throws IOException {
        // Test reassignment with multiple eliminated candidates
        // Five candidates total
        // Candidate A ballots:     [1,3,2,5,4] [1,0,4,3,2]
        // Candidate B ballots:     [0,1,0,0,0]
        // Previously eliminated:   Candidates in indices 2, 4
        // Reassign A to B:         [0,2,1,4,3]
        // B afterwards:            [0,1,0,0,0] [0,1,0,2,0]

        FileReader reassign2Input = new FileReader((IRtests.class.getResource("IRReassign2.csv")).getFile());
        BufferedReader brReassign2Input = new BufferedReader(reassign2Input);
        brReassign2Input.readLine();
        irReassign2 = new IR(reassign2Input, null, brReassign2Input);
        irReassign2.parseHeader();
        irReassign2.processFile();
        irReassign2.getCandidates()[2].setEliminated(true);
        irReassign2.getCandidates()[4].setEliminated(true);

        ArrayList<Integer> eliminated = new ArrayList<>();
        eliminated.add(2);
        eliminated.add(4);
        Node ballotNode = irReassign2
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[2];
        ArrayList<ArrayList<Integer>> toInsert = irReassign2
                .getCandidates()[0]
                .getBallots()
                .getBallots(ballotNode);
        irReassign2.reassignVotes(eliminated, toInsert);
        Assert.assertTrue(irReassign2.getCandidates()[1].getNumVotes() == 2);
    }

    @Test
    public void test_eliminate_2() throws IOException {
        FileReader eliminate2Input = new FileReader((IRtests.class.getResource("IREliminate2.csv")).getFile());
        BufferedReader brEliminate2Input = new BufferedReader(eliminate2Input);
        brEliminate2Input.readLine();

        irEliminate2 = new IR(eliminate2Input, null, brEliminate2Input);
        irEliminate2.parseHeader();
        irEliminate2.processFile();
        irEliminate2.getCandidates()[2].setEliminated(true);
        irEliminate2.getCandidates()[4].setEliminated(true);
        irEliminate2.eliminateCandidate(0);

        Assert.assertTrue(irEliminate2.getCandidates()[1].getNumVotes() == 2);
    }

   @Test
   public void test_conductAlgorithm_1() throws IOException {
        // Simple case where there are 2 candidates
        // Candidate A has 2 votes, Candidate B has 0 votes -- Candidate A wins
       FileReader conductAlgorithm1Input = new FileReader((IRtests.class.getResource("IRConductAlgorithm1.csv")).getFile());

       FileWriter conductAlgorithm1Audit;
       String path = System.getProperty("user.dir");
       File f = new File(path, "Audit_IRConductAlgorithm1.txt");
       conductAlgorithm1Audit = new FileWriter(f);

       BufferedReader brConductAlgorithm1Input = new BufferedReader(conductAlgorithm1Input);
       brConductAlgorithm1Input.readLine();
       irConductAlgorithm1 = new IR(conductAlgorithm1Input, conductAlgorithm1Audit, brConductAlgorithm1Input);
       irConductAlgorithm1.parseHeader();
       irConductAlgorithm1.processFile();
       irConductAlgorithm1.conductAlgorithm();
   }
    @Test
    public void test_conductAlgorithm_2() throws IOException {
        // Test where there are no ties -- there is no immediate majority
        // A candidate must be eliminated to determine the winner
        // Simple election with 3 candidates -- Candidate B wins
        // Candidate A: [1,0,0] [1,2,3] [1,3,2]
        // Candidate B: [0,1,2] [2,1,3] [0,1,0] [0,1,2]
        // Candidate C: [0,2,1] [0,2,1]
        // After C is eliminated:
        // Candidate A: [1,0,0] [1,2,3] [1,3,2]
        // Candidate B: [0,1,2] [2,1,3] [0,1,0] [0,1,2] [0,0,1] [0,0,1]
        // Candidate B has 6/9 votes ---> has majority ---> wins

        FileReader conductAlgorithm2Input = new FileReader((IRtests.class.getResource("IRConductAlgorithm2.csv")).getFile());

        FileWriter conductAlgorithm2Audit;
        String path = System.getProperty("user.dir");
        File f = new File(path, "Audit_IRConductAlgorithm2.txt");
        conductAlgorithm2Audit = new FileWriter(f);

        BufferedReader brConductAlgorithm2Input = new BufferedReader(conductAlgorithm2Input);
        brConductAlgorithm2Input.readLine();
        irConductAlgorithm2 = new IR(conductAlgorithm2Input, conductAlgorithm2Audit, brConductAlgorithm2Input);
        irConductAlgorithm2.parseHeader();
        irConductAlgorithm2.processFile();
        irConductAlgorithm2.conductAlgorithm();
    }

    @Test
    public void test_conductAlgorithm_3() throws IOException {
        // NOTE: This test ONLY works while in development
        // As we need to change the breakTie method to return a predetermined number
        // In order to predict exactly who the winner will be every time this test is run.
        // Test where there is one tie, and there are three candidates -- B should win, A & C tied -> A eliminated
        // For purposes of testing, the first candidate in the tied list will be eliminated
        // Candidate A: [1,0,0] [1,2,3]
        // Candidate B: [3,1,2] [2,1,3] [0,1,0] [2,1,0]
        // Candidate C: [0,0,1] [2,0,1]
        // After A gets eliminated ->
        // Candidate B: [3,1,2] [2,1,3] [0,1,0] [2,1,0]    [0,1,2]
        // Candidate C: [0,0,1] [2,0,1]
        // B wins with 5 votes
        FileReader conductAlgorithm3Input = new FileReader((IRtests.class.getResource("IRConductAlgorithm3.csv")).getFile());

        FileWriter conductAlgorithm3Audit;
        String path = System.getProperty("user.dir");
        File f = new File(path, "Audit_IRConductAlgorithm3.txt");
        conductAlgorithm3Audit = new FileWriter(f);

        BufferedReader brConductAlgorithm3Input = new BufferedReader(conductAlgorithm3Input);
        brConductAlgorithm3Input.readLine();
        irConductAlgorithm3 = new IR(conductAlgorithm3Input, conductAlgorithm3Audit, brConductAlgorithm3Input);
        irConductAlgorithm3.parseHeader();
        irConductAlgorithm3.processFile();
        irConductAlgorithm3.conductAlgorithm();
    }

    @Test
    public void test_conductAlgorithm_4() throws IOException {
        // Test where there are multiple ties for elimination
        // First, tie between B & C & D -> B Eliminated
        // Next, tie between C & D -> C Eliminated
        // D will win the majority compared to A
        // For purposes of testing, the first candidate in the tied list will be eliminated
        // Candidate A: [1,0,0,0] [1,2,3,4] [1,2,3,4] [1,2,3,4] [1,0,0,0] [1,0,0,0]
        // Candidate B: [0,1,0,0] [3,1,2,0] [0,1,0,0] [3,1,4,2]
        // Candidate C: [2,0,1,0] [2,0,1,0] [2,0,1,3] [2,0,1,3]
        // Candidate D: [4,3,2,1] [0,2,0,1] [2,0,0,1] [2,3,0,1]
        // After B is eliminated:
        // Candidate A: [1,0,0,0] [1,2,3,4] [1,2,3,4] [1,2,3,4] [1,0,0,0] [1,0,0,0]
        // Candidate C: [2,0,1,0] [2,0,1,0] [2,0,1,3] [2,0,1,3] *[2,0,1,0]*
        // Candidate D: [4,3,2,1] [0,2,0,1] [2,0,0,1] [2,3,0,1] *[2,0,3,1]*
        // After C is eliminated:
        // Candidate A: [1,0,0,0] [1,2,3,4] [1,2,3,4] [1,2,3,4] [1,0,0,0] [1,0,0,0]
        //             *[1,0,0,0] [1,0,0,0] [1,0,0,2] [1,0,0,2] [1,0,0,0]*
        // Candidate D: [4,3,2,1] [0,2,0,1] [2,0,0,1] [2,3,0,1] [2,0,3,1]
        // Candidate A wins with 11 votes (total votes: 16)
        FileReader conductAlgorithm4Input = new FileReader((IRtests.class.getResource("IRConductAlgorithm4.csv")).getFile());
        FileWriter conductAlgorithm4Audit;
        String path = System.getProperty("user.dir");
        File f = new File(path, "Audit_IRConductAlgorithm4.txt");
        conductAlgorithm4Audit = new FileWriter(f);

        BufferedReader brConductAlgorithm4Input = new BufferedReader(conductAlgorithm4Input);
        brConductAlgorithm4Input.readLine();
        irConductAlgorithm4 = new IR(conductAlgorithm4Input, conductAlgorithm4Audit, brConductAlgorithm4Input);

        irConductAlgorithm4.parseHeader();
        irConductAlgorithm4.processFile();
        irConductAlgorithm4.conductAlgorithm();
    }

    @Test
    public void test_conductAlgorithm_5() throws IOException {
        // Test where POPULARITY decides the winner
        // Candidate A: [1,2] [1,0] [1,2]
        // Candidate B: [2,1] [2,1]
        // A wins
        FileReader conductAlgorithm5Input = new FileReader((IRtests.class.getResource("IRConductAlgorithm5.csv")).getFile());

        FileWriter conductAlgorithm5Audit;
        String path = System.getProperty("user.dir");
        File f = new File(path, "Audit_IRConductAlgorithm5.txt");
        conductAlgorithm5Audit = new FileWriter(f);

        BufferedReader brConductAlgorithm5Input = new BufferedReader(conductAlgorithm5Input);
        brConductAlgorithm5Input.readLine();
        irConductAlgorithm5 = new IR(conductAlgorithm5Input, conductAlgorithm5Audit, brConductAlgorithm5Input);

        irConductAlgorithm5.parseHeader();
        irConductAlgorithm5.processFile();
        irConductAlgorithm5.conductAlgorithm();
    }

    @Test
    public void test_conductAlgorithm_6() throws IOException {
        // test ballots.csv
        FileReader conductAlgorithm6Input = new FileReader((IRtests.class.getResource("ballots.csv")).getFile());

        FileWriter conductAlgorithm6Audit = null;
        String path = System.getProperty("user.dir");
        File f = new File(path, "Audit_IRConductAlgorithm6.txt");
        conductAlgorithm6Audit = new FileWriter(f);

        BufferedReader brConductAlgorithm6Input = new BufferedReader(conductAlgorithm6Input);
        brConductAlgorithm6Input.readLine();
        irConductAlgorithm6 = new IR(conductAlgorithm6Input, conductAlgorithm6Audit, brConductAlgorithm6Input);

        irConductAlgorithm6.parseHeader();
        irConductAlgorithm6.processFile();
        irConductAlgorithm6.conductAlgorithm();
    }


}
