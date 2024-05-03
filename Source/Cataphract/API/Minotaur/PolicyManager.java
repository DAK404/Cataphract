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

package Cataphract.API.Minotaur;

import java.io.Console;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.util.Properties;

import Cataphract.API.Build;
import Cataphract.API.IOStreams;

/**
* A class implementing policy management system.
*
* @author DAK404 (https://github.com/DAK404)
* @version 2.0.1 (11-October-2023, Cataphract)
* @since 0.2.4.6 (Mosaic)
*/
public class PolicyManager
{
    /** Stores the value if the user is an administrator or not.*/
    private boolean _userIsAdmin = false;

    /** Stores the default Cataphract values in an array.*/
    public final String [] resetValues = {"auth", "update", "download", "script", "filemgmt", "read", "edit", "policy", "account_create", "account_delete", "account_modify"};

    /** Stores the path of the policy file.*/
    private final String policyFileName = "./System/Cataphract/Private/Policy.burn";

    /** Provide a set of applicable commands to the users.*/
    private String suggestedInputs = "";

    /** Instantiate Console to get user inputs*/
    private Console console = System.console();

    /** Instantiate Properties to load and write to the policy file.*/
    private Properties props = null;

    /** Sole constructor. Checks if the policy file exists. Will reset if the file does not exist.*/
    public PolicyManager()
    {
        try
        {
            if(!new File(policyFileName).exists())
            resetPolicyFile();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
    * Logic to edit the policy file with the policy name and its corresponding values.
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public final void policyEditorLogic()throws Exception
    {
        //If authentication logic fails, exit the module.
        if(!authenticationLogic())
        IOStreams.printError("Authentication Failure. Exiting...");
        else
        //Check if the policy management is enabled for users. If disabled, check if the user is admin and override the policy.
        if((new Cataphract.API.Minotaur.PolicyCheck().retrievePolicyValue("policy").equalsIgnoreCase("on")) || _userIsAdmin)
        //Call the policy editor if conditions are met.
        policyEditor();
        else
        //Notify the user about the privileges and policy.
        IOStreams.printError("Policy Management Disabled: Insufficient Privileges");

        System.gc();
    }

    /**
    * Logic to authenticate a user to access the policy management system
    *
    * @return boolean Returns the Authentication status.
    */
    private final boolean authenticationLogic()
    {
        //Boolean to store the result of the authentication check.
        boolean challengeStatus = false;

        //Added a try-catch block for better error handling mechanism
        try
        {
            //Clear the screen and display build information
            Build.viewBuildInfo();
            IOStreams.printAttention("This module requires the user to authenticate to continue. Please enter the user credentials.");

            //Store the username, will be required to check if the user has administrator privileges.
            String username = Cryptography.stringToSHA3_256(console.readLine("Username: "));

            //Use the Login API to check if the entered credentials are valid or not
            challengeStatus = (new Cataphract.API.Dragon.Login(username).authenticationLogic(Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")))));

            //Update the value to check if the user has administrator privileges.
            _userIsAdmin = new Cataphract.API.Dragon.Login(username).checkPrivilegeLogic();
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            //Set the challenge status to be false
            challengeStatus = false;
        }
        return challengeStatus;
    }

    /**
    * Logic containing the implementation to edit policies.
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final void policyEditor()throws Exception
    {
        //Display the suggested inputs to the user.
        suggestedInputs = "[ MODIFY " + (_userIsAdmin?"| RESET ":"") + "| REFRESH | HELP | EXIT ]";

        //Load the properties from the policy file.
        props = new Properties();

        //If the policy file is not found, reset the policies and force recreate the policy file.
        if(! new File(policyFileName).exists())
        resetPolicyFile();

        //Display the policies.
        viewPolicyInfo();

        //A string to hold the inputs provided by the user.
        String input;

        //Loop the logic until the user wants to exit the module.
        do
        {
            //Store the user input command in variable "input"
            input = console.readLine("PolicyEditor)> ");

            //Split the contents of "input" at the occurrence of a blank space and store it in an array.
            String[] policyCommandArray = Cataphract.API.Anvil.splitStringToArray(input);

            //Logic to decide which command needs to be executed, and converting the input to lowercase to avoid any discrepancies.
            switch(policyCommandArray[0].toLowerCase())
            {
                //Logic to modify the policy file.
                case "modify":
                //If the command syntax is invalid, notify the user and display the correct syntax.
                if(policyCommandArray.length < 2)
                IOStreams.printError("Invalid Syntax: Missing Policy Name or Value");
                //Else, save the policy and the value to the file.
                else
                savePolicy(policyCommandArray[1], policyCommandArray[2]);
                break;

                //Logic to reset the policy file.
                case "reset":
                //Reset can be performed by Administrators only. Restrict normal users from using this command.
                if(_userIsAdmin)
                {
                    IOStreams.printAttention("Resetting Policy File...");
                    resetPolicyFile();
                }
                break;

                //Logic to reload and display the policy file
                case "refresh":
                viewPolicyInfo();
                break;

                //logic to handle the exiting of program. NOTE: the while loop shall take care of the exit, therefore, a break is suitable here.
                case "exit":
                case "":
                break;

                //Default string when a command is not found.
                default:
                IOStreams.printError("Invalid command. Please try again.");
                break;
            }
            System.gc();
        }
        while(! input.equalsIgnoreCase("exit"));
    }

    /**
    * Display the policies and the required data.
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final void viewPolicyInfo()throws Exception
    {
        displaySettings();
        IOStreams.println(suggestedInputs + "\n");
    }

    /**
     * Display the details of the policy file and the policies itself.
     * 
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private final void displaySettings()throws Exception
    {
        Build.viewBuildInfo();
        IOStreams.println("         Minotaur Policy Editor 2.0         ");
        IOStreams.println("--------------------------------------------");
        IOStreams.println("      - Current Policy Configuration -      ");
        IOStreams.println("--------------------------------------------");
        IOStreams.println("\nPolicy File  : " + policyFileName);
        IOStreams.println("Policy Format: XML\n");
        FileInputStream configStream = new FileInputStream(policyFileName);
        props.loadFromXML(configStream);
        configStream.close();
        props.list(System.out);
        IOStreams.println("\n--------------------------------------------\n");
        System.gc();
    }

    /**
    * Saves a policy to the file, with the key and value structure
    *
    * The policies are stored in an XML structured file
    * @param policyName Name of the policy that needs to be stored.
    * @param policyValue Value of the policy that needs to be stored.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final void savePolicy(String policyName, String policyValue)throws Exception
    {
        //Set the policy name and the policy value specified in the arguments.
        props.setProperty(policyName, policyValue);

        //Write the data out to the file.
        FileOutputStream output = new FileOutputStream(policyFileName);

        //Store the policy data under CataphractSettings.
        props.storeToXML(output, "CataphractSettings");

        //Close the output stream
        output.close();
        System.gc();
    }

    /**
    * Resets all the policies to its default values.
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final void resetPolicyFile()throws Exception
    {
        //force delete the policy file
        new File(policyFileName).delete();

        //Instantiate the Properties class.
        props = new Properties();

        //Set the policy name and values.
        for(int i = 0; i < resetValues.length; ++i)
        savePolicy(resetValues[i], "on");

        //Generate a random system name and store it in the policy file
        savePolicy("sysname", "SYSTEM" + ((int)(Math.random() * (999999 - 100000 + 1)) + 100000));

        // EXCEPTIONS! These policies need to be set to off for the sake of program security. //

        savePolicy("module", "off");
        savePolicy("policy", "off");
    }
}
