/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class DBFacade {

    private CustomerMapper customerMapper;
    private RoomMapper roomMapper;
    private BookingMapper bookingMapper;
    private Connection con;

    // Singleton for at sikre at der kun er en forbindelse samt at give global adgang til domænet.
    private static DBFacade instance;
    private UnitOfWork unitOfWork;

    private DBFacade() {
        unitOfWork = new UnitOfWork();
        customerMapper = new CustomerMapper();
        roomMapper = new RoomMapper();
        bookingMapper = new BookingMapper();
        con = null;
    }

    public static DBFacade getInstance() {
        if (instance == null) {
            instance = new DBFacade();
        }
        return instance;
    }

    private Connection openConnection() {
        if (con == null) {
            try {
                con = new DBConnector().getConnection();
            }
            catch (Exception e) {
                System.out.println("Fail in establishing connection - DBFacade");
                System.out.println(e.getMessage());
            }
        }
        return con;
    }

    private void closeConnection(Connection con) {
        try {
            con.close();
        }
        catch (SQLException e) {
            System.out.println("Fail in closing connection - DBFacade");
            System.out.println(e.getMessage());
        }
    }

    //returns arraylist of rooms
    public ArrayList<Room> getRoomFromDB() {
        con = null;
        ArrayList<Room> tempRoomList;
        try {
            con = openConnection();
            tempRoomList = roomMapper.getRoomsFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempRoomList;
    }

    //returns arraylist of customers
    public ArrayList<Customer> getCustomersFromDB() {
        con = null;
        ArrayList<Customer> tempCustomerList;
        try {
            con = openConnection();
            tempCustomerList = customerMapper.getCustomersFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempCustomerList;
    }

    //returns arraylist of bookings
    public ArrayList<Booking> getBookingsFromDB() {
        con = null;
        ArrayList<Booking> tempBookingList;
        try {
            con = openConnection();
            tempBookingList = bookingMapper.getBookingsFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempBookingList;
    }

    public int getNewBookingId() {
        con = null;
        int newBookingId;
        try {
            con = openConnection();
            newBookingId = bookingMapper.getNewBookingId(con);
        }
        finally {
            closeConnection(con);
        }
        return newBookingId;
    }

    public ArrayList<Customer> getGuestsInRoom(Booking booking) {
        con = null;
        ArrayList<Customer> roomGuestList;
        try {
            con = openConnection();
            roomGuestList = bookingMapper.getGuestsInBooking(booking, con);
        }
        finally {
            closeConnection(con);
        }
        return roomGuestList;
    }

    //Gemmer en transaction i database
    public boolean commitTransaction() throws SQLException {
        boolean commitSuccess;
        con = null;
        try {
            con = openConnection();
            commitSuccess = unitOfWork.commitTransaction(con);
        }
        finally {
            closeConnection(con);
        }
        return commitSuccess;
    }
    
    public boolean updateRoomDB(Room room) {
        return unitOfWork.updateRoomDB(room);
    }

    public boolean updateBookingDB(Booking booking) {
        return unitOfWork.updateBookingDB(booking);
    }

    public boolean updateGuestsInRoomDB(RoomGuest roomGuest) {
        return unitOfWork.updateGuestsInRoomDB(roomGuest);
    }

    public boolean bookRoom(Booking booking) {
        return unitOfWork.bookRoom(booking);
    }

    public boolean addGuestToRoom(RoomGuest roomguest) {
        return unitOfWork.addGuestToRoom(roomguest);
    }

}
