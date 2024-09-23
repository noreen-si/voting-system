# Project 1 README

## Project Structure
Our project structure follows the structure provided in the handout. One addition is that under src, the all the src files are under a VotingSystem package for easier grouping.


## Steps for Compilation
Please navigate to the repo-Team18/Project1/src directory.  
(To make sure you're in the right place, typing "ls" in terminal should show "VotingSytem" as the only listed file)
Next, to compile type "javac VotingSystem/*.java"  
Next, to run the Voting System program, type "java VotingSystem.Main" (if Java17)  
For older versions, you may need to type "java VotingSystem/Main" instead.  
You may also pass along the name of the input file if you wish.  

**Important**: Next, as our test files with the code for the tests are currently located in the src/VotingSystem directory, 
please note that if there is a compilation error, it may be due to not being able to find JUnit.  
This can be solved by moving the test files with the code to the Testing folder while you run the program.  
(Moving the testing files out of the src folder will mean compiling and running the code WITHOUT tests. Package org.junit must exists in order to compile WITH the tests)

Please refer to Misc. Notes below for dependency information.

## Notes on Testing
Please note that for our final product, many of the methods that are used in tests are private, primarily in IR, CPL, etc. as there is no reason to use them
externally. This may further affect the tests as the tests were primarily used in development when these methods were not private. Please note
that if the tests show you an error when you run them it may be due to the method being private now. You may be able to change them back to being public
if you wish to run a specific test on that method. It may be easier to change all private methods to public to avoid going back and forth.
All test resources (files) may be found in repo-Team18/Project1/VotingSystem/Testing  
If you wish to run the tests, we strongly recommend using IntelliJ.
We have noticed that different IDEs may set up the classpath, configuration, etc. differently.  
This may cause trouble in configuring the tests such that they are runnable on your IDE.
Next, please ensure that the user's directory is set such that the working directory is the one
that contains the testing files. Occasionally, a person's default may be set to be repo-Team18 instead
depending on their IDE. This may show an error that makes it seem like a test file cannot be accessed in VotingSystem.RunElectionTests and possibly other test classes. As long as the user's
directory is set to the one that holds the test file, it should be able to find the test file. You may check the user directory by
typing in "String path = System.getProperty("user.dir");" and then checking that path to make sure it's not just repo-Team18 (otherwise the test WILL NOT work)
For VotingSystem.RunElectionTests and VotingSystem.Maintests, please ensure the test files described are located in the src/VotingSystem directory prior to running tests.  
Next, the program can only find JUnit and run tests involving JUnit if the test file
is located in whichever directory is marked as the test sources root for IDEs like IntelliJ. Please ensure that if JUnit is giving an error that
the test file can access JUnit in its directory.  
Lastly, please note that the src files located in src will not allow access for the tests to certain
protected fields or private fields, methods, etc. When running a test and if it gives an error depending on your setup,
please ensure that it is not due to accessing a private or protected method that is not accessible in the test sources root directory.
We have left getters that should provide access to these fields in their respective classes.
Many of our tests currently rely on using these auto-generated getters. However, for private/protected methods, you must first publicize these.

It is easiest to re-publicize all methods in the beginning for testing.

## Misc Notes
If you ever receive any error message that says that a dependency cannot be located, please click the rebuild option
to rebuild the project with Maven to refresh.

