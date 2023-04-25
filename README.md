# Car Rental System
This is a Java application for managing car rental companies and their customers.  
It provides a simple interface for adding and updating companies, cars, and customers, as well as renting and returning cars.  
All operations are stored in a database. You can simply start using the program and it will create an empty database.  
This application uses Java with manually configured JDBC requests, SQL statements and embedded H2 database.


### Sample Database
To quickly test all functionalities I provided a <ins>prepopulated sample database</ins>, which you can load when running the program.  
You can use it by passing "-databaseName sampleDb" as command line arguments in your IDE or in the command line.

## Getting Started
To run this application, you will need to have Java and Maven or an IDE installed on your system, 
depending on how do you want to run the application.

## Running the Application

### In an IDE
You can run the application in an IDE such as IntelliJ IDEA or Eclipse by running the `Main` class. 
Once the application is running, you will be presented with a menu of options that allow you to interact with the application.  
Don't forget to pass "-databaseName sampleDb" as CLI arguments if you want to use a sample database.


### On the Command Line
To run the application from the command line, you can build the application yourself using Maven or use provided `.jar` file.  

The file location is: `carRentalJDBC/out/artifacts/carRentalJDBC_jar/carRentalJDBC.jar`  

Go to that directory and start the program using the formula:  
`java -jar carRentalJDBC.jar`  
or  
`java -jar carRentalJDBC.jar -databaseName sampleDB` to use a prepopulated database.

