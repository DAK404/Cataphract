package Cataphract.API.Minotaur;

//Import the required Java IO classes
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

import Cataphract.API.IOStreams;

/**
* Program to Enforce the policies set by the administrators
*
* @version 0.4.2
* @since 0.2.7
* @author DAK404
*/
public class PolicyEnforce
{
    /**
    * 
    *
    * @param Policy : The policy that must be checked from the .BURN file
    * @return boolean : The value of the policy set in the file
    * @throws Exception : Handle exceptions thrown during program runtime
    */
    public final boolean checkPolicy(String Policy)throws Exception
    {
        //Initialize the policy value as false by default
        boolean policyValue = false;

        try
        {
            //Feeds the retrievePolicyValue() with the policy to be checked
            switch(retrievePolicyValue(Policy).toLowerCase())
            {
                //If the policy value returned is false, print an error statement about the policy value
                case "off":
                IOStreams.printError("This module has been restricted for Standard Users by the System Administrators.");
                break;

                //Set the stat value as true if the policy value returned is true
                case "on":
                policyValue = true;
                break;

                //When a policy is not found or cannot be loaded, display an error message about the misconfigured policy
                case "error":
                IOStreams.printAttention("This policy has either been corrupted or misconfigured. Please contact the System Administrators for more information.");
                break;

                //Handle any other inputs returned after checking the policy
                default:
                IOStreams.printError("Policy Configuration Error!");
                break;
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
        return policyValue;
    }

    public final boolean checkPolicyQuiet(String policyName)throws Exception
    {
        return retrievePolicyValue(policyName).equalsIgnoreCase("on");
    }

    /**
    * Retrieves the policy value in a string format, to the program requesting the value
    *
    * @param policyParameter : The policy that must be checked against the policy file
    * @return String : The value of the value retrieved from the file
    * @throws Exception : Handle exceptions thrown during program runtime
    */
    public final String retrievePolicyValue(String policyParameter)throws Exception
    {
        //Initialize the policy value as an empty string
        String policyValue = "";
        try
        {
            //Open the properties streams
            Properties prop = new Properties();
            String propsFileName="./System/Cataphract/Private/Policy.burn";

            //Load the file stream containing the program properties
            FileInputStream configStream = new FileInputStream(propsFileName);

            //Load the properties from an XML formatted file
            prop.loadFromXML(configStream);

            //Get the property value specified in the file
            policyValue = prop.getProperty(policyParameter);

            //Close the streams
            configStream.close();
        }
        catch(Exception E)
        {
            //Set the string value to "error" if the given property is not found, unreadable or is misconfigured
            policyValue = "error";
        }
        
        if(policyValue == null)
            policyValue = "Error";
            
        //return the policy value in the string format
        return policyValue;
    }
}