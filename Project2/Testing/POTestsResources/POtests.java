package POTestsResources;

import java.io.*;
import java.util.ArrayList;

import VotingSystem.PO;
import VotingSystem.RunElection;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class POtests {
    RunElection runElection;
    //below are PO objects for unit testing (testing specific functions)
    PO poParse;
    PO poProcess;
    PO poAlgorithm;
    //below are PO objects for tesing special/edge cases
    PO poTwoTie;
    PO poMultiTie;
    PO poAllTie;
    PO poSuperSmall;
    //below are PO objects for testing general scenarios
    PO poBasic;

    @Test
    public void test_parseHeader() throws IOException {
        // Test for the parseHeader() function
        FileReader reader1 = new FileReader((POtests.class.getResource("TestPO.csv")).getFile());
        ArrayList<FileReader> reader1List = new ArrayList<>();
        reader1List.add(reader1);
        BufferedReader br1 = new BufferedReader(reader1);
        br1.readLine();
        poParse = new PO(reader1List, null, br1);
        poParse.parseHeader();
        Assert.assertTrue(  poParse.getCandidates()[0].equals("[Pike, D]") &&
                            poParse.getCandidates()[1].equals("[Foster, R]") &&
                            poParse.getCandidates()[2].equals("[Borg, I]") &&
                            poParse.getVoteTotal() == 10);
    }

    @Test
    public void test_processFile() throws IOException {
        // Test for the processFile() function
        FileReader reader2 = new FileReader((POtests.class.getResource("TestPO.csv")).getFile());
        ArrayList<FileReader> reader2List = new ArrayList<>();
        reader2List.add(reader2);
        BufferedReader br2 = new BufferedReader(reader2);
        br2.readLine();
        poProcess = new PO(reader2List, null, br2);
        poProcess.parseHeader();
        poProcess.processFile();
        Assert.assertTrue(  poProcess.getVotes()[0] == 3 &&
                            poProcess.getVotes()[1] == 3 &&
                            poProcess.getVotes()[2] == 4);
    }

    @Test
    public void test_conductAlgorithm() throws IOException {
        // Test for the conductAlgorithm() function
        FileReader reader3 = new FileReader((POtests.class.getResource("TestPO.csv")).getFile());
        ArrayList<FileReader> reader3List = new ArrayList<>();
        reader3List.add(reader3);
        BufferedReader br3 = new BufferedReader(reader3);
        br3.readLine();
        poAlgorithm = new PO(reader3List, null, br3);
        poAlgorithm.parseHeader();
        poAlgorithm.processFile();
        poAlgorithm.conductAlgorithm();
        Assert.assertTrue( poAlgorithm.getWinner().equals("[Borg, I]"));
    }

    @Test
    public void test_twoTie() throws IOException {
        // Test for when there are two ties
        FileReader reader4 = new FileReader((POtests.class.getResource("TwoTiePO.csv")).getFile());
        ArrayList<FileReader> reader4List = new ArrayList<>();
        reader4List.add(reader4);
        BufferedReader br4 = new BufferedReader(reader4);
        br4.readLine();
        poTwoTie = new PO(reader4List, null, br4);
        poTwoTie.parseHeader();
        poTwoTie.processFile();
        poTwoTie.conductAlgorithm();
        Assert.assertTrue(  poTwoTie.getWinners()[0].equals("[Foster, R]") &&
                            poTwoTie.getWinners()[1].equals("[Borg, I]") &&
                            poTwoTie.getWinners().length == 2 );
    }

    @Test
    public void test_multiTie() throws IOException {
        // Test for when there are multiple ties
        FileReader reader5 = new FileReader((POtests.class.getResource("MultiTiePO.csv")).getFile());
        ArrayList<FileReader> reader5List = new ArrayList<>();
        reader5List.add(reader5);
        BufferedReader br5 = new BufferedReader(reader5);
        br5.readLine();
        poMultiTie = new PO(reader5List, null, br5);
        poMultiTie.parseHeader();
        poMultiTie.processFile();
        poMultiTie.conductAlgorithm();
        Assert.assertTrue(  poMultiTie.getWinners()[0].equals("[Pike, D]") &&
                            poMultiTie.getWinners()[1].equals("[Foster, R]") &&
                            poMultiTie.getWinners()[2].equals("[Borg, I]") &&
                            poMultiTie.getWinners().length == 3 );
    }

    @Test
    public void test_allTie() throws IOException {
        // Test for when everyone is tied
        FileReader reader6 = new FileReader((POtests.class.getResource("AllTiePO.csv")).getFile());
        ArrayList<FileReader> reader6List = new ArrayList<>();
        reader6List.add(reader6);
        BufferedReader br6 = new BufferedReader(reader6);
        br6.readLine();
        poAllTie = new PO(reader6List, null, br6);
        poAllTie.parseHeader();
        poAllTie.processFile();
        poAllTie.conductAlgorithm();
        Assert.assertTrue(  poAllTie.getWinners()[0].equals("[Pike, D]") &&
                            poAllTie.getWinners()[1].equals("[Foster, R]") &&
                            poAllTie.getWinners()[2].equals("[Borg, I]") &&
                            poAllTie.getWinners().length == 3 );
    }

    @Test
    public void test_superSmall_1() throws IOException {
        // Test for when the file only has one vote
        FileReader reader7 = new FileReader((POtests.class.getResource("SuperSmallPO.csv")).getFile());
        ArrayList<FileReader> reader7List = new ArrayList<>();
        reader7List.add(reader7);
        BufferedReader br7 = new BufferedReader(reader7);
        br7.readLine();
        poSuperSmall = new PO(reader7List, null, br7);
        poSuperSmall.parseHeader();
        poSuperSmall.processFile();
        poSuperSmall.conductAlgorithm();
        Assert.assertTrue(  poSuperSmall.getWinners()[0].equals("[Pike, D]") &&
                            poSuperSmall.getWinner().equals("[Pike, D]"));
    }

    @Test
    public void test_superSmall() throws IOException {
        // Test for when the file only has one vote
        FileReader reader8 = new FileReader((POtests.class.getResource("BasicPO.csv")).getFile());
        ArrayList<FileReader> reader8List = new ArrayList<>();
        reader8List.add(reader8);
        BufferedReader br8 = new BufferedReader(reader8);
        br8.readLine();
        poBasic = new PO(reader8List, null, br8);
        poBasic.parseHeader();
        poBasic.processFile();
        poBasic.conductAlgorithm();
        Assert.assertTrue(  poBasic.getWinners()[0].equals("[Pike, D]") &&
                            poBasic.getWinner().equals("[Pike, D]") &&
                            poBasic.getVoteTotal() == 14 &&
                            poBasic.getVotes()[0] == 4 &&
                            poBasic.getVotes()[1] == 2 &&
                            poBasic.getVotes()[2] == 2 &&
                            poBasic.getVotes()[3] == 3 &&
                            poBasic.getVotes()[4] == 3);
    }
}
