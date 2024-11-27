                                                      |
                                                     ||
  |||||| ||||||||| |||||||| ||||||||| |||||||  |||  ||| ||||||| |||||||||  |||||| |||||||||
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

Package: Cataphract.API.Wraith.Archive
Author: DAK404 (https://github.com/DAK404)

-[ INTRODUCTION ]-

This Package provides utilities to zip and unzip files in the current directory. Also
provides logic to install updates from an update zip file.

-[ DETAILS ]-

The classes currently implemented in this package are:

    * FileZip.java
    * FileUnzip.java

The following details the functionality of each class:

* FileZip.java - Zips the directory specified, bearing the name provided by the user.

* FileUnzip.java - Unzips the zip file specified, inside the destination directory
specified by the user. Also unzips the update.zip file and installs the program update.

============================================================================================