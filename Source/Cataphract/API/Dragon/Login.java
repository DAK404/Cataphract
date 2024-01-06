package Cataphract.API.Dragon;

import java.io.Console;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 
 */
public class Login
{
    private String _username;

    Console console = System.console();

    /**
     * 
     * @param username : Username of the current user
     * @throws Exception : Throws any exceptions encountered during runtime.
     */
    public Login(String username)throws Exception
    {
        _username = (username.equals("") | username == null)?"DEFAULT USER":username;
    }

    /**
     * 
     * @param psw
     * @param key
     * @return
     * @throws Exception
     */
    public final boolean authenticationLogic(String psw, String key)throws Exception
    {
        return retrieveDatabaseEntry("SELECT Password FROM MUD WHERE Username = ?", "Password").equals(psw) && retrieveDatabaseEntry("SELECT SecurityKey FROM MUD WHERE Username = ?", "SecurityKey").equals(key);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public final boolean checkPrivilegeLogic()throws Exception
    {
        return retrieveDatabaseEntry("SELECT Privileges FROM MUD WHERE Username = ?", "Privileges").equals("Yes");
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public final String getNameLogic()throws Exception
    {
        return retrieveDatabaseEntry("SELECT Name FROM MUD WHERE Username = ?", "Name");
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public final String getPINLogic()throws Exception
    {
        return retrieveDatabaseEntry("SELECT PIN FROM MUD WHERE Username = ?", "PIN");
    }

    public final boolean checkUserExistence()throws Exception
    {
        return checkForExistingAccount();
    }

    /**
     * 
     * @param sqlCommand
     * @param parameter
     * @return
     * @throws Exception
     */
    private final String retrieveDatabaseEntry(String sqlCommand, String parameter)throws Exception
    {
        String result = "DEFAULT_STRING";
        Class.forName("org.sqlite.JDBC");
        String databasePath = "jdbc:sqlite:./System/Cataphract/Private/Mud.dbx";

        Connection dbConnection = DriverManager.getConnection(databasePath);
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);
        ResultSet resultSet = null;
        
        try
        {
            preparedStatement.setString(1, _username);
            resultSet = preparedStatement.executeQuery();
            result = resultSet.getString(parameter);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = "ERROR";
        }
        finally
        {
            preparedStatement.close();
            dbConnection.close();
            resultSet.close();
        }
        if(result == null)
            result = "Error";

        System.gc();
        return result;
    }

    private boolean checkForExistingAccount()throws Exception
    {
        boolean userExists = false;

        Class.forName("org.sqlite.JDBC");
        String databasePath = "jdbc:sqlite:./System/Cataphract/Private/Mud.dbx";

        Connection dbConnection = DriverManager.getConnection(databasePath);

        String query = "SELECT (count(*) > 0) FROM MUD WHERE Username LIKE ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, _username);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        try 
        {
            if (resultSet.next()) 
                userExists = resultSet.getBoolean(1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            preparedStatement.close();
            dbConnection.close();
            resultSet.close();
        }
        System.gc();
        return userExists;
    }
}