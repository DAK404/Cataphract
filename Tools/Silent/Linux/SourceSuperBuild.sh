# -----------------------------
#   Truncheon Tools Suite 3.1
# -----------------------------
#
# This tool suite is written to
#  enable developers to easily
#   develop and run Truncheon
#
# -----------------------------
#
# =============================
#      Program Information
# =============================
#
# Author  : DAK404
# Purpose : A tool which helps
# in compiling everything!
#
# THIS IS NOT RECOMMENDED FOR
#         END USERS!
#
# =============================
#
# NOTE: THIS IS A SILENT TOOL!
#   USER INTERACTION IS NOT
#         RECOMMENDED!
#
# This tool is to be used in a
# terminal session or in editor
#  terminal sessions for quick
#     and easy compilation
#
# =============================

# Clear the screen
clear

# Display the build information
echo ========================
echo     Nion Tools Suite   
echo ========================
echo VERSION : 3.1
echo DATE    : 13-MAY-2022
echo ------------------------

# Write the documentation information to the log file
echo "Tulzscha Tools 3.1" > "CompileDoc.log" 2>&1

# Compile the launcher first
echo [1] Compiling Launcher...
javac -d ../Binaries Main.java

# Compile the Program then
echo [2] Compiling Program...
javac -d ../Binaries ./Cataphract/Core/Loader.java

# Compile the documentation
echo [3] Compiling Documentation
echo Compiling Documentation >> "CompileDoc.log" 2>&1

# List all the files in Source directory and its subdirectories to a SuperFileList.temp file
find . -type f -name "*.java" > SuperFileList.temp

# Run the javadoc command to compile the documentation by parsing every file found in sources.txt
# additionally, write the statuses to the compile log
echo [4] Creating Javadoc documentation...
javadoc -private -author -version -d ../docs/InternalDocumentation @SuperFileList.temp > "Internal_Docs.log" 2>&1
javadoc -author -version -d ../docs/DeveloperDocumentation @SuperFileList.temp > "Dev_Doc.log" 2>&1

# Delete SuperFileList after use
rm SuperFileList.temp

# Sign the build binaries for use
echo [5] Signing Build...
cd  ../Binaries/
java BuildSigner.java

# Confirm the status
echo [ ATTENTION ] Super Build complete.
