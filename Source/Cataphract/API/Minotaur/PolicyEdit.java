package Cataphract.API.Minotaur;


//Import the required Java IO classes
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

//Import the Cataphract classes
import Cataphract.API.Build;
import Cataphract.API.IOStreams;

/**
*
*/
public class PolicyEdit
{
    boolean _userIsAdmin = false;

    public final String [] resetValues = {"auth", "update", "download", "script", "filemgmt", "read", "edit", "usermgmt", "policy"};

    private final String policyFileName = "./System/Cataphract/Private/Policy.burn";

    private String suggestedInputs = "";

    private Console console = System.console();
    private Properties props = null;

    public PolicyEdit()
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
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void policyEditorLogic()throws Exception
    {
        if(!authenticationLogic())
            IOStreams.printError("Authentication Failure. Exiting...");
        else
            if(new Cataphract.API.Minotaur.PolicyEnforce().checkPolicy("policy") || _userIsAdmin)
                policyEditor();
    }

    /**
    *
    * @return
    */
    private final boolean authenticationLogic()
    {
        boolean challengeStatus = false;
        try
        {
            Cataphract.API.Build.viewBuildInfo();
            IOStreams.printAttention("This module requires the user to authenticate to continue. Please enter the user credentials.");

            String username = new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(console.readLine("Username: "));

            challengeStatus = (new Cataphract.API.Dragon.LoginAuth(username).authenticationLogic(new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), new Cataphract.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")))));

            _userIsAdmin = new Cataphract.API.Dragon.LoginAuth(username).checkPrivilegeLogic();
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
        }
        return challengeStatus & _userIsAdmin;
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void policyEditor()throws Exception
    {
        suggestedInputs = "[ MODIFY " + (_userIsAdmin?"| RESET ":"") + "| UPDATE | HELP | EXIT ]";
        props = new Properties();
        if(! new File(policyFileName).exists())
            resetPolicyFile();
        viewPolicyInfo();
        
        String input;
        
        do
        {
            input = console.readLine("PolicyEditor)> ");

            String[] policyCommandArray = Cataphract.API.Anvil.splitStringToArray(input);

            switch(policyCommandArray[0].toLowerCase())
            {
                case "modify":
                if(policyCommandArray.length < 2)
                    System.out.println("Goober");
                else
                    savePolicy(policyCommandArray[1], policyCommandArray[2]);
                break;

                case "reset":
                if(_userIsAdmin)
                {
                    IOStreams.printAttention("Resetting Policy File...");
                    resetPolicyFile();
                }
                break;

                case "update":
                    viewPolicyInfo();
                break;

                case "exit":
                case "":
                break;

                default:
                IOStreams.printError("Invalid command. Please try again.");
                break;
            }
            System.gc();
        }
        while(! input.equalsIgnoreCase("exit"));
    }

    private void viewPolicyInfo()throws Exception
    {
        displaySettings();
        IOStreams.println(suggestedInputs + "\n");
        IOStreams.println("Add or Modify Policy Syntax:\nmodify <policy_name> <policy_value>");
        IOStreams.println("");
    }

    /**
    *
    */
    private final void displaySettings()throws Exception
    {
        BuildInfo.viewBuildInfo();
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
    * @param policyName
    * @param policyValue
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void savePolicy(String policyName, String policyValue)throws Exception
    {
        props.setProperty(policyName, policyValue);
        FileOutputStream output = new FileOutputStream(policyFileName);
        props.storeToXML(output, "CataphractSettings");
        output.close();
        System.gc();
    }

    /**
    * Resets all the policies to its default values.
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void resetPolicyFile()throws Exception
    {
        new File(policyFileName).delete();

        props = new Properties();

        for(int i = 0; i < resetValues.length; ++i)
            savePolicy(resetValues[i], "on");

        savePolicy("sysname", "SYSTEM" + ((int)(Math.random() * (999999 - 100000 + 1)) + 100000));
        savePolicy("module", "off");
    }
}