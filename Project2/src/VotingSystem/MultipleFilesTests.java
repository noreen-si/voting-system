package VotingSystem;

import org.junit.Assert;
import org.junit.Test;

import VotingSystem.RunElection;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MultipleFilesTests {
    private final InputStream originalIn = System.in;

    @AfterEach
    public void tearDown() {
        // Restore the original input stream after each test
        System.setIn(originalIn);
    }
    @Test
    public void pass_multipleIR_files() {
        String[] files = {"IRTestBallot.csv", "IRTestBallot2.csv","IRTestBallot3.csv","IRTestBallot4.csv","IRTestBallot5.csv","IRTestBallot6.csv"};
        ArrayList<String> fileNames = new ArrayList<String>();
        String file = "";
        for(int i = 0; i < files.length; i++){
            file = files[i];
            fileNames.add(file);
        }
        RunElection runElection = new RunElection(fileNames);
        String simulatedInput = "P";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        runElection.start();
        String s = runElection.toString();
        String expected = "Number of Candidates: 2\n" +
                "Number of Ballots: 314938\n" +
                "Total Files read: 6\n";
        Assert.assertEquals(expected, s);
    }
    @Test
    public void pass_multipleCPL_files() {
        String[] files = {"CPLTestBallot.csv", "CPLTestBallot2.csv","CPLTestBallot3.csv","CPLTestBallot4.csv","CPLTestBallot5.csv"};
        ArrayList<String> fileNames = new ArrayList<String>();
        String file = "";
        for(int i = 0; i < files.length; i++){
            file = files[i];
            fileNames.add(file);
        }
        RunElection runElection = new RunElection(fileNames);
        String simulatedInput = "P";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        runElection.start();
        String s = runElection.toString();
        String expected = "Total Votes in election: 379696\n" +
                "Total files used: 5\n" +
                "Total Seats available: 18\n";
        Assert.assertEquals(expected, s);
    }
    @Test
    public void pass_multiplePO_files() {
        String[] files = {"POTestBallot1.csv", "POTestBallot2.csv","POTestBallot3.csv","POTestBallot4.csv","POTestBallot5.csv"};
        ArrayList<String> fileNames = new ArrayList<String>();
        String file = "";
        for(int i = 0; i < files.length; i++){
            file = files[i];
            fileNames.add(file);
        }
        RunElection runElection = new RunElection(fileNames);
        String simulatedInput = "P";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        runElection.start();
        String s = runElection.toString();
        String expected = "Total Votes in election: 800603\n" +
                "Total files used: 5\n";
        Assert.assertEquals(expected, s);
    }

}
