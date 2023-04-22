package org.legacy.customer;

import org.legacy.Database;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO{
    @Override
    public Customer get(int id) throws SQLException {
        return null;
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLException {
        Connection connection = Database.getConnection();
        ArrayList<Customer> allCustomers = new ArrayList<>();
        try {
            String allCustomersSQL = "SELECT * FROM CUSTOMER " +
                    "ORDER BY ID;";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(allCustomersSQL);
            while (rs.next()) {
                int customerId = rs.getInt("ID");
                String name = rs.getString("NAME");
                Integer carId = rs.getInt("RENTED_CAR_ID");
                if (carId == 0)
                    carId = null;
                Customer customer = new Customer(customerId, name, carId);
                allCustomers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong during printing allCustomers");
        } finally {
            connection.close();
        }
        return allCustomers;
    }

    @Override
    public int add(Customer customer) throws SQLException {
        Connection connection = Database.getConnection();
        try {
            if (nameExists(customer))
                return 1;
            String insertCustomerSQL = "INSERT INTO CUSTOMER (NAME) " +
                    "VALUES (?);";
            PreparedStatement ps = connection.prepareStatement(insertCustomerSQL);
            ps.setString(1, customer.getName());
            ps.executeUpdate();
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            System.out.println("Something went wrong during adding a customer. Please try another name.");
        } finally {
            connection.close();
        }
        return 0;
    }

    public int update(Customer customer) throws SQLException {
        Connection connection = Database.getConnection();
        try {
            boolean customerExists = nameExists(customer);
            if (customerExists)
                return 1;
            String updateCustomerSQL = "UPDATE CUSTOMER SET NAME = ? WHERE ID = ?;";
            PreparedStatement ps = connection.prepareStatement(updateCustomerSQL);
            ps.setString(1, customer.getName());
            ps.setInt(2, customer.getId());
            ps.executeUpdate();
            System.out.println("Customer data successfully updated");
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("There was an error during customer data update.");
        } finally {
            connection.close();
        }
        return 0;
    }

    public boolean nameExists(Customer customer) throws SQLException {
        Connection connection = Database.getConnection();
        try {
            String checkNameSQL = "SELECT ID FROM CUSTOMER WHERE NAME = ?;";
            PreparedStatement ps = connection.prepareStatement(checkNameSQL);
            ps.setString(1, customer.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("This customer name already exists, please try another name");
                return true;
            }
        } catch (SQLException e) {
            System.out.println();
        } finally {
            connection.close();
        }
        return false;
    }

    @Override
    public int updateCarId(Customer customer) throws SQLException {
        Connection connection = Database.getConnection();
        try {
            String updateCarIdSQL = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?;";
            PreparedStatement ps = connection.prepareStatement(updateCarIdSQL);
            if (customer.getRentedCarId() == null)
                ps.setNull(1, java.sql.Types.INTEGER);
            else
                ps.setInt(1, customer.getRentedCarId());
            ps.setInt(2, customer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("There was a error during updating rentedCarId");
        } finally {
            connection.close();
        }
        return 0; // Update customer table, SET RENTED_CAR_ID to null;
    }

    @Override
    public int delete(Customer customer) throws SQLException {
        return 0;
    }
}
