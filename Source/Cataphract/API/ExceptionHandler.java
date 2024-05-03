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

package Cataphract.API;

import java.io.Console;
import java.io.PrintWriter;
import java.io.StringWriter;

import Cataphract.API.Wraith.FileWrite;

/**
 * A class to handle exceptions and log error details.
 * 
 * @author DAK404 (https://github.com/DAK404)
 * @version 5.6.6 (20-February-2024, Cataphract)
 * @since 0.0.1 (Mosaic 0.0.1)
 */
public class ExceptionHandler
{
    /** Console instance for user input/output. */
    Console console = System.console();

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public ExceptionHandler()
    {
    }

    /**
     * Method to handle exceptions.
     * 
     * @param e The exception to handle.
     */
    public void handleException(Exception e)
    {
        // Define error log file name
        String errorLogFileName = "ExceptionLog";
        try
        {
            // Create a StringWriter to capture stack trace
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            // Retrieve stack trace as a string
            String stackTrace = sw.toString();

            // Format exception stack trace
            String exceptionStackTrace = """
            ***************************************
            !         PROGRAM STACK TRACE         !
            ***************************************

            """ + stackTrace + """

            ***************************************
            !           STACK TRACE END           !
            ***************************************
            """;

            // Print technical details of the exception
            System.err.println("\n[ FATAL ERROR ] AN EXCEPTION OCCURRED DURING THE EXECUTION OF THE PROGRAM.");
            System.err.println("\n[ --- TECHNICAL DETAILS --- ]\n");
            System.err.println("Class: " + e.getClass().getName());
            System.err.println("Trace Details: " + e.getStackTrace());
            System.err.println(exceptionStackTrace);
            System.err.println("[ END OF TECHNICAL DETAILS ]\n");

            // Prompt user for additional comments
            System.err.println("This information will be written into a log file which can be used to debug the cause of the failure.\nAny additional information can be useful to find the root cause of the issue efficiently.");

            // Write technical details and user comments into the log file
            FileWrite.logger("[--- TECHNICAL DETAILS ---]", errorLogFileName);
            FileWrite.logger(e.getClass().getName(), errorLogFileName);
            FileWrite.logger(e.getStackTrace().toString(), errorLogFileName);
            FileWrite.logger(exceptionStackTrace, errorLogFileName);
            FileWrite.logger("User Comment> " + console.readLine("User Comment> ") + "\n\n", errorLogFileName);
        }
        catch(Exception ex)
        {
            // Print stack trace if an error occurs during exception handling
            e.printStackTrace();
        }
        // Exit program with code 5 if user wants to restart, otherwise exit with code 4
        System.exit((console.readLine("Do you want to restart the program? [ Y | N ]> ").equalsIgnoreCase("y") ? 5 : 4));
    }
}
