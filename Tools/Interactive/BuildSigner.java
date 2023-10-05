import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

public class BuildSigner
{

    private static List<String> filePaths = new  ArrayList<String>();
    private static String fileSeparator = System.getProperty("file.separator");

    //////////////////////////////////////////////////////////
    //                                                      //
    // NON EXECUTABLE LOGICS - METHODS WITH NO ENTRY POINTS //
    //                                                      //
    //////////////////////////////////////////////////////////

    public static void main(String[] args)throws Exception
    {
        new BuildSigner().debug();
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
                //filePaths.add(f.getPath());
                {
                    filePaths.add((f.getPath()).replaceAll(fileSeparator, ">"));
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    /**
    * Logic to ignore a few files/directories
    *
    * @param fileName: The file name to be checked
    * @return status: True if the file/directory is to be ignored, false otherwise
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

    private final String fileToMD5(String fileName) throws Exception
    {
        return hashFile(new File(fileName), "MD5");
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

    private void convertFileSeparators()
    {
        //run this method only once.
        for(String tempPath: filePaths)
        {
            
        }
    }

    private void debug()throws Exception
    {
        enumerateFiles(new File("./"));
        for(String fileName: filePaths)
        {
            System.out.println("Signing File: " + fileName);
        }
    }

}