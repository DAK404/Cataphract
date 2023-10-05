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

package Cataphract.Core;

import Cataphract.API.IOStreams;
import Cataphract.API.Build;

public class Loader
{
    public static void main(String[] args)throws Exception
    {
        Build.viewBuildInfo();

        switch(args[0])
        {
            case "probe":
            System.exit(7);
            break;

            case "normal":
            break;

            default:
            System.exit(3);
        }

        IOStreams.println("Hello World!");
    }
}