package org.legacy;

import java.io.IOException;
import java.sql.SQLException;

// The prepopulated database name is: sampleDb
// You can use it by passing "-databaseName sampleDb" as command line arguments

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {

        Database.initDatabase(args);
        UserInterface ui = new UserInterface();

    }
}