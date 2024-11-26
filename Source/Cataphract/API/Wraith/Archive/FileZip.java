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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import Cataphract.API.IOStreams;
import Cataphract.API.Dragon.Login;
import Cataphract.API.Minotaur.PolicyCheck;


/**
* Utility class for zipping files and directories.
*
* @author DAK404 (https://github.com/DAK404)
* @version 1.0.0 (11-October-2023, Cataphract)
* @since 0.0.1 (Cataphract 0.0.1)
*/
public class FileZip
{
    /** Store value of user privileges. */
    private boolean _isUserAdmin = false;

    /** Store the username of the current user */
    private String _username = "";

    /**
    * Constructor to check if the current user is an administrator or not.
    *
    * @param username The currently logged in username
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public FileZip(String username)throws Exception
    {
        _username = username;
        _isUserAdmin = new Login(username).checkPrivilegeLogic();
    }

    /**
    * Zips a directory into a specified zip file.
    *
    * @param zipFileName The name of the zip file to create.
    * @param directoryToCompress The directory to compress.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public void zipFile(String zipFileName, String directoryToCompress) throws Exception
    {
        directoryToCompress = IOStreams.convertFileSeparator(directoryToCompress);
        // Check the policy if file zipping is allowed in the policy file, can be bypassed by the accounts with administrator privileges
        if(new PolicyCheck().retrievePolicyValue("filemgmt").equals("on") || _isUserAdmin)
        {
            // Create a FileOutputStream for the zip file
            FileOutputStream fos = new FileOutputStream(IOStreams.convertFileSeparator(".|Users|Cataphract|" + _username + "|") + zipFileName);
            // Create a ZipOutputStream using the FileOutputStream
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            // Get the File object for the directory to be zipped
            File directoryToZip = new File(directoryToCompress);
            // Recursively zip the contents of the directory
            zipFileLogic(directoryToZip, directoryToZip.getName(), zipOut);

            // Close the ZipOutputStream and FileOutputStream
            zipOut.close();
            fos.close();
        }
        else
        IOStreams.printError("Policy Management System - Permission Denied.");
    }

    /**
    * Recursively zips a file or directory.
    *
    * @param fileToZip The file or directory to zip.
    * @param fileName The name of the file or directory.
    * @param zipOut The ZipOutputStream to write to.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private void zipFileLogic(File fileToZip, String fileName, ZipOutputStream zipOut) throws Exception
    {
        try
        {
            if (fileToZip.isDirectory())
            {
                // If it's a directory, create a zip entry for it
                if (fileName.endsWith(File.separator))
                zipOut.putNextEntry(new ZipEntry(fileName));
                else
                zipOut.putNextEntry(new ZipEntry(fileName + File.separator));

                // Get the list of children files and recursively zip them
                File[] children = fileToZip.listFiles();
                if (children != null)
                {
                    for (File childFile : children)
                    {
                        zipFileLogic(childFile, fileName + File.separator + childFile.getName(), zipOut);
                    }
                }
            }
            else
            {
                // If it's a file, create a zip entry for it
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOut.putNextEntry(zipEntry);

                // Read and write the file content in chunks
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0)
                {
                    zipOut.write(bytes, 0, length);
                }

                // Close the FileInputStream
                fis.close();
            }
        }
        catch (Exception e)
        {
            // Handle any exceptions here
            e.printStackTrace();
        }
    }
}
