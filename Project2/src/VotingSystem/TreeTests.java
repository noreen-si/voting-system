package VotingSystem;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import VotingSystem.Tree;

public class TreeTests {
    private Tree tree;

    @Before
    public void setUp() {
        tree = new Tree(0, 3); // initialize tree with root index 0 and 3 candidates
    }

    @Test
    public void testInsert() {
        ArrayList<Integer> ballot1 = new ArrayList<Integer>();
        ballot1.add(1);
        ballot1.add(2);
        ballot1.add(3);
        tree.insert(ballot1);

        ArrayList<Integer> ballot2 = new ArrayList<Integer>();
        ballot2.add(1);
        ballot2.add(3);
        ballot2.add(2);
        tree.insert(ballot2);

        ArrayList<ArrayList<Integer>> ballots = tree.getBallots(tree.getRoot());
        Assert.assertEquals(2, ballots.size());

        ArrayList<Integer> expected1 = new ArrayList<Integer>();
        expected1.add(0);
        expected1.add(1);
        expected1.add(2);

        ArrayList<Integer> expected2 = new ArrayList<Integer>();
        expected2.add(0);
        expected2.add(2);
        expected2.add(1);

        Assert.assertTrue(ballots.contains(expected1));
        Assert.assertTrue(ballots.contains(expected2));
    }

    @Test
    public void testGetNodes() {
        ArrayList<Integer> ballot1 = new ArrayList<Integer>();
        ballot1.add(1);
        ballot1.add(2);
        ballot1.add(3);
        tree.insert(ballot1);

        ArrayList<Integer> ballot2 = new ArrayList<Integer>();
        ballot2.add(1);
        ballot2.add(3);
        ballot2.add(2);
        tree.insert(ballot2);

        ArrayList<ArrayList<Integer>> nodes = tree.getNodes(tree.getRoot());
        Assert.assertEquals(2, nodes.size());

        ArrayList<Integer> expected1 = new ArrayList<Integer>();
        expected1.add(1);
        expected1.add(2);
        expected1.add(3);
        expected1.add(1);

        ArrayList<Integer> expected2 = new ArrayList<Integer>();
        expected2.add(1);
        expected2.add(3);
        expected2.add(2);
        expected2.add(1);

        Assert.assertTrue(nodes.contains(expected1));
        Assert.assertTrue(nodes.contains(expected2));
    }
}

