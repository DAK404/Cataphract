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
# in compiling the program
# javadoc documentation.
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
echo DATE    : 18-JUN-2022
echo ------------------------

# List all the files in Source directory and its subdirectories to a sources.txt file
find . -type f -name "*.java" > DevDocumentationFileList.temp

# Run the javadoc command to compile the documentation by parsing every file found in sources.txt
# additionally, write the statuses to the compile log
javadoc -author -version -d ../docs/DeveloperDocumentation @SuperFileList.temp > "Dev_Doc.log" 2>&1

# Delete the sources.txt file after use
rm DevDocumentationFileList.temp
