package VotingSystem;

import VotingSystem.IR;
import VotingSystem.IRtests;
import VotingSystem.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

public class TableTests {
    public static void test_tableDisplay_oneRound() {
        // Test table display with 3 sample candidate info.
        // Will have exhausted pile and total row.
        // Test with only one round.
        ArrayList<ArrayList<String>> candidateInfo = new ArrayList<ArrayList<String>>();

        String[] candidateA = {"CandidateA", "(A)", "100", "+100"};
        String[] candidateB = {"CandidateB", "(B)", "200", "+200"};
        String[] candidateC = {"CandidateC", "(C)", "250", "+250"};

        ArrayList<String> candidateAList = new ArrayList<>();
        Collections.addAll(candidateAList, candidateA);
        ArrayList<String> candidateBList = new ArrayList<>();
        Collections.addAll(candidateBList, candidateB);
        ArrayList<String> candidateCList = new ArrayList<>();
        Collections.addAll(candidateCList, candidateC);

        // add to giant candidate list
        candidateInfo.add(candidateAList);
        candidateInfo.add(candidateBList);
        candidateInfo.add(candidateCList);

        // Make the exhausted ballots (doesn't make sense in this election but
        // add it just to test out the display)
        String[] exhausted = {"EXHAUSTED PILE", "---", "12345", "+12345"};
        ArrayList<String> exhaustedPile = new ArrayList<>();
        Collections.addAll(exhaustedPile, exhausted);
        // Make totals
        String[] total = {"TOTAL", "---", "8960", "+8960"};
        ArrayList<String> totals = new ArrayList<>();
        Collections.addAll(totals, total);

        new Table(candidateInfo, exhaustedPile, totals, 1);
    }
    public static void test_tableDisplay() {
        // Test table display with 3 sample candidate info.
        // Will have exhausted pile and total row.
        ArrayList<ArrayList<String>> candidateInfo = new ArrayList<ArrayList<String>>();

        String[] candidateA = {"CandidateA", "(A)", "100", "+100", "0", "-100"};
        String[] candidateB = {"CandidateB", "(B)", "200", "+200", "250", "+50"};
        String[] candidateC = {"CandidateC", "(C)", "250", "+250", "250", "+50"};

        ArrayList<String> candidateAList = new ArrayList<>();
        Collections.addAll(candidateAList, candidateA);
        ArrayList<String> candidateBList = new ArrayList<>();
        Collections.addAll(candidateBList, candidateB);
        ArrayList<String> candidateCList = new ArrayList<>();
        Collections.addAll(candidateCList, candidateC);

        // add to giant candidate list
        candidateInfo.add(candidateAList);
        candidateInfo.add(candidateBList);
        candidateInfo.add(candidateCList);

        // Make the exhausted ballots (doesn't make sense in this election but
        // add it just to test out the display)
        String[] exhausted = {"EXHAUSTED PILE", "---", "12345", "+13245", "111", "-11111"};
        ArrayList<String> exhaustedPile = new ArrayList<>();
        Collections.addAll(exhaustedPile, exhausted);
        // Make totals
        String[] total = {"TOTAL", "---", "8960", "+8960"};
        ArrayList<String> totals = new ArrayList<>();
        Collections.addAll(totals, total);

        new Table(candidateInfo, exhaustedPile, totals, 2);
    }

    public static void test_tableDisplay_mutipleEmptyCells() {
        // Test table display with 3 sample candidate info.
        // Will have exhausted pile and total row.
        ArrayList<ArrayList<String>> candidateInfo = new ArrayList<ArrayList<String>>();

        String[] candidateA = {"CandidateA", "(A)", "100", "+100", "0", "-100"};
        String[] candidateB = {"CandidateB", "(B)", "200", "+200"};
        String[] candidateC = {"CandidateC", "(C)", "250", "+250"};

        ArrayList<String> candidateAList = new ArrayList<>();
        Collections.addAll(candidateAList, candidateA);
        ArrayList<String> candidateBList = new ArrayList<>();
        Collections.addAll(candidateBList, candidateB);
        ArrayList<String> candidateCList = new ArrayList<>();
        Collections.addAll(candidateCList, candidateC);

        // add to giant candidate list
        candidateInfo.add(candidateAList);
        candidateInfo.add(candidateBList);
        candidateInfo.add(candidateCList);

        // Make the exhausted ballots (doesn't make sense in this election but
        // add it just to test out the display)
        String[] exhausted = {"EXHAUSTED PILE", "---", "12345", "+13245", "111", "-11111"};
        ArrayList<String> exhaustedPile = new ArrayList<>();
        Collections.addAll(exhaustedPile, exhausted);
        // Make totals
        String[] total = {"TOTAL", "---", "8960", "+8960"};
        ArrayList<String> totals = new ArrayList<>();
        Collections.addAll(totals, total);

        new Table(candidateInfo, exhaustedPile, totals, 2);
    }

    public static void test_tableIntegration_1() throws IOException {
        FileReader tableIntegration1Input = new FileReader((IRtests.class.getResource("ballots.csv")).getFile());

        FileWriter tableIntegration1Audit = null;
        String path = System.getProperty("user.dir");
        File f = new File(path, "Audit_tableIntegration1Input.txt");
        tableIntegration1Audit = new FileWriter(f);

        BufferedReader brTableIntegration1Input = new BufferedReader(tableIntegration1Input);
        brTableIntegration1Input.readLine();
        ArrayList<FileReader> tableIntegration1InputList = new ArrayList<>();
        tableIntegration1InputList.add(tableIntegration1Input);
        IR tableIntegration1 = new IR(tableIntegration1InputList, tableIntegration1Audit, brTableIntegration1Input);

        tableIntegration1.parseHeader();
        tableIntegration1.processFile();
        tableIntegration1.conductAlgorithm();
    }

    public static void main(String[] args) throws IOException {
        test_tableDisplay();
        test_tableDisplay_oneRound();
        test_tableDisplay_mutipleEmptyCells();
        test_tableIntegration_1();
    }
}
