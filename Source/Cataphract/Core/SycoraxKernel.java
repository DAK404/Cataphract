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

/**
 * SycoraxKernel is the core kernel that handles user authentication,
 * session management, and command processing including
 * command-line processing, and script execution.
 *
 * @author DAK404 (https://github.com/DAK404)
 * @version 1.0
 * @since 0.0.1 (Zen Quantum 1.0)
 */
package Cataphract.Core;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;

import Cataphract.API.Anvil;
import Cataphract.API.Build;
import Cataphract.API.IOStreams;

import Cataphract.API.Astaroth.Time;

import Cataphract.API.Dragon.AccountCreate;
import Cataphract.API.Dragon.AccountDelete;
import Cataphract.API.Dragon.AccountModify;
import Cataphract.API.Dragon.Login;

import Cataphract.API.Minotaur.Cryptography;
import Cataphract.API.Minotaur.PolicyCheck;
import Cataphract.API.Minotaur.PolicyManager;

/**
 * Main class for the Sycorax operating system kernel.
 */
public class SycoraxKernel
{

    // Default account details and system settings
    /** Store the account name */
    private String _accountName = "DEFAULT_USER";
    /** Store the username */
    private String _username = "DEFAULT_USERNAME";
    /** Store the user unlock PIN */
    private String _userUnlockPIN = "";
    /** Store the system name */
    private String _systemName = "DEFAULT_SYSNAME";

    // Flags for user privileges and script mode
    /** Store value if user is an admin */
    private boolean _isUserAdmin = false;
    /** Store the value if a script is currently running */
    private boolean _scriptMode = false;

    // Command prompt symbol based on user privilege
    /** Store the value for the prompt */
    private char _prompt = '?';

    // Counter for login attempts before lock
    /** Store the number of attempts remaining for authentication */
    private int _loginAttemptsRemaining = 5;

    // Console object for input/output
    /** Instantiate Console to get user inputs. */
    private Console console = System.console();

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public SycoraxKernel()
    {
    }

    /**
     * Starts the Sycorax kernel, handles login and launches user shell.
     *
     * @throws Exception If there is an error in the login process or user shell.
     */
    public void startSycoraxKernel() throws Exception
    {
        // Display system build information
        Build.viewBuildInfo();

        // Loop until the user successfully logs in
        while(!login())
        {
            IOStreams.printError("Incorrect Credentials! Please try again.");
            // Handle login attempts
            loginCounterLogic();
        }

        // Successful login message
        IOStreams.printInfo("Login Successful. Loading Sycorax Kernel...");
        // Reset login attempts
        _loginAttemptsRemaining = 5;
        // Fetch user details after login
        fetchUserDetails();
        // Start the user command shell
        userShell();
    }

    /**
     * User shell that continuously reads and processes user commands.
     *
     * @throws Exception If there is an error during command processing.
     */
    private void userShell() throws Exception
    {
        String input = "";

        do
        {
            // Build the command prompt string dynamically
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append(_accountName).append("@").append(_systemName).append(_prompt).append("> ");
            // Read user input
            input = console.readLine(promptBuilder.toString());
            // Process the user command
            commandProcessor(input);
            // Exit loop on logout
        } while(!input.equalsIgnoreCase("logout"));
    }

    /**
     * Processes user commands entered in the shell.
     *
     * @param input The command entered by the user.
     * @throws Exception If there is an error in executing the command.
     */
    private void commandProcessor(String input) throws Exception
    {
        // Split the input command into array
        String[] commandArray = Anvil.splitStringToArray(input);

        // Process command based on first keyword
        switch(commandArray[0].toLowerCase())
        {
            // Refresh user details
            case "refresh":
                fetchUserDetails();
            break;

            // Lock the console
            case "lock":
                lockConsole();
            break;

            // Policy management logic
            case "policymgmt":
                new PolicyManager().policyEditorLogic();
            break;

            // File management logic
            case "grinch":
            case "filemanagement":
            case "files":
                new Cataphract.API.Wraith.FileManagement(_username).fileManagementLogic();
            break;

            // Exit the system
            case "exit":
                System.exit(0);
            break;

            // Restart the system
            case "restart":
                System.exit(100);
            break;

            case "script":
                if(commandArray.length < 2)
                    // Check for correct syntax
                    IOStreams.printError("Invalid Syntax");
                else
                    // Execute script
                    anvilScriptEngine(commandArray[1]);
            break;

            // Do nothing for logout or empty command
            case "logout":
            case " ":
            case "":
            break;

            // System update
            case "update":
                new Cataphract.API.Wyvern.NionUpdate(_username).updater();
                // Delete update file after completion
                new File("./Update.zip").delete();
            break;

            case "usermgmt":
                switch(commandArray[1].toLowerCase())
                {
                    // Create user account
                    case "create":
                        new AccountCreate(_username).accountCreateLogic();
                    break;

                    // Modify user account
                    case "modify":
                        new AccountModify(_username).accountModifyLogic();
                    break;

                    // Delete user account
                    case "delete":
                        new AccountDelete(_username).deleteUserAccount();
                        // Exit after deletion
                        System.exit(0);
                    break;

                    default:
                        IOStreams.printError("Module Usermgmt: " + commandArray[1] + " - Command Not Found");
                    break;
                }
            break;

            // Interpret other commands through Anvil API
            default:
                Anvil.anvilInterpreter(commandArray);
            break;
        }
    }

    /**
     * Handles user login by verifying username, password, and security key.
     *
     * @return true if the login is successful, false otherwise.
     * @throws Exception If there is an error during authentication.
     */
    private boolean login() throws Exception
    {
        Build.viewBuildInfo();  // Display system build info

        // Show remaining authentication attempts
        IOStreams.printInfo("Authentication Attempts Left: " + _loginAttemptsRemaining);

        // Read and hash user input for credentials
        _username = Cryptography.stringToSHA3_256(console.readLine("> Username: "));
        String password = Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> Password: ")));
        String securityKey = Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> Security Key: ")));

        // Authenticate using Login class logic
        return new Login(_username).authenticationLogic(password, securityKey);
    }

    /**
     * Logic to handle failed login attempts. Locks the system after 5 failed attempts.
     *
     * @throws Exception If there is an error during sleep or authentication lock.
     */
    private void loginCounterLogic() throws Exception
    {
        // Decrease remaining attempts
        _loginAttemptsRemaining--;
        if(_loginAttemptsRemaining <= 0)
        {
            IOStreams.printError("Authentication Attempts Exceeded! Further attempts are locked!");
            // Lock the system for 36 seconds after 5 failed attempts
            Thread.sleep(36000);
        }
    }

    /**
     * Fetches user account details such as name, admin status, PIN, and system name.
     *
     * @throws Exception If there is an error in fetching the details.
     */
    private void fetchUserDetails() throws Exception
    {
        // Fetch account name
        _accountName = new Login(_username).getNameLogic();
        // Check if the user is an admin
        _isUserAdmin = new Login(_username).checkPrivilegeLogic();
        // Fetch the unlock PIN
        _userUnlockPIN = new Login(_username).getPINLogic();
        // Fetch system name from policy
        _systemName = new PolicyCheck().retrievePolicyValue("sysname");

        // Set prompt based on user privilege
        _prompt = _isUserAdmin ? '!' : '*';
    }

    /**
     * Locks the console and prompts for the unlock PIN.
     * @throws Exception If there is an error during the lock process.
     */
    private void lockConsole() throws Exception
    {
        Build.viewBuildInfo();  // Display build info

        String input = "";

        // Prompt the user with a locked screen message until "unlock" command is entered
        do
        {
            StringBuilder lockPromptBuilder = new StringBuilder();
            lockPromptBuilder.append((char)27).append("[33;49m")
                    .append(new Time().getDateTimeUsingSpecifiedFormat("yyyy-MMM-dd HH:mm:ss"))
                    .append("  LOCKED\n")
                    .append(_accountName).append("@Cataphract").append(_prompt).append("> ")
                    .append((char)27).append("[0m");
            input = console.readLine(lockPromptBuilder.toString());
        } while(!input.equalsIgnoreCase("unlock"));  // Continue until "unlock" command is entered

        // Prompt the user to enter the unlock PIN
        IOStreams.printAttention("Please Enter Unlock PIN To Continue.");

        // Validate the PIN until correct PIN is entered
        while(!challengePIN())
        {
            IOStreams.printError("Incorrect PIN.");
            loginCounterLogic();  // Handle failed PIN attempts
        }
        _loginAttemptsRemaining = 5;  // Reset login attempts on successful unlock
        System.gc();  // Run garbage collector
        Build.viewBuildInfo();  // Display build info again after unlock
    }

    /**
     * Challenges the user to enter the correct PIN.
     * @return true if the entered PIN matches the stored PIN, false otherwise.
     * @throws Exception If there is an error during the PIN validation.
     */
    private boolean challengePIN() throws Exception
    {
        return Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> PIN : "))).equals(_userUnlockPIN);
    }

    /**
     * Executes a script file with AnvilScript engine.
     * @param scriptFileName The name of the script file to execute.
     * @return true if the script is executed successfully, false otherwise.
     * @throws Exception If there is an error during script execution.
     */
    private boolean anvilScriptEngine(String scriptFileName) throws Exception
    {
        boolean status = false;

        // Validate script file name
        if(scriptFileName == null || scriptFileName.equalsIgnoreCase("") || scriptFileName.startsWith(" "))
            IOStreams.printError("The name of the script file cannot be blank.");
        else
        {
            // Check if script execution is enabled and the user is an admin
            if(new PolicyCheck().retrievePolicyValue("script").equals("on") && _isUserAdmin)
            {
                scriptFileName = IOStreams.convertFileSeparator(".|Users|Cataphract|" + _username + "|" + scriptFileName);

                // Check if the script file exists
                if(!new File(scriptFileName).exists())
                {
                    StringBuilder errorBuilder = new StringBuilder();
                    errorBuilder.append("Script file ").append(scriptFileName.replace(_username, _accountName))
                            .append(" has not been found.\nPlease check the directory of the script file and try again.");
                    IOStreams.printAttention(errorBuilder.toString());
                }
                else
                {
                    if(_scriptMode)
                    {
                        IOStreams.printError("Cannot execute script within another script.");
                    }
                    else
                    {
                        // Activate script mode
                        _scriptMode = true;

                        // Initialize BufferedReader to read the script file
                        BufferedReader br = new BufferedReader(new FileReader(scriptFileName));

                        // Read script line by line and process commands
                        String scriptLine;

                        while ((scriptLine = br.readLine()) != null)
                        {
                            if(scriptLine.startsWith("#") || scriptLine.equalsIgnoreCase(""))
                                // Skip comment or blank lines
                                continue;
                            else if(scriptLine.equalsIgnoreCase("End Script"))
                                // Stop if "End Script" command is encountered
                                break;
                            else
                                // Process script command
                                commandProcessor(scriptLine);
                        }

                        // Close BufferedReader
                        br.close();
                        // Run garbage collector
                        System.gc();
                        // Deactivate script mode
                        _scriptMode = false;
                    }
                }
            }
            else
                IOStreams.printError("Insufficient Privileges to run scripts! Please contact the Administrator for more information.");
        }
        return status;
    }
}
