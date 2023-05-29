# Java SQL Database ETL Project
This readme provides instructions on how to run the Java code,
which involves generating tables, parsing XML and CSV data, storing the data into hashmaps,
generating queries, and filling multiple types of entities in a schema.
The program utilizes Maven for build management and requires specific data file locations.

-> See the code comments for further information.
## This Java program does the following:
- Loads a database schema including constraints and indexes into a postgresDB (local database)
- Parses XML and CSV data plus performs a cleanup for preprocessing (adding missing special characters based on an algorithm)
- Loads the data from XML and CSV into the created local database schema
- Generates a failure log including rejected data based on various checks
- Generates logs for debugging purposes

## Prerequisites
Before running the Java program, ensure that you have the following prerequisites installed:

- Local postgres database (change credentials if necessary in the main method database constructor)
- Java Development Kit (JDK)
- IntelliJ
- Maven

## Run the code

1. Copy the respective XML files in
   -> ./data/raw/xml/leipzig_transformed.xml
   -> ./data/raw/xml/dresden.xml
   -> ./data/raw/xml/categories
   (keep naming or specify in ETL Process Method)
2. Go to root directory where the .pom file lives and run ``mvn clean install`` to install external libs
3. Go to ./src/main/java/ and run the Main Class "ETLProcess" in IntelliJ directly

Note: If the database connection fails, make sure to specify the right credentials in the Main Class postgres connector construction.

## Failure Log
The program generates a failure log that includes rejected data based on various checks.
This log provides information about the data that failed to meet certain criteria or validations.
It is generated at the end of the ETL process and outputted in the terminal.

The failure log for the handover is given via E-Mail to our supervisor.
## Logging
The program generates logs for debugging purposes.
By default, the logs are stored in the ./logs/ directory. One can review these logs to troubleshoot any issues or track the execution flow of the program.
Attention: These logs are simply for debugging purposes.



