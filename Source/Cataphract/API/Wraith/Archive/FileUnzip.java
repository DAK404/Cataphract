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

package Cataphract.API.Wraith.Archive;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import Cataphract.API.IOStreams;
import Cataphract.API.Dragon.Login;
import Cataphract.API.Minotaur.PolicyCheck;
import Cataphract.API.Wraith.FileWrite;

/**
* A utility class for unzipping files.
*
* @author DAK404 (https://github.com/DAK404)
* @version 1.0.0 (11-October-2023, Cataphract)
* @since 0.0.1 (Cataphract 0.0.1)
*/
public class FileUnzip
{
    /** Store value of user privileges. */
    private boolean isUserAdmin = false;

    /** Store value of the update mode; True if program is updating, False if program is not*/
    private boolean updateMode = false;

    /**
    * Constructor to check if the current user is an administrator or not.
    *
    * @param username The currently logged in username.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public FileUnzip(String username)throws Exception
    {
        isUserAdmin = new Login(username).checkPrivilegeLogic();
    }

    /**
    * Unzips a user file in the specified destination directory
    *
    * @param fileName Name of the file to be unzipped.
    * @param unzipDestination Destination for the file to be unzipped in.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public void unzip(String fileName, String unzipDestination)throws Exception
    {
        // Check the policy, and override it if the user has Administrator privileges
        if(new PolicyCheck().retrievePolicyValue("filemgmt").equals("on") || isUserAdmin)
        unzipLogic(fileName, unzipDestination);
        else
        IOStreams.printError("Policy Configuration Error!");
    }

    /**
    * Installs an update by unzipping the "Update.zip" file.
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public void installUpdate()throws Exception
    {
        updateMode = true;
        if(new PolicyCheck().retrievePolicyValue("update").equals("on") || isUserAdmin)
        unzipLogic("./Update.zip", "./");
        else
        IOStreams.printError("Policy Configuration Error!");
    }

    /**
    * Unzips a given zip file into the specified output directory.
    *
    * @param zipFilePath Path to the zip file.
    * @param outputDirectory Directory where the unzipped files will be placed.
    */
    private void unzipLogic(String zipFilePath, String outputDirectory)
    {
        try
        {
            // Open the zip file for reading
            FileInputStream fis = new FileInputStream(zipFilePath);
            FileOutputStream fos = null;
            try (ZipInputStream zipIn = new ZipInputStream(fis))
            {
                byte[] buffer = new byte[1024];
                ZipEntry entry;

                // Iterate through each entry in the zip file
                while ((entry = zipIn.getNextEntry()) != null)
                {
                    String entryName = entry.getName();
                    Path entryPath = Paths.get(outputDirectory, entryName);

                    if (entry.isDirectory())
                    {
                        // Create directories for directory entries
                        Files.createDirectories(entryPath);
                    }
                    else
                    {
                        // Create directories for file entries and write the file content
                        Files.createDirectories(entryPath.getParent());
                        try
                        {
                            if(updateMode)
                            {
                                IOStreams.printInfo("Installing File: " + entryName);
                                FileWrite.logger("Installing: " + entryName, "Update");
                            }

                            fos = new FileOutputStream(entryPath.toFile());
                            int bytesRead;

                            while ((bytesRead = zipIn.read(buffer)) != -1)
                            fos.write(buffer, 0, bytesRead);
                        }
                        catch(Exception e)
                        {
                            IOStreams.printError("File Error: " + entryName);
                        }
                    }
                    fos.close();
                    zipIn.closeEntry();
                }
            }
        }
        catch (Exception e)
        {
            // Handle exceptions (e.g., file not found, I/O errors)
            IOStreams.printError("Unable to proceed.");
        }

        System.gc();
    }
}
