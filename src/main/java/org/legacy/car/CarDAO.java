package org.legacy.car;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CarDAO {
    Car get(int id) throws SQLException;
    ArrayList<Car> getAll(int companyId) throws SQLException;
    int add(Car car) throws SQLException;
    int update(Car car) throws SQLException;
    int delete(Car car) throws SQLException;

    boolean isAvailable(Car car) throws SQLException;
}