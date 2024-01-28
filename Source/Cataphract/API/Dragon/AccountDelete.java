package Cataphract.API.Dragon;

//Import the required Java IO classes
import java.io.Console;
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

//Import Cataphract APIs
import Cataphract.API.Build;
import Cataphract.API.IOStreams;

/**
 * 
 */
public class AccountDelete
{
    private String _currentUsername = "";

    private Console console = System.console();

    public AccountDelete(String currentUsername)throws Exception
    {
        _currentUsername = currentUsername;   
    }

    public void userDeletionLogic()throws Exception
    {
        Build.viewBuildInfo();
        if(! login())
            IOStreams.printError("Invalid Login Credentials. Please Try Again.");
        else
        {
            if(_currentUsername.equals(new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256("Administrator")))
                administratorSession();
            else
                accountDeletionLogic(_currentUsername);
        }
    }

    private void administratorSession()throws Exception
    {
        IOStreams.printWarning("ADMINISTRATOR MODE! PROCEED WITH CAUTION!");
        String[] command;
        
        do
        {
            command = Cataphract.API.Anvil.splitStringToArray(console.readLine("AccMgmt-Del!> "));
            switch(command[0].toLowerCase())
            {
                case "del":
                case "delete":
                    if(command.length<2)
                        IOStreams.printError("Incorrect Syntax.");
                    else
                        IOStreams.printInfo("Account Deletion: " + (accountDeletionLogic(command[1])?"Successful":"Failed"));
                break;

                default:
                IOStreams.printError("Command Not Found: " + command[1]);
                break;
            }
        }
        while(! command[0].equalsIgnoreCase("exit"));
    }

    private boolean accountDeletionLogic(String username)throws Exception
    {
        boolean status = false;

        if(username.equals(new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256("Administrator")))
        {
            IOStreams.printError("Deletion of Administrator Account is not allowed!");
        }
        else
        {
            IOStreams.println("-------------------------------------------------");
            IOStreams.println("|   User Management Console: Account Deletion   |");
            IOStreams.println("-------------------------------------------------\n");
            
            try
            {
                if(console.readLine("Are you sure you wish to delete your user account? [ YES | NO ]\n> ").equalsIgnoreCase("yes"))
                {
                    status = deleteFromDatabase() & deleteDirectories(new File("./Users/Cataphract/" + _currentUsername));
                    IOStreams.printAttention("Account Successfully Deleted. Press ENTER to continue.");
                    console.readLine();
                    System.exit(211);
                }
            }
            catch(Exception e)
            {
                IOStreams.printError("System Error: Unable to delete account.");
            }
        }

        return status;
    }

    private boolean login()throws Exception
    {
        boolean status = false;
        try
        {
            IOStreams.println("Please login to continue.");
            IOStreams.println("Username: " + new Cataphract.API.Dragon.Login(_currentUsername).getNameLogic());
            status = new Cataphract.API.Dragon.Login(_currentUsername).authenticationLogic(new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: "))));
        }
        catch(Exception e)
        {
            status = false;
            e.printStackTrace();
            System.in.read();
        }
        return status;
    }

    private boolean deleteFromDatabase()
    {
        boolean status = false;
        try
        {
            String databasePath = "jdbc:sqlite:./System/Cataphract/Private/Mud.dbx";
            String sqlCommand = "DELETE FROM MUD WHERE Username = ?";

            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection(databasePath);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);
            preparedStatement.setString(1, _currentUsername);
            preparedStatement.executeUpdate();

            preparedStatement.closeOnCompletion();
            dbConnection.close();
            status = true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();

        return status;
    }

    private boolean deleteDirectories(File delFile)throws Exception
    {
        boolean status = false;
        try
        {
            if (delFile.listFiles() != null)
            {
                for (File fn : delFile.listFiles())
                deleteDirectories(fn);
            }
            status = delFile.delete();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }
}