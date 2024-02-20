                                                      |
                                                     ||
  |||||| ||||||||| |||||||| ||||||||| |||||||  |||  ||| ||||||| |||||||||  |||||| ||||||||
 |||            ||    |||          ||       || |||  |||       ||       || |||        |||
 |||      ||||||||    |||    ||||||||  ||||||  ||||||||  ||||||  |||||||| |||        |||
 |||      |||  |||    |||    |||  |||  |||     |||  |||  ||  ||  |||  ||| |||        |||
  ||||||  |||  |||    |||    |||  |||  |||     |||  |||  ||   || |||  |||  ||||||    |||
                                               ||
                                               |

A Cross Platform OS Shell.
Powered By Truncheon Core.

============================================================================================

Readme - Package Level Documentation.

Package: Cataphract
Author: DAK404 (https://github.com/DAK404)

-[ INTRODUCTION ]-

This Package provides utilities to handle date/time efficiently. Can also print a calendar,
helpful for users and scripting.


-[ HISTORY ]-

While working on Truncheon (then called Lamashtu before being cancelled and reworked),
date/time handling became essential since the program would write log files. Additionally,
for scripting, end users could not print the date and time information. At that point of
time, WraithEdit.java handled the logging and used the java.util.Date class.

This created a problem.

For example, let us consider 3 classes: A, B and C.

If class A uses java.util.Date, class B uses java.time.LocalDateTime and class C uses a 3rd
party library or java.text.date to implement the same features, the memory usage would be
very high due to the number of classes loaded into the memory for runtime.


-[ DETAILS ]-

Currently, the Astaroth package shall return date/time values for a given format.
The formats can be found on the official Java Documentation website:

https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html

The Astaroth package also provides a feature to display a calendar for a specified month and
year. If the input values are 0, the calendar for the current month is displayed.


-[ IMPORTANT LINKS ]-

EasyGuide - Nion Directory Specification :
Developer Program Documentation          : https://dak404.github.io/Cataphract/DeveloperDocumentation/index.html
Internal Program Documentation           : https://dak404.github.io/Cataphract/InternalDocumentation/index.html
Website                                  : https://dak404.github.io/Cataphract
GitHub Repository                        : https://github.com/DAK404/Cataphract

============================================================================================