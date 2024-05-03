/*
*                                                      |
*                                                     ||
*  |||||| ||||||||| |||||||| ||||||||| |||||||  |||  ||| ||||||| |||||||||  |||||| ||||||||
* |||            ||    |||          ||       || |||  |||       ||       || |||        |||
* |||      ||||||||    |||    ||||||||  ||||||  ||||||||  ||||||  |||||||| |||        |||
* |||      |||  |||    |||    |||  |||  |||     |||  |||  ||  ||  |||  ||| |||        |||
*  ||||||  |||  |||    |||    |||  |||  |||     |||  |||  ||   || |||  |||  ||||||    |||
*                                               ||
*                                               |
*
* A Cross Platform OS Shell
* Powered By Truncheon Core
*/

package Cataphract.Core;

//Import the required Java IO classes
import java.io.File;
import java.io.Console;
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//Import the required Cataphract APIs
import Cataphract.API.IOStreams;
import Cataphract.API.Minotaur.Cryptography;
import Cataphract.API.Anvil;
import Cataphract.API.Build;
import Cataphract.API.ExceptionHandler;

/**
* Entry point to the kernel loader. Checks conditions and loads the kernel.
*
* @author DAK404 (https://github.com/DAK404)
* @version 1.44.7 (11-October-2023, Cataphract)
* @since 0.0.1 (Zen Quantum 1.0)
*/
public class Loader
{
    /** A list of kernel file paths used by Abraxis subsystem to verify kernel integrity. */
    private static List<String> abraxisFilePathList = new  ArrayList<String>();
    
    /** Instantiate Console to get user inputs. */
    private Console console = System.console();
    
    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public Loader()
    {
    }
    
    /**
    * The entry point to the loader. Boots the kernel based on the arguments supplied.
    *
    * @param args Parameters passed to the loader, specifying the kernel modes.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public static void main(String[] args)throws Exception
    {
        try
        {
            //Check the boot parameters received and perform an action based on the parameters.
            switch(args[0])
            {
                //Probe mode: exit with status 7 to notify that the kernel exists
                case "probe":
                System.exit(7);
                break;
                
                //Boot to the normal mode, no changes made here
                case "normal":
                IOStreams.println("Starting Kernel...");
                break;
                
                case "crash":
                throw new Exception();
                
                case "astaroth":
                IOStreams.println(String.valueOf(new Cataphract.API.Astaroth.Time().getUnixEpoch()));
                IOStreams.println(String.valueOf(new Cataphract.API.Astaroth.Time().getDateTimeUsingSpecifiedFormat("dd-MMMM-yyyy \nEEEE HH:mm:ss")));
                new Cataphract.API.Astaroth.Calendar().printCalendar(0,0);
                new Cataphract.API.Astaroth.Calendar().printCalendar(8, 2077);
                System.exit(0);
                
                case "iostreams":
                IOStreams.printError("This is an error message.");
                IOStreams.printWarning("This is a warning message.");
                IOStreams.printAttention("This is an attention message.");
                IOStreams.printInfo("This is an information message.");
                IOStreams.println("This is a normal printline message. Printing the same with colors");
                for(int i = 0; i < 9; i++)
                {
                    for(int j = 0; j < 9; j++)
                    {
                        IOStreams.print(i, j, "testing IOStreams");
                        System.out.print(" ");
                    }
                    System.out.println();
                }
                System.exit(0);
                
                //Rejects boot if the mode is not specified
                default:
                IOStreams.printError("Invalid Boot Mode. Aborting...\n");
                System.exit(3);
            }
            
            //Display the build information
            Build.viewBuildInfo();
            //Instantiate and start the loader logic
            new Loader().loaderLogic();
        }
        catch(Exception e)
        {
            new ExceptionHandler().handleException(e);
        }
    }
    
    /**
    * Logic that will load the kernel after checking the kernel integrity
    * 
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private void loaderLogic()throws Exception
    {
        switch(abraxisLogic())
        {
            case 0:
            break;
            
            case 1:
            IOStreams.printError("Unable to locate or parse Manifest Files! Aborting boot...");
            System.exit(4);
            break;
            
            case 2:
            IOStreams.printError("Unable to populate the Kernel files! Aborting boot...");
            System.exit(4);
            break;
            
            case 3:
            IOStreams.printError("File Signature verification failed! Aborting boot...");
            System.exit(4);
            break;
            
            case 4:
            IOStreams.printError("File verification failed: Found File Size Discrepancy! Aborting boot...");
            System.exit(4);
            break;
            
            case 5:
            if(new Setup().setupCataphract())
            System.exit(100);
            else
            {
                IOStreams.printError("Setup Failed! Reverting changes...");
                
                //TODO: Delete /System and /Users directories
            }
            break;
            
            case 55:
            IOStreams.printError("Could not initialize Abraxis! Aborting boot...");
            System.exit(4);
            break;
            
            default:
            IOStreams.printError("Generic Failure. Cannot Boot.");
            System.exit(4);
            break;
        }
        
        guestShell();
    }
    
    /**
    * Logic of the Abraxis sub-system to check if kernel programs are verified and can be booted without issues.
    *
    * @return byte abraxisResult - Returns the results of the integrity checks.
    */
    private byte abraxisLogic()
    {
        //Initialize the return value to be defaulted at 55
        byte abraxisResult = 55;
        
        IOStreams.printInfo("Stage 0: Checking Manifest Files...");
        
        //check if the Manifest files exist
        if(manifestFilesCheck())
        {
            IOStreams.printInfo("Stage 1: Manifest Files Found. Populating Kernel Files and Directories...");
            
            //Begin populating the Kernel files
            if(populateKernelFiles(new File("./")))
            {
                IOStreams.printInfo("Stage 2: Kernel Files and Directories populated. Checking File Integrity - Phase 1...");
                
                //Begin checking the file hashes from the M1 manifest
                if(checkFileHashes())
                {
                    IOStreams.printInfo("Stage 3: File Integrity Check - Phase 1 Complete. Checking File Integrity - Phase 2...");
                    
                    //Begin checking the file sizes from the M2 manifest
                    if(checkFileSizes())
                    {
                        //All checks passed, set the return value to be 0
                        abraxisResult = 0;
                        IOStreams.printInfo("Stage 4: File Integrity Check - Phase 2 Complete. Checking System and User Files...");
                        
                        //Check if program setup needs to be run once.
                        if(setupStatusCheck())
                        IOStreams.printInfo("File Check Complete. Booting Cataphract...");
                        else
                        {
                            //Set return value to be 5, denoting that the setup program needs to be run.
                            abraxisResult = 5;
                            IOStreams.printAttention("Setting up Cataphract...");
                        }
                    }
                    else
                    //Set return value to be 4, denoting that the file size check failed.
                    abraxisResult = 4;
                }
                else
                //Set the return value to be 3, denoting that the file hash check failed.
                abraxisResult = 3;
            }
            else
            //Set the return value to be 2, denoting that the population of kernel files failed.
            abraxisResult = 2;
        }
        else
        //Set the return value to be 1, denoting that there is an error with finding or reading the manifest files
        abraxisResult = 1;
        
        //Clear the file list, save memory.
        abraxisFilePathList.clear();
        
        //Return the value stored in the return value
        return abraxisResult;
    }
    
    /**
    * Logic to check if the Manifest files exists in the manifest directory.
    *
    * @return boolean - Returns if the M1 and M2 manifest files exist
    */
    private boolean manifestFilesCheck()
    {
        return new File("./.Manifest/Cataphract/KernelFilesHashes.m1").exists() & new File("./.Manifest/Cataphract/KernelFiles.m2").exists();
    }
    
    /**
    * Logic to populate the Kernel files into a list
    *
    * @param fileDirectory The directory to check for sub-directories and files to be added to the list
    * @return boolean status - The status of populating the directories
    */
    private boolean populateKernelFiles(File fileDirectory)
    {
        //Initialize the status return value to false
        boolean status = false;
        
        //Added a try-catch block for better error handling mechanism
        try
        {
            //Store an array of files and directories from the specified directory.
            File[] fileList = fileDirectory.listFiles();
            
            //Iterate through each entry in the array
            for(File fileName: fileList)
            {
                //Check if the entry is present in the ignore list.
                if(fileIgnoreList(fileName.getName()))
                //If true, start the iteration with the next entry
                continue;
                
                //if the entry is a directory, recursively iterate the contents
                if(fileName.isDirectory())
                //populate the entries in the sub-directory
                populateKernelFiles(fileName);
                
                //Else get the path of the entry and add it to the list
                else
                abraxisFilePathList.add(fileName.getPath());
            }
            
            //set the status to be true to denote that the population of the specified directory is successful.
            status = true;
        }
        //Catch any exceptions thrown while populating the files
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        //Return the status value
        return status;
    }
    
    /**
    * Logic to ignore certain files and directories who's value may or may not change over time.
    * These may be external libraries, JREs, manifest files, the System and User directories.
    *
    * @param fileName Name of the file to be checked against the ignore list.
    * @return boolean status - Returns the value, whether the file needs to be ignored or not
    */
    private boolean fileIgnoreList(String fileName)
    {
        boolean status = false;
        
        String[] ignoreList = {".Manifest", "System", "Users", "org", "JRE", "Logs"};
        
        for(String files : ignoreList)
        {
            if(fileName.equalsIgnoreCase(files))
            {
                status = true;
                break;
            }
        }
        System.gc();
        return status;
    }
    
    /**
    * Logic to check the file hashes against the M1 manifest hashes.
    *
    * @return boolean kernelIntegrity - Returns if the file hashes match or not against the M1 manifest.
    */
    private boolean checkFileHashes()
    {
        //Initialize the return value to be true.
        boolean kernelIntegrity = true;
        
        //Added a try-catch block for better error handling mechanism.
        try
        {
            //Create an object (manifestM1Entries) to parse through the M1 manifest file.
            Properties manifestM1Entries = new Properties();
            //Initialize the input stream to read the values from the M1 manifest file
            FileInputStream m1FileStream = new FileInputStream("./.Manifest/Cataphract/KernelFilesHashes.m1");
            
            //Store the XML values into the Properties object.
            manifestM1Entries.loadFromXML(m1FileStream);
            //Close the file input stream
            m1FileStream.close();
            
            /*
            * DEBUG: List the properties loaded from the M1 Manifest file.
            * manifestM1Entries.list(System.out);
            */
            
            //Iterate through each entry in the list containing the file paths.
            for(String fileName: abraxisFilePathList)
            {
                //Do not hash the file if it is specified in the ignore list.
                if(fileIgnoreList(fileName))
                continue;
                
                //store the hash of the file present on disk (fileName).
                String kernelFileHash = Cryptography.fileToSHA3_256(fileName);
                
                //Added a try-catch block for alerting the user about unknown files.
                try
                {
                    //Store the hash of the file present in the manifest.
                    String manifestHash = manifestM1Entries.getProperty(convertToNionSeparator(fileName));
                    
                    //Check if the manifest and the file hashes are equal
                    if(!manifestHash.equals(kernelFileHash))
                    {
                        //If not equal, set Kernel Integrity status to false, and alert the user on the same.
                        kernelIntegrity = false;
                        IOStreams.printError("Integrity Check Failure at " + kernelFileHash + "\t" + fileName + "\n");
                    }
                }
                //Catch error for the specified file that is not found, and present it to the user as an unknown file.
                catch(Exception e)
                {
                    IOStreams.printWarning("Unknown File at " + kernelFileHash + "\t" + fileName + "\n");
                }
            }
        }
        //Catch errors relating to the parsing of the manifest file.
        catch(Exception e)
        {
            IOStreams.println("Manifest Parse Error! Unable to continue.");
            kernelIntegrity = false;
        }
        
        //return the status of the kernel integrity
        return kernelIntegrity;
    }
    
    /**
    * Logic to check the file sizes against the Manifest M2 sizes
    *
    * @return boolean status - Returns if the file sizes match or not against the M2 manifest.
    */
    private boolean checkFileSizes()
    {
        //Initialize the file count to be 0
        int fileCount = 0;
        //Initialize the return value to be true
        boolean status = true;
        
        //Added a try-catch block for better error handling mechanism.
        try
        {
            //Create an object (manifestM2Entries) to parse through the M2 manifest file.
            Properties manifestM2Entries = new Properties();
            //Initialize the input stream to read the values from the M2 manifest file
            FileInputStream m2FileStream = new FileInputStream("./.Manifest/Cataphract/KernelFiles.m2");
            
            //Store the XML values into the Properties object.
            manifestM2Entries.loadFromXML(m2FileStream);
            //Close the file input stream
            m2FileStream.close();
            
            /*
            * DEBUG: List the properties loaded from the M1 Manifest file
            * manifestM1Entries.list(System.out);
            */
            
            //Iterate through each entry in the list containing the file paths.
            for(String fileName: abraxisFilePathList)
            {
                //Checks only the ".class" files present
                if(fileName.endsWith(".class"))
                {
                    //Store the file size in the M2 manifest
                    long fileSizeM2 = Long.parseLong(String.valueOf(manifestM2Entries.get(convertToNionSeparator(fileName))));
                    //Store the file size present on the disk
                    long fileSize = new File(fileName).length();
                    
                    //Check if the file sizes match
                    if(fileSize != fileSizeM2)
                    {
                        //If they don't match, set the return status to false, and alert the user on the same.
                        status = false;
                        IOStreams.printError("Integrity Check Failure at " + fileName + "\t" + fileSize + ". Expected " + fileSizeM2);
                    }
                    
                    //Increment the file count for each iteration
                    fileCount++;
                }
            }
            
            //Check if the number of files on disk is bigger than the number of entries present in the manifest file
            if(fileCount < manifestM2Entries.size())
            {
                //If the files on disk is lesser than the number of entries in the manifest, set the return status to false and alert the user on the same.
                status = false;
                IOStreams.printError("Integrity Check Failure. Expected " + manifestM2Entries.size() + ". Found " + fileCount);
            }
        }
        //Catch any exceptions related to the file operations
        catch(Exception e)
        {
            status = false;
            e.printStackTrace();
            IOStreams.printError("File Operations Failure! Unable to continue.");
        }
        
        //return the status of the file size check
        return status;
    }
    
    /**
    * Logic to check if Cataphract has been setup on the system.
    *
    * @return boolean Returns if the "System" and "User" directories have been initialized
    */
    private boolean setupStatusCheck()
    {
        return new File("./System/Cataphract").exists() & new File("./Users/Cataphract").exists();
    }
    
    /**
    * Logic to convert from an OS dependent file separator format of file paths to Nion File Separator format.
    *
    * @param nionPath String that contains the file path in Nion File Separator format
    * @return String The value of the String converted to the OS dependent file separator format
    */
    // private String convertFileSeparator(String nionPath)
    // {
    //     return nionPath.replaceAll("|", Matcher.quoteReplacement(File.separator));
    // }
        
    /**
    * Logic to convert from an OS dependent file separator format of file paths to Nion File Separator format.
    *
    * @param filePath String that contains the file path in OS dependent file separator format.
    * @return String The value of the String converted to Nion File Separator format.
    */
    private String convertToNionSeparator(String filePath)
    {
        return filePath.replaceAll(Matcher.quoteReplacement(File.separator), "|");
    }
    
    /**
    * Logic to provide the user with a guest shell: a shell with limited functionality to be used before login
    */
    private void guestShell() throws Exception
    {
        String input;
        do
        {
            input = console.readLine("> ");
            String[] commandArray = Anvil.splitStringToArray(input);
            
            switch(commandArray[0].toLowerCase())
            {
                case "exit":
                case "":
                break;

                case "clear":
                Build.clearScreen();
                break;

                case "login":
                new SycoraxKernel().startSycoraxKernel();

                // A Windows 95/98 Easter Egg?
                IOStreams.println(3, 0, "It is now safe to turn off your PC ;)");
                break;

                default:
                IOStreams.printError(commandArray[0] + " Command Not Found.");
                break;
            }
        }
        while(!input.equalsIgnoreCase("exit"));
    }
}

/**
* A class to setup the files and directories of Cataphract for it to work as expected.
*/
class Setup
{
    /**
    * Boolean to store status if the user has accepted EULA.
    */
    private boolean prereqInfoStatus = false;
    
    /**
    * Boolean to store status if the Cataphract directories are initialized successfully
    */
    private boolean initDirs = false;
    
    /**
    * Boolean to store status if the Database has been successfully initialized
    */
    private boolean initDB = false;
    
    /**
    * Boolean to store status if the Default Policies have been initialized
    */
    private boolean initPolicies = false;
    
    /**
    * Boolean to store status if the Administrator account has been created
    */
    private boolean initAdminAccount = false;
    
    /**
    * Instantiate Console to get user inputs
    */
    Console console = System.console();
    
    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    protected Setup()
    {
        
    }
    
    /**
    * Display the progress of the setup to the user: what's pending and what's completed
    */
    private void displaySetupProgress()
    {
        Cataphract.API.Build.viewBuildInfo();
        IOStreams.println("[ -- Program Setup Checklist -- ]");
        IOStreams.println("[*] Show Program Prerequisites   : " + (prereqInfoStatus?"COMPLETED":"PENDING"));
        IOStreams.println("[*] Initialize Directories       : " + (initDirs?"COMPLETED":"PENDING"));
        IOStreams.println("[*] Initialize Database System   : " + (initDB?"COMPLETED":"PENDING"));
        IOStreams.println("[*] Initialize Program Policies  : " + (initPolicies?"COMPLETED":"PENDING"));
        IOStreams.println("[*] Create Administrator Account : " + (initAdminAccount?"COMPLETED":"PENDING"));
        IOStreams.println("[ ----------------------------- ]\n");
    }
    
    /**
    * The entry point to the setup. Sets the environment up to run Cataphract.
    * 
    * @return boolean returnValue - Returns if the setup was successful or not.
    */
    boolean setupCataphract()throws Exception
    {
        boolean returnValue = false;
        
        //A brief introduction to Cataphract and why it is being setup //
        Cataphract.API.Build.clearScreen();
        
        /**
        * Display a message to give a brief introduction to Cataphract and the reason why it is being setup
        */
        String oobeIntroduction = Cataphract.API.Build._Branding + """
        
        Welcome to Cataphract!
        
        As this is the first time the program is being run, several setup setup steps need to be completed for normal use.
        
        [*] ACCEPT EULA: Agree to the End User License Agreement to begin the setup.
        [*] CREATE SYSTEM DIRECTORIES: Create directories essential for Cataphract to function as expected.
        [*] INITIALIZE DATABASE: Initialize the user database to store the user credentials.
        [*] CREATE ADMINISTRATOR ACCOUNT: Create the administrator account to engage and maintain the functioning of the system.
        
        These steps are required to be performed by the system administrator. If the current user is an end user,
        please press [ CTRL + C ] keys and contact the System Administrator.
        
        If the current user is a System Administrator,\u00A0""";
        
        Cataphract.API.IOStreams.confirmReturnToContinue(oobeIntroduction, ".\nSetup> ");
        
        //User needs to accept EULA
        showAndAcceptEULA();
        
        //Program will then setup required directories: /System and /Users
        createSystemDirectories();
        
        //Initialize the database to store the user credentials
        initializeDatabase();
        
        //Initialize the default policy values.
        initializeDefaultPolicies();
        
        //Initialize a default administrator account.
        createAdministratorAccount();
        
        //Show the set of actions undertaken to the user before restarting
        displaySetupProgress();
        Cataphract.API.IOStreams.confirmReturnToContinue("Setup complete! ", ".\nSetup> ");
        
        
        returnValue = prereqInfoStatus & initAdminAccount & initDB & initDirs & initPolicies;
        
        //force return false until Setup implementation is complete.
        return returnValue;
    }
    
    /**
    * Logic to display the EULA to the user. User must accept it to complete the setup.
    */
    private void showAndAcceptEULA()
    {
        //Display the setup progress.
        displaySetupProgress();
        
        //logic to read EULA; Omitted for now.
        System.out.println("DUMMY TEXT: EULA");
        
        //Check if the user accepts the EULA
        if(! console.readLine("Do you accept the EULA? [ Y / N ]\nEULA?> ").equalsIgnoreCase("y"))
        System.exit(0);
        
        //Update the EULA completion status to COMPLETE.
        prereqInfoStatus = true;
    }
    
    /**
    * Logic to create directories required by Cataphract.
    */
    private void createSystemDirectories()
    {
        //Display the setup progress.
        displaySetupProgress();
        IOStreams.printInfo("Creating Directories...");
        
        //Define a set of directories to be created by Cataphract Setup program.
        String [] directoryNames = {"./System/Cataphract/Public/Logs", "./Users/Cataphract"};
        
        //Iterate through the array and create the specified directories
        for (String dirs: directoryNames)
        new File(dirs).mkdirs();
        
        //Update the Directory Initialization to COMPLETE.
        initDirs = true;
    }
    
    /**
    * Logic to create MUD (Multi User Database) table to store user credentials.
    */
    private void initializeDatabase()
    {
        //Display the setup progress.
        displaySetupProgress();
        
        //Store the database creation status.
        boolean initializeDatabaseStatus = false;
        
        //Added a try-catch block for better error handling mechanism
        try
        {
            //Create Backups directory to store the database backups
            new File("./System/Cataphract/Private/Backups").mkdirs();
            
            //Store the database path that the program will require to access the database
            String databasePath = "jdbc:sqlite:./System/Cataphract/Private/Mud.dbx";
            
            //Check if the Database already exists
            IOStreams.printInfo("Checking for existing Master User Database...");
            if(new File(databasePath).exists())
            //Abort the creation of the database; since it already exists
            IOStreams.printError("Master User Database already exists! Aborting...");
            
            //Else, continue to create the database table and file
            else
            {
                //Initialize the database driver to be used by Cataphract Setup
                Class.forName("org.sqlite.JDBC");
                
                /**
                * String that contains the CREATE statement of the database table.
                */
                String createMUDTable = "CREATE TABLE IF NOT EXISTS MUD (" +
                "Username TEXT," +
                "Name TEXT NOT NULL," +
                "Password TEXT NOT NULL," +
                "SecurityKey TEXT NOT NULL," +
                "PIN TEXT NOT NULL," +
                "Privileges TEXT NOT NULL," +
                "PRIMARY KEY(Username));";
                
                /**
                * Get the connection of the database using the given path
                */
                Connection dbConnection = DriverManager.getConnection(databasePath);
                
                /**
                * Build the statement using the createMUDTable string
                */
                Statement statement = dbConnection.createStatement();
                
                //Execute the SQL statement
                statement.execute(createMUDTable);
                
                //Close the statement
                statement.close();
                
                //Close the database connection
                dbConnection.close();
                
                //Run garbage collector to free any freeable spaces in memory
                System.gc();
                
                //Set the database initialization status as true
                initializeDatabaseStatus = true;
            }
        }
        catch(Exception e)
        {
            //Catch any exceptions caught during runtime and pass it on to the ExceptionHandler class
            new Cataphract.API.ExceptionHandler().handleException(e);
        }
        
        //Update the Database Initialization to COMPLETE or failed
        initDB = (initializeDatabaseStatus?true:false);
    }
    
    /**
    * Logic to initialize the default policies for the users.
    */
    private void initializeDefaultPolicies()
    {
        displaySetupProgress();
        new Cataphract.API.Minotaur.PolicyManager();
        initPolicies = true;
    }
    
    /**
    * Logic to create a default administrator account
    */
    private void createAdministratorAccount()throws Exception
    {
        new Cataphract.API.Dragon.AccountCreate().createDefaultAdministratorAccount();
        initAdminAccount = true;
    }
}