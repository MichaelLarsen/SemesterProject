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
            System.out.println("Fail in BookingMapper - addBooking");
            System.out.println(e.getMessage());
        }
        finally // Skal lukke statement
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - addBooking");
                System.out.println(e.getMessage());
            }
        }
        return guestAdded == newGuestInRoomList.size();
    }
    
}
