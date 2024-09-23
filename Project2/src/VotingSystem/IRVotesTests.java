package VotingSystem;

import java.io.*;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class IRVotesTests {
    IR irInitialize;
    String[] candidateNames = {"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)"};
    @Test
    public void test_initializeCandidates_VoteTotals() throws IOException{
        irInitialize = new IR(null, null, null);
        irInitialize.setCandidates(new Candidate[4]);
        irInitialize.initializeCandidates(candidateNames);
        Assert.assertTrue(irInitialize.getCandidates()[0].getVoteInfoPerRound().get(0).equals("Rosen"));
        Assert.assertTrue(irInitialize.getCandidates()[0].getVoteInfoPerRound().get(1).equals("(D)"));
        Assert.assertTrue(irInitialize.getCandidates()[1].getVoteInfoPerRound().get(0).equals("Kleinberg"));
        Assert.assertTrue(irInitialize.getCandidates()[1].getVoteInfoPerRound().get(1).equals("(R)"));
        Assert.assertTrue(irInitialize.getCandidates()[2].getVoteInfoPerRound().get(0).equals("Chou"));
        Assert.assertTrue(irInitialize.getCandidates()[2].getVoteInfoPerRound().get(1).equals("(I)"));
        Assert.assertTrue(irInitialize.getCandidates()[3].getVoteInfoPerRound().get(0).equals("Royce"));
        Assert.assertTrue(irInitialize.getCandidates()[3].getVoteInfoPerRound().get(1).equals("(L)"));
    }

    @Test
    public void test_updateVotes_firstRound() throws IOException {
        // Test that the vote tracking variables are properly updated per round
        FileReader majority1Input = new FileReader((IRVotesTests.class.getResource("IRMajority1.csv")).getFile());
        BufferedReader brMajority1Input = new BufferedReader(majority1Input);
        brMajority1Input.readLine();
        ArrayList<FileReader> majority1InputList = new ArrayList<>();
        majority1InputList.add(majority1Input);
        IR irVoteDifference1 = new IR(majority1InputList, null, brMajority1Input);
        irVoteDifference1.parseHeader();
        irVoteDifference1.processFile();
        irVoteDifference1.updateVoteDifferenceAfterRound();
        // get each candidate's vote information
        ArrayList<String> AVoteInfo = irVoteDifference1.getCandidates()[0].getVoteInfoPerRound();
        ArrayList<String> BVoteInfo = irVoteDifference1.getCandidates()[1].getVoteInfoPerRound();

        Assert.assertEquals("4", AVoteInfo.get(2));
        Assert.assertEquals("+4", AVoteInfo.get(3));
        Assert.assertEquals("2", BVoteInfo.get(2));
        Assert.assertEquals("+2", BVoteInfo.get(3));
        Assert.assertEquals("6", irVoteDifference1.getTotals().get(2));
        Assert.assertEquals("+6", irVoteDifference1.getTotals().get(3));
    }

    @Test
    public void test_updateVotes_withEliminations() throws IOException {
        FileReader conductAlgorithm2Input = new FileReader((IRtests.class.getResource("IRConductAlgorithm2.csv")).getFile());
        BufferedReader brConductAlgorithm2Input = new BufferedReader(conductAlgorithm2Input);
        brConductAlgorithm2Input.readLine();
        ArrayList<FileReader> conductAlgorithm2InputList = new ArrayList<>();
        conductAlgorithm2InputList.add(conductAlgorithm2Input);
        IR updateVotes_withElimination = new IR(conductAlgorithm2InputList, null, brConductAlgorithm2Input);
        updateVotes_withElimination.parseHeader();
        updateVotes_withElimination.processFile();
        updateVotes_withElimination.updateVoteDifferenceAfterRound();
        // get each candidate's vote information list
        ArrayList<String> AVoteInfo = updateVotes_withElimination.getCandidates()[0].getVoteInfoPerRound();
        ArrayList<String> BVoteInfo = updateVotes_withElimination.getCandidates()[1].getVoteInfoPerRound();
        ArrayList<String> CVoteInfo = updateVotes_withElimination.getCandidates()[2].getVoteInfoPerRound();

        Assert.assertEquals(AVoteInfo.get(2), "3");
        Assert.assertEquals(AVoteInfo.get(3), "+3");
        Assert.assertEquals(BVoteInfo.get(2), "4");
        Assert.assertEquals(BVoteInfo.get(3), "+4");
        Assert.assertEquals(CVoteInfo.get(2), "2");
        Assert.assertEquals(CVoteInfo.get(3), "+2");
        Assert.assertEquals(updateVotes_withElimination.getTotals().get(2), "9");
        Assert.assertEquals(updateVotes_withElimination.getTotals().get(3), "+9");

        // ELIMINATE a candidate and check that each candidate's vote info, elimination properly updated
        // eliminate C --> Candidate B will gain 2 votes.
        updateVotes_withElimination.eliminateCandidate(2);
        updateVotes_withElimination.incrementTotalRounds();
        updateVotes_withElimination.updateVoteDifferenceAfterRound();

        // Assert statements
        Assert.assertEquals("3", AVoteInfo.get(4));
        Assert.assertEquals("+0", AVoteInfo.get(5));
        Assert.assertEquals("6", BVoteInfo.get(4));
        Assert.assertEquals("+2", BVoteInfo.get(5));
        // IMPORTANT: C should now have 0 votes, lost -2 votes.
        Assert.assertEquals("0", CVoteInfo.get(4));
        Assert.assertEquals("-2", CVoteInfo.get(5));
    }

    @Test
    public void test_exhaustedBallotsTracking() throws IOException {
        // Test that makes sure that exhausted ballots are properly tracked.
        FileReader exhaustedInput = new FileReader((IRtests.class.getResource("IRTrackExhaustedVotes.csv")).getFile());
        BufferedReader brConductAlgorithm2Input = new BufferedReader(exhaustedInput);
        brConductAlgorithm2Input.readLine();
        ArrayList<FileReader> exhaustedInputList = new ArrayList<>();
        exhaustedInputList.add(exhaustedInput);
        IR updateVotes_Exhausted = new IR(exhaustedInputList, null, brConductAlgorithm2Input);

        updateVotes_Exhausted.parseHeader();
        updateVotes_Exhausted.processFile();
        updateVotes_Exhausted.updateVoteDifferenceAfterRound();
        // Eliminate 2/3 candidates (A and B) for the purpose of tracking exhausted ballots
        updateVotes_Exhausted.eliminateCandidate(0);
        updateVotes_Exhausted.incrementTotalRounds();
        updateVotes_Exhausted.updateVoteDifferenceAfterRound();

        updateVotes_Exhausted.eliminateCandidate(1);
        updateVotes_Exhausted.incrementTotalRounds();
        updateVotes_Exhausted.updateVoteDifferenceAfterRound();

        ArrayList<String> exhaustedBallots = updateVotes_Exhausted.getTotalExhausted();

        Assert.assertEquals("0", exhaustedBallots.get(2));
        Assert.assertEquals("+0", exhaustedBallots.get(3));
        Assert.assertEquals("3", exhaustedBallots.get(4));
        Assert.assertEquals("+3", exhaustedBallots.get(5));
        Assert.assertEquals("6", exhaustedBallots.get(6));
        Assert.assertEquals("+3", exhaustedBallots.get(7));
    }

    @Test
    public void test_totals() throws IOException {
        // Test that the vote tracking variables are properly updated per round
        FileReader majority1Input = new FileReader((IRVotesTests.class.getResource("IRMajority1.csv")).getFile());
        BufferedReader brMajority1Input = new BufferedReader(majority1Input);
        brMajority1Input.readLine();
        ArrayList<FileReader> majority1InputList = new ArrayList<>();
        majority1InputList.add(majority1Input);
        IR irVoteDifference1 = new IR(majority1InputList, null, brMajority1Input);
        irVoteDifference1.parseHeader();
        irVoteDifference1.processFile();
        irVoteDifference1.updateVoteDifferenceAfterRound();
        // get the totals
        ArrayList<String> totals = irVoteDifference1.getTotals();

        Assert.assertEquals("TOTALS", totals.get(0));
        Assert.assertEquals("---", totals.get(1));
        Assert.assertEquals("6", totals.get(2));
        Assert.assertEquals("+6", totals.get(3));
    }

}
