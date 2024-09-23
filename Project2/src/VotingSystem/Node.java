package VotingSystem;

import java.util.ArrayList;

/**
 * This represents a single Node (a candidate) in a ballot tree
 * We can use this to access the children (further rankings along the ballot)
 * @author Jonathan Haak
 */
public class Node {
    protected int numVotes;
    protected Node [] children;
    protected int curDepth;
    protected int index;
    protected Node parent;
    protected boolean hasChildren = false;
    protected ArrayList<Integer> ballot = null;

    /**
     * Getter for the children nodes
     * @return a Node[] that is the children of the node
     */
    // Testing purposes ONLY
    public Node[] getChildren() {
        return this.children;
    }
}
