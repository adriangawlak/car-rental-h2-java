package org.legacy.customer;

public class Customer {
    private int id; // auto-incremented in the database, starting from 1
    private String name;
    private Integer rentedCarId;

    public Customer(String name) {
        this.name = name;
    }

    public Customer(int id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    @Override
    public String toString() {
        return id + ". " +
                name;
    }
}
