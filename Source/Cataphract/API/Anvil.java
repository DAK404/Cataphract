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

import Cataphract.API.Astaroth.Calendar;
import Cataphract.API.Astaroth.Time;

/**
* A class that provides a set of built in commands for all classes. Also provides a utility to split a string into an array for processing.
*
* @author DAK404 (https://github.com/DAK404)
* @version 2.1.0 (20-February-2024, Cataphract)
* @since 0.0.1 (Truncheon 1.0.1)
*/
public class Anvil
{
    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public Anvil()
    {
    }

    /**
    * Module implementing the interpreter for common commands. Also helps in scripting and reduces code duplication.
    *
    * @param command Command String that the interpreter shall... Interpret
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public static void anvilInterpreter(String commandArray[])throws Exception
    {
        //Logic to check which command was fed into the interpreter. Converted to lowercase for avoiding case sensitivity
        switch(commandArray[0].toLowerCase())
        {
            //Time: Print the date/time in the specified format
            case "time":
            if(commandArray.length < 2)
            IOStreams.println(new Time().getTime());
            else
            IOStreams.println(new Time().getDateTimeUsingSpecifiedFormat(commandArray[1]));
            break;

            case "cal":
            case "calendar":
            int month = 0;
            int year = 0;
            try
            {
                switch(commandArray.length)
                {
                    case 3:
                    year = Integer.parseInt(commandArray[2]);

                    case 2:
                    month = Integer.parseInt(commandArray[1]);
                    break;
                }
                new Calendar().printCalendar(month, year);
            }
            catch(NumberFormatException e)
            {
                IOStreams.printError("Please provide a numeric input for month and year!");
            }
            break;

            //Clear: Clears the screen, calls the viewBuildInfo() to display the build info and clear the rest of the contents
            case "clear":
            if(commandArray[1].equalsIgnoreCase("force"))
            Build.clearScreen();
            else
            Build.viewBuildInfo();
            break;

            //Echo: Prints a string on the display
            case "echo":
            //Display an error message if the entered syntax is incorrect
            if(commandArray.length < 2)
            IOStreams.println("echo <STRING> \n\nOR\n\necho \"<STRING_WITH_SPACES>\"");
            else
            {
                try
                {
                    IOStreams.println(commandArray[1]);
                }
                catch(Exception e)
                {
                    IOStreams.printError("ANVIL : ERROR IN ECHO MODULE!");
                    e.printStackTrace();
                }
            }
            break;


            //Wait: Waits for the specified value (milliseconds) for the shell to wait for a second
            case "wait":
            try
            {
                //Display an error message if the entered syntax is incorrect
                if(Integer.parseInt(commandArray[1]) < 1)
                IOStreams.println("This will make the prompt wait for a given number of milliseconds.\nSyntax: wait 1000\n\nPrompt shall wait for 1 second.");
                else
                Thread.sleep(Integer.parseInt(commandArray[1]));
            }
            catch(NumberFormatException e)
            {
                IOStreams.printError("Please provide a numeric input for the wait timer!");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            break;

            //Confirm: requests the user to press RETURN to continue with the execution
            case "confirm":
            //Check for arguments so that if there are prefixes and suffixes, call the method with the arguments to have a custom string instead of the default.
            IOStreams.confirmReturnToContinue();
            break;

            //If no command is found, return false. Usually, Sycorax Kernel shall display an error if no commands in both lists are found.
            default:
            IOStreams.printError(commandArray[0] + " - Command Not Found");
            break;
        }
    }

    /**
    * Method to split an input string to individual words by at the occurrence of a blank space.
    *
    * @param command String that will need to be split into an array.
    * @return String[] The string split into an array.
    */
    public static String[] splitStringToArray(String command)
    {
        //Regex to split the string at the occurrence of a blank space
        String[] arr = command.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        //Fix to remove the quotes, make the logic to split the input at every space only.
        //Check the EasyGuide Documentation on why this is implemented the way it is.
        for(int i = 0; i < arr.length; i++)
        if(arr[i].startsWith("\"") && arr[i].endsWith("\""))
        arr[i] = arr[i].substring(1, arr[i].length()-1);

        //return the array of words split.
        return arr;
    }
}