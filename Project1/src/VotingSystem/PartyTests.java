package VotingSystem;

import java.io.*;

import VotingSystem.Party;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class PartyTests {
    Party incrementSeat;
    Party incrementVote;

    @Test
    public void test_incrementSeatCount(){
        incrementSeat = new Party("PartyA");
        incrementSeat.setSeatsReceived(5);
        incrementSeat.incrementSeatCount();
        Assert.assertTrue(incrementSeat.getSeatsReceived() == 6);
    }

    @Test
    public void test_incrementVoteCount(){
        incrementVote = new Party("PartyB");
        incrementVote.setNumVotes(20);
        incrementVote.incrementVoteCount();
        Assert.assertTrue(incrementVote.getNumVotes() == 21);
    }
}