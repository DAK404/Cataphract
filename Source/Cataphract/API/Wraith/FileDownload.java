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

package Cataphract.API.Wraith;

import java.io.FileOutputStream;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import java.net.URI;
import java.net.URL;

import Cataphract.API.IOStreams;
import Cataphract.API.Dragon.Login;
import Cataphract.API.Minotaur.PolicyCheck;

/**
* A utility class for downloading files from URLs.
*
* @author DAK404 (https://github.com/DAK404)
* @version 3.1.0 (11-October-2023, Cataphract)
* @since 0.0.1 (Zen Quantum 0.0.1)
*/
public class FileDownload
{
    /** Variable to store if the current user has administrator privileges */
    private boolean isUserAdmin = false;

    /**
    * Constructor to check if the current user is an administrator or not.
    *
    * @param username The currently logged in username
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public FileDownload(String username)throws Exception
    {
        isUserAdmin = new Login(username).checkPrivilegeLogic();
    }

    /**
    * Downloads a file from the specified URL.
    *
    * @param URL The URL of the file to download.
    * @param fileName The desired name for the downloaded file.
    * @return {@code true} if the download was successful, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public final boolean downloadFile(String URL, String fileName) throws Exception
    {
        boolean status = false;

        // Attempt to download the file using NIO only if the policy is configured to "on"
        if (new PolicyCheck().retrievePolicyValue("download").equals("on") || isUserAdmin)
        status = downloadUsingNIO(URL, fileName);

        // Check for an invalid URL
        if (URL == null || fileName == null || URL.equalsIgnoreCase("") || fileName.equalsIgnoreCase(""))
        IOStreams.printError("Invalid File Name. Enter a valid file name.");

        return status;
    }

    /**
    * Downloads an update file based on the provided username.
    *
    * @return {@code true} if the update download was successful, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public final boolean downloadUpdate() throws Exception
    {
        boolean status = false;

        if (new PolicyCheck().retrievePolicyValue("update").equals("on") || isUserAdmin)
        {
            // Define the URL for the update file.
            String updateFileURL = "https://github.com/DAK404/Cataphract/releases/download/TestBuilds/Cataphract.zip";
            IOStreams.printInfo("Downloading update file from: " + updateFileURL);
            // Attempt to download the update file using NIO.
            status = downloadUsingNIO(updateFileURL, "Update.zip");
        }
        else
        IOStreams.println("Policy Restriction: Program Update Disallowed! Please contact the Administrator for more information.");

        return status;
    }

    /**
    * Downloads a file using NIO (non-blocking I/O).
    *
    * @param urlStr The URL of the file to download.
    * @param fileName The desired name for the downloaded file.
    * @return {@code true} if the download was successful, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime. if any error occurs during the download process.
    */
    private boolean downloadUsingNIO(String urlStr, String fileName) throws Exception
    {
        boolean status = false;

        try
        {
            // Convert the URL string to a URL object.
            URL url = new URI(urlStr).toURL();

            // Open a readable byte channel from the URL stream.
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());

            // Create a file output stream for the specified file.
            FileOutputStream fos = new FileOutputStream(fileName);

            // Transfer data from the channel to the file.
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            // Close the streams.
            fos.close();
            rbc.close();

            // Download successful.
            status = true;
        }
        catch (Exception e)
        {
            // Print any exceptions that occur during the download.
            e.printStackTrace();
        }

        // Explicitly request garbage collection (optional).
        System.gc();

        // Return the status of the download
        return status;
    }
}
