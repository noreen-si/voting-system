package VotingSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
* Class meant to handle information before running the actual elections. Establishes what elections takes place, and
* establishes the name of the input and output (audit) files.
* @author Jonathan Haak
* @author Noreen Si
*/
public class RunElection {
    public String filename;
    private FileReader input;
    private FileWriter audit;
    private BufferedReader br;

    /**
     * Contructor for this class. The input can be null, as it will later be filled by the user in the start() function.
     * @param filename String that represents the name of the file that contains the voting information.
     */
    public RunElection(String filename){
        this.filename = filename;
    }

    /**
     * Controller function that begins the program. Prompts the user for the name of the ballots file, and proceeds with
     * creating the audit file, initializes the proper election class and runs it. Invoked in Main.java
     */
    public void start(){
        boolean validFile = false;
        while (!validFile) {
            validFile = true;
            if(filename == null){
                promptUser("Enter the name of the file containing the election data: ");
                Scanner sc = new Scanner(System.in);

                filename = sc.nextLine();
            }
            try {
                URL path = RunElection.class.getResource(filename);
                File f = new File(path.getFile());
                input = new FileReader(f);
            } catch (Exception e) {
                validFile = false;
                promptUser("Error, File not found" + 
                    "\nPlease ensure the file is in the same directory as the program\n" +
                    "The expected input is filename.csv, please try again\n");
                filename = null;
            }
        }
        String elecType = parseElectType();
        String auditFileName = generateAuditFileName(elecType);
        try {
            String path = System.getProperty("user.dir");
            File f = new File(path, auditFileName);
            audit = new FileWriter(f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(elecType.equals("IR")){
            runIR();
        }
        else if(elecType.equals("CPL")){
            runCPL();
        }
        else{
            promptUser("FILE READ ERROR\n");
            return;
        }


    }

    /**
     * Helper function for the start() function. Simply writes to terminal a message, prompting the user.
     * @param prompt String that represents the message desired to be prompted to the user.
     */
    private void promptUser(String prompt){
        System.out.printf(prompt);
    }

    /**
     * Helper function for the start() function. Generates an appropriate filename for the audit file.
     * @param elecType A String representing what type of election is taking place.
     * @return String that is the full name of the soon-to-be audit file.
     */
    private String generateAuditFileName(String elecType){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String d2 = now.format(f);
        return elecType + d2 + ".txt";
    }

    /**
     * Helper function for the start() function, used in conjunture with the generateAuditFileName(). Reads first line of input file.
     * @return String that represents the type of election.
     */
    private String parseElectType(){
        String line = "";
        this.br = new BufferedReader(input);
        try {
            line = br.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return line;

    }

    /**
     * Helper function for the start() function. Creates an IR election and runs its main methods to complete the program.
     */
    private void runIR(){
        IR Election = new IR(input, audit, br);
        Election.parseHeader();
        Election.processFile();
        Election.conductAlgorithm();
        Election.printResults();
    }

    /**
     * Helper function for the start() function. Creates a CPL election and runs its main methods to complete the program.
     */
    private void runCPL(){
        CPL Election = new CPL(input, audit, br);
        Election.parseHeader();
        Election.processFile();
        Election.conductAlgorithm();
        Election.printResults();
    }

    // Only for running tests
    /**
     * Setter for input
     * @param input A FileReader that serves as the input
     */
    public void setInput(FileReader input) {this.input = input;}

    /**
     * Setter for audit
     * @param audit FileWriter that represents the audit
     */
    public void setAudit(FileWriter audit) {this.audit = audit;}

    /**
     * setter for br
     * @param br BufferedReader that represents the br to read the file
     */
    public void setBr(BufferedReader br) {this.br = br;}
}