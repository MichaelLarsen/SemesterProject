/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dataSource;

import static dataSource.BookingMapper.log;
import domain.BookingDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * BookingDetailMapper klassen håndterer persistering af data mellem programmet 
 * og databasens BOOKING_DETAILS tabel.
 * 
 * @author Andreas, Michael & Sebastian
 */
public class BookingDetailMapper {

    /**
     * Gæster som ønskes tilføjet til en booking, persisteres i databasen.
     * 
     * @param newGuestInRoomList    Liste af BookingDetail-objekter, som inderholder guestId & bookingId.
     * @param con                   Forbindelse til databasen.
     * @return                      TRUE, hvis persistering lykkes.  Rækker tilføjet == antallet af BookingDetails i liste.
     */
    public boolean addGuestToRoom(ArrayList<BookingDetail> newGuestInRoomList, Connection con) {
        int guestAdded = 0;
        String SQLString = "INSERT INTO booking_details VALUES (?, ?)";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < newGuestInRoomList.size(); i++) {
                statement.setInt(1, newGuestInRoomList.get(i).getGuestId());
                statement.setInt(2, newGuestInRoomList.get(i).getBookingId());
                guestAdded += statement.executeUpdate();
                log(newGuestInRoomList.get(i).getBookingId(), BookingMapper.ActionType.ADDED_GUEST, con);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in RoomGuestMapper - addGuestToRoom");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in RoomGuestMapper - addGuestToRoom");
                System.out.println(e.getMessage());
            }
        }
        return guestAdded == newGuestInRoomList.size();
    }

    /**
     * Benyttes ikke pt
     * 
     * TODO slet?
     */
    public boolean updateBookingDetail(ArrayList<BookingDetail> updateGuestInRoomList, Connection con) {
       int rowsUpdated = 0;
        String SQLString = "UPDATE booking_details "
                + "SET booking_id = ? "
                + "WHERE guest_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < updateGuestInRoomList.size(); i++) {
                statement.setInt(1, updateGuestInRoomList.get(i).getBookingId());
                statement.setInt(2, updateGuestInRoomList.get(i).getGuestId());
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
