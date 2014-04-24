/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Booking;
import domain.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class BookingMapper {

    public ArrayList<Booking> getBookingsFromDB(Connection con) {
        Booking booking = null;
        ArrayList<Booking> bookingList = new ArrayList<>();
        String SQLString = "select * from BOOKINGS "
                + "order by booking_id desc";       
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                booking = new Booking(rs);
                bookingList.add(booking);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getBookingsFromDB");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getBookingsFromDB");
                System.out.println(e.getMessage());
            }
        }
        return bookingList;
    }

    public int getNewBookingId(Connection con) {
        int nextBookingId = 0;

        String SQLString = "select booking_id_seq.nextval from SYS.DUAL";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nextBookingId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getNewBookingId");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getNewBookingId");
                System.out.println(e.getMessage());
            }
        }
        return nextBookingId;
    }

    public boolean addBooking(ArrayList<Booking> newBookingList, Connection con) {
        int bookingAdded = 0;
        String SQLString = "insert into BOOKINGS values (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < newBookingList.size(); i++) {
                int bookingId = getNewBookingId(con);
                statement.setInt(1, bookingId);
                statement.setInt(2, newBookingList.get(i).getBookingOwnerId());
                statement.setInt(3, newBookingList.get(i).getRoomNo());
                statement.setString(4, newBookingList.get(i).getAgency());
                statement.setDate(5, new java.sql.Date(newBookingList.get(i).getCheckInDate().getTime()));
                statement.setDate(6, new java.sql.Date(newBookingList.get(i).getCheckOutDate().getTime()));
                bookingAdded += statement.executeUpdate(); //bookingAdded bliver = newBookingList.size(), hvis Update går igennem
                log(bookingId, ActionType.CREATE, con);
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
        return bookingAdded == newBookingList.size();
    }

    public ArrayList<Guest> getGuestsInBooking(Booking booking, Connection con) {
        ArrayList<Guest> roomGuestList = new ArrayList<>();
        Guest guest = null;
        String SQLString = "SELECT * "
                + "FROM GUESTS "
                + "JOIN BOOKING_DETAILS "
                + "ON GUESTS.guest_id = BOOKING_DETAILS.guest_id "
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
            System.out.println("Fail in BookingMapper - getGuestsInRoom");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getGuestsInRoom");
                System.out.println(e.getMessage());
            }
        }
        return roomGuestList;
    }

    public boolean updateBooking(ArrayList<Booking> updateBookingList, Connection con) {
        int rowsUpdated = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "update BOOKINGS"
                + " set booking_owner = ?, room_no = ?, agency = ?, check_in_date = ?, check_out_date = ?"
                + " where booking_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < updateBookingList.size(); i++) {
                statement.setInt(1, updateBookingList.get(i).getBookingOwnerId());
                statement.setInt(2, updateBookingList.get(i).getRoomNo());
                statement.setString(3, updateBookingList.get(i).getAgency());
                statement.setDate(4, new java.sql.Date(updateBookingList.get(i).getCheckInDate().getTime()));
                statement.setDate(5, new java.sql.Date(updateBookingList.get(i).getCheckOutDate().getTime()));
                statement.setInt(6, updateBookingList.get(i).getBookingId());
                rowsUpdated += statement.executeUpdate(); //rowsInserted bliver = updateBookingList.size(), hvis Update går igennem
                log(updateBookingList.get(i).getBookingId(), ActionType.UPDATE, con);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - updateBookingDB");
            System.out.println(e.getMessage());
        }
        finally // Skal køres efter catch
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - updateBookingDB");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == updateBookingList.size(); //hvis dette passer returneres true ellers false  
    }

    public ArrayList<Booking> getGuestBookingsFromDB(Guest guest, Connection con) {
        Booking booking = null;
        ArrayList<Booking> tempBookingList = new ArrayList<>();
        String SQLString = "select * from bookings b "
                + "join BOOKING_DETAILS rg on rg.booking_id = b.booking_id "
                + "where guest_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, guest.getGuestId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                booking = new Booking(rs);
                tempBookingList.add(booking);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getGuestBookingsFromDB");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getGuestBookingsFromDB");
                System.out.println(e.getMessage());
            }
        }
        return tempBookingList;
    }

    public boolean deleteBookingFromDB(ArrayList<Integer> deleteBookingsList, Connection con) {
        
        int rowsDeleted = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString1 = "delete from BOOKING_DETAILS" // først slettes evt gæster på bookingen
                + " where booking_id = ?";
        String SQLString2 = "delete from BOOKINGS" // herefter slettes selve bookingen
                + " where booking_id = ?";
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        try {
            statement1 = con.prepareStatement(SQLString1);
            statement2 = con.prepareStatement(SQLString2);
            for (int i = 0; i < deleteBookingsList.size(); i++) {
                log(deleteBookingsList.get(i), ActionType.DELETE, con);
                statement1.setInt(1, deleteBookingsList.get(i));
                statement2.setInt(1, deleteBookingsList.get(i));
                statement1.executeUpdate(); //rowsInserted bliver = updateBookingList.size(), hvis Update går igennem
                rowsDeleted += statement2.executeUpdate(); //rowsInserted bliver = updateBookingList.size(), hvis Update går igennem
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - deleteBookingFromDB");
            System.out.println(e.getMessage());
        }
        finally // Skal køres efter catch
        {
            try {
                statement1.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - deleteBookingFromDB");
                System.out.println(e.getMessage());
            }
        }
        return rowsDeleted == deleteBookingsList.size(); //hvis dette passer returneres true ellers false  
    }
    
    public enum ActionType {

        CREATE(1), UPDATE(2), DELETE(3), ADDED_GUEST(4);
        private int actionType;

        private ActionType(int state) {
            this.actionType = state;
        }
    }

    public static void log(int booking_id, ActionType action, Connection con) {
        
        String SQLString = "INSERT INTO BOOKING_LOG (Id, Action, Booking_Id, Logdate, Content) " //laver ny log for ændret booking
                + "SELECT BOOKING_LOG_ID_SEQ.Nextval, ?, ?, CURRENT_TIMESTAMP(3), SYS.Dbms_Xmlgen.Getxml('SELECT" //vælger alle bookings med en unik ID og indsætter actiontype og bookingID på '?'
                    + "(SELECT LISTAGG(CONCAT(CONCAT(g.FIRST_NAME, '' ''), g.LAST_NAME), '','') WITHIN GROUP (ORDER BY 1) AS Fullname " //henter de gæster som bor på bookingen med fornavn og efternavn aggregeret som en kolonne og gruppere dem som Fullname
                    + "FROM GUESTS g JOIN BOOKING_DETAILS bd ON bd.GUEST_ID = g.GUEST_ID AND bd.BOOKING_ID = " + booking_id + ") AS GUESTS, "
                + "* FROM Bookings WHERE booking_id = " + booking_id + "') xmlstr FROM Dual"; //Vi henter data som xml da det kan bruges af mange programmer og bruges i mange sammenhænge
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, action.actionType);
            statement.setInt(2, booking_id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - log");
            System.out.println(e.getMessage());
        }
        finally // Skal lukke statement
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - log");
                System.out.println(e.getMessage());
            }
        }
    }
    
     
    public ArrayList<String> getLog(Connection con) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        String SQLString = "select * from booking_log"
                + " order by id";
           PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                stringArrayList.add(""+rs.getInt("id")+rs.getInt("booking_id")+rs.getDate("logdate")+rs.getInt("action")+rs.getString("content")+"\n");
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getLog");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getLog");
                System.out.println(e.getMessage());
            }
        }
        return stringArrayList;
    }
}
