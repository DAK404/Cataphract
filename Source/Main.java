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

/**
* A program to launch kernels that follow the Nion Directory Specification and Nion Kernel Specification
*
* @author DAK404 (https://github.com/DAK404)
* @version 4.24.0 (11-October-2023, Cataphract)
* @since 0.0.1 (Zen Quantum 1.0)
*/
public class Main
{
    /************************************************************
    *             MESSAGE HEADER AND FOOTER SECTION
    ************************************************************/

    /** A header used for messages, displayed right before the message. */
    private static final String messageHeader = """
    *********************************
    NION PROGRAM LOADER
    *********************************
    Version : 7.247

    """;

    /** A footer used for messages, displayed right after the message. */
    private static final String messageFooter = """
    *********************************
    """;

    /************************************************************
    *           END MESSAGE HEADER AND FOOTER SECTION
    ************************************************************/


    /************************************************************
    *                  MESSAGE STRINGS SECTION
    ************************************************************/

    /** A quick help for the launcher, if the user does not know how to boot a Kernel. */
    private static final String launcherHelp = """
    This program helps in launching Sycorax Kernel, and
    any other Kernel that follows the Nion Directory Specification.

    Detailed documentation can be found on: https://dak404.github.io/Cataphract/Docs

    Quick Help:

    * Command to boot the Kernel:
    java <arguments> Main <Kernel_Name> <Boot_Mode>

    * Command to probe a Kernel:
    java <arguments> Main <Kernel_Name> probe
    """;

    /** Message informing the user that the kernel probe was successful. */
    private static final String KERNEL_FOUND = """
    [ INFORMATION ] : KERNEL FOUND

    This Kernel can be booted by the launcher program!
    Please do note that there is no way to enforce a Kernel to utilize the Nion Kernel Specification.
    If you are using a 3rd party Kernel, please be cautious and beware of unexpected behavior.
    """;

    /** An error notifying the user that the launcher syntax is invalid. */
    private static final String INVALID_SYNTAX = """
    [ ERROR ] : INVALID LAUNCHER SYNTAX.

    Description: This Launcher Program conforms to Nion Kernel Specifications.
    This means that the kernels to be compatible with this launcher will need to:

    1. Have a public class called Loader within the Kernel Package
    2. Have a main method with argument type String[]
    3. Have a public method called 'probe' with return type boolean

    The kernels require a set of parameters to be started. Without which,
    the kernels shall not boot.

    Please provide the required parameters for the Kernel to boot.
    """;

    /** An error notifying the user that the kernel probe was unsuccessful. */
    private static final String KERNEL_NOT_FOUND = """
    [ ERROR ] : KERNEL NOT FOUND.

    Description: The specified Kernel in the arguments is not found.
    Please provide a valid Kernel name to boot into.
    """;

    /** An error notifying the user that the provided boot mode is not found. */
    private static final String UNDEFINED_BOOTMODE = """
    [ ERROR ] : UNDEFINED BOOT MODE

    The boot mode provided is currently not supported by the Kernel.
    Please update the Kernel and try again. If it does not work, please have a look at
    all the supported boot modes for the specified Kernel.
    """;

    /** A critical information notifying the user that the kernel exited fatally. */
    private static final String FATAL_ERROR_EXIT = """
    [ CRITICAL ] : FATAL ERROR EXIT

    The Kernel exited fatally. All unsaved work will be lost.

    The cause may be due to an unhandled exception or a module logic that exited the
    program due to an exception or error. You must manually boot the Kernel again to
    continue to use the program.
    """;

    /** A critical information notifying the user that the kernel is restarting after exiting fatally. */
    private static final String FATAL_ERROR_RESTART = """
    [ CRITICAL ] : FATAL ERROR RESTART

    The Kernel exited fatally. All unsaved work will be lost.
    Program will reboot automatically

    The cause may be due to an unhandled exception or a module logic that exited the
    program due to an exception or error.
    """;

    /** An information that the kernel is restarting after an update installation. */
    private static final String RESTART_UPDATE = """
    [ INFORMATION ] : SYSTEM UPDATE

    The program is restarting after an update installation.
    The Kernel will boot soon. Please wait...
    """;

    /** A Warning displayed to the user that the kernel exited with an undefined exit code. */
    private static final String UNDEFINED_EXIT_CODE = """
    [ WARNING ] : UNDEFINED EXIT CODE

    The program has exited with a code that the launcher cannot recognize.
    If the Kernel follows the Nion Kernel Specification, please contact the
    program author with relevant details (eg. which module you were trying
    to use, kernel version, launcher version, etc.)

    If the Kernel does not follow the Nion Kernel Specification, this may be
    normal. Please contact the program authors if you think it was an abnormal
    exit.

    The program will exit normally.
    """;

    /************************************************************
    *                END MESSAGE STRINGS SECTION
    ************************************************************/

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public Main()
    {
    }

    /**
    * A method that displays messages with the specified header and footer.
    *
    * @param message The message to be displayed.
    */
    private static void displayMessages(String message)
    {
        System.out.println(messageHeader + message + messageFooter);
    }


    /**
    * A method that has the logic to start or probe a kernel. The kernel needs to follow the Nion Directory Specification to be booted from this launcher.
    *
    * @param parameters The boot parameters that is passed on to the kernel.
    * @return int exitCode: The exit code returned after executing the logic in the kernel booted.
    */
    private static int startKernelLogic(String[] parameters)
    {
        //Initialize an exit code with a default value of 999
        int exitCode = 999;

        try
        {
            //Check if the kernel exists before booting
            if(!new File(parameters[0]).exists())
            //If it does not exist, the exit code is set to 1024
            exitCode = 1024;

            //If the kernel exists, boot the kernel
            else
            {
                //Create a new process and spawn it with the Kernel name and the parameters.
                ProcessBuilder sessionMonitor=new ProcessBuilder("java", parameters[0]+".Core.Loader", parameters[1]);
                //Allow the process to start and allow the current console window to inherit IO features.
                Process processMonitor = sessionMonitor.inheritIO().start();

                //Wait for the spawned process to exit.
                processMonitor.waitFor();
                //Set exitCode to the kernel's exit value
                exitCode = processMonitor.exitValue();
            }
        }
        //Catch any exceptions caught during execution
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //return the exit code.
        return exitCode;
    }

    /**
    * Entry point of the Launcher program. Will boot any kernels that follow the Nion Directory Specification and Nion Kernel Specification.
    *
    * @param args Parameters, such as Kernel name and Kernel parameters are stored here.
    * @throws Exception Throws any exceptions encountered during runtime.
    */
    public static void main(String[] args)throws Exception
    {
        //Display the help message if the "help" argument is used
        if(args[0].equalsIgnoreCase("help"))
                displayMessages(launcherHelp);
        
        //An assert created to check if the arguments provided are less than 2
        else if(args.length < 2)
        {
            displayMessages(INVALID_SYNTAX);
        }

        //if above assert succeeds, start the below logic
        else

        //execute the logic infinitely
        while(true)
        //logic to handle the exit codes by the kernel
        switch(startKernelLogic(args))
        {
            //Normal exit mode
            case 0:
            System.exit(0);
            break;

            //Kernel not found
            case 1:
            displayMessages(KERNEL_NOT_FOUND);
            System.out.println(1234);
            System.exit(0);
            break;

            //Normal Restart mode (for legacy compatibility)
            case 100:
            //Normal Restart mode (New implementation)
            case 211:
            args[1] = "normal";
            break;

            //Force repair mode
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

            //Boot mode not found
            case 3:
            displayMessages(UNDEFINED_BOOTMODE);
            System.exit(0);
            break;

            //Fatal error exit
            case 4:
            displayMessages(FATAL_ERROR_EXIT);
            System.exit(0);
            break;

            //Fatal error restart
            case 5:
            displayMessages(FATAL_ERROR_RESTART);
            System.in.read();
            break;

            //Restart after update
            case 6:
            displayMessages(RESTART_UPDATE);
            System.exit(0);
            break;

            //PROBE: Kernel found
            case 7:
            displayMessages(KERNEL_FOUND);
            System.exit(0);
            break;

            //PROBE: Kernel not found
            case 1024:
            displayMessages(KERNEL_NOT_FOUND);
            System.exit(0);
            break;

            //Kernel not found
            default:
            displayMessages(UNDEFINED_EXIT_CODE);
            System.exit(0);
        }

    }
}