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
import java.net.URL;
import java.nio.Buffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import VotingSystem.CPL;
import VotingSystem.IR;
import VotingSystem.RunElection;

public class RunElectionTests {

    private RunElection election;
    private File file;
    private FileWriter audit;

    @BeforeEach
    public void setUp() throws IOException {
        file = new File("RunElectionTest.csv");
        audit = mock(FileWriter.class);
        election = new RunElection(file.getName());
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
        election.setInput(new FileReader(file));
        String actualType = election.parseElectType();
        String expectedType = "IR";
        Assertions.assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseElectType_CPL() throws IOException {
        File f = new File("RunElectionTestCPL.csv");
        election.setInput(new FileReader(f));
        String actualType = election.parseElectType();
        String expectedType = "CPL";
        Assertions.assertEquals(expectedType, actualType);
    }

    @Test
    public void testRunIR() throws IOException {
        IR ir = mock(IR.class);
        file = new File("RunElectionTests1.csv");
        election.setInput(new FileReader(file));
        BufferedReader br = new BufferedReader(new FileReader(file));
        election.setBr(br);
        br.readLine();
        File mockAudit = new File("Audit_RunElectionTests1.csv");
        FileWriter fw = new FileWriter(mockAudit);
        election.setAudit(fw);
        election.runIR();
    }

    @Test
    public void testRunCPL() throws IOException {
        CPL cpl = mock(CPL.class);
        file = new File("RunElectionTest2.csv");
        election.setInput(new FileReader(file));
        BufferedReader br = new BufferedReader(new FileReader(file));
        election.setBr(br);
        br.readLine();
        File mockAudit = new File("Audit_RunElectionTest2.csv");
        FileWriter fw = new FileWriter(mockAudit);
        election.setAudit(fw);
        election.runCPL();
    }

    @Test
    public void testStartWithValidFile() throws IOException {
        // Please be certain that your user directory set up is properly configured BEFORE running this test
        // If it is not properly configured, this test does not work as expected -- otherwise it works properly
        // Additionally please put testStartWithValidFile.csv in the same directory as RunElection
        // (Under src/VotingSystem) BEFORE running this test
        file = new File("testStartWithValidFile.csv");
        election = new RunElection(file.getName());
        election.setInput(new FileReader(file));
        File mockAudit = new File("Audit_testStartWithValidFile.csv");
        FileWriter fw = new FileWriter(mockAudit);
        election.setAudit(fw);
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        election.setBr(br);
        RunElection spyElection = org.mockito.Mockito.spy(election);

        spyElection.start();

        org.mockito.Mockito.verify(spyElection).runIR();
    }

    @Test
    public void testStartWithInvalidFile() throws IOException {
        election = new RunElection("invalid.csv");
        RunElection spyElection = org.mockito.Mockito.spy(election);

        Assertions.assertThrows(NullPointerException.class, () -> {
            spyElection.start();
        });
    }
}

