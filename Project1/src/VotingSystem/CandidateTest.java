package VotingSystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import VotingSystem.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CandidateTest {
    Candidate c;
    Tree cTree;
    @BeforeEach
    public void setUp() {
        cTree = new Tree(0,2);
        c = new Candidate("CandidateC", "(C)", cTree);
        ArrayList<Integer> sampleBallot = new ArrayList<>();
        sampleBallot.add(1);
        sampleBallot.add(0);
        cTree.insert(sampleBallot);
    }
    @Test
    void getName() {
        Assert.assertTrue(c.getName().equals("CandidateC"));
    }

    @Test
    void getParty() {
        Assert.assertTrue(c.getParty().equals("(C)"));
    }

    @Test
    void getNumVotes() {
        Assert.assertTrue(c.getNumVotes() == 1);
    }

    @Test
    void isEliminated() {
        Assert.assertTrue(!c.isEliminated());
    }

    @Test
    void setEliminated() {
        c.setEliminated(true);
        Assert.assertTrue(c.isEliminated());
    }

    @Test
    void getBallots() {
        Assert.assertTrue(c.getBallots() == cTree);
    }
}