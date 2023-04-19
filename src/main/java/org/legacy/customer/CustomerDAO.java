package org.legacy.customer;

import org.legacy.DAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO extends DAO<Customer> {
    Customer get(int id) throws SQLException;
    ArrayList<Customer> getAll() throws SQLException;
    int add(Customer customer) throws SQLException;
    int updateCarId(Customer customer) throws SQLException;
    int delete(Customer customer) throws SQLException;
}

