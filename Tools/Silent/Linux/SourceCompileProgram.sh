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
printf " ========================\n"
printf "     Nion Tools Suite    \n"
printf " ========================\n"
printf " VERSION : 3.2.1         \n"
printf " DATE    : 06-OCT-2023   \n"
printf " ------------------------\n"

# Compile the launcher first
printf "[1] Compiling Launcher...\n"
javac -d ../Binaries Main.java

# Compile the program then
printf "[2] Compiling Program...\n"
javac -d ../Binaries ./Cataphract/Core/Loader.java

# Sign the build binaries for use
printf "[3] Signing Build...\n"
cd ../Binaries
java BuildSigner.java

# Confirm the status
printf "[ ATTENTION ] Build Complete.\n"