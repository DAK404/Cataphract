package Cataphract.API.Wraith;

//Import the required Java IO classes
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;

//Import the required Java Util classes
import java.util.Date;

//Import the required Java Text Formatting classes
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import Cataphract.API.IOStreams;

/**
 * 
 */
public class FileWrite
{
    //private Console console = System.console();

    /**
     * 
     * 
     * @param fileName
     * @param dir
     */
    public final void editFile(String fileName, String dir)
    {
        try
        {
            if(checkFileValidity(fileName))
            {
                boolean appendFile = true;
                String message = "";

                System.out.println("Wraith Text Editor 1.5");
                System.out.println("______________________\n");

                Console console=System.console();

                File writeToFile = new File(dir + fileName);
                System.out.println("\nEditing File : " + fileName + "\n\n");

                if(writeToFile.exists())
                {
                    switch(console.readLine("[ ATTENTION ] : A file with the same name has been found in this directory. Do you want to OVERWRITE it, APPEND to the file, or GO BACK? \n\nOptions:\n[ OVERWRITE | APPEND | RETURN | HELP ]\n\n> ").toLowerCase())
                    {
                        case "overwrite":

                        appendFile = false;
                        System.out.println("The new content will overwrite the previous content present in the file!");
                        break;

                        case "append":

                        System.out.println("The new content will be added to the end of the file! Previous data will remain unchanged.");
                        break;

                        case "return":

                        return;

                        case "help":

                        System.out.println("Work in Progress");
                        break;

                        default:

                        System.out.println("Invalid choice. Exiting...");
                        return;
                    }
                }

                BufferedWriter obj = new BufferedWriter(new FileWriter(writeToFile, appendFile));
                PrintWriter pr = new PrintWriter(obj);

                do
                {
                    pr.println(message);
                    message = console.readLine();
                }
                while( !(message.equals("<exit>")) );

                pr.close();
                obj.close();
                System.gc();
            }
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Cataphract.API.ExceptionHandler().handleException(E);
        }
    }

    /**
     * 
     * 
     * @param printToFile
     * @param fileName
     */
    public static final void logger(String printToFile, String fileName)
    {
        try
        {
            String logfilePath = "./System/Cataphract/Public/Logs/";
            if(checkFileValidity(fileName))
            {
                DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
                Date date = new Date();

                logfilePath = (new File(logfilePath).exists()?logfilePath:"./Logs/Cataphract/");

                File logFile = new File(logfilePath);
                if(! logFile.exists())
                    logFile.mkdirs();

                BufferedWriter obj = new BufferedWriter(new FileWriter(logfilePath + fileName + ".log", true));
                PrintWriter pr = new PrintWriter(obj);
                pr.println(dateFormat.format(date) + ": " + printToFile);
                pr.close();
                obj.close();
            }
            else
                IOStreams.printError("The provided file name is invalid. Please provide a valid file name to continue.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
    }

    /**
     * 
     * 
     * @param fileName
     * @return
     * @throws Exception
     */
    private static final boolean checkFileValidity(String fileName)throws Exception
    {
        return !(fileName == null || fileName.equals("") || fileName.startsWith(" "));
    }
}