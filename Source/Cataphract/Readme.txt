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

This is a super package that contains all the required files and programs to run Cataphract.
As per the Nion Directory Specification, this package contains Core and API sub-packages.


-[ HISTORY ]-

When Zen Quantum was being developed, it was easier to maintain code if APIs could be under
a single package. This helped in code readability too, and therefore, became a structure of
how Nion program source code files were eventually arranged.

When developing Mosaic, we further went towards developing sub-packages. These sub-packages 
contained various programs that helped in implementing a functionality.

Example:
For managing accounts, there were several programs to create, modify and delete accounts.
Before Zen Quantum, all the account management functionality were in one single program, 
amounting to almost 700 lines of code!

When the sub-packages were introduced in Zen Quantum, the code became easy to read, more
efficient, manageable and maintainable.

The package nomenclature is discussed, in-depth, in the Nion Directory Specification that 
can be found in EasyGuide Documentation.


-[ DETAILS ]-

The super package contains Core and API, following the Nion Directory Specification.

Core:
This package contains the Kernel and the Loader programs. This is the core of Cataphract.

API:
This package contains APIs that the programs in the Core package rely on.

Sub-packages may exist with various programs, essentially grouped by features and the
functionality of the sub-programs.

-[ IMPORTANT LINKS ]-

EasyGuide - Nion Directory Specification : 
Developer Program Documentation          :
Internal Program Documentation           : 
Website                                  : https://dak404.github.io/Cataphract
GitHub Repository                        : https://github.com/DAK404/Cataphract

============================================================================================