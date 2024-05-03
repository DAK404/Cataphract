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

/**
* A class implementing console output with prefixes, colors, and a "Press RETURN To Continue" functionality
*
* @author DAK404 (https://github.com/DAK404)
* @version 1.2.0 (11-October-2023, Cataphract)
* @since 1.1.0 (Truncheon Katana 1.2.0)
*/
public class IOStreams
{
    /** Instantiate Console to get user inputs. */
    private static Console console = System.console();

    /** Array that holds the text foreground values. */
    private final static String[] _textColorForeground = {"[30", "[31", "[32", "[33", "[34", "[35", "[36", "[37", "[39"};

    /** Array that holds the text background values. */
    private final static String[] _textColorBackground = {"40", "41", "42", "43", "44", "45", "46", "47", "49"};

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public IOStreams()
    {
    }

    /**
    * Prints the text specified, prefixed with an Information tag
    *
    * @param message The text specified to be printed onto the console.
    */
    public static void printInfo(String message)
    {
        println(2, 8, "[ INFORMATION ] " + message);
    }

    /**
    * Prints the text specified, prefixed with an Error tag
    *
    * @param message The text specified to be printed onto the console.
    */
    public static void printError(String message)
    {
        println(1, 8, "[    ERROR    ] " + message);
    }

    /**
    * Prints the text specified, prefixed with a Warning tag
    *
    * @param message The text specified to be printed onto the console.
    */
    public static void printWarning(String message)
    {
        println(3, 8, "[   WARNING   ] " + message);
    }

    /**
    * Prints the text specified, prefixed with an Attention tag
    *
    * @param message The text specified to be printed onto the console.
    */
    public static void printAttention(String message)
    {
        println(5, 8, "[  ATTENTION  ] " + message);
    }

    /**
    * Prints the text specified. Does not include any formatting.
    *
    * @param message The text specified to be printed onto the console.
    */
    public static void println(String message)
    {
        System.out.println(message);
    }

    /**
    * Text that can have a formatted background and foreground color to make a piece of text distinct from the rest. Prints the line and moves the curson to the next line.
    *
    * @param foregroundIndex Index value for the foreground color
    * @param backgroundIndex Index value for the background color
    * @param message The intended message that needs to be printed on the screen
    */
    public static void print(int foregroundIndex, int backgroundIndex, String message)
    {
        try
        {
            //Print the text with the specified indices correlating with the table specified
            System.out.print((char)27 + _textColorForeground[foregroundIndex] + ";" + _textColorBackground[backgroundIndex] + "m" + message + (char)27 + "[0m");
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            //Refuse to print anything else, prevent abuse of this method
            System.out.println(e + "Invalid Syntax.");
        }
        catch(Exception e)
        {
            //Catch any generic errors
            System.out.println(e);
        }
    }

     /**
    * Text that can have a formatted background and foreground color to make a piece of text distinct from the rest.
    *
    * @param foregroundIndex Index value for the foreground color
    * @param backgroundIndex Index value for the background color
    * @param message The intended message that needs to be printed on the screen
    */
    public static void println(int foregroundIndex, int backgroundIndex, String message)
    {
        print(foregroundIndex, backgroundIndex, message + "\n");
    }

    /**
    * Provide a method that will ask the user to press the RETURN key.
    * Useful when there is a long text to be read by the user.
    * 
    * @return String The value provided to the input
    */
    public static String confirmReturnToContinue()
    {
        return console.readLine("Press RETURN to Continue.");
    }

    /**
    * [ OVERLOAD ] Provide a method that will ask the user to press the RETURN key, with a prefix text and a suffix text.
    *
    * Useful when someone wants to insert text before or after the default "Press RETURN to Continue..." string
    * You might want to use a unicode escape character \u00A0 to essentially add a non breaking space to multiline Strings.     * 
     * 
     * @param prefix The prefix to be added before displaying the "Press RETURN to Continue" message
     * @param suffix The suffix to be added after displaying the "Press RETURN to Continue" message
     * @return String The value provided to the input
     */
    public static String confirmReturnToContinue(String prefix, String suffix)
    {
        return console.readLine(prefix + "Press RETURN to Continue" + suffix);
    }
}