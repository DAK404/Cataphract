package Cataphract.API.Dragon;

import java.io.Console;
import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import Cataphract.API.Build;
import Cataphract.API.IOStreams;

public final class AccountCreate 
{
    /*
    ----------------------------------------------------------------------------------
    SECTION NAME : VARIABLE DECLARATION
    OBJECTIVE    : Set all the dependent variable values that shall help in creating a
    user account. The variables here store the current user data and the new user data
    which will be written to the database. Each variable is a string datatype and is
    assigned to a null value. This ensures that if the account creation data has not
    conformed to the policies, the user account creation cannot proceed.
    AUTHOR       : Deepak Anil Kumar (@DAK404)
    ----------------------------------------------------------------------------------
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

    protected final static String _accountUsernamePolicy = """
    Account Username Policy Information
    -----------------------------------
    * Username cannot contain the word \'Administrator\'
    * Username can contain numbers, special characters and symbols.
    -----------------------------------
    """;

    protected final static String _accountPasswordPolicy = """
    Account Password Policy Information
    -----------------------------------
    * Password must contain atleast 8 characters
    * Password is recommended to have special characters and numbers
    -----------------------------------
    """;
    
    protected final static String _accountSecurityKeyPolicy = """
    Account Security Key Policy Information
    -----------------------------------
    * Security Key must contain atleast 8 characters
    * Security Key is recommended to have special characters and numbers
    -----------------------------------
    """;

    protected final static String _accountPINPolicy = """
    Account PIN Policy Information
    -------------------------------
    * PIN must contain atleast 4 characters
    * PIN is recommended to have special characters and numbers
    -------------------------------
    """;


    private boolean _status = false;

    private String _currentUsername = "DEFAULT";
    private boolean _currentAccountAdmin = false;

    private String _newAccountName = "";
    private String _newAccountUsername = "";
    private String _newAccountPassword = "";
    private String _newAccountSecurityKey = "";
    private String _newAccountPIN = "";
    private boolean _newAccountAdmin = false;

    private Console console = System.console();

    /*
    ----------------------------------------------------------------------------------
    END OF VARIABLE DECLARATION
    ----------------------------------------------------------------------------------
    */

    public final void accountCreateLogic(String username)throws Exception
    {
        _currentUsername = username;

        if(! authenticateCurrentUser())
            IOStreams.println("Failed to authenticate user. Exiting...");
        else
        {
            Build.viewBuildInfo();
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
            _currentAccountAdmin = new Cataphract.API.Dragon.Login(_currentUsername).checkPrivilegeLogic();
        }
        catch(Exception e)
        {
            new Cataphract.API.ExceptionHandler().handleException(e);
        }

        return authenticationStatus;
    }

    /*
    ----------------------------------------------------------------------------------
    SECTION NAME : ACCOUNT CREDENTIAL VALIDATION LOGIC
    OBJECTIVE    : A set of methods which will check if the credentials entered are
    conforming to the rules of the system. Should any of the rules are not followed,
    the program shall ask to re-enter the credential that is violating the guideline.
    This also means that if the password rules are not followed, the user will need
    to re-enter the password that conforms to the guidelines.
    AUTHOR       : Deepak Anil Kumar (@DAK404)
    ----------------------------------------------------------------------------------
    */

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
        credentialDashboard();

        _newAccountName = console.readLine(_accountNamePolicy + "Account Name> ");

        if(_newAccountName == null | _newAccountName.contains(" ") | _newAccountName.equals("") | !(_newAccountName.matches("^[a-zA-Z0-9]*$")) | _newAccountName.equalsIgnoreCase("Administrator") | _newAccountName.length() < 2)
        {
            _newAccountName = "";
            _status = false;
            console.readLine("Invalid Account Name. Press ENTER to try again.");
        }
        else
            _status = true;

        return _status;
    }

    private final boolean setAccountUsername()throws Exception
    {
        credentialDashboard();

        _newAccountUsername = (console.readLine(_accountUsernamePolicy + "Account Username> "));

        if(new Cataphract.API.Dragon.Login(_newAccountUsername).checkUserExistence())
        {
            IOStreams.printError("Username has already been enrolled! Please try again with another username.");
            _status = false;
            _newAccountUsername = "";
            console.readLine();
        }
        else if(_newAccountUsername == null | _newAccountUsername.equals("") | _newAccountUsername.equalsIgnoreCase("Administrator"))
        {
            _newAccountUsername = "";
            _status = false;
            console.readLine("Invalid Account Username. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _newAccountUsername = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newAccountUsername);
        }
        
        return _status;
    }

    private final boolean setAccountPassword()throws Exception
    {
        credentialDashboard();
        
        _newAccountPassword = String.valueOf(console.readPassword(_accountPasswordPolicy + "Account Password> "));
        String confirmPassword = String.valueOf(console.readPassword("Confirm Password> "));

        if(_newAccountPassword == null | _newAccountPassword.equals("") | _newAccountPassword.length() < 8 | !(_newAccountPassword.equals(confirmPassword)))
        {
            _newAccountPassword = "";
            confirmPassword = "";
            _status = false;
            console.readLine("Invalid Account Password. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _newAccountPassword = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newAccountPassword);
        }

        return _status;
    }

    private final boolean setAccountSecurityKey()throws Exception
    {
        credentialDashboard();

        _newAccountSecurityKey = String.valueOf(console.readPassword(_accountSecurityKeyPolicy + "Account Security Key> "));
        String confirmKey = String.valueOf(console.readPassword("Confirm Security Key> "));

        if(_newAccountSecurityKey == null | !(_newAccountSecurityKey.equals(confirmKey)))
        {
            _newAccountSecurityKey = "";
            confirmKey = "";
            _status = false;
            console.readLine("Invalid Account Security Key. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _newAccountSecurityKey = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newAccountSecurityKey);
        }

        return _status;
    }

    private final boolean setAccountPIN()throws Exception
    {
        credentialDashboard();

        _newAccountPIN = String.valueOf(console.readPassword(_accountPINPolicy + "Account PIN> "));
        String confirmPIN = String.valueOf(console.readPassword("Confirm PIN> "));

        if(_newAccountPIN == null | _newAccountPIN.equals("") | _newAccountPIN.length() < 4 | !(_newAccountPIN.equals(confirmPIN)))
        {
            _newAccountPIN = "";
            confirmPIN = "";
            _status = false;
            console.readLine("Invalid Account PIN. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _newAccountPIN = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(_newAccountPIN);
        }

        return _status;
    }

    /*
    ----------------------------------------------------------------------------------
    END OF ACCOUNT CREDENTIAL VALIDATION LOGIC
    ----------------------------------------------------------------------------------
    */

    /*
    ----------------------------------------------------------------------------------
    SECTION NAME : ADD ACCOUNT TO DATABASE LOGIC
    OBJECTIVE    : This logic shall help in setting the account credentials to the MUD
    database file. This shall commit the fields of Name, Username, Password, 
    SecurityKey, PIN and the Administrator status to the database file, which can later
    be used for authentication and credential validation. The credentials, excluding
    Account Name, will be Hashed in SHA3-256 format to help in keeping the credentials
    secure in the event of a data breach. By following the specified policies, the
    credentials will be harder to break from the hashed format. Although the system is
    may have areas to strengthen the robustness and the security, this will be looked 
    into in a future point of time.
    AUTHOR       : Deepak Anil Kumar (@DAK404)
    ----------------------------------------------------------------------------------
    */
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

    /*
    ----------------------------------------------------------------------------------
    END OF ADD ACCOUNT TO DATABASE LOGIC
    ----------------------------------------------------------------------------------
    */

    /*
    ----------------------------------------------------------------------------------
    SECTION NAME : SETUP DEFAULT ADMINISTRATOR ACCOUNT LOGIC
    OBJECTIVE    : A logic dedicated to make sure that a default administrator account
    can be created during setup. This is the equivalent of a "root" account in Linux.
    Other user accounts can be created by the administrator as a standard user or an
    administrator user account.
    AUTHOR       : Deepak Anil Kumar (@DAK404)
    ----------------------------------------------------------------------------------
    */

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
        
        Cataphract.API.IOStreams.confirmReturnToContinue();
    }

    /*
    ----------------------------------------------------------------------------------
    END OF SETUP DEFAULT ADMINISTRATOR ACCOUNT LOGIC
    ----------------------------------------------------------------------------------
    */
}