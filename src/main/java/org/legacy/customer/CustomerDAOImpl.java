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
        }
        connection.close();
        return allCustomers;
    }

    @Override
    public int add(Customer customer) throws SQLException {
        Connection connection = Database.getConnection();
        try {
            String insertCustomerSQL = "INSERT INTO CUSTOMER (NAME) " +
                    "VALUES (?);";
            PreparedStatement ps = connection.prepareStatement(insertCustomerSQL);
            ps.setString(1, customer.getName());
//            ps.setInt(2, customer.getRentedCarId());
            ps.executeUpdate();
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            System.out.println("Something went wrong during adding a customer.");
        }
        return 0;
    }

    @Override
    public int updateCarId(Customer customer) throws SQLException {
        Connection connection = Database.getConnection();
        try {
            String updateCustomerSQL = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?;";
            PreparedStatement ps = connection.prepareStatement(updateCustomerSQL);
            if (customer.getRentedCarId() == null)
                ps.setNull(1, java.sql.Types.INTEGER);
            else
                ps.setInt(1, customer.getRentedCarId());
            ps.setInt(2, customer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("There was a error during updating rentedCarId");
        }
        return 0; // Update customer table, SET RENTED_CAR_ID to null;
    }

    @Override
    public int delete(Customer customer) throws SQLException {
        return 0;
    }
}
