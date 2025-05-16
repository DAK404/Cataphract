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

package Cataphract.API.Astaroth;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Cataphract.API.IOStreams;

/**
* A class that provides a utility for handling date and time operations.
*
* @author DAK404 (https://github.com/DAK404)
* @version 0.0.1 (20-February-2024, Cataphract)
* @since 0.0.1 (Cataphract 0.0.1)
*/
public class Time
{
    /** Store the current date and time as a LocalDateTime object*/
    private LocalDateTime currentDateTime = LocalDateTime.now();

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public Time()
    {
    }

    /**
    * Gets the current date and time formatted according to the specified format.
    *
    * @param format The desired format (e.g., "yyyy-MM-dd HH:mm:ss").
    * @return A string representing the current date and time in the specified format.
    */
    public String getDateTimeUsingSpecifiedFormat(String format)
    {
        String result = "";

        //Handle any invalid date and time formats.
        try
        {
            result = currentDateTime.format(DateTimeFormatter.ofPattern(format));
        }
        catch(DateTimeParseException e)
        {
            IOStreams.printError("Invalid Date/Time Format Detected! Please enter a valid Date/Time format.");
        }
        catch(IllegalArgumentException e)
        {
            IOStreams.printError("Invalid Date/Time Format Detected! Please enter a valid Date/Time format.");
        }
        catch(DateTimeException e)
        {
            IOStreams.printError("Invalid Date/Time Format Detected! Please enter a valid Date/Time format.");
        }
        return result;
    }

    /**
    * Gets the current Unix epoch timestamp (seconds since January 1, 1970).
    *
    * @return The Unix epoch timestamp.
    */
    public long getUnixEpoch()
    {
        return Instant.now().getEpochSecond();
    }

    /**
    * Gets the current time (hours, minutes, and seconds) in the format "HH:mm:ss".
    *
    * @return A string representing the current time.
    */
    public String getTime()
    {
        return getDateTimeUsingSpecifiedFormat("HH:mm:ss");
    }
}
