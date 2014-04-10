/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class CustomerMapper {

    /**
     * Metoden henter en customer fra databasen via customerId.
     *
     * @param customerId
     * @param con
     * @return customer
     */
    public Customer getGuestFromID(int customerId, Connection con) {
        Customer customer = null;
        String SQLString = "select * from CUSTOMERS "
                + "where customer_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, customerId);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                customer = new Customer(rs);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in CustomerMapper - getGuestFromID");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in CustomerMapper - getGuestFromID");
                System.out.println(e.getMessage());
            }
        }
        return customer;
    }

    public boolean createCustomer(ArrayList<Customer> newCustomerList, Connection con) {
        int customerCreated = 0;
        String SQLString = "insert into CUSTOMERS values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < newCustomerList.size(); i++) {
                statement.setInt(1, getNewCustomerId(con));
                statement.setString(2, newCustomerList.get(i).getFirstName());
                statement.setString(3, newCustomerList.get(i).getLastName());
                statement.setString(4, newCustomerList.get(i).getStreet());
                statement.setString(5, newCustomerList.get(i).getZipcode());
                statement.setString(6, newCustomerList.get(i).getCity());
                statement.setString(7, newCustomerList.get(i).getCountry());
                statement.setString(8, newCustomerList.get(i).getEmail());
                statement.setInt(9, newCustomerList.get(i).getPhone1());
                statement.setInt(10, newCustomerList.get(i).getPhone2());

                customerCreated += statement.executeUpdate(); // customerCreated bliver = newCustomerList.size(), hvis Update går igennem
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in CustomerMapper - createCustomer");
            System.out.println(e.getMessage());
        }
        finally // Skal lukke statement
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in CustomerMapper - createCustomer");
                System.out.println(e.getMessage());
            }
        }
        return customerCreated == newCustomerList.size();
    }

    public int getNewCustomerId(Connection con) {
        int nextCustomerId = 0;

        String SQLString = "select customer_id_seq.nextval from SYS.DUAL";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nextCustomerId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in CustomerMapper - getNewCustomerId");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in CustomerMapper - getNewCustomerId");
                System.out.println(e.getMessage());
            }
        }
        return nextCustomerId;
    }

    public ArrayList<Customer> getCustomersFromDB(Connection con) {
        Customer customer = null;
        ArrayList<Customer> customerList = new ArrayList<>();
        String SQLString = "select * from CUSTOMERS "
                + "order by customer_id desc";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                customer = new Customer(rs);
                customerList.add(customer);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in CustomerMapper - getCustomersFromDB");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in CustomerMapper - getCustomersFromDB");
                System.out.println(e.getMessage());
            }
        }
        return customerList;
    }

    public boolean updateCustomerDB(ArrayList<Customer> dirtyCustomerList, Connection con) {
        int rowsUpdated = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "update CUSTOMERS"
                + " set first_name = ?, last_name = ?, street = ?, zipcode = ?, city = ?, country = ?, email = ?, phone_1 = ?, phone_2 = ?"
                + " where customer_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (Customer customer : dirtyCustomerList) {
                statement.setString(1, customer.getFirstName());
                statement.setString(2, customer.getLastName());
                statement.setString(3, customer.getStreet());
                statement.setString(4, customer.getZipcode());
                statement.setString(5, customer.getCity());
                statement.setString(6, customer.getCountry());
                statement.setString(7, customer.getEmail());
                statement.setInt(8, customer.getPhone1());
                statement.setInt(9, customer.getPhone2());
                statement.setInt(10, customer.getCustomerId());
                rowsUpdated += statement.executeUpdate(); //rowsInserted bliver = 1, hvis Update går igennem
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in CustomerMapper - UpdateCustomerDB");
            System.out.println(e.getMessage());
        }
        finally // Skal køres efter catch
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in CustomerMapper - UpdateCustomerDB");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == dirtyCustomerList.size(); //hvis dette passer returneres true ellers false
    }
}
