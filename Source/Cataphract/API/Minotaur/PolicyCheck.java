/*
*                                                      |
*                                                     ||
*  |||||| ||||||||| |||||||| ||||||||| |||||||  |||  ||| ||||||| |||||||||  |||||| |||||||||
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

/*
* This file is part of the Cataphract project.
* Copyright (C) 2024 DAK404 (https://github.com/DAK404)
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

package Cataphract.API.Minotaur;

//Import the required Java IO classes
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

import Cataphract.API.IOStreams;

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
            String propsFileName = IOStreams.convertFileSeparator(".|System|Cataphract|Private|Policy.burn");

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
