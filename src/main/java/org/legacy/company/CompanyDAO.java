package org.legacy.company;

import org.legacy.DAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CompanyDAO extends DAO<Company> {

    Company get(int id) throws SQLException;
    ArrayList<Company> getAll() throws SQLException;
    int add(Company company) throws SQLException;
    int updateCarId(Company company) throws SQLException;
    int delete(Company company) throws SQLException;

}
