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
    
    public boolean addCustomerToRoom(Customer customer, Connection con) {
        int rowsInserted = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "insert into Customers values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, customer.getCustomerId());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setString(4, customer.getStreet());
            statement.setString(5, customer.getZipcode());
            statement.setString(6, customer.getCity());
            statement.setString(7, customer.getCountry());
            statement.setString(8, customer.getEmail());
            statement.setInt(9, customer.getPrivatePhone());
            statement.setInt(10, customer.getWorkPhone());
            rowsInserted = statement.executeUpdate(); //rowsInserted bliver = 1, hvis Update går igennem
        }
        catch (SQLException e) {
            System.out.println("Fail in CustomerMapper - addCustomerToRoom");
            System.out.println(e.getMessage());
        }
        finally // Skal lukke statement
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in CustomerMapper - addCustomerToRoom");
                System.out.println(e.getMessage());
            }
        }
        return rowsInserted == 1; //hvis dette passer returneres true ellers false  
    }

    public ArrayList<Customer> getCustomersFromDB(Connection con) {
        Customer customer = null;
        ArrayList<Customer> customerList = new ArrayList<>();
        String SQLString = "select * from CUSTOMERS";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                customer = new Customer(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getInt(9),
                        rs.getInt(10));
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

    boolean updateCustomerDB(Customer customer, Connection con) {
        int rowsUpdated = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "update CUSTOMERS"
                + " set first_name = ?, last_name = ?, street = ?, zipcode = ?, city = ?, country =?, email = ?, private_phone = ?, work_phone = ?,"
                + " agency = ?, check_in_date = ?, no_of_nights = ?, room_no = ?"
                + " where customer_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getStreet());
            statement.setString(4, customer.getZipcode());
            statement.setString(5, customer.getCity());
            statement.setString(6, customer.getCountry());
            statement.setString(7, customer.getEmail());
            statement.setInt(8, customer.getPrivatePhone());
            statement.setInt(9, customer.getWorkPhone());
            statement.setInt(10, customer.getCustomerId());
            rowsUpdated = statement.executeUpdate(); //rowsInserted bliver = 1, hvis Update går igennem
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
        return rowsUpdated == 1; //hvis dette passer returneres true ellers false
    }
    
}
