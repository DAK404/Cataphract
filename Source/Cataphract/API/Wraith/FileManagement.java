package Cataphract.API.Wraith;

import java.io.Console;
import java.io.File;

import Cataphract.API.IOStreams;
import Cataphract.API.Minotaur.Cryptography;

public class FileManagement
{
    private String _username = "";
    private String _name = "";
    private String _defaultPath = "";
    private String _presentWorkingDirectory = "";

    private Console console = System.console();

    public FileManagement(String username)throws Exception
    {
        _username = username;
        _name = new Cataphract.API.Dragon.Login(username).getNameLogic();
        _defaultPath = "./Users/Cataphract/" + _username;
    }

    public final void fileManagerLogic()throws Exception
    {

    }

    public final boolean login()throws Exception
    {
        IOStreams.println("Username: " + _username);
        return new Cataphract.API.Dragon.Login(_username).authenticationLogic(Cataphract.API.Minotaur.Cryptography.stringToSHA3_256(console.readLine("Password: ")), Cataphract.API.Minotaur.Cryptography.stringToSHA3_256(console.readLine("Security Key: ")));
    }

    /*****************************************
     * FILE & DIRECTORY MANAGEMENT UTILITIES *
     *****************************************/
    
    private boolean checkEntityExistence(String fileName)throws Exception
    {
        return new File(fileName).exists();
    }

    private void deleteDirectoryFile(String fileName)throws Exception
    {
        fileName = _defaultPath + _presentWorkingDirectory + fileName;
        File deletionEntity = new File(fileName);

        if (! checkEntityExistence(fileName))
        {
            IOStreams.printError("The specified file or directory does not exist.");
        }
        else
        {
            if(deletionEntity.isDirectory())
                for (File filesInDirectory : deletionEntity.listFiles())
                    deleteDirectoryFile(filesInDirectory.getName());
            deletionEntity.delete();
        }
    }

    private void viewDirectoryTree()throws Exception
    {
        File treeView = new File(_defaultPath + _presentWorkingDirectory);
        IOStreams.println("\n--- [ TREE VIEW ] ---\n");
        viewDirTreeHelper(0, treeView);
        IOStreams.println("");
    }

    private final void viewDirTreeHelper(int indent, File file) {
        System.out.print("|");
        
        for (int i = 0; i < indent; ++i)
        System.out.print('-');
        
        System.out.println(file.getName().replace(_username, _name + " [ USER ROOT DIRECTORY ]"));
        
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            for (File f : files)
            viewDirTreeHelper(indent + 2, f);
        }
    }

    private final void navPreviousDirectory()throws Exception
    {
        int lastSlashIndex = _presentWorkingDirectory.lastIndexOf('/');
        if (lastSlashIndex != -1)
        {
            _presentWorkingDirectory = _presentWorkingDirectory.substring(0, lastSlashIndex);
        }
        else
        {
            IOStreams.printError("Permission Denied.");
            resetToHomeDirectory();
        }
    }

    private final void resetToHomeDirectory()
    {
        _presentWorkingDirectory = "/";
    }

    private final void makeDirectory(String fileName) throws Exception
    {
        new File(_defaultPath + _presentWorkingDirectory + fileName).mkdirs();
    }

    private final void renameEntity(String fileName, String newFileName) throws Exception
    {
        fileName = _defaultPath + _presentWorkingDirectory + fileName;
        newFileName = _defaultPath + _presentWorkingDirectory + newFileName;

        if(checkEntityExistence(newFileName))
            new File(fileName).renameTo(new File(newFileName));
        else
            IOStreams.printError("Specified file or directory does not exist.");
    }

    private final void copyMoveEntity(String fileName, String destination, boolean move)throws Exception
    {
        if(!checkEntityExistence(fileName) && !checkEntityExistence(destination))
            IOStreams.printError("Invalid file name or destination. Permission Denied.");
        
        copyMoveHelper(new File(_defaultPath + _presentWorkingDirectory + fileName, new File(_defaultPath + _presentWorkingDirectory + destination)));
    }

    private final void copyMoveHelper(File source, File destination, boolean move)
    {
        
    }
}
