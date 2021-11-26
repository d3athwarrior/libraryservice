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
5. Postgres via [ElephantSQL](https://www.elephantsql.com/) (Service Limitation: Data size 20MB, Max Connection: 1)
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
4. Navigate to ```build/libs/``` in the current directory to locate the generated executable with the name format as '
   libraryservice-\<version_number\>.jar'

### How to run the project

1. Database installation
   1. If you decide to use the database details already provided the application.yml file
   2. (WARNING: NOT YET SUPPORTED)If you want to install your own database instance(postgres, of course) and change the
      configurations then read on else skip to step 2
      1. Navigate to 'databasescripts' folder and execute 'database_setup.sql'
         1. For dummy data execute app_user_dummy_data.sql, book_dummy_data.sql and issue_dummy_data.sql in that order
         2. \<More steps for custom application.yml needs to be added here\>
2. Make sure you have read and performed the steps of 'How to build the project'
3. Open a command window where the jar file is and enter ```java -jar libraryservice-\<version_number\>.jar```

### How to run the tests

1. Navigate to the project root directory
2. As per your OS (Windows or Linux) run either ```gradlew.bat test``` or ```./gradlew test```

### Features

#### Available

1. User can view books in library
   1. ser can view list of books in the library
2. User can borrow a book from the library
3. User can borrow a copy of a book from the library

#### Future Scope

1. User can view books in library
   1. ~~User can view list of books in the library~~
   2. User can view the detail of a book in the library
2. ~~User can borrow a book from the library~~
3. ~~User can borrow a copy of a book from the library~~
4. User can return books to the library
5. User Login