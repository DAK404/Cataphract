package Cataphract.Core;

import java.io.Console;

import Cataphract.API.Anvil;
import Cataphract.API.Build;
import Cataphract.API.IOStreams;

import Cataphract.API.Astaroth.Time;

import Cataphract.API.Dragon.AccountCreate;
import Cataphract.API.Dragon.AccountDelete;
import Cataphract.API.Dragon.AccountModify;
import Cataphract.API.Dragon.Login;

import Cataphract.API.Minotaur.Cryptography;

public class SycoraxKernel
{

    private String _accountName = "";
    private String _username = "";
    private String _userUnlockPIN = "";
    
    private boolean _isUserAdministrator = false;

    private char _prompt = '?';

    private int _loginAttemptsRemaining = 5;

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
        String[] commandArray;
        String input = "";

        do
        {
            input = console.readLine(_accountName + "@Cataphract" + _prompt + "> ");

            commandArray = Anvil.splitStringToArray(input);

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

                case "clear":
                viewSycoraxInformation();
                break;

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
        while(!input.equalsIgnoreCase("logout"));
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

    private boolean scriptParseLogic(String scriptFileName) throws Exception
    {
        return false;
    }
}