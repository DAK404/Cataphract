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

public class SycoraxKernel
{

    private String _accountName = "DEFAULT_USER";
    private String _username = "DEFAULT_USERNAME";
    private String _userUnlockPIN = "";
    private String _systemName = "DEFAULT_SYSNAME";
    
    private boolean _isUserAdministrator = false;
    private boolean _scriptMode = false;

    private char _prompt = '?';

    private int _loginAttemptsRemaining = 5;
    private int _scriptLineNumber = 0;

    private Console console = System.console();

    public SycoraxKernel()
    {
    }

    public void startSycoraxKernel() throws Exception
    {
        Build.viewBuildInfo();
        IOStreams.printInfo("ADVISORY! Work in progress!");
        IOStreams.println("Sycorax Kernel is still being implemented. Expect crashes, errors and isses."); 

        IOStreams.println(" --- Login Test --- ");
        
        while(!login())
        {
            IOStreams.printError("Incorrect Credentials! Please try again.");
            loginCounterLogic();
        }
        viewSycoraxInformation();
        IOStreams.printInfo("Login Successful. Loading Sycorax Kernel...");
        _loginAttemptsRemaining = 5;
        fetchUserDetails();
        userShell();
    }

    private void userShell() throws Exception
    {
        String input = "";
        do
        {
            input = console.readLine(_accountName + "@" + _systemName + _prompt + "> ");
            commandProcessor(input);
        }
        while(!input.equalsIgnoreCase("logout"));
    }

    private void commandProcessor(String input)throws Exception
    {
        String[] commandArray = Anvil.splitStringToArray(input);

        switch(commandArray[0].toLowerCase())
            {
                case "refresh":
                fetchUserDetails();
                break;

                case "restart":
                System.exit(100);
                break;

                case "lock":
                lockConsole();
                break;

                // case "clear":
                // viewSycoraxInformation();
                // break;

                case "exit":
                System.exit(0);
                break;

                case "logout":
                case " ":
                case "":
                break;

                case "usermgmt":
                switch(commandArray[1].toLowerCase())
                {
                    case "create":
                    new AccountCreate(_username).accountCreateLogic();
                    break;

                    case "modify":
                    new AccountModify(_username).accountModifyLogic();
                    break;

                    case "delete":
                    new AccountDelete(_username).deleteUserAccount();
                    break;

                    default:
                    IOStreams.printError("Module Usermgmt: " + commandArray[1] + " - Command Not Found");
                    break;
                }
                viewSycoraxInformation();
                break;

                default:
                Anvil.anvilInterpreter(commandArray);
                break;
            }
    }

    private boolean login() throws Exception
    {
        viewSycoraxInformation();
        IOStreams.printInfo("Authentication Attempts Left: " + _loginAttemptsRemaining);
        _username = Cryptography.stringToSHA3_256(console.readLine("> Username: "));
        String password = Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> Password: ")));
        String securityKey = Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> Security Key: ")));

        return new Login(_username).authenticationLogic(password, securityKey);
    }

    private void loginCounterLogic() throws Exception
    {
        _loginAttemptsRemaining--;
        if(_loginAttemptsRemaining <= 0)
        {
            IOStreams.printError("Authentication Attempts Exceeded! Further attempts are locked!");
            Thread.sleep(36000);
        }
    }

    private void fetchUserDetails()throws Exception
    {
        _accountName = new Login(_username).getNameLogic();
        _isUserAdministrator = new Login(_username).checkPrivilegeLogic();
        _userUnlockPIN = new Login(_username).getPINLogic();
        _systemName = new PolicyCheck().retrievePolicyValue("sysname");

        _prompt = _isUserAdministrator?'!':'*';
    }

    private void viewSycoraxInformation()
    {
        Build.clearScreen();
        IOStreams.println(Build._Branding);
        IOStreams.println("Powered by Sycorax Kernel.\n");
    }

    private void lockConsole() throws Exception
    {
        viewSycoraxInformation();

        String input = "";

        do
        {
            input = console.readLine((char)27 + "[33;49m" + new Time().getDateTimeUsingSpecifiedFormat("yyyy-MMM-dd HH:mm:ss") + "  LOCKED\n" + _accountName + "@Cataphract" + _prompt + "> " + (char)27 + "[0m");
        }
        while(!input.equalsIgnoreCase("unlock"));
        
        IOStreams.printAttention("Please Enter Unlock PIN To Continue.");

        while(!challengePIN())
        {
            IOStreams.printError("Incorrect PIN.");
            loginCounterLogic();
        }
        viewSycoraxInformation();
    }

    private boolean challengePIN() throws Exception
    {
        String userPIN = Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> PIN : "))) ;
        return userPIN.equals(_userUnlockPIN);
    }

    private boolean anvilScriptEngine(String scriptFileName)throws Exception
    {
        boolean status = false;

        if(! _isUserAdministrator && ! new Cataphract.API.Minotaur.PolicyCheck().retrievePolicyValue("script").equals("on"))
        {
            //to process the anvil script files
            try
            {
                //Check if the name of the script file is a valid string
                if(scriptFileName == null || scriptFileName.equalsIgnoreCase("") || scriptFileName.startsWith(" "))
                IOStreams.printError("The name of the script file cannot be be blank.");

                //Check if the script file specified exists.
                else if(! new File(scriptFileName).exists())
                //Return an error and pass the control back in case the file is not found.
                IOStreams.printAttention("Script file "+scriptFileName.replace(_username, _accountName)+" has not been found.\nPlease check the directory of the script file and try again.");

                else
                {
                    //Activate the script mode.
                    _scriptMode = true;

                    //Initialize a stream to read the given file.
                    BufferedReader br = new BufferedReader(new FileReader(scriptFileName));

                    //Initialize a string to hold the contents of the script file being executed.
                    String scriptLine = "";

                    //Read the script file, line by line.
                    while ((scriptLine = br.readLine()) != null)
                    {
                        //Check if the line is a comment or is blank in the script file and skip the line.
                        if(scriptLine.startsWith("#") || scriptLine.equalsIgnoreCase(""))
                        continue;

                        //Check if End Script command is encountered, which will stop the execution of the script.
                        else if(scriptLine.equalsIgnoreCase("End Script"))
                        break;

                        //Read the command in the script file, and pass it on to menuLogic(<command>) for it to be processed.
                        commandProcessor(scriptLine);
                        _scriptLineNumber++;
                    }

                    //Close the streams, run the garbage collector and clean.
                    br.close();
                    _scriptLineNumber = 0;
                    System.gc();

                    //Deactivate the script mode.
                    _scriptMode = false;
                }
            }
            catch(Exception e)
            {
                //handle exception
            }
        }
        else
        {
            IOStreams.printError("Insufficient Privileges to run scripts! Please contact the Administrator for more information.");
        }
        return status;
    }
}