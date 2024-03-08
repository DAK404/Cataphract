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

package Cataphract.API.Dragon;

import java.io.Console;
import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import Cataphract.API.Build;
import Cataphract.API.IOStreams;

/**
 * A class to create new user accounts on the system. Can be restricted by policy "account_create"
 *
 * @author DAK404 (https://github.com/DAK404)
 * @version 3.9.7 (20-February-2024, Cataphract)
 * @since 0.0.1 (Zen Quantum 0.0.1)
 */
public final class AccountCreate
{

    /**
     * String to display rules for setting the account names
     */
    protected final static String _accountNamePolicy = """
    Account Name Policy Information
    -------------------------------
    * Name cannot be \'Administrator\'
    * Name must contain English Alphabet, can have numbers
    * Name must have atleast 2 characters or more
    * Name cannot contain spaces
    -------------------------------
    """;

    /**
     * String to display rules for setting the account usernames
     */
    protected final static String _accountUsernamePolicy = """
    Account Username Policy Information
    -----------------------------------
    * Username cannot contain the word \'Administrator\'
    * Username can contain numbers, special characters and symbols.
    -----------------------------------
    """;

    /**
     * String to display rules for setting the account passwords
     */
    protected final static String _accountPasswordPolicy = """
    Account Password Policy Information
    -----------------------------------
    * Password must contain atleast 8 characters
    * Password is recommended to have special characters and numbers
    -----------------------------------
    """;

    /**
     * String to display rules for setting the account Security Key
     */
    protected final static String _accountSecurityKeyPolicy = """
    Account Security Key Policy Information
    -----------------------------------
    * Security Key must contain atleast 8 characters
    * Security Key is recommended to have special characters and numbers
    -----------------------------------
    """;

    /**
     * String to display rules for setting account PIN
     */
    protected final static String _accountPINPolicy = """
    Account PIN Policy Information
    -------------------------------
    * PIN must contain atleast 4 characters
    * PIN is recommended to have special characters and numbers
    -------------------------------
    """;

    /**
     * String to store the current user
     */
    private String _currentUsername = "DEFAULT";

    /**
     * Boolean to store whether the current account is an admin
     */
    private boolean _currentAccountAdmin = false;

    /**
     * String to store the new account name
     */
    private String _newAccountName = "";

    /**
     * String to store the new account username
     */
    private String _newAccountUsername = "";

    /**
     * String to store the new account password
     */
    private String _newAccountPassword = "";

    /**
     * String to store the new account Security Key
     */
    private String _newAccountSecurityKey = "";

    /**
     * String to store the new account PIN
     */
    private String _newAccountPIN = "";

    /**
     * Boolean to store whether the new account is an admin
     */
    private boolean _newAccountAdmin = false;

    /**
    * Instantiate Console to get user inputs
    */
    private Console console = System.console();

    /**
     * Logic that will help in creating a new user in Cataphract
     *
     * @param username Value of the current username
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    public final void accountCreateLogic(String username)throws Exception
    {
        //Store the current username locally to _currentUsername
        _currentUsername = username;
        _currentAccountAdmin = new Cataphract.API.Dragon.Login(_currentUsername).checkPrivilegeLogic();

        if(new Cataphract.API.Minotaur.PolicyCheck().retrievePolicyValue("account_create").equals("on") || _currentAccountAdmin)
        {
            //If the authentication check fails, exit from the module
            if(! authenticateCurrentUser())
                IOStreams.println("Failed to authenticate user. Exiting...");
            //Else, continue with the user creation logic
            else
            {
                //Clear the screen and view the build information
                Build.viewBuildInfo();

                //If the current user has administrator rights, ask if the new accounts should have the administrator rights too
                if(_currentAccountAdmin)
                {
                    IOStreams.printAttention("The currently logged in user is an administrator.\nYou have the privileges to create other administrator accounts or standard user accounts.\n");
                    IOStreams.printWarning("Administrative rights have additional privileges over standard users! Beware on who the administrative privileges are granted to!\n");
                    IOStreams.println("Would you like to grant administrative privileges to the new user account? [ Y | N ]");
                    if(console.readLine("Grant Administrator Privileges?> ").equalsIgnoreCase("Y"))
                        _newAccountAdmin = true;
                }

                while(!setAccountName());
                while(!setAccountUsername());
                while(!setAccountPassword());
                while(!setAccountSecurityKey());
                while(!setAccountPIN());
                addAccountToDatabase();
                IOStreams.confirmReturnToContinue();
            }
        }
        else
            IOStreams.printError("Policy Configuration Error!");
        System.gc();
    }

    private final boolean authenticateCurrentUser()throws Exception
    {
        Build.viewBuildInfo();

        boolean authenticationStatus = false;

        try
        {
            IOStreams.println("Username: " + new Cataphract.API.Dragon.Login(_currentUsername).getNameLogic());

            authenticationStatus = new Cataphract.API.Dragon.Login(_currentUsername).authenticationLogic(new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("SecurityKey: "))));
        }
        catch(Exception e)
        {
            new Cataphract.API.ExceptionHandler().handleException(e);
        }

        return authenticationStatus;
    }

    //Displays a dashboard to the user, pertaining to the details entered.
    private final void credentialDashboard()
    {
        Build.viewBuildInfo();

        IOStreams.println("-------------------------------------------------");
        IOStreams.println("| User Management Console: Account Creation     |");
        IOStreams.println("-------------------------------------------------\n");

        IOStreams.println("Account Name  : " + (_newAccountName.equalsIgnoreCase("")?"NOT SET":_newAccountName));
        IOStreams.println("Username      : " + (_newAccountUsername.equalsIgnoreCase("")?"NOT SET":_newAccountUsername));
        IOStreams.println("Password      : " + (_newAccountPassword.equalsIgnoreCase("")?"NOT SET":"********"));
        IOStreams.println("SecurityKey   : " + (_newAccountSecurityKey.equalsIgnoreCase("")?"NOT SET":"********"));
        IOStreams.println("PIN           : " + (_newAccountPIN.equalsIgnoreCase("")?"NOT SET":"****") + "\n");
        IOStreams.printAttention("Account Privileges: " + (_newAccountAdmin?"Administrator":"Standard") + "\n");
        IOStreams.println("========================================");
    }

    private final boolean setAccountName()throws Exception
    {
        boolean status = false;
        credentialDashboard();

        _newAccountName = console.readLine(_accountNamePolicy + "Account Name> ");

        if(_newAccountName == null | _newAccountName.contains(" ") | _newAccountName.equals("") | !(_newAccountName.matches("^[a-zA-Z0-9]*$")) | _newAccountName.equalsIgnoreCase("Administrator") | _newAccountName.length() < 2)
        {
            _newAccountName = "";
            status = false;
            console.readLine("Invalid Account Name. Press ENTER to try again.");
        }
        else
            status = true;

        return status;
    }

    private final boolean setAccountUsername()throws Exception
    {
        boolean status = false;
        credentialDashboard();

        _newAccountUsername = (console.readLine(_accountUsernamePolicy + "Account Username> "));

        if(new Cataphract.API.Dragon.Login(_newAccountUsername).checkUserExistence())
        {
            IOStreams.printError("Username has already been enrolled! Please try again with another username.");
            _newAccountUsername = "";
            console.readLine();
        }
        else if(_newAccountUsername == null | _newAccountUsername.equals("") | _newAccountUsername.equalsIgnoreCase("Administrator"))
        {
            _newAccountUsername = "";
            console.readLine("Invalid Account Username. Press ENTER to try again.");
        }
        else
        {
            status = true;
            _newAccountUsername = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newAccountUsername);
        }

        return status;
    }

    private final boolean setAccountPassword()throws Exception
    {
        boolean status = false;
        credentialDashboard();

        _newAccountPassword = String.valueOf(console.readPassword(_accountPasswordPolicy + "Account Password> "));
        String confirmPassword = String.valueOf(console.readPassword("Confirm Password> "));

        if(_newAccountPassword == null | _newAccountPassword.equals("") | _newAccountPassword.length() < 8 | !(_newAccountPassword.equals(confirmPassword)))
        {
            _newAccountPassword = "";
            confirmPassword = "";
            console.readLine("Invalid Account Password. Press ENTER to try again.");
        }
        else
        {
            status = true;
            _newAccountPassword = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newAccountPassword);
        }

        return status;
    }

    private final boolean setAccountSecurityKey()throws Exception
    {
        boolean status = false;
        credentialDashboard();

        _newAccountSecurityKey = String.valueOf(console.readPassword(_accountSecurityKeyPolicy + "Account Security Key> "));
        String confirmKey = String.valueOf(console.readPassword("Confirm Security Key> "));

        if(_newAccountSecurityKey == null | !(_newAccountSecurityKey.equals(confirmKey)))
        {
            _newAccountSecurityKey = "";
            confirmKey = "";
            console.readLine("Invalid Account Security Key. Press ENTER to try again.");
        }
        else
        {
            status = true;
            _newAccountSecurityKey = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newAccountSecurityKey);
        }

        return status;
    }

    private final boolean setAccountPIN()throws Exception
    {
        boolean status = false;
        credentialDashboard();

        _newAccountPIN = String.valueOf(console.readPassword(_accountPINPolicy + "Account PIN> "));
        String confirmPIN = String.valueOf(console.readPassword("Confirm PIN> "));

        if(_newAccountPIN == null | _newAccountPIN.equals("") | _newAccountPIN.length() < 4 | !(_newAccountPIN.equals(confirmPIN)))
        {
            _newAccountPIN = "";
            confirmPIN = "";
            console.readLine("Invalid Account PIN. Press ENTER to try again.");
        }
        else
        {
            status = true;
            _newAccountPIN = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newAccountPIN);
        }

        return status;
    }

    private final void addAccountToDatabase()
    {
        try
        {
            String databasePath = "jdbc:sqlite:./System/Cataphract/Private/Mud.dbx";
            String sqlCommand = "INSERT INTO MUD(Username, Name, Password, SecurityKey, PIN, Privileges) VALUES(?,?,?,?,?,?)";

            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection(databasePath);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);

            preparedStatement.setString(1, _newAccountUsername);
            preparedStatement.setString(2, _newAccountName);
            preparedStatement.setString(3, _newAccountPassword);
            preparedStatement.setString(4, _newAccountSecurityKey);
            preparedStatement.setString(5, _newAccountPIN);
            preparedStatement.setString(6, (_newAccountAdmin?"Yes":"No"));

            preparedStatement.executeUpdate();

            preparedStatement.close();
            dbConnection.close();

            new File("./Users/Cataphract/"+_newAccountUsername).mkdirs();

            System.gc();

            credentialDashboard();
            IOStreams.printInfo("Account Creation Successful!");
        }
        catch(Exception e)
        {
            IOStreams.printInfo("Account Creation Failed.");
        }
    }

    public final void createDefaultAdministratorAccount()throws Exception
    {
        _newAccountAdmin = true;
        _newAccountName = "Administrator";
        _newAccountUsername = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256("Administrator");

        IOStreams.println("Administrator : " + _newAccountAdmin);
        IOStreams.println("Account Name  : " + _newAccountName);
        IOStreams.println("Username      : " + _newAccountUsername);

        while(!setAccountPassword());
        while(!setAccountSecurityKey());
        while(!setAccountPIN());

        addAccountToDatabase();

        System.gc();

        Cataphract.API.IOStreams.confirmReturnToContinue();
    }
}