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

Package: Cataphract.Core
Author: DAK404 (https://github.com/DAK404)

-[ INTRODUCTION ]-

The Core package contains the Kernel and the programs that utilize the APIs whilst providing
an interface for the user to access the available functionalities.


-[ HISTORY ]-

The programs in the Core packages have, more or less, had the same purposes since the start.
Before Zen Quantum, there were no APIs implemented separately, therefore each class was a
standalone executable. This was because the program was significantly smaller than what it
is currently. As the number of functionalities grew, it became difficult to maintain code
due the vast inconsistencies across programs.

From Zen Quantum onwards, an API package was created to hold programs that could be reused
by the classes in the Core package. At this time, the APIs were still rudimentary and poorly
implemented since many classes were large blocks of code which was not very flexible to be
reused across programs.

These problems were mostly fixed in Mosaic and Lamashtu, where the API code was simplified
and the classes in Core were mostly designed to provide a good user interface and a simple
command-set to the user.


-[ DETAILS ]-

The Core package mainly consists of 3 important classes: The Loader, Setup and the Kernel
itself.

LOADER:

The Loader class, in Cataphract, shall boot the program to the specified boot mode.
Here the following boot modes are available in Cataphract:

    * probe: The Loader class shall exit with exit code 7, signifying that the kernel exists
    and can be booted with the Launcher application.

    * debug: The Loader class shall try to utilize a specific API or class, usually to test
    a feature or functionality. Usually the debug options are the names of the packages.
    The following shall detail on the available debug options:
        * iostreams - Tests the IOStreams by printing various types of messages.
        * astaroth  - Tests the functionality of the astaroth class
        * crash     - Simulates a program crash to check the Error Handling functionalities.

The Loader then begins to check the files to check if they have been tampered with.
Checks are made on the file signatures and the file sizes. This will ensure that the files
are clean and will then proceed on to check if the program needs to be setup or can go ahead
to start the kernel.

SETUP:

The Setup sub-class is a part of the Loader class. It will initialize the necessary files and
directories required for Cataphract to run.

First, the user will be asked to accept the End User License Agreement (EULA) and once the
user has accepted the EULA, the Setup class shall make the essential directories, initialize
the database, create the default set of policies and will then prompt the user to create an
Administrator account.

Once the Administrator account has been created, the program shall restart and boot normally
to the Guest Prompt.

SYCORAXKERNEL:

This is the Kernel that will be loaded after the file checks and the setup. This will give
the user a login interface and a prompt to enter the necessary commands to use a feature or
functionality.

It utilizes the Anvil Script Engine and the Anvil class for basic features and functionalities.

============================================================================================
