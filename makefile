# Makefile for Nion Projects
# Author: DAK404

# Variables
FOUNDRY_VER = 4.0
PROJECT_NAME = $(project)
SRC_DIR = ./Source
BIN_DIR = ./Binaries
DOC_DIR = ./docs/$(PROJECT_NAME)
TOOLS_DIR = ./Tools
COMP_LOG_DIR = ./CompileLogs
BUILDSIGNER_FILE = BuildSigner.java

# Classpath (if needed)
CLASSPATH = ./Source
BIN_CLASSPATH = ./Binaries

# Default target
default: help

help:
	@echo ""
	@echo "Nion Foundry Build System"
	@echo "Version: $(FOUNDRY_VER)"
	@echo ""
	@echo "Usage: make project=<project_name> <target>"
	@echo ""
	@echo "->> Targets Available <<-"
	@echo " * all       ->  [ RECOMMENDED ] Compiles everything."
	@echo " * super     ->  [ RECOMMENDED ] Compiles everything + documentation."
	@echo " * kernel    ->  Compiles only the program"
	@echo " * launcher  ->  Compiles only the launcher"
	@echo " * docs      ->  Generates only the documentation"
	@echo " * sign      ->  Signs the build"
	@echo " * clean     ->  Cleans build and documentation directories"
	@echo ""
	@echo "To read the complete documentation on this project, please visit"
#TO-DO: Insert documentation link
	@echo "Link to documentation will be available soon."
	@echo ""

.PHONY: help

# Ensure targets are treated as phony
.PHONY: all setup kernel launcher docs sign clean copy_docs

# Build steps
all: setup kernel launcher sign copy_docs

# Super Build (includes documentations)
super: setup kernel launcher sign docs copy_docs

# Preliminary setup
setup:
	@echo ""
	@echo "----->>>>> Foundry Build started @ [$(shell date +'%Y-%m-%d %H:%M:%S %Z')]"
	@echo "Compiling: $(PROJECT_NAME)"
	@echo ""
	@echo "============================="
	@echo "  Nion Foundry Build System  "
	@echo "============================="
	@echo "   VERSION : 4.0"
	@echo "   UPDATED : 15-OCT-2024"
	@echo "-----------------------------"
	@echo ""
	@echo "Note: Logs are being saved to ./CompileLogs directory."
	@echo "______________________________________________________"
	@echo ""
	@echo "[*] Checking Prerequisites..."
	@echo ""
	@echo "->>> Checking for ./Binaries/"
	@if [ ! -d "$(BIN_DIR)" ]; then mkdir -p $(BIN_DIR); fi
	@echo "->>> Checking for ./CompileLogs/"
	@if [ ! -d "$(COMP_LOG_DIR)" ]; then mkdir -p $(COMP_LOG_DIR); fi
	@echo "->>> Checking for BuildSigner.java"
	@if [ ! -f "$(BIN_DIR)/$(BUILDSIGNER_FILE)" ]; then cp $(TOOLS_DIR)/$(BUILDSIGNER_FILE) $(BIN_DIR); fi
	@echo "->>> Checking Binaries/docs directory"
	@if [ ! -d "$(BIN_DIR)/docs/$(project)/Help" ]; then mkdir -p $(BIN_DIR)/docs/$(project)/Help; fi
	@echo ""
	@echo "--- !   PREREQUISITES CHECK DONE   ! ---"
	@echo ""

# Compilation targets
kernel: setup
	@echo "[*] Compiling Program..."
	@echo ""
	javac -cp $(CLASSPATH) -d $(BIN_DIR) $(SRC_DIR)/$(PROJECT_NAME)/Core/Loader.java
	@echo ""
	@echo "--- ! PROGRAM COMPILATION DONE ! ---"
	@echo ""

launcher: setup
	@echo "[*] Compiling Launcher..."
	@echo ""
	javac -cp $(CLASSPATH) -d $(BIN_DIR) $(SRC_DIR)/Main.java
	@echo ""
	@echo "--- !   LAUNCHER COMPILATION DONE  ! ---"
	@echo ""

# Documentation generation
docs: setup
	find $(SRC_DIR)/$(PROJECT_NAME) -type f -name "*.java" > SuperFileList.temp
	@echo "[*] Compiling Internal Documentation..."
	@echo ""
	javadoc -private -author -version -d $(DOC_DIR)/InternalDocumentation @SuperFileList.temp > $(COMP_LOG_DIR)/Internal_Docs.log 2>&1
	@echo ""
	@echo "--- !  PROGRAM DOCUMENTATION DONE  ! ---"
	@echo ""
	@echo "[*] Compiling Developer Documentation..."
	@echo ""
	javadoc -author -version -d $(DOC_DIR)/DeveloperDocumentation @SuperFileList.temp > $(COMP_LOG_DIR)/Dev_Doc.log 2>&1
	@echo ""
	@echo "--- ! DEVELOPER DOCUMENTATION DONE ! ---"
	@echo ""
	@echo "[*] Cleaning Up..."
	@echo ""
	rm SuperFileList.temp
	@echo ""
	@echo "--- !         CLEANUP DONE         ! ---"
	@echo ""
	@echo "--- !   JAVADOC COMPILATION DONE   ! ---"
	@echo ""

# Post-build signing
sign: setup
	@echo "[*] Signing Build..."
	@echo ""
	cd $(BIN_DIR) && java -cp $(BIN_CLASSPATH) BuildSigner.java
	@echo ""
	@echo "--- !      BUILD SIGNING DONE      ! ---"
	@echo ""

# Copy documentation to Binaries directory
copy_docs: setup
	@echo "[*] Copying Documentation..."
	@echo ""
	@echo "->>> Copying contents from docs/$(project)/Help to Binaries/docs/$(project)/Help"
	@cp -r docs/$(project)/Help $(BIN_DIR)/docs/$(project)
	@echo ""
	@echo "--- !   DOCUMENTATION COPIED   ! ---"
	@echo ""

clean:
	@echo "[*] Cleaning Up Directories..."
	@echo ""
	rm -rf $(BIN_DIR)* $(DOC_DIR)/DeveloperDocumentation/* $(DOC_DIR)/InternalDocumentation/*
	@echo ""
	@echo "--- !       CLEANUP COMPLETE       ! ---"
	@echo ""

.PHONY: default help all setup kernel launcher docs sign clean copy_docs
