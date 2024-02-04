package Cataphract.API;

//Import the required Java IO classes
import java.io.Console;
import java.io.PrintWriter;
import java.io.StringWriter;

import Cataphract.API.Wraith.FileWrite;

public class ExceptionHandler
{
    Console console = System.console();
    public void handleException(Exception e)
    {
        String errorLogFileName = "ExceptionLog";
        try
        {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            String stackTrace = sw.toString();

            String exceptionStackTrace = """

            ***************************************
            !         PROGRAM STACK TRACE         !
            ***************************************

            """ + stackTrace + """

            ***************************************
            !           STACK TRACE END           !
            ***************************************
            """;

            System.err.println("\n[ FATAL ERROR ] AN EXCEPTION OCCURRED DURING THE EXECUTION OF THE PROGRAM.");
            System.err.println("\n[ --- TECHNICAL DETAILS --- ]\n");
            System.err.println("Class: " + e.getClass().getName());
            System.err.println("Trace Details: " + e.getStackTrace());
            System.err.println(exceptionStackTrace);
            System.err.println("[ END OF TECHNICAL DETAILS ]\n");

            System.err.println("This information will be written into a log file which can be used to debug the cause of the failure.\nAny additional information can be useful to find the root cause of the issue efficiently.");

            //write the user comments into the log file.
            FileWrite.logger("[--- TECHNICAL DETAILS ---]", errorLogFileName);
            FileWrite.logger(e.getClass().getName(), errorLogFileName);
            FileWrite.logger(e.getStackTrace().toString(), errorLogFileName);
            FileWrite.logger(exceptionStackTrace, errorLogFileName);
            FileWrite.logger("User Comment> " + console.readLine("User Comment> ") + "\n\n", errorLogFileName);

        }
        catch(Exception ex)
        {
            e.printStackTrace();
        }
        System.exit((console.readLine("Do you want to restart the program? [ Y | N ]> ").equalsIgnoreCase("y")?5:4));
    }
}