# Makefile for Nion Projects
# Author: DAK404

# Default target
default: help

help:
	@echo "Usage: make project=<project_name> <target>"
	@echo "Targets:"
	@echo "  all        -> Compiles everything"
	@echo "  kernel     -> Compiles the program"
	@echo "  launcher   -> Compiles the launcher"
	@echo "  docs       -> Generates documentation"
	@echo "  sign       -> Signs the build"
	@echo "  clean      -> Cleans the build and documentation directories"

.PHONY: help

# Variables
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

# Ensure targets are treated as phony
.PHONY: all setup kernel launcher docs sign clean

# Build steps
all: setup kernel launcher docs sign

# Preliminary setup
setup:
	@echo ""
	@echo "----->>> Foundry Build started @ [$(shell date +'%Y-%m-%d %H:%M:%S %Z')]"
	@echo "Compiling: $(PROJECT_NAME)"
	@echo ""
	@echo "============================="
	@echo "  Nion Foundry Build System  "
	@echo "============================="
	@echo "VERSION : 4.0"
	@echo "UPDATED : 15-OCT-2024"
	@echo "-----------------------------"
	@echo ""
	@echo "Note: Logs are being saved to ./CompileLogs directory."
	@echo "______________________________________________________"
	@echo ""
	@echo "[*] Checking Prerequisites..."
	@echo ""
	@echo "->>> Checking for ./Binaries"
	@if [ ! -d "$(BIN_DIR)" ]; then mkdir -p $(BIN_DIR); fi
	@echo "->>> Checking for ./CompileLogs"
	@if [ ! -d "$(COMP_LOG_DIR)" ]; then mkdir -p $(COMP_LOG_DIR); fi
	@echo "->>> Checking for BuildSigner.java"
	@if [ ! -f "$(BIN_DIR)/$(BUILDSIGNER_FILE)" ]; then cp $(TOOLS_DIR)/$(BUILDSIGNER_FILE) $(BIN_DIR); fi
	@echo ""
	@echo "--- ! PREREQUISITES CHECK DONE ! ---"
	@echo ""

# Compilation targets
kernel: setup launcher sign
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
	@echo "--- ! LAUNCHER COMPILATION DONE ! ---"
	@echo ""

# Documentation generation
docs: setup
	find $(SRC_DIR)/$(PROJECT_NAME) -type f -name "*.java" > SuperFileList.temp
	@echo "[*] Compiling Internal Documentation..."
	@echo ""
	javadoc -private -author -version -d $(DOC_DIR)/InternalDocumentation @SuperFileList.temp > $(COMP_LOG_DIR)/Internal_Docs.log 2>&1
	@echo ""
	@echo "--- ! INTERNAL DOCUMENTATION DONE ! ---"
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
	@echo "--- ! CLEANUP DONE ! ---"
	@echo ""
	@echo "--- ! JAVADOC COMPILATION DONE ! ---"
	@echo ""

# Post-build signing
sign: setup
	@echo "[*] Signing Build..."
	@echo ""
	cd $(BIN_DIR) && java -cp $(BIN_CLASSPATH) BuildSigner.java
	@echo ""
	@echo "--- ! BUILD SIGNING DONE ! ---"
	@echo ""

clean:
	@echo "Cleaning up..."
	rm -rf $(BIN_DIR)* $(DOC_DIR)*
	@echo ""

.PHONY: default help all setup kernel launcher docs sign clean
