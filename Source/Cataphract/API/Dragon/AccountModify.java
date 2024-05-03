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
    * @throws Exception Throws any exceptions encountered during runtime.
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
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public final void accountModifyLogic()throws Exception
    {
        if(new Cataphract.API.Minotaur.PolicyCheck().retrievePolicyValue("account_modify").equals("on") || new Cataphract.API.Dragon.Login(_currentUsername).checkPrivilegeLogic())
        {
            // Check login credentials
            if(!login())
            IOStreams.printError("Incorrect Credentials! Aborting...");
            else
            // Proceed to account management menu
            accountManagementMenu();

            // Trigger garbage collection
            System.gc();
        }
        else
        IOStreams.printError("Policy Configuration Error!");

        // Trigger garbage collection
        System.gc();

    }

    /**
    * Method to handle user authentication
    *
    * @return {@code true} if authentication is successful, {@code false} otherwise
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private boolean login()throws Exception
    {
        // Display build information
        Build.viewBuildInfo();
        // Prompt user for authentication
        IOStreams.printAttention("Please authenticate to continue.");
        IOStreams.println("Username: " + _currentAccountName);
        // Read password and security key
        String password = Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String key = Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

        // Authenticate user
        return new Login(_currentUsername).authenticationLogic(password, key);
    }

    /**
    * Method to display and manage account modification menu
    *
    * @throws Exception Throws any exceptions encountered during runtime.
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
                case "":
                case " ":
                case "exit":
                break;

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
                IOStreams.printError("Invalid Syntax. Use: \npromote <target_username>\n   OR\ndemote <target_username>");
                else
                accountPromoteDemoteLogic(usermgmtModifyCommandArray[0], usermgmtModifyCommandArray[1]);
                break;

                case "view":
                if(usermgmtModifyCommandArray.length < 2)
                    IOStreams.printError("Invalid Syntax. Use: \nview <target_username>");
                else
                    viewUserInformation(Cryptography.stringToSHA3_256(usermgmtModifyCommandArray[1]));
                break;

                case "list":
                break;

                case "clear":
                accountManagementMenuDisplay();
                break;

                default:
                IOStreams.printError("Invalid account modification option. Please specify a valid account modification option.");
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
            IOStreams.println(1, 8, "[!] Promote Account to Administrator");
            IOStreams.println(1, 8, "[!] Demote Account to Standard User");
            IOStreams.println(1, 8, "[!] View Account Details of a User");
            IOStreams.println(1, 8, "[!] List All User Accounts\n");
            IOStreams.println(1, 8, "[ PROMOTE | DEMOTE | VIEW | LIST ]");
        }
        IOStreams.println("");
    }

    //action is either promote or demote, same as commandArray[0]
    /**
     * Logic to handle account promotion or demotion by users with Administrator privileges
     *
     * @param action The intended action to be undertaken; promotion or demotion of a target user
     * @param targetUser The intended user to be either promoted to administrator or demoted to standard user
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private void accountPromoteDemoteLogic(String action, String targetUser)throws Exception
    {
        //Reject any attempts to promote or demote if specified user is an Administrator
        if(targetUser.equalsIgnoreCase("Administrator"))
        IOStreams.printError("Cannot promote or demote the user Administrator.");
        else
        {
            if(_currentAccountAdmin)
            {
                //encode the selected user to a hashed format
                targetUser = Cryptography.stringToSHA3_256(targetUser);

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
                        IOStreams.printInfo(action.toUpperCase() + "D " + new Login(targetUser).getNameLogic() + " successfully!");
                    }
                }
                else
                {
                    IOStreams.printError("Specified User does not exist!");
                }
            }
            else
            IOStreams.printError("Invalid Privileges! Cannot Modify User Privileges.");
        }
        // Trigger garbage collection
        System.gc();
    }

    /**
     * Logic to view account information by users with Administrator privileges
     * 
     * @param targetUser The user account intended to be viewed
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private void viewUserInformation(String targetUser) throws Exception
    {
        // Check if the current user is an Administrator
        if(_currentAccountAdmin)
        {
            // Check if the specified user exists
            if(new Login(targetUser).checkUserExistence())
            {
                // Display the target user information
                IOStreams.println("\n--- User Information ---");
                IOStreams.println("Account Name        : " + new Login(targetUser).getNameLogic());
                IOStreams.println("Account Privileges  : " + new Login(targetUser).checkPrivilegeLogic());
                IOStreams.println("User Home Directory : " + (new File("./Users/Cataphract/" + targetUser).exists()?"./Users/Cataphract/" + targetUser:"Home Directory Does Not Exist!") + "\n");
            }
            // Notify that the specified user does not exist
            else
            {
                IOStreams.printError("User Does Not Exist! Please Enter A Valid Username.");
            }
        }
        // Notify that the user privileges are insufficient view user details
        else
        {
            IOStreams.printError("Invalid Privileges! Cannot Modify User Privileges.");
        }
    }

    private void listAllUserAccounts()
    {
        //reuse code from FileManagement.java? It'd be easier to do so.
    }

    /**
    * Method to change account name
    */
    private void changeAccountName()
    {
        String _newAccountName = console.readLine(_accountNamePolicy + "Account Name> ");

        // Check if the entered account name is invalid
        if(_newAccountName == null || _newAccountName.contains(" ") || _newAccountName.equals("") || !(_newAccountName.matches("^[a-zA-Z0-9]*$")) || _newAccountName.equalsIgnoreCase("Administrator") || _newAccountName.length() < 2)
        console.readLine("Invalid Account Name. Press ENTER to try again.");

        // Else, write the changes to the database
        else
        commitChangesToDatabase("Name", _newAccountName, targetUser);

        // Trigger garbage collection
        System.gc();
    }

    /**
    * Method to change account password
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private void changeAccountPassword()throws Exception
    {
        String _newPassword = String.valueOf(console.readPassword(_accountPasswordPolicy + "Account Password> "));
        String confirmPassword = String.valueOf(console.readPassword("Confirm Password> "));

        // Check if the new password entered matches the confirmation password.
        if(_newPassword == null || _newPassword.equals("") || _newPassword.length() < 8 || !(_newPassword.equals(confirmPassword)))
        console.readLine("Invalid Account Password. Press ENTER to try again.");

        // Else, write the changes to the database
        else
        commitChangesToDatabase("Password", Cryptography.stringToSHA3_256(_newPassword), targetUser);

        // Trigger garbage collection
        System.gc();
    }

    /**
    * Method to change account security key
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private void changeAccountSecurityKey()throws Exception
    {
        String _newSecKey = String.valueOf(console.readPassword(_accountKeyPolicy + "Account Security Key> "));
        String confirmKey = String.valueOf(console.readPassword("Confirm Security Key> "));

        // Check if the new Security Key entered matches the confirmation Security Key.
        if(_newSecKey == null || !(_newSecKey.equals(confirmKey)))
        console.readLine("Invalid Account Security Key. Press ENTER to try again.");

        // Else, write the changes to the database
        else
        commitChangesToDatabase("SecurityKey", Cryptography.stringToSHA3_256(_newSecKey), targetUser);

        // Trigger garbage collection
        System.gc();
    }

    /**
    * Method to change account PIN
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private void changeAccountPIN()throws Exception
    {
        String _newPIN = String.valueOf(console.readPassword(_accountPINPolicy + "Account Key> "));
        String confirmPIN = String.valueOf(console.readPassword("Confirm PIN> "));

        // Check if the new PIN entered matches the confirmation PIN.
        if(_newPIN == null || !(_newPIN.equals(confirmPIN)))
        console.readLine("Invalid Account PIN. Press ENTER to try again.");

        // Else, write the changes to the database
        else
        commitChangesToDatabase("PIN", Cryptography.stringToSHA3_256(_newPIN), targetUser);

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
            // Store the path of the database
            String databasePath = "jdbc:sqlite:./System/Cataphract/Private/Mud.dbx";

            // Store the SQL command that needs to be executed by the SQLite database engine
            String sqlCommand = "UPDATE MUD SET " + parameter + " = ? WHERE Username = ?";

            // Initialize the JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create a connection to the database
            Connection dbConnection = DriverManager.getConnection(databasePath);

            // Using PreparedStatement, create the statement that needs to be executed by the database engine
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);

            // Substitute appropriate values
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, targetUser);

            // Execute the statement
            preparedStatement.executeUpdate();

            // Close the PreparedStatement resource
            preparedStatement.close();

            // Close the connection to the database
            dbConnection.close();

            // Notify the user that the account modification was successful
            IOStreams.printInfo("Account Modification Successful!");
        }
        // If there are any exceptions, notify that the account modification failed
        catch(Exception e)
        {
            IOStreams.printInfo("Account Modification Failed.");
        }

        // Trigger garbage collection
        System.gc();
    }
}
