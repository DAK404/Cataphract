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

/**
* A utility class for file management.
*
* @author DAK404 (https://github.com/DAK404)
* @version 1.3.0 (12-August-2024, Cataphract)
* @since 0.0.1 (Zen Quantum 0.0.1)
*/
public class FileManagement
{
    private String _username = "";
    private String _name = "";
    private String _presentWorkingDirectory = "";

    private Console console = System.console();

    public FileManagement(String username)throws Exception
    {
        _username = username;
        _name = new Cataphract.API.Dragon.Login(username).getNameLogic();
        _presentWorkingDirectory = "./Users/Cataphract/" + _username + "/";
    }

    /*****************************************
     *      AUTHENTICATION/LOGIN METHOD      *
     *****************************************/

    private final boolean login()throws Exception
    {
        IOStreams.println("> Username: " + _name);
        return new Cataphract.API.Dragon.Login(_username).authenticationLogic(Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> Password: "))), Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("> Security Key: ")))) ;
    }

    /*****************************************
     * FILE & DIRECTORY MANAGEMENT UTILITIES *
     *****************************************/

    private boolean checkEntityExistence(String fileName)throws Exception
    {
        return new File(fileName).exists();
    }

    private final void deleteEntity(String delFile)throws Exception
    {
        try
        {
            delFile = _presentWorkingDirectory+delFile;
            if(checkEntityExistence(delFile))
            {
                File f=new File(delFile);
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

    private final void deleteEntityHelper(File delfile)throws Exception
    {
        if (delfile.listFiles() != null)
        {
            for (File fileInDirectory : delfile.listFiles())
                deleteEntityHelper(fileInDirectory);
        }
        delfile.delete();
    }

    private void viewDirectoryTree()throws Exception
    {
        File treeView = new File(_presentWorkingDirectory);
        IOStreams.println("\n--- [ TREE VIEW ] ---\n");
        viewDirTreeHelper(0, treeView);
        IOStreams.println("");
    }

    private final void viewDirTreeHelper(int indent, File file) {
        System.out.print("|");

        for (int i = 0; i < indent; ++i)
        IOStreams.print("-");

        IOStreams.println(file.getName().replace(_username, _name + " [ USER HOME DIRECTORY ]"));

        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            for (File f : files)
            viewDirTreeHelper(indent + 2, f);
        }
    }

    private final void navPreviousDirectory()throws Exception
    {
        _presentWorkingDirectory = _presentWorkingDirectory.substring(0, _presentWorkingDirectory.length() - 1);
        _presentWorkingDirectory = _presentWorkingDirectory.replace(_presentWorkingDirectory.substring(_presentWorkingDirectory.lastIndexOf('/'), _presentWorkingDirectory.length()), "/");
        if (_presentWorkingDirectory.equals("./Users/Cataphract/"))
        {
            IOStreams.printError("Permission Denied.");
            resetToHomeDirectory();
        }
    }

    private final void resetToHomeDirectory()
    {
        _presentWorkingDirectory = "./Users/Cataphract/" + _username + '/'  ;
    }

    private final void makeDirectory(String fileName) throws Exception
    {
        new File(_presentWorkingDirectory + fileName).mkdirs();
    }

    private final void renameEntity(String fileName, String newFileName) throws Exception
    {
        fileName = _presentWorkingDirectory + fileName;
        newFileName = _presentWorkingDirectory + newFileName;

        if(checkEntityExistence(newFileName))
            new File(fileName).renameTo(new File(newFileName));
        else
            IOStreams.printError("Specified file or directory does not exist.");
    }

    private final void copyMoveEntity(String fileName, String destination, boolean move)throws Exception
    {
        if(!checkEntityExistence(fileName) && !checkEntityExistence(destination))
            IOStreams.printError("Invalid file name or destination. Permission Denied.");

        copyMoveHelper(new File(_presentWorkingDirectory + fileName), new File(_presentWorkingDirectory + destination), move);
    }

    private final void copyMoveHelper(File source, File destination, boolean move)throws Exception
    {
        if (source.isDirectory())
        {
            destination.mkdirs();
            for (File sourceChild : source.listFiles())
            {
                File destChild = new File(destination, sourceChild.getName());
                copyMoveHelper(sourceChild, destChild, move);
            }
        }
        else
        {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            if (move)
            {
                Files.delete(source.toPath());
            }
        }
    }

    private final void listEntities()throws Exception
    {
        //String format = "%1$-60s|%2$-50s|%3$-20s\n";
        String format = "%1$-32s| %2$-24s| %3$-10s\n";
        String c = "-";
        if(checkEntityExistence(_presentWorkingDirectory))
        {
            File dPath=new File(_presentWorkingDirectory);
            System.out.println("\n");
            String disp = (String.format(format, "Directory/File Name", "File Size [In KB]","Type"));
            System.out.println(disp + c.repeat(disp.length()) + "\n");
            for(File file : dPath.listFiles())
            {
                //System.out.format(String.format(format, file.getPath().replace(User,Name), file.getName().replace(User,Name), file.length()/1024+" KB"));
                System.out.format(String.format(format, file.getName().replace(_username, _name), file.length()/1024+" KB", file.isDirectory()?"Directory":"File"));
            }
            System.out.println();
        }
        else
        IOStreams.printError("The Specified File/Directory Does Not Exist.");
        System.gc();
    }

    private final void changeDirectory(String destination)throws Exception
    {
        if(destination.equals(".."))
        {
            navPreviousDirectory();
            System.gc();
        }
        else
        {
            if(checkEntityExistence(_presentWorkingDirectory + destination))
            {
                _presentWorkingDirectory = _presentWorkingDirectory + destination + "/";
            }
            else
            {
                IOStreams.printError("\'" + destination + "\' does not exist");
            }
        }
    }

    /*****************************************
     * GRINCH FILE MANAGEMENT & SCRIPT LOGIC *
     *****************************************/

    public void fileManagementLogic()throws Exception
    {
        if (new PolicyCheck().retrievePolicyValue("filemgmt").equals("on") || new Login(_username).checkPrivilegeLogic())
        {
            if(login())
            {
                String inputValue = "";
                do
                {
                    inputValue = console.readLine(_name + "@" + _presentWorkingDirectory.replace(_username, _name) + "> ");
                    grinchInterpreter(inputValue);
                }
                while(!inputValue.equalsIgnoreCase("exit"));
            }
            else
                IOStreams.printError("Invalid Credentials.");
        }
        else
            IOStreams.printError("Policy Management System - Permission Denied.");
    }

    public void fileManagementLogic(String scriptFileName)throws Exception
    {
        if ((new PolicyCheck().retrievePolicyValue("filemgmt").equals("on") && new PolicyCheck().retrievePolicyValue("script").equals("on")) || new Login(_username).checkPrivilegeLogic())
        {
            if(scriptFileName == null || scriptFileName.equalsIgnoreCase("") || scriptFileName.startsWith(" ") || new File(scriptFileName).isDirectory() || ! (new File("./Users/Truncheon/" + _username + "/" + scriptFileName + ".fmx").exists()))
            {
                IOStreams.printError("Invalid Script File!");
            }
            else
            {
                if(login())
                {
                    //Initialize a stream to read the given file.
                    BufferedReader br = new BufferedReader(new FileReader(scriptFileName));
                    //Initialize a string to hold the contents of the script file being executed.
                    String scriptLine;

                    //Read the script file, line by line.
                    while ((scriptLine = br.readLine()) != "<EndGrinch>")
                    {
                        //Check if the line is a comment or is blank in the script file and skip the line.
                        if(scriptLine.startsWith("#") || scriptLine.equalsIgnoreCase(""))
                        continue;

                        //Check if End Script command is encountered, which will stop the execution of the script.
                        else if(scriptLine.equalsIgnoreCase("End Script"))
                        break;

                        //Read the command in the script file, and pass it on to menuLogic(<command>) for it to be processed.
                        grinchInterpreter(scriptLine);
                    }

                    //Close the streams, run the garbage collector and clean.
                    br.close();
                }
                else
                {
                    IOStreams.printError("Invalid Credentials.");
                }
            }
        }
        else
            IOStreams.printError("Policy Management System - Permission Denied.");
    }

    private void grinchInterpreter(String command)throws Exception
    {
        String[] commandArray = Cataphract.API.Anvil.splitStringToArray(command);
        switch(commandArray[0].toLowerCase())
        {
            case "cut":
            case "move":
            case "mov":
            case "mv":
            if(commandArray.length < 3)
                IOStreams.printError("Invalid Syntax.");
            else
            copyMoveEntity(commandArray[1], commandArray[2], true);
            break;

            case "copy":
            case "cp":
            if(commandArray.length < 3)
                IOStreams.printError("Invalid Syntax.");
            else
            copyMoveEntity(commandArray[1], commandArray[2], false);
            break;

            case "delete":
            case "del":
            case "rm":
            if(commandArray.length < 2)
                IOStreams.printError("Invalid Syntax.");
            else
            deleteEntity(commandArray[1]);
            break;

            case "rename":
            if(commandArray.length < 2)
                IOStreams.printError("Invalid Syntax.");
            else
            renameEntity(commandArray[1], commandArray[2]);
            break;

            case "mkdir":
            if(commandArray.length < 2)
                IOStreams.printError("Invalid Syntax.");
            else
            makeDirectory(commandArray[1]);
            break;

            case "edit":
            if(commandArray.length < 2)
                IOStreams.printError("Invalid Syntax.");
            else
                new FileWrite(_username).editFile(commandArray[1], _presentWorkingDirectory);
            break;

            case "read":
            if(commandArray.length < 2)
                IOStreams.printError("Invalid Syntax.");
            else
                new FileRead(_username).readUserFile(_presentWorkingDirectory + commandArray[1]);
            break;

            case "pwd":
            IOStreams.println((_presentWorkingDirectory).replace(_username, _name));
            break;

            case "cd":
            if(commandArray.length < 2)
            IOStreams.printError("Invalid Syntax.");
            else
            changeDirectory(commandArray[1]);
            break;

            case "cd..":
            navPreviousDirectory();
            break;

            case "tree":
            viewDirectoryTree();
            break;

            case "dir":
            case "ls":
            listEntities();
            break;

            case "download":
            if(commandArray.length < 3)
            IOStreams.printError("Invalid Syntax.");
            else
            new FileDownload(_username).downloadFile(commandArray[1], commandArray[2]);
            break;

            case "home":
            resetToHomeDirectory();
            break;

            case "exit":
            case "":
            break;

            default:
            Anvil.anvilInterpreter(commandArray);
        }
    }
}
