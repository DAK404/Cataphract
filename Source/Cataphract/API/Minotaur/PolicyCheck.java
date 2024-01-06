package Cataphract.API.Minotaur;

//Import the required Java IO classes
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

/**
 * An API that helps to check the policy for a given module.
 * 
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
