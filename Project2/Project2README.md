# Project 2 README

## Project 2 Structure
Our project structure follows the structure provided in the handout. One addition is that under src, all the src files are under a VotingSystem package for easier grouping.


## Steps for Compilation
**Important**: Our IR table is a gui. Therefore, when you run an IR election, a small java icon will appear on your computer
that you have to click on to pull up the table. (It is not printed textually to the terminal).

Please navigate to the repo-Team18/Project2/src directory.  
(To make sure you're in the right place, typing "ls" in terminal should show "VotingSystem" as the only listed file)
Next, to compile type "javac VotingSystem/*.java"  
Next, to run the Voting System program, type "java VotingSystem.Main" (if Java17)  
For older versions, you may need to type "java VotingSystem/Main" instead.  
You may also pass along the name of the input file if you wish.  

**Important**: Next, as our test files with the code for the tests are currently located in the src/VotingSystem directory, 
please note that if there is a compilation error, it may be due to not being able to find JUnit.  
This can be solved by moving the test files with the code to the Testing folder while you run the program.  
(Moving the testing files out of the src folder will mean compiling and running the code WITHOUT tests. Package org.junit must exists in order to compile WITH the tests)

Please ensure that the src folder is marked as src root and testing is marked as test sources root for tests to work.
Please refer to Misc. Notes below for dependency information.

## Notes on Testing

Where to move our test code files from src into Testing:
CPLtests -> Testing/CPLTestsResources
IRTests -> Testing/IRTestsResources
IrVotesTests -> Testing/IRTestsResources
TableTests -> Testing/IRTestsResources
POTests -> Testing/POTestsResources
All other tests from src may be moved into the Testing folder.

Please ensure that package names are properly refactored.

All test resources (files) may be found in repo-Team18/Project2/VotingSystem/Testing  
If you wish to run the tests, we strongly recommend using IntelliJ.

Please ensure that the test code files are moved out from src and into the testing folder that has been marked as
the test sources root.

To be safe, ensure that there's a copy of the .csv files used for testing (described in each testing class) in both src/VotingSystem
AND in the testing folder when running out tests.

It is easiest to run our tests through an IDE like IntelliJ that will automatically run the tests for you.

We have noticed that different IDEs may set up the classpath, configuration, etc. differently.  
This may cause trouble in configuring the tests such that they are runnable on your IDE.
Next, please ensure that the user's directory is set such that the working directory is the one
that contains the testing files. Occasionally, a person's default may be set to be repo-Team18 instead
depending on their IDE. This may show an error that makes it seem like a test file cannot be accessed in VotingSystem.RunElectionTests and possibly other test classes. As long as the user's
directory is set to the one that holds the test file, it should be able to find the test file. You may check the user directory by
typing in "String path = System.getProperty("user.dir");" and then checking that path to make sure it's not just repo-Team18 (otherwise the test WILL NOT work)
For VotingSystem.RunElectionTests, please ensure the test files described are located in the src/VotingSystem directory prior to running tests.

Next, the program can only find JUnit and run tests involving JUnit if the test file
is located in whichever directory is marked as the test sources root for IDEs like IntelliJ (our testing folder). Please ensure that if JUnit is giving an error that
the test file can access JUnit in its directory.

## Misc Notes
If you ever receive any error message that says that a dependency cannot be located, please click the rebuild option
to rebuild the project with Maven to refresh.

