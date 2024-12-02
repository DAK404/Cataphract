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

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import Cataphract.API.Anvil;
import Cataphract.API.IOStreams;
import Cataphract.API.Dragon.Login;
import Cataphract.API.Minotaur.Cryptography;
import Cataphract.API.Minotaur.PolicyCheck;
import Cataphract.API.Wraith.Archive.FileZip;
import Cataphract.API.Wraith.Archive.FileUnzip;

/**
* A utility class for file management.
*
* @author DAK404 (https://github.com/DAK404)
* @version 1.3.0 (12-August-2024, Cataphract)
* @since 0.0.1 (Zen Quantum 0.0.1)
*/
public class FileManagement
{
    /** Store the username of the current user */
    private String _username = "";
    /** Store the name of the current user */
    private String _name = "";
    /** Store the present working directory during file management */
    private String _presentWorkingDirectory = "";
    /** Store the default user home directory */
    private String _userHomeDirectory = "";

    /** Instantiate Console to get user inputs. */
    private Console console = System.console();

    /**
     * Constructor for FileManagement class.
     *
     * @param username The username of the current user
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    public FileManagement(String username)throws Exception
    {
        // Set username to the global variable
        _username = username;
        // Set the name of the user to the global variable
        _name = new Login(username).getNameLogic();
        // Initialize the present working directory
        _userHomeDirectory = ".|Users|Cataphract|" + _username + "|";
        _presentWorkingDirectory = _userHomeDirectory;
    }

    /*****************************************
     *      AUTHENTICATION/LOGIN METHOD      *
     *****************************************/

    /**
     * Logic to authenticate the current user logged in.
     *
     * @return {@code true} if the user creation was successful, {@code false} otherwise.
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private final boolean login()throws Exception
    {
        IOStreams.println("> Username: " + _name);
        return new Login(_username).authenticationLogic(Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> Password: "))), Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> Security Key: ")))) ;
    }

    /*****************************************
     * FILE & DIRECTORY MANAGEMENT UTILITIES *
     *****************************************/

    /**
     * Logic to check if the specified file or directory exists
     *
     * @param fileName The name of the file/directory to be checked
     * @return {@code true} if the file/directory exists, else {@code false} if the file/directory does not exist
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private boolean checkEntityExistence(String fileName)throws Exception
    {
        return new File(IOStreams.convertFileSeparator(fileName)).exists();
    }

    /**
     * Logic to delete the specified file or directory
     *
     * @param fileName The name of the file/directory to be deleted
     * @throws Exception Throws any exceptions encountered during runtime
     */
    private final void deleteEntity(String fileName)throws Exception
    {
        try
        {
            fileName = IOStreams.convertFileSeparator(_presentWorkingDirectory+fileName);
            if(checkEntityExistence(fileName))
            {
                File f=new File(fileName);
                if(f.isDirectory())
                deleteEntityHelper(f);
                else
                f.delete();
            }
            else
            IOStreams.printError("The Specified File/Directory Does Not Exist.");
            System.gc();
        }
        catch (Exception E)
        {
            //troubleshooting tips here
            E.printStackTrace();
        }
    }

    /**
     * Logic to help in recursively delete files and directories inside sub-directories
     *
     * @param fileName The name of the file/directory to be checked
     * @throws Exception Throws any exceptions encountered during runtime
     */
    private final void deleteEntityHelper(File fileName)throws Exception
    {
        if (fileName.listFiles() != null)
        {
            for (File fileInDirectory : fileName.listFiles())
                deleteEntityHelper(fileInDirectory);
        }
        fileName.delete();
    }

    /**
     * Logic to display the list of files and directories in a tree view
     *
     * @throws Exception Throws any exceptions encountered during runtime
     */
    private void viewDirectoryTree()throws Exception
    {
        // Create a File object for the present working directory
        File treeView = new File(IOStreams.convertFileSeparator(_presentWorkingDirectory));

        // Print the header for the tree view
        IOStreams.println("\n--- [ TREE VIEW ] ---\n");

        // Call the helper method to recursively display the directory tree
        viewDirTreeHelper(0, treeView);

        // Print a newline for better formatting
        IOStreams.println("");
    }

    /**
     * Helper method to recursively display in a tree view of the file and directories within sub-directories
     *
     * @param indent The level at which a file or directory is within a sub-directory
     * @param file The file or directory which will be displayed in the tree view
     */
    private final void viewDirTreeHelper(int indent, File file)
    {
        // Print the tree structure line prefix
        System.out.print("|");

        // Print the indentation for the current level
        for (int i = 0; i < indent; ++i)
            IOStreams.print("-");

        // Print the name of the file or directory, replacing the username with a custom label
        IOStreams.println(file.getName().replace(_username, _name + " [ USER HOME DIRECTORY ]"));

        // If the current file is a directory, list its contents
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            // Recursively call this method for each file in the directory, increasing the indentation
            for (File f : files)
                viewDirTreeHelper(indent + 2, f);
        }
    }

    /**
     * Logic to navigate to the previous directory
     *
     * @throws Exception Throws any exceptions encountered during runtime
     */
    private final void navPreviousDirectory()throws Exception
    {
        // Remove the trailing file separator from the present working directory
        _presentWorkingDirectory = _presentWorkingDirectory.substring(0, _presentWorkingDirectory.length() - 1);

        // Replace the last directory in the path with a single slash
        _presentWorkingDirectory = _presentWorkingDirectory.replace(
            _presentWorkingDirectory.substring(_presentWorkingDirectory.lastIndexOf('|'), _presentWorkingDirectory.length()),"|");

        // Check if the present working directory is the restricted user home directory
        if (_presentWorkingDirectory.equals(".|Users|Cataphract|"))
        {
            // Print an error message if access is denied
            IOStreams.printError("Permission Denied.");

            // Reset to the home directory
            resetToHomeDirectory();
        }
    }


    /**
     * Logic to reset the present working directory to the user home directory
     */
    private final void resetToHomeDirectory()
    {
        _presentWorkingDirectory = _userHomeDirectory;
    }

    /**
     * Logic to create a new directory
     *
     * @param fileName The name of the directory to be created
     * @throws Exception Throws any exceptions encountered during runtime
     */
    private final void makeDirectory(String fileName) throws Exception
    {
        new File(IOStreams.convertFileSeparator(_presentWorkingDirectory) + fileName).mkdirs();
    }

    /**
     * Logic to rename a file or directory
     *
     * @param fileName The name of the file or directory to be renamed
     * @param newFileName The new name of the file or directory
     * @throws Exception Throws any exceptions encountered during runtime
     */
    private final void renameEntity(String fileName, String newFileName) throws Exception
    {
        // Concatenate the present working directory with the file name
        fileName = _presentWorkingDirectory + fileName;
        newFileName = _presentWorkingDirectory + newFileName;

        // Check if the new file or directory already exists
        if (checkEntityExistence(newFileName))
            // Rename the file or directory if it exists
            new File(fileName).renameTo(new File(newFileName));
        else
            // Print an error message if the specified file or directory does not exist
            IOStreams.printError("Specified file or directory does not exist.");
    }

    /**
     * Logic to copy or move a given file or directory
     *
     * @param fileName The name of the file or directory to be moved
     * @param destination The location of the file or directory to be moved
     * @param move Determines if the source file or directory needs to be deleted after copying
     * @throws Exception Throws any exceptions encountered during runtime
     */
    private final void copyMoveEntity(String fileName, String destination, boolean move)throws Exception
    {
        // Convert paths from Nion paths to OS specific paths
        fileName = IOStreams.convertFileSeparator(fileName);
        destination = IOStreams.convertFileSeparator(destination);

        // Check if the specified file or directory is valid
        if(!checkEntityExistence(fileName) && !checkEntityExistence(destination))
            IOStreams.printError("Invalid file name or destination.");

        // Call helper method to recursively copy/move the specified file/directory to the destination
        copyMoveHelper(new File(_presentWorkingDirectory + fileName), new File(_presentWorkingDirectory + destination), move);
    }

    /**
     * Logic to recursively copy or move the files or directories to the provided destination path.
     *
     * @param fileName The name of the file or directory to be moved
     * @param destination The location of the file or directory to be moved
     * @param move Determines if the source file or directory needs to be deleted after copying
     * @throws Exception Throws any exceptions encountered during runtime
     */
    private final void copyMoveHelper(File source, File destination, boolean move)throws Exception
    {
        // Check if the source is a directory
        if (source.isDirectory())
        {
            // Create the destination directory
            destination.mkdirs();

            // Iterate through each file in the source directory
            for (File sourceChild : source.listFiles()) {
                // Create a new destination file for each source file
                File destChild = new File(destination, sourceChild.getName());

                // Recursively call this method for each file in the directory
                copyMoveHelper(sourceChild, destChild, move);
            }
        }
        else
        {
            // Copy the source file to the destination
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // If move is true, delete the source file after copying
            if (move)
                Files.delete(source.toPath());
        }
    }

    /**
     * Logic to list the diles and directories in the present working directory
     *
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private final void listEntities() throws Exception
    {
        // Define the format for displaying the directory/file information
        String format = "%1$-32s| %2$-24s| %3$-10s| %4$-32s\n";
        String c = "-";
        String ls = IOStreams.convertFileSeparator(_presentWorkingDirectory);
        // Check if the present working directory exists
        if (checkEntityExistence(ls))
        {
            // Create a File object for the present working directory
            File dPath = new File(ls);
            // Print a newline for better formatting
            System.out.println("\n");
            // Format and print the header for the directory listing
            String disp = String.format(format, "Directory/File Name", "File Size [In KB]", "Type", "MD5 Hash");
            System.out.println(disp + c.repeat(disp.length()) + "\n");

            // Iterate through each file in the directory
            for (File file : dPath.listFiles())
            {
                // Format and print the file or directory information
                System.out.format(format, file.getName().replace(_username, _name), file.length() / 1024 + " KB", file.isDirectory() ? "Directory" : "File", file.isDirectory() ? "" : Cryptography.fileToMD5(file));
            }
            // Print a newline for better formatting
            System.out.println();
        }
        else
        {
            // Print an error message if the specified file or directory does not exist
            IOStreams.printError("The Specified File/Directory Does Not Exist.");
        }
    }


    /**
     * Logic to change the present working directory to a given directory
     *
     * @param destination The name of the directory to navigate to
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private final void changeDirectory(String destination)throws Exception
    {
        // Check if the destination is the parent directory
        if (destination.equals(".."))
        {
            // Navigate to the previous directory
            navPreviousDirectory();

        }
        else
        {
            // Check if the specified destination exists within the present working directory
            if (checkEntityExistence(_presentWorkingDirectory + destination))
            {
                // Update the present working directory to the new destination
                _presentWorkingDirectory = _presentWorkingDirectory + destination + "|";
            }
            else
            {
                // Print an error message if the specified destination does not exist
                IOStreams.printError("'" + destination + "' does not exist");
            }
        }
    }

    /*****************************************
     * GRINCH FILE MANAGEMENT & SCRIPT LOGIC *
     *****************************************/

    /**
     * Logic to perform policy check, login and the file management actions
     *
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    public void fileManagementLogic()throws Exception
    {
        // Check if file management policy is enabled or if the user has the necessary privileges
        if (new PolicyCheck().retrievePolicyValue("filemgmt").equals("on") || new Login(_username).checkPrivilegeLogic())
        {
            // Check if the user is logged in
            if (login())
            {
                String inputValue = "";
                // Loop to continuously read and execute commands until 'exit' is entered
                do
                {
                    // Read a line of input from the console
                    inputValue = console.readLine(_name + "@" + IOStreams.convertFileSeparator(_presentWorkingDirectory).replace(_username, _name) + "> ");

                    // Interpret and execute the command
                    grinchInterpreter(inputValue);
                }
                while (!inputValue.equalsIgnoreCase("exit"));
            }
            else
                IOStreams.printError("Invalid Credentials.");
        }
        else
            IOStreams.printError("Policy Management System - Permission Denied.");
    }

    /**
     * [ OVERLOAD ] Provide a method to execute file management actions from a script file
     *
     * @param scriptFileName The name of the script file
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    public void fileManagementLogic(String scriptFileName)throws Exception
    {
        // Check if file management and script policies are enabled or if the user has the necessary privileges
        if ((new PolicyCheck().retrievePolicyValue("filemgmt").equals("on") && new PolicyCheck().retrievePolicyValue("script").equals("on")) || new Login(_username).checkPrivilegeLogic())
        {
            // Validate the script file name
            if (scriptFileName == null || scriptFileName.equalsIgnoreCase("") || scriptFileName.startsWith(" ") || new File(scriptFileName).isDirectory() || !(new File(IOStreams.convertFileSeparator(".|Users|Cataphract|" + _username + "|" + scriptFileName + ".fmx")).exists()))
                IOStreams.printError("Invalid Script File!");
            else
            {
                // Check if the user is logged in
                if (login())
                {
                    // Initialize a stream to read the given file
                    BufferedReader br = new BufferedReader(new FileReader(scriptFileName));

                    // Initialize a string to hold the contents of the script file being executed
                    String scriptLine;

                    // Read the script file, line by line
                    while (!(scriptLine = br.readLine()).equalsIgnoreCase("End Grinch"))
                    {
                        // Check if the line is a comment or is blank in the script file and skip the line
                        if (scriptLine.startsWith("#") || scriptLine.equalsIgnoreCase(""))
                            continue;

                        // Check if End Script command is encountered, which will stop the execution of the script
                        else if (scriptLine.equalsIgnoreCase("End Script"))
                            break;

                        // Read the command in the script file, and pass it on to menuLogic(<command>) for it to be processed
                        grinchInterpreter(scriptLine);
                    }

                    // Close the streams, run the garbage collector and clean
                    br.close();
                }
                else
                    IOStreams.printError("Invalid Credentials.");
            }
        }
        else
            IOStreams.printError("Policy Management System - Permission Denied.");
    }

    /**
     * Logic to process user input and perform the necessary actions
     *
     * @param command The command string input provided by the user
     * @throws Exception Throws any exceptions encountered during runtime.
     */
    private void grinchInterpreter(String command)throws Exception
    {
        // Convert present working directory string to Nion File Separator format for compatibility
        _presentWorkingDirectory = IOStreams.convertToNionSeparator(_presentWorkingDirectory);

        // Split the command string into an array of command arguments
        String[] commandArray = IOStreams.splitStringToArray(command);

        // Switch statement to handle different commands
        switch (commandArray[0].toLowerCase())
        {
            case "cut":
            case "move":
            case "mov":
            case "mv":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 3)
                    IOStreams.printError("Invalid Syntax.");
                else
                    // Call the method to move the entity
                    copyMoveEntity(commandArray[1], commandArray[2], true);
            break;

            case "copy":
            case "cp":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 3)
                    IOStreams.printError("Invalid Syntax.");
                else
                    // Call the method to copy the entity
                    copyMoveEntity(commandArray[1], commandArray[2], false);
            break;

            case "delete":
            case "del":
            case "rm":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 2)
                    IOStreams.printError("Invalid Syntax.");
                else
                    // Call the method to delete the entity
                    deleteEntity(commandArray[1]);
            break;

            case "rename":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 2)
                    IOStreams.printError("Invalid Syntax.");
                else
                    // Call the method to rename the entity
                    renameEntity(commandArray[1], commandArray[2]);
            break;

            case "mkdir":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 2)
                    IOStreams.printError("Invalid Syntax.");
                else
                    // Call the method to create a directory
                    makeDirectory(commandArray[1]);
            break;

            case "edit":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 2)
                    IOStreams.printError("Invalid Syntax.");
                else
                    // Call the method to edit a file
                    new FileWrite(_username).editFile(commandArray[1], _presentWorkingDirectory);
            break;

            case "read":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 2)
                    IOStreams.printError("Invalid Syntax.");
                else
                    // Call the method to read a file
                    new FileRead(_username).readUserFile(_presentWorkingDirectory + commandArray[1]);
            break;

            case "pwd":
                // Print the present working directory
                IOStreams.println((_presentWorkingDirectory).replace(_username, _name));
            break;

            case "cd":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 2)
                    IOStreams.printError("Invalid Syntax.");
                else
                    // Call the method to change the directory
                    changeDirectory(commandArray[1]);
            break;

            case "cd..":
                // Navigate to the previous directory
                navPreviousDirectory();
            break;

            case "tree":
                // Display the directory tree
                viewDirectoryTree();
            break;

            case "dir":
            case "ls":
                // List the entities in the current directory
                listEntities();
            break;

            case "download":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 3)
                    IOStreams.printError("Invalid Syntax.");
                else
                    // Call the method to download a file
                    new FileDownload(_username).downloadFile(commandArray[1], commandArray[2]);
            break;

            case "home":
                // Reset to the home directory
                resetToHomeDirectory();
            break;

            case "exit":
            case "":
                // Exit the interpreter
            break;

            case "zip":
                // Check if the command has the correct number of arguments
                if (commandArray.length < 3)
                    IOStreams.printError("Invalid Syntax.");
                else
                    new FileZip(_username).zipFile(commandArray[1], _presentWorkingDirectory + commandArray[2]);
            break;

            case "unzip":
                    // Check if the command has the correct number of arguments
                    if (commandArray.length < 3)
                    IOStreams.printError("Invalid Syntax.");
                else
                    new FileUnzip(_username).unzip(_presentWorkingDirectory + commandArray[1], _presentWorkingDirectory + commandArray[2]);
            break;

            default:
                // Pass the command to the Anvil interpreter for further processing
                Anvil.anvilInterpreter(commandArray);
        }
    }
}
