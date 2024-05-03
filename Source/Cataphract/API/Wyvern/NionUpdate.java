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

package Cataphract.API.Wyvern;

import Cataphract.API.IOStreams;
import Cataphract.API.Dragon.Login;
import Cataphract.API.Minotaur.PolicyCheck;
import Cataphract.API.Wraith.FileDownload;
import Cataphract.API.Wraith.Archive.FileUnzip;

/**
* A class that provides utilities for updating Cataphract
*
* @author DAK404 (https://github.com/DAK404)
* @version 2.0.0 (03-March-2024, Cataphract)
* @since 0.0.1 (Mosaic 0.0.1)
*/
public class NionUpdate
{
    /** Stores the username of the current user. */
    private String _username = "";

    /** Stores the value of user privileges. */
    private boolean isUserAdmin = false;

    /**
    * Constructor to store username and check if the current user is an administrator or not.
    *
    * @param username Username of the current user
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public NionUpdate(String username)throws Exception
    {
        _username = username;
        isUserAdmin = new Login(username).checkPrivilegeLogic();
    }

    /**
    * Updates Cataphract, fetching the latest release from remote,
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public void updater()throws Exception
    {
        //Check if the policy allows user to update of if user has the privilege to update the program
        if(new PolicyCheck().retrievePolicyValue("update").equals("on") || isUserAdmin)
        {
            IOStreams.println("---- Wyvern: Program Update Utility 2.0 ----");
            IOStreams.printAttention("[*] This will install the lastest version of Cataphract. Please ensure that there is internet connectivity.");
            IOStreams.printAttention("[*] After updating, Cataphract will require a restart to updated files.\n");
            IOStreams.printWarning("DO NOT TURN OFF THE SYSTEM, CHANGE NETWORK STATES OR CLOSE THIS PROGRAM.\nBY DOING THIS, YOU MIGHT RISK THE LOSS OF DATA OR PROGRAM INSTABILITY.");
            IOStreams.println("--------------------------------------------\n");

            downloadUpdate();
            installUpdate();
        }
        else
        IOStreams.printError("Policy Configuration Error!");
    }

    /**
    * Downloads the update
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private void downloadUpdate()throws Exception
    {
        IOStreams.println("Download Status: " + (new FileDownload(_username).downloadUpdate()?"Complete":"Failed"));
    }

    /**
    * Unzips the update file to the Cataphract install directory
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private void installUpdate()throws Exception
    {
        IOStreams.println("Installing Update...");
        new FileUnzip(_username).installUpdate();
    }
}
