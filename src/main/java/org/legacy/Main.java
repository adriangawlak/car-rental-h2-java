package org.legacy;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {

        Database.initDatabase(args);
        UserInterface ui = new UserInterface();

    }
}