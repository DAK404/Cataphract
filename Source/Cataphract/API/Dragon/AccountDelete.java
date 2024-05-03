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
* A class to delete user accounts on the system. Can be restricted by policy "account_delete".
*
* @author DAK404 (https://github.com/DAK404)
* @version 3.9.7 (20-February-2024, Cataphract)
* @since 0.0.1 (Mosaic 0.0.1)
*/
public class AccountDelete
{
    /** Variable to store the current username */
    private String _currentUsername = "";

    /** Variable to store if the current user has administrator privileges */
    private boolean _currentUserAdministrator = false;

    /** Console object for user input */
    private Console console = System.console();

    /**
    * Constructor for AccountDelete class.
    *
    * @param currentUsername The current username.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public AccountDelete(String currentUsername) throws Exception
    {
        // Set the current username
        _currentUsername = currentUsername;
        //_currentUserAdministrator = new Cataphract.API.Dragon.Login(currentUsername).checkPrivilegeLogic();
        _currentUserAdministrator = new Login(currentUsername).checkUserExistence();
    }

    /**
    * Performs user deletion logic.
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public void deleteUserAccount() throws Exception
    {
        // Display build information
        Build.viewBuildInfo();

        // Check the policy to see if the account deletion is allowed for the current user
        if(new Cataphract.API.Minotaur.PolicyCheck().retrievePolicyValue("account_delete").equals("on") || new Cataphract.API.Dragon.Login(_currentUsername).checkPrivilegeLogic())
        {
            // Check login credentials
            if (!login())
            IOStreams.printError("Invalid Login Credentials. Please Try Again.");
            // If login successful, proceed to check privileges and perform actions accordingly
            else
            userManagementConsoleDelete();
        }
        else
        IOStreams.printError("Policy Configuration Error!");
    }

    /**
    * Provides a console to the user to delete the user accounts for both administrators and standard users.
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private void userManagementConsoleDelete()throws Exception
    {
        IOStreams.println("-------------------------------------------------");
        IOStreams.println("|   User Management Console: Account Deletion   |");
        IOStreams.println("-------------------------------------------------\n");

        if(_currentUserAdministrator)
        {
            // Warning message for administrator mode
            IOStreams.printWarning("ADMINISTRATOR MODE ACTIVE!");

            // Array to store command and arguments
            String[] command;

            // Loop for administrator session
            do
            {
                // Read user input for command
                command = Cataphract.API.Anvil.splitStringToArray(console.readLine("AccMgmt-Del!> "));

                // Switch statement for different commands
                switch (command[0].toLowerCase())
                {
                    case "del":
                    case "delete":
                    // Check syntax for delete command
                    if (command.length < 2)
                    IOStreams.printError("Incorrect Syntax.");
                    else
                    {
                        // force deletion of user account by specifying the hashed username
                        if(command[1].equalsIgnoreCase("force") && command.length > 2)
                            accountDeletionLogic(command[3]);
                        else
                            // Perform account deletion and display result
                            IOStreams.printInfo("Account Deletion: " + (accountDeletionLogic(Cryptography.stringToSHA3_256(command[1])) ? "Successful" : "Failed"));
                    }
                    break;

                    default:
                    // Display error for unknown command
                    IOStreams.printError("Command Not Found: " + command[0]);
                    break;
                }
            } while (!command[0].equalsIgnoreCase("exit"));
        }
        else
        accountDeletionLogic(_currentUsername);
    }


    /**
    * Logic for account deletion.
    *
    * @param username The username to be deleted.
    * @return {@code true} if the deletion is successful, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private boolean accountDeletionLogic(String username) throws Exception 
    {
        boolean status = false;

        // Check if the username is "Administrator"
        if (username.equals(Cryptography.stringToSHA3_256("Administrator")))
        IOStreams.printError("Deletion of Administrator Account is not allowed!");
        else if (! new Login(Cryptography.stringToSHA3_256(username)).checkUserExistence())
        IOStreams.printError("User does not exist! Please enter a valid username.");
        else
        {
            try
            {
                // Prompt user for confirmation
                if (console.readLine("Are you sure you wish to delete user account \"" + new Login(username).getNameLogic() + "\"? [ YES | NO ]\n> ").equalsIgnoreCase("yes"))
                {
                    // Delete account from database and directories
                    status = deleteFromDatabase() & deleteDirectories(new File("./Users/Cataphract/" + username));
                    // Print success message and exit
                    IOStreams.printAttention("Account Successfully Deleted.");
                    if(! _currentUserAdministrator)
                    {
                        //wait for 5 seconds and then restart
                        Thread.sleep(5000);
                        System.exit(211);
                    }
                }
            }
            catch (Exception e)
            {
                // Print error message for any exceptions
                IOStreams.printError("System Error: Unable to delete account.");
            }
        }

        return status;
    }

    /**
    * Login method.
    *
    * @return {@code true} if login is successful, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private boolean login() throws Exception
    {
        boolean status = false;
        try
        {
            // Prompt user for login credentials
            IOStreams.println("Please login to continue.");
            IOStreams.println("Username: " + new Login(_currentUsername).getNameLogic());
            // Authenticate user
            status = new Login(_currentUsername).authenticationLogic(Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("Security Key: "))));
        }
        catch (Exception e)
        {
            // Print error message for any exceptions
            status = false;
            e.printStackTrace();
            System.in.read();
        }
        return status;
    }

    /**
    * Method to delete user account from the database.
    *
    * @return {@code true} if the deletion is successful, {@code false} otherwise.
    */
    private boolean deleteFromDatabase()
    {
        boolean status = false;
        try
        {
            // Define database path and SQL command
            String databasePath = "jdbc:sqlite:./System/Cataphract/Private/Mud.dbx";
            String sqlCommand = "DELETE FROM MUD WHERE Username = ?";

            // Load JDBC driver and establish connection
            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection(databasePath);
            // Prepare statement and execute deletion
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);
            preparedStatement.setString(1, _currentUsername);
            preparedStatement.executeUpdate();

            // Close resources
            preparedStatement.closeOnCompletion();
            dbConnection.close();
            status = true;
        }
        catch (Exception e)
        {
            // Print error message for any exceptions
            e.printStackTrace();
        }
        // Explicitly call garbage collector
        System.gc();

        return status;
    }

    /**
    * Method to delete directories recursively.
    *
    * @param delFile The file to be deleted.
    * @return {@code true} if the deletion is successful, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private boolean deleteDirectories(File delFile) throws Exception
    {
        boolean status = false;
        try
        {
            // Check if file is a directory
            if (delFile.listFiles() != null)
            // Iterate over files in directory and delete recursively
            for (File fn : delFile.listFiles())
            deleteDirectories(fn);
            // Delete the directory itself
            status = delFile.delete();
        }
        catch (Exception e)
        {
            // Print error message for any exceptions
            e.printStackTrace();
        }

        return status;
    }
}
