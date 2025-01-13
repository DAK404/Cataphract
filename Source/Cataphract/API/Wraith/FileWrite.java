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

package Cataphract.API.Wraith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.Console;

import Cataphract.API.IOStreams;
import Cataphract.API.Astaroth.Time;
import Cataphract.API.Dragon.Login;
import Cataphract.API.Minotaur.PolicyCheck;

/**
* A class that provides methods for writing to files and logging messages.
*
* @author DAK404 (https://github.com/DAK404)
* @version 3.6.1 (20-February-2024, Cataphract)
* @since 0.0.1 (Zen Quantum 0.0.1)
*/
public class FileWrite
{
    /** Store the current username to check privileges */
    private String _username = "";

    /**
     * Constructor to be used for logging data to a log file
     */
    public FileWrite()
    {
    }

    /**
     * Constructor to be used for writing data into a user created file
     *
     * @param username The username of the currently logged in user
     */
    public FileWrite(String username)
    {
        _username = username;
    }

    /**
    * Edits a file by either overwriting, appending, or returning based on user input.
    *
    * @param fileName The name of the file to be edited.
    * @param dir The directory path where the file is located.
    */
    public final void editFile(String fileName, String dir)throws Exception
    {
        fileName = IOStreams.convertFileSeparator(fileName);
        dir = IOStreams.convertFileSeparator(dir);
        System.out.println(dir);
        // Check the policy if file writing is allowed in the policy file, can be bypassed by the accounts with administrator privileges
        if (new PolicyCheck().retrievePolicyValue("update").equals("on") || new Login(_username).checkPrivilegeLogic())
        {
            try
            {
                System.out.println(new File(dir).exists());
                // Check if the provided file name is valid
                if (new File(dir).exists())
                {
                    // Flag to determine if file content should be appended or overwritten
                    boolean appendFile = true;
                    // Initialize the message variable
                    String message = "";

                    // Get the console object to read user input
                    Console console = System.console();

                    // Create a File object representing the file to be edited
                    File writeToFile = new File(dir + fileName);

                    // Check if the file already exists
                    if (writeToFile.exists())
                    {
                        // Prompt the user for action (overwrite, append, return, or help)
                        switch (console.readLine("[ ATTENTION ] : A file with the same name has been found in this directory. Do you want to OVERWRITE it, APPEND to the file, or GO BACK? \n\nOptions:\n[ OVERWRITE | APPEND | RETURN | HELP ]\n\n> ").toLowerCase()) {
                            // If the user chooses to overwrite the file
                            case "overwrite":
                            appendFile = false; // Set appendFile flag to false to overwrite the file content
                            System.out.println("The new content will overwrite the previous content present in the file!");
                            break;
                            // If the user chooses to append to the file
                            case "append":
                            System.out.println("The new content will be added to the end of the file! Previous data will remain unchanged.");
                            break;
                            // If the user chooses to return without making any changes
                            case "return":
                            return;
                            // If the user requests help
                            case "help":
                            System.out.println("Work in Progress");
                            break;
                            // If the user enters an invalid choice
                            default:
                            System.out.println("Invalid choice. Exiting...");
                            return;
                        }
                    }

                    // Create a BufferedWriter to write to the file
                    BufferedWriter obj = new BufferedWriter(new FileWriter(writeToFile, appendFile));
                    PrintWriter pr = new PrintWriter(obj);

                    System.out.println("Wraith Text Editor 1.5");
                    System.out.println("______________________\n");

                    System.out.println("\nEditing File : " + fileName + "\n\n");

                    // Prompt the user for input and write to the file until "<exit>" is entered
                    do
                    {
                        pr.println(message); // Write the message to the file
                        message = console.readLine(); // Read the next line of input from the user
                    }
                    while (!(message.equals("<exit>"))); // Continue until "<exit>" is entered

                    // Close the streams
                    pr.close();
                    obj.close();

                    // Request garbage collection to free up resources
                    System.gc();
                }
            }
            catch (FileNotFoundException fnfe)
            {
                IOStreams.printError("File Error - The Specified path is either invalid or is not found.");
            }
            catch (Exception E)
            {
                // Handle any exceptions thrown during runtime
                new Cataphract.API.ExceptionHandler().handleException(E);
            }
        }
        else
            IOStreams.printError("Policy Management System - Permission Denied.");
    }

    /**
    * Logs a message to a file.
    *
    * @param printToFile The message to be logged.
    * @param fileName The name of the log file.
    */
    public static final void logger(String printToFile, String fileName)
    {
        try
        {
            String logfilePath = IOStreams.convertFileSeparator(".|System|Cataphract|Public|Logs|");
            // Check if the provided file name is valid
            if (checkFileValidity(fileName)) {

                // Set the default log file path if it doesn't exist
                logfilePath = (new File(logfilePath).exists() ? logfilePath : IOStreams.convertFileSeparator(".|Logs|Cataphract|"));

                // Create the directory structure for logs if it doesn't exist
                File logFile = new File(logfilePath);
                if (!logFile.exists())
                logFile.mkdirs();

                // Create a BufferedWriter to write to the log file
                BufferedWriter obj = new BufferedWriter(new FileWriter(logfilePath + fileName + ".log", true));
                PrintWriter pr = new PrintWriter(obj);
                // Write the message along with the timestamp to the log file
                pr.println(new Time().getDateTimeUsingSpecifiedFormat("dd-MMMM-yyyy HH:mm:ss") + " (" + new Time().getUnixEpoch() +  "): " + printToFile);

                // Close the streams
                pr.close();
                obj.close();
            }
            else
            // Print an error message if the provided file name is invalid
            IOStreams.printError("The provided file name is invalid. Please provide a valid file name to continue.");
        }
        catch (Exception e)
        {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
        // Request garbage collection to free up resources
        System.gc();
    }

    /**
    * Checks the validity of a file name.
    *
    * @param fileName The name of the file to be checked.
    * @return {@code true} if the file name is valid, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private static final boolean checkFileValidity(String fileName) throws Exception
    {
        // Return false if the file name is null, empty, or starts with a space; otherwise, return true
        return !(fileName == null || fileName.equals("") || fileName.startsWith(" "));
    }
}
