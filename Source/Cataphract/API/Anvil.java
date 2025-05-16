/*
*                                                      |
*                                                     ||
*  |||||| ||||||||| |||||||| ||||||||| |||||||  |||  ||| ||||||| |||||||||  |||||| |||||||||
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

/*
* This file is part of the Cataphract project.
* Copyright (C) 2024 DAK404 (https://github.com/DAK404)
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

package Cataphract.API;

import Cataphract.API.Astaroth.Calendar;
import Cataphract.API.Astaroth.Time;
import Cataphract.API.Wraith.FileRead;

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
                    new Calendar().printCalendar(month, year);
                    break;

                    default:
                    break;
                }
            }
            catch(NumberFormatException e)
            {
                IOStreams.printError("Please provide a numeric input for month and year!");
            }
            break;

            //Clear: Clears the screen, calls the viewBuildInfo() to display the build info and clear the rest of the contents
            case "clear":
            if(commandArray.length < 2)
                Build.viewBuildInfo();
            else
                if(commandArray[1].equalsIgnoreCase("force"))
                    Build.clearScreen();
            break;

            //Echo: Prints a string on the display
            case "echo":
            //Display an error message if the entered syntax is incorrect
            if(commandArray.length < 2)
            {
                IOStreams.printError("Invalid Syntax.");
            IOStreams.printInfo("Expected Syntax: echo <String> OR echo \"<String With Spaces>\"");
            }
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

            case "help":
            if(commandArray.length < 2)
            {
                new FileRead().readHelpFile("API|Anvil.help");
            }
            else
                new FileRead().readHelpFile(commandArray[1]);
            break;


            //Wait: Waits for the specified value (milliseconds) for the shell to wait for a second
            case "wait":
            try
            {
                //Display an error message if the entered syntax is incorrect
                if(commandArray.length < 2 || Integer.parseInt(commandArray[1]) < 1)
                {
                    IOStreams.printError("Invalid Syntax.");
                    IOStreams.printInfo("Expected Syntax: wait <milliseconds> (Integer > 0)");
                }

                else
                Thread.sleep(Integer.parseInt(commandArray[1]));
            }
            catch(NumberFormatException e)
            {
                IOStreams.printError("Invalid Argument!\nExpected Argument: milliseconds (Integer)");
                IOStreams.printInfo("Expected Syntax: wait <milliseconds> (Integer)");
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
}