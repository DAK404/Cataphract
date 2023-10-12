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

package Cataphract.Core;

//Import the required Java IO classes
import java.io.File;
import java.io.Console;
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import Cataphract.API.IOStreams;
import Cataphract.API.Build;

public class Loader
{
    /**
     * A list of kernel file paths used by Abraxis subsystem to verify kernel integrity
     */
    private static List<String> abraxisFilePathList = new  ArrayList<String>();

    private Console console = System.console();

    public static void main(String[] args)throws Exception
    {
        switch(args[0])
        {
            case "probe":
            System.exit(7);
            break;

            case "normal":
            IOStreams.println("Starting Kernel...");
            break;

            default:
            IOStreams.printError("Invalid Boot Mode. Aborting...\n");
            System.exit(3);
        }

        Build.viewBuildInfo();
        new Loader().loaderLogic();
    }

    private void loaderLogic()
    {
        System.out.println("Abraxis result: " + abraxisLogic());
    }

    private byte abraxisLogic()
    {
        byte abraxisResult = 55;
        
        IOStreams.printInfo("Stage 0: Checking Manifest Files...");
        
        //check if the Manifest files exist
        if(manifestFilesCheck())
        {
            IOStreams.printInfo("Stage 1: Manifest Files Found. Populating Kernel Files and Directories...");
            
            if(populateKernelFiles(new File("./")))
            {
                IOStreams.printInfo("Stage 2: Kernel Files and Directories populated. Checking File Integrity - Phase 1...");

                if(checkFileHashes())
                {
                    IOStreams.printInfo("Stage 3: File Integrity Check - Phase 1 Complete. Checking File Integrity - Phase 2...");
                    
                    if(checkFileSizes())
                    {
                        abraxisResult = 0;
                        IOStreams.printInfo("Stage 4: File Integrity Check - Phase 2 Complete. Checking System and User Files...");
                        if(setupStatusCheck())
                            IOStreams.printInfo("File Check Complete. Booting Cataphract...");
                        else
                        {
                            abraxisResult = 5;
                            IOStreams.printAttention("Setting up Cataphract...");
                        }
                    }
                    else
                        abraxisResult = 4;
                }
                else
                    abraxisResult = 3;
            }
            else
                abraxisResult = 2;
        }
        else
            abraxisResult = 1;

        return abraxisResult;

    }

    private boolean manifestFilesCheck()
    {
        return new File("./.Manifest/Cataphract/KernelFilesHashes.m1").exists() & new File("./.Manifest/Cataphract/KernelFiles.m2").exists();
    }

    private boolean populateKernelFiles(File fileDirectory)
    {
        boolean status = false;
        
        try
        {
            File[] fileList = fileDirectory.listFiles();

            for(File fileName: fileList)
            {
                if(fileIgnoreList(fileName.getName()))
                    continue;
                
                if(fileName.isDirectory())
                    populateKernelFiles(fileName);
                else
                    abraxisFilePathList.add(fileName.getPath());                
            }
            status = true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }

    private boolean fileIgnoreList(String fileName)
    {
        boolean status = false;

        String[] ignoreList = {".Manifest", "System", "Users", "org", "JRE", "Logs"};

        for(String files : ignoreList)
        {
            if(fileName.equalsIgnoreCase(files))
            {
                status = true;
                break;
            }
        }
        System.gc();
        return status;
    }

    private boolean checkFileHashes()
    {
        boolean kernelIntegrity = true;

        try
        {
            Properties manifestM1Entries = new Properties();
            FileInputStream m1FileStream = new FileInputStream("./.Manifest/Cataphract/KernelFilesHashes.m1");

            manifestM1Entries.loadFromXML(m1FileStream);
            m1FileStream.close();

            /*
             * DEBUG: List the properties loaded from the M1 Manifest file
             * manifestM1Entries.list(System.out);
             */

             Cataphract.API.Minotaur.Cryptography fileHash = new Cataphract.API.Minotaur.Cryptography();

             for(String fileName: abraxisFilePathList)
             {
                if(fileIgnoreList(fileName))
                    continue;
                
                String kernelFileHash = fileHash.fileToSHA3_256(fileName);

                try
                {
                    String manifestHash = manifestM1Entries.getProperty(convertFileSeparator(fileName), kernelFileHash);

                    if(!manifestHash.equals(kernelFileHash))
                    {
                        kernelIntegrity = false;
                        IOStreams.printError("Integrity Check Failure at " + fileHash + "\t" + fileName + "\n");
                    }
                }
                catch(Exception e)
                {
                    IOStreams.printWarning("Unknown File at " + fileHash + "\t" + fileName + "\n");
                }
             }
        }
        catch(Exception e)
        {
            IOStreams.println("Manifest Parse Error! Unable to continue.");
            kernelIntegrity = false;
        }

        return kernelIntegrity;
    }

    private boolean checkFileSizes()
    {
        int fileCount = 0;
        boolean status = true;

        try
        {
            Properties manifestM2Entries = new Properties();
            FileInputStream m2FileStream = new FileInputStream("./.Manifest/Cataphract/KernelFiles.m2");

            manifestM2Entries.loadFromXML(m2FileStream);
            m2FileStream.close();

            /*
             * DEBUG: List the properties loaded from the M1 Manifest file
             * manifestM1Entries.list(System.out);
             */

            for(String fileName: abraxisFilePathList)
            {
                if(fileName.endsWith(".class"))
                {
                    long fileSizeM2 = Long.parseLong(String.valueOf(manifestM2Entries.get(convertToNionSeparator(fileName))));
                    long fileSize = new File(fileName).length();
                    if(fileSize != fileSizeM2)
                    {
                        status = false;
                        IOStreams.printError("Integrity Check Failure at " + fileName + "\t" + fileSize + ". Expected " + fileSizeM2);
                    }
                    fileCount++;
                }
            }

            if(fileCount < manifestM2Entries.size())
            {
                IOStreams.printError("Integrity Check Failure. Expected " + manifestM2Entries.size() + ". Found " + fileCount);
                status = false;
            }
        }
        catch(Exception e)
        {
            IOStreams.printError("File Operations Failure! Unable to continue.");
            e.printStackTrace();
        }

        return status;
    }

    private boolean setupStatusCheck()
    {
        return new File("./System/Cataphract").exists() & new File("./Users/Cataphract").exists();
    }

    private String convertFileSeparator(String nionPath)
    {
        return nionPath.replaceAll("|", Matcher.quoteReplacement(File.separator));
    }

    private String convertToNionSeparator(String filePath)
    {
        return filePath.replaceAll(Matcher.quoteReplacement(File.separator), "|");
    }

    private void guestShell()
    {
        
    }
}