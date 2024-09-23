package VotingSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Assertions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.Buffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import VotingSystem.CPL;
import VotingSystem.IR;
import VotingSystem.RunElection;


// NOTE:
// for all these tests, you must duplicate the corresponding testing file such that it is both under
// the Testing directory AND VotingSystem/src
public class RunElectionTests {

    private RunElection election;
    private File file;
    private FileWriter audit;
    private ArrayList<String> fileList;
    private ArrayList<FileReader> inputList;

    @BeforeEach
    public void setUp() throws IOException {
        URL path = RunElectionTests.class.getResource("RunElectionTest.csv");
        file = new File(path.getFile());
        FileReader fr = new FileReader(file);
        //audit = mock(FileWriter.class);
        fileList = new ArrayList<>();
        fileList.add("RunElectionTest.csv");
                inputList = new ArrayList<>();
        inputList.add(fr);
        election = new RunElection(fileList);
    }

    @Test
    public void testGenerateAuditFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String expectedIRName = "IR" + now.format(formatter) + ".txt";
        String expectedCPLName = "CPL" + now.format(formatter) + ".txt";

        String actualIRName = election.generateAuditFileName("IR");
        String actualCPLName = election.generateAuditFileName("CPL");

        Assertions.assertEquals(expectedIRName, actualIRName);
        Assertions.assertEquals(expectedCPLName, actualCPLName);
    }

    @Test
    public void testParseElectType_IR() throws IOException {
        election.setInput(inputList);
        String actualType = election.parseElectType();
        String expectedType = "IR";
        Assertions.assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseElectType_CPL() throws IOException {
        URL path = RunElectionTests.class.getResource("RunElectionTestCPL.csv");
        File fileCPL= new File(path.getFile());
        FileReader fr = new FileReader(fileCPL);
        ArrayList<FileReader> list = new ArrayList<>();
        list.add(fr);
        ArrayList<String> namesList = new ArrayList<>();
        namesList.add("RunElectionTestCPL.csv");
        RunElection election1 = new RunElection(namesList);
        election1.setInput(list);
        String actualType = election1.parseElectType();
        String expectedType = "CPL";
        Assertions.assertEquals(expectedType, actualType);
    }

    @Test
    public void testRunIR() throws IOException {
        file = new File("RunElectionTests1.csv");
        URL path = RunElectionTests.class.getResource("RunElectionTests1.csv");
        file = new File(path.getFile());
        FileReader fr = new FileReader(file);
        ArrayList<FileReader> list = new ArrayList<>();
        list.add(fr);
        ArrayList<String> namesList = new ArrayList<>();
        namesList.add("RunElectionTests1.csv");
        RunElection election1 = new RunElection(namesList);
        election1.setInput(list);
        BufferedReader br = new BufferedReader(fr);
        election1.setBr(br);
        br.readLine();
        File mockAudit = new File("Audit_RunElectionTests1.csv");
        FileWriter fw = new FileWriter(mockAudit);
        election1.setAudit(fw);
        election1.runIR();
    }

    @Test
    public void testRunCPL() throws IOException {
        URL path = RunElectionTests.class.getResource("RunElectionTest2.csv");
        file = new File(path.getFile());
        FileReader fr = new FileReader(file);
        ArrayList<FileReader> list = new ArrayList<>();
        list.add(fr);
        ArrayList<String> namesList = new ArrayList<>();
        namesList.add("RunElectionTest2.csv");
        RunElection election1 = new RunElection(namesList);
        election1.setInput(list);
        BufferedReader br = new BufferedReader(fr);
        election1.setBr(br);
        br.readLine();
        File mockAudit = new File("Audit_RunElectionTests1.csv");
        FileWriter fw = new FileWriter(mockAudit);
        election1.setAudit(fw);
        election1.runCPL();
    }
}

