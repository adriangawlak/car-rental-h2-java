package org.legacy.company;

import org.legacy.Database;

import java.sql.*;
import java.util.ArrayList;

public class CompanyDAOImpl implements CompanyDAO{
    @Override
    public Company get(int id) throws SQLException {
        Connection connection = Database.getConnection();
        Company company = null;
        try {
            String selectCompanySQL = "SELECT * FROM COMPANY WHERE ID = ?";
            PreparedStatement ps = connection.prepareStatement(selectCompanySQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("NAME");
                company = new Company(id, name);
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong during checking the company in the database.");
        }
        connection.close();
        return company;
    }

    @Override
    public ArrayList<Company> getAll() throws SQLException {
        Connection connection = Database.getConnection();
        ArrayList<Company> allCompanies = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            String allCompaniesSQL = "SELECT * FROM COMPANY " +
                    "ORDER BY ID;";
            ResultSet rs = st.executeQuery(allCompaniesSQL);
            while (rs.next()) {
                String name = rs.getString("NAME");
                int id = rs.getInt("ID");
                Company company = new Company(id, name);
                allCompanies.add(company);
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong during printing the allCompanies list.");
        }
        connection.close();
        return allCompanies;
    }

    @Override
    public int add(Company company) throws SQLException {
        //        public static void addCompanyToDB(String companyName) throws SQLException {
        Connection connection = Database.getConnection();
        try {
            String insertCompanySQL = "INSERT INTO COMPANY (NAME) " +
                    "VALUES ('" + company.getName() + "');";
            PreparedStatement ps = connection.prepareStatement(insertCompanySQL);
            ps.executeUpdate();
            System.out.println("The company was created!");
        } catch (SQLException e) {
            System.out.println("Something went wrong during adding a company to a database");
            connection.close();
        }
        return 0;
    }

    @Override
    public int updateCarId(Company company) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Company company) throws SQLException {
        return 0;
    }

}
