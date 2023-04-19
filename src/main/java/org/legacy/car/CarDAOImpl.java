package org.legacy.car;

import org.legacy.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarDAOImpl implements CarDAO {
    @Override
    public Car get(int id) throws SQLException {
        Connection connection = Database.getConnection();
        Car car = null;
        try {
            String selectCarSQL = "SELECT * FROM CAR WHERE ID = ?";
            PreparedStatement ps = connection.prepareStatement(selectCarSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("NAME");
                int compId = rs.getInt("COMPANY_ID");
                car = new Car(id, name, compId);
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong during checking a car in the database.");
        }
        connection.close();
        return car;
    }

    @Override
    public ArrayList<Car> getAll(int companyId) throws SQLException {
        Connection connection = Database.getConnection();
        ArrayList<Car> allCars = new ArrayList<>();

        try {
            String allCarsSQL = "SELECT * FROM CAR WHERE COMPANY_ID = ? " +
                    "ORDER BY ID;";
            PreparedStatement ps = connection.prepareStatement(allCarsSQL);
//            Statement st = connection.createStatement(); // v2
            ps.setInt(1, companyId);
//            ResultSet rs = st.executeQuery(allCarsSQL); // v2
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int compId = rs.getInt("COMPANY_ID");
                Car car = new Car(id, name, compId);
                boolean available = this.isAvailable(car);
                if (available)
                    allCars.add(car);
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong during printing allCars list.");
        }
        connection.close();
        return allCars;
    }

    @Override
    public int add(Car car) throws SQLException {
        Connection connection = Database.getConnection();
        try {
            String insertCarSQL = "INSERT INTO CAR (NAME, COMPANY_ID) " +
                    "VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(insertCarSQL);
            ps.setString(1, car.getName());
            ps.setInt(2, car.getCompanyId());
            ps.executeUpdate();
            System.out.println("The car was added!\n");
        } catch (SQLException e) {
            System.out.println("\nSomething went wrong during adding a car.");
        }
        connection.close();
        return 0;
    }

    @Override
    public int update(Car car) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Car car) throws SQLException {
        return 0;
    }

    @Override
    public boolean isAvailable(Car car) throws SQLException {
        Connection connection = Database.getConnection();
        boolean availability = true;
        try {
            String isRentedSQL = "SELECT * FROM CUSTOMER WHERE RENTED_CAR_ID = ?";
            PreparedStatement ps = connection.prepareStatement(isRentedSQL);
            ps.setInt(1, car.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
//                System.out.println("The car " + car.getName() + " is rented out");
                availability = false;
            }
//            else
//                System.out.println("The car " + car.getName() + " is available");
        } catch (SQLException e) {
            System.out.println("Something went wrong during checking the database");
        }
        connection.close();
        return availability;
    }
}
