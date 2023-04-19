package org.legacy.car;

import org.legacy.company.Company;

public class Car {
    private int id;
    private String name;
    private int companyId;

    public Car (int id, String name, int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public Car (String name, Company company) {
        this.name = name;
        this.companyId = company.getId();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
