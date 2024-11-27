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

Readme - Source Code Documentation

This directory contains the Cataphract source code.

This project contains Java code that is licensed under Lesser General Public License (LGPL).
LGPL is a permissive open-source license that allows you to use, modify, and distribute the
code as long as you comply with the terms of the license.

Please see the LICENSE file for more details.


-[ COMPILE INSTRUCTIONS ]-

PREREQUISITES: You will need Java to be installed on the system for the program to compile.
Installation and configuration of Java will not be covered in the Documentation as it is
out of scope.

There are various guides online that cover installing JDK and configuring your system to run
JDK. The Documentation shall also cover limited scope of using jlink to create a custom JRE
build that can be used to run Cataphract on any device without installing JDK.


SOURCE CODE COMPILE INSTRUCTIONS:

To compile the program, copy the SourceCompileProgram from Tools directory into the root of
the Source directory.

The directory structure should look something like this:

./ ProjectRoot
|- .vscode/
|- docs/
|- Source/
    |- Cataphract/
    |- Main.java
|- Tools/

In the project root directory, there is a file called makefile. This is a GNU Make file,
which is a script to compile the program efficiently.

--- NOTE ---
You may need to install GNU Make by via your Linux Distribution's package manager.
If you're on Windows, you may need to install Chocolatey Package Manager and then install
Make via Chocolatey.
------------

Opening a terminal and simply running the command `make` in the project root directory shall
display the following:

Nion Foundry Build System
Version: 4.0

Usage: make project=<project_name> <target>

->> Targets Available <<-
 * all       ->  [ RECOMMENDED ] Compiles everything.
 * super     ->  [ RECOMMENDED ] Compiles everything + documentation.
 * Kernel    ->  Compiles only the program
 * launcher  ->  Compiles only the launcher
 * docs      ->  Generates only the documentation
 * sign      ->  Signs the build
 * clean     ->  Cleans build and documentation directories

To read the complete documentation on this project, please visit
Link to documentation will be available soon.

This is the help viewer showing the available commands and available targets.

--- NOTE ---
* This is a handy command to compile your project using Nion Foundry Build System:

    make project=<project_name> <target>

To compile Cataphract, the following can be used:

    make project=Cataphract all
------------

The following explains how to use the Nion Foundry Build System.

1. project parameter:
    The project parameter shall specify the project that needs to be compiled. In this case,
    the value that needs to be provided is `Cataphract`.

    --- NOTE ---
    The project name is case sensitive.
    ------------

2. target:
    This shall specify what aspects of the program needs to be compiled. For those who
    would like to compile the program and be able to use it, the `all` target needs to be
    selected.

    The following shall explain the available targets to the end user:

    * all - This target shall compile the launcher, Kernel, and sign the build.
    * super - This target shall execute the target `all` and additionally, generate
        Javadoc Documentation pages.
    * kernel - This target is for developers who would like to compile the Kernel only.
    * launcher - This target is for developers who would like to compile the launcher only.
    * docs - This target is for developers who would like to generate the Javadoc pages.
    * sign - This target is used to sign the build present in the `Binaries` directory.
    * clean - This target cleans the directories of `docs` and `Binaries`

    --- NOTE ---
    * The targets are case sensitive.
    * Target and project name is necessary for Nion Foundry Build System
      to work as expected.
    ------------

- [ RUNNING THE PROJECT ]-

SQLITE JDBC SETUP INSTRUCTIONS:

To run the project, there is one last step necessary: Downloading and extracting SQLite
JDBC driver.

The SQLite JDBC driver is used to connect to a database (which is created during the program
setup stages to store user information). Without this driver, the program will fail to login
and work as expected.

Since SQLite is not a project undertaken by me, it is recommended to download the version
directly from the release page of the SQLite repository on GitHub.

-------------------------------------------------------------
---! Please note that only version 3.43.0.0 is supported !---
-------------------------------------------------------------

RUNNING THE COMPILED BINARIES:

The compiled binaries can be run by using the following syntax:

java Main <kernel_name> <mode> <other_args>

The meaning of the above syntax is as follows:

Main: This is the Main.class executed. This is the Launcher application which will start
the specified Kernel.

<kernel_name>: This is the name of the Kernel to start. The Launcher application is designed
to run with various kernels that are compatible with the Nion File Structure Specification.
With multiple kernels existing in the same directory, it would make sense to specify which
Kernel needs to be started. This will remove the necessity for each Kernel to have its own
dedicated Launcher application.

<mode>: This will tell the Kernel to start in the specified mode. This mode shall vary from
Kernel to Kernel. Please refer to the documentation for the specified Kernel to learn more
about the modes supported.

<other_args>: These are other arguments that are required by the Kernel, which will be
passed on from the Launcher application to the Kernel.


-[ DOCUMENTATION ]-

Documentation is present in the ./docs directory. There are 2 types of documentation that
can be used for different purposes.

DEVELOPER DOCUMENTATION:

If you are working on building programs by utilizing the Cataphract APIs, the Developer
Documentation should help, by providing the details of classes, methods and fields. This
documentation does not include the details on private methods and fields, and presents only
the set of essential data necessary to utilize the APIs.

INTERNAL DOCUMENTATION:

If you are working on adding new features, fixing issues or modifying the code of the APIs
and Kernel itself, the Internal Documentation should help, by providing details of classes,
methods and fields including the private methods and fields. This documentation shall detail
the structure of each class without the implementation, making it easier to understand how
to modify the program as per the needs.

============================================================================================