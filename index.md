# Nion: Cataphract

2024-October-22 | Documentation Version 0.0.1

---

# Table of Contents

- [Nion: Cataphract](#nion-cataphract)
- [Table of Contents](#table-of-contents)
- [Important Links](#important-links)
  - [Mandatory Software](#mandatory-software)
  - [Recommended Software](#recommended-software)
  - [Miscellaneous Resources](#miscellaneous-resources)
- [Introduction](#introduction)
- [Getting Started](#getting-started)
- [Documentation](#documentation)
  - [Developer Documentation](#developer-documentation)
  - [Internal Documentation](#internal-documentation)

---

# Important Links

To save time, here are a few links that can help in finding the required resources for developing, building and testing Cataphract.

## Mandatory Software

* [SQLite JDBC Driver](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/)
* [OpenJDK](https://jdk.java.net)
  * Please do note that JDK from any vendor shall suffice.
* Cataphract Java Runtime Environment --> To be updated

## Recommended Software

* [Visual Studio Code](https://code.visualstudio.com/) or [VSCodium](https://vscodium.com/)
* Basic knowledge on:
  * [Java](https://docs.oracle.com/en/java/)
  * RDBMS Concepts, SQL and JDBC using [SQLite](https://sqlite.org/java/raw/doc/overview.html?name=0a704f4b7294a3d63e6ea2b612daa3b997c4b5f1)
* Patience and Time 😄

## Miscellaneous Resources

* [Cataphract Website](https://dak404.github.io/Cataphract)
* [Cataphract Repository on GitHub](https://github.com/DAK404/Cataphract)
* [Source Code License](https://github.com/DAK404/Cataphract/LICENSE)
* [End User License Agreement](https://github.com/DAK404/Cataphract/EULA)
* [Developer Documentation](https://dak404.github.io/Cataphract/docs/DeveloperDocumentation/index.html)
* [Internal Documentation](https://dak404.github.io/Cataphract/docs/InternalDocumentation/index.html)
* Latest Release --> To be updated


[Back To Top](#table-of-contents)

---

# Introduction

Nion Cataphract is a modular, cross-platform and easy-to-use shell written in Java. It has a modular design, providing sufficient APIs to develop applications alongside or on top of Cataphract.

[Back To Top](#table-of-contents)

---

# Getting Started

This will guide the user to download the repository and build the source code.

> ⚠️ **ATTENTION** - This guide assumes that all the prerequisite software has been downloaded, installed and configured correctly and will not cover the same.

Clone the Cataphract repository from GitHub to your local machine by navigating to the preferred directory and running the following command in your preferred terminal:

> ℹ️ **INFORMATION** - Usually, the GitHub repository has 2 branches: **Main** and **Development**. The **Main** repository contains code that is finalized and tested, whereas the **Development** If you want to download the latest code (which may be untested and/or incomplete), please clone from the **Development** branch. 

```bash
git clone https://github.com/DAK404/Cataphract.git
```

Once cloned, the user will need to navigate into the directory by running the following command:

```bash
cd Cataphract
```

After navigating to the `Cataphract` directory, the directory structure should look like this:

```
- PROJECT_ROOT_DIR -
|
|-- .github/
|-- .vscode/
|-- docs/
|-- Source/
|-- Tools/
```

Now, the repository has been cloned from GitHub and is ready to be built on the user's local system.

For more information on building and testing, please see --> INSERT PAGE HERE

[Back To Top](#table-of-contents)

--- 

# Documentation

This project has 2 levels of documentation available:

* Developer Documentation
* Internal Documentation

## Developer Documentation

> ℹ️ **INFORMATION** - This documentation provides an overview of the APIs, which means that the user can understand the structure without having to read through the implementation details.

The Developer Documentation contains just enough information to developers who would want to write their own code by utilizing the available APIs in Cataphract. It contains information regarding the public classes, fields, constructors and methods only.


## Internal Documentation

> ℹ️ **INFORMATION** - This documentation details the complete structure of the program where users who are interested in the implementation and inner workings.

The Internal Documentation contains full information to developers who would want to understand, improve and optimize the Cataphract code. It contains information regarding private, protected and public classes, fields, constructors and methods.

[Back To Top](#table-of-contents)

---

