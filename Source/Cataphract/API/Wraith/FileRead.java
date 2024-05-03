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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import Cataphract.API.IOStreams;
import Cataphract.API.Build;

/**
* A class provides methods for reading files and displaying their contents.
*
* @author DAK404 (https://github.com/DAK404)
* @version 3.6.2 (20-February-2024, Cataphract)
* @since 0.0.1 (Zen Quantum 0.0.1)
*/
public class FileRead
{

    /** Flag to indicate whether help mode is enabled. */
    private boolean helpMode = false;

    /** Static variable to store the file name. */
    private static File fileName = null;

    /**
     * Sole constructor. (For invocation by subclass constructors, typically implicit.)
     */
    public FileRead()
    {
    }

    /**
    * Checks the validity of the file name.
    *
    * @return {@code true} if the file name is valid, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final boolean checkFileValidity() throws Exception
    {
        // Return false if the file name is null, empty, or starts with a space; otherwise, return true
        return !(fileName.getName() == null || fileName.getName().equals("") || fileName.getName().startsWith(" "));
    }

    /**
    * Handles the logic for reading the file.
    *
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private void readFileLogic() throws Exception
    {
        if (!checkFileValidity())
        {
            // If the file name is invalid
            IOStreams.printError("Invalid File Name! Please Enter A Valid File Name.");
        }
        else if (!fileName.exists())
        {
            // If the specified file does not exist
            IOStreams.printError("The Specified File Does Not Exist. Please Enter A Valid File Name.");
        }
        else
        {
            // Flag to control file reading loop
            boolean continueFileRead = true;

            // Display build information
            Build.viewBuildInfo();

            // Create a BufferedReader to read from the file
            BufferedReader bufferObject = new BufferedReader(new FileReader(fileName));

            // Variable to store file contents
            String fileContents = "";

            // If help mode is enabled
            if (helpMode)
            {
                do
                {
                    // Read a line from the file
                    fileContents = bufferObject.readLine();

                    if (fileContents.equalsIgnoreCase("<end of page>"))
                    {
                        // If it reaches the end of the page marker, prompt the user to continue or exit the help viewer
                        if (IOStreams.confirmReturnToContinue("", "else type EXIT to quit Help Viewer.\\n" + "~DOC_HLP?> ").equalsIgnoreCase("exit"))
                        // Set flag to stop reading
                        continueFileRead = false;
                        else
                        {
                            // Clear the screen and display build information and continue reading the file
                            Build.viewBuildInfo();
                            continue;
                        }
                    }
                    // If it reaches the end of the help file marker
                    else if (fileContents.equalsIgnoreCase("<end of help>"))
                    {
                        // Print end of help file message
                        IOStreams.println("\n\nEnd of Help File.");
                        break;
                    }
                    // If it encounters a comment line, skip this line
                    else if (fileContents.startsWith("#"))
                    {
                        continue;
                    }
                    // Print the file contents
                    IOStreams.println(fileContents);
                }
                // Continue until the end of file or instructed to stop
                while (fileContents != null || continueFileRead);
            }
            // If help mode is not enabled
            else
            {
                do
                {
                    // Read a line from the file
                    fileContents = bufferObject.readLine();
                    // Print the file contents
                    IOStreams.println(fileContents);
                }
                // Continue until the end of file
                while (fileContents != null);
            }

            // Close the streams
            bufferObject.close();

            // Request garbage collection to free up resources
            System.gc();

            // Prompt to return to continue
            Cataphract.API.IOStreams.confirmReturnToContinue();
        }
    }

    /**
    * Reads a file specified by the user.
    *
    * @param userFileName The name of the user's file to be read.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public void readUserFile(String userFileName) throws Exception
    {
        // Set the file name
        fileName = new File("./Users/Cataphract/" + userFileName);
        // Perform file reading logic
        readFileLogic();
    }

    /**
    * Reads a help file.
    *
    * @param helpFile The name of the help file to be read.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public void readHelpFile(String helpFile) throws Exception
    {
        // Enable help mode
        helpMode = true;
        // Set the file name
        fileName = new File("./Docs/Cataphract/Help/" + helpFile);
        // Perform file reading logic
        readFileLogic();
    }
}
