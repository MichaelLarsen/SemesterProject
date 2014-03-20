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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Seb
 */
public class Mapper {

    public boolean addCustomerToRoom(Customer currentCustomer, Connection con) {
        int rowsInserted = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "insert into Customers values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, currentCustomer.getCustomerId());
            statement.setString(2, currentCustomer.getFirstName());
            statement.setString(3, currentCustomer.getLastName());
            statement.setString(4, currentCustomer.getStreet());
            statement.setString(5, currentCustomer.getZipcode());
            statement.setString(6, currentCustomer.getCity());
            statement.setString(7, currentCustomer.getCountry());
            statement.setString(8, currentCustomer.getEmail());
            statement.setString(9, currentCustomer.getAgency());
            statement.setString(10, currentCustomer.getCheckInDate());
            statement.setInt(11, currentCustomer.getNumberOfNights());
            statement.setInt(12, currentCustomer.getRoomNo());
            statement.setDouble(13, currentCustomer.getDebt());
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

    public Room getRoomsFromDB(Connection con) {
        Room room = null;
        String SQLString = "select * from Rooms";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                room = new Room(rs.getInt(1), rs.getString(2), rs.getInt(3));
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
            } catch (SQLException e) {
                System.out.println("Fail in mapper - getRoomsFromDB");
                System.out.println(e.getMessage());
            }
        }
        return room;

    }
}
