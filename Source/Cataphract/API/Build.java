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

/**
* A class to display the build information and clear the screen.
*
* @author DAK404 (https://github.com/DAK404)
* @version 1.3.0 (11-October-2023, Cataphract)
* @since 0.0.1 (Zen Quantum 1.0)
*/
public class Build
{
    /** String that holds the branding of the Kernel. */
    public final static String _Branding = """
                                                      |
                                                     ||
  |||||| ||||||||| |||||||| ||||||||| |||||||  |||  ||| ||||||| |||||||||  |||||| |||||||||
 |||            ||    |||          ||       || |||  |||       ||       || |||        |||
 |||      ||||||||    |||    ||||||||  ||||||  ||||||||  ||||||  |||||||| |||        |||
 |||      |||  |||    |||    |||  |||  |||     |||  |||  ||  ||  |||  ||| |||        |||
  ||||||  |||  |||    |||    |||  |||  |||     |||  |||  ||   || |||  |||  ||||||    |||
                                               ||
                                               |

    """;

    /** An array that holds the values for Kernel Name, version, build date, build ID and the kernel branch/build type. */
    public final static String[] _BuildInfo = {"Cataphract", "1.3.0", "14-August-2024", "20240814-003624_NION", "Development"};

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public Build()
    {
    }

    /**
     * Logic to display the build information
     */
    public static void viewBuildInfo()
    {
        //clear the screen before printing the branding.
        clearScreen();
        //print the branding string.
        IOStreams.println(_Branding + "\nVersion " + _BuildInfo[1]);

        debug();
    }

    /**
     * Logic to clear the terminal screen.
     */
    public static void clearScreen()
    {
        //Added a try-catch block for better error handling mechanism.
        try
        {
            /*
            * Clear Screen Notes:
            *
            * The program is reliant on clearing the screen based on the OS being run
            * Clear screen has been tested on Windows and Linux platforms only
            * Clear screen should have the IO Flush right after clearing the screen
            *
            */

            //Checks if the specified OS is Windows
            if(System.getProperty("os.name").contains("Windows"))
                //Spawns a new process within cmd to clear the screen
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            //else assume that the OS is Linux/Unix-like and clear the screen thru bash shell
            else
                //invokes bash to clear the screen
                new ProcessBuilder("/bin/bash", "-c" ,"reset").inheritIO().start().waitFor();

            //Flush the stream
            System.out.flush();
        }
        //Catch any exceptions when clearing the screen
        catch(Exception e)
        {
            System.err.println("\n\nERROR WHILE CLEARING SCREEN");
            //print the error for debugging
            System.err.println("ERROR: " + e + "\n\n");
        }
    }

    private static void debug()
    {
        //int mb = 1024 * 1024;
        // get Runtime instance

        System.out.println("! DEBUG INFORMATION SPEW START !");
        
        Runtime instance = Runtime.getRuntime();
        
        long memoryUsed = instance.totalMemory() - instance.freeMemory();
        
        System.out.println("\n000000000000000000000000000000");
        System.out.println("! DEBUG - MEMORY INFORMATION !");
        System.out.println("000000000000000000000000000000");
        System.out.println("> Process ID   : " + ProcessHandle.current().pid());
        System.out.println("> Total Memory : " + instance.totalMemory() + " Bytes");
        System.out.println("> Free Memory  : " + instance.freeMemory() + " Bytes");
        System.out.println("> Used Memory  : " + memoryUsed + " Bytes");
        System.out.println("000000000000000000000000000000\n");

        System.out.println("!  DEBUG INFORMATION SPEW END  !");
    }
}
