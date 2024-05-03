#  -----------------------------
#    Truncheon Tools Suite 3.1
#  -----------------------------
#
#  This tool suite is written to
#   enable developers to easily
#    develop and run Truncheon
#
#  -----------------------------
#
#  =============================
#       Program Information
#  =============================
#
#  Author  : DAK404
#  Purpose : A tool which helps
#  in compiling the program and
#  the documentation.
#
#  THIS IS NOT RECOMMENDED FOR
#          END USERS!
#
#  =============================
#
#  NOTE: THIS IS A SILENT TOOL!
#    USER INTERACTION IS NOT
#          RECOMMENDED!
#
#  This tool is to be used in a
#  terminal session or in editor
#   terminal sessions for quick
#      and easy compilation
#
#  =============================

# Clear the screen
clear

# Display the build information
echo ========================
echo     Nion Tools Suite   
echo ========================
echo VERSION : 3.2.1
echo DATE    : 06-OCT-2023
echo ------------------------

# Compile the launcher first
echo [1] Compiling Launcher...
javac -d ../Binaries Main.java

# Compile the program then
echo [2] Compiling Program...
javac -d ../Binaries ./Cataphract/Core/Loader.java

# Sign the build binaries for use
echo [3] Signing Build...
cd ../Binaries
java BuildSigner.java

# Confirm the status
echo [ ATTENTION ] Build Complete.