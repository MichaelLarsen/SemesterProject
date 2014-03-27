/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Booking;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class BookingMapper {

    public ArrayList<Booking> getBookingsFromDB(Connection con) {
        Booking booking = null;
        ArrayList<Booking> bookingList = new ArrayList<>();
        String SQLString = "select * from BOOKINGS";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                booking = new Booking(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getInt(6));
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
                statement.setInt(1, newBookingList.get(i).getBookingId());
                statement.setInt(2, newBookingList.get(i).getBookingOwner());
                statement.setInt(3, newBookingList.get(i).getRoomNo());
                statement.setString(4, newBookingList.get(i).getAgency());
                statement.setDate(5, newBookingList.get(i).getCheckInDate());
                statement.setInt(6, newBookingList.get(i).getNumberOfNights());
                bookingAdded = statement.executeUpdate(); //bookingAdded bliver = 1, hvis Update går igennem
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
}
