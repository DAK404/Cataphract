:: -----------------------------
::   Truncheon Tools Suite 3.1
:: -----------------------------
::
:: This tool suite is written to
::  enable developers to easily
::   develop and run Truncheon
::
:: -----------------------------
::
:: =============================
::      Program Information
:: =============================
::
:: Author  : DAK404
:: Purpose : A tool which helps
:: in compiling the program and
:: the documentation.
::
:: THIS IS NOT RECOMMENDED FOR
::         END USERS!
::
:: =============================
::
:: NOTE: THIS IS A SILENT TOOL!
::   USER INTERACTION IS NOT
::         RECOMMENDED!
::
:: This tool is to be used in a
:: terminal session or in editor
::  terminal sessions for quick
::     and easy compilation
::
:: =============================


:: Set the terminal echo mode to off
@ECHO OFF

:: Clear the screen
CLS

:: Display the build information
ECHO ========================
ECHO     Nion Tools Suite   
ECHO ========================
ECHO VERSION : 3.1
ECHO DATE    : 13-MAY-2022
ECHO ------------------------

:: Compile the launcher first
ECHO [1] Compiling Launcher...
javac -d ../Binaries Main.java

:: Compile the Program then
ECHO [2] Compiling Program...
javac -d ../Binaries ./Cataphract/Core/Loader.java

:: Sign the build binaries for use
ECHO [3] Signing Build...
cd  ../Binaries/
java BuildSigner.java

:: Confirm the status
ECHO [ ATTENTION ] Build complete.