/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Customer;
import domain.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Seb
 */
public class Mapper {

    public boolean addCustomerToRoom(Customer customer, Connection con) {
        int rowsInserted = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "insert into Customers values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            statement.setString(9, customer.getAgency());
            statement.setDate(10, customer.getCheckInDate());
            statement.setInt(11, customer.getNumberOfNights());
            statement.setInt(12, customer.getRoomNo());
            rowsInserted = statement.executeUpdate(); //rowsInserted bliver = 1, hvis Update går igennem
        }
        catch (SQLException e) {
            System.out.println("Fail in mapper - addCustomerToRoom");
            System.out.println(e.getMessage());
        }
        finally // Skal lukke statement
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in mapper - addCustomerToRoom");
                System.out.println(e.getMessage());
            }
        }
        return rowsInserted == 1; //hvis dette passer returneres true ellers false  
    }

    public ArrayList<Room> getRoomsFromDB(Connection con) {
        Room room = null;
        ArrayList<Room> roomList = new ArrayList<>();
        String SQLString = "select * from ROOMS";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                room = new Room(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4));
                roomList.add(room);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in mapper - getRoomsFromDB");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in mapper - getRoomsFromDB");
                System.out.println(e.getMessage());
            }
        }
        return roomList;
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
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getDate(12),
                        rs.getInt(13),
                        rs.getInt(14));
                customerList.add(customer);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in mapper - getCustomersFromDB");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in mapper - getCustomersFromDB");
                System.out.println(e.getMessage());
            }
        }
        return customerList;
    }

    public boolean updateCustomerDB(Customer customer, Connection con) {
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
            statement.setString(10, customer.getAgency());
            statement.setDate(11, customer.getCheckInDate());
            statement.setInt(12, customer.getNumberOfNights());
            statement.setInt(13, customer.getRoomNo());
            statement.setInt(14, customer.getCustomerId());
            rowsUpdated = statement.executeUpdate(); //rowsInserted bliver = 1, hvis Update går igennem
        }
        catch (SQLException e) {
            System.out.println("Fail in mapper - UpdateCustomerDB");
            System.out.println(e.getMessage());
        }
        finally // Skal køres efter catch
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in mapper - UpdateCustomerDB");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == 1; //hvis dette passer returneres true ellers false  
    }

    public boolean updateRoomDB(Room room, Connection con) {
        int rowsUpdated = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "update ROOMS"
                + " set room_type = ?, price = ?, occupied_beds = ?"
                + " where room_no = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setString(1, room.getRoomTypeString());
            statement.setInt(2, room.getPrice());
            statement.setInt(3, room.getOccupiedBeds());
            statement.setInt(4, room.getRoomNo());
            
            rowsUpdated = statement.executeUpdate(); //rowsInserted bliver = 1, hvis Update går igennem
        }
        catch (SQLException e) {
            System.out.println("Fail in mapper - UpdateRoomDB");
            System.out.println(e.getMessage());
        }
        finally // Skal køres efter catch
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in mapper - UpdateRoomDB");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == 1; //hvis dette passer returneres true ellers false  
    }
}
