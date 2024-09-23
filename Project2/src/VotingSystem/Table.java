package VotingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * This is a class that will represent our IR table.
 * When an instance of Table is created with information from IR, a table
 * that includes all the IR round information will appear on the user's screen.
 * @author Noreen Si
 */
public class Table {
    JFrame f;

    /**
     * @param candidateInfo An ArrayList that has all the candidates' lists with the vote information per round.
     * @param exhaustedPile An ArrayList that represents the exhausted ballots and totals per round.
     * @param total An ArrayList that represents the total votes gained in the first round.
     * @param totalRounds An int that represents the total number of rounds for an IR election.
     */
    public Table(ArrayList<ArrayList<String>> candidateInfo, ArrayList<String> exhaustedPile, ArrayList<String> total, int totalRounds) {
        // Frame initialization
        f = new JFrame();
        // Frame Title
        f.setTitle("IR Votes Results");
        // Column Names --
        // there will be 2 * totalRounds + 2 columns
        // First two are for Candidate, Party
        // The 2 multiplier represents how there's one column for total votes and another for +- votes in a round
        String[] columnNames = generateColumnNames(2 * totalRounds + 2);
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable j = new JTable(model);

        // Insert candidate rows into table
        int i;
        for (i = 0; i < candidateInfo.size(); i++) {
            model.addRow(candidateInfo.get(i).toArray());
        }
        // Insert exhausted row into table
        model.addRow(exhaustedPile.toArray());
        // Insert total row into table
        model.addRow(total.toArray());
        // make some table settings
        j.setBounds(60, 80, 400, 600);
        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(j);
        f.add(sp);
        // Frame Size
        f.setSize(500, 200);
        // Table will show up
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * @param totalColumns an int that represents the total number of columns for the table.
     * @return columnNames which is a String[] that represents the column names.
     */
    public static String[] generateColumnNames(int totalColumns) {
        // name party r1votes += r2votes += r3votes += r4votes += etc.
        String[] columnNames = new String[totalColumns];
        columnNames[0] = "Candidate";
        columnNames[1] = "Party";
        int i;
        int round = 1;
        for (i = 2; i < totalColumns; i+=2) {
            columnNames[i] = "R" + round + " votes";
            columnNames[i+1] = "R" + round + " +/-";
            round++;
        }
        return columnNames;
    }
}
