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

//Import the required Java IO classes
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

/**
* A class that helps to check the policy for a given module.
*
* @author DAK404 (https://github.com/DAK404)
* @version 2.0.6 (11-October-2023, Cataphract)
* @since 0.0.1 (Mosaic 1.0)
*/
public class PolicyCheck
{

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public PolicyCheck()
    {
    }

    /**
    * Retrieves the policy value in a string format, to the program requesting the value
    *
    * @param policyParameter The policy that must be checked against the policy file
    * @return String The value of the value retrieved from the file
    * @throws Exception Throws any exceptions encountered during runtime.
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
        policyValue = "error";

        System.gc();

        //return the policy value in the string format
        return policyValue;
    }
}
