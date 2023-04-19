package org.legacy;

import org.legacy.car.Car;
import org.legacy.car.CarDAO;
import org.legacy.car.CarDAOImpl;
import org.legacy.company.Company;
import org.legacy.company.CompanyDAO;
import org.legacy.company.CompanyDAOImpl;
import org.legacy.customer.Customer;
import org.legacy.customer.CustomerDAO;
import org.legacy.customer.CustomerDAOImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UserInterface {
    Scanner scan = new Scanner(System.in);
    private CompanyDAO companyDAO;
    private CarDAO carDAO;
    private CustomerDAO customerDAO;

    public UserInterface () throws SQLException {
        companyDAO = new CompanyDAOImpl();
        carDAO = new CarDAOImpl();
        customerDAO = new CustomerDAOImpl();
        mainMenu();
    }

    // this shows main menu
    public void mainMenu() throws SQLException {
        boolean menuOn = true;
        while (menuOn) {
            System.out.println("\n1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            int choice = scan.nextInt();

            switch (choice) {
                case 1 -> managerMenu();
                case 2 -> customerList();
                case 3 -> createCustomer();
                case 0 -> menuOn = false;
            }
        }
    }

    // this shows manager menu
    private void managerMenu() throws SQLException {
        boolean managerOn = true;
        while (managerOn) {
            System.out.println("\n1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scan.nextInt();

            switch (choice) {
                case 1 -> {
                    Company company = companyList();
                    if (company != null)
                        companyMenu(company);
                }
                case 2 -> createCompany();
                case 0 -> {
                    managerOn = false;
                }
            }
        }
    }

    // this prints list of companies sorted by IDs, indexed from 1 or info that the list is empty
    // function returns company chosen from the list
    private Company companyList() throws SQLException {
        ArrayList<Company> companies = companyDAO.getAll();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return null;
        } else {
            System.out.println("\nChoose a company:");
            companies.forEach(System.out::println);
            System.out.println("0. Back");
            System.out.print("> ");

            int choice = scan.nextInt();
            Company chosenCompany;
            if (choice == 0)
                return null;
            else {
                chosenCompany = companies.get(choice - 1);
                return chosenCompany;
            }
        }
    }

    private void createCompany() throws SQLException {
        System.out.println("Enter the company name:");
        System.out.print("> ");
        scan.nextLine();
        String companyName = scan.nextLine();
        Company company = new Company(companyName);
        companyDAO.add(company);
    }

    // this shows company menu
    private void companyMenu(Company company) throws SQLException {
        boolean companyOn = true;
        System.out.println("\n'" + company.getName() + "' company");
        while (companyOn) {
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scan.nextInt();
            switch (choice) {
                case 1 -> {
                    HashMap availableCars = carList(company.getId());
                    if (availableCars == null)
                        System.out.println("The car list is empty!\n");
                }
                case 2 -> createCar(company);
                case 0 -> companyOn = false;
            }
        }
    }

    // this shows main menu
    public HashMap<Integer, Car> carList(int companyId) throws SQLException {
        ArrayList<Car> cars = carDAO.getAll(companyId);
        HashMap<Integer, Car> carList;
        if (cars.isEmpty()) {
            return null;
        } else {
            System.out.println("\nCar list:");
            int count = 1;
            carList = new HashMap<>();
            for (Car car : cars) {
                carList.put(count, car);
                System.out.println(count++ + ". " + car.getName());
            }
            System.out.println("0. Back");
            System.out.println();
        }
        return carList;
    }

    public void createCar(Company company) throws SQLException {
        System.out.println("\nEnter the car name:");
        System.out.print("> ");
        scan.nextLine();
        String carName = scan.nextLine();
        Car car = new Car(carName, company);
        carDAO.add(car);
    }

    // This shows list of customers saved in a database
    public void customerList() throws SQLException {
        ArrayList<Customer> customers = customerDAO.getAll();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            System.out.println("\nCustomer list:");
            customers.forEach(System.out::println);
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scan.nextInt();
            Customer chosenCustomer;
            if (choice == 0)
                return;
            else {
                chosenCustomer = customers.get(choice - 1);
                customerMenu(chosenCustomer);
            }
        }
    }

    // This shows customer menu
    public void customerMenu(Customer customer) throws SQLException {
        boolean menuOn = true;
        while (menuOn) {
            System.out.println("\n1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scan.nextInt();
            switch (choice) {
                case 1 -> rentCar(customer);
                case 2 -> returnCar(customer);
                case 3 -> showMyCar(customer);
                case 0 -> {
                    return;
                }
            }
        }

    }

    public void createCustomer() throws SQLException {
        System.out.println("\nEnter the customer name:");
        System.out.print("> ");
        scan.nextLine();
        String customerName = scan.nextLine();
        Customer customer = new Customer(customerName);
        customerDAO.add(customer);
    }

    // This prints all rental companies and their available cars
    // The function marks chosen car as rented in the database
    public void rentCar(Customer customer) throws SQLException {
        // -> print companies to choose (if empty company list print info) and return to customer menu
        // Check if the customer has rented a car
        if (customer.getRentedCarId() != null) {
            System.out.println("You've already rented a car!");
        } else {
            // check if the customer pressed "0. Back"
            Company chossenCompany = companyList();
            if (chossenCompany == null)
                return;
            HashMap availableCars = carList(chossenCompany.getId());
            if (availableCars == null) {
                System.out.println("No available cars in the " + chossenCompany.getName() + " company.");
            } else {
                System.out.print("> ");
                int choice = scan.nextInt();
                if (choice == 0)
                    return;
                Car chosenCar = (Car) availableCars.get(choice);
                System.out.println("You rented '" + chosenCar.getName() + "'");
                customer.setRentedCarId(chosenCar.getId());
                customerDAO.updateCarId(customer);
            }
        }
    }

    public void returnCar(Customer customer) throws SQLException {
        // set column rentedCarId to null;
        if (customer.getRentedCarId() == null) {
            System.out.println("\nYou didn't rent a car!");
        } else {
            customer.setRentedCarId(null);
            customerDAO.updateCarId(customer);
            System.out.println("\nYou've returned a rented car!");
        }

    }

    // This prints info about currently rented car
    public void showMyCar(Customer customer) throws SQLException {
        Integer carId = customer.getRentedCarId();
        if (carId == null)
            System.out.println("\nYou didn't rent a car!");
        else {
            Car rentedCar = carDAO.get(carId);
            System.out.println("\nYour rented car:");
            System.out.println(rentedCar.getName());
            System.out.println("Company:");
            int companyId = rentedCar.getCompanyId(); // check company name for compID
            String companyName = companyDAO.get(companyId).getName();
            System.out.println(companyName);
        }
    }



}