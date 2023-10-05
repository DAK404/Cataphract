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

package Cataphract.API;

import java.io.Console;

public class Build
{
    public final static String _Branding = """
                                                      |
                                                     ||
  |||||| ||||||||| |||||||| ||||||||| |||||||  |||  ||| ||||||| |||||||||  |||||| ||||||||
 |||            ||    |||          ||       || |||  |||       ||       || |||        |||
 |||      ||||||||    |||    ||||||||  ||||||  ||||||||  ||||||  |||||||| |||        |||
 |||      |||  |||    |||    |||  |||  |||     |||  |||  ||  ||  |||  ||| |||        |||
  ||||||  |||  |||    |||    |||  |||  |||     |||  |||  ||   || |||  |||  ||||||    |||
                                               ||
                                               |

    """;

    public final static String[] _BuildInfo = {"Cataphract", "0.0.1", "08-August-2023", "20230808-125258_NION", "Development"};

    public static void viewBuildInfo()
    {
        clearScreen();
        IOStreams.println(_Branding);
    }

    public static void clearScreen()
    {
        try
        {
            /*
            * Clear Screen Notes:

            * The program is reliant on clearing the screen based on the OS being run
            * Clear screen has been tested on Windows and Linux platforms only
            * Clear screen should have the IO Flush right after clearing the screen

            */

            if(System.getProperty("os.name").contains("Windows"))

            //Spawns a new process within cmd to clear the screen
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            else
            //invokes bash to clear the screen
            new ProcessBuilder("/bin/bash", "-c" ,"reset").inheritIO().start().waitFor();

            System.out.flush();
        }
        catch(Exception e)
        {
            System.err.println("\n\nERROR WHILE CLEARING SCREEN");
            System.err.println("ERROR: " + e + "\n\n");
        }
    }
}