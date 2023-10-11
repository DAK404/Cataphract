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
    |- SourceCompileProgram
|- Tools/

NOTE: Please choose the tool according to the OS you're running on. As of now Mac OS is not
officially supported. You may try to compile it at your own risk!

Once the SourceCompileProgram is copied into the Source directory, you can either just run the
tool in the Terminal/Console or simply double click it if the OS supports it.

The tool will display the status of the actions undertaken, and the build shall succeed or
fail depending on the compilation errors encountered. If there are errors, please correct the
specified issues and try again.

Once compiled, you'll see a Binaries directory under ProjectRoot.

If the Binaries directory did not exist previously, the build will not be signed.
Build Signing must be done to boot the kernel. It will not be covered in this guide as it is
covered in EasyGuide and is out of scope for this Documentation.

(TIP: Just re-run the SourceCompileProgram again after copying BuildSigner.java to Binaries
directory and the program should be signed on the 2nd run)

Once done, it may be useful to build the documentation if any changes were made to the program
and reflect the changes for the same.

There are 2 types of Documentation that can be built here:
Developer Documentation
Internal Documentation


Developer Documentation contains just enough details for developers to build applications on
top of or alongside Cataphract. This is useful to build an application that will utilize or
interact with the APIs that Cataphract offers. This does not cover the kernel architecture
and gives a limited scope and insight into the program structure.

Internal Documentation contains ALL details, including private, protected and public methods,
classes, constructors, methods and variables. This is useful to understand, improve, optimize
the kernel and help in building additional functionalities and features into the kernel.


DEVELOPER DOCUMENTATION COMPILE INSTRUCTIONS:

To compile the Developer Documentation, copy the SourceCompileDocs from tools directory into
the root of the Source directory.

The directory structure should look something like this:

./ ProjectRoot
|- .vscode/
|- docs/
|- Source/
    |- Cataphract/
    |- Main.java
    |- SourceCompileProgram
    |- SourceCompileDocs
|- Tools/

Once the SourceCompileDocs is copied into the Source directory, you can either just run the
tool in in the Terminal/Console or simply double click it if the OS supports it.

The tool will display the status of the actions undertaken, and will spew out the Dev_Doc.log
file. This log contains any possible errors and warnings produced by running the tool. This
is helpful to make the Documentation more readable and efficient.


INTERNAL DOCUMENTATION COMPILE INSTRUCTIONS:

To compile the Developer Documentation, copy the SourceCompileDocs_Internal from tools directory
into the root of the Source directory.

The directory structure should look something like this:

./ ProjectRoot
|- .vscode/
|- docs/
|- Source/
    |- Cataphract/
    |- Main.java
    |- SourceCompileProgram
    |- SourceCompileDocs
    |- SourceCompileDocs_Internal
|- Tools/

Once the SourceCompileDocs_Internal is copied into the Source directory, you can either just
run the tool in in the Terminal/Console or simply double click it if the OS supports it.

The tool will display the status of the actions undertaken, and will spew out the Internal_Doc.log
file. This log contains any possible errors and warnings produced by running the tool. This
is helpful to make the Documentation more readable and efficient.

SUPER BUILD COMPILE INSTRUCTIONS:

To compile the program and all documentation, copy the SourceSuperBuild file from the Tools 
directory to the Source directory.

The directory structure should look something like this:

./ ProjectRoot
|- .vscode/
|- docs/
|- Source/
    |- Cataphract/
    |- Main.java
    |- SourceSuperBuild
|- Tools/

The tool will display the status of the actions undertaken, and will spew out ALL log files.
This log contains any possible errors and warnings produced by running the tool.


The Documentation should be present in the docs directory present in the ProjectRoot directory.
The program binaries should be present in the Binaries directory present in the ProjectRoot
directory.

============================================================================================