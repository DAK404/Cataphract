import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;


/**
* An Application to sign the build files, to ensure the program integrity
*
* @author: DAK
* @version: 1.0
*/
public class BuildSigner
{
    private static List<String> filePaths = new  ArrayList<String>();
    private static String fileSeparator = System.getProperty("file.separator");
    
    /**
    * Logic to sign the build
    *
    * @param Args arguments passed during invokation 
    */
    public static void main(String[] Args)
    {
        try
        {
            new File("./.Manifest/Cataphract").mkdirs();
            new BuildSigner().enumerateFiles(new File("./"));
            new BuildSigner().hashFiles();
            new BuildSigner().kernelFileList();
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }
    
    /**
    * Enumerate all the files and subdirectory in the specified directory.
    *
    * @param directory: The directory to be traversed through to enumerate contents
    */
    private void enumerateFiles(File directory)
    {
        try
        {
            //Initialize an array to hold the contents of the directory
            File[] filesList = directory.listFiles();
            
            //Begin the directory traversal
            for (File f: filesList)
            {
                //Check if the file/folder is specified in the ignore list
                if(ignoreFiles(f.getName()))
                //If true, begin traversing the next entry
                continue;
                
                //Check if the entry is a directory
                if (f.isDirectory())
                //If true, recurse the method to traverse through the subdirectory
                enumerateFiles(f);
                
                //Check if the entry is a file
                else if (f.isFile())
                //If true, add the file to the list of files to be signed
                filePaths.add(f.getPath());
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }
    
    private void kernelFileList()throws Exception
    {
        Properties props = new Properties();
        FileOutputStream output = new FileOutputStream("./.Manifest/Cataphract/KernelFiles.m2");
        
        for(String fileName: filePaths)
        {
            if(fileName.endsWith(".class"))
            {
                String temp =  fileName.replaceAll(Matcher.quoteReplacement(fileSeparator), "|");
                props.setProperty(temp, String.valueOf(new File(fileName).length()));
                System.out.println("Adding File: " + temp);
            }
        }
        props.storeToXML(output, "FileSizes");
        output.close();
        System.gc();
    }
    
    /**
    * Logic to ignore a few files/directories
    *
    * @param fileName The file name to be checked
    * @return status True if the file/directory is to be ignored, false otherwise
    */
    private boolean ignoreFiles(String fileName)
    {
        //Set the default value to be false
        boolean status = false;
        
        //Initialize the list of the directories/files to be ignored
        String[] ignoreList = {".Manifest", "System", "Users", "org", "JRE", "BootShell.cmd", "BuildSigner.java", "Logs"};
        
        //Check the fileName against the ignore list
        for(String files : ignoreList)
        {
            if(fileName.equalsIgnoreCase(files))
            {
                status = true;
                break;
            }
        }
        return status;
    }
    
    private void hashFiles()
    {
        try
        {
            Properties props = new Properties();
            FileOutputStream output = new FileOutputStream("./.Manifest/Cataphract/KernelFilesHashes.m1");
            //System.out.println(filePaths);
            
            for(String fileName: filePaths)
            {
                String temp =  fileName.replaceAll(Matcher.quoteReplacement(fileSeparator), "|");
                props.setProperty(temp, fileToSHA3_256(fileName));
                System.out.println("Signing File: " + temp);
            }
            
            props.storeToXML(output, "FileManifest");
            output.close();
            System.gc();
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }
    
    private final String fileToSHA3_256(String fileName) throws Exception
    {
        return hashFile(new File(fileName), "SHA3-256");
    }
    
    private final String convertByteArrayToHexString(byte[] arrayBytes)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++)
        stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        return stringBuffer.toString();
    }
    
    private final String hashFile(File file, String algorithm)throws Exception
    {
        String result = null;
        if(file.exists())
        {
            try (FileInputStream inputStream = new FileInputStream(file))
            {
                MessageDigest digest = MessageDigest.getInstance(algorithm);
                
                byte[] bytesBuffer = new byte[1024];
                int bytesRead = -1;
                
                while ((bytesRead = inputStream.read(bytesBuffer)) != -1)
                {
                    digest.update(bytesBuffer, 0, bytesRead);
                }
                
                byte[] hashedBytes = digest.digest();
                
                result =  convertByteArrayToHexString(hashedBytes);
            }
            catch (NoSuchAlgorithmException E)
            {
                System.out.println("Unsupported Algorithm.\n\n");
                E.printStackTrace();
            }
        }
        return result;
    }
}