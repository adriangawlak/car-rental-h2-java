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
import java.util.InputMismatchException;
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
        System.out.println("\nWelcome to Car Rental System!");
        boolean menuOn = true;
        while (menuOn) {
            System.out.println("\nChoose menu by typing a number and press enter");
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            System.out.print("> ");
            int choice = scanNumber();

            switch (choice) {
                case 1 -> managerMenu();
                case 2 -> chooseCustomer();
                case 3 -> createCustomer();
                case 0 -> menuOn = false;
                default -> System.out.println("Invalid input, please enter a number corresponding with your choice");
            }
        }
    }

    // this shows manager menu
    private void managerMenu() throws SQLException {
        boolean managerOn = true;
        while (managerOn) {
            System.out.println("\n1. Company list");
            System.out.println("2. Create a company");
            System.out.println("3. Delete a company");
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scanNumber();

            switch (choice) {
                case 1 -> {
                    Company company = chooseCompany();
                    if (company != null)
                        companyMenu(company);
                }
                case 2 -> createCompany();
                case 3 -> deleteCompany();
                case 0 -> managerOn = false;
                default -> System.out.println("Invalid input, please enter digit corresponding with your choice");
            }
        }
    }

    // this prints list of companies sorted by IDs, indexed from 1 or info that the list is empty
    // function returns company chosen from the list
    private Company chooseCompany() throws SQLException {
        ArrayList<Company> companies = companyDAO.getAll();
        HashMap<Integer, Company> companiesList;
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return null;
        } else {
            System.out.println("\nChoose a rental company:");
//            HashMap keeps list in order, independent of companyId in the database.
            int listCount = 1;
            companiesList = new HashMap<>();
            for (Company company : companies) {
                companiesList.put(listCount, company);
                System.out.println(listCount++ + ". " + company.getName());
            }
            System.out.println("0. Back");
            System.out.print("> ");

            int choice = scanNumber();
            Company chosenCompany;
            while (choice < 0 || choice > companiesList.size()) {
                System.out.println("Please enter a number corresponding with a company from the list.");
                choice = scanNumber();
            }
            if (choice == 0)
                return null;
            else {
                chosenCompany = companiesList.get(choice);
                return chosenCompany;
            }
        }
    }

    private void createCompany() throws SQLException {
        System.out.println("Enter company name:");
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
            System.out.println("3. Delete a car");
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scanNumber();

            switch (choice) {
                case 1 -> {
                    Car car = chooseCar(company.getId(), false);
                    if (car != null)
                        System.out.println(car.toString() + "\n");
                }
                case 2 -> createCar(company);
                case 3 -> deleteCar(company);
                case 0 -> companyOn = false;
                default -> System.out.println("Invalid input, please enter digit corresponding with your choice.");
            }
        }
    }

    private void deleteCompany() throws SQLException {
        String message = companyDAO.getAll().isEmpty()
                ? "\nNo companies to delete." : "\nEnter a number and press enter to delete";
        System.out.println(message);
        Company company = chooseCompany();
        if (company != null) {
            System.out.println("Are you sure you want to delete the company " + company.getName() + " with all its cars?");
            System.out.println("Type \"y\" or \"yes\" to confirm. To cancel type \"n\" or \"no\"");
            String confirmation = scan.next().toLowerCase();
            if (confirmation.equals("yes") || confirmation.equals("y"))
                companyDAO.delete(company);
            else
                System.out.println("Deleting canceled, please try again.");
        }
    }

    // this prints list of available cars and returns a chosen car
    public Car chooseCar(int companyId, boolean showAvailable) throws SQLException {
        ArrayList<Car> cars = carDAO.getAll(companyId, showAvailable);
        HashMap<Integer, Car> carsList;
        String message = showAvailable ? "List of currently available cars:" : "List of all cars in the company:";
        if (cars.isEmpty()) {
            System.out.println("\nNo registered cars in the chosen company.\n");
            return null;
        } else {
            System.out.println("\n" + message);
            int count = 1;
            carsList = new HashMap<>();
            // This keeps numbers in order, starting from 1, since ID in the DB isn't ordered
            for (Car car : cars) {
                carsList.put(count, car);
                System.out.println(count++ + ". " + car.getName());
            }
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scanNumber();
            Car chosenCar;
            while (choice < 0 || choice > carsList.size()) {
                System.out.println("Please enter a number corresponding with a car from the list");
                System.out.print("> ");
                choice = scanNumber();
            }
            if (choice == 0) {
                System.out.println();
                return null;
            } else {
                chosenCar = carsList.get(choice);
                return chosenCar;
            }
        }
    }

    public void createCar(Company company) throws SQLException {
        System.out.println("\nEnter the car name:");
        System.out.print("> ");
        scan.nextLine();
        String carName = scan.nextLine();
        Car car = new Car(carName, company);
        carDAO.add(car);
    }

    private void deleteCar(Company company) throws SQLException {
        Car car = null;
        if (!carDAO.getAll(company.getId(), true).isEmpty()) {
            System.out.println("\nEnter a number of the car that you want to delete");
            car = chooseCar(company.getId(), true);
        }
        if (car != null) {
//            int choice = scanDigit();
            System.out.println("Are you sure you want to delete the car " + car.getName() + "?");
            System.out.println("Type \"y\" or \"yes\" to confirm, to cancel type \"n\" or \"no\"");
            System.out.print("> ");
            String confirmation = scan.next().toLowerCase();
            if (confirmation.equals("yes") || confirmation.equals("y"))
                carDAO.delete(car);
            else
                System.out.println("\nDeleting canceled, please try again.\n");
        } else
            System.out.println("\nThere are no cars to delete!\n");
    }

    // This shows list of customers saved in a database
    public void chooseCustomer() throws SQLException {
        ArrayList<Customer> customers = customerDAO.getAll();
        if (customers.isEmpty()) {
            System.out.println("\nThe customer list is empty. Create a customer account to log in.");
        } else {
            System.out.println("\nChoose a customer account by typing a number and press enter");
            customers.forEach(System.out::println);
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scanNumber();
            Customer chosenCustomer;
            if (choice == 0)
                return;
            else if (choice < 0 || choice > customers.size())
                System.out.println("Wrong number, please try again.");
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
            System.out.println("\nWelcome " + customer.getName());
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("4. Change customer data");
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scanNumber();

            switch (choice) {
                case 1 -> rentCar(customer);
                case 2 -> returnCar(customer);
                case 3 -> showMyCar(customer);
                case 4 -> changeCustomerData(customer);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid input, please enter a number corresponding with your choice.");
            }
        }

    }

    public void createCustomer() throws SQLException {
        System.out.println("\nEnter customer name:");
        System.out.print("> ");
        scan.nextLine();
        String customerName = scan.nextLine();
        System.out.println("Do you want to save the customer '" + customerName + "'?");
        System.out.println("Type \"y\" or \"yes\" to save. Type \"n\" or \"no\" to cancel");
        String confirmation = scan.nextLine().toLowerCase();
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            Customer customer = new Customer(customerName);
            customerDAO.add(customer);
        } else {
            System.out.println("Saving canceled");
        }
    }

    // This prints all rental companies and their available cars
    // The function marks chosen car as rented in the database
    public void rentCar(Customer customer) throws SQLException {
        // -> print companies to choose (if empty company list print info and return to customer menu)
        // Check if the customer has rented a car, only one rental allowed
        if (customer.getRentedCarId() != null) {
            System.out.println("You've already rented a car!");
        } else {
            // check if the customer pressed "0. Back"
            Company chosenCompany = chooseCompany();
            if (chosenCompany == null)
                return;
            Car chosenCar = chooseCar(chosenCompany.getId(), true);
            // check if the customer typed a correct number
            if (chosenCar == null) {
                return;
            } else {
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

    private void changeCustomerData(Customer customer) throws SQLException {
        System.out.println("Enter new customer name:");
        System.out.print("> ");
        scan.nextLine();
        String newName = scan.nextLine();
        String previousName = customer.getName();
        customer.setName(newName);
        int result = customerDAO.update(customer);
        // revert a name change in case of an error
        if (result == 1)
            customer.setName(previousName);
    }

    // This takes input that needs to be a number and returns it, else a message is printed for incorrect input
    public int scanNumber() {
        int choice = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                choice = scan.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number corresponding with your choice");
                scan.next();
            }
        }
        return choice;
    }


}