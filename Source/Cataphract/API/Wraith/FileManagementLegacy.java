package Cataphract.API.Wraith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Console;

import Cataphract.API.Build;
import Cataphract.API.IOStreams;
import Cataphract.API.Minotaur.Cryptography;

public class FileManagement
{
    private String _username = "";
    private String _accountName = "";
    
    private String _defaultPath = "";
    private String _presentWorkingDir = "/";
    
    private Console console = System.console();
    
    public FileManagement(String username)throws Exception
    {
        _username = username;
        _defaultPath = "./Users/Cataphract/" + username + "/";
    }
    
    public void FileManagementLogic()throws Exception
    {
        if(!authenticateCurrentUser())
        {
            IOStreams.printError("Invalid Credentials.");
        }
        else
        {
            IOStreams.println("Grinch File Management System 1.0");
            Build.viewBuildInfo();
            String inputValue = "";
            do
            {
                inputValue = console.readLine(_accountName + "@" + _presentWorkingDir.replace(_username, _accountName) + "&> ");
                
            }
            while(! inputValue.equalsIgnoreCase("exit"));
        }
    }
    
    /**
    * Logic to authenticate the current user logged in.
    *
    * @return {@code true} if the user creation was successful, {@code false} otherwise.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    private final boolean authenticateCurrentUser()throws Exception
    {
        //Clear the screen, view the build information
        Build.viewBuildInfo();
        
        //Common variable used to store the status of the authentication
        boolean authenticationStatus = false;
        
        try
        {
            //Display the name of the user currently logged in
            IOStreams.println("Username: " + new Cataphract.API.Dragon.Login(_username).getNameLogic());
            
            new Cataphract.API.Minotaur.Cryptography();
            //challenge the database for the provided credentials, and store the status
            authenticationStatus = new Cataphract.API.Dragon.Login(_username).authenticationLogic(Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), Cryptography.stringToSHA3_256(String.valueOf(console.readPassword("SecurityKey: "))));
        }
        catch(Exception e)
        {
            //Handle any exceptions encountered
            new Cataphract.API.ExceptionHandler().handleException(e);
        }
        
        //return the status value
        return authenticationStatus;
    }
    
    private boolean checkFileExistence(String fileName)throws Exception
    {
        return new File(fileName).exists();
    }
    
    private final void del(String delFile) throws Exception {
        try {
            delFile = _defaultPath + _presentWorkingDir + delFile;
            if (checkFileExistence(delFile)) {
                File f = new File(delFile);
                if (f.isDirectory()) {
                    delHelper(f);
                } else {
                    f.delete();
                }
            } else {
                System.out.println("[ ERROR ] : The specified file/directory does not exist.");
            }
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
    * @param delfile
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void delHelper(File delfile) throws Exception {
        if (delfile.isDirectory()) {
            for (File fock : delfile.listFiles()) {
                delHelper(fock);
            }
        }
        delfile.delete();
    }
    
    private void viewDirTree() throws Exception {
        try {
            File tree = new File(_defaultPath + _presentWorkingDir);
            IOStreams.println("\n--- [ TREE VIEW ] ---\n");
            viewDirTreeHelper(0, tree);
            IOStreams.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private final void viewDirTreeHelper(int indent, File file) {
        System.out.print("|");
        
        for (int i = 0; i < indent; ++i)
        System.out.print('-');
        
        System.out.println(file.getName().replace(_username, _accountName + " [ USER ROOT DIRECTORY ]"));
        
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files)
            viewDirTreeHelper(indent + 2, f);
        }
    }
    
    private void navToPreviousDir() throws Exception 
    {
        int lastSlashIndex = _presentWorkingDir.lastIndexOf('/');
        if (lastSlashIndex != -1) {
            _presentWorkingDir = _presentWorkingDir.substring(0, lastSlashIndex);
        } else {
            IOStreams.printError("Illegal Operation. Permission Denied.");
            resetToHomeDir();
        }
    }
    
    
    private void resetToHomeDir()throws Exception
    {
        _presentWorkingDir = "/";
    }
    
    private void makeDir(String fileName)throws Exception
    {
        new File(_defaultPath + _presentWorkingDir + fileName).mkdirs();
    }
    
    private final void rename(String oldFileName, String newFileName)throws Exception
    {
        try
        {
            oldFileName = _defaultPath + _presentWorkingDir + oldFileName;
            newFileName = _defaultPath + _presentWorkingDir + newFileName;
            
            if(checkFileExistence(oldFileName))
            new File(oldFileName).renameTo(new File(newFileName));
            else
            System.out.println("[ ERROR ] : The specified file/directory does not exist.");
            System.gc();
        }
        catch (Exception E)
        {
            //troubleshooting tips here
            E.printStackTrace();
        }
    }
    
    private final void copyMove(boolean move, String source, String destination) throws Exception {
        try {
            if (!checkFileExistence(_defaultPath + _presentWorkingDir + source)) {
                System.out.println("The Source File Does Not Exist");
                return;
            }
            if (!checkFileExistence(_defaultPath + _presentWorkingDir + destination)) {
                System.out.println("The Destination File Does Not Exist");
                return;
            }
            copyMoveHelper(new File(_defaultPath + _presentWorkingDir + source), new File(_defaultPath + _presentWorkingDir + destination), move);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
    * @param src
    * @param dest
    * @param move
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void copyMoveHelper(File src, File dest, boolean move) throws Exception {
        try {
            if (src.isDirectory()) {
                dest.mkdirs();
                for (File sourceChild : src.listFiles()) {
                    File destChild = new File(dest, sourceChild.getName());
                    copyMoveHelper(sourceChild, destChild, move);
                }
            } else {
                
                //------------------------------------
                // USE File.copy method for efficiency
                //------------------------------------
                
                
                
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                if (move) {
                    delHelper(src);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void grinchInterpreter(String command)throws Exception
    {
        try
        {
            String[] commandArray = Cataphract.API.Anvil.splitStringToArray(command);
            switch(commandArray[0].toLowerCase())
            {
                case "execute":
                break;
                
                case "cut":
                break;
                
                case "copy":
                break;
                
                case "delete":
                break;
                
                case "rename":
                break;
                
                case "mkdir":
                if(commandArray.length < 2)
                IOStreams.printError("Invalid Syntax for command \'mkdir\'.");
                else
                makeDir(commandArray[1]);
                break;
                
                case "edit":
                break;
                
                case "open":
                break;
                
                case "home":
                resetToHomeDir();
                break;
                
                case "pwd":
                IOStreams.println(_defaultPath);
                IOStreams.println(_defaultPath + _presentWorkingDir);
                break;
                
                case "cd":
                if(commandArray.length < 2)
                IOStreams.printError("Invalid Syntax for command \'cd\'.");
                else
                changeDirectory(commandArray[1]);
                break;
                
                case "tree":
                viewDirTree();
                break;
                
                case "dir":
                case "ls":
                //listEntitiesInDirectory();
                break;
                
                case "download":
                break;
                
                //Override exit to quit the module than to quit the program
                case "exit":
                case "":
                break;
                
                //Use the Anvil functions if none of the cases are followed
                default:
                //Cataphract.API.Anvil.anvilInterpreter(commandArray[0]);
                break;
            }
        }
        catch(Exception E)
        {
            
        }
    }
}