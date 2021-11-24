# libraryservice

### About
libraryservice is a backend for a library management software which will exposed RESTful end points to perform various functions that would usually be performed manually in a library.

The details about the features currently implemented and planned are available in the following sections.

Design documents available in the 'documents' folder.

#### Tech Stack
1. Java 11
2. Spring Boot 2.6.0
3. Spring Web
4. Spring Data JPA
5. Postgres via [ElephantSQL](https://www.elephantsql.com/)
6. Linux (Arch Linux)
7. IDE - IntelliJ


### How to build the project
1. Install JDK 11 as per the instructions for your OS
2. Extract the project files into a folder of your choice
3. Run gradlew to generate the build
   1. On Linux
      1. Open the project folder in a terminal window
      2. Type ```./gradlew bootJar``` and hit enter.
   2. On Windows
      1. Open the project folder in a cmd window
      2. Type ```gradlew.bat bootJar``` and hit enter
4. Navigate to ```build/libs/``` in the current directory to locate the generated executable with the name format as 'libraryservice-\<version_number\>.jar'

### Features

#### Available

#### Future Scope
1. User can view books in library
   1. User can view list of books in the library
   2. User can view the detail of a book in the library
2. User can borrow a book from the library
3. User can borrow a copy of a book from the library
4. User can return books to the library