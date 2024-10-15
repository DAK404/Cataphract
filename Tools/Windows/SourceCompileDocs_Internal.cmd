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
:: in compiling the program
:: javadoc documentation.
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
ECHO DATE    : 18-JUN-2022
ECHO ------------------------

:: List all the files in Source directory and its subdirectories to a sources.txt file
dir /s /B *.java > IntDocumentationFileList.temp

:: Run the javadoc command to compile the documentation by parsing every file found in sources.txt
:: additionally, write the statuses to the compile log
javadoc -d ../docs/InternalDocumentation -author -version --show-members private @IntDocumentationFileList.temp > "Internal_Docs.log" 2>&1

:: Delete the sources.txt file after use
DEL /s /q IntDocumentationFileList.temp