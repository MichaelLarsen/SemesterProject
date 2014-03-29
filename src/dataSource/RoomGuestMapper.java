/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dataSource;

import domain.RoomGuest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class RoomGuestMapper {

    public boolean addGuestToRoom(ArrayList<RoomGuest> newGuestInRoomList, Connection con) {
        int guestAdded = 0;
        String SQLString = "insert into ROOM_GUESTS values (?, ?)";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < newGuestInRoomList.size(); i++) {
                statement.setInt(1, newGuestInRoomList.get(i).getCustomerId());
                statement.setInt(2, newGuestInRoomList.get(i).getBookingId());
                guestAdded += statement.executeUpdate(); //bookingAdded bliver = newGuestInRoomList.size(), hvis Update går igennem
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in RoomGuestMapper - addGuestToRoom");
            System.out.println(e.getMessage());
        }
        finally // Skal lukke statement
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in RoomGuestMapper - addGuestToRoom");
                System.out.println(e.getMessage());
            }
        }
        return guestAdded == newGuestInRoomList.size();
    }

    public boolean updateGuestInRoom(ArrayList<RoomGuest> updateGuestInRoomList, Connection con) {
       int rowsUpdated = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "update ROOM_GUESTS"
                + " set booking_id = ?"
                + " where customer_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < updateGuestInRoomList.size(); i++) {
                statement.setInt(1, updateGuestInRoomList.get(i).getBookingId());
                statement.setInt(2, updateGuestInRoomList.get(i).getCustomerId());
            }

            rowsUpdated += statement.executeUpdate(); //rowsInserted bliver = updateGuestInRoomList.size(), hvis Update går igennem
        }
        catch (SQLException e) {
            System.out.println("Fail in RoomGuestMapper - updateGuestInRoom");
            System.out.println(e.getMessage());
        }
        finally // Skal køres efter catch
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in RoomGuestMapper - updateGuestInRoom");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == updateGuestInRoomList.size(); //hvis dette passer returneres true ellers false  
    }
    
}
