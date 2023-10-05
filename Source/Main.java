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

import java.io.File;

public class Main
{

    /************************************************************
     *             MESSAGE HEADER AND FOOTER SECTION
     ************************************************************/

    /**
     * A header used for messages, displayed right before the message.
     */
    private static final String messageHeader = """
    *********************************
           NION PROGRAM LOADER
    *********************************
    Version : 7.247

    """;

    /**
     * A footer used for messages, displayed right after the message.
     */
    private static final String messageFooter = """
    *********************************
    """;

    /************************************************************
     *           END MESSAGE HEADER AND FOOTER SECTION
     ************************************************************/


    /************************************************************
     *                  MESSAGE STRINGS SECTION
     ************************************************************/

    /**
     * A quick help for the launcher, if the user does not know how to boot a kernel.
     */
    private static final String launcherHelp = """
    This program helps in launching Sycorax Kernel, and
    any other kernel that follows the Nion Directory Specification.

    Detailed documentation can be found on: https://dak404.github.io/Cataphract/Docs

    Quick Help:

    * Command to boot the kernel:
        java <arguments> Main <Kernel_Name> <Boot_Mode>

    * Command to probe a kernel:
        java <arguments> Main <Kernel_Name> probe
    """;

    private static final String KERNEL_FOUND = """
    [ INFORMATION ] : KERNEL FOUND

    This kernel can be booted by the launcher program!
    Please do note that there is no way to enforce a kernel to utilize the Nion Kernel Specification.
    If you are using a 3rd party kernel, please be cautious and unexpected behavior.
    """;

    /**
     * A message displayed when incorrect number of arguments are provided to the program.
     */
    private static final String INVALID_SYNTAX = """
    [ ERROR ] : INVALID LAUNCHER SYNTAX.

    Description: This Launcher Program conforms to Nion Kernel Specifications.
    This means that the kernels to be compatible with this launcher will need to:

    1. Have a public class called Loader within the Kernel Package
    2. Have a main method with argument type String[]
    3. Have a public method called 'probe' with return type boolean

    The kernels require a set of parameters to be started. Without which,
    the kernels shall not boot.

    Please provide the required parameters for the kernel to boot.
    """;

    private static final String KERNEL_NOT_FOUND = """
    [ ERROR ] : KERNEL NOT FOUND.

    Description: The specified kernel in the arguments used is not found.
    Please try to probe the Kernel to check if the Kernel exists and can be
    booted into.
    """;

    private static final String UNDEFINED_BOOTMODE = """
    [ ERROR ] : UNDEFINED BOOT MODE
    
    The boot mode provided is currently not supported by the Kernel.
    Please update the kernel and try again. If it does not work, please have a look at
    all the supported boot modes for the specified Kernel.
    """;

    private static final String FATAL_ERROR_EXIT = """
    [ CRITICAL ] : FATAL ERROR EXIT

    The Kernel exited fatally. All unsaved work will be lost.

    The cause may be due to an unhandled exception or a module logic that exited the
    program due to an exception or error. You must manually boot the kernel again to
    continue to use the program.
    """;

    private static final String FATAL_ERROR_RESTART = """
    [ CRITICAL ] : FATAL ERROR RESTART

    The Kernel exited fatally. All unsaved work will be lost.
    Program will reboot automatically

    The cause may be due to an unhandled exception or a module logic that exited the
    program due to an exception or error.
    """;

    private static final String RESTART_UPDATE = """
    [ INFORMATION ] : SYSTEM UPDATE

    The program is restarting after an update installation.
    The kernel will boot soon. Please wait...
    """;

    private static final String UNDEFINED_EXIT_CODE = """
    [ WARNING ] : UNDEFINED EXIT CODE

    The program has exited with a code that the launcher cannot recognize.
    Please check with the kernel author(s) on why this might have happened.
    The program will exit normally.
    """;

    /************************************************************
     *                END MESSAGE STRINGS SECTION
     ************************************************************/



    private static void displayMessages(String message)
    {
        System.out.println(messageHeader + message + messageFooter);
    }


    /**
     * 
     * 
     * @param parameters
     * @return
     */
    private static int startKernelLogic(String[] parameters)
    { 
        int exitCode = 999;
        try
        {
            if(!new File(parameters[0]).exists()) 
            {
                displayMessages(KERNEL_NOT_FOUND);
                exitCode = 1024;
            }
            else
            {
                ProcessBuilder sessionMonitor=new ProcessBuilder("java", parameters[0]+".Core.Loader", parameters[1]);
                Process processMonitor = sessionMonitor.inheritIO().start();

                processMonitor.waitFor();
                exitCode = processMonitor.exitValue();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return exitCode; 
    }

    /**
     * Entry point of the Launcher program. Will boot any kernels that follow the Nion Directory Specification and Nion Kernel Specification.
     * 
     * @param args : Parameters, such as kernel name and kernel parameters are stored here.
     * @throws Exception : Handles any thrown Exceptions during runtime.
     */
    public static void main(String[] args)throws Exception
    {
        if(args.length < 2)
        {
            displayMessages(INVALID_SYNTAX);
        }
        while(true)
            switch(startKernelLogic(args))
            {
                case 0:
                System.exit(0);
                break;

                case 1:
                displayMessages(KERNEL_NOT_FOUND);
                System.exit(0);
                break;

                case 100:
                break;

                case 211:
                args[1] = "normal";
                break;

                case 212:
                args[1] = "repair";
                break;

                /*
                 * DEBUG OPTION!
                 * 
                 * case 213:
                 * args[1] = "debug";
                 * break;
                 */

                case 3:
                displayMessages(UNDEFINED_BOOTMODE);
                System.exit(0);
                break;

                case 4:
                displayMessages(FATAL_ERROR_EXIT);
                System.exit(0);
                break;

                case 5:
                displayMessages(FATAL_ERROR_RESTART);
                System.in.read();
                break;

                case 6:
                displayMessages(RESTART_UPDATE);
                System.exit(0);
                break;

                case 7:
                displayMessages(KERNEL_FOUND);
                System.exit(0);
                break;

                case 8:
                displayMessages(KERNEL_NOT_FOUND);
                System.exit(0);
                break;

                default:
                displayMessages(UNDEFINED_EXIT_CODE);
                System.exit(0);
            }

    }
}