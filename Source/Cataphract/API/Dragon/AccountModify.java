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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import Cataphract.API.Build;
import Cataphract.API.IOStreams;

/**
 * A class to modify user accounts on the system. Can be restricted by policy "account_modify"
 *
 * @author DAK404 (https://github.com/DAK404)
 * @version 3.9.7 (20-February-2024, Cataphract)
 * @since 0.0.1 (Zen Quantum 0.0.1)
 */
public class AccountModify
{
    /** Stores the current username */
    private String _currentUsername = "";
    /** Stores the current account name */
    private String _currentAccountName = "";
    /** Stores the value of the user's privileges */
    private boolean _currentAccountAdmin = false;

    /** Stores the account name policy */
    private final String _accountNamePolicy = AccountCreate._accountNamePolicy;
    /** Stores the account password policy */
    private final String _accountPasswordPolicy = AccountCreate._accountPasswordPolicy;
    /** Stores the account security key policy */
    private final String _accountKeyPolicy = AccountCreate._accountSecurityKeyPolicy;
    /** Stores the account PIN policy */
    private final String _accountPINPolicy = AccountCreate._accountPINPolicy;

    /** Stores the user targeted with the changes */
    private String targetUser = "";

    /** Instantiate the Console class to accept console inputs */
    private Console console = System.console();

    /**
     * Constructor to store details about the current user to global variables
     *
     * @param user Name of the user currently logged in
     * @throws Exception Any exceptions thrown during the execution of the program
     */
    public AccountModify(String user)throws Exception
    {
        _currentUsername = user;
        _currentAccountName = new Login(user).getNameLogic();
        _currentAccountAdmin = new Login(user).checkPrivilegeLogic();
    }

    /**
     * Method to execute account modification logic
     *
     * @throws Exception Any exceptions thrown during the execution of the program
     */
    public final void accountModifyLogic()throws Exception
    {
        // Trigger garbage collection
        System.gc();

        // Check login credentials
        if(!login())
            IOStreams.printError("Incorrect Credentials! Aborting...");
        else
            // Proceed to account management menu
            accountManagementMenu();

        // Trigger garbage collection
        System.gc();
    }

    /**
     * Method to handle user authentication
     *
     * @return true if authentication is successful, false otherwise
     * @throws Exception Any exceptions thrown during the execution of the program
     */
    private boolean login()throws Exception
    {
        // Display build information
        Build.viewBuildInfo();
        // Prompt user for authentication
        IOStreams.printAttention("Please authenticate to continue.");
        IOStreams.println("Username: " + _currentAccountName);
        // Read password and security key
        String password = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String key = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

        // Authenticate user
        return new Login(_currentUsername).authenticationLogic(password, key);
    }

    /**
     * Method to display and manage account modification menu
     *
     * @throws Exception Any exceptions thrown during the execution of the program
     */
    private void accountManagementMenu()throws Exception
    {
        // Display build information
        Build.viewBuildInfo();

        String tempInput;

        // Display account management menu
        accountManagementMenuDisplay();

        do
        {
            // Read user input
            tempInput = console.readLine(_currentAccountName + "} ");

            targetUser = _currentUsername;

            String[] usermgmtModifyCommandArray = Cataphract.API.Anvil.splitStringToArray(tempInput);

            // Switch statement for different menu options
            switch(usermgmtModifyCommandArray[0].toLowerCase())
            {
                case "name":
                    changeAccountName();
                    break;
                case "password":
                    changeAccountPassword();
                    break;
                case "key":
                    changeAccountSecurityKey();
                    break;
                case "pin":
                    changeAccountPIN();
                    break;
                case "promote":
                case "demote":
                    if(usermgmtModifyCommandArray.length < 2)
                        IOStreams.printError("Invalid Syntax. Use: promote <target_username>\nOR\ndemote <target_username>");
                    else
                        accountPromoteDemoteLogic(usermgmtModifyCommandArray[0], usermgmtModifyCommandArray[1]);
                    break;
                case "clear":
                    accountManagementMenuDisplay();
                    break;
                default:
                    break;
            }
        } while(!tempInput.equalsIgnoreCase("exit"));

        // Trigger garbage collection
        System.gc();
    }

    /**
     * Method to display the account management menu
     */
    private void accountManagementMenuDisplay()
    {
        // Display build information
        Build.viewBuildInfo();
        IOStreams.println("-------------------------------------------------");
        IOStreams.println("| User Management Console: Account Modification |");
        IOStreams.println("-------------------------------------------------\n");

        IOStreams.println("How do you wish to manage or modify your account?\n");

        IOStreams.println("[1] Change Account Name");
        IOStreams.println("[2] Change Account Password");
        IOStreams.println("[3] Change Account Security Key");
        IOStreams.println("[4] Change Session Unlock PIN\n");

        IOStreams.println("[ NAME | PASSWORD | KEY | PIN | HELP | EXIT ]");
        if(_currentAccountAdmin)
        {
            IOStreams.println(1, 8, "\n       [ DANGER ZONE ]       ");
            IOStreams.println(1, 8, "--- ADMINISTRATOR TOOLKIT ---");
            IOStreams.println(1, 8,"[!] Promote Account to Administrator");
            IOStreams.println(1, 8,"[!] Demote Account to Standard User\n");

            IOStreams.println(1, 8,"[ PROMOTE | DEMOTE ]");
        }
        IOStreams.println("");
    }

    //action is either promote or demote, same as commandArray[0]
    private void accountPromoteDemoteLogic(String action, String targetUser)throws Exception
    {
        //Reject any attempts to promote or demote if specified user is an Administrator
        if(targetUser.equalsIgnoreCase("Administrator"))
            IOStreams.printError("Cannot promote or demote the user Administrator.");
        else
        {
            //encode the selected user to a hashed format
            targetUser = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(targetUser);

            //Check if the specified user exists in the database
            if(new Login(targetUser).checkUserExistence())
            {
                //Confirm if the user selected is to be promoted to or demoted from an account with administrator rights
                IOStreams.printAttention("YOU ARE ABOUT TO " + action.toUpperCase() + " \""  + new Login(targetUser).getNameLogic() + "\". ARE YOU SURE? [ Y | N ]");

                //If user confirms, then commit the changes to the database
                if(console.readLine("Change Privileges?> ").equalsIgnoreCase("y"))
                {
                    //Update the values in the database to reflect the changes
                    switch(action)
                    {
                        //updates the Administrator column value to Yes in case of promote
                        case "promote":
                            commitChangesToDatabase("Privileges", "Yes", targetUser);
                            break;

                        //updates the Administrator column value to No in case of demote
                        case "demote":
                            commitChangesToDatabase("Privileges", "No", targetUser);
                            break;
                    }
                }
            }
            else
                IOStreams.printError("Specified User does not exist!");
        }
        // Trigger garbage collection
        System.gc();
    }

    /**
     * Method to change account name
     */
    private void changeAccountName()
    {
        String _newAccountName = console.readLine(_accountNamePolicy + "Account Name> ");

        if(_newAccountName == null || _newAccountName.contains(" ") || _newAccountName.equals("") || !(_newAccountName.matches("^[a-zA-Z0-9]*$")) || _newAccountName.equalsIgnoreCase("Administrator") || _newAccountName.length() < 2)
            console.readLine("Invalid Account Name. Press ENTER to try again.");
        else
            commitChangesToDatabase("Name", _newAccountName, targetUser);

        // Trigger garbage collection
        System.gc();
    }

    /**
     * Method to change account password
     *
     * @throws Exception Any exceptions thrown during the execution of the program
     */
    private void changeAccountPassword()throws Exception
    {
        String _newPassword = String.valueOf(console.readPassword(_accountPasswordPolicy + "Account Password> "));
        String confirmPassword = String.valueOf(console.readPassword("Confirm Password> "));

        if(_newPassword == null || _newPassword.equals("") || _newPassword.length() < 8 || !(_newPassword.equals(confirmPassword)))
            console.readLine("Invalid Account Password. Press ENTER to try again.");
        else
            commitChangesToDatabase("Password", new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newPassword), targetUser);

        // Trigger garbage collection
        System.gc();
    }

    /**
     * Method to change account security key
     *
     * @throws Exception Any exceptions thrown during the execution of the program
     */
    private void changeAccountSecurityKey()throws Exception
    {
        String _newSecKey = String.valueOf(console.readPassword(_accountKeyPolicy + "Account Security Key> "));
        String confirmKey = String.valueOf(console.readPassword("Confirm Security Key> "));

        if(_newSecKey == null || !(_newSecKey.equals(confirmKey)))
            console.readLine("Invalid Account Security Key. Press ENTER to try again.");
        else
            commitChangesToDatabase("Password", new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newSecKey), targetUser);

        // Trigger garbage collection
        System.gc();
    }

    /**
     * Method to change account PIN
     *
     * @throws Exception Any exceptions thrown during the execution of the program
     */
    private void changeAccountPIN()throws Exception
    {
        String _newPIN = String.valueOf(console.readPassword(_accountPINPolicy + "Account Key> "));
        String confirmPIN = String.valueOf(console.readPassword("Confirm PIN> "));

        if(_newPIN == null || !(_newPIN.equals(confirmPIN)))
            console.readLine("Invalid Account PIN. Press ENTER to try again.");
        else
            commitChangesToDatabase("PIN", new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newPIN), targetUser);

        // Trigger garbage collection
        System.gc();
    }

    /**
     * Method to commit changes to the database
     *
     * @param parameter The parameter to be updated
     * @param value The new value for the parameter
     * @param targetUser The target user for the changes
     */
    private void commitChangesToDatabase(String parameter, String value, String targetUser)
    {
        try
        {
            String databasePath = "jdbc:sqlite:./System/Cataphract/Private/Mud.dbx";
            String sqlCommand = "UPDATE MUD SET " + parameter + " = ? WHERE Username = ?";

            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection(databasePath);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);

            preparedStatement.setString(1, value);
            preparedStatement.setString(2, targetUser);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            dbConnection.close();

            IOStreams.printInfo("Account Modification Successful!");
        }
        catch(Exception e)
        {
            IOStreams.printInfo("Account Modification Failed.");
        }
        // Trigger garbage collection
        System.gc();
    }
}
