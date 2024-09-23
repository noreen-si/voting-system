package VotingSystem;

import java.io.*;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class CPLtests {
    RunElection runElection;
    //below are CPL objects for unit testing (testing specific functions)
    CPL cplParse;
    CPL cplProcess;
    CPL cplAlgorithm;
    CPL cplInitialize;
    CPL cplLotto;
    //below are CPL objects for tesing special/edge cases
    CPL cplTwoTie;
    CPL cplMultiTie;
    CPL cplLottoTie;
    CPL cplFull;
    CPL cplKnockout;
    CPL cplAllTie;
    //below are CPL objects for testing general scenarios
    CPL basic;
    CPL france;

    @Test
    public void test_parseHeader() throws IOException {
        // Test for the parseHeader() function
        FileReader reader1 = new FileReader((CPLtests.class.getResource("basic.csv")).getFile());
        BufferedReader br1 = new BufferedReader(reader1);
        br1.readLine();
        cplParse = new CPL(reader1, null, br1);
        cplParse.parseHeader();
        Assert.assertTrue(  cplParse.getParties()[0].getName().equals("PartyA") &&
                            cplParse.getParties()[1].getName().equals("PartyB") &&
                            cplParse.getParties()[2].getName().equals("PartyC") &&
                            cplParse.getTotalSeats() == 1 &&
                            cplParse.getVoteTotal() == 6 &&
                            cplParse.getParties()[0].getCandidates()[0].equals("Bob") &&
                            cplParse.getParties()[1].getCandidates()[0].equals("Mike") &&
                            cplParse.getParties()[2].getCandidates()[0].equals("Sam"));
    }

    @Test
    public void test_processFile() throws IOException {
        // Test for the processFile() function
        FileReader reader2 = new FileReader((CPLtests.class.getResource("basic.csv")).getFile());
        BufferedReader br2 = new BufferedReader(reader2);
        br2.readLine();
        cplProcess = new CPL(reader2, null, br2);
        cplProcess.parseHeader();
        cplProcess.processFile();
        Assert.assertTrue(  cplProcess.getParties()[0].getNumVotes() == 1 && 
                            cplProcess.getParties()[1].getNumVotes() == 4 && 
                            cplProcess.getParties()[2].getNumVotes() == 1 );
    }

    @Test
    public void test_processFile_2() throws IOException {
        // Test for the conductAlgorithm() function
        FileReader reader3 = new FileReader((CPLtests.class.getResource("basic.csv")).getFile());
        BufferedReader br3 = new BufferedReader(reader3);
        br3.readLine();
        cplAlgorithm = new CPL(reader3, null, br3);
        cplAlgorithm.parseHeader();
        cplAlgorithm.processFile();
        cplAlgorithm.conductAlgorithm();
        Assert.assertTrue(  cplAlgorithm.getParties()[0].getSeatsReceived() == 0 && 
                            cplAlgorithm.getParties()[1].getSeatsReceived() == 1 && 
                            cplAlgorithm.getParties()[2].getSeatsReceived() == 0 );
    }

    @Test
    public void test_initializeParty() throws IOException {
        // Test for the initializeParty() function
        FileReader reader4 = new FileReader((CPLtests.class.getResource("basic.csv")).getFile());
        BufferedReader br4 = new BufferedReader(reader4);
        br4.readLine();
        cplInitialize = new CPL(reader4, null, br4);
        cplInitialize.parseHeader();
        cplInitialize.initializeParty(new String[]{"PartyD", "PartyE", "PartyF"});
        Assert.assertTrue(  cplInitialize.getParties()[0].getName().equals("PartyD") &&
                            cplInitialize.getParties()[1].getName().equals("PartyE") &&
                            cplInitialize.getParties()[2].getName().equals("PartyF"));
    }

    @Test
    public void test_drawLotto() throws IOException {
        // Test for the drawLotto() function
        // This occurs during a special case where a party earns more seats than it has candidates
        FileReader reader5 = new FileReader((CPLtests.class.getResource("lottoCase.csv")).getFile());
        BufferedReader br5 = new BufferedReader(reader5);
        br5.readLine();
        cplLotto = new CPL(reader5, null, br5);
        cplLotto.parseHeader();
        cplLotto.processFile();
        cplLotto.conductAlgorithm();
        Assert.assertTrue(  cplLotto.getParties()[0].getSeatsReceived() == 1 && 
                            (cplLotto.getParties()[1].getSeatsReceived() == 1 || cplLotto.getParties()[2].getSeatsReceived() == 1));
    }

    @Test
    public void test_break_two_tie() throws IOException {
        // Test for the breakTie() function (or actually the case where its invoked from the parent class)
        // This occurs during a special case where two or more parties have a tie in the second round of seat allocation
        FileReader reader6 = new FileReader((CPLtests.class.getResource("twoTieCase.csv")).getFile());
        BufferedReader br6 = new BufferedReader(reader6);
        br6.readLine();
        cplTwoTie = new CPL(reader6, null, br6);
        cplTwoTie.parseHeader();
        cplTwoTie.processFile();
        cplTwoTie.conductAlgorithm();
        Assert.assertTrue(  cplTwoTie.getParties()[0].getSeatsReceived() == 1 && 
                            (cplTwoTie.getParties()[1].getSeatsReceived() == 1 || cplTwoTie.getParties()[2].getSeatsReceived() == 1));
    }

    @Test
    public void test_break_multi_tie() throws IOException {
        // Test for the breakTie() function (or actually the case where its invoked from the parent class)
        // This occurs during a special case where two or more parties have a tie in the second round of seat allocation
        FileReader reader7 = new FileReader((CPLtests.class.getResource("multiTieCase.csv")).getFile());
        BufferedReader br7 = new BufferedReader(reader7);
        br7.readLine();
        cplMultiTie = new CPL(reader7, null, br7);
        cplMultiTie.parseHeader();
        cplMultiTie.processFile();
        cplMultiTie.conductAlgorithm();
        Assert.assertTrue(  cplMultiTie.getParties()[0].getSeatsReceived() == 1 && 
                            (cplMultiTie.getParties()[1].getSeatsReceived() == 1 || cplMultiTie.getParties()[2].getSeatsReceived() == 1 || cplMultiTie.getParties()[3].getSeatsReceived() == 1));
    }

    @Test
    public void test_break_lotto_tie() throws IOException {
        // Tests the case when there are ties and lottos in the same election
        FileReader reader8 = new FileReader((CPLtests.class.getResource("lottoTieCase.csv")).getFile());
        BufferedReader br8 = new BufferedReader(reader8);
        br8.readLine();
        cplLottoTie = new CPL(reader8, null, br8);
        cplLottoTie.parseHeader();
        cplLottoTie.processFile();
        cplLottoTie.conductAlgorithm();
        Assert.assertTrue(  cplLottoTie.getParties()[0].getSeatsReceived() == 1 && 
                            ((cplLottoTie.getParties()[1].getSeatsReceived() == 2 && cplLottoTie.getParties()[2].getSeatsReceived() == 0) ||
                             (cplLottoTie.getParties()[1].getSeatsReceived() == 1 && cplLottoTie.getParties()[2].getSeatsReceived() == 1) ||
                             (cplLottoTie.getParties()[1].getSeatsReceived() == 0 && cplLottoTie.getParties()[2].getSeatsReceived() == 2) )
                         );
    }

    @Test
    public void test_full() throws IOException {
        // Tests the case when there are exactly the same number of seats as there are total candidates
        // This is the "everyone wins regarless of votes" case
        FileReader reader9 = new FileReader((CPLtests.class.getResource("fullCase.csv")).getFile());
        BufferedReader br9 = new BufferedReader(reader9);
        br9.readLine();
        cplFull = new CPL(reader9, null, br9);
        cplFull.parseHeader();
        cplFull.processFile();
        cplFull.conductAlgorithm();
        Assert.assertTrue(  cplFull.getParties()[0].getSeatsReceived() == 1 && 
                            cplFull.getParties()[1].getSeatsReceived() == 1 && 
                            cplFull.getParties()[2].getSeatsReceived() == 1 );
    }

    @Test
    public void test_knockout() throws IOException {
        // Tests the case when every single ballot goes to the same party; a Knockout election
        FileReader reader10 = new FileReader((CPLtests.class.getResource("knockoutCase.csv")).getFile());
        BufferedReader br10 = new BufferedReader(reader10);
        br10.readLine();
        cplKnockout = new CPL(reader10, null, br10);
        cplKnockout.parseHeader();
        cplKnockout.processFile();
        cplKnockout.conductAlgorithm();
        Assert.assertTrue(  cplKnockout.getParties()[0].getSeatsReceived() == 2 && 
                            cplKnockout.getParties()[1].getSeatsReceived() == 0 && 
                            cplKnockout.getParties()[2].getSeatsReceived() == 0 );
    }

    @Test
    public void test_all_tie() throws IOException {
        // Tests the case when everyone has the same amount of votes
        FileReader reader11 = new FileReader((CPLtests.class.getResource("allTieCase.csv")).getFile());
        BufferedReader br11 = new BufferedReader(reader11);
        br11.readLine();
        cplAllTie = new CPL(reader11, null, br11);
        cplAllTie.parseHeader();
        cplAllTie.processFile();
        cplAllTie.conductAlgorithm();
        Assert.assertTrue(  cplAllTie.getParties()[0].getSeatsReceived() + cplAllTie.getParties()[1].getSeatsReceived() + cplAllTie.getParties()[2].getSeatsReceived() == 4 &&
                            cplAllTie.getParties()[0].getSeatsReceived() >= 1 &&
                            cplAllTie.getParties()[1].getSeatsReceived() >= 1 &&
                            cplAllTie.getParties()[2].getSeatsReceived() >= 1
                         );
    }


    @Test
    public void test_basic_file() throws IOException {
        // Not a particular unit test. Simply a general test to see if everything works
        // on a very basic and small file without any super special cases
        FileReader reader12 = new FileReader((CPLtests.class.getResource("basic.csv")).getFile());
        BufferedReader br12 = new BufferedReader(reader12);
        br12.readLine();
        basic = new CPL(reader12, null, br12);
        basic.parseHeader();
        basic.processFile();
        basic.conductAlgorithm();
        Assert.assertTrue(  basic.getParties()[0].getName().equals("PartyA") &&
                            basic.getParties()[1].getName().equals("PartyB") &&
                            basic.getParties()[2].getName().equals("PartyC") &&
                            basic.getTotalSeats() == 0 &&
                            basic.getVoteTotal() == 6 &&
                            basic.getParties()[0].getCandidates()[0].equals("Bob") &&
                            basic.getParties()[1].getSeatsReceived() == 1);
    }

    @Test
    public void test_france() throws IOException {
        // Scenario depicting a french election
        FileReader reader13 = new FileReader((CPLtests.class.getResource("france.csv")).getFile());
        BufferedReader br13 = new BufferedReader(reader13);
        br13.readLine();
        france = new CPL(reader13, null, br13);
        france.parseHeader();
        france.processFile();
        france.conductAlgorithm();
        Assert.assertTrue(  france.getParties()[0].getName().equals("La France Insoumise") &&
                            france.getParties()[1].getName().equals("Les Verts") &&
                            france.getParties()[2].getName().equals("En Marche") &&
                            france.getParties()[3].getName().equals("Les Republicains") &&
                            france.getParties()[4].getName().equals("Rassemblement National") &&
                            france.getTotalSeats() == 0 &&
                            france.getVoteTotal() == 25 &&
                            france.getParties()[0].getCandidates()[0].equals("Melenchon") &&
                            france.getParties()[2].getCandidates()[0].equals("Macron") &&
                            france.getParties()[2].getCandidates()[2].equals("Lagarde") &&
                            france.getParties()[2].getSeatsReceived() >= 1 &&
                            (france.getParties()[0].getSeatsReceived() == 1 || france.getParties()[4].getSeatsReceived() == 1)
                         );
    }

}
