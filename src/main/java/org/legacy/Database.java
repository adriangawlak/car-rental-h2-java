package org.legacy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static String dbPath = "jdbc:h2:./src/database/";
    private static String dbName;

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(dbPath + dbName);
        connection.setAutoCommit(true);
        return connection;
    }

    public static void initDatabase(String[] args) throws SQLException {
        dbName = "newDB";

        if (args.length > 0) {
            if (args[0].equals("-databaseFileName")) {
                dbName = args[1];
            }
        }

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Connection connection = getConnection();
        Statement st = connection.createStatement();

        String tableCompanySQL = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " + //IDENTITY(1,1) by default
                "NAME VARCHAR(50) UNIQUE NOT NULL);";
        st.execute(tableCompanySQL);

        String tableCarSQL = "CREATE TABLE IF NOT EXISTS CAR (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(50) UNIQUE NOT NULL, " +
                "COMPANY_ID INT NOT NULL REFERENCES COMPANY(ID) " + //adds foreign key referring to ID col of Company table
                ");"; // ??? - read from a DB?
        st.execute(tableCarSQL);

        String tableCustomerSQL = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(50) UNIQUE NOT NULL, " +
                "RENTED_CAR_ID INT REFERENCES CAR(ID)" +
                ");";
        st.execute(tableCustomerSQL);

        st.close();
        connection.close();

    }


}