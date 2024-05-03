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

package Cataphract.API.Astaroth;

import java.time.LocalDate;
import java.time.YearMonth;

import Cataphract.API.IOStreams;

/**
* A class that provides calendar utility for printing calendars.
*
* @author DAK404 (https://github.com/DAK404)
* @version 0.0.1 (20-February-2024, Cataphract)
* @since 0.0.1 (Cataphract 0.0.1)
*/
public class Calendar
{
    /** An integer to store the month value */
    private int month;

    /** An integer to store the year value */
    private int year;

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public Calendar()
    {
    }

    /**
    * Prints the calendar for a specific month and year.
    *
    * @param specificMonth The desired month (1-12). If 0 or out of range, current month is used.
    * @param specificYear  The desired year. If 0, current year is used.
    */
    public void printCalendar(int specificMonth, int specificYear)
    {
        // Set the month to current month if invalid input
        month = specificMonth == 0 || (specificMonth > 12 || specificMonth < 0) ? Integer.parseInt(new Time().getDateTimeUsingSpecifiedFormat("MM")) : specificMonth;
        // Set the year to current year if not specified
        year = specificYear == 0 ? Integer.parseInt(new Time().getDateTimeUsingSpecifiedFormat("YYYY")) : specificYear;
        // Delegate to the private method for printing the calendar
        printCalendar();
    }

    /**
    * Prints the calendar for the stored month and year.
    * Calculates the initial space (padding) for the first day of the month.
    * Prints days of the week and the days of the month.
    */
    private void printCalendar()
    {
        // Get the current date in the specified format
        String currentDate = new Time().getDateTimeUsingSpecifiedFormat("dd-MMMM-yyyy  EEEE");

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);

        // Print month and year
        IOStreams.println("\nToday is: " + currentDate + "\n");

        // Print the year and month currently viewing
        IOStreams.printInfo("You are currently viewing " + yearMonth.getMonth().toString() + "-" + yearMonth.getYear() + "\n");

        // Print days of the week header
        System.out.println("Su Mo Tu We Th Fr Sa");

        // Calculate the initial space (padding) for the first day
        int initialSpace = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        // Print the days of the month
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++)
        {
            if (day == 1)
            // Add initial space for the first day
            System.out.print(" ".repeat(initialSpace * 3));

            System.out.printf("%2s ", String.format("%02d", day));

            // Move to the next line after each week
            if ((day + initialSpace) % 7 == 0 || day == yearMonth.lengthOfMonth())
            System.out.println();
        }
        System.out.println();
    }
}
