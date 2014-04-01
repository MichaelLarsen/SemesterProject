/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Booking;
import domain.Customer;
import domain.Room;
import java.sql.Connection;
import java.util.Date;
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
        String SQLString = "select * from BOOKINGS";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt(2);
                int roomNo = rs.getInt(3);

                booking = new Booking(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getDate(6));
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
                statement.setInt(2, newBookingList.get(i).getBookingOwner().getCustomerId());
                statement.setInt(3, newBookingList.get(i).getRoom().getRoomNo());
                statement.setString(4, newBookingList.get(i).getAgency());
                statement.setDate(5, new java.sql.Date(newBookingList.get(i).getCheckInDate().getTime()));
                statement.setDate(6, new java.sql.Date(newBookingList.get(i).getCheckOutDate().getTime()));
                bookingAdded += statement.executeUpdate(); //bookingAdded bliver = newBookingList.size(), hvis Update går igennem
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

    public ArrayList<Customer> getGuestsInRoom(Booking booking, Connection con) {
        ArrayList<Customer> roomGuestList = new ArrayList<>();
        Customer customer = null;
        String SQLString = "SELECT * "
                + "FROM CUSTOMERS "
                + "JOIN ROOM_GUESTS "
                + "ON CUSTOMERS.customer_id = ROOM_GUESTS.customer_id "
                + "WHERE booking_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, booking.getBookingId());

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
                roomGuestList.add(customer);
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
                statement.setInt(1, updateBookingList.get(i).getBookingOwner().getCustomerId());
                statement.setInt(2, updateBookingList.get(i).getRoom().getRoomNo());
                statement.setString(3, updateBookingList.get(i).getAgency());
                statement.setDate(4, new java.sql.Date(updateBookingList.get(i).getCheckInDate().getTime()));
                statement.setDate(5, new java.sql.Date(updateBookingList.get(i).getCheckOutDate().getTime()));
                statement.setInt(6, updateBookingList.get(i).getBookingId());
            }

            rowsUpdated += statement.executeUpdate(); //rowsInserted bliver = updateBookingList.size(), hvis Update går igennem
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

//    private Customer getCustomerForBookingDB(int customerId, Connection con) {
//        Customer customer = null;
//        String SQLString = "select * from CUSTOMERS where customer_id = " + customerId;
//        PreparedStatement statement = null;
//        try {
//            statement = con.prepareStatement(SQLString);
//            ResultSet rs = statement.executeQuery();
//            if (rs.next()) {
//                customer = new Customer(
//                        rs.getInt(1),
//                        rs.getString(2),
//                        rs.getString(3),
//                        rs.getString(4),
//                        rs.getString(5),
//                        rs.getString(6),
//                        rs.getString(7),
//                        rs.getString(8),
//                        rs.getInt(9),
//                        rs.getInt(10));
//            }
//        }
//        catch (SQLException e) {
//            System.out.println("Fail in BookingMapper - getCustomerForBookingDB");
//            System.out.println(e.getMessage());
//        }
//        finally // must close statement
//        {
//            try {
//                statement.close();
//            }
//            catch (SQLException e) {
//                System.out.println("Fail in BookingMapper - getCustomerForBookingDB");
//                System.out.println(e.getMessage());
//            }
//        }
//        return customer;
//    }

//    private Room getRoomForBookingDB(int roomNo, Connection con) {
//        Room room = null;
//        String SQLString = "select * from ROOMS where room_no = " + roomNo;
//        PreparedStatement statement = null;
//        try {
//            statement = con.prepareStatement(SQLString);
//            ResultSet rs = statement.executeQuery();
//            if (rs.next()) {
//                room = new Room(
//                        rs.getInt(1),
//                        rs.getString(2),
//                        rs.getInt(3));
//            }
//        }
//        catch (SQLException e) {
//            System.out.println("Fail in BookingMapper - getRoomForBookingDB");
//            System.out.println(e.getMessage());
//        }
//        finally // must close statement
//        {
//            try {
//                statement.close();
//            }
//            catch (SQLException e) {
//                System.out.println("Fail in BookingMapper - getRoomForBookingDB");
//                System.out.println(e.getMessage());
//            }
//        }
//        return room;
//    }
}
