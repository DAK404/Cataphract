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


-[ HISTORY ]-

While working on Zen Quantum, it was essential to have a class that handled authentication.
Therefore a new package called "Anima" was created. Anima was aimed to contain all user
account management utilities. The following functions were implemented:

    * AddUser.java
    * CreateDB.java
    * LoginAPI.java

AddUser.java -> Contained code to add a user to the database. The code was non-modular and
did not effectively handle invalid credentials.

CreateDB.java -> Contained code to create a database file, a table to hold the user details.
CreateDB was called by the setup program.

LoginAPI.java -> Contained code to handle credential authentication. It was inefficient
since it required 3 variables to check the validity of the credentials.

In Mosaic, UpdateCredentials.java was introduced. It was aimed to update the user
credentials. Again, the code was non-modular, which made it very difficult to work on,
although it provided the required functionality of updating credentials.

In Truncheon, package was renamed to Dragon. The class was renamed to ModifyAccount.java.
With the privilege system in place, it was now necessary to check for the privilege to
promote or demote users. There was no dedicated method that would return the privilege of
the user, therefore, it had to be manually queried and then stored in a variable. It is also
notable that DeleteAccount.java was created which helped in deleting accounts. With the
privilege system, an administrator could now delete other user accounts.

In Truncheon: Katana, the LoginAPI, now renamed to LoginAuth.java, had an option to query
and return privileges therefore it was easier to implement features without rewriting the
logic to query and acquire the privilege value from the database.

Currently, the class is named as Login.java for a better nomenclature. It is also modular,
therefore the code is easier to read and develop upon.


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