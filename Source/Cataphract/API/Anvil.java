package Cataphract.API;

//Scripting featureset

// COMMON COMPONENTS LIKE WAIT, INPUT, ECHO ARE HERE. NO REDUNDANCY
//ANY EXTRA FEATURES ARE PASSED BACK TO THE PROGRAM AND ARE PROCESSED BY THE CALLING PROGRAM

/**
 *
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
     * @param command
     * @return
     * @throws Exception : Throws any exceptions encountered during runtime.
     */
    public static boolean anvilInterpreter(String command)throws Exception
    {
        boolean status = true;
        String[] commandArray = splitStringToArray(command);

        switch(commandArray[0].toLowerCase())
        {
            case "clear":
                Build.viewBuildInfo();
            break;

            case "echo":
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

            // case "about":
            //     Build.aboutProgram();
            // break;

            case "wait":
                try
                {
                    if(Integer.parseInt(commandArray[1]) < 1)
                        IOStreams.println("This will make the prompt wait for a given number of milliseconds.\nSyntax: wait (1000)\n\nPrompt shall wait for 1 second.");
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

            case "confirm":
                //Check for arguments so that if there are prefixes and suffixes, call the method with the arguments to have a custom string instead of the default.
                IOStreams.confirmReturnToContinue();
            break;

            default:
                status = false;
            break;
        }
        return status;
    }

    /**
     * Method to split an input string to individual words by at the occurrence of a blank space.
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