/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */

package dataSource;

import static dataSource.BookingMapper.log;
import domain.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            System.out.println("Fail in BookingDetailMapper.addGuestToRoom()");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingDetailMapper.addGuestToRoom()");
                System.out.println(e.getMessage());
            }
        }
        return guestAdded == newGuestInRoomList.size();
    }
    
    /**
     * Henter gæster som bor på booking/room, til visning i bookingoversigt.
     *
     * @param booking       Bookingen vi ønsker gæsterne for.
     * @param con           Forbindelse til databasen.
     * @return              Liste med gæster fra bookingen.
     */
    public ArrayList<Guest> getBookingDetailsFromDB(Booking booking, Connection con) {
        ArrayList<Guest> roomGuestList = new ArrayList<>();
        Guest guest = null;
        String SQLString = "SELECT * "
                + "FROM GUESTS "
                + "JOIN booking_details "
                + "ON guests.guest_id = booking_details.guest_id "
                + "WHERE booking_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, booking.getBookingId());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                guest = new Guest(rs);
                roomGuestList.add(guest);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper.getGuestsInRoom()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.getGuestsInRoom()");
                System.out.println(e.getMessage());
            }
        }
        return roomGuestList;
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
            System.out.println("Fail in BookingDetailMapper.updateGuestInRoom()");
            System.out.println(e.getMessage());
        }
        finally // Skal køres efter catch
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingDetailMapper.updateGuestInRoom()");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == updateGuestInRoomList.size(); //hvis dette passer returneres true ellers false  
    }
}