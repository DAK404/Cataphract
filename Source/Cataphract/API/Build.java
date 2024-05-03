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
  |||||| ||||||||| |||||||| ||||||||| |||||||  |||  ||| ||||||| |||||||||  |||||| ||||||||
 |||            ||    |||          ||       || |||  |||       ||       || |||        |||
 |||      ||||||||    |||    ||||||||  ||||||  ||||||||  ||||||  |||||||| |||        |||
 |||      |||  |||    |||    |||  |||  |||     |||  |||  ||  ||  |||  ||| |||        |||
  ||||||  |||  |||    |||    |||  |||  |||     |||  |||  ||   || |||  |||  ||||||    |||
                                               ||
                                               |

    """;

    /** An array that holds the values for Kernel Name, version, build date, build ID and the kernel branch/build type. */
    public final static String[] _BuildInfo = {"Cataphract", "1.0.0", "27-April-2024", "20240427-182445_NION", "Development"};

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
        IOStreams.println(_Branding + "\n");
        //new Build().debug();
        IOStreams.printWarning("Features disabled until integration tests are complete.\n\nIntegration test ongoing.\nCore features are almost complete.\n");
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

    private void debug()
    {
        //int mb = 1024 * 1024;
        // get Runtime instance
        Runtime instance = Runtime.getRuntime();
        System.out.println("\n*********************************************");
        System.out.println("        ---   DEBUG INFORMATION   ---        ");
        System.out.println("*********************************************");
        System.out.println("\n   - Heap utilization statistics [Bytes] -  \n");
        System.out.println("      [*]  Process ID   : "+ProcessHandle.current().pid());
        // available memory
        System.out.println("      [*]  Total Memory : " + instance.totalMemory() + " Bytes");
        // free memory
        System.out.println("      [*]  Free Memory  : " + instance.freeMemory() + " Bytes");
        // used memory
        System.out.println("      [*]  Used Memory  : " + (instance.totalMemory() - instance.freeMemory()) + " Bytes");
        // Maximum available memory
        System.out.println("      [*]  Max Memory   : " + instance.maxMemory() + " Bytes");
        System.out.println("\n*********************************************\n\n");
    }
}