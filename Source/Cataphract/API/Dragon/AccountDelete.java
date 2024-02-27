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
 * A class to delete user accounts on the system. Can be restricted by policy "account_delete".
 *
 * @author DAK404 (https://github.com/DAK404)
 * @version 3.9.7 (20-February-2024, Cataphract)
 * @since 0.0.1 (Mosaic 0.0.1)
 */
public class AccountDelete
{
    /**
     * Variable to store the current username
     */
    private String _currentUsername = "";

    /**
     * Console object for user input
     */
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
    }

    /**
     * Performs user deletion logic.
     *
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    public void userDeletionLogic() throws Exception
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
            {
                if (new Login(_currentUsername).checkPrivilegeLogic())
                    administratorSession();
                // If not administrator, proceed with account deletion
                else
                    accountDeletionLogic(_currentUsername);
            }
        }
        else
            IOStreams.printError("Policy Configuration Error!");
    }

    /**
     * Administrator session for account deletion. Administrators can delete other user accounts.
     *
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private void administratorSession() throws Exception
    {
        // Warning message for administrator mode
        IOStreams.printWarning("ADMINISTRATOR MODE! PROCEED WITH CAUTION!");

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
                        // Perform account deletion and display result
                        IOStreams.printInfo("Account Deletion: " + (accountDeletionLogic(command[1]) ? "Successful" : "Failed"));
                    break;

                default:
                    // Display error for unknown command
                    IOStreams.printError("Command Not Found: " + command[1]);
                    break;
            }
        } while (!command[0].equalsIgnoreCase("exit"));
    }

    /**
     * Logic for account deletion.
     *
     * @param username The username to be deleted.
     * @return true if the deletion is successful, otherwise false.
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private boolean accountDeletionLogic(String username) throws Exception {
        boolean status = false;

        // Check if the username is "Administrator"
        if (username.equals(new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256("Administrator"))) {
            IOStreams.printError("Deletion of Administrator Account is not allowed!");
        } else {
            // Print confirmation message for account deletion
            IOStreams.println("-------------------------------------------------");
            IOStreams.println("|   User Management Console: Account Deletion   |");
            IOStreams.println("-------------------------------------------------\n");

            try {
                // Prompt user for confirmation
                if (console.readLine("Are you sure you wish to delete your user account? [ YES | NO ]\n> ").equalsIgnoreCase("yes")) {
                    // Delete account from database and directories
                    status = deleteFromDatabase() & deleteDirectories(new File("./Users/Cataphract/" + _currentUsername));
                    // Print success message and exit
                    IOStreams.printAttention("Account Successfully Deleted. Press ENTER to continue.");
                    console.readLine();
                    System.exit(211);
                }
            } catch (Exception e) {
                // Print error message for any exceptions
                IOStreams.printError("System Error: Unable to delete account.");
            }
        }

        return status;
    }

    /**
     * Login method.
     *
     * @return true if login is successful, otherwise false.
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private boolean login() throws Exception {
        boolean status = false;
        try {
            // Prompt user for login credentials
            IOStreams.println("Please login to continue.");
            IOStreams.println("Username: " + new Cataphract.API.Dragon.Login(_currentUsername).getNameLogic());
            // Authenticate user
            status = new Cataphract.API.Dragon.Login(_currentUsername).authenticationLogic(new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: "))));
        } catch (Exception e) {
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
     * @return true if the deletion is successful, otherwise false.
     */
    private boolean deleteFromDatabase() {
        boolean status = false;
        try {
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
        } catch (Exception e) {
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
     * @return true if the deletion is successful, otherwise false.
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
