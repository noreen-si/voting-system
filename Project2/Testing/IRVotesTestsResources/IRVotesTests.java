package IRVotesTestsResources;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import IRTestsResources.IRtests;
import VotingSystem.Candidate;
import VotingSystem.IR;
import VotingSystem.Node;
import VotingSystem.RunElection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IRVotesTests {
    IR irInitialize;
    String[] candidateNames = {"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)"};
    @Before
    public void setUp() throws IOException {

    }
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
}
