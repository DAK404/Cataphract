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

Readme - Package Level Documentation.

Package: Cataphract.API.Dragon
Author: DAK404 (https://github.com/DAK404)

-[ INTRODUCTION ]-

This Package provides utilities to create, manage and delete user accounts. Also provides a
class for authenticating user credentials.

-[ DETAILS ]-

The classes currently implemented in this package are:

    * AccountCreate.java
    * AccountDelete.java
    * AccountModify.java
    * Login.java

The following details the functionality of each class:

* AccountCreate.java - Helps in creating a new user account. Accepts the account name,
username, password, security key, PIN of the new account. Once valid data and credentials
are provided, the data is then written to the database.

* AccountDelete.java - Helps in deleting an existing user account. Also allows administrators
to delete other standard and administrator accounts.

* AccountModify.java - Helps in updating user credentials. Also allows administrators to
promote standard user to an administrator or to demote administrators to a standard user.

* Login.java - Helps the programmers to authenticate user credentials. Also helps to check
the existence of a user, privileges of the user, retrieves the name of the user and
retrieves the user's PIN (in a hashed format).

The implementation is modular and additional functionalities can be built by providing the
method retrieveDatabaseEntry() with the correct arguments. Please check the documentation
for a detailed view of the method, and please check the source code for the implementation
of the functionalities.


-[ IMPORTANT LINKS ]-

EasyGuide - Nion Directory Specification :
Developer Program Documentation          : https://dak404.github.io/Cataphract/DeveloperDocumentation/index.html
Internal Program Documentation           : https://dak404.github.io/Cataphract/InternalDocumentation/index.html
Website                                  : https://dak404.github.io/Cataphract
GitHub Repository                        : https://github.com/DAK404/Cataphract

============================================================================================