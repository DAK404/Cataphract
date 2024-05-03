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
import Cataphract.API.Minotaur.Cryptography;

/**
* A class to create new user accounts on the system. Can be restricted by policy "account_create"
*
* @author DAK404 (https://github.com/DAK404)
* @version 3.9.7 (20-February-2024, Cataphract)
* @since 0.0.1 (Zen Quantum 0.0.1)
*/
public final class AccountCreate
{
    /** String to display rules for setting the account names */
    protected final static String _accountNamePolicy = """
    Account Name Policy Information
    -------------------------------
    * Name cannot be \'Administrator\'
    * Name must contain English Alphabet, can have numbers
    * Name must have atleast 2 characters or more
    * Name cannot contain spaces
    -------------------------------
    """;

    /** String to display rules for setting the account usernames */
    protected final static String _accountUsernamePolicy = """
    Account Username Policy Information
    -----------------------------------
    * Username cannot contain the word \'Administrator\'
    * Username can contain numbers, special characters and symbols.
    -----------------------------------
    """;

    /** String to display rules for setting the account passwords */
    protected final static String _accountPasswordPolicy = """
    Account Password Policy Information
    -----------------------------------
    * Password must contain atleast 8 characters
    * Password is recommended to have special characters and numbers
    -----------------------------------
    """;

    /** String to display rules for setting the account Security Key */
    protected final static String _accountSecurityKeyPolicy = """
    Account Security Key Policy Information
    -----------------------------------
    * Security Key must contain atleast 8 characters
    * Security Key is recommended to have special characters and numbers
    -----------------------------------
    """;

    /** String to display rules for setting account PIN */
    protected final static String _accountPINPolicy = """
    Account PIN Policy Information
    -------------------------------
    * PIN must contain atleast 4 characters
    * PIN is recommended to have special characters and numbers
    -------------------------------
    """;

    /** String to store the current user */
    private String _currentUsername = "DEFAULT";

    /** Boolean to store whether the current account is an admin */
    private boolean _currentAccountAdmin = false;

    /** String to store the new account name */
    private String _newAccountName = "";

    /** String to store the new account username */
    private String _newAccountUsername = "";

    /** String to store the new account password */
    private String _newAccountPassword = "";

    /** String to store the new account Security Key */
    private String _newAccountSecurityKey = "";

    /** String to store the new account PIN */
    private String _newAccountPIN = "";

    /** Boolean to store whether the new account is an admin */
    private boolean _newAccountAdmin = false;

    /** Instantiate Console to get user inputs */
    private Console console = System.console();

    /**
    * Constructor that can be used to create an administrator account during setup.
    */
    public AccountCreate()
    {
    }

    /**
    * Constructor for AccountCreate class
    *
    * @param username The currently logged in username.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public AccountCreate(String username) throws Exception
    {
        //Store the current username locally to _currentUsername
        _currentUsername = username;
        _currentAccountAdmin = new Cataphract.API.Dragon.Login(_currentUsername).checkPrivilegeLogic();
    }

    /**
    * Logic that will help in creating a new user in Cataphract
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public final void accountCreateLogic()throws Exception
    {
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

                //Accept the credential data from the user, hash it if necessary and store it in variables.

                //Accept the values for account name, that follows the given policy
                while(!setAccountName());

                //Accept the values for account name, that follows the given policy
                while(!setAccountUsername());

                //Accept the values for account password, that follows the given policy
                while(!setAccountPassword());

                //Accept the values for account Security Key, that follows the given policy
                while(!setAccountSecurityKey());

                //Accept the values for account PIN, that follows the given policy
                while(!setAccountPIN());

                //Finally, write the credential values to the database
                addAccountToDatabase();

                IOStreams.confirmReturnToContinue();
            }
        }
        //If the policy for usermanagement system is not setup, incorrect or the policy file is corrupt, display the same
        else
        IOStreams.printError("Policy Configuration Error!");
        System.gc();
    }

    /**
    * Logic to authenticate the current user logged in.
    *
    * @return {@code true} if the user creation was successful, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final boolean authenticateCurrentUser()throws Exception
    {
        //Clear the screen, view the build information
        Build.viewBuildInfo();

        //Common variable used to store the status of the authentication
        boolean authenticationStatus = false;

        try
        {
            //Display the name of the user currently logged in
            IOStreams.println("Username: " + new Cataphract.API.Dragon.Login(_currentUsername).getNameLogic());

            new Cataphract.API.Minotaur.Cryptography();
            //challenge the database for the provided credentials, and store the status
            authenticationStatus = new Cataphract.API.Dragon.Login(_currentUsername).authenticationLogic(Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("SecurityKey: "))));
        }
        catch(Exception e)
        {
            //Handle any exceptions encountered
            new Cataphract.API.ExceptionHandler().handleException(e);
        }

        //return the status value
        return authenticationStatus;
    }

    /**
    * Logic to print the credential dashboard to the user
    */
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

    /**
    * Logic to set the account name. This is displayed in the shell.
    *
    * @return {@code true} if the name adheres to the policies and is valid, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final boolean setAccountName()throws Exception
    {
        boolean status = false;

        // Display the credential dashboard
        credentialDashboard();

        // set the value of the account name
        _newAccountName = console.readLine(_accountNamePolicy + "Account Name> ");

        // Check if the account name entered is valid
        if(_newAccountName == null | _newAccountName.contains(" ") || _newAccountName.equals("") || !(_newAccountName.matches("^[a-zA-Z0-9]*$")) || _newAccountName.equalsIgnoreCase("Administrator") || _newAccountName.length() < 2)
        {
            _newAccountName = "";
            status = false;
            console.readLine("Invalid Account Name. Press ENTER to try again.");
        }
        else
        status = true;

        return status;
    }

    /**
    * Logic to set the account username. This is displayed in the shell.
    *
    * @return {@code true} if the username adheres to the policies and is valid, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final boolean setAccountUsername()throws Exception
    {
        boolean status = false;

        // Display the credential dashboard
        credentialDashboard();

        // set the value of the account username
        _newAccountUsername = (console.readLine(_accountUsernamePolicy + "Account Username> "));

        // check if the entered username exists in the database already
        if(new Cataphract.API.Dragon.Login(_newAccountUsername).checkUserExistence())
        {
            IOStreams.printError("Username has already been enrolled! Please try again with another username.");
            _newAccountUsername = "";
            console.readLine();
        }

        // else if, check if the username is a valid username
        else if(_newAccountUsername == null || _newAccountUsername.equals("") || _newAccountUsername.equalsIgnoreCase("Administrator"))
        {
            _newAccountUsername = "";
            console.readLine("Invalid Account Username. Press ENTER to try again.");
        }

        // else, hash the username and store it
        else
        {
            // set return status to true to denote that the username entered is valid
            status = true;
            _newAccountUsername = Cryptography.stringToSHA3_256(_newAccountUsername);
        }

        // return the status
        return status;
    }

    /**
    * Logic to set the account password.
    *
    * @return {@code true} if the password adheres to the policies and is valid, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final boolean setAccountPassword()throws Exception
    {
        boolean status = false;

        // Display the credential dashboard
        credentialDashboard();

        // set the value of the account password
        _newAccountPassword = String.valueOf(console.readPassword(_accountPasswordPolicy + "Account Password> "));

        // Confirm the password entered
        String confirmPassword = String.valueOf(console.readPassword("Confirm Password> "));

        // Compare if the passwords entered are the same or not, and checking if the password length is greater than 8 characters

        // if the password is invalid or does not follow the policy, the user needs to try again
        if(_newAccountPassword == null || _newAccountPassword.equals("") || _newAccountPassword.length() < 8 || !(_newAccountPassword.equals(confirmPassword)))
        {
            // clear old credentials to avoid ambiguity
            _newAccountPassword = "";
            confirmPassword = "";
            console.readLine("Invalid Account Password. Press ENTER to try again.");
        }

        // else, hash the credentials and store it
        else
        {
            // set return status to true to denote that the password entered is valid
            status = true;
            _newAccountPassword = Cryptography.stringToSHA3_256(_newAccountPassword);
        }

        // return the status
        return status;
    }

    /**
    * Logic to set the account security key.
    *
    * @return {@code true} if the security key adheres to the policies and is valid, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final boolean setAccountSecurityKey()throws Exception
    {
        boolean status = false;

        // Display the credential dashboard
        credentialDashboard();

        // set the value of the account security key
        _newAccountSecurityKey = String.valueOf(console.readPassword(_accountSecurityKeyPolicy + "Account Security Key> "));

        // Confirm the security key entered
        String confirmKey = String.valueOf(console.readPassword("Confirm Security Key> "));

        // Compare if the security keys match

        // if the security key is not the same as confirmation, the user needs to try again
        if(_newAccountSecurityKey == null || !(_newAccountSecurityKey.equals(confirmKey)))
        {
            // clear old credentials to avoid ambiguity
            _newAccountSecurityKey = "";
            confirmKey = "";
            console.readLine("Invalid Account Security Key. Press ENTER to try again.");
        }

        // else, hash the credentials and store it
        else
        {
            // set return status to true to denote that the security key entered is valid
            status = true;
            _newAccountSecurityKey = Cryptography.stringToSHA3_256(_newAccountSecurityKey);
        }

        // return the status
        return status;
    }

    /**
    * Logic to set the account PIN
    *
    * @return {@code true} if the account PIN adheres to the policies and is valid, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final boolean setAccountPIN()throws Exception
    {
        boolean status = false;

        // Display the credential dashboard
        credentialDashboard();

        // set the value of the account PIN
        _newAccountPIN = String.valueOf(console.readPassword(_accountPINPolicy + "Account PIN> "));

        // Confirm the PIN entered
        String confirmPIN = String.valueOf(console.readPassword("Confirm PIN> "));

        // if the PIN is not the same as confirmation, or the PIN length is lesser than 4, the user needs to try again
        if(_newAccountPIN == null || _newAccountPIN.equals("") || _newAccountPIN.length() < 4 || !(_newAccountPIN.equals(confirmPIN)))
        {
            // clear old credentials to avoid ambiguity
            _newAccountPIN = "";
            confirmPIN = "";
            console.readLine("Invalid Account PIN. Press ENTER to try again.");
        }

        // else, hash the credentials and store it
        else
        {
            // set return status to true to denote that the security key entered is valid
            status = true;
            _newAccountPIN = Cryptography.stringToSHA3_256(_newAccountPIN);
        }

        return status;
    }

    /**
    * Write the credentials stored in the variables into the database.
    */
    private final void addAccountToDatabase()
    {
        try
        {
            // Store the path of the database
            String databasePath = "jdbc:sqlite:./System/Cataphract/Private/Mud.dbx";

            // Store the SQL command that needs to be executed by the SQLite database engine
            String sqlCommand = "INSERT INTO MUD(Username, Name, Password, SecurityKey, PIN, Privileges) VALUES(?,?,?,?,?,?)";

            // Initialize the JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create a connection to the database
            Connection dbConnection = DriverManager.getConnection(databasePath);

            // Using PreparedStatement, create the statement that needs to be executed by the database engine
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);

            // Substitute appropriate values
            preparedStatement.setString(1, _newAccountUsername);
            preparedStatement.setString(2, _newAccountName);
            preparedStatement.setString(3, _newAccountPassword);
            preparedStatement.setString(4, _newAccountSecurityKey);
            preparedStatement.setString(5, _newAccountPIN);
            preparedStatement.setString(6, (_newAccountAdmin?"Yes":"No"));

            // Execute the statement
            preparedStatement.executeUpdate();

            // Close the PreparedStatement resource
            preparedStatement.close();

            // Close the connection to the database
            dbConnection.close();

            //Create directory for the new user account
            new File("./Users/Cataphract/"+_newAccountUsername).mkdirs();

            // Invoke garbage collector to free resources up
            System.gc();

            // Display the credential dashboard
            credentialDashboard();

            // Notify the user that the account creation was successful
            IOStreams.printInfo("Account Creation Successful!");
        }

        // If there are any exceptions, notify that the account creation failed
        catch(Exception e)
        {
            IOStreams.printError("Account Creation Failed.");
        }
    }

    /**
    * Create a default Administrator account during the setup.
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public final void createDefaultAdministratorAccount()throws Exception
    {
        // Check if the Administrator account exists already. If not, then create the Administrator account
        if(!new Cataphract.API.Dragon.Login("Administrator").checkUserExistence())
        {
            // Set the parameters for the Administrator account
            _newAccountAdmin = true;
            _newAccountName = "Administrator";
            _newAccountUsername = Cryptography.stringToSHA3_256("Administrator");

            IOStreams.println("Administrator : " + _newAccountAdmin);
            IOStreams.println("Account Name  : " + _newAccountName);
            IOStreams.println("Username      : " + _newAccountUsername);

            // Accept the values for account password, that follows the given policy
            while(!setAccountPassword());

            // Accept the values for account Security Key, that follows the given policy
            while(!setAccountSecurityKey());

            // Accept the values for account PIN, that follows the given policy
            while(!setAccountPIN());

            // Add the Administrator account to the database
            addAccountToDatabase();

            // Invoke garbage collector to free resources up
            System.gc();

            IOStreams.confirmReturnToContinue();
        }
    }
}